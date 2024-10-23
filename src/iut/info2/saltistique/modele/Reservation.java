package iut.info2.saltistique.modele;

import java.util.Date;

public class Reservation {

    private String identifiant;
    private Date dateDebut;
    private Date dateFin;
    private String description;
    private String Organisation;
    private Utilisateur[] personne;
    private String telephone;

    public Reservation(String identifiant, Date dateDebut, Date dateFin, String description, String organisation, Utilisateur[] personne, String telephone) {
        this.identifiant = identifiant;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.description = description;
        Organisation = organisation;
        this.personne = personne;
        this.telephone = telephone;
    }
}
