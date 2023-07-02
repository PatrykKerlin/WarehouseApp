package wsb.project.models;

import wsb.project.controllers.Mediator;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;

/**
 * Klasa odpowiedzialna za obsługę połączeń od klientów.
 * Tworząca dedykowany wątek dla każdego klienta, aby umożliwić obsługę wielu żądań jednocześnie.
 */
class ClientHandler implements Runnable {
	
	private final Socket socket;
	private final RequestServerHandler requestServerHandler;
	private final Mediator mediator;
	
	/**
	 * Konstruktor inicjujący obiekt reprezentujący nowego klienta.
	 *
	 * @param mediator     obiekt mediatora, który służy do komunikacji między komponentami.
	 * @param socket       gniazdo dla obsługi połączenia z klientem.
	 * @param lock         obiekt Lock służący do blokady w celu zapewnienia bezpieczeństwa wątków.
	 * @param itemList     lista elementów do obsługi.
	 * @param itemIDMap    mapa identyfikatorów elementów i ich indeksów w liście.
	 * @param itemIndexSet zbiór indeksów elementów.
	 */
	public ClientHandler(Mediator mediator, Socket socket, Lock lock, List<Item> itemList,
			Map<Integer, Integer> itemIDMap, Set<String> itemIndexSet) {
		
		this.mediator = mediator;
		this.socket = socket;
		this.requestServerHandler = new RequestServerHandler(mediator, lock, itemList, itemIDMap, itemIndexSet);
	}
	
	/**
	 * Metoda wywoływana, gdy wątek jest uruchamiany. Odpowiada za obsługę połączenia z klientem i odczytywanie żądań.
	 */
	@Override
	public void run() {
		
		try (BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
			String request;
			if ((request = inputStream.readLine()) != null) {
				requestServerHandler.handleRequest(outputStream, request);
			}
		} catch (Exception e) {
			mediator.notify(new Exception("Server could not read the request body!"));
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				mediator.notify(new Exception("Server could not close the socket!"));
			}
		}
	}
}
