package wsb.project.views;

import javax.swing.*;
import java.awt.*;

public class JScrollPaneServer extends JScrollPane {
	
	private final JScrollBar verticalScrollBar;
	
	public JScrollPaneServer(Component view, int vsbPolicy, int hsbPolicy) {
		
		super(view, vsbPolicy, hsbPolicy);
		setPreferredSize(new Dimension(500, 200));
		verticalScrollBar = getVerticalScrollBar();
	}
	
	public void scrollToBottom() {
		
		SwingUtilities.invokeLater(() -> verticalScrollBar.setValue(verticalScrollBar.getMaximum()));
	}
}
