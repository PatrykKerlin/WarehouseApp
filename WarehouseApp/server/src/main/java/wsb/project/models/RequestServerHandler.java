package wsb.project.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import wsb.project.controllers.Mediator;
import wsb.project.helpers.ServerLogMessages;

import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.Lock;

/**
 * Klasa RequestServerHandler odpowiedzialna za obsługę żądań od klientów dotyczących operacji na elementach.
 * Służy jako most między klientem a serwerem, umożliwiając wykonywanie operacji CRUD (Create, Read, Update, Delete)
 * na liście elementów.
 */
public class RequestServerHandler {
	
	private final Mediator mediator;
	private final List<Item> itemList;
	private final Map<Integer, Integer> itemIDMap;
	private final Set<String> itemIndexSet;
	private final ObjectMapper objectMapper;
	private final Lock lock;
	private final Exception generalException, notFoundException, versionException, itemExistsException;
	private final Random random;
	private BufferedWriter outputStream;
	
	/**
	 * Konstruktor inicjujący obiekt obsługujący żądania klienta.
	 *
	 * @param mediator     obiekt mediatora, który służy do komunikacji między komponentami.
	 * @param lock         obiekt Lock służący do blokady w celu zapewnienia bezpieczeństwa wątków.
	 * @param itemList     lista elementów do obsługi.
	 * @param itemIDMap    mapa identyfikatorów elementów i ich indeksów w liście.
	 * @param itemIndexSet zbiór indeksów elementów.
	 */
	public RequestServerHandler(Mediator mediator, Lock lock, List<Item> itemList, Map<Integer, Integer> itemIDMap,
			Set<String> itemIndexSet) {
		
		this.mediator = mediator;
		this.itemList = itemList;
		this.itemIDMap = itemIDMap;
		this.itemIndexSet = itemIndexSet;
		this.objectMapper = new ObjectMapper();
		this.lock = lock;
		generalException = new Exception("Server could not resolve properly current request!");
		notFoundException = new Exception("Server could not find given Item in the database!");
		versionException = new Exception("Given item already has been changed!");
		itemExistsException = new Exception("Item with provided Index already exists!");
		random = new Random();
		
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}
	
	/**
	 * Metoda obsługująca żądanie od klienta, analizując zawartość JSON i wywołując odpowiednie metody do wykonania akcji.
	 *
	 * @param outputStream strumień wyjściowy, do którego zostanie wysłana odpowiedź.
	 * @param json         ciąg JSON zawierający żądanie od klienta.
	 */
	public void handleRequest(BufferedWriter outputStream, String json) {
		
		this.outputStream = outputStream;
		
		try {
			JsonNode jsonNode = objectMapper.readTree(json);
			if (jsonNode.has("method")) {
				String method = jsonNode.get("method").asText();
				if (Objects.equals(method, "get")) {
					if (getAllRecords()) {
						mediator.notify(this, ServerLogMessages.GET_REQUEST);
					} else {
						mediator.notify(generalException);
					}
				} else if (jsonNode.has("item")) {
					Item item = objectMapper.treeToValue(jsonNode.get("item"), Item.class);
					if (Objects.equals(method, "put")) {
						if (addOrUpdateRecord(item) && getAllRecords()) {
							mediator.notify(this, ServerLogMessages.PUT_REQUEST);
						} else {
							mediator.notify(generalException);
						}
					} else if (Objects.equals(method, "delete")) {
						if (deleteRecord(item) && getAllRecords()) {
							mediator.notify(this, ServerLogMessages.DELETE_REQUEST);
						} else {
							mediator.notify(generalException);
						}
					}
				}
			}
		} catch (IOException e) {
			mediator.notify(generalException);
		}
	}
	
	/**
	 * Metoda wysyłająca odpowiedź do klienta przez podany strumień wyjściowy.
	 *
	 * @param response ciąg zawierający odpowiedź do wysłania.
	 */
	public void sendResponse(String response) {
		
		try {
			writeToOutputStream(response);
		} catch (Exception e) {
			writeToOutputStream(generalException.getMessage());
		}
	}
	
	/**
	 * Metoda zapisująca odpowiedź do strumienia wyjściowego i obsługująca wyjątki,
	 * które mogą wystąpić podczas zapisu do strumienia.
	 *
	 * @param response ciąg zawierający odpowiedź do wysłania.
	 */
	private void writeToOutputStream(String response) {
		
		try {
			Thread.sleep(random.nextInt(500, 3000));
			outputStream.write(response);
			outputStream.newLine();
			outputStream.flush();
		} catch (Exception e) {
			mediator.notify(generalException);
			e.printStackTrace();
		}
	}
	
