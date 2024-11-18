/*
 * GestionDonnees.java              21/10/2024
 * Tom GUTIERREZ Jules VIALAS, pas de copyrights
 */

package iut.info2.saltistique.modele.donnees;

import iut.info2.saltistique.modele.*;


import java.util.HashMap;
import java.util.ArrayList;

/**
 * Classe responsable de la gestion des données, y compris l'importation et l'exportation des fichiers.
 * Elle contient des expressions régulières pour valider les entrées des employés, salles, activités et réservations.
 */
public class GestionDonnees {

    private HashMap<Integer, Salle> salles;
    private HashMap<Integer, Activite> activites;
    private HashMap<Integer, Utilisateur> utilisateurs;
    private HashMap<Integer, Reservation> reservations;

    private ArrayList<String[]> lignesIncorrectesActivites;
    private ArrayList<String[]> lignesIncorrectesReservations;
    private ArrayList<String[]> lignesIncorrectesSalles;
    private ArrayList<String[]> lignesIncorrectesUtilisateurs;

    /**
     * Constructeur de la classe GestionDonnees.
     * Initialise les tableaux des employés, salles, activités, et réservations.
     * Initialise les tableaux des lignes incorrectes pour les employés, salles, activités, et réservations.
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
     * Vide les données stockées dans les tableaux des employés, salles, activités, et réservations.
     * Vide également les tableaux des lignes incorrectes pour les employés, salles, activités, et réservations.
     * Remet à null les fichiers importés. // TODO : vérifier si les fichiers se ferment bien
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
        } catch (Exception e) {
            new Notification("Impossible de vider les données", "Erreur lors de la suppression des données.");
        }
    }

    public HashMap<Integer, Salle> getSalles() {
        return this.salles;
    }

    public HashMap<Integer, Utilisateur> getUtilisateurs() {
        return this.utilisateurs;
    }

    public HashMap<Integer, Activite> getActivites() {
        return this.activites;
    }

    public HashMap<Integer, Reservation> getReservations() {
        return this.reservations;
    }

    public ArrayList<String[]> getLignesIncorrectesActivites() {
        return this.lignesIncorrectesActivites;
    }

    public ArrayList<String[]> getLignesIncorrectesReservations() {
        return this.lignesIncorrectesReservations;
    }

    public ArrayList<String[]> getLignesIncorrectesSalles() {
        return this.lignesIncorrectesSalles;
    }

    public ArrayList<String[]> getLignesIncorrectesUtilisateurs() {
        return this.lignesIncorrectesUtilisateurs;
    }

}