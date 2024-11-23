/*
 * ControleurConsulterStatistiques.java 20/10/2024
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique.controleur;
import iut.info2.saltistique.modele.Salle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.shape.Line;

/**
 * Contrôleur de la vue de consultation des statistiques.
 * Permet de consulter les statistiques globales et les salles non réservées.
 */
public class ControleurConsulterStatistiques extends Controleur {

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

    /**
     * Action associée au bouton filtrer pour filtrer les données en fonction du filtre demandé
     */
    @FXML
    void clickFiltrer() {
        actualiserFiltres();
        creationFiltres();
    }

    /**
     * Permet d'actualiser l'affichage des données en fonction des filtres appliqués
     */
    void actualiserFiltres() {
        setListeSalles(listeSalles);
    }

    public void afficherStatistiquesGlobales(ActionEvent actionEvent) {
    }

    public void afficherSallesNonReservees(ActionEvent actionEvent) {
    }
}
