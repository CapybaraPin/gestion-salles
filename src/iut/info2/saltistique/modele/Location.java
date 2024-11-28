/*
 * Location.java              21/10/2024
 * IUT de Rodez, pas de copyrights
 */

package iut.info2.saltistique.modele;

import java.time.LocalDateTime;


/**
 *Représente une Location avec le nom, le prénom et les numéros de téléphone de l'interlocuteur.
 *Cette classe implémente Reservation afin de pouvoir récupérer de l'information de
 * résevation liée à une location
 *
 * @author Tom Gutierrez
 */
public class Location extends Reservation {


    /**
     * Nom de l'organisme
     */
    private final String nomOrganisme;

    /**
     * Nom de l'interlocuteur
     */
    private final String nomInterlocuteur;

    /**
     * Prénom de l'interlocuteur
     */
    private final String prenomInterlocuteur;

    /**
     * Numéro de téléphone de l'interlocuteur
     */
    private final String telephoneInterlocuteur;

    /**
     * * Constructeur de la classe Location.
     *
     * @param identifiant            Un identifiant unique.
     * @param dateDebut              La date de debut de la location .
     * @param dateFin                La date de fin de la location
     * @param description            La description de la location
     * @param salle                  La salle concerné par la location
     * @param activite               L'activité de la location
     * @param utilisateur            L'utilisateur de cette location
     * @param nomOrganisme           Le nom de l'organisme
     * @param nomInterlocuteur       Le nom de l'interlocuteur de la location
     * @param prenomInterlocuteur    Le prénom de l'interlocuteur de la location
     * @param telephoneInterlocuteur Le numéro de téléphone de l'interlocuteur
     */
    public Location(String identifiant, LocalDateTime dateDebut, LocalDateTime dateFin, String description,
                    Salle salle, Activite activite, Utilisateur utilisateur, String nomOrganisme, String nomInterlocuteur,
                    String prenomInterlocuteur, String telephoneInterlocuteur) {
        super(identifiant, dateDebut, dateFin, description, salle, activite, utilisateur);
        this.nomOrganisme = nomOrganisme;
        this.nomInterlocuteur = nomInterlocuteur;
        this.prenomInterlocuteur = prenomInterlocuteur;
        this.telephoneInterlocuteur = telephoneInterlocuteur;
    }

    /**
     * Obtient le nom de l'organisme.
     *
     * @return Le nom de l'organisme.
     */
    public String getNomOrganisme() {
        return nomOrganisme;
    }

    /**
     * Obtient le nom de l'interlocuteur.
     *
     * @return Le nom de l'interlocuteur.
     */
    public String getNomInterlocuteur() {
        return nomInterlocuteur;
    }

    /**
     * Obtient le prénom de l'interlocuteur.
     *
     * @return Le prénom de l'interlocuteur.
     */
    public String getPrenomInterlocuteur() {
        return prenomInterlocuteur;
    }

    /**
     * Obtient le numéro de téléphone de l'interlocuteur.
     *
     * @return Le numéro de téléphone de l'interlocuteur.
     */
    public String getTelephoneInterlocuteur() {
        return telephoneInterlocuteur;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères du nom de l'interlocuteur.
     *
     * @return Le nom de l'interlocuteur sous forme de chaîne de caractères.
     */
    @Override
    public String toString() {
        return "Reservation - location{" +
                "identifiant='" + getIdentifiant() + '\'' +
                ", dateDebut=" + getDateDebut() + '\'' +
                ", dateFin=" + getDateFin() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", salle=" + getSalle() + '\'' +
                ", activite=" + getActivite() + '\'' +
                ", utilisateur=" + getUtilisateur() + '\'' +
                ", nomOrganisme='" + nomOrganisme + '\'' +
                ", nomInterlocuteur='" + nomInterlocuteur + '\'' +
                ", prenomInterlocuteur='" + prenomInterlocuteur + '\'' +
                ", telephoneInterlocuteur='" + telephoneInterlocuteur + '\'' +
                '}';
    }
}