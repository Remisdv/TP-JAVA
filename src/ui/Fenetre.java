package ui;

import export.ExportService;
import history.Historique;
import model.Saisie;
import search.RechercheForkJoin;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * Fenêtre principale "Mon application"
 */
public class Fenetre extends JFrame {

    private final JTextField champRepertoire = new JTextField(30);
    private final JTextField champTexte = new JTextField(30);
    private final Historique historique = new Historique();

    public Fenetre() {
        configurerFenetre();
        creerMenu();
        creerFormulaire();
    }

    /**
     * Configure la fenêtre (titre, dimensions, fermeture).
     */
    private void configurerFenetre() {
        this.setTitle("Mon application");
        this.setSize(600, 220);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }

    /**
     * Crée le menu "Application" avec Retour et Quitter.
     */
    private void creerMenu() {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu menu = new JMenu("Application");
        menuBar.add(menu);

        JMenuItem itemRetour = new JMenuItem("Retour");
        itemRetour.addActionListener(e -> retournerEnArriere());

        JMenuItem itemQuitter = new JMenuItem("Quitter");
        itemQuitter.addActionListener(e -> quitter());

        menu.add(itemRetour);
        menu.addSeparator();
        menu.add(itemQuitter);
    }

    /**
     * Crée le formulaire avec les champs et le bouton Appliquer.
     */
    private void creerFormulaire() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        ajouterChamp(panel, "Répertoire :", champRepertoire, 0);
        ajouterChamp(panel, "Texte recherché :", champTexte, 1);

        JButton boutonAppliquer = new JButton("Appliquer");
        boutonAppliquer.addActionListener(e -> appliquer());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(boutonAppliquer, gbc);

        this.getContentPane().add(panel);
    }

    /**
     * Ajoute un label + champ de saisie au formulaire.
     */
    private void ajouterChamp(JPanel panel, String label, JTextField champ, int ligne) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = ligne;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(champ, gbc);
    }

    // ─────────────────────── Actions métier ───────────────────────

    /**
     * Restaure l'état précédent depuis l'historique (pattern Mémento).
     */
    void retournerEnArriere() {
        Saisie precedent = historique.restaurer();

        if (precedent == null) {
            afficherInfo("Information", "Aucun état précédent disponible.");
            return;
        }

        champRepertoire.setText(precedent.getRepertoire());
        champTexte.setText(precedent.getTexte());
        afficherInfo("Retour", "État restauré :\n" + precedent);
    }

    /**
     * Ferme l'application.
     */
    void quitter() {
        this.dispose();
    }

    /**
     * Lance une recherche Fork/Join, affiche les résultats et exporte.
     */
    void appliquer() {
        // Récupérer et valider les saisies
        String repertoire = champRepertoire.getText().trim();
        String texte = champTexte.getText().trim();

        if (!validerSaisies(repertoire, texte)) {
            return;
        }

        // Sauvegarder l'état dans l'historique (Mémento)
        historique.sauvegarder(new Saisie(repertoire, texte));

        // Lancer la recherche en parallèle
        List<String> resultats = lancerRecherche(repertoire, texte);

        // Afficher les résultats
        afficherResultats(resultats);

        // Exporter si résultats
        if (!resultats.isEmpty()) {
            ExportService.exporter(resultats);
        }
    }

    /**
     * Valide les saisies (non vides, répertoire existant).
     */
    private boolean validerSaisies(String repertoire, String texte) {
        if (repertoire.isEmpty() || texte.isEmpty()) {
            afficherErreur("Erreur", "Veuillez remplir les deux champs.");
            return false;
        }

        if (!new File(repertoire).exists()) {
            afficherErreur("Erreur", "Le répertoire n'existe pas :\n" + repertoire);
            return false;
        }

        return true;
    }

    /**
     * Lance la recherche avec Fork/Join.
     */
    private List<String> lancerRecherche(String repertoire, String texte) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        return pool.invoke(new RechercheForkJoin(repertoire, texte));
    }

    /**
     * Affiche les résultats en console et dans une modale.
     */
    private void afficherResultats(List<String> resultats) {
        System.out.println("\n=== Résultats (" + resultats.size() + ") ===");
        resultats.forEach(System.out::println);
        System.out.println("================================\n");

        String message = resultats.isEmpty()
                ? "Aucun résultat trouvé."
                : resultats.size() + " résultat(s) trouvé(s).\nExportés dans out.txt";

        afficherInfo("Résultats", message);
    }

    // ─────────────────────── Affichage (modales) ───────────────────────

    private void afficherInfo(String titre, String message) {
        new Modale(this, titre, message).open();
    }

    private void afficherErreur(String titre, String message) {
        new Modale(this, titre, message).open();
    }
}
