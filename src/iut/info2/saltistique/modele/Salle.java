package iut.info2.saltistique.modele;

import java.io.Serializable;

/**
 * Représente une salle avec un identifiant unique, un nom, une capacité, un vidéoprojecteur,
 * un ecranXXL, une imprimante et un ordinateur.
 * Cette classe implémente l'interface Serializable afin de pouvoir être
 * sérialisée et désérialisée.
 *
 * @author Jules Vialas
 */
public class Salle implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Identifiant d'une salle */
    private String identifiant;

    /** Le nom de la salle */
    private String nom;

    /** Capacité d'une salle */
    private int capacite;

    /** La présence d'un vidéo projecteur*/
    private boolean videoProjecteur;

    /** Présence d'un ecran XXL */
    private boolean ecranXXL;

    /** Présence d'une iprimante */
    private boolean imprimante;

    /** Ordinateur présent dans la salle */
    private GroupeOrdinateurs ordinateurs;

    /**
     * Constructeur de la classe salle.
     *
     * @param identifiant L'identifiant unique d'une salle.
     * @param nom Le nom d'une salle.
     * @param capacite La capcité d'une salle.
     * @param videoProjecteur La présence d'un video projecteur dans la salle.
     * @param ecranXXL La présence d'un ecran XXL dans une salle .
     * @param imprimante La présence d'une imprimante dans la salle.
     * @param ordinateurs Les ordinateurs dans la salle .
     */
    public Salle(String identifiant, String nom, int capacite, boolean videoProjecteur, boolean ecranXXL, boolean imprimante, GroupeOrdinateurs ordinateurs) {
        this.identifiant = identifiant;
        this.nom = nom;
        this.capacite = capacite;
        this.videoProjecteur = videoProjecteur;
        this.ecranXXL = ecranXXL;
        this.imprimante = imprimante;
        this.ordinateurs = ordinateurs;
    }

    /**
     * Retourne l'identifiant.
     *
     * @return l'identifiant
     */
    public String getIdentifiant() {
        return identifiant;
    }

    /**
     * Retourne le nom.
     *
     * @return le nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne la capacité.
     *
     * @return la capacité
     */
    public int getCapacite() {
        return capacite;
    }

    /**
     * Indique si l'objet dispose d'un vidéoprojecteur.
     *
     * @return "OUI" si l'objet dispose d'un vidéoprojecteur, "NON" sinon
     */
    public String isVideoProjecteur() {
        return videoProjecteur ? "Oui" : "Non";
    }

    /**
     * Indique si l'objet dispose d'un écran XXL.
     *
     * @return "OUI" si l'objet dispose d'un écran XXL, "NON" sinon
     */
    public String isEcranXXL() {
        return ecranXXL ? "Oui" : "Non";
    }

    /**
     * Indique si l'objet dispose d'une imprimante.
     *
     * @return "OUI" si l'objet dispose d'une imprimante, "NON" sinon
     */
    public String isImprimante() {
        return imprimante ? "Oui" : "Non";
    }

    /**
     * Retourne la quantité d'ordinateurs associés.
     * Si aucun ordinateur n'est associé, retourne 0.
     *
     * @return la quantité d'ordinateurs associés, ou 0 si aucun ordinateur
     */
    public int getOrdinateurs() {
        if (ordinateurs == null) {
            return 0;
        }
        return ordinateurs.getQuantite();
    }

    /**
     * Retourne le type d'ordinateur associé.
     * Si aucun ordinateur n'est associé, retourne null.
     *
     * @return le type d'ordinateur, ou null si aucun ordinateur
     */
    public String getType() {
        if (ordinateurs == null) {
            return null;
        }
        return ordinateurs.getType();
    }

    /**
     * Retourne les logiciels installés sur l'ordinateur associé.
     * Si aucun ordinateur n'est associé, retourne null.
     *
     * @return les logiciels installés, ou null si aucun ordinateur
     */
    public String getLogiciels() {
        if (ordinateurs == null) {
            return null;
        }
        return ordinateurs.getLogiciels();
    }

    /**
     * Retourne le nom de l'objet.
     * Cette méthode est une représentation sous forme de chaîne de caractères.
     *
     * @return le nom
     */
    @Override
    public String toString() {
        return nom;
    }

}
