package iut.info2.saltistique.modele;


/**
 * Représente un groupe d'ordinateur avec une quantité, un type et des logiciels.
 *
 * @author Jules Vialas, Néo Bécogné, Dorian Adams, Hugo Robles, Tom Gutierrez
 */
public class GroupeOrdinateurs {

    /** Quantité d'ordinateur */
    private int quantite;

    /** Type d'ordinateur */
    private String type;

    /** Logiciel disponible sur les ordinateurs*/
    private String[] logiciels;

    /**
     * * Constructeur de la classe GroupeOrdinateurs.
     *
     * @param quantite Quantité d'ordinateur.
     * @param type Type d'ordinateur.
     * @param logiciels Logiciel disponible sur les ordinateurs.
     */
    public GroupeOrdinateurs(int quantite, String type, String[] logiciels) {
        this.quantite = quantite;
        this.type = type;
        this.logiciels = logiciels;
    }

    /**
     * Obtient le type d'ordinateur.
     *
     * @return Le type d'ordinateur.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Obtient les logiciels disponible sur les ordinateurs.
     *
     * @return Les logiciels disponible sur les ordinateurs.
     */
    public String getLogiciels() {
        String logicielsRetour = "";
        for (int i = 0; i < logiciels.length; i++) {
            logicielsRetour += logiciels[i].toLowerCase() + ", ";
        }
        return logicielsRetour;
    }

    /**
     * Obtient la quantité d'ordinateur.
     *
     * @return La quantité d'ordinateur.
     */
    public int getQuantite() {
        return this.quantite;
    }
}