	/**
	 * Metoda pobierająca wszystkie rekordy z listy elementów i wysyłająca je do klienta w formacie JSON.
	 *
	 * @return wartość logiczna wskazująca, czy operacja się powiodła.
	 */
	private boolean getAllRecords() {
		
		try {
			sendResponse(objectMapper.writeValueAsString(itemList));
			return true;
		} catch (IOException e) {
			mediator.notify(generalException);
			sendResponse(generalException.getMessage());
		}
		return false;
	}
	
	/**
	 * Metoda dodająca nowy rekord lub aktualizująca istniejący rekord na liście elementów.
	 *
	 * @param newItem obiekt elementu, który ma zostać dodany lub zaktualizowany.
	 * @return wartość logiczna wskazująca, czy operacja się powiodła.
	 */
	private boolean addOrUpdateRecord(Item newItem) {
		
		boolean success = false;
		try {
			lock.lock();
			int nextID;
			if (itemList.size() == 0) {
				nextID = 1;
			} else {
				nextID = itemList.get(itemList.size() - 1).getId() + 1;
			}
			Integer itemListIndex = getListIndexFromMap(newItem.getId());
			LocalDateTime timeNow = LocalDateTime.now();
			boolean indexExists = itemIndexSet.contains(newItem.getIndex().trim().toLowerCase());
			
			if (itemListIndex != null) {
				Item existingItem = itemList.get(itemListIndex);
				if (!indexExists || newItem.getIndex().trim().equals(existingItem.getIndex())) {
					if (newItem.getVersion().equals(existingItem.getVersion())) {
						existingItem.setVersion(existingItem.getVersion() + 1);
						if (!indexExists) {
							existingItem.setIndex(newItem.getIndex().trim());
						}
						existingItem.setDescription(newItem.getDescription());
						existingItem.setPrice(newItem.getPrice());
						existingItem.setQuantity(newItem.getQuantity());
						existingItem.setModified(timeNow);
						if (!indexExists) {
							itemIndexSet.add(newItem.getIndex());
						}
						success = true;
					} else {
						mediator.notify(versionException);
						sendResponse(versionException.getMessage());
					}
				} else {
					mediator.notify(itemExistsException);
					sendResponse(itemExistsException.getMessage());
				}
			} else {
				if (!indexExists) {
					newItem.setId(nextID);
					newItem.setVersion(1);
					newItem.setCreated(timeNow);
					newItem.setModified(timeNow);
					itemList.add(newItem);
					itemIDMap.put(newItem.getId(), itemList.size() - 1);
					itemIndexSet.add(newItem.getIndex());
					success = true;
				} else {
					mediator.notify(itemExistsException);
					sendResponse(itemExistsException.getMessage());
				}
			}
		} finally {
			lock.unlock();
		}
		return success;
	}
	
	/**
	 * Metoda usuwająca rekord z listy elementów.
	 *
	 * @param item obiekt elementu do usunięcia.
	 * @return wartość logiczna wskazująca, czy operacja się powiodła.
	 */
	private boolean deleteRecord(Item item) {
		
		boolean success = false;
		try {
			lock.lock();
			Integer itemListIndex = getListIndexFromMap(item.getId());
			if (itemListIndex != null) {
				Item itemToDelete = itemList.get(itemListIndex);
				if (item.getVersion().equals(itemToDelete.getVersion())) {
					itemList.remove(itemToDelete);
					itemIDMap.remove(itemToDelete.getId());
					itemIndexSet.remove(itemToDelete.getIndex());
					itemIDMap.clear();
					for (int i = 0; i < itemList.size(); i++) {
						itemIDMap.put(itemList.get(i).getId(), i);
					}
					success = true;
				} else {
					mediator.notify(versionException);
					sendResponse(versionException.getMessage());
				}
			} else {
				mediator.notify(notFoundException);
				sendResponse(notFoundException.getMessage());
			}
		} finally {
			lock.unlock();
		}
		
		return success;
	}
	
	/**
	 * Metoda pobierająca indeks elementu na liście na podstawie jego identyfikatora za pomocą mapy.
	 *
	 * @param id identyfikator elementu.
	 * @return indeks elementu na liście lub null, jeśli element nie istnieje.
	 */
	private Integer getListIndexFromMap(Integer id) {
		
		Integer index = itemIDMap.get(id);
		if (index != null && index < itemList.size()) {
			return index;
		}
		return null;
	}
}
