package model;

/**
 * DTO / Mémento : stocke l'état des deux champs de saisie.
 */
public class Saisie {

    private final String repertoire;
    private final String texte;

    public Saisie(String repertoire, String texte) {
        this.repertoire = repertoire;
        this.texte = texte;
    }

    public String getRepertoire() { return repertoire; }
    public String getTexte()      { return texte; }

    @Override
    public String toString() {
        return "Saisie{repertoire='" + repertoire + "', texte='" + texte + "'}";
    }
}
