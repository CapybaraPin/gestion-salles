package iut.info2.saltistique.modele;

import javafx.collections.ObservableList;

import java.io.Serializable;

/**
 * Représente une activité avec un identifiant unique et un nom.
 * Cette classe implémente l'interface Serializable afin de pouvoir être
 * sérialisée et désérialisée.
 *
 * @author Jules Vialas, Néo Bécogné, Dorian Adams, Hugo Robles, Tom Gutierrez
 */
public class Activite implements Serializable {

    private static final long serialVersionUID = 1L;

    private String identifiant;

    private String nom;

    /**
     * Constructeur de la classe Activite.
     *
     * @param identifiant L'identifiant unique de l'activité.
     * @param nom Le nom de l'activité.
     */
    public Activite(String identifiant, String nom) {
        this.identifiant = identifiant;
        this.nom = nom;
    }

    /**
     * Obtient l'identifiant de l'activité.
     *
     * @return L'identifiant de l'activité.
     */
    public String getIdentifiant() {
        return identifiant;
    }

    /**
     * Obtient le nom de l'activité.
     *
     * @return Le nom de l'activité.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères du nom de l'activité.
     *
     * @return Le nom de l'activité sous forme de chaîne de caractères.
     */
    @Override
    public String toString() {
        return nom;
    }

    public long getTempsTotalReservation(ObservableList<Reservation> listeReservations) {
        long totalMinutes = listeReservations.stream()
                .filter(reservation -> reservation.getActivite().equals(this))
                .mapToLong(reservation -> java.time.Duration.between(reservation.getDateDebut(), reservation.getDateFin()).toMinutes())
                .sum();
        return totalMinutes;
    }
}
