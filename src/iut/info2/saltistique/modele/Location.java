package iut.info2.saltistique.modele;

import java.time.LocalDateTime;

public class Location extends Reservation {

    private String nomOrganisme;
    private String nomInterlocuteur;
    private String prenomInterlocuteur;
    private String telephoneInterlocuteur;

    public Location(String identifiant, LocalDateTime dateDebut, LocalDateTime dateFin, String description,
                    Salle salle, Activite activite, Utilisateur utilisateur, String nomOrganisme, String nomInterlocuteur,
                    String prenomInterlocuteur, String telephoneInterlocuteur) {
        super(identifiant, dateDebut, dateFin, description, salle, activite, utilisateur);
        this.nomOrganisme = nomOrganisme;
        this.nomInterlocuteur = nomInterlocuteur;
        this.prenomInterlocuteur = prenomInterlocuteur;
        this.telephoneInterlocuteur = telephoneInterlocuteur;
    }

    public String getNomOrganisme() {
        return nomOrganisme;
    }

    public String getNomInterlocuteur() {
        return nomInterlocuteur;
    }

    public String getPrenomInterlocuteur() {
        return prenomInterlocuteur;
    }

    public String getTelephoneInterlocuteur() {
        return telephoneInterlocuteur;
    }
}
