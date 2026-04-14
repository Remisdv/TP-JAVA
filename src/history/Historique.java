package history;

import model.Saisie;
import java.util.Stack;

/**
 * Gardien du pattern Mémento.
 * Utilise une Stack<Saisie> pour conserver l'historique des états.
 */
public class Historique {

    private final Stack<Saisie> pile = new Stack<>();

    /** Sauvegarde l'état courant (appelé par appliquer()). */
    public void sauvegarder(Saisie saisie) {
        pile.add(saisie);
    }

    /**
     * Retire l'état courant et retourne le précédent.
     * Retourne null s'il n'y a plus d'état antérieur.
     */
    public Saisie restaurer() {
        if (!pile.isEmpty()) {
            pile.pop(); // enlève l'état courant
            return pile.isEmpty() ? null : pile.peek();
        }
        return null;
    }

    public boolean isEmpty() { return pile.isEmpty(); }
}
