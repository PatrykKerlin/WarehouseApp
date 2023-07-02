package wsb.project.views;

import wsb.project.controllers.Mediator;
import wsb.project.helpers.ComponentActions;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Klasa odpowiedzialna za tworzenie kontenera przycisków z powiązanymi akcjami.
 */
class JButtonContainerClient {
	
	private final Map<ComponentActions, JButton> jButtonMap = new HashMap<>();
	
	/**
	 * Konstruktor odpowiedzialny za inicjalizację kontenera przycisków i ich mapowania do akcji.
	 *
	 * @param mediator Obiekt mediatora do obsługi powiadomień i komunikacji.
	 * @param actions  Tablica akcji, które mają być powiązane z przyciskami.
	 */
	public JButtonContainerClient(Mediator mediator, ComponentActions[] actions) {
		
		for (ComponentActions action : actions) {
			JButton jButton = new JButton(action.getAction());
			jButton.addActionListener(e -> mediator.notify(jButton));
			jButtonMap.put(action, jButton);
		}
	}
	
	/**
	 * Metoda zwracająca mapę powiązań akcji z przyciskami.
	 *
	 * @return Mapa powiązań akcji z przyciskami.
	 */
	public Map<ComponentActions, JButton> getjButtonMap() {
		
		return jButtonMap;
	}
}
