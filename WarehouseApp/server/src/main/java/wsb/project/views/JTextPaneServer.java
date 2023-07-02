package wsb.project.views;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.*;

/**
 * Klasa rozszerzająca klasę JTextPane.
 * Pozwala na wyświetlanie tekstu w różnych stylach, takich jak normalny tekst oraz tekst reprezentujący wyjątek.
 * Tekst wyjątku jest wyświetlany na czerwono.
 * Klasa ta jest przeznaczona tylko do wyświetlania tekstu i nie pozwala na jego edycję.
 */
public class JTextPaneServer extends JTextPane {
	
	/**
	 * Konstruktor inicjujący obiekt z dwoma stylami tekstu: normalnym i dla wyjątków.
	 * Ustawia również obiekt jako nieedytowalny.
	 */
	public JTextPaneServer() {
		
		Style normalText = addStyle("Normal", null);
		StyleConstants.setForeground(normalText, Color.BLACK);
		Style exceptionText = addStyle("Exception", null);
		StyleConstants.setForeground(exceptionText, Color.RED);
		setEditable(false);
	}
}
