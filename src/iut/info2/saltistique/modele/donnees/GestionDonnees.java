/*
 * GestionDonnees.java              21/10/2024
 * Tom GUTIERREZ Jules VIALAS, pas de copyrights
 */

package iut.info2.saltistique.modele.donnees;

import iut.info2.saltistique.modele.*;


import java.util.HashMap;
import java.util.ArrayList;

/**
 * Classe responsable de la gestion des données pour les salles, activités, utilisateurs et réservations.
 *
 * <p>Fournit des méthodes pour gérer, vider et accéder à ces données, ainsi que pour
 * suivre les lignes incorrectes lors de l'importation de fichiers.</p>
 *
 * <p>Cette classe agit comme un conteneur central pour les données manipulées dans l'application.</p>
 *
 * @author Hugo ROBLES, Tom GUTIERREZ
 */
public class GestionDonnees {

    /**
     * HashMap contenant les salles, activités, utilisateurs,
     * reservations, indexées par leur identifiant unique.
     */
    private HashMap<Integer, Salle> salles;
    private HashMap<Integer, Activite> activites;
    private HashMap<Integer, Utilisateur> utilisateurs;
    private HashMap<Integer, Reservation> reservations;

    /** Liste des lignes incorrectes rencontrées lors de l'importation. */
    private ArrayList<String[]> lignesIncorrectesActivites;
    private ArrayList<String[]> lignesIncorrectesReservations;
    private ArrayList<String[]> lignesIncorrectesSalles;
    private ArrayList<String[]> lignesIncorrectesUtilisateurs;

    /** Tableau des fichiers importés, utilisé pour traiter les données. */
    private Fichier[] fichiers;

    /**
     * Constructeur de la classe GestionDonnees.
     * Initialise les structures de données pour les salles, activités, utilisateurs et réservations,
     * ainsi que les listes pour stocker les lignes incorrectes lors des importations.
     */
    public GestionDonnees() {
        activites = new HashMap<>();
        reservations = new HashMap<>();
        salles = new HashMap<>();
        utilisateurs = new HashMap<>();
        lignesIncorrectesActivites = new ArrayList<>();
        lignesIncorrectesReservations = new ArrayList<>();
        lignesIncorrectesSalles = new ArrayList<>();
        lignesIncorrectesUtilisateurs = new ArrayList<>();
    }

    /**
     * Vide toutes les données stockées dans les HashMaps et les listes.
     * Réinitialise également les fichiers importés.
     * Gère les exceptions pour éviter les problèmes liés à la suppression des données.
     * <p><b>Note :</b> Vérifier si les fichiers sont bien fermés lors de l'appel (TODO).</p>
     */
    public void viderDonnees() {
        try {
            activites.clear();
            reservations.clear();
            salles.clear();
            utilisateurs.clear();
            lignesIncorrectesActivites.clear();
            lignesIncorrectesReservations.clear();
            lignesIncorrectesSalles.clear();
            lignesIncorrectesUtilisateurs.clear();
            for (Fichier f : fichiers) {
                f.fermerFichier();
            }
            fichiers = null;
        } catch (Exception e) {
            new Notification("Impossible de vider les données", "Erreur lors de la suppression des données.");
        }
    }

    /**
     * Calcule le pourcentage d'utilisation d'une salle en fonction des réservations.
     * Cette méthode calcule le pourcentage d'occupation basé sur le temps total réservé par rapport à la capacité de la salle.
     *
     * @param salle la salle dont on souhaite calculer le pourcentage d'utilisation
     * @param reservations la liste des réservations, où chaque réservation contient une salle associée
     * @return le pourcentage d'utilisation de la salle
     */
    public double calculerPourcentageReservation(Salle salle, HashMap<Integer, Reservation> reservations) {
        // Calculer le temps total réservé pour cette salle (en heures)
        long tempsTotalReserve = reservations.values().stream()
                .filter(reservation -> reservation.getSalle().equals(salle)) // Filtrer les réservations pour la salle
                .mapToLong(reservation -> reservation.getTempsTotalReservation()) // Récupérer la durée de chaque réservation
                .sum(); // Additionner toutes les durées des réservations

        // Calculer le nombre d'heures disponibles dans la salle (en fonction de sa capacité et du nombre de jours ouvrés)
        // Supposons que la salle est disponible toute la journée pendant un certain nombre d'heures.
        // Par exemple, la capacité d'une salle pourrait être interprétée comme le nombre d'heures de réservation possibles par jour.
        long heuresDisponibles = 24; // Par exemple, 24 heures disponibles pour une journée complète

        // Calculer le pourcentage d'utilisation en fonction du temps réservé par rapport aux heures disponibles
        if (heuresDisponibles > 0) {
            return Math.min((tempsTotalReserve * 100.0) / (heuresDisponibles), 100.0); // Assurez-vous que le pourcentage ne dépasse pas 100
        }
        return 0; // Si aucune réservation n'est possible, retourner 0%
    }


    /**
     * Définit les fichiers à utiliser pour l'importation ou d'autres opérations.
     *
     * @param fichiers Tableau de fichiers à associer à cette instance.
     */
    public void setFichiers(Fichier[] fichiers) {
        this.fichiers = fichiers;
    }

    /**
     * Retourne les fichiers actuellement associés à cette instance.
     *
     * @return Tableau de fichiers importés.
     */
    public Fichier[] getFichiers() {
        return this.fichiers;
    }

    /**
     * Retourne les salles gérées par cette instance.
     *
     * @return HashMap contenant les salles indexées par leur identifiant unique.
     */
    public HashMap<Integer, Salle> getSalles() {
        return this.salles;
    }

    /**
     * Retourne les utilisateurs gérés par cette instance.
     *
     * @return HashMap contenant les utilisateurs indexés par leur identifiant unique.
     */
    public HashMap<Integer, Utilisateur> getUtilisateurs() {
        return this.utilisateurs;
    }

    /**
     * Retourne les activités gérées par cette instance.
     *
     * @return HashMap contenant les activités indexées par leur identifiant unique.
     */
    public HashMap<Integer, Activite> getActivites() {
        return this.activites;
    }

    /**
     * Retourne les réservations gérées par cette instance.
     *
     * @return HashMap contenant les réservations indexées par leur identifiant unique.
     */
    public HashMap<Integer, Reservation> getReservations() {
        return this.reservations;
    }

    /**
     * Retourne la liste des lignes incorrectes détectées lors de l'importation des activités.
     *
     * @return Liste des lignes incorrectes pour les activités.
     */
    public ArrayList<String[]> getLignesIncorrectesActivites() {
        return this.lignesIncorrectesActivites;
    }

    /**
     * Retourne la liste des lignes incorrectes détectées lors de l'importation des réservations.
     *
     * @return Liste des lignes incorrectes pour les réservations.
     */
    public ArrayList<String[]> getLignesIncorrectesReservations() {
        return this.lignesIncorrectesReservations;
    }

    /**
     * Retourne la liste des lignes incorrectes détectées lors de l'importation des salles.
     *
     * @return Liste des lignes incorrectes pour les salles.
     */
    public ArrayList<String[]> getLignesIncorrectesSalles() {
        return this.lignesIncorrectesSalles;
    }

    /**
     * Retourne la liste des lignes incorrectes détectées lors de l'importation des utilisateurs.
     *
     * @return Liste des lignes incorrectes pour les utilisateurs.
     */
    public ArrayList<String[]> getLignesIncorrectesUtilisateurs() {
        return this.lignesIncorrectesUtilisateurs;
    }
}
