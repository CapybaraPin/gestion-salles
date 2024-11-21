package iut.info2.saltistique.modele;

import javafx.collections.ObservableList;

import java.util.Comparator;

public class Classement {

    /** Liste des reservation sur laquelle le classement s'applique */
    private ObservableList<Reservation> listeReservations;

    /**
     * TODO : javadoc
     * @param listeReservations
     */
    public Classement(ObservableList<Reservation> listeReservations) {
        this.listeReservations = listeReservations;
    }

    /**
     * TODO : javadoc
     * @param selected
     * @param listeSalles
     * @return
     */
    public ObservableList<Salle> appliquerClassementSalles(boolean selected, ObservableList<Salle> listeSalles) {
        if (selected) {
            listeSalles.sort(Comparator.comparingLong(salle -> salle.getTempsTotalReservation(listeReservations)));
        } else {
            listeSalles.sort(Comparator.comparing(salle -> salle.getNom()));
        }
        return listeSalles;
    }

    /**
     * TODO : javadoc
     * @param selected
     * @param listeActivites
     * @return
     */
    public ObservableList<Activite> appliquerClassementActivites(boolean selected, ObservableList<Activite> listeActivites) {
        if (selected) {
            listeActivites.sort(Comparator.comparingLong(activite -> activite.getTempsTotalReservation(listeReservations)));
        } else {
            listeActivites.sort(Comparator.comparing(activite -> activite.getNom()));
        }
        return listeActivites;
    }

    /**
     * TODO : JavaDoc
     * @param selected
     * @param listeEmployes
     * @return
     */
    public ObservableList<Utilisateur> appliquerClassementEmployees(boolean selected, ObservableList<Utilisateur> listeEmployes) {
        if (selected) {
            listeEmployes.sort(Comparator.comparingLong(utilisateur -> utilisateur.getTempsTotalReservation(listeReservations)));
        } else {
            listeEmployes.sort(Comparator.comparing(utilisateur -> utilisateur.getNom()));
        }
        return listeEmployes;
    }

    public void setListeReservations(ObservableList<Reservation> listeReservations) {
        this.listeReservations = listeReservations;
    }
}
