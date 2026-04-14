package export;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Service responsable de l'export des résultats de recherche.
 *
 * Responsabilités :
 * - Déterminer le chemin de sortie via réflexion sur @Output
 * - Écrire les résultats en minuscules dans le fichier
 */
public class ExportService {

    private static final String CHEMIN_PAR_DEFAUT = "out.txt";

    /**
     * Exporte les résultats de recherche.
     * @param resultats liste des résultats (format : nomFichier§numéroLigne)
     */
    public static void exporter(List<String> resultats) {
        if (resultats.isEmpty()) {
            return;
        }

        String cheminSortie = obtenirCheminSortie();
        ecrireResultats(cheminSortie, resultats);
    }

    /**
     * Récupère le chemin de sortie en lisant l'annotation @Output sur FormatExport.
     */
    private static String obtenirCheminSortie() {
        try {
            FormatExport dummy = new FormatExport("dummy§0");
            Output annotation = dummy.getClass().getAnnotation(Output.class);
            return (annotation != null) ? annotation.value() : CHEMIN_PAR_DEFAUT;
        } catch (Exception e) {
            return CHEMIN_PAR_DEFAUT;
        }
    }

    /**
     * Écrit les résultats en minuscules dans le fichier via Stream.
     */
    private static void ecrireResultats(String chemin, List<String> resultats) {
        try (FileWriter fw = new FileWriter(chemin, Charset.forName("UTF-8"))) {
            resultats.stream()
                .map(String::toLowerCase)
                .forEach(ligne -> {
                    try {
                        fw.append(ligne).append(System.lineSeparator());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            System.out.println("[✓ Export] " + resultats.size() + " ligne(s) → " + chemin);
        } catch (IOException e) {
            System.err.println("[✗ Export] Erreur : " + e.getMessage());
        }
    }
}
