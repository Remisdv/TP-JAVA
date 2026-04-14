package ui.fenetre;

import javax.swing.*;

/**
 * Fenêtre principale "Mon application".
 * Orchestration pure : crée les composants et branche les actions.
 */
public class Fenetre extends JFrame {

    private final FenetreUI ui;
    private final FenetreHandler handler;

    public Fenetre() {
        this.ui = new FenetreUI();
        this.handler = new FenetreHandler(this, ui);

        this.setTitle("Mon application");
        this.setSize(600, 220);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        this.setJMenuBar(ui.creerMenuBar());
        this.getContentPane().add(ui.creerPanel());

        ui.getItemRetour().addActionListener(e -> handler.retournerEnArriere());
        ui.getItemQuitter().addActionListener(e -> handler.quitter());
        ui.getBoutonAppliquer().addActionListener(e -> handler.appliquer());

        this.setVisible(true);
    }
}
