package wsb.project.views;

import wsb.project.controllers.Mediator;
import wsb.project.helpers.ComponentActions;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Klasa rozszerzająca ButtonGroup, odpowiedzialna za tworzenie grupy przycisków radiowych z powiązanymi akcjami.
 */
class ButtonGroupClient extends ButtonGroup {
	
	private final Map<ComponentActions, JRadioButton> jRadioButtonMap;
	
	/**
	 * Konstruktor odpowiedzialny za inicjalizację grupy przycisków radiowych i ich mapowania do akcji.
	 *
	 * @param mediator Obiekt mediatora do obsługi powiadomień i komunikacji.
	 * @param actions  Tablica akcji, które mają być powiązane z przyciskami radiowymi.
	 */
	public ButtonGroupClient(Mediator mediator, ComponentActions[] actions) {
		
		jRadioButtonMap = new HashMap<>();
		
		for (ComponentActions action : actions) {
			JRadioButton jRadioButton = new JRadioButton(action.getAction());
			jRadioButton.addActionListener(e -> mediator.notify(jRadioButton));
			add(jRadioButton);
			jRadioButtonMap.put(action, jRadioButton);
		}
		
		jRadioButtonMap.get(actions[0]).setSelected(true);
	}
	
	/**
	 * Metoda zwracająca mapę powiązań akcji z przyciskami radiowymi.
	 *
	 * @return Mapa powiązań akcji z przyciskami radiowymi.
	 */
	public Map<ComponentActions, JRadioButton> getjRadioButtonMap() {
		
		return jRadioButtonMap;
	}
}
