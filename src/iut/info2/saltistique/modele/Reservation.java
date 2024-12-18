/*
 * Reservation.java              21/10/2024
 * IUT de Rodez, pas de copyrights
 */

package iut.info2.saltistique.modele;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Représente une réservation avec un identifiant unique, une date de debut, une date de fin,
 * une description, une salle concerné, une activité et un utilisateur.
 * Cette classe implémente l'interface Serializable afin de pouvoir être
 * sérialisée et désérialisée.
 *
 * @author Jules Vialas
 */
public class Reservation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Identifant de la réservation*/
    private String identifiant;

    /** Date de debut de la réservation */
    private LocalDateTime dateDebut;

    /** Date de fin de la réservation*/
    private LocalDateTime dateFin;

    /** Descritpion de la réservation */
    private String description;

    /** Salle réservé */
    private Salle salle;

    /** Activité de la réservation */
    private Activite activite;

    /** Utilisatuer de la réservation */
    private Utilisateur utilisateur;

    /**
     * Constructeur de la classe Formation.
     * Ce constructeur initialise une réservation de formation en prenant en compte
     * l'identifiant, les dates de début et de fin, la description, la salle,
     * l'activité, l'utilisateur ainsi que les informations relatives au formateur.
     *
     * @param identifiant L'identifiant unique de la réservation.
     * @param dateDebut La date et l'heure de début de la réservation.
     * @param dateFin La date et l'heure de fin de la réservation.
     * @param description Une description de la réservation, du motif ou des objectifs.
     * @param salle La salle réservé.
     * @param activite L'activité associée à la réservation.
     * @param utilisateur L'utilisateur ayant réservé.
     */
    public Reservation(String identifiant, LocalDateTime dateDebut, LocalDateTime dateFin, String description,
                       Salle salle, Activite activite, Utilisateur utilisateur) {
        this.identifiant = identifiant;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.description = description;
        this.salle = salle;
        this.activite = activite;
        this.utilisateur = utilisateur;
    }

    /**
     * Obtient la salle de la réservation.
     *
     * @return La salle de la réservation.
     */
    public Salle getSalle() {
        return salle;
    }

    /**
     * Obtient l'activité de la réservation.
     *
     * @return L'activité de la réservation
     */
    public Activite getActivite() {
        return activite;
    }

    /**
     * Obtient l'utilisateur de la réservation.
     *
     * @return L'utilisateur de la réservation.
     */
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    /**
     * Obtient la date de debut de la réservation.
     *
     * @return La date de debut de la réservation.
     */
    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    /**
     * Obtient la date de fin de la réservation.
     *
     * @return La date de fin de la réservation.
     */
    public LocalDateTime getDateFin() {
        return dateFin;
    }

    /**
     * Obtient la déscription de la réservation.
     *
     * @return La déscription de la réservation.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Obtient l'identifiant de la réservation.
     *
     * @return L'identifiant de la réservation.
     */
    public String getIdentifiant() {
        return identifiant;
    }

    /**
     * Calcul la durée de la réservation en heures.
     *
     * @return La durée de la réservation en heures.
     */
    public double getDuree() {
        long durationInMinutes = java.time.Duration.between(dateDebut, dateFin).toMinutes();
        return durationInMinutes / 60.0; // Retourne la durée en heures
    }

    /**
     * Cette méthode génère une description détaillée de la réservation
     * incluant les informations telles que l'identifiant, les dates, la salle,
     * l'activité, l'utilisateur.
     *
     * @return Une chaîne de caractères représentant les informations complètes
     *         de la réservation sous la forme d'une description.
     */
    @Override
    public String toString() {
        return "Reservation{" +
                "identifiant='" + identifiant + '\'' +
                ", dateDebut='" + dateDebut + '\'' +
                ", dateFin='" + dateFin + '\'' +
                ", description='" + description + '\'' +
                ", salle='" + salle + '\'' +
                ", activite='" + activite + '\'' +
                ", utilisateur='" + utilisateur + '\'' +
                '}';
    }

    /**
     * Calcule le temps total de la réservation en minutes.
     * Utilise la méthode {@link java.time.Duration#between} pour calculer
     * @return Le temps total de la réservation en minutes.
     */
    public long getTempsTotalReservation() {
        return java.time.Duration.between(dateDebut, dateFin).toMinutes();
    }
}
