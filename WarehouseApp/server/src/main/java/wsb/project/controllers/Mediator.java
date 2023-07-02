package wsb.project.controllers;

import wsb.project.helpers.ServerLogMessages;

/**
 * Interfejs definiujący mediatora, który ułatwia komunikację między różnymi komponentami.
 */
public interface Mediator {
	
	/**
	 * Metoda obsługująca powiadomienia od nadawcy z określoną wiadomością.
	 *
	 * @param sender  Nadawca powiadomienia.
	 * @param message Wiadomość, która ma być przekazana.
	 */
	void notify(Object sender, ServerLogMessages message);
	
	/**
	 * Metoda obsługująca powiadomienia o wyjątkach.
	 *
	 * @param exception Wyjątek, który ma być przekazany.
	 */
	void notify(Exception exception);
}
