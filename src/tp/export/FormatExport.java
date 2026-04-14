package tp.export;

/**
 * Classe annotée @Output("out.txt").
 * Parse une ligne au format "nomFichier§numéroLigne" dans ses attributs.
 */
@Output("out.txt")
public class FormatExport {

    private final String nomFichier;
    private final int    position;

    public FormatExport(String ligne) {
        String[] parts = ligne.split("§");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Format invalide (attendu nomFichier§ligne) : " + ligne);
        }
        this.nomFichier = parts[0].trim();
        this.position   = Integer.parseInt(parts[1].trim());
    }

    public String getNomFichier() { return nomFichier; }
    public int    getPosition()   { return position; }

    @Override
    public String toString() {
        return nomFichier + "§" + position;
    }
}
