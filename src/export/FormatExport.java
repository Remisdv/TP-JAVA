package export;

/**
 * Parse une ligne "nomFichier§numéroLigne" dans ses attributs.
 * Annotée @Output("out.txt") : le chemin d'export est déterminé
 * par réflexion à l'exécution.
 */
@Output("out.txt")
public class FormatExport {

    private final String nomFichier;
    private final String positionDansFichier;

    public FormatExport(String ligne) {
        String[] parts = ligne.split("§");
        if (parts.length != 2) {
            throw new IllegalArgumentException(
                "Format invalide (attendu nomFichier§ligne) : " + ligne);
        }
        this.nomFichier          = parts[0].trim();
        this.positionDansFichier = parts[1].trim();
    }

    public String getNomFichier()          { return nomFichier; }
    public String getPositionDansFichier() { return positionDansFichier; }

    @Override
    public String toString() {
        return nomFichier + "§" + positionDansFichier;
    }
}
