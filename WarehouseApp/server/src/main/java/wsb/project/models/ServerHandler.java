package wsb.project.models;

import wsb.project.controllers.Mediator;
import wsb.project.helpers.Constants;
import wsb.project.helpers.ServerLogMessages;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Klasa odpowiedzialna za zarządzanie serwerem. Przyjmuje połączenia od klientów i deleguje ich obsługę do innego wątku.
 */
public class ServerHandler {
	
	private final AsyncHandler asyncHandler;
	private final Mediator mediator;
	private final AtomicBoolean serverRunning;
	private final Exception serverStartException;
	private final Exception serverStopException;
	private ServerSocket serverSocket;
	private Socket socket;
	
	/**
	 * Konstruktor inicjalizujący obiekt zarządzający serwerem.
	 *
	 * @param mediator     Obiekt odpowiedzialny za komunikację między komponentami.
	 * @param asyncHandler Obiekt odpowiedzialny za asynchroniczne przetwarzanie żądań klienta.
	 */
	public ServerHandler(Mediator mediator, AsyncHandler asyncHandler) {
		
		this.asyncHandler = asyncHandler;
		this.mediator = mediator;
		this.serverRunning = new AtomicBoolean(false);
		serverStartException = new Exception("Server could not start properly!");
		serverStopException = new Exception("Server could not stop properly!");
		
		try {
			serverSocket = new ServerSocket(Constants.SERVER_PORT);
		} catch (IOException e) {
			stopServer();
		}
	}
	
	/**
	 * Metoda służąca do uruchamiania serwera, który nasłuchuje na połączenia od klientów.
	 */
	public void startServer() {
		
		if (serverRunning.compareAndSet(false, true)) {
			new Thread(() -> {
				try {
					mediator.notify(this, ServerLogMessages.SERVER_START);
					while (serverRunning.get()) {
						socket = serverSocket.accept();
						if (serverRunning.get()) {
							asyncHandler.addClient(socket);
						}
					}
				} catch (Exception e) {
					try {
						socket.close();
					} catch (IOException ex) {
						mediator.notify(serverStartException);
					}
					mediator.notify(serverStartException);
				}
			}).start();
		}
	}
	
	/**
	 * Metoda służąca do zatrzymywania serwera.
	 */
	public void stopServer() {
		
		if (serverRunning.compareAndSet(true, false)) {
			new Thread(() -> {
				try {
					Socket closingSocket = new Socket(serverSocket.getInetAddress(), Constants.SERVER_PORT);
					closingSocket.close();
				} catch (IOException e) {
					mediator.notify(serverStopException);
				} finally {
					try {
						serverSocket.close();
					} catch (IOException e) {
						mediator.notify(serverStopException);
					}
				}
			}).start();
		}
	}
}
