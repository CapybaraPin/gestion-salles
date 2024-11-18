package iut.info2.saltistique.modele;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

/**
 * Classe permettant de gérer les filtres appliqués aux réservations.
 * <p>
 * Cette classe offre des fonctionnalités pour ajouter, supprimer et appliquer
 * des filtres sur des salles, employés, et activités. Les filtres sont utilisés
 * pour restreindre les résultats affichés en fonction de critères spécifiques.
 * </p>
 */
public class Filtre {

    /**
     * Liste des salles actuellement filtrées.
     */
    private final ObservableList<Salle> sallesFiltres;

    /**
     * Liste des employés (utilisateurs) actuellement filtrés.
     */
    private final ObservableList<Utilisateur> employesFiltres;

    /**
     * Liste des activités actuellement filtrées.
     */
    private final ObservableList<Activite> activitesFiltres;

    /**
     * Constructeur par défaut qui initialise les listes de filtres.
     */
    public Filtre() {
        this.sallesFiltres = FXCollections.observableArrayList();
        this.employesFiltres = FXCollections.observableArrayList();
        this.activitesFiltres = FXCollections.observableArrayList();
    }

    /**
     * Ajoute une salle aux filtres.
     *
     * @param salle la salle à ajouter. Si la salle est déjà filtrée, elle n'est pas ajoutée.
     */
    public void ajouterFiltreSalle(Salle salle) {
        if (salle != null && !sallesFiltres.contains(salle)) {
            sallesFiltres.add(salle);
        }
    }

    /**
     * Ajoute un employé aux filtres.
     *
     * @param employe l'employé (utilisateur) à ajouter. Si l'employé est déjà filtré, il n'est pas ajouté.
     */
    public void ajouterFiltreEmploye(Utilisateur employe) {
        if (employe != null && !employesFiltres.contains(employe)) {
            employesFiltres.add(employe);
        }
    }

    /**
     * Ajoute une activité aux filtres.
     *
     * @param activite l'activité à ajouter. Si l'activité est déjà filtrée, elle n'est pas ajoutée.
     */
    public void ajouterFiltreActivite(Activite activite) {
        if (activite != null && !activitesFiltres.contains(activite)) {
            activitesFiltres.add(activite);
        }
    }

    /**
     * Supprime une salle des filtres.
     *
     * @param salle la salle à retirer des filtres.
     */
    public void supprimerFiltreSalle(Salle salle) {
        sallesFiltres.remove(salle);
    }

    /**
     * Supprime un employé des filtres.
     *
     * @param employe l'employé (utilisateur) à retirer des filtres.
     */
    public void supprimerFiltreEmploye(Utilisateur employe) {
        employesFiltres.remove(employe);
    }

    /**
     * Supprime une activité des filtres.
     *
     * @param activite l'activité à retirer des filtres.
     */
    public void supprimerFiltreActivite(Activite activite) {
        activitesFiltres.remove(activite);
    }

    /**
     * Applique les filtres aux réservations données.
     *
     * @param reservations la liste des réservations à filtrer.
     * @return une liste observable contenant uniquement les réservations qui correspondent
     *         aux critères définis par les filtres.
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
     * Récupère la liste des salles filtrées.
     *
     * @return une liste observable des salles actuellement dans les filtres.
     */
    public ObservableList<Salle> getSallesFiltrees() {
        return sallesFiltres;
    }

    /**
     * Récupère la liste des employés filtrés.
     *
     * @return une liste observable des employés actuellement dans les filtres.
     */
    public ObservableList<Utilisateur> getEmployesFiltres() {
        return employesFiltres;
    }

    /**
     * Récupère la liste des activités filtrées.
     *
     * @return une liste observable des activités actuellement dans les filtres.
     */
    public ObservableList<Activite> getActivitesFiltrees() {
        return activitesFiltres;
    }
}
