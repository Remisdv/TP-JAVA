import ui.Fenetre;

import javax.swing.SwingUtilities;

/**
 * Point d'entrée de l'application.
 * Lance la fenêtre sur l'EDT (Event Dispatch Thread) Swing.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Fenetre::new);
    }
}
