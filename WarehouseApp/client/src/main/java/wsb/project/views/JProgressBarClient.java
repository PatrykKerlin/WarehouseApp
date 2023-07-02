package wsb.project.views;

import javax.swing.*;

/**
 * Klasa reprezentująca niestandardowy pasek postępu.
 */
class JProgressBarClient extends JProgressBar {
	
	/**
	 * Konstruktor inicjalizujący domyślnie nieaktywny pasek postępu.
	 */
	public JProgressBarClient() {
		
		setIndeterminate(false);
	}
}
