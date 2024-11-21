package iut.info2.saltistique.controleur;

import iut.info2.saltistique.modele.Reservation;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import iut.info2.saltistique.modele.Salle;

public class ControleurConsultationSalle extends Controleur {

    @FXML
    public Label nomSalle;

    @FXML
    public Label dureeReservation;

    @FXML
    public Label dureeMoyenneReservation;

    /**
     * Configure les informations spécifiques à la salle sélectionnée.
     *
     * @param salle             La salle à afficher.
     * @param listeReservations
     */
    public void setSalle(Salle salle, ObservableList<Reservation> listeReservations) {
        if (salle != null) {
            nomSalle.setText(salle.getNom());

            // Exemple : calculer et afficher des durées fictives
            dureeReservation.setText(salle.getTempsTotalReservations(listeReservations));
            dureeMoyenneReservation.setText(salle.getTempsMoyenReservations(listeReservations) + " par jours");
        }
    }
}
