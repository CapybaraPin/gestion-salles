package iut.info2.saltistique.modele;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Map;

public class Filtre {
    private Utilisateur employeFiltre;

    public Filtre() {
        this.employeFiltre = null;
    }
    public Utilisateur getEmployeFiltre() {
        return employeFiltre;
    }
    public void setEmployeFiltre(Utilisateur employeFiltre) {
        this.employeFiltre = employeFiltre;
    }
    /**
     * Applique les filtres pour les données de réservation et met à jour les listes filtrées.
     *
     * @param reservations Liste des réservations complètes.
     * @return Les listes filtrées par type : réservations, salles, activités, utilisateurs.
     */
    public Map<String, ObservableList<?>> appliquerFiltres(Map<Integer, Reservation> reservations) {
        boolean match;
        ObservableList<Reservation> reservationsFiltrees = FXCollections.observableArrayList();
        ObservableList<Salle> sallesFiltrees = FXCollections.observableArrayList();
        ObservableList<Activite> activitesFiltrees = FXCollections.observableArrayList();
        ObservableList<Utilisateur> utilisateursFiltrees = FXCollections.observableArrayList();

        for (Reservation reservation : reservations.values()) {
            match = employeFiltre == null || reservation.getUtilisateur().equals(employeFiltre);
            if (match) {
                reservationsFiltrees.add(reservation);
                if (!sallesFiltrees.contains(reservation.getSalle())) {
                    sallesFiltrees.add(reservation.getSalle());
                }
                if (!activitesFiltrees.contains(reservation.getActivite())) {
                    activitesFiltrees.add(reservation.getActivite());
                }
                if (!utilisateursFiltrees.contains(reservation.getUtilisateur())) {
                    utilisateursFiltrees.add(reservation.getUtilisateur());
                }
            }
        }
        if(utilisateursFiltrees.isEmpty()) {
            utilisateursFiltrees.add(employeFiltre);
        }
        return Map.of(
                "reservations", reservationsFiltrees,
                "salles", sallesFiltrees,
                "activites", activitesFiltrees,
                "utilisateurs", utilisateursFiltrees
        );
    }
}
