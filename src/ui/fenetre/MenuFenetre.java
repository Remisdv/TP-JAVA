package ui.fenetre;

import javax.swing.*;

/**
 * Construit le menu "Application" avec ses items.
 */
public class MenuFenetre {

    private final JMenuItem itemRetour;
    private final JMenuItem itemQuitter;

    public MenuFenetre() {
        this.itemRetour = new JMenuItem("Retour");
        this.itemQuitter = new JMenuItem("Quitter");
    }

    /**
     * Crée et retourne la barre de menu.
     */
    public JMenuBar creerMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Application");

        menu.add(itemRetour);
        menu.addSeparator();
        menu.add(itemQuitter);

        menuBar.add(menu);
        return menuBar;
    }

    public JMenuItem getItemRetour()  { return itemRetour; }
    public JMenuItem getItemQuitter() { return itemQuitter; }
}
