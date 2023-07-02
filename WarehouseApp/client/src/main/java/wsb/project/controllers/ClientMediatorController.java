package wsb.project.controllers;

import wsb.project.helpers.ComponentActions;
import wsb.project.models.Item;
import wsb.project.models.RequestClientHandler;
import wsb.project.views.JDialogClient;
import wsb.project.views.JFrameClient;
import wsb.project.views.TableModelClient;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Klasa koordynująca i zarządzająca interakcjami między obiektami w aplikacji.
 */
public class ClientMediatorController implements Mediator {
	
	private final JFrameClient jFrame;
	private final RequestClientHandler requestClientHandler;
	private final List<Item> itemList;
	private final TableModelClient tableModel;
	private final JComboBox<String> jComboBoxSort;
	private final JComboBox<String> jComboBoxSearch;
	private final Map<ComponentActions, JRadioButton> jRadioButtonMap;
	private final Map<ComponentActions, JMenuItem> jMenuItemMap;
	private final Map<ComponentActions, JButton> jButtonMapMain;
	private Map<ComponentActions, JButton> jButtonMapJDialog;
	private JDialogClient jDialog;
	private Integer currentSelectedIndex;
	
	/**
	 * Konstruktor tworzący nowy obiekt ClientMediatorController i inicjujący jego składniki.
	 */
	public ClientMediatorController() {
		
		currentSelectedIndex = null;
		itemList = new ArrayList<>();
		jFrame = new JFrameClient(this, itemList);
		requestClientHandler = new RequestClientHandler(this, itemList, jFrame);
		tableModel = jFrame.getTableModel();
		jComboBoxSort = jFrame.getjComboBoxSort();
		jComboBoxSearch = jFrame.getjComboBoxSearch();
		jRadioButtonMap = jFrame.getjRadioButtonMap();
		jButtonMapMain = jFrame.getjButtonMap();
		jMenuItemMap = jFrame.getjMenuItemMap();
		
		refreshMainWindow();
		sortTable();
	}
	
	/**
	 * Metoda reagująca na powiadomienia od różnych obiektów i wykonująca odpowiednie akcje.
	 *
	 * @param sender Obiekt wysyłający powiadomienie.
	 */
	@Override
	public void notify(Object sender) {
		
		if (sender instanceof JButton) {
			if (sender == jButtonMapMain.get(ComponentActions.REFRESH)) {
				refreshMainWindow();
			} else if (sender == jButtonMapMain.get(ComponentActions.DETAILS)) {
				showDetailsWindow((AbstractButton) sender);
			} else if (sender == jButtonMapMain.get(ComponentActions.ADD)) {
				showAddWindow((AbstractButton) sender);
			} else if (sender == jButtonMapMain.get(ComponentActions.DELETE)) {
				deleteRecord();
			} else if (sender == jButtonMapMain.get(ComponentActions.GO)) {
				filterTable();
			} else if (sender == jButtonMapMain.get(ComponentActions.EXIT)) {
				closeMainWindow();
			} else if (jButtonMapJDialog != null) {
				if (sender == jButtonMapJDialog.get(ComponentActions.SAVE)) {
					saveItem();
				} else if (sender == jButtonMapJDialog.get(ComponentActions.EDIT)) {
					showEditWindow();
				} else if (sender == jButtonMapJDialog.get(ComponentActions.CANCEL)) {
					closeJDialog();
				}
			}
		} else if (sender instanceof JTable) {
			setCurrentSelectedIndex((JTable) sender);
		} else if (sender instanceof JRadioButton) {
			sortTable();
		} else if (sender instanceof JComboBox<?>) {
			sortTable();
		} else if (sender instanceof JMenuItem) {
			if (sender == jMenuItemMap.get(ComponentActions.REFRESH)) {
				refreshMainWindow();
			} else if (sender == jMenuItemMap.get(ComponentActions.DETAILS)) {
				showDetailsWindow((AbstractButton) sender);
			} else if (sender == jMenuItemMap.get(ComponentActions.ADD)) {
				showAddWindow((AbstractButton) sender);
			} else if (sender == jMenuItemMap.get(ComponentActions.DELETE)) {
				deleteRecord();
			} else if (sender == jMenuItemMap.get(ComponentActions.EXIT)) {
				closeMainWindow();
			} else if (sender == jMenuItemMap.get(ComponentActions.ABOUT)) {
				showAboutWindow();
			}
		} else if (sender instanceof RequestClientHandler) {
			manageProgressBar();
		} else if (sender instanceof TableModel) {
			refreshRecordsQuantity();
		} else if (sender instanceof Exception) {
			handleException((Exception) sender);
		}
	}
	
	/**
	 * Metoda wyświetlająca okno dialogowe z informacją o błędzie w przypadku wystąpienia wyjątku.
	 *
	 * @param exception Wyjątek, który wystąpił.
	 */
	private void handleException(Exception exception) {
		
		JOptionPane.showMessageDialog(jFrame, exception.getMessage(), "Server error!", JOptionPane.ERROR_MESSAGE);
		refreshMainWindow();
	}
	
