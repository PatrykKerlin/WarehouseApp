package wsb.project.views;

import wsb.project.controllers.Mediator;
import wsb.project.helpers.ComponentActions;
import wsb.project.helpers.ComponentWithConstraints;
import wsb.project.helpers.Helpers;
import wsb.project.models.Item;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Klasa odpowiedzialna za tworzenie i zarządzanie komponentami głównego okna aplikacji.
 */
public class JFrameClient extends JFrame {
	
	private final List<Item> itemList;
	private final Map<ComponentActions, JButton> jButtonMap;
	private final Map<ComponentActions, JRadioButton> jRadioButtonMap;
	private final Map<ComponentActions, JMenuItem> jMenuItemMap;
	private final JScrollPaneClient jScrollPane;
	private final TableModelClient tableModel;
	private final JComboBoxClient jComboBoxSort, jComboBoxSearch;
	private final JTextField searchJTextField, recordsQuantityJTextField;
	private final JProgressBar jProgressBar;
	
	/**
	 * Konstruktor odpowiedzialny za inicjalizację i konfigurację okna głównego aplikacji.
	 *
	 * @param mediator Obiekt umożliwiający komunikację między komponentami.
	 * @param itemList Lista elementów do wyświetlenia w oknie.
	 */
	public JFrameClient(Mediator mediator, List<Item> itemList) {
		
		super("WarehouseClient");
		
		ComponentActions[] jButtonActions = new ComponentActions[]{
				ComponentActions.REFRESH, ComponentActions.DETAILS, ComponentActions.ADD, ComponentActions.DELETE,
				ComponentActions.GO, ComponentActions.EXIT,
		};
		ComponentActions[] sortActions = new ComponentActions[]{ComponentActions.ASC, ComponentActions.DESC};
		ComponentActions[] sortAndSearchActions = new ComponentActions[]{
				ComponentActions.ID, ComponentActions.INDEX, ComponentActions.QUANTITY, ComponentActions.PRICE
		};
		ComponentActions[] jMenuItemActions = new ComponentActions[]{
				ComponentActions.REFRESH, ComponentActions.DETAILS, ComponentActions.ADD, ComponentActions.DELETE,
				ComponentActions.EXIT,
		};
		
		this.itemList = itemList;
		jButtonMap = new JButtonContainerClient(mediator, jButtonActions).getjButtonMap();
		jRadioButtonMap = new ButtonGroupClient(mediator, sortAndSearchActions).getjRadioButtonMap();
		jComboBoxSort = new JComboBoxClient(mediator, sortActions);
		jComboBoxSearch = new JComboBoxClient(mediator, sortAndSearchActions);
		tableModel = new TableModelClient(mediator, itemList);
		jScrollPane = new JScrollPaneClient(tableModel.getjTable(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		searchJTextField = new JTextFieldClient(10, "", true, null, null);
		recordsQuantityJTextField = new JTextFieldClient(8, "", false, SwingConstants.CENTER, Font.BOLD);
		jProgressBar = new JProgressBarClient();
		JPanel jPanel = new JPanelClient(mainWindowComponents());
		JMenuBarClient jMenuBar = new JMenuBarClient(mediator, jMenuItemActions);
		jMenuItemMap = jMenuBar.getjMenuItemMap();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		add(jPanel);
		setJMenuBar(jMenuBar);
		pack();
		setVisible(true);
	}
	
	/**
	 * Metoda odpowiedzialna za przygotowanie i konfigurację komponentów do wyświetlenia w oknie.
	 *
	 * @return Tablica komponentów wraz z informacjami o ich rozmieszczeniu.
	 */
	private ComponentWithConstraints[] mainWindowComponents() {
		
		return new ComponentWithConstraints[]{
				new ComponentWithConstraints(new JLabel("Order by:"),
						Helpers.setConstraints(0, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE)),
				new ComponentWithConstraints(jRadioButtonMap.get(ComponentActions.ID),
						Helpers.setConstraints(1, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE)),
				new ComponentWithConstraints(jRadioButtonMap.get(ComponentActions.INDEX),
						Helpers.setConstraints(2, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE)),
				new ComponentWithConstraints(jRadioButtonMap.get(ComponentActions.QUANTITY),
						Helpers.setConstraints(3, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE)),
				new ComponentWithConstraints(jRadioButtonMap.get(ComponentActions.PRICE),
						Helpers.setConstraints(4, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE)),
				new ComponentWithConstraints(jComboBoxSort,
						Helpers.setConstraints(5, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE)),
				new ComponentWithConstraints(recordsQuantityJTextField,
						Helpers.setConstraints(6, 0, 2, 1, GridBagConstraints.WEST, null)),
				new ComponentWithConstraints(jScrollPane, Helpers.setConstraints(0, 1, 7, 4)),
				new ComponentWithConstraints(jButtonMap.get(ComponentActions.REFRESH),
						Helpers.setConstraints(7, 1, 1, 1)),
				new ComponentWithConstraints(jButtonMap.get(ComponentActions.DETAILS),
						Helpers.setConstraints(7, 2, 1, 1)),
				new ComponentWithConstraints(jButtonMap.get(ComponentActions.ADD), Helpers.setConstraints(7, 3, 1, 1)),
				new ComponentWithConstraints(jButtonMap.get(ComponentActions.DELETE),
						Helpers.setConstraints(7, 4, 1, 1)),
				new ComponentWithConstraints(jButtonMap.get(ComponentActions.EXIT),
						Helpers.setConstraints(7, 6, 1, 1, GridBagConstraints.SOUTHWEST, null)),
				new ComponentWithConstraints(new JLabel("Search:"),
						Helpers.setConstraints(0, 5, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE)),
				new ComponentWithConstraints(jComboBoxSearch,
						Helpers.setConstraints(1, 5, 2, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL)),
				new ComponentWithConstraints(searchJTextField,
						Helpers.setConstraints(3, 5, 2, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL)),
				new ComponentWithConstraints(jButtonMap.get(ComponentActions.GO),
						Helpers.setConstraints(5, 5, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE)),
				new ComponentWithConstraints(jProgressBar,
						Helpers.setConstraints(0, 6, 7, 1, GridBagConstraints.SOUTH, null))
		};
	}
	
	/**
	 * Metoda pozwalająca na zmianę stanu aktywności składników interfejsu w kontenerze.
	 *
	 * @param container Kontener zawierający składniki.
	 * @param enabled   Wartość określająca, czy składniki mają być aktywne.
	 */
	private void setAllComponentsEnabled(Container container, boolean enabled) {
		
		for (Component component : container.getComponents()) {
			if (!(component instanceof JLabel) && component != jButtonMap.get(ComponentActions.EXIT)) {
				component.setEnabled(enabled);
				if (component instanceof Container) {
					setAllComponentsEnabled((Container) component, enabled);
				}
			}
		}
	}
	
	/**
	 * Metoda resetująca ustawienia sortowania i filtry w głównym oknie.
	 */
	public void refreshMainWindow() {
		
		tableModel.getjTableRowSorter().setRowFilter(null);
		searchJTextField.setText("");
		jRadioButtonMap.get(ComponentActions.ID).setSelected(true);
		jComboBoxSort.setSelectedItem(ComponentActions.ASC.getAction());
		jComboBoxSearch.setSelectedItem(ComponentActions.ID.getAction());
	}
	
	/**
	 * Metoda aktualizująca liczbę wyświetlanych rekordów w oknie głównym.
	 */
	public void refreshRecordsQuantity() {
		
		recordsQuantityJTextField.setText(
				"Records displayed: " + tableModel.getjTableRowSorter().getViewRowCount() + " / " + itemList.size());
	}
	
	/**
	 * Metoda odświeżająca dane wyświetlane w tabeli w oknie głównym.
	 */
	public void refreshJTable() {
		
		tableModel.refreshTable();
	}
	
	/**
	 * Metoda przewijająca tabelę do jej dolnej części.
	 */
	public void scrollToBottomJTable() {
		
		jScrollPane.scrollToBottom();
	}
	
	/**
	 * Metoda zarządzająca paskiem postępu, kontrolująca jego stan.
	 *
	 * @return True, jeśli pasek postępu został zatrzymany, w przeciwnym razie false.
	 */
	public boolean manageProgressBar() {
		
		if (jProgressBar.isIndeterminate()) {
			jProgressBar.setIndeterminate(false);
			setAllComponentsEnabled(this, true);
			return true;
		}
		
		jProgressBar.setIndeterminate(true);
		setAllComponentsEnabled(this, false);
		return false;
	}
	
	/**
	 * Metoda zwracająca komponent odpowiedzialny za wybór kryterium sortowania.
	 *
	 * @return Komponent do wyboru kryterium sortowania.
	 */
	public JComboBoxClient getjComboBoxSort() {
		
		return jComboBoxSort;
	}
	
	/**
	 * Metoda zwracająca komponent odpowiedzialny za wybór kryterium wyszukiwania.
	 *
	 * @return Komponent do wyboru kryterium wyszukiwania.
	 */
	public JComboBoxClient getjComboBoxSearch() {
		
		return jComboBoxSearch;
	}
	
	/**
	 * Metoda zwracająca model tabeli wykorzystywany w oknie głównym.
	 *
	 * @return Model tabeli.
	 */
	public TableModelClient getTableModel() {
		
		return tableModel;
	}
	
	/**
	 * Metoda zwracająca pole tekstowe służące do wprowadzania kryteriów wyszukiwania.
	 *
	 * @return Pole tekstowe do wprowadzania wyszukiwania.
	 */
	public JTextField getSearchJTextField() {
		
		return searchJTextField;
	}
	
	/**
	 * Metoda zwracająca mapę przycisków radiowych używanych do wyboru kryteriów sortowania.
	 *
	 * @return Mapa przycisków radiowych.
	 */
	public Map<ComponentActions, JRadioButton> getjRadioButtonMap() {
		
		return jRadioButtonMap;
	}
	
	/**
	 * Metoda zwracająca mapę elementów menu górnego używanych w oknie głównym.
	 *
	 * @return Mapa elementów menu.
	 */
	public Map<ComponentActions, JMenuItem> getjMenuItemMap() {
		
		return jMenuItemMap;
	}
	
	/**
	 * Metoda zwracająca mapę przycisków używanych w oknie głównym.
	 *
	 * @return Mapa przycisków.
	 */
	public Map<ComponentActions, JButton> getjButtonMap() {
		
		return jButtonMap;
	}
}
