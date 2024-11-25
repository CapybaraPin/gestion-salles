/*
 * ControleurConsulterStatistiques.java 20/10/2024
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique.controleur;
import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.modele.Reservation;
import iut.info2.saltistique.modele.SalleStatistiques;
import iut.info2.saltistique.modele.Scenes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Line;

import java.util.Map;

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

    ObservableList<Reservation> listeReservations;

    @FXML
    void initialize() {
        setHoverEffect();
        remplirListeDonnees();
        initialiserTableauStatistiques();
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
}