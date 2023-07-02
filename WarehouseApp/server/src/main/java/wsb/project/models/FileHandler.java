package wsb.project.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import wsb.project.controllers.Mediator;
import wsb.project.helpers.Constants;
import wsb.project.helpers.ServerLogMessages;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Klasa odpowiedzialna za obsługę plików, w tym odczytywanie i zapisywanie danych do pliku.
 */
public class FileHandler {
	
	private final Mediator mediator;
	private final List<Item> itemList;
	private final Map<Integer, Integer> itemIDMap;
	private final Set<String> itemIndexSet;
	private final ObjectMapper objectMapper;
	private final String fileName;
	
	/**
	 * Konstruktor inicjalizujący obiekt odpowiedzialny za obsługę plików.
	 *
	 * @param mediator     Obiekt odpowiedzialny za komunikację między komponentami.
	 * @param itemList     Lista obiektów typu Item do odczytu lub zapisu.
	 * @param itemIDMap    Mapa przechowująca identyfikatory elementów.
	 * @param itemIndexSet Zbiór indeksów elementów.
	 */
	public FileHandler(Mediator mediator, List<Item> itemList, Map<Integer, Integer> itemIDMap,
			Set<String> itemIndexSet) {
		
		this.mediator = mediator;
		this.itemList = itemList;
		this.itemIDMap = itemIDMap;
		this.itemIndexSet = itemIndexSet;
		this.objectMapper = new ObjectMapper();
		fileName = Constants.DATABASE_JSON_FILE;
		
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}
	
	/**
	 * Metoda odpowiedzialna za odczytywanie danych z pliku.
	 */
	public void readFromFile() {
		
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
			String jsonContent;
			if ((jsonContent = bufferedReader.readLine()) != null) {
				JsonNode rootNode = objectMapper.readTree(jsonContent);
				if (rootNode.isArray()) {
					for (JsonNode node : rootNode) {
						itemList.add(new Item(node.get("id").asInt(), node.get("version").asInt(),
								node.get("index").asText(), node.get("description").asText(),
								new BigDecimal(node.get("price").asText()), node.get("quantity").asInt(),
								LocalDateTime.parse(node.get("modified").asText()),
								LocalDateTime.parse(node.get("created").asText())));
						itemIDMap.put(node.get("id").asInt(), itemList.size() - 1);
						itemIndexSet.add(node.get("index").asText().trim().toLowerCase());
					}
					mediator.notify(this, ServerLogMessages.DATA_READY);
				}
			}
		} catch (IOException e) {
			mediator.notify(new Exception("Server could not read data from the database file!"));
		}
	}
	
	/**
	 * Metoda odpowiedzialna za zapisywanie danych do pliku.
	 */
	public void saveToFile() {
		
		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
			String jsonContent = objectMapper.writeValueAsString(itemList);
			bufferedWriter.write(jsonContent);
			mediator.notify(this, ServerLogMessages.DATA_SAVED);
		} catch (IOException e) {
			mediator.notify(new Exception("Server could not save data to the database file!"));
		}
	}
}

