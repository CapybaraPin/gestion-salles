package iut.info2.saltistique.modele;

public class Utilisateur {

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
}
