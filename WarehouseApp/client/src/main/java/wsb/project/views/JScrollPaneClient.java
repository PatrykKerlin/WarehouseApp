package wsb.project.views;

import javax.swing.*;
import java.awt.*;

/**
 * Klasa reprezentująca niestandardową przewijaną ramkę do wyświetlania komponentów.
 */
class JScrollPaneClient extends JScrollPane {
	
	private final JScrollBar verticalScrollBar;
	
	/**
	 * Konstruktor inicjalizujący przewijaną ramkę z wybranymi właściwościami.
	 *
	 * @param view      Komponent do wyświetlenia w ramce przewijanej.
	 * @param vsbPolicy Widoczność pionowego paska przewijania.
	 * @param hsbPolicy Widoczność poziomego paska przewijania.
	 */
	public JScrollPaneClient(Component view, int vsbPolicy, int hsbPolicy) {
		
		super(view, vsbPolicy, hsbPolicy);
		setPreferredSize(new Dimension(500, 200));
		verticalScrollBar = getVerticalScrollBar();
	}
	
	/**
	 * Metoda przewijająca zawartość ramki do jej dolnej części.
	 */
	public void scrollToBottom() {
		
		SwingUtilities.invokeLater(() -> verticalScrollBar.setValue(verticalScrollBar.getMaximum()));
	}
}
