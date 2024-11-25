/*
 * ControleurConsulterStatistiques.java 20/10/2024
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique.controleur;
import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.modele.Reservation;
import iut.info2.saltistique.modele.Salle;
import iut.info2.saltistique.modele.Scenes;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.util.HashMap;

/**
 * Contrôleur de la vue de consultation des statistiques.
 * Permet de consulter les statistiques globales et les salles non réservées.
 */
public class ControleurConsulterStatistiques extends Controleur {

    /** Section Statistiques Globales */
    @FXML
    public VBox sectionStatistiquesGlobales;

    /** Section Salles Non Réservées */
    @FXML
    public VBox sectionSallesNonReservees;

    /** Onglet Statistiques Globales */
    @FXML
    public Button statistiquesGlobales;

    /** Onglet salles non réservées */
    @FXML
    public Button sallesNonReservees;

    /** Ligne de soulignage de l'onglet Statistiques Globales */
    @FXML
    public Line SelectionStatistiquesGlobales;

    /** Ligne de soulignage de l'onglet Statistiques Globales */
    @FXML
    public Line SelectionSallesNonReservees;

    /** Liste des salles */
    @FXML
    public ObservableList<Salle> listeSalles;

    @FXML
    public BorderPane borderPane;

    /**
     * Initialise le contrôleur.
     */
    @FXML
    public void initialize() {
        initialiserFiltres();
    }

    /**
     * Action associée au bouton filtrer pour filtrer les données en fonction du filtre demandé
     */
    @FXML
    void clickFiltrer() {
        actualiserFiltres();
        creationFiltres();
        borderPane.setCenter(buildPieChart());
    }

    /**
     * Action associée au bouton statistiques globales pour afficher les statistiques globales
     */
    void initialiserGraphiqueSalles(){
        borderPane.setCenter(buildPieChart());
    }

    /**
     * Permet d'actualiser l'affichage des données en fonction des filtres appliqués
     */
    void actualiserFiltres() {
        setListeSalles(listeSalles);
    }

    public void afficherStatistiquesGlobales() {
        masquerTousLesTableauxEtSelections();
        sectionStatistiquesGlobales.setVisible(true);
        SelectionStatistiquesGlobales.setVisible(true);
    }

    public void afficherSallesNonReservees() {
        Saltistique.changeScene(Scenes.SALLES_NON_RESERVEES);
    }

    /**
     * Masque toutes les sections de la vue.
     */
    private void masquerTousLesTableauxEtSelections() {
        sectionStatistiquesGlobales.setVisible(false);
        sectionSallesNonReservees.setVisible(false);
        SelectionStatistiquesGlobales.setVisible(false);
        SelectionSallesNonReservees.setVisible(false);
    }

    public PieChart buildPieChart() {
        // Récupérer la liste des salles
        HashMap<Integer, Salle> listeSallesMap = Saltistique.gestionDonnees.getSalles();
        ObservableList<Salle> listeSalles = FXCollections.observableArrayList(listeSallesMap.values());

        // Récupérer toutes les réservations sous forme de Map
        HashMap<Integer, Reservation> reservations = Saltistique.gestionDonnees.getReservations();

        // Préparer les données pour le graphique
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        listeSalles.forEach(salle -> {
            double pourcentageReservation = Saltistique.gestionDonnees.calculerPourcentageReservation(salle, reservations);
            if (pourcentageReservation > 0){
                pieChartData.add(new PieChart.Data(salle.getNom(), pourcentageReservation));
            }
        });

        // Créer un graphique en camembert
        PieChart pieChart = new PieChart(pieChartData);

        // Configurer le graphique
        pieChart.setTitle("Pourcentage d'utilisation des salles");
        pieChart.setClockwise(true);
        pieChart.setLabelLineLength(50);
        pieChart.setLabelsVisible(true);
        pieChart.setLegendVisible(true);
        pieChart.setStartAngle(180);

        // Lier les valeurs et les étiquettes pour chaque segment
        pieChartData.forEach(data ->
                data.nameProperty().bind(Bindings.concat(data.getName(), " ", String.format("%.2f", data.getPieValue()), " %"))
        );

        // Ajouter un menu contextuel pour changer de graphique
        ContextMenu contextMenu = new ContextMenu();
        MenuItem miSwitchToBarChart = new MenuItem("Changer en histogramme");
        contextMenu.getItems().add(miSwitchToBarChart);

        pieChart.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.SECONDARY) {
                            contextMenu.show(pieChart, event.getScreenX(), event.getScreenY());
                        }
                    }
                });

        return pieChart;
    }
}
