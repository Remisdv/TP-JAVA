package search;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * Parallélise la recherche avec le framework Fork/Join.
 * Chaque sous-répertoire est traité dans une tâche séparée (fork),
 * puis les résultats sont réunis (join).
 */
public class RechercheForkJoin extends RecursiveTask<List<String>> {

    private final String chemin;
    private final String aChercher;

    public RechercheForkJoin(String chemin, String aChercher) {
        this.chemin = chemin;
        this.aChercher = aChercher;
    }

    @Override
    protected List<String> compute() {
        List<String> resultats = new ArrayList<>();
        File fichier = new File(chemin);

        if (fichier.isFile()) {
            // Cas de base : inspecter ce fichier via le Singleton
            resultats.addAll(Recherche.getInstance().inspecter(chemin, aChercher));
        } else if (fichier.isDirectory()) {
            String[] enfants = fichier.list();
            if (enfants != null) {
                List<RechercheForkJoin> sousTaches = new ArrayList<>();

                for (String enfant : enfants) {
                    RechercheForkJoin tache = new RechercheForkJoin(
                            chemin + File.separator + enfant, aChercher);
                    tache.fork(); // division (Fork)
                    sousTaches.add(tache);
                }

                for (RechercheForkJoin tache : sousTaches) {
                    resultats.addAll(tache.join()); // réunion (Join)
                }
            }
        }

        return resultats;
    }
}
