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

    /**
     * Configure les informations spécifiques à la salle sélectionnée.
     *
     * @param salle             La salle à afficher.
     */
    public void setSalle(Salle salle, ObservableList<Reservation> listeReservations) {
        if (salle != null) {
            nomSalle.setText(salle.getNom());

            // Exemple : calculer et afficher des durées fictives
            dureeReservation.setText(salle.getTempsTotalReservations(listeReservations));
            dureeMoyenneReservation.setText(salle.getTempsMoyenReservations(listeReservations) + " par jours");
        }
    }

    Filtre filtre;

    /**
     * Listes observables contenant les objets de chaques types.
     * Ces listes sont utilisées pour afficher et gérer les types de données disponibles
     * dans l'application, permettant la liaison de données pour des composants
     * de l'interface utilisateur, tels que des tableaux ou des listes.
     */
    private ObservableList<Salle> listeSalles;
    private ObservableList<Activite> listeActivites;
    private ObservableList<Utilisateur> listeEmployes;
    private ObservableList<Reservation> listeReservations;

    /**
     * Initialise le contrôleur après le chargement de la vue FXML.
     * Configure les filtres, les listes observables, et initialise les tableaux.
     */
    public void initialize() {
        listeSalles = FXCollections.observableArrayList();
        listeActivites = FXCollections.observableArrayList();
        listeEmployes = FXCollections.observableArrayList();
        listeReservations = FXCollections.observableArrayList();

        // Initialisation des filtres
        filtre = new Filtre();
        setFiltre(filtre);
        actualiserFiltres();
        initialiserFiltres();
    }

    /**
         * Gère le clic sur le bouton "Filtrer".
         * Applique les filtres en fonction du critère et de la valeur sélectionnés.
         */
    @FXML
    void clickFiltrer() {
        actualiserFiltres();
        creationFiltres();
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
}