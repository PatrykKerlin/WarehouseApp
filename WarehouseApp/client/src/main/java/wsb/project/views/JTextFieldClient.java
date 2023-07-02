package wsb.project.views;

import javax.swing.*;
import java.awt.*;

/**
 * Klasa reprezentująca niestandardowe pole tekstowe.
 */
class JTextFieldClient extends JTextField {
	
	/**
	 * Konstruktor przygotowujący pole tekstowe z określonymi właściwościami.
	 *
	 * @param columns    Liczba kolumn w polu tekstowym.
	 * @param text       Tekst wyświetlany w polu.
	 * @param isEditable Czy pole ma być edytowalne.
	 * @param alignment  Wyrównanie tekstu.
	 * @param weight     Waga czcionki.
	 */
	public JTextFieldClient(Integer columns, String text, Boolean isEditable, Integer alignment, Integer weight) {
		
		if (alignment != null) {
			setHorizontalAlignment(alignment);
		}
		if (weight != null) {
			setFont(new Font(this.getFont().getFontName(), weight, this.getFont().getSize()));
		}
		
		setColumns(columns);
		setText(text);
		setEditable(isEditable);
	}
}
