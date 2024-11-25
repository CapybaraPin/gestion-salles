/*
 * GestionDonnees.java              21/10/2024
 * Tom GUTIERREZ Jules VIALAS, pas de copyrights
 */

package iut.info2.saltistique.modele.donnees;

import iut.info2.saltistique.modele.*;


import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
     * Calcule le pourcentage d'utilisation des salles en fonction des réservations,
     * et normalise le total des pourcentages pour qu'il ne dépasse pas 100%.
     *
     * @param salles la liste des salles à analyser
     * @param reservations la liste des réservations associées
     * @return une map des salles et leur pourcentage d'utilisation normalisé
     */
    public Map<Salle, Double> calculerPourcentageReservation(List<Salle> salles, HashMap<Integer, Reservation> reservations) {
        // Calculer le temps total réservé pour chaque salle
        Map<Salle, Double> pourcentages = new HashMap<>();

        // Première étape : calculer le pourcentage d'utilisation pour chaque salle
        for (Salle salle : salles) {
            // Calculer le temps total réservé pour cette salle (en heures)
            long tempsTotalReserve = reservations.values().stream()
                    .filter(reservation -> reservation.getSalle().equals(salle)) // Filtrer les réservations pour cette salle
                    .mapToLong(reservation -> reservation.getTempsTotalReservation()) // Récupérer la durée de chaque réservation
                    .sum(); // Additionner toutes les durées des réservations

            // Calculer la capacité de la salle en heures (par exemple, 24 heures par jour)
            long heuresDisponibles = salle.getCapacite(); // Ou la capacité peut être calculée autrement si nécessaire

            // Calculer le pourcentage d'utilisation
            double pourcentage = (tempsTotalReserve * 100.0) / heuresDisponibles;
            pourcentages.put(salle, pourcentage);
        }

        // Deuxième étape : Normaliser les pourcentages pour que la somme soit égale à 100%
        double totalUtilisation = pourcentages.values().stream().mapToDouble(Double::doubleValue).sum();

        // Si le total d'utilisation est supérieur à 0, on ajuste les pourcentages
        if (totalUtilisation > 0) {
            // Normaliser chaque pourcentage pour qu'ils somment à 100%
            for (Salle salle : pourcentages.keySet()) {
                double pourcentageNormalise = (pourcentages.get(salle) * 100) / totalUtilisation;
                pourcentages.put(salle, pourcentageNormalise);
            }
        }

        return pourcentages;
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
