package iut.info2.saltistique.modele;

import java.io.Serializable;

public class Salle implements Serializable {

    private static final long serialVersionUID = 1L;

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

    public String getIdentifiant() {
        return identifiant;
    }

    public String getNom() {
        return nom;
    }

    public int getCapacite() {
        return capacite;
    }

    public boolean isVideoProjecteur() {
        return videoProjecteur;
    }

    public boolean isEcranXXL() {
        return ecranXXL;
    }

    public boolean isImprimante() {
        return imprimante;
    }

    public int getOrdinateurs() {
        if (ordinateurs == null) {
            return 0;
        }
        return ordinateurs.getQuantite();
    }

    public String getType() {
        if (ordinateurs == null) {
            return null;
        }
        return ordinateurs.getType();
    }

    public String getLogiciels() {
        if (ordinateurs == null) {
            return null;
        }
        return ordinateurs.getLogiciels();
    }

    @Override
    public String toString() {
        return identifiant;
    }
}
