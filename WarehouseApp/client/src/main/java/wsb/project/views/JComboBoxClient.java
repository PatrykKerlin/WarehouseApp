package wsb.project.views;

import wsb.project.controllers.Mediator;
import wsb.project.helpers.ComponentActions;

import javax.swing.*;

/**
 * Klasa rozszerzająca JComboBox, odpowiedzialna za tworzenie rozwijanej listy wyboru z powiązanymi akcjami.
 */
class JComboBoxClient extends JComboBox<String> {
	
	/**
	 * Konstruktor odpowiedzialny za inicjalizację rozwijanej listy wyboru i jej elementów.
	 *
	 * @param mediator Obiekt mediatora do obsługi powiadomień i komunikacji.
	 * @param actions  Tablica akcji, które mają być powiązane z elementami listy.
	 */
	public JComboBoxClient(Mediator mediator, ComponentActions[] actions) {
		
		for (ComponentActions action : actions) {
			addItem(action.getAction());
		}
		setSelectedIndex(0);
		addActionListener(e -> mediator.notify(this));
	}
}
