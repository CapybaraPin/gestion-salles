/*
 * ControleurInformationReservation.java 20/10/2024
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique.controleur;

import iut.info2.saltistique.modele.Formation;
import iut.info2.saltistique.modele.Location;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Contrôleur de la vue de consultation des reservations.
 *
 * @author ADAMS Dorian
 */
public class ControleurInformationReservation extends Controleur {

    Formation formation;
    Location location;

    /** FORMATION */

    @FXML
    private VBox formationVBox;

    /** Nom du Formateur */
    @FXML
    public Label nomFormateur;

    /** Numéro de téléphone du Formateur */
    @FXML
    private Label telephoneFormateur;

    /** LOCATION */

    @FXML
    private VBox locationVBox;

    /** Nom de l'organisme */
    @FXML
    private Label nomOrganisme;

    /** Nom de l'interlocuteur */
    @FXML
    private Label nomInterlocuteur;

    /** Numéro de téléphone de l'interlocuteur */
    @FXML
    private Label telephoneInterlocuteur;

    @FXML
    void initialize() {
        setHoverEffect();
    }

    public void setlocation(Location location) {
        this.location = location;
        this.nomOrganisme.setText(location.getNomOrganisme());
        this.nomInterlocuteur.setText(location.getNomInterlocuteur() + " " + location.getPrenomInterlocuteur());
        this.telephoneInterlocuteur.setText(location.getTelephoneInterlocuteur());
        formationVBox.setVisible(false);
        locationVBox.setVisible(true);
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
        this.nomFormateur.setText(formation.getNomFormateur() + " " + formation.getPrenomFormateur());
        this.telephoneFormateur.setText(formation.getTelephoneFormateur());
        locationVBox.setVisible(false);
        formationVBox.setVisible(true);
    }

}