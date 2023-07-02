package wsb.project.views;

import wsb.project.controllers.Mediator;
import wsb.project.helpers.ComponentActions;
import wsb.project.helpers.ComponentWithConstraints;
import wsb.project.helpers.Helpers;
import wsb.project.models.Item;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

/**
 * Klasa reprezentująca niestandardowe okno dialogowe,
 * umożliwiająca wyświetlenie okna z różnymi elementami i akcjami.
 */
public class JDialogClient extends JDialog {
	
	private final Mediator mediator;
	private final JLabel indexLabel, descriptionLabel, priceLabel, quantityLabel;
	private final Item item;
	private final DateTimeFormatter timeFormat;
	private Map<ComponentActions, JButton> jButtonMap;
	private ComponentActions[] actions;
	private JLabel titleLabel;
	private JTextField idTextField, indexTextField, descriptionTextField, priceTextField, quantityTextField;
	private JPanel jPanel;
	
	/**
	 * Konstruktor odpowiedzialny za inicjalizację niestandardowego okna dialogowego.
	 *
	 * @param mediator Obiekt mediatora do obsługi powiadomień i komunikacji.
	 * @param owner    Ramka będąca właścicielem tego okna dialogowego.
	 * @param title    Tytuł okna dialogowego.
	 * @param caller   Przycisk, który wywołał okno dialogowe.
	 * @param item     Przedmiot, na którym będą wykonywane operacje.
	 */
	public JDialogClient(Mediator mediator, Frame owner, String title, AbstractButton caller, Item item) {
		
		super(owner, title);
		
		this.mediator = mediator;
		this.item = item;
		timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS", Locale.getDefault());
		
		indexLabel = new JLabel("Index:");
		descriptionLabel = new JLabel("Description:");
		priceLabel = new JLabel("Price:");
		quantityLabel = new JLabel("Quantity:");
		
		if (caller.getActionCommand().equals(ComponentActions.DETAILS.getAction())) {
			showDetailsWindow();
		} else if (caller.getActionCommand().equals(ComponentActions.ADD.getAction())) {
			showAddWindow();
		}
		
		setResizable(false);
		setLocationRelativeTo(null);
		add(jPanel);
		pack();
	}
	
	/**
	 * Metoda wyświetlająca okno dialogowe ze szczegółami wybranego rekordu.
	 */
	private void showDetailsWindow() {
		
		actions = new ComponentActions[]{
				ComponentActions.EDIT, ComponentActions.CANCEL, ComponentActions.SAVE
		};
		jButtonMap = new JButtonContainerClient(mediator, actions).getjButtonMap();
		jButtonMap.get(ComponentActions.SAVE).setVisible(false);
		jPanel = new JPanelClient(detailsWindowComponents());
	}
	
	/**
	 * Metoda wyświetlająca okno dialogowe do dodawania nowego rekordu.
	 */
	private void showAddWindow() {
		
		actions = new ComponentActions[]{ComponentActions.CANCEL, ComponentActions.SAVE};
		jButtonMap = new JButtonContainerClient(mediator, actions).getjButtonMap();
		jPanel = new JPanelClient(addWindowComponents());
	}
	
	/**
	 * Metoda tworząca i zwracająca tablicę komponentów do okna dialogowego dodawania nowego rekordu.
	 *
	 * @return Tablica komponentów z parametrami do dodania do okna dialogowego.
	 */
	private ComponentWithConstraints[] addWindowComponents() {
		
		titleLabel = new JLabel("Add new record:");
		
		idTextField = new JTextFieldClient(15, "-1", false, null, null);
		indexTextField = new JTextFieldClient(15, "", true, null, null);
		descriptionTextField = new JTextFieldClient(15, "", true, null, null);
		priceTextField = new JTextFieldClient(15, "", true, null, null);
		quantityTextField = new JTextFieldClient(15, "", true, null, null);
		
		return new ComponentWithConstraints[]{
				new ComponentWithConstraints(titleLabel, Helpers.setConstraints(0, 0, 2, 1)),
				new ComponentWithConstraints(indexLabel, Helpers.setConstraints(0, 1, 1, 1)),
				new ComponentWithConstraints(descriptionLabel, Helpers.setConstraints(0, 2, 1, 1)),
				new ComponentWithConstraints(quantityLabel, Helpers.setConstraints(0, 3, 1, 1)),
				new ComponentWithConstraints(priceLabel, Helpers.setConstraints(0, 4, 1, 1)),
				
				new ComponentWithConstraints(indexTextField, Helpers.setConstraints(1, 1, 1, 1)),
				new ComponentWithConstraints(descriptionTextField, Helpers.setConstraints(1, 2, 1, 1)),
				new ComponentWithConstraints(quantityTextField, Helpers.setConstraints(1, 3, 1, 1)),
				new ComponentWithConstraints(priceTextField, Helpers.setConstraints(1, 4, 1, 1)),
				
				new ComponentWithConstraints(jButtonMap.get(ComponentActions.CANCEL),
						Helpers.setConstraints(0, 5, 1, 1)),
				new ComponentWithConstraints(jButtonMap.get(ComponentActions.SAVE), Helpers.setConstraints(1, 5, 1, 1))
		};
	}
	
