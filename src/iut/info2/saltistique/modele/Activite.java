package iut.info2.saltistique.modele;

import java.io.Serializable;

public class Activite implements Serializable {

    private static final long serialVersionUID = 1L;

    private String identifiant;
    private String nom;

    public Activite(String identifiant, String nom) {
        this.identifiant = identifiant;
        this.nom = nom;
    }

    public String getIdentifiant() {
        return identifiant;
    }
}