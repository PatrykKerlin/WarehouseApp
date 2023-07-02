package wsb.project.helpers;

import java.awt.*;

/**
 * Klasa pomocnicza zawierająca metody do ustawiania parametrów dla menadżera układu GridBagLayout.
 */
public class Helpers {
	
	/**
	 * Metoda ustawiająca podstawowe ograniczenia dla menadżera układu GridBagLayout.
	 *
	 * @param x      Położenie w poziomie.
	 * @param y      Położenie w pionie.
	 * @param width  Szerokość komponentu.
	 * @param height Wysokość komponentu.
	 * @return Parametry dla komponentu.
	 */
	public static GridBagConstraints setConstraints(int x, int y, int width, int height) {
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);
		
		return gbc;
	}
	
	/**
	 * Metoda ustawiająca rozszerzone parametry dla menadżera układu GridBagLayout.
	 *
	 * @param x      Położenie w poziomie.
	 * @param y      Położenie w pionie.
	 * @param width  Szerokość komponentu.
	 * @param height Wysokość komponentu.
	 * @param anchor Określa czy i gdzie komponent powinien być zakotwiczony.
	 * @param fill   Określa czy i jak komponent powinien być rozciągnięty.
	 * @return Parametry dla komponentu.
	 */
	public static GridBagConstraints setConstraints(int x, int y, int width, int height, Integer anchor, Integer fill) {
		
		GridBagConstraints gbc = setConstraints(x, y, width, height);
		
		gbc.anchor = anchor != null ? anchor : GridBagConstraints.NORTHWEST;
		gbc.fill = fill != null ? fill : GridBagConstraints.HORIZONTAL;
		
		return gbc;
	}
}
