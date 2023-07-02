package wsb.project.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import wsb.project.controllers.Mediator;

import javax.swing.*;
import java.io.*;
import java.math.BigDecimal;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Klasa odpowiedzialna za obsługę żądań klienta do serwera, wykorzystując połączenie socketowe.
 */
public class RequestClientHandler {
	
	private final Mediator mediator;
	private final ObjectMapper objectMapper;
	private final List<Item> itemList;
	private final JFrame jFrame;
	private final Exception generalException, socketCloseException;
	private final RequestClientHandler requestClientHandler;
	private Socket socket;
	private String hostAddress;
	
	/**
	 * Konstruktor inicjalizujący obsługę żądań klienta i konfigurujący potrzebne składniki.
	 *
	 * @param mediator Obiekt mediatora do obsługi powiadomień i komunikacji.
	 * @param itemList Lista elementów do przetwarzania.
	 * @param jFrame   Referencja na główne okno aplikacji.
	 */
	public RequestClientHandler(Mediator mediator, List<Item> itemList, JFrame jFrame) {
		
		this.mediator = mediator;
		this.itemList = itemList;
		this.jFrame = jFrame;
		requestClientHandler = this;
		generalException = new Exception("The request could not be handled correctly!");
		socketCloseException = new Exception("The connection could not be closed properly!");
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		hostAddress = "";
		
		setHostAddress();
		setSocket();
		
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				mediator.notify(socketCloseException);
			}
		}
	}
	
	/**
	 * Metoda odpowiedzialna za wysyłanie żądania do serwera w celu listy przedmiotów.
	 */
	public void requestItemListFromServer() {
		
		setSocket();
		
		if (socket != null) {
			String request = buildRequest("get", null);
			sendRequest(request);
		}
	}
	
	/**
	 * Metoda odpowiedzialna za wysyłanie rekordu do serwera w celu jego zapisania.
	 *
	 * @param item Element do zapisania na serwerze.
	 */
	public void putRecordToServer(Item item) {
		
		setSocket();
		
		if (socket != null) {
			String request = buildRequest("put", objectMapper.valueToTree(item));
			sendRequest(request);
		}
	}
	
	/**
	 * Metoda odpowiedzialna za wysłanie żądania do serwera w celu usunięcia rekordu.
	 *
	 * @param item Element do usunięcia z serwera.
	 */
	public void deleteRecordFromServer(Item item) {
		
		setSocket();
		
		if (socket != null) {
			String request = buildRequest("delete", objectMapper.valueToTree(item));
			sendRequest(request);
		}
	}
	
	/**
	 * Metoda pomocnicza odpowiedzialna za budowanie żądania JSON.
	 *
	 * @param method   Metoda HTTP do użycia (np. "get", "put", "delete").
	 * @param itemNode Węzeł JSON zawierający dane elementu.
	 * @return String reprezentujący żądanie JSON.
	 */
	private String buildRequest(String method, JsonNode itemNode) {
		
		try {
			ObjectNode requestJson = objectMapper.createObjectNode();
			requestJson.put("method", method);
			
			if (itemNode != null) {
				requestJson.set("item", itemNode);
			}
			
			return objectMapper.writeValueAsString(requestJson);
		} catch (JsonProcessingException e) {
			mediator.notify(generalException);
		}
		return "";
	}
	
	/**
	 * Metoda odpowiedzialna za wysłanie żądania do serwera i przetworzenie odpowiedzi.
	 *
	 * @param request Żądanie do wysłania do serwera.
	 */
	private void sendRequest(String request) {
		
		SwingWorker<String, Void> worker = new SwingWorker<>() {
			@Override
			protected String doInBackground() {
				
				String response = "";
				try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
						BufferedReader inputStream = new BufferedReader(
								new InputStreamReader(socket.getInputStream()))) {
					
					out.write(request);
					out.newLine();
					out.flush();
					
					mediator.notify(requestClientHandler);
					
					response = inputStream.readLine();
				} catch (IOException e) {
					if (!socket.isClosed()) {
						mediator.notify(generalException);
					}
				}
				return response;
			}
			
			@Override
			protected void done() {
				
				try {
					String response = get();
					if (updateItemList(response)) {
						mediator.notify(requestClientHandler);
						try {
							socket.close();
						} catch (IOException e) {
							mediator.notify(socketCloseException);
						}
					} else {
						mediator.notify(requestClientHandler);
					}
				} catch (Exception e) {
					mediator.notify(generalException);
				}
			}
		};
		
		worker.execute();
	}
	
	/**
	 * Metoda pomocnicza odpowiedzialna za aktualizację listy elementów na podstawie odpowiedzi z serwera.
	 *
	 * @param response Odpowiedź JSON otrzymana od serwera.
	 * @return Wartość logiczna wskazująca, czy lista elementów została pomyślnie zaktualizowana.
	 */
	private boolean updateItemList(String response) {
		
		try {
			if (response != null) {
				JsonNode rootNode = objectMapper.readTree(response);
				if (rootNode.isArray()) {
					itemList.clear();
					for (JsonNode node : rootNode) {
						itemList.add(new Item(node.get("id").asInt(), node.get("version").asInt(),
								node.get("index").asText(), node.get("description").asText(),
								new BigDecimal(node.get("price").asText()), node.get("quantity").asInt(),
								LocalDateTime.parse(node.get("modified").asText()),
								LocalDateTime.parse(node.get("created").asText())));
					}
					return true;
				}
			}
		} catch (IOException e) {
			mediator.notify(new Exception(response));
		}
		return false;
	}
	
	/**
	 * Metoda pomocnicza odpowiedzialna za pobranie od użytkownika i ustawienie adresu hosta.
	 */
	private void setHostAddress() {
		
		hostAddress = (String) JOptionPane.showInputDialog(jFrame, "Enter the host address:", "Host address",
				JOptionPane.QUESTION_MESSAGE, null, null, "localhost");
		if (hostAddress == null) {
			jFrame.dispose();
		}
	}
	
	/**
	 * Metoda pomocnicza odpowiedzialna za nawiązanie połączenia socketowego z serwerem.
	 */
	private void setSocket() {
		
		if (socket != null) {
			if (!socket.isClosed()) {
				try {
					socket.close();
				} catch (IOException e) {
					mediator.notify(socketCloseException);
				}
			}
		}
		
		boolean loopContinue = true;
		while (loopContinue) {
			try {
				socket = new Socket(hostAddress, 3000);
				loopContinue = false;
			} catch (IOException e) {
				if (hostAddress == null) {
					break;
				}
				JOptionPane.showMessageDialog(jFrame, "Server is not responding!", "Connection error!",
						JOptionPane.ERROR_MESSAGE);
				setHostAddress();
			}
		}
	}
}