	/**
	 * Metoda odświeżająca główne okno aplikacji.
	 */
	private void refreshMainWindow() {
		
		requestClientHandler.requestItemListFromServer();
		currentSelectedIndex = null;
		jFrame.refreshMainWindow();
	}
	
	/**
	 * Metoda wyświetlająca okno dialogowe z szczegółami wybranego rekordu.
	 *
	 * @param component Komponent wywołujący tę metodę.
	 */
	private void showDetailsWindow(AbstractButton component) {
		
		if (currentSelectedIndex != null) {
			jDialog = new JDialogClient(this, jFrame, "Record details", component, itemList.get(currentSelectedIndex));
			jButtonMapJDialog = jDialog.getjButtonMap();
			jDialog.setModal(true);
			jDialog.setVisible(true);
		}
	}
	
	/**
	 * Metoda wyświetlająca okno dialogowe umożliwiające dodanie nowego rekordu.
	 *
	 * @param component Komponent wywołujący tę metodę.
	 */
	private void showAddWindow(AbstractButton component) {
		
		jDialog = new JDialogClient(this, jFrame, "Add new record", component, null);
		jButtonMapJDialog = jDialog.getjButtonMap();
		jDialog.setModal(true);
		jDialog.setVisible(true);
	}
	
	/**
	 * Metoda zmieniająca widok okna dialogowego na tryb edycji.
	 */
	private void showEditWindow() {
		
		jDialog.changeDetailsToEditWindow();
	}
	
	/**
	 * Metoda zamykająca okno dialogowe.
	 */
	private void closeJDialog() {
		
		if (jDialog != null) {
			jDialog.dispose();
		}
	}
	
	/**
	 * Metoda zamykająca główne okno aplikacji.
	 */
	private void closeMainWindow() {
		
		jFrame.dispose();
	}
	
	/**
	 * Metoda usuwająca wybrany rekord.
	 */
	private void deleteRecord() {
		
		if (currentSelectedIndex != null) {
			requestClientHandler.deleteRecordFromServer(itemList.get(currentSelectedIndex));
			currentSelectedIndex = null;
		}
	}
	
	/**
	 * Metoda zapisująca nowy lub zmodyfikowany rekord.
	 */
	private void saveItem() {
		
		Item newItem = jDialog.generateNewItem();
		if (newItem != null) {
			requestClientHandler.putRecordToServer(jDialog.generateNewItem());
			currentSelectedIndex = null;
			jDialog.dispose();
		}
	}
	
	/**
	 * Metoda ustawiająca indeks aktualnie wybranego rekordu w tabeli.
	 *
	 * @param jTable Tabela, w której dokonano wyboru.
	 */
	private void setCurrentSelectedIndex(JTable jTable) {
		
		if (jTable.isEnabled()) {
			currentSelectedIndex = jTable.convertRowIndexToModel(jTable.getSelectedRow());
		}
	}
	
	/**
	 * Metoda sortująca rekordy w tabeli na podstawie wybranych kryteriów.
	 */
	private void sortTable() {
		
		int column;
		if (jRadioButtonMap.get(ComponentActions.ID).isSelected()) {
			column = tableModel.findColumn(jRadioButtonMap.get(ComponentActions.ID).getText());
		} else if (jRadioButtonMap.get(ComponentActions.INDEX).isSelected()) {
			column = tableModel.findColumn(jRadioButtonMap.get(ComponentActions.INDEX).getText());
		} else if (jRadioButtonMap.get(ComponentActions.QUANTITY).isSelected()) {
			column = tableModel.findColumn(jRadioButtonMap.get(ComponentActions.QUANTITY).getText());
		} else if (jRadioButtonMap.get(ComponentActions.PRICE).isSelected()) {
			column = tableModel.findColumn(jRadioButtonMap.get(ComponentActions.PRICE).getText());
		} else {
			column = tableModel.findColumn(jRadioButtonMap.get(ComponentActions.ID).getText());
		}
		tableModel.sortTable(column, (String) jComboBoxSort.getSelectedItem());
	}
	
	/**
	 * Metoda filtrująca rekordy w tabeli na podstawie wprowadzonych kryteriów wyszukiwania.
	 */
	private void filterTable() {
		
		tableModel.filterTable(jComboBoxSearch.getSelectedIndex(), jFrame.getSearchJTextField().getText());
		jFrame.refreshJTable();
		jFrame.scrollToBottomJTable();
	}
	
	/**
	 * Metoda odświeżająca liczbę rekordów wyświetlanych w tabeli.
	 */
	private void refreshRecordsQuantity() {
		
		jFrame.refreshRecordsQuantity();
		jFrame.scrollToBottomJTable();
	}
	
	/**
	 * Metoda wyświetlająca okno dialogowe z informacjami o autorze.
	 */
	private void showAboutWindow() {
		
		JOptionPane.showMessageDialog(jFrame, "Author: Patryk Kerlin", "About author", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Metoda zarządzająca paskiem postępu, odświeżająca go w zależności od stanu załadowania danych.
	 */
	private void manageProgressBar() {
		
		if (jFrame.manageProgressBar()) {
			jFrame.refreshJTable();
		}
	}
}
