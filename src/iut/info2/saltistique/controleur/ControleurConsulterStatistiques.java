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
public class ControleurConsulterStatistiques extends ControleurFiltres {

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

    /** Liste contenant les données à afficher */
    ObservableList<Map<String, String>> listeDonnees;

    /** Liste des réservations utilisées pour les calculs */
    ObservableList<Reservation> listeReservations;

    @FXML
    void initialize() {
        setHoverEffect();
        remplirListeDonnees();
        initialiserTableauStatistiques();
    }

    void initialiserTableauStatistiques() {
        identifiantSalle.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("identifiant")));
        nomSalle.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("nom")));
        tempsMoyenOccupationJour.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("tempsMoyenOccupationJour")));
        tempsMoyenOccupationSemaine.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("tempsMoyenOccupationSemaine")));
        tempsOccupationTotal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get("tempsOccupationTotal")));
        tableauStatistiquesGlobales.setItems(listeDonnees);
    }

    private void remplirListeDonnees() {
        listeDonnees = FXCollections.observableArrayList();
        for (Salle salle : Saltistique.gestionDonnees.getSalles().values()) {
            try {
                Map<String, String> statistiqueSalle = new HashMap<>();
                statistiqueSalle.put("identifiant", salle.getIdentifiant());
                statistiqueSalle.put("nom", salle.getNom());
                statistiqueSalle.put("tempsMoyenOccupationJour", String.valueOf(salle.getTempsMoyenReservationsJour(listeReservations)));
                statistiqueSalle.put("tempsMoyenOccupationSemaine", String.valueOf(salle.getTempsMoyenReservationsSemaine(listeReservations)));
                statistiqueSalle.put("tempsOccupationTotal", String.valueOf(salle.getTempsTotalReservations(listeReservations)));

                listeDonnees.add(statistiqueSalle);
            } catch (Exception e) {
                System.err.println("Erreur lors du traitement de la salle : " + e.getMessage());
            }
        }
    }

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
}