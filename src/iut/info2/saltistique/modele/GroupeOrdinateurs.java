package iut.info2.saltistique.modele;

public class GroupeOrdinateurs {

    private int quantite;
    private String type;
    private String[] logiciels;

    public GroupeOrdinateurs(int quantite, String type, String[] logiciels) {
        this.quantite = quantite;
        this.type = type;
        this.logiciels = logiciels;
    }

    public String getType() {
        return this.type;
    }
    public String[] getLogiciels() {
        return this.logiciels;
    }
    public int getQuantite() {
        return this.quantite;
    }
}
