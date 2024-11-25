package iut.info2.saltistique.modele;

import java.io.Serializable;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;

/**
 * Représente une salle avec un identifiant unique, un nom, une capacité, un vidéoprojecteur,
 * un ecranXXL, une imprimante et un ordinateur.
 * Cette classe implémente l'interface Serializable afin de pouvoir être
 * sérialisée et désérialisée.
 *
 * @author Jules Vialas, Néo Bécogné, Dorian Adams, Hugo Robles, Tom Gutierrez
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

    /**
     * Calcule le temps total des réservations pour une salle spécifique à partir d'une liste de réservations.
     * Le temps total est calculé en additionnant la durée de chaque réservation associée à la salle actuelle
     * (la salle pour laquelle la méthode est appelée) et exprimé en heures et minutes.
     *
     * @param listeReservations La liste des réservations à analyser. Chaque élément de cette liste est une réservation
     *                          contenant des informations sur la salle, la date de début et la date de fin.
     * @return Une chaîne de caractères représentant le temps total des réservations pour la salle, au format "Xh Ymin",
     *         où X est le nombre d'heures et Y le nombre de minutes.
     */
    public String getTempsTotalReservations(List<Reservation> listeReservations) {
        long totalMinutes = listeReservations.stream()
                .filter(reservation -> reservation.getSalle().equals(this))
                .mapToLong(reservation -> java.time.Duration.between(reservation.getDateDebut(), reservation.getDateFin()).toMinutes())
                .sum();
        long heures = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        return heures + "h " + minutes + "min";
    }

    /**
     * Calcule le temps moyen des réservations pour la salle actuelle.
     *
     * @param listeReservations La liste des réservations.
     * @return Le temps moyen des réservations sous la forme "Xh Ymin".
     */
    public String getTempsMoyenReservationsJour(List<Reservation> listeReservations) {
        // Filtrer les réservations pour la salle actuelle
        List<Reservation> reservations = listeReservations.stream()
                .filter(reservation -> reservation.getSalle().equals(this))  // Filtrer par la salle actuelle
                .collect(Collectors.toList());  // Collecter les réservations filtrées

        // Si aucune réservation, renvoyer "0h 0min"
        if (reservations.isEmpty()) {
            return "0h 0min";
        }

        // Calculer le temps total en minutes pour toutes les réservations filtrées
        long totalMinutes = reservations.stream()
                .mapToLong(reservation -> java.time.Duration.between(reservation.getDateDebut(), reservation.getDateFin()).toMinutes())
                .sum();

        // Calculer la moyenne des minutes
        long moyenneMinutes = totalMinutes / reservations.size();

        // Convertir les minutes en heures et minutes
        long heures = moyenneMinutes / 60;
        long minutes = moyenneMinutes % 60;

        // Retourner le format "Xh Ymin"
        return heures + "h " + minutes + "min";
    }

    /**
     * Calcule le temps moyen des réservations par semaine pour la salle actuelle.
     *
     * Cette méthode prend une liste de réservations, filtre celles qui concernent la salle actuelle,
     * identifie les semaines distinctes où des réservations ont eu lieu, calcule la durée totale des
     * réservations, puis en déduit le temps moyen par semaine. Le résultat est retourné sous la
     * forme d'une chaîne formatée "Xh Ymin", où X est le nombre d'heures et Y le nombre de minutes.
     *
     * Si aucune réservation n'existe pour la salle, la méthode retourne "0h 0min".
     *
     * @param listeReservations La liste des réservations à analyser. Chaque élément contient
     *                          des informations sur la salle, les dates de début et de fin.
     * @return Une chaîne représentant le temps moyen des réservations par semaine pour la salle,
     *         au format "Xh Ymin". Retourne "0h 0min" si aucune réservation n'est trouvée.
     */
    public String getTempsMoyenReservationsSemaine(List<Reservation> listeReservations) {
        // Obtenir un champ pour identifier les semaines en fonction de la locale
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        // Filtrer les réservations de la salle actuelle
        List<Reservation> reservationsSalle = listeReservations.stream()
                .filter(reservation -> reservation.getSalle().equals(this)) // Filtrer par salle
                .toList();

        // Si aucune réservation, retourner "0h 0min"
        if (reservationsSalle.isEmpty()) {
            return "0h 0min";
        }

        // Obtenir le nombre de semaines distinctes
        long nombreSemaines = reservationsSalle.stream()
                .map(reservation -> reservation.getDateDebut().get(weekFields.weekOfYear())) // Obtenir le numéro de semaine
                .distinct() // Retenir les semaines uniques
                .count();

        // Calculer le temps total des réservations en minutes
        long totalMinutes = reservationsSalle.stream()
                .mapToLong(reservation -> java.time.Duration.between(
                        reservation.getDateDebut(), reservation.getDateFin()).toMinutes()) // Calculer la durée
                .sum();

        // Calculer le temps moyen en minutes par semaine
        long moyenneMinutesParSemaine = totalMinutes / nombreSemaines;

        // Convertir en heures et minutes
        long heures = moyenneMinutesParSemaine / 60;
        long minutes = moyenneMinutesParSemaine % 60;

        // Retourner le résultat formaté
        return heures + "h " + minutes + "min";
    }

}
