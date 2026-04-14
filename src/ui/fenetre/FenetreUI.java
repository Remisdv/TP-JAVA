package ui.fenetre;

import javax.swing.*;
import java.awt.*;

/**
 * Construit l'interface : menu + formulaire.
 */
public class FenetreUI {

    // Menu
    private final JMenuItem itemRetour;
    private final JMenuItem itemQuitter;

    // Formulaire
    private final JTextField champRepertoire;
    private final JTextField champTexte;
    private final JButton boutonAppliquer;

    public FenetreUI() {
        this.itemRetour = new JMenuItem("Retour");
        this.itemQuitter = new JMenuItem("Quitter");
        this.champRepertoire = new JTextField(30);
        this.champTexte = new JTextField(30);
        this.boutonAppliquer = new JButton("Appliquer");
    }

    public JMenuBar creerMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Application");
        menu.add(itemRetour);
        menu.addSeparator();
        menu.add(itemQuitter);
        menuBar.add(menu);
        return menuBar;
    }

    public JPanel creerPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(new JLabel("Répertoire :"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(champRepertoire, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(new JLabel("Texte recherché :"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(champTexte, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(boutonAppliquer, gbc);

        return panel;
    }

    public JMenuItem getItemRetour()         { return itemRetour; }
    public JMenuItem getItemQuitter()        { return itemQuitter; }
    public JTextField getChampRepertoire()   { return champRepertoire; }
    public JTextField getChampTexte()        { return champTexte; }
    public JButton getBoutonAppliquer()      { return boutonAppliquer; }
}
