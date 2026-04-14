package tp.export;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Écrit les résultats en minuscules dans le fichier désigné par @Output sur FormatExport.
 * Le chemin est déterminé par réflexion.
 */
public class Exporteur {

    public static void exporter(List<String> resultats) {
        // Réflexion : lire @Output sur FormatExport pour obtenir le chemin
        String chemin = "out.txt";
        if (FormatExport.class.isAnnotationPresent(Output.class)) {
            chemin = FormatExport.class.getAnnotation(Output.class).value();
        }

        // Écriture en minuscules via Stream
        List<String> lignes = resultats.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(chemin))) {
            for (String ligne : lignes) {
                writer.write(ligne);
                writer.newLine();
            }
            System.out.println("[Exporteur] " + lignes.size() + " ligne(s) écrite(s) dans : " + chemin);
        } catch (IOException e) {
            System.err.println("[Exporteur] Erreur d'écriture : " + e.getMessage());
        }
    }
}