	/**
	 * Metoda tworząca i zwracająca tablicę komponentów do okna dialogowego wyświetlającego szczegóły rekordu.
	 *
	 * @return Tablica komponentów z parametrami do dodania do okna dialogowego.
	 */
	private ComponentWithConstraints[] detailsWindowComponents() {
		
		titleLabel = new JLabel("Record details:");
		JLabel idLabel = new JLabel("ID:");
		JLabel modifiedLabel = new JLabel("Modified:");
		JLabel createdLabel = new JLabel("Created:");
		
		idTextField = new JTextFieldClient(15, item.getId().toString(), false, null, null);
		indexTextField = new JTextFieldClient(15, item.getIndex(), false, null, null);
		descriptionTextField = new JTextFieldClient(15, item.getDescription(), false, null, null);
		priceTextField = new JTextFieldClient(15, item.getPrice().toString(), false, null, null);
		quantityTextField = new JTextFieldClient(15, item.getQuantity().toString(), false, null, null);
		JTextField modifiedTextField =
				new JTextFieldClient(15, item.getModified().format(timeFormat), false, null, null);
		JTextField createdTextField = new JTextFieldClient(15, item.getCreated().format(timeFormat), false, null, null);
		
		return new ComponentWithConstraints[]{
				new ComponentWithConstraints(titleLabel, Helpers.setConstraints(0, 0, 2, 1)),
				new ComponentWithConstraints(idLabel, Helpers.setConstraints(0, 1, 1, 1)),
				new ComponentWithConstraints(indexLabel, Helpers.setConstraints(0, 2, 1, 1)),
				new ComponentWithConstraints(descriptionLabel, Helpers.setConstraints(0, 3, 1, 1)),
				new ComponentWithConstraints(quantityLabel, Helpers.setConstraints(0, 4, 1, 1)),
				new ComponentWithConstraints(priceLabel, Helpers.setConstraints(0, 5, 1, 1)),
				new ComponentWithConstraints(modifiedLabel, Helpers.setConstraints(0, 6, 1, 1)),
				new ComponentWithConstraints(createdLabel, Helpers.setConstraints(0, 7, 1, 1)),
				
				new ComponentWithConstraints(idTextField, Helpers.setConstraints(1, 1, 1, 1)),
				new ComponentWithConstraints(indexTextField, Helpers.setConstraints(1, 2, 1, 1)),
				new ComponentWithConstraints(descriptionTextField, Helpers.setConstraints(1, 3, 1, 1)),
				new ComponentWithConstraints(quantityTextField, Helpers.setConstraints(1, 4, 1, 1)),
				new ComponentWithConstraints(priceTextField, Helpers.setConstraints(1, 5, 1, 1)),
				new ComponentWithConstraints(modifiedTextField, Helpers.setConstraints(1, 6, 1, 1)),
				new ComponentWithConstraints(createdTextField, Helpers.setConstraints(1, 7, 1, 1)),
				
				new ComponentWithConstraints(jButtonMap.get(ComponentActions.CANCEL),
						Helpers.setConstraints(0, 8, 1, 1)),
				new ComponentWithConstraints(jButtonMap.get(ComponentActions.EDIT), Helpers.setConstraints(1, 8, 1, 1)),
				new ComponentWithConstraints(jButtonMap.get(ComponentActions.SAVE), Helpers.setConstraints(1, 8, 1, 1))
		};
	}
	
	/**
	 * Metoda odpowiedzialna za generowanie nowego obiektu Item na podstawie danych wprowadzonych w oknie dialogowym.
	 *
	 * @return Nowy obiekt Item lub null, jeśli wprowadzone dane są niepoprawne.
	 */
	public Item generateNewItem() {
		
		if (indexTextField.getText().isBlank() || descriptionTextField.getText().isBlank() ||
				priceTextField.getText().isBlank() || quantityTextField.getText().isBlank()) {
			return null;
		}
		Integer version = null;
		if (item != null) {
			version = item.getVersion();
		}
		
		try {
			return new Item(Integer.parseInt(idTextField.getText()), version, indexTextField.getText().trim(),
					descriptionTextField.getText().trim(), new BigDecimal(priceTextField.getText().trim()),
					Integer.parseInt(quantityTextField.getText().trim()), null, null);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Price must be a double and quantity must be an integer!",
					"Value error!", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
	
	/**
	 * Metoda zmieniająca okno dialogowe wyświetlające szczegóły rekordu na okno umożliwiające edycję rekordu.
	 */
	public void changeDetailsToEditWindow() {
		
		jButtonMap.get(ComponentActions.EDIT).setVisible(false);
		jButtonMap.get(ComponentActions.SAVE).setVisible(true);
		indexTextField.setEditable(true);
		descriptionTextField.setEditable(true);
		priceTextField.setEditable(true);
		quantityTextField.setEditable(true);
		jPanel.revalidate();
		jPanel.repaint();
	}
	
	/**
	 * Metoda zwracająca mapę powiązań akcji z przyciskami w oknie dialogowym.
	 *
	 * @return Mapa powiązań akcji z przyciskami.
	 */
	public Map<ComponentActions, JButton> getjButtonMap() {
		
		return jButtonMap;
	}
}
