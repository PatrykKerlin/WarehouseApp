package wsb.project.helpers;

/**
 * Enum zawierający stałe wiadomości logowania serwera.
 */
public enum ServerLogMessages {
	SERVER_START("Server is listening..."),
	SERVER_STOP("Server is terminated!"),
	NEW_REQUEST("New request received."),
	THREADS_CLOSING("Closing all threads..."),
	GET_REQUEST("GET request resolved."),
	POST_REQUEST("POST request resolved."),
	PUT_REQUEST("PUT request resolved."),
	DELETE_REQUEST("DELETE request resolved."),
	PREPARING_DATA("Preparing data, please wait..."),
	DATA_READY("Data is ready."),
	DATA_SAVED("Data has been saved successfully.");
	
	private final String message;
	
	/**
	 * Konstruktor inicjalizujący wiadomość logowania.
	 *
	 * @param message Wiadomość do zalogowania.
	 */
	ServerLogMessages(String message) {
		
		this.message = message;
	}
	
	/**
	 * Metoda zwracająca reprezentację tekstową wiadomości logowania.
	 *
	 * @return Wiadomość logowania jako tekst.
	 */
	@Override
	public String toString() {
		
		return message + "\n";
	}
}
