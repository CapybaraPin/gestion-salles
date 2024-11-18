package iut.info2.saltistique.modele;

import java.time.LocalDateTime;


/**
 *Représente une Location avec le nom, le prénom et le numéros de téléphone de l'interlocuteur.
 *Cette classe implémente Reservation afin de pouvoir récupérer des information de
 * résevation liée a une location
 *
 * @author Jules Vialas, Néo Bécogné, Dorian Adams, Hugo Robles, Tom Gutierrez
 */
public class Location extends Reservation {


    /** Nom de l'organisme */
    private String nomOrganisme;

    /** Nom de l'interlocuteur */
    private String nomInterlocuteur;

    /** Prénom de l'interlocuteur*/
    private String prenomInterlocuteur;

    /** Numéro de téléphone de l'interlocuteur */
    private String telephoneInterlocuteur;

    /**
     * * Constructeur de la classe Location.
     *
     * @param identifiant Un identifiant unique.
     * @param dateDebut La date de debut de la location .
     * @param dateFin La date de fin de la location
     * @param description La description de la location
     * @param salle La salle concerné par la location
     * @param activite L'activité de la location
     * @param utilisateur Les utilisateur de cette location
     * @param nomOrganisme Le nom de l'organisme
     * @param nomInterlocuteur Le nom de l'interlocuteur de la location
     * @param prenomInterlocuteur Le prénom de l'interlocuteur de la location
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
     * Obtient le numéro de téléphone.
     *
     * @return Le numéro de téléphone.
     */
    public String getTelephoneInterlocuteur() {
        return telephoneInterlocuteur;
    }
}
