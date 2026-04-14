package ui;

import export.FormatExport;
import export.Output;
import history.Historique;
import model.Saisie;
import search.RechercheForkJoin;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * Fenêtre principale "Mon application".
 * Couvre : Swing, lambdas, Mémento, Fork/Join, Stream, réflexion.
 */
public class Fenetre extends JFrame {

    private final JTextField champRepertoire = new JTextField(30);
    private final JTextField champTexte      = new JTextField(30);
    private final Historique historique       = new Historique();

    public Fenetre() {
        this.setTitle("Mon application");
        this.setSize(600, 220);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        // ── Menu ──────────────────────────────────────────────────────────────
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu menu = new JMenu("Application");
        menuBar.add(menu);

        JMenuItem itemRetour  = new JMenuItem("Retour");
        JMenuItem itemQuitter = new JMenuItem("Quitter");
        menu.add(itemRetour);
        menu.addSeparator();
        menu.add(itemQuitter);

        // ── Actions lambda ────────────────────────────────────────────────────
        itemRetour.addActionListener(event  -> this.retournerEnArriere());
        itemQuitter.addActionListener(event -> this.quitter());

        // ── Contenu principal ─────────────────────────────────────────────────
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panel.add(new JLabel("Répertoire :"), gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(champRepertoire, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(new JLabel("Texte recherché :"), gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(champTexte, gbc);

        JButton boutonAppliquer = new JButton("Appliquer");
        boutonAppliquer.addActionListener(event -> this.appliquer()); // lambda

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(boutonAppliquer, gbc);

        this.getContentPane().add(panel);
        this.setVisible(true);
    }

    // ── Méthodes métier ───────────────────────────────────────────────────────

    /** Restaure l'état précédent depuis l'historique (pattern Mémento). */
    void retournerEnArriere() {
        Saisie precedent = historique.restaurer();
        if (precedent == null) {
            new Modale(this, "Information", "Aucun état précédent disponible.").open();
            return;
        }
        champRepertoire.setText(precedent.getRepertoire());
        champTexte.setText(precedent.getTexte());
        new Modale(this, "Retour", "État restauré :\n" + precedent).open();
    }

    /** Ferme l'application. */
    void quitter() {
        this.dispose();
    }

    /**
     * Sauvegarde l'état courant (Mémento), lance la recherche Fork/Join,
     * affiche les résultats en console et exporte dans out.txt.
     */
    void appliquer() {
        String repertoire = champRepertoire.getText().trim();
        String texte      = champTexte.getText().trim();

        if (repertoire.isEmpty() || texte.isEmpty()) {
            new Modale(this, "Erreur", "Veuillez remplir les deux champs.").open();
            return;
        }
        if (!new File(repertoire).exists()) {
            new Modale(this, "Erreur", "Le répertoire n'existe pas :\n" + repertoire).open();
            return;
        }

        // ── Pattern Mémento : sauvegarde ──────────────────────────────────────
        historique.sauvegarder(new Saisie(repertoire, texte));

        // ── Fork/Join ─────────────────────────────────────────────────────────
        ForkJoinPool pool = ForkJoinPool.commonPool();
        List<String> resultats = pool.invoke(new RechercheForkJoin(repertoire, texte));

        // ── Affichage console ─────────────────────────────────────────────────
        System.out.println("=== Résultats (" + resultats.size() + ") ===");
        resultats.forEach(System.out::println);
        System.out.println("================================");

        // ── Export ────────────────────────────────────────────────────────────
        if (!resultats.isEmpty()) {
            exporter(resultats);
        }

        // ── Modale de résultat ────────────────────────────────────────────────
        String msg = resultats.isEmpty()
            ? "Aucun résultat trouvé."
            : resultats.size() + " résultat(s) trouvé(s).\nExportés dans out.txt";
        new Modale(this, "Résultats", msg).open();
    }

    /**
     * Écrit les résultats en minuscules dans le fichier désigné par @Output
     * (déterminé par réflexion) via un Stream.
     */
    private void exporter(List<String> resultats) {
        // Réflexion : lire @Output sur FormatExport pour obtenir le chemin
        String chemin = "out.txt";
        Output annotation = FormatExport.class.getAnnotation(Output.class);
        if (annotation != null) {
            chemin = annotation.value();
        }

        final String cheminFinal = chemin;
        try (FileWriter fw = new FileWriter(cheminFinal, Charset.forName("UTF-8"))) {
            resultats.stream()
                .map(String::toLowerCase)
                .forEach(ligne -> {
                    try {
                        fw.append(ligne).append(System.lineSeparator());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            System.out.println("[Export] Fichier écrit : " + cheminFinal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
