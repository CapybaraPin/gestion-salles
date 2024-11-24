/*
 * Filtre.java 19/11/2024
 * IUT de RODEZ, tous les droits sont réservés
 */

package iut.info2.saltistique.modele;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.List;

/**
 * La classe {@code Filtre} permet de gérer les filtres appliqués aux réservations,
 * permettant de restreindre les résultats affichés en fonction de critères spécifiques :
 * - salles
 * - employés
 * - activités
 * - périodes de dates
 * <p>
 * Cette classe offre des méthodes pour ajouter, supprimer et appliquer des filtres,
 * en garantissant que seuls les résultats correspondant aux critères définis sont affichés.
 * </p>
 * @author Jules VIALAS
 */
public class Filtre {

    /** Liste des salles actuellement filtrées. */
    private final ObservableList<Salle> sallesFiltres;

    /** Liste des employés (utilisateurs) actuellement filtrés. */
    private final ObservableList<Utilisateur> employesFiltres;

    /** Liste des activités actuellement filtrées. */
    private final ObservableList<Activite> activitesFiltres;

    /** Date de début qui permet de filtrer par périodes */
    private LocalDateTime dateDebutFiltre;

    /** Date de fin qui permet de filtrer par périodes */
    private LocalDateTime dateFinFiltre;

    /** Initialise les listes de filtres. */
    public Filtre() {
        this.sallesFiltres = FXCollections.observableArrayList();
        this.employesFiltres = FXCollections.observableArrayList();
        this.activitesFiltres = FXCollections.observableArrayList();
    }

    /**
     * Ajoute une salle aux filtres.
     * La salle sera ajoutée si elle n'est pas déjà présente dans les filtres.
     * @param salle la salle a ajouté aux filtres.
     */
    public void ajouterFiltreSalle(Salle salle) {
        if (salle != null && !sallesFiltres.contains(salle)) {
            sallesFiltres.add(salle);
        }
    }

    /**
     * Supprime une salle des filtres.
     * La salle sera retirée des filtres si elle y est présente.
     * @param salle la salle a retiré des filtres.
     */
    public void supprimerFiltreSalle(Salle salle) {
        sallesFiltres.remove(salle);
    }

    /**
     * Récupère la liste des salles actuellement filtrées.
     *
     * @return la liste des salles filtrées.
     */
    public ObservableList<Salle> getSallesFiltrees() {
        return sallesFiltres;
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
     * Supprime un employé des filtres.
     * L'employé sera retiré des filtres s'il y est présent.
     *
     * @param employe l'employé à retirer des filtres.
     */
    public void supprimerFiltreEmploye(Utilisateur employe) {
        employesFiltres.remove(employe);
    }

    /**
     * Récupère la liste des employés actuellement filtrés.
     *
     * @return la liste des employés filtrés.
     */
    public ObservableList<Utilisateur> getEmployesFiltres() {
        return employesFiltres;
    }

    /**
     * Ajoute une activité aux filtres.
     * L'activité sera ajoutée si elle n'est pas déjà présente dans les filtres.
     * @param activite l'activité à ajouter aux filtres.
     */
    public void ajouterFiltreActivite(Activite activite) {
        if (activite != null && !activitesFiltres.contains(activite)) {
            activitesFiltres.add(activite);
        }
    }

    /**
     * Supprime une activité des filtres.
     * L'activité sera retirée des filtres si elle y est présente.
     * @param activite l'activité à retirer des filtres.
     */
    public void supprimerFiltreActivite(Activite activite) {
        activitesFiltres.remove(activite);
    }

    /**
     * Récupère la liste des activités actuellement filtrées.
     * @return la liste des activités filtrées.
     */
    public ObservableList<Activite> getActivitesFiltrees() {
        return activitesFiltres;
    }

    /**
     * Ajoute un filtre de réservation basé sur une période définie par une date et une heure de début et de fin.
     * <p>
     * Cette méthode met à jour les propriétés de filtre pour les réservations en fonction
     * des valeurs passées en paramètres, représentant la période sélectionnée.
     * Les filtres sont appliqués uniquement si les deux paramètres ne sont pas nuls.
     * </p>
     * @param debut La date et heure de début du filtre (ne peut pas être {@code null}).
     * @param fin La date et heure de fin du filtre (ne peut pas être {@code null}).
     */
    public void ajouterFiltreDate(LocalDateTime debut, LocalDateTime fin) {
        if (debut != null && fin != null) {
            // Crée les objets LocalDateTime avec date et heure
            this.dateDebutFiltre = debut;
            this.dateFinFiltre = fin;
        }
    }

    /**
     * Récupère la période de dates actuellement définie comme filtre.
     * <p>
     * Cette méthode retourne un tableau contenant la date et heure de début
     * et de fin du filtre appliqué. Si aucune période n'est définie, les valeurs
     * retournées dans le tableau seront {@code null}.
     * </p>
     * @return Un tableau de deux éléments {@code LocalDateTime}, où :
     *         <ul>
     *             <li>Le premier élément correspond à la date et heure de début du filtre.</li>
     *             <li>Le second élément correspond à la date et heure de fin du filtre.</li>
     *         </ul>
     *         Si aucune période n'est définie, les deux éléments seront {@code null}.
     */
    public LocalDateTime[] getFiltreDate() {
        return new LocalDateTime[]{dateDebutFiltre, dateFinFiltre};
    }


    /**
     * Supprime le filtre de période de dates.
     */
    public void supprimerFiltreDate() {
        this.dateDebutFiltre = null;
        this.dateFinFiltre = null;
    }

    /**
     * Applique les filtres aux réservations données.
     * Cette méthode prend en compte les périodes de dates (avec heures et minutes) ainsi que les autres critères.
     * @param reservations la liste des réservations à filtrer.
     * @return une liste observable contenant uniquement les réservations qui correspondent aux critères définis.
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
        if (dateDebutFiltre != null && dateFinFiltre != null) {
            resultatsFiltres.removeIf(reservation -> reservation.getDateDebut().isBefore(dateDebutFiltre) ||
                    reservation.getDateDebut().isAfter(dateFinFiltre));
        }
        return resultatsFiltres;
    }
}
