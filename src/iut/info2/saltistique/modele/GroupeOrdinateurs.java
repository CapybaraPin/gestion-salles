package iut.info2.saltistique.modele;


/**
 * Représente un groupe d'ordinateur avec une quantité, un type et des logiciels.
 *
 * @author Jules Vialas
 */
public class GroupeOrdinateurs {

    /** Quantité d'ordinateur */
    private final int quantite;

    /** Type d'ordinateur */
    private final String type;

    /** Logiciel disponible sur les ordinateurs*/
    private final String[] logiciels;

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
     * Obtient les logiciels disponibles sur les ordinateurs.
     *
     * @return Les logiciels disponibles sur les ordinateurs.
     */
    public String getLogiciels() {
        StringBuilder logicielsRetour = new StringBuilder();
        for (String logiciel : logiciels) {
            logicielsRetour.append(logiciel.toLowerCase()).append(", ");
        }
        return logicielsRetour.toString();
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
