package wsb.project.models;

import wsb.project.controllers.Mediator;
import wsb.project.helpers.ServerLogMessages;

import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Klasa zarządzająca pulą wątków odpowiedzialną za asynchroniczną obsługę połączeń klientów.
 * Umożliwia przetwarzanie wielu żądań jednocześnie bez blokowania głównego wątku serwera.
 */
public class AsyncHandler {
	
	private final ExecutorService threadPool;
	private final Mediator mediator;
	private final List<Item> itemList;
	private final Map<Integer, Integer> itemIDMap;
	private final Set<String> itemIndexSet;
	private final Lock lock;
	
	/**
	 * Konstruktor inicjujący zarządcę asynchronicznych operacji z podanymi parametrami.
	 * Tworzy pulę wątków, która będzie używana do obsługi połączeń od klientów.
	 *
	 * @param mediator     obiekt mediatora, który służy do komunikacji między komponentami.
	 * @param itemList     lista elementów do obsługi.
	 * @param itemIDMap    mapa identyfikatorów elementów i ich indeksów w liście.
	 * @param itemIndexSet zbiór indeksów elementów.
	 */
	public AsyncHandler(Mediator mediator, List<Item> itemList, Map<Integer, Integer> itemIDMap,
			Set<String> itemIndexSet) {
		
		this.mediator = mediator;
		this.itemList = itemList;
		this.itemIDMap = itemIDMap;
		this.itemIndexSet = itemIndexSet;
		this.lock = new ReentrantLock();
		threadPool = Executors.newFixedThreadPool(5);
	}
	
	/**
	 * Metoda przyjmująca nowe połączenie od klienta i dodająca je do puli wątków.
	 * Dzięki temu żądanie klienta jest obsługiwane asynchronicznie.
	 *
	 * @param socket gniazdo dla obsługi połączenia z klientem.
	 */
	public void addClient(Socket socket) {
		
		threadPool.execute(new ClientHandler(mediator, socket, lock, itemList, itemIDMap, itemIndexSet));
		mediator.notify(this, ServerLogMessages.NEW_REQUEST);
	}
	
	/**
	 * Metoda zamykająca pulę wątków, upewniając się, że wszystkie uruchomione zadania zostaną zakończone
	 * przed zamknięciem. Wywoływana przy zamykaniu serwera.
	 */
	public void closeThreadPool() {
		
		boolean allThreadsFinished;
		mediator.notify(this, ServerLogMessages.THREADS_CLOSING);
		try {
			threadPool.shutdown();
			allThreadsFinished = threadPool.awaitTermination(15, TimeUnit.SECONDS);
			if (allThreadsFinished) {
				mediator.notify(this, ServerLogMessages.SERVER_STOP);
			}
		} catch (InterruptedException e) {
			mediator.notify(new Exception("Server could not close all threads!"));
		}
	}
}