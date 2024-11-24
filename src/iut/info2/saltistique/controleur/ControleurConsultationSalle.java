/*
 * ControleurConsultationSalle.java 20/10/2024
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique.controleur;

import iut.info2.saltistique.modele.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrôleur de la vue de consultation des salles.
 */
public class ControleurConsultationSalle extends Controleur {

    /** Label indiquant le nom de la salle */
    @FXML
    public Label nomSalle;

    /** Label indiquant la durée totale des réservations de la salle */
    @FXML
    public Label dureeReservation;

    /** Label indiquant la durée moyenne des réservations de la salle */
    @FXML
    public Label dureeMoyenneReservation;

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
        actualiserFiltres();

        if (salleSelectionnee != null) {
            // Filtrer les réservations pour la salle sélectionnée
            List<Reservation> reservationsFiltrees = listeReservations.stream()
                    .filter(reservation -> reservation.getSalle().equals(salleSelectionnee))  // Filtrage par salle
                    .collect(Collectors.toList());

            // Appliquer les filtres
            reservationsFiltrees = filtre.appliquerFiltres(reservationsFiltrees);
            System.out.println(reservationsFiltrees);

            // Mettre à jour les données de l'interface
            nomSalle.setText(salleSelectionnee.getNom());
            dureeReservation.setText(salleSelectionnee.getTempsTotalReservations(reservationsFiltrees));
            dureeMoyenneReservation.setText(salleSelectionnee.getTempsMoyenReservations(reservationsFiltrees) + " par jours");
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
    void clickFiltrer() {
        creationFiltres();
        actualiserStats();
    }
}