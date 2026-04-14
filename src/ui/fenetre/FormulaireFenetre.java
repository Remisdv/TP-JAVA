package ui.fenetre;

import javax.swing.*;
import java.awt.*;

/**
 * Construit le formulaire de recherche (champs + bouton Appliquer).
 */
public class FormulaireFenetre {

    private final JTextField champRepertoire;
    private final JTextField champTexte;
    private final JButton boutonAppliquer;

    public FormulaireFenetre() {
        this.champRepertoire = new JTextField(30);
        this.champTexte      = new JTextField(30);
        this.boutonAppliquer = new JButton("Appliquer");
    }

    /**
     * Crée et retourne le panel du formulaire.
     */
    public JPanel creerPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        ajouterLabelChamp(panel, "Répertoire :", champRepertoire, 0);
        ajouterLabelChamp(panel, "Texte recherché :", champTexte, 1);
        ajouterBouton(panel);

        return panel;
    }

    /**
     * Ajoute un label et un champ de saisie au panel.
     */
    private void ajouterLabelChamp(JPanel panel, String label, JTextField champ, int ligne) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label
        gbc.gridx = 0;
        gbc.gridy = ligne;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);

        // Champ
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(champ, gbc);
    }

    /**
     * Ajoute le bouton Appliquer au formulaire.
     */
    private void ajouterBouton(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(boutonAppliquer, gbc);
    }

    // Accesseurs

    public JTextField getChampRepertoire() { return champRepertoire; }
    public JTextField getChampTexte()      { return champTexte; }
    public JButton getBoutonAppliquer()    { return boutonAppliquer; }
}
