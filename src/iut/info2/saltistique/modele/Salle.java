/*
 * Salle.java              21/10/2024
 * IUT de RODEZ, pas de copyrights
 */

package iut.info2.saltistique.modele;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
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

    /**
     * Identifiant d'une salle
     */
    private String identifiant;

    /**
     * Le nom de la salle
     */
    private String nom;

    /**
     * Capacité d'une salle
     */
    private int capacite;

    /**
     * La présence d'un vidéo projecteur
     */
    private boolean videoProjecteur;

    /**
     * Présence d'un ecran XXL
     */
    private boolean ecranXXL;

    /**
     * Présence d'une iprimante
     */
    private boolean imprimante;

    /**
     * Ordinateur présent dans la salle
     */
    private GroupeOrdinateurs ordinateurs;


    /**
     * Constructeur de la classe salle.
     *
     * @param identifiant     L'identifiant unique d'une salle.
     * @param nom             Le nom d'une salle.
     * @param capacite        La capcité d'une salle.
     * @param videoProjecteur La présence d'un video projecteur dans la salle.
     * @param ecranXXL        La présence d'un ecran XXL dans une salle .
     * @param imprimante      La présence d'une imprimante dans la salle.
     * @param ordinateurs     Les ordinateurs dans la salle .
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
     * où X est le nombre d'heures et Y le nombre de minutes.
     */
    public long getTempsTotalReservations(List<Reservation> listeReservations) {
        long totalMinutes;

        totalMinutes = 0;
        for(Reservation reservation : listeReservations){
            if(reservation.getSalle().equals(this)){
                totalMinutes += reservation.getTempsTotalReservation();
            }
        }

        return totalMinutes;
    }

    /**
     * Calcule le temps moyen des réservations pour la salle actuelle.
     *
     * @param listeReservations La liste des réservations.
     * @return Le temps moyen des réservations sous la forme "Xh Ymin".
     */
    public long getTempsMoyenReservationsJour(List<Reservation> listeReservations) {
        LocalDateTime dateDebut;
        LocalDateTime dateLaPlusAncienne;
        LocalDateTime dateLaPlusRecente;
        long totalMinutes;
        long nbJours;

        dateLaPlusAncienne = null;
        dateLaPlusRecente = null;
        for (Reservation reservation : listeReservations) {
            dateDebut = reservation.getDateDebut();
            if (dateLaPlusAncienne == null || dateDebut.isBefore(dateLaPlusAncienne)) {
                dateLaPlusAncienne = dateDebut;
            }
            if (dateLaPlusRecente == null || dateDebut.isAfter(dateLaPlusRecente)) {
                dateLaPlusRecente = dateDebut;
            }
        }

        nbJours = Duration.between(dateLaPlusAncienne, dateLaPlusRecente).toDays() + 1;

        totalMinutes = getTempsTotalReservations(listeReservations);
        return totalMinutes / nbJours;
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
    public long getTempsMoyenReservationsSemaine(List<Reservation> listeReservations) {
        long totalMinutes;
        int nbSemaines;
        int semaineDebut;
        int anneeDebut;
        int semaineFin;
        int anneeFin;
        WeekFields weekFields;
        LocalDateTime dateDebut;
        LocalDateTime dateLaPlusAncienne;
        LocalDateTime dateLaPlusRecente;

        dateLaPlusAncienne = null;
        dateLaPlusRecente = null;
        for (Reservation reservation : listeReservations) {
            dateDebut = reservation.getDateDebut();
            if (dateLaPlusAncienne == null || dateDebut.isBefore(dateLaPlusAncienne)) {
                dateLaPlusAncienne = dateDebut;
            }
            if (dateLaPlusRecente == null || dateDebut.isAfter(dateLaPlusRecente)) {
                dateLaPlusRecente = dateDebut;
            }
        }

        // Calculer les semaines uniques
        weekFields = WeekFields.of(Locale.getDefault());
        semaineDebut = dateLaPlusAncienne.get(weekFields.weekOfWeekBasedYear());
        anneeDebut = dateLaPlusAncienne.get(weekFields.weekBasedYear());
        semaineFin = dateLaPlusRecente.get(weekFields.weekOfWeekBasedYear());
        anneeFin = dateLaPlusRecente.get(weekFields.weekBasedYear());

        nbSemaines = (anneeFin - anneeDebut) * 52
                + semaineFin - semaineDebut + 1;
        totalMinutes = getTempsTotalReservations(listeReservations);
        return totalMinutes / nbSemaines;
    }

    /**
     * Calcule le pourcentage d'utilisation de la salle en fonction du temps total des réservations.
     * La durée théorique disponible est calculée comme l'intervalle total (en minutes) entre la
     * première et la dernière réservation de la liste.
     *
     * @param listeReservations La liste des réservations de la salle.
     * @return Le pourcentage d'utilisation de la salle (entre 0 et 100).
     */
    public double getPourcentageUtilisation(List<Reservation> listeReservations) {
        long minutesTotalesReservations;

        if (listeReservations == null || listeReservations.isEmpty()) {
            return 0;
        }

        minutesTotalesReservations = 0;
        // Parcourir chaque salle unique à partir des réservations
        for (Salle salle : listeReservations.stream()
                .map(Reservation::getSalle)
                .collect(Collectors.toSet())) {
            // Ajouter les minutes totales des réservations pour cette salle
            minutesTotalesReservations += salle.getTempsTotalReservations(listeReservations);
        }


        // Calculer le temps total des réservations
        long tempsTotalReservations = getTempsTotalReservations(listeReservations);

        // Calculer le pourcentage
        return (double) tempsTotalReservations / minutesTotalesReservations * 100;
    }



}
