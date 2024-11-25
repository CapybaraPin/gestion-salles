/*
 * ControleurConsulterStatistiques.java 20/10/2024
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique.controleur;
import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.modele.Reservation;
import iut.info2.saltistique.modele.Salle;
import iut.info2.saltistique.modele.SalleStatistiques;
import iut.info2.saltistique.modele.Scenes;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Line;

import java.util.Map;

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

    /** Ligne de selection des statistiques globales */
    @FXML
    public Line selectionStatistiquesGlobales;
    /** Ligne de selection des salles non réservées */
    @FXML
    public Line selectionSallesNonreservees;
    /** Tableau représentatif des statistiques globales */
    @FXML
    public TableView<SalleStatistiques> tableauStatistiquesGlobales;
    /** Colonne pour l'identifiant de la salle. */
    @FXML
    public TableColumn<SalleStatistiques, String> identifiantSalle;
    /** Nom de la salle */
    @FXML
    public TableColumn<SalleStatistiques, String> nomSalle;
    /** Colonne pour le temps moyen d'occupation par jour. */
    @FXML
    public TableColumn<SalleStatistiques, String> tempsMoyenOccupationJour;
    /** Colonne pour le temps moyen d'occupation par semaine. */
    @FXML
    public TableColumn<SalleStatistiques, String> tempsMoyenOccupationSemaine;
    /** Colonne pour le temps d'occupation total. */
    @FXML
    public TableColumn<SalleStatistiques, String> tempsOccupationTotal;

    /** Liste contenante les données à afficher */
    ObservableList<SalleStatistiques> listeDonnees;

    /** Liste contenant les données de réservations */
    ObservableList<Reservation> listeReservations;

    @FXML
    public BorderPane borderPane;


    @FXML
    void initialize() {
        setHoverEffect();
        initialiserTableauStatistiques();
    }
  
    @FXML
    void clickFiltrer() {
        actualiserFiltres();
        creationFiltres();
        borderPane.setCenter(buildPieChart());
    }

    void initialiserGraphiqueSalles(){
        borderPane.setCenter(buildPieChart());
    }

    void initialiserTableauStatistiques() {
        identifiantSalle.setCellValueFactory(new PropertyValueFactory<>("identifiant"));
        nomSalle.setCellValueFactory(new PropertyValueFactory<>("nom"));
        tempsMoyenOccupationJour.setCellValueFactory(new PropertyValueFactory<>("tempsMoyenOccupationJour"));
        tempsMoyenOccupationSemaine.setCellValueFactory(new PropertyValueFactory<>("tempsMoyenOccupationSemaine"));
        tempsOccupationTotal.setCellValueFactory(new PropertyValueFactory<>("tempsOccupationTotal"));
        tableauStatistiquesGlobales.setItems(listeDonnees);
    }

    private void remplirListeDonnees() {
        listeDonnees = FXCollections.observableArrayList();
        for (int rang = 0; rang < Saltistique.gestionDonnees.getSalles().size(); rang++) {
            try {
                listeDonnees.add(new SalleStatistiques(
                        Saltistique.gestionDonnees.getSalles().get(rang).getIdentifiant(),
                        Saltistique.gestionDonnees.getSalles().get(rang).getNom(),
                        Saltistique.gestionDonnees.getSalles().get(rang).getTempsMoyenReservationsJour(listeReservations),
                        Saltistique.gestionDonnees.getSalles().get(rang).getTempsMoyenReservationsSemaine(listeReservations),
                        Saltistique.gestionDonnees.getSalles().get(rang).getTempsTotalReservations(listeReservations)
                ));
                System.out.println(Saltistique.gestionDonnees.getSalles().get(rang).getTempsMoyenReservationsJour(listeReservations));
            } catch (Exception e) {
                System.err.println("Erreur lors du traitement de la salle : " + e.getMessage());
            }
        }
    }

    void rafraichirTableauStatistiques() {
        listeReservations = FXCollections.observableArrayList();
        for (Map.Entry<Integer, Reservation> entry : Saltistique.gestionDonnees.getReservations().entrySet()) {
            listeReservations.add(entry.getValue());
        }
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
