package iut.info2.saltistique.modele;

import java.io.Serializable;

public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    private String identifiant;
    private String nom;
    private String prenom;
    private String numeroTelephone;

    public Utilisateur(String identifiant, String nom, String prenom, String numeroTelephone) {
        this.identifiant = identifiant;
        this.nom = nom;
        this.prenom = prenom;
        this.numeroTelephone = numeroTelephone;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNumeroTelephone() {
        return numeroTelephone;
    }

}
