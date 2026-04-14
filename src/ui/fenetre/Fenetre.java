package ui.fenetre;

import export.ExportService;
import history.Historique;
import model.Saisie;
import search.RechercheForkJoin;
import ui.Modale;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * Fenêtre principale "Mon application".
 * Orchestre l'interaction utilisateur avec les services métier.
 */
public class Fenetre extends JFrame {

    private final FormulaireFenetre formulaire;
    private final MenuFenetre menu;
    private final Historique historique;

    public Fenetre() {
        this.historique = new Historique();
        this.formulaire = new FormulaireFenetre();
        this.menu = new MenuFenetre();

        configurerFenetre();
        brancherActions();
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

        // Ajouter le menu
        this.setJMenuBar(menu.creerMenuBar());

        // Ajouter le formulaire
        this.getContentPane().add(formulaire.creerPanel());

        this.setVisible(true);
    }

    /**
     * Branche les actions (lambdas) sur les éléments UI.
     */
    private void brancherActions() {
        menu.getItemRetour().addActionListener(e -> retournerEnArriere());
        menu.getItemQuitter().addActionListener(e -> quitter());
        formulaire.getBoutonAppliquer().addActionListener(e -> appliquer());
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

        formulaire.getChampRepertoire().setText(precedent.getRepertoire());
        formulaire.getChampTexte().setText(precedent.getTexte());
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
        String repertoire = formulaire.getChampRepertoire().getText().trim();
        String texte = formulaire.getChampTexte().getText().trim();

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

    // ─────────────────────── Validations ───────────────────────

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

    // ─────────────────────── Métier ───────────────────────

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
