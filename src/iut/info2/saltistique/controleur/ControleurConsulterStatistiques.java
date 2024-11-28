/*
 * ControleurConsulterStatistiques.java 20/10/2024
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique.controleur;
import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.modele.Reservation;
import iut.info2.saltistique.modele.Salle;
import iut.info2.saltistique.modele.Scenes;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.shape.Line;

import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur de la vue de consultation des statistiques.
 * Permet de consulter les statistiques globales et les salles non réservées.
 */
public class ControleurConsulterStatistiques extends Controleur {

    /** Ligne de sélection des statistiques globales */
    @FXML
    public Line selectionStatistiquesGlobales;
    /** Ligne de sélection des salles non réservées */
    @FXML
    public Line selectionSallesNonreservees;
    /** Tableau représentatif des statistiques globales */
    @FXML
    public TableView<Map<String, String>> tableauStatistiquesGlobales;
    /** Colonne pour l'identifiant de la salle */
    @FXML
    public TableColumn<Map<String, String>, String> identifiantSalle;
    /** Nom de la salle */
    @FXML
    public TableColumn<Map<String, String>, String> nomSalle;
    /** Colonne pour le temps moyen d'occupation par jour */
    @FXML
    public TableColumn<Map<String, String>, String> tempsMoyenOccupationJour;
    /** Colonne pour le temps moyen d'occupation par semaine */
    @FXML
    public TableColumn<Map<String, String>, String> tempsMoyenOccupationSemaine;
    /** Colonne pour le temps d'occupation total */
    @FXML
    public TableColumn<Map<String, String>, String> tempsOccupationTotal;
    /** Colonne pour le pourcentage d'utilisation */
    @FXML
    public TableColumn<Map<String, String>, String> pourcentageUtilisation;

    /** Liste contenant les données à afficher */
    ObservableList<Map<String, String>> listeDonnees;

    /** Liste des réservations utilisées pour les calculs */
    ObservableList<Reservation> listeReservations;

    @FXML
    void initialize() {
        setHoverEffect();
        initialiserTableauStatistiques();
    }

    void initialiserTableauStatistiques() {
        identifiantSalle.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("identifiant")));
        nomSalle.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("nom")));
        tempsMoyenOccupationJour.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("tempsMoyenOccupationJour")));
        tempsMoyenOccupationSemaine.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("tempsMoyenOccupationSemaine")));
        tempsOccupationTotal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("tempsOccupationTotal")));
        pourcentageUtilisation.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("pourcentageUtilisation")));

        tableauStatistiquesGlobales.setItems(listeDonnees);
    }

    /**
     * Remplit la liste des données à afficher dans le tableau.
     * Calcule les statistiques pour chaque salle. Les données sont stockées dans une liste de maps.
     *
     */
    private void remplirListeDonnees() {
        listeDonnees = FXCollections.observableArrayList();
        long tempsMoyenOccupationJour;
        long tempsMoyenOccupationSemaine;
        long tempsOccupationTotal;
        double pourcentageUtilisation;

        // Pour chaque salle, on calcule les statistiques et on les ajoute à la liste
        for (Salle salle : Saltistique.gestionDonnees.getSalles().values()) {
            try {
                tempsOccupationTotal = salle.getTempsTotalReservations(listeReservations);
                tempsMoyenOccupationJour = salle.getTempsMoyenReservationsJour(listeReservations);
                tempsMoyenOccupationSemaine = salle.getTempsMoyenReservationsSemaine(listeReservations);
                pourcentageUtilisation = salle.getPourcentageUtilisation(listeReservations);
                Map<String, String> statistiqueSalle = new HashMap<>();
                statistiqueSalle.put("identifiant", salle.getIdentifiant());
                statistiqueSalle.put("nom", salle.getNom());
                statistiqueSalle.put("tempsMoyenOccupationJour", (int) tempsMoyenOccupationJour / 60 + "h " + (int) tempsMoyenOccupationJour % 60 + "min");
                statistiqueSalle.put("tempsMoyenOccupationSemaine", (int) tempsMoyenOccupationSemaine / 60 + "h " + (int) tempsMoyenOccupationSemaine % 60 + "min");
                statistiqueSalle.put("tempsOccupationTotal", (int) tempsOccupationTotal / 60 + "h " + (int) tempsOccupationTotal % 60 + "min");
                statistiqueSalle.put("pourcentageUtilisation", String.format("%.2f", pourcentageUtilisation) + "%");

                listeDonnees.add(statistiqueSalle);
            } catch (Exception e) {
                System.err.println("Erreur lors du traitement de la salle : " + e.getMessage());
            }
        }
    }

    /**
     * Rafraîchit le tableau des statistiques.
     * Utilisé pour mettre à jour les données après une modification.
     *
     */
    void rafraichirTableauStatistiques() {
        listeReservations = FXCollections.observableArrayList();
        listeReservations.addAll(Saltistique.gestionDonnees.getReservations().values());
        remplirListeDonnees();
        tableauStatistiquesGlobales.setItems(listeDonnees);
    }

    /**
     * Passe sur l'affichage des salles non réservées
     */
    @FXML
    void afficherSallesNonReservees() {
        Saltistique.changeScene(Scenes.SALLES_NON_RESERVEES);
    }

    /**
     * Retour au menu principal.
     */
    @Override
    @FXML
    public void handlerRetourMenu() {
        Saltistique.changeScene(Scenes.CONSULTER_DONNEES);
        menuClick();
    }

    /**
     * Affiche la consultation de données.
     */
    public void afficherConsultationDonnees() {
        Saltistique.changeScene(Scenes.CONSULTER_DONNEES);
    }

}