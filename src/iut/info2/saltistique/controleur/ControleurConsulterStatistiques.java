package iut.info2.saltistique.controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.shape.Line;

public class ControleurConsulterStatistiques extends Controleur{
    public Button statistiquesGlobales;
    public Button sallesNonReservees;
    public Line SelectionStatistiquesGlobales;
    public Line SelectionSallesNonReservees;
    public TableView tableauSalle;
    public TableColumn Identifiant;
    public TableColumn Nom;
    public TableColumn OccupationTotale;
    public TableColumn OccupationParJour;
    public TableColumn OccupationParSemaine;

    @FXML
    void afficherTableauStatistiquesGlobales() {
        tableauSalle.setVisible(false);
    }

    @FXML
    void afficherTableauSallesNonReservees() {
        tableauSalle.setVisible(true);
    }


}
