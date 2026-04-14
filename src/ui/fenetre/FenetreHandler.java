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
 * Gère les actions et la logique de recherche.
 */
public class FenetreHandler {

    private final JFrame frame;
    private final FenetreUI ui;
    private final Historique historique;

    public FenetreHandler(JFrame frame, FenetreUI ui) {
        this.frame = frame;
        this.ui = ui;
        this.historique = new Historique();
    }

    public void retournerEnArriere() {
        Saisie precedent = historique.restaurer();
        if (precedent == null) {
            afficherInfo("Information", "Aucun état précédent disponible.");
            return;
        }
        ui.getChampRepertoire().setText(precedent.getRepertoire());
        ui.getChampTexte().setText(precedent.getTexte());
        afficherInfo("Retour", "État restauré :\n" + precedent);
    }

    public void quitter() {
        frame.dispose();
    }

    public void appliquer() {
        String repertoire = ui.getChampRepertoire().getText().trim();
        String texte = ui.getChampTexte().getText().trim();

        if (repertoire.isEmpty() || texte.isEmpty()) {
            afficherErreur("Erreur", "Veuillez remplir les deux champs.");
            return;
        }
        if (!new File(repertoire).exists()) {
            afficherErreur("Erreur", "Le répertoire n'existe pas :\n" + repertoire);
            return;
        }

        historique.sauvegarder(new Saisie(repertoire, texte));

        ForkJoinPool pool = ForkJoinPool.commonPool();
        List<String> resultats = pool.invoke(new RechercheForkJoin(repertoire, texte));

        System.out.println("\n=== Résultats (" + resultats.size() + ") ===");
        resultats.forEach(System.out::println);
        System.out.println("================================\n");

        String msg = resultats.isEmpty()
            ? "Aucun résultat trouvé."
            : resultats.size() + " résultat(s) trouvé(s).\nExportés dans out.txt";
        afficherInfo("Résultats", msg);

        if (!resultats.isEmpty()) {
            ExportService.exporter(resultats);
        }
    }

    private void afficherInfo(String titre, String message) {
        new Modale(frame, titre, message).open();
    }

    private void afficherErreur(String titre, String message) {
        new Modale(frame, titre, message).open();
    }
}
