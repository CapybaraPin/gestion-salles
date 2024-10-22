package iut.info2.saltistique.modele;

public class Salle {

    private String identifiant;
    private String nom;
    private int capacite;
    private boolean videoProjecteur;
    private boolean ecranXXL;
    private boolean imprimante;
    private GroupeOrdinateurs ordinateurs;

    public Salle(String identifiant, String nom, int capacite, boolean videoProjecteur, boolean ecranXXL, boolean imprimante, GroupeOrdinateurs ordinateurs) {
        this.identifiant = identifiant;
        this.nom = nom;
        this.capacite = capacite;
        this.videoProjecteur = videoProjecteur;
        this.ecranXXL = ecranXXL;
        this.imprimante = imprimante;
        this.ordinateurs = ordinateurs;
    }
}
