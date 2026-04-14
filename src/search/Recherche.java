package search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Service de recherche — Pattern Singleton.
 *
 * Responsabilité : inspecter un fichier .java ligne par ligne.
 * (La récursivité sur les dossiers est pour RechercheForkJoin)
 */
public class Recherche {

    private static Recherche instance;

    private Recherche() {
    }

    public static Recherche getInstance() {
        if (instance == null) {
            instance = new Recherche();
        }
        return instance;
    }

    /**
     * Inspecte un fichier .java et retourne les lignes contenant le texte cherché.
     * Format : nomFichier§numéroLigne
     */
    public List<String> inspecter(String cheminFichier, String texteCherche) {
        List<String> resultats = new ArrayList<>();

        if (!cheminFichier.endsWith(".java")) {
            return resultats; // ignorer les fichiers non-.java
        }

        File fichier = new File(cheminFichier);
        try (
                FileReader fr = new FileReader(fichier, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(fr)) {
            String ligne;
            int numeroLigne = 0;

            while ((ligne = br.readLine()) != null) {
                numeroLigne++;
                if (ligne.indexOf(texteCherche) > -1) {
                    resultats.add(fichier.getName() + "§" + numeroLigne);
                }
            }
        } catch (IOException e) {
            System.err.println("[Recherche] Erreur lecture " + cheminFichier + " : " + e.getMessage());
        }

        return resultats;
    }
}
