package wsb.project.views;

import wsb.project.controllers.Mediator;
import wsb.project.helpers.ComponentActions;
import wsb.project.models.Item;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Klasa zarządzająca modelem tabeli, zawierająca metody do manipulacji danymi w tabeli.
 */
public class TableModelClient extends DefaultTableModel {
	
	private final JTable jTable;
	private final List<Item> itemList;
	private final TableRowSorter<TableModel> jTableRowSorter;
	private final Mediator mediator;
	
	/**
	 * Konstruktor odpowiedzialny za przygotowanie modelu tabeli z danymi przedmiotów.
	 *
	 * @param mediator Obiekt mediatora umożliwiający komunikację między komponentami.
	 * @param itemList Lista przedmiotów, które mają być wyświetlane w tabeli.
	 */
	public TableModelClient(Mediator mediator, List<Item> itemList) {
		
		String[] columnNames = new String[]{"ID", "Index", "Description", "Quantity", "Price"};
		jTableRowSorter = new TableRowSorter<>(this);
		jTable = new JTable(this);
		this.itemList = itemList;
		this.mediator = mediator;
		
		jTable.setCellSelectionEnabled(false);
		jTable.setColumnSelectionAllowed(false);
		jTable.setRowSelectionAllowed(true);
		
		for (String columnName : columnNames) {
			addColumn(columnName);
		}
		
		jTable.getColumnModel().getColumn(0).setPreferredWidth(50);
		jTable.getColumnModel().getColumn(1).setPreferredWidth(125);
		jTable.getColumnModel().getColumn(2).setPreferredWidth(175);
		jTable.getColumnModel().getColumn(3).setPreferredWidth(75);
		jTable.getColumnModel().getColumn(4).setPreferredWidth(75);
		jTable.setRowSorter(jTableRowSorter);
		jTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if (e.getClickCount() >= 1) {
					
					mediator.notify(e.getComponent());
				}
			}
		});
		
		jTable.setModel(this);
	}
	
	/**
	 * Metoda określająca, czy komórka w tabeli jest edytowalna.
	 *
	 * @param row    Indeks wiersza.
	 * @param column Indeks kolumny.
	 * @return Zawsze zwraca false, ponieważ komórki nie są edytowalne.
	 */
	@Override
	public boolean isCellEditable(int row, int column) {
		
		return false;
	}
	
	/**
	 * Metoda określająca typ danych w kolumnie tabeli.
	 *
	 * @param columnIndex Indeks kolumny.
	 * @return Klasa reprezentująca typ danych w kolumnie.
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		
		return switch (columnIndex) {
			case 0, 3 -> Integer.class;
			case 4 -> Double.class;
			default -> String.class;
		};
	}
	
	/**
	 * Metoda odświeżająca dane w tabeli.
	 */
	public void refreshTable() {
		
		SwingUtilities.invokeLater(() -> {
			setRowCount(0);
			for (Item item : itemList) {
				addRow(new Object[]{
						item.getId(), item.getIndex(), item.getDescription(), item.getQuantity(), item.getPrice()
				});
			}
			mediator.notify(this);
		});
	}
	
	/**
	 * Metoda sortująca dane w tabeli według wybranej kolumny i kierunku.
	 *
	 * @param column Indeks kolumny do sortowania.
	 * @param action Kierunek sortowania ("ASC" lub "DESC").
	 */
	public void sortTable(Integer column, String action) {
		
		SortOrder sortOrder = null;
		if (Objects.equals(action, ComponentActions.ASC.getAction())) {
			sortOrder = SortOrder.ASCENDING;
		} else if (Objects.equals(action, ComponentActions.DESC.getAction())) {
			sortOrder = SortOrder.DESCENDING;
		}
		jTableRowSorter.setSortKeys(Collections.singletonList(new RowSorter.SortKey(column != null ? column : 0,
				sortOrder != null ? sortOrder : SortOrder.ASCENDING)));
	}
	
	/**
	 * Metoda filtrująca dane w tabeli według tekstu i kolumny.
	 *
	 * @param column Indeks kolumny do filtrowania.
	 * @param text   Tekst używany do filtrowania.
	 */
	public void filterTable(Integer column, String text) {
		
		if (text.trim().length() == 0) {
			jTableRowSorter.setRowFilter(null);
		} else {
			jTableRowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, column < 2 ? column : column + 1));
		}
	}
	
	/**
	 * Metoda pozwalająca na pobranie obiektu JTable związanego z tym modelem.
	 *
	 * @return Obiekt JTable.
	 */
	public JTable getjTable() {
		
		return jTable;
	}
	
	/**
	 * Metoda pozwalająca na pobranie obiektu TableRowSorter używanego do sortowania danych w tabeli.
	 *
	 * @return Obiekt TableRowSorter.
	 */
	public TableRowSorter<TableModel> getjTableRowSorter() {
		
		return jTableRowSorter;
	}
}
