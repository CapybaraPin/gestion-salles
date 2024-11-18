/*
 * Filtre.java 02/11/2024
 * IUT de RODEZ, tous les droits sont réservés
 *
 * @author Jules VIALAS
 */

package iut.info2.saltistique.modele;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * La classe {@code Filtre} permet de gérer les filtres appliqués aux réservations,
 * permettant de restreindre les résultats affichés en fonction de critères spécifiques tels que les salles,
 * les employés, les activités et les dates.
 * <p>
 * Cette classe offre des méthodes pour ajouter, supprimer et appliquer des filtres sur des réservations,
 * et elle permet de vérifier si certaines données sont filtrées.
 * </p>
 */
public class Filtre {

    /**
     * Liste des salles actuellement filtrées.
     * Cette liste contient les salles qui sont prises en compte dans le filtrage des réservations.
     */
    private final ObservableList<Salle> sallesFiltres;

    /**
     * Liste des employés (utilisateurs) actuellement filtrés.
     * Cette liste contient les employés qui sont pris en compte dans le filtrage des réservations.
     */
    private final ObservableList<Utilisateur> employesFiltres;

    /**
     * Liste des activités actuellement filtrées.
     * Cette liste contient les activités qui sont prises en compte dans le filtrage des réservations.
     */
    private final ObservableList<Activite> activitesFiltres;

    /**
     * Liste des dates actuellement filtrées.
     * Cette liste contient les dates qui sont prises en compte dans le filtrage des réservations.
     */
    private final List<LocalDate> datesFiltrees;

    /**
     * Constructeur par défaut qui initialise les listes de filtres.
     * Il crée des listes vides pour les salles, les employés, les activités et les dates filtrées.
     */
    public Filtre() {
        this.sallesFiltres = FXCollections.observableArrayList();
        this.employesFiltres = FXCollections.observableArrayList();
        this.activitesFiltres = FXCollections.observableArrayList();
        this.datesFiltrees = new ArrayList<>();
    }

    /**
     * Ajoute une salle aux filtres.
     * La salle sera ajoutée si elle n'est pas déjà présente dans les filtres.
     *
     * @param salle la salle a ajouté aux filtres.
     */
    public void ajouterFiltreSalle(Salle salle) {
        if (salle != null && !sallesFiltres.contains(salle)) {
            sallesFiltres.add(salle);
        }
    }

    /**
     * Ajoute un employé aux filtres.
     * L'employé sera ajouté s'il n'est pas déjà présent dans les filtres.
     *
     * @param employe l'employé à ajouter aux filtres.
     */
    public void ajouterFiltreEmploye(Utilisateur employe) {
        if (employe != null && !employesFiltres.contains(employe)) {
            employesFiltres.add(employe);
        }
    }

    /**
     * Ajoute une activité aux filtres.
     * L'activité sera ajoutée si elle n'est pas déjà présente dans les filtres.
     *
     * @param activite l'activité a ajouté aux filtres.
     */
    public void ajouterFiltreActivite(Activite activite) {
        if (activite != null && !activitesFiltres.contains(activite)) {
            activitesFiltres.add(activite);
        }
    }

    /**
     * Supprime une salle des filtres.
     * La salle sera retirée des filtres si elle y est présente.
     *
     * @param salle la salle a retiré des filtres.
     */
    public void supprimerFiltreSalle(Salle salle) {
        sallesFiltres.remove(salle);
    }

    /**
     * Supprime un employé des filtres.
     * L'employé sera retiré des filtres s'il y est présent.
     *
     * @param employe l'employé à retirer des filtres.
     */
    public void supprimerFiltreEmploye(Utilisateur employe) {
        employesFiltres.remove(employe);
    }

    /**
     * Supprime une activité des filtres.
     * L'activité sera retirée des filtres si elle y est présente.
     *
     * @param activite l'activité a retiré des filtres.
     */
    public void supprimerFiltreActivite(Activite activite) {
        activitesFiltres.remove(activite);
    }

    /**
     * Applique les filtres aux réservations données.
     * Cette méthode parcourt la liste des réservations et retourne une nouvelle liste contenant uniquement
     * celles qui respectent les critères définis par les filtres.
     *
     * @param reservations la liste des réservations à filtrer.
     * @return une liste observable contenant uniquement les réservations qui correspondent aux critères
     *         définis par les filtres (salles, employés, activités et dates).
     */
    public ObservableList<Reservation> appliquerFiltres(List<Reservation> reservations) {
        ObservableList<Reservation> resultatsFiltres = FXCollections.observableArrayList(reservations);
        if (!sallesFiltres.isEmpty()) {
            resultatsFiltres.removeIf(reservation -> !sallesFiltres.contains(reservation.getSalle()));
        }
        if (!employesFiltres.isEmpty()) {
            resultatsFiltres.removeIf(reservation -> !employesFiltres.contains(reservation.getUtilisateur()));
        }
        if (!activitesFiltres.isEmpty()) {
            resultatsFiltres.removeIf(reservation -> !activitesFiltres.contains(reservation.getActivite()));
        }
        return resultatsFiltres;
    }

    /**
     * Récupère la liste des salles actuellement filtrées.
     * Cette méthode retourne une liste observable contenant toutes les salles filtrées.
     *
     * @return la liste des salles actuellement filtrées.
     */
    public ObservableList<Salle> getSallesFiltrees() {
        return sallesFiltres;
    }

    /**
     * Récupère la liste des employés actuellement filtrés.
     * Cette méthode retourne une liste observable contenant tous les employés filtrés.
     *
     * @return la liste des employés actuellement filtrés.
     */
    public ObservableList<Utilisateur> getEmployesFiltres() {
        return employesFiltres;
    }

    /**
     * Récupère la liste des activités actuellement filtrées.
     * Cette méthode retourne une liste observable contenant toutes les activités filtrées.
     *
     * @return la liste des activités actuellement filtrées.
     */
    public ObservableList<Activite> getActivitesFiltrees() {
        return activitesFiltres;
    }

    /**
     * Ajoute une date aux filtres.
     * La date sera ajoutée si elle n'est pas déjà présente dans les filtres.
     *
     * @param date la date a ajouté aux filtres.
     */
    public void ajouterFiltreDate(LocalDate date) {
        if (!datesFiltrees.contains(date)) {
            datesFiltrees.add(date);
        }
    }

    /**
     * Supprime une date des filtres.
     * La date sera retirée des filtres si elle y est présente.
     *
     * @param date la date a retiré des filtres.
     */
    public void supprimerFiltreDate(LocalDate date) {
        datesFiltrees.remove(date);
    }

    /**
     * Récupère la liste des dates actuellement filtrées.
     * Cette méthode retourne la liste des dates filtrées.
     *
     * @return la liste des dates actuellement filtrées.
     */
    public List<LocalDate> getDatesFiltrees() {
        return datesFiltrees;
    }

    /**
     * Vérifie si une date est actuellement filtrée.
     * Cette méthode retourne {@code true} si la date est présente dans les filtres,
     * sinon elle retourne {@code false}.
     *
     * @param date la date a vérifié.
     * @return {@code true} si la date est filtrée, {@code false} sinon.
     */
    public boolean estDateFiltree(LocalDate date) {
        return datesFiltrees.contains(date);
    }

}
