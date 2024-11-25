package iut.info2.saltistique.modele;

public class SalleStatistiques {
    private final String identifiant;
    private final String nom;
    private final String tempsMoyenOccupationJour;
    private final String tempsMoyenOccupationSemaine;
    private final String tempsOccupationTotal;

    public SalleStatistiques(String identifiant, String nom, String tempsMoyenOccupationJour, String tempsMoyenOccupationSemaine, String tempsOccupationTotal) {
        this.identifiant = identifiant;
        this.nom = nom;
        this.tempsMoyenOccupationJour = tempsMoyenOccupationJour;
        this.tempsMoyenOccupationSemaine = tempsMoyenOccupationSemaine;
        this.tempsOccupationTotal = tempsOccupationTotal;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public String getNom() {
        return nom;
    }

    public String getTempsMoyenOccupationJour() {
        return tempsMoyenOccupationJour;
    }

    public String getTempsMoyenOccupationSemaine() {
        return tempsMoyenOccupationSemaine;
    }

    public String getTempsOccupationTotal() {
        return tempsOccupationTotal;
    }
}
