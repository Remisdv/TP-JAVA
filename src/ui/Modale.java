package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Fenêtre de dialogue modale (bloquante) pour afficher infos ou erreurs.
 * Pattern cours : JDialog(frame, true) = bloquant.
 */
public class Modale extends JDialog {

    private final JTextArea zoneTexte;

    public Modale(JFrame frame, String titre, String message) {
        super(frame, titre, true); // true = modale bloquante
        this.setSize(450, 250);
        this.setLocationRelativeTo(frame);
        this.setResizable(false);

        this.zoneTexte = new JTextArea(message);
        this.zoneTexte.setEditable(false);
        this.zoneTexte.setLineWrap(true);
        this.zoneTexte.setWrapStyleWord(true);
        this.zoneTexte.setFont(new Font("SansSerif", Font.PLAIN, 13));
        this.zoneTexte.setMargin(new Insets(10, 10, 10, 10));

        JButton btnFermer = new JButton("Fermer");
        btnFermer.addActionListener(e -> this.dispose());

        JPanel panelBas = new JPanel();
        panelBas.add(btnFermer);

        this.getContentPane().add(new JScrollPane(zoneTexte), BorderLayout.CENTER);
        this.getContentPane().add(panelBas, BorderLayout.SOUTH);
    }

    /** Affiche la modale (bloquant). */
    public void open() {
        this.setVisible(true);
    }
}
