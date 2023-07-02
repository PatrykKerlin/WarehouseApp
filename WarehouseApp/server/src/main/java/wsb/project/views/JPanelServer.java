package wsb.project.views;

import javax.swing.*;
import java.awt.*;

public class JPanelServer extends JPanel {
	
	private final GridBagConstraints gbc;
	
	public JPanelServer(JScrollPane jScrollPane, JButtonContainerServer jButtonsContainer) {
		
		gbc = new GridBagConstraints();
		
		setLayout(new GridBagLayout());
		add(jScrollPane, setCords(0, 0, 3));
		add(jButtonsContainer.getStartJButton(), setCords(1, 0, 1));
		add(jButtonsContainer.getStopJButton(), setCords(1, 1, 1));
		add(jButtonsContainer.getExitJButton(), setCords(1, 2, 1));
	}
	
	private GridBagConstraints setCords(int x, int y, int height) {
		
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = 1;
		gbc.gridheight = height;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);
		
		return gbc;
	}
}
