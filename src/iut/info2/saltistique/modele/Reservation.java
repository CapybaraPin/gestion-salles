package iut.info2.saltistique.modele;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    private String identifiant;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private String description;
    private Salle salle;
    private Activite activite;
    private Utilisateur utilisateur;

    public Reservation(String identifiant, LocalDateTime dateDebut, LocalDateTime dateFin, String description,
                       Salle salle, Activite activite, Utilisateur utilisateur) {
        this.identifiant = identifiant;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.description = description;
        this.salle = salle;
        this.activite = activite;
        this.utilisateur = utilisateur;
    }

    public Salle getSalle() {
        return salle;
    }

    public Activite getActivite() {
        return activite;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public String getDescription() {
        return description;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "identifiant='" + identifiant + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", description='" + description + '\'' +
                ", salle=" + salle +
                ", activite=" + activite +
                ", utilisateur=" + utilisateur +
                '}';
    }
}
