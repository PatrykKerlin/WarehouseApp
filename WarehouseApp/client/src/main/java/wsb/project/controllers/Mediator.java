package wsb.project.controllers;

/**
 * Interfejs służący do obsługi komunikacji między różnymi komponentami.
 */
public interface Mediator {
	
	/**
	 * Metoda obsługująca powiadomienia wysyłane przez różne elementy.
	 *
	 * @param sender Obiekt, który wysyła powiadomienie.
	 */
	void notify(Object sender);
}
