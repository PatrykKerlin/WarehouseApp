package wsb.project.helpers;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Enum definiujący akcje komponentów wraz z ich nazwami i skrótami klawiaturowymi.
 */
public enum ComponentActions {
	
	ABOUT("About author", KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK)),
	REFRESH("Refresh", KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK)),
	DETAILS("Details", KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK)),
	ADD("Add", KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, InputEvent.CTRL_DOWN_MASK)),
	DELETE("Delete", KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, InputEvent.CTRL_DOWN_MASK)),
	EXIT("Exit", KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK)),
	GO("Go", null),
	EDIT("Edit", null),
	CANCEL("Cancel", null),
	SAVE("Save", null),
	ASC("Asc", null),
	DESC("Desc", null),
	ID("ID", null),
	INDEX("Index", null),
	QUANTITY("Quantity", null),
	PRICE("Price", null);
	
	private final String action;
	private final KeyStroke keyStroke;
	
	/**
	 * Konstruktor inicjalizujący akcję i skrót klawiaturowy.
	 *
	 * @param action    Nazwa akcji.
	 * @param keyStroke Skrót klawiaturowy akcji.
	 */
	ComponentActions(String action, KeyStroke keyStroke) {
		
		this.action = action;
		this.keyStroke = keyStroke;
	}
	
	/**
	 * Metoda zwracająca nazwę akcji.
	 *
	 * @return Nazwa akcji.
	 */
	public String getAction() {
		
		return action;
	}
	
	/**
	 * Metoda zwracająca skrót klawiaturowy akcji.
	 *
	 * @return Skrót klawiaturowy akcji.
	 */
	public KeyStroke getKeyStroke() {
		
		return keyStroke;
	}
}
