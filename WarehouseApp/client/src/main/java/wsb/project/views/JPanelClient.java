package wsb.project.views;

import wsb.project.helpers.ComponentWithConstraints;

import javax.swing.*;
import java.awt.*;

/**
 * Klasa rozszerzająca JPanel, odpowiedzialna za tworzenie niestandardowego panelu z komponentami i ich ograniczeniami.
 */
class JPanelClient extends JPanel {
	
	/**
	 * Konstruktor odpowiedzialny za inicjalizację panelu z ustawionym układem siatki (GridBagLayout)
	 * i dodanie komponentów z ich parametrami.
	 *
	 * @param components Tablica komponentów wraz z ich parametrami, które mają być dodane do panelu.
	 */
	public JPanelClient(ComponentWithConstraints[] components) {
		
		setLayout(new GridBagLayout());
		
		for (ComponentWithConstraints component : components) {
			add(component.component(), component.constraints());
		}
		
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	}
}
