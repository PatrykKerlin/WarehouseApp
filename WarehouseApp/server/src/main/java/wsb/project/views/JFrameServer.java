package wsb.project.views;

import javax.swing.*;

public class JFrameServer extends JFrame {
	
	public JFrameServer(JPanel jPanel) {
		
		super("WarehouseServer");
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		add(jPanel);
		pack();
		setVisible(true);
	}
}
