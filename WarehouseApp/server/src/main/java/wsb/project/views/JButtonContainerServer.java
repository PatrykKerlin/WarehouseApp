package wsb.project.views;

import wsb.project.controllers.Mediator;

import javax.swing.*;

public class JButtonContainerServer {
	
	private final JButton startJButton;
	private final JButton stopJButton;
	private final JButton exitJButton;
	private final Mediator mediator;
	
	public JButtonContainerServer(Mediator mediator) {
		
		this.mediator = mediator;
		
		startJButton = createButton("Start");
		stopJButton = createButton("Stop");
		exitJButton = createButton("Exit");
		
		stopJButton.setEnabled(false);
	}
	
	private JButton createButton(String action) {
		
		JButton button = new JButton(action);
		button.addActionListener(event -> mediator.notify(button, null));
		return button;
	}
	
	public JButton getStartJButton() {
		
		return startJButton;
	}
	
	public JButton getStopJButton() {
		
		return stopJButton;
	}
	
	public JButton getExitJButton() {
		
		return exitJButton;
	}
}
