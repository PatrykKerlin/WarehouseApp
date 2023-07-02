package wsb.project.controllers;

import wsb.project.helpers.ServerLogMessages;
import wsb.project.models.AsyncHandler;
import wsb.project.models.FileHandler;
import wsb.project.models.Item;
import wsb.project.models.ServerHandler;
import wsb.project.views.*;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Klasa kontrolująca serwer i zarządzająca interakcjami użytkownika w interfejsie graficznym.
 */
public class ServerMediatorController implements Mediator {
	
	private final JButtonContainerServer jButtonsContainer;
	private final JTextPane jTextPane;
	private final JScrollPaneServer jScrollPane;
	private final JFrame jFrame;
	private final FileHandler fileHandler;
	private final String appendTextExceptionMessage;
	private final DateTimeFormatter timeFormat;
	private final Style normalText, exceptionText;
	private final StyledDocument styledJTextPane;
	private final List<Item> itemList;
	private final Map<Integer, Integer> itemIDMap;
	private final Set<String> itemIndexSet;
	private AsyncHandler asyncHandler;
	private ServerHandler server;
	
	/**
	 * Konstruktor inicjalizujący kontroler, konfigurujący interfejs użytkownika i przygotowujący dane.
	 */
	public ServerMediatorController() {
		
		timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS", Locale.getDefault());
		appendTextExceptionMessage = "Couldn't append new line of text!";
		jButtonsContainer = new JButtonContainerServer(this);
		jTextPane = new JTextPaneServer();
		normalText = jTextPane.getStyle("Normal");
		exceptionText = jTextPane.getStyle("Exception");
		styledJTextPane = jTextPane.getStyledDocument();
		
		jScrollPane = new JScrollPaneServer(jTextPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JPanel jPanel = new JPanelServer(jScrollPane, jButtonsContainer);
		jFrame = new JFrameServer(jPanel);
		
		this.itemIDMap = new HashMap<>();
		this.itemIndexSet = new HashSet<>();
		this.itemList = new ArrayList<>();
		fileHandler = new FileHandler(this, itemList, itemIDMap, itemIndexSet);
		appendNewLogLine(ServerLogMessages.PREPARING_DATA.toString(), normalText);
		fileHandler.readFromFile();
	}
	
	/**
	 * Metoda obsługująca powiadomienia od nadawcy z określoną wiadomością serwera.
	 *
	 * @param sender  Nadawca powiadomienia.
	 * @param message Wiadomość serwera do przekazania.
	 */
	@Override
	public void notify(Object sender, ServerLogMessages message) {
		
		if (sender instanceof JButton) {
			handleButtonAction((JButton) sender);
		} else {
			appendNewLogLine(message.toString(), normalText);
		}
	}
	
	/**
	 * Metoda obsługująca powiadomienia o wyjątkach i wyświetlająca je w interfejsie graficznym.
	 *
	 * @param exception Wyjątek do przekazania.
	 */
	@Override
	public void notify(Exception exception) {
		
		appendNewLogLine(exception.getMessage() + "\n", exceptionText);
	}
	
	/**
	 * Metoda obsługująca akcje przycisków w interfejsie graficznym.
	 *
	 * @param button Przycisk, którego akcja ma być obsłużona.
	 */
	private void handleButtonAction(JButton button) {
		
		if (button == jButtonsContainer.getStartJButton()) {
			if (server == null) {
				jButtonsContainer.getStartJButton().setEnabled(false);
				jButtonsContainer.getExitJButton().setEnabled(false);
				jButtonsContainer.getStopJButton().setEnabled(true);
				jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				asyncHandler = new AsyncHandler(this, itemList, itemIDMap, itemIndexSet);
				server = new ServerHandler(this, asyncHandler);
				server.startServer();
			}
		} else if (button == jButtonsContainer.getStopJButton()) {
			if (server != null) {
				appendNewLogLine(ServerLogMessages.PREPARING_DATA.toString(), normalText);
				stopServer();
				jButtonsContainer.getStopJButton().setEnabled(false);
				jButtonsContainer.getExitJButton().setEnabled(true);
				jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			}
		} else if (button == jButtonsContainer.getExitJButton()) {
			jFrame.dispose();
		}
	}
	
	/**
	 * Metoda dodająca nową linię dziennika do interfejsu graficznego.
	 *
	 * @param message Wiadomość do dodania.
	 * @param style   Styl tekstu wiadomości.
	 */
	private void appendNewLogLine(String message, Style style) {
		
		SwingUtilities.invokeLater(() -> {
			try {
				styledJTextPane.insertString(styledJTextPane.getLength(),
						LocalDateTime.now().format(timeFormat) + " | " + message, style);
				jScrollPane.scrollToBottom();
			} catch (BadLocationException e) {
				notify(new Exception(appendTextExceptionMessage));
			}
		});
	}
	
	/**
	 * Metoda zatrzymująca serwer i przygotowująca do bezpiecznego zamknięcia programu.
	 */
	private void stopServer() {
		
		if (asyncHandler != null) {
			asyncHandler.closeThreadPool();
		}
		if (server != null) {
			server.stopServer();
		}
		if (fileHandler != null) {
			fileHandler.saveToFile();
		}
	}
}
