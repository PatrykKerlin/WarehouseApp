package wsb.project.views;

import wsb.project.controllers.Mediator;
import wsb.project.helpers.ComponentActions;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Klasa rozszerzająca JMenuBar, odpowiedzialna za tworzenie paska menu z powiązanymi akcjami.
 */
class JMenuBarClient extends JMenuBar {
	
	private final Map<ComponentActions, JMenuItem> jMenuItemMap;
	
	/**
	 * Konstruktor odpowiedzialny za inicjalizację paska menu i jego elementów.
	 *
	 * @param mediator Obiekt mediatora do obsługi powiadomień i komunikacji.
	 * @param actions  Tablica akcji, które mają być powiązane z elementami menu.
	 */
	public JMenuBarClient(Mediator mediator, ComponentActions[] actions) {
		
		jMenuItemMap = new HashMap<>();
		
		JMenu infoJMenu = new JMenu("Info");
		JMenuItem aboutJMenuItem = new JMenuItem(ComponentActions.ABOUT.getAction());
		aboutJMenuItem.setAccelerator(ComponentActions.ABOUT.getKeyStroke());
		aboutJMenuItem.addActionListener(e -> mediator.notify(aboutJMenuItem));
		infoJMenu.add(aboutJMenuItem);
		jMenuItemMap.put(ComponentActions.ABOUT, aboutJMenuItem);
		
		JMenu actionsJMenu = new JMenu("Actions");
		
		for (ComponentActions action : actions) {
			JMenuItem jMenuItem = new JMenuItem(action.getAction());
			jMenuItem.setAccelerator(action.getKeyStroke());
			jMenuItem.addActionListener(e -> mediator.notify(jMenuItem));
			if (action == ComponentActions.EXIT) {
				actionsJMenu.addSeparator();
			}
			actionsJMenu.add(jMenuItem);
			jMenuItemMap.put(action, jMenuItem);
		}
		
		add(actionsJMenu);
		add(infoJMenu);
	}
	
	/**
	 * Metoda zwracająca mapę powiązań akcji z elementami menu.
	 *
	 * @return Mapa powiązań akcji z elementami menu.
	 */
	public Map<ComponentActions, JMenuItem> getjMenuItemMap() {
		
		return jMenuItemMap;
	}
}
