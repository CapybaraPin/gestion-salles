/*
 * ControleurConsultationSalle.java 20/10/2024
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique.controleur;

import iut.info2.saltistique.modele.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.util.List;

/**
 * Contrôleur de la vue de consultation des salles.
 */
public class ControleurConsultationSalle extends ControleurFiltres {

    /** Label indiquant le nom de la salle */
    @FXML
    public Label nomSalle;

    /** Label indiquant la durée totale des réservations de la salle */
    @FXML
    public Label dureeReservation;

    /** Label indiquant la durée moyenne des réservations de la salle */
    @FXML
    public Label dureeMoyenneReservationJour;

    /** Label indiquant la durée moyenne des réservations de la salle */
    @FXML
    public Label dureeMoyenneReservationSemaine;

    /** Filtre actuellement appliqué */
    private Filtre filtre;

    /** Salle actuellement sélectionnée */
    private Salle salleSelectionnee;

    /** Listes observables contenant les objets de chaque type. */
    private ObservableList<Reservation> listeReservations;
    private ObservableList<Utilisateur> listeEmployes;
    private ObservableList<Activite> listeActivites;
    private ObservableList<Salle> listeSalles;

    /**
     * Initialise le contrôleur après le chargement de la vue FXML.
     */
    @FXML
    public void initialize() {
        listeReservations = FXCollections.observableArrayList();
        listeEmployes = FXCollections.observableArrayList();
        listeActivites = FXCollections.observableArrayList();
        listeSalles = FXCollections.observableArrayList();

        filtre = new Filtre();
        setFiltre(filtre);
        initialiserFiltres();

        Filtres.setItems(FXCollections.observableArrayList("Activité", "Employé", "Période"));
        Filtres.getSelectionModel().selectFirst();
    }

    /**
     * Met à jour les listes observables contenant les objets de chaque type.
     * @param listeReservations Liste des réservations.
     * @param listeActivites Liste des activités.
     * @param listeEmployes Liste des employés.
     * @param listeSalles Liste des salles.
     */
     public void setDonneesFiltres(ObservableList<Reservation> listeReservations,
                            ObservableList<Activite> listeActivites,
                            ObservableList<Utilisateur> listeEmployes,
                            ObservableList<Salle> listeSalles) {

        this.listeReservations = listeReservations;
        this.listeActivites = listeActivites;
        this.listeEmployes = listeEmployes;
        this.listeSalles = listeSalles;
    }

    /**
     * Met à jour la salle sélectionnée.
     *
     * @param salle La salle sélectionnée.
     */
    public void setSalle(Salle salle) {
        this.salleSelectionnee = salle;
    }

    /**
     * Actualise les statistiques affichées en fonction des données filtrées.
     */
    public void actualiserStats() {
        List<Reservation> reservationsFiltrees;
        long tempsTotal;
        long tempsMoyenJour;
        long tempsMoyenSemaine;
        actualiserFiltres();

        if (salleSelectionnee != null) {
            // Ajouter le filtre de salle
            filtre.ajouterFiltreSalle(salleSelectionnee);

            // Appliquer les filtres
            reservationsFiltrees = filtre.appliquerFiltres(listeReservations);

            // Mettre à jour les données de l'interface
            nomSalle.setText(salleSelectionnee.getNom());

            tempsTotal = salleSelectionnee.getTempsTotalReservations(reservationsFiltrees);
            tempsMoyenJour = salleSelectionnee.getTempsMoyenReservationsJour(reservationsFiltrees);
            tempsMoyenSemaine = salleSelectionnee.getTempsMoyenReservationsSemaine(reservationsFiltrees);

            dureeReservation.setText((int) tempsTotal / 60 + "h " + (int) tempsTotal % 60 + "min");
            dureeMoyenneReservationJour.setText((int) tempsMoyenJour / 60 + "h " + (int) tempsMoyenJour % 60 + "min par jour");
            dureeMoyenneReservationSemaine.setText((int) tempsMoyenSemaine / 60 + "h " + (int) tempsMoyenSemaine % 60 + "min par semaine");
        }
    }
    /**
     * Permet l'actualisation des données dans le contrôleur
     * de gestion des filtres.
     */
    void actualiserFiltres() {
        setListeSalles(listeSalles);
        setListeActivites(listeActivites);
        setListeEmployes(listeEmployes);
        setListeReservations(listeReservations);
    }

    /**
     * Appliqué lors du clic sur "Filtrer", réactualise les données en fonction du filtre.
     */
    @FXML
    void clickFiltrerSalle() {
        creationFiltres();
        actualiserStats();
    }
}