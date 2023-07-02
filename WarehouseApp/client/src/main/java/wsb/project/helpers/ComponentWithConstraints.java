package wsb.project.helpers;

import java.awt.*;

/**
 * Klasa reprezentująca komponent wraz z jego parametrami dla menadżera układu GridBagLayout.
 */
public record ComponentWithConstraints(Component component, GridBagConstraints constraints) {
	
}
