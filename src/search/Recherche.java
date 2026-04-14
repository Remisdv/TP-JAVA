package search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Moteur de recherche — Pattern Singleton.
 * Deux méthodes :
 *   - rechercher() : parcourt récursivement un répertoire ou fichier.
 *   - inspecter()  : lit ligne par ligne un fichier .java et retourne
 *                    les lignes contenant le texte, au format nomFichier§numéroLigne.
 */
public class Recherche {

    // ── Singleton ──────────────────────────────────────────────────────────────
    private static Recherche instance;

    private Recherche() {}

    public static Recherche getInstance() {
        if (instance == null) {
            instance = new Recherche();
        }
        return instance;
    }

    // ── Moteur ─────────────────────────────────────────────────────────────────

    /**
     * Parcourt récursivement le chemin donné et délègue l'inspection
     * aux fichiers .java trouvés.
     */
    public List<String> rechercher(String repertoireOuFichier, String aChercher) {
        List<String> resultats = new ArrayList<>();
        File fichier = new File(repertoireOuFichier);

        if (fichier.isFile()) {
            resultats.addAll(this.inspecter(repertoireOuFichier, aChercher));
        } else if (fichier.isDirectory()) {
            String[] enfants = fichier.list();
            if (enfants != null) {
                for (String enfant : enfants) {
                    resultats.addAll(this.rechercher(
                        repertoireOuFichier + File.separator + enfant, aChercher
                    ));
                }
            }
        }
        return resultats;
    }

    /**
     * Lit ligne par ligne un fichier .java.
     * Retourne les correspondances au format "nomFichier§numéroLigne".
     */
    public List<String> inspecter(String chemin, String aChercher) {
        List<String> resultats = new ArrayList<>();
        if (!chemin.endsWith(".java")) return resultats;

        File fichier = new File(chemin);
        try (
            FileReader fr = new FileReader(fichier, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(fr)
        ) {
            String ligne;
            int numero = 0;
            while ((ligne = br.readLine()) != null) {
                numero++;
                if (ligne.indexOf(aChercher) > -1) {
                    resultats.add(fichier.getName() + "§" + numero);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultats;
    }
}
