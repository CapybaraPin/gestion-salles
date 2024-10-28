/**
 * Scenes.java                      SAE 3.A.01
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique.modele;

/**
 * L'énumération {@code Scenes} représente les différentes scènes de l'application JavaFX.
 * <p>Chaque élément de cette énumération correspond à une scène spécifique de l'application,
 * et est associé au chemin du fichier FXML correspondant qui définit l'interface utilisateur de la scène.</p>
 *
 * <p>Cette énumération fournit également une méthode pour récupérer le chemin du fichier FXML associé à chaque scène.</p>
 *
 * @author Hugo ROBLES
 */
public enum Scenes {

    ACCUEIL("/iut/info2/saltistique/vue/accueil.fxml"),
    IMPORTATION_RESEAU("/iut/info2/saltistique/vue/importerReseau.fxml"),
    EXPORTER_RESEAU("/iut/info2/saltistique/vue/exporterReseau.fxml");

    /** Le chemin du fichier FXML correspondant à la scène. */
    private String chemin;

    /**
     * Constructeur de l'énumération {@code Scenes}.
     *
     * @param chemin le chemin du fichier FXML associé à la scène
     */
    Scenes(String chemin) {
        this.chemin = chemin;
    }

    /**
     * Retourne le chemin du fichier FXML associé à la scène.
     *
     * @return le chemin du fichier FXML sous forme de chaîne de caractères
     */
    public String getChemin() {
        return chemin;
    }
}
