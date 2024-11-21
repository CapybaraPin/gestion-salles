package iut.info2.saltistique.modele;

import javafx.collections.ObservableList;

import java.io.Serializable;

/**
 * Représente un utilisateur avec un identifiant unique, un nom, un prénom
 * et un numéro téléphone.
 * Cette classe implémente l'interface Serializable afin de pouvoir être
 * sérialisée et désérialisée.
 *
 * @author Jules Vialas
 */
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    /** identifiat unique de l'utilisateur */
    private String identifiant;

    /** Nom de l'utilisateur */
    private String nom;

    /** Prénom de l'utilisateur */
    private String prenom;

    /** Le numéro de téléphone de l'utilisateur */
    private String numeroTelephone;

    /**
     * Constructeur de la classe Utilisateur.
     *
     * @param identifiant L'identifiant unique de l'utilisateur.
     * @param nom Le nom de l'utilisateur.
     * @param prenom Le prénom de l'utilisateur.
     * @param numeroTelephone Le numéro de téléphone de l'utilisateur.
     */
    public Utilisateur(String identifiant, String nom, String prenom, String numeroTelephone) {
        this.identifiant = identifiant;
        this.nom = nom;
        this.prenom = prenom;
        this.numeroTelephone = numeroTelephone;
    }

    /**
     * Obtient l'identifiant de l'utilisateur.
     *
     * @return L'identifiant de l'utilisateur.
     */
    public String getIdentifiant() {
        return identifiant;
    }

    /**
     * Obtient le nom de l'utilisateur.
     *
     * @return Le nom de l'utilisateur.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Obtient le prénom de l'utilisateur.
     *
     * @return Le prénom de l'utilisateur.
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Obtient le numéro de téléphone de l'utilisateur.
     *
     * @return Le numéro de téléphone de l'utilisateur.
     */
    public String getNumeroTelephone() {
        return numeroTelephone;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères.
     * La chaîne est constituée du prénom et du nom séparés par un espace.
     *
     * @return une chaîne de caractères représentant le prénom et le nom de l'utilisateur
     */
    @Override
    public String toString() {
        return prenom + " " + nom;
    }

    public long getTempsTotalReservation(ObservableList<Reservation> listeReservations) {
        long totalMinutes = listeReservations.stream()
                .filter(reservation -> reservation.getUtilisateur().equals(this))
                .mapToLong(reservation -> java.time.Duration.between(reservation.getDateDebut(), reservation.getDateFin()).toMinutes())
                .sum();
        return totalMinutes;
    }

}
