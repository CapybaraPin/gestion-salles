/**
 * Saltistique.java                 SAE 3.A.01
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique;

import iut.info2.saltistique.modele.Notification;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.util.EnumMap;
import java.util.HashMap;

import iut.info2.saltistique.modele.Scenes;
import iut.info2.saltistique.modele.donnees.GestionDonnees;

/**
 * <b>Saltistique</b> est la classe principale de l'application JavaFX.
 *
 * <p>Elle gère le cycle de vie de l'application, le chargement des différentes
 * scènes et permet le changement de celles-ci. </p>
 *
 * <p>Les scènes de l'application sont définies dans un {@link EnumMap} associant
 * chaque valeur de l'énumération {@link Scenes} à sa représentation JavaFX {@link Scene}.</p>
 *
 * <h2>Responsabilités principales :</h2>
 * <ul>
 *     <li>Initialisation de l'application JavaFX</li>
 *     <li>Chargement des scènes au démarrage</li>
 *     <li>Gestion du changement de scène via une méthode statique</li>
 * </ul>
 *
 * <h2>Utilisation :</h2>
 * <p>L'application démarre via la méthode {@link #start(Stage)} et affiche par défaut
 * la scène d'accueil {@link Scenes#ACCUEIL}. Il est ensuite possible de changer de scène à
 * partir d'autres classes via la méthode statique {@link #changeScene(Scenes)}.</p>
 *
 * @author Hugo ROBLES
 */
public class Saltistique extends Application {

    /** Un EnumMap qui stocke les scènes associées à chaque valeur de l'énumération {@link Scenes}. */
    public static EnumMap<Scenes, Scene> scenes = new EnumMap<>(Scenes.class);

    /** Un HashMap qui stocke les contrôleurs associés aux champs de l'énumération {@link Scenes}. */
    private static HashMap<Scenes, Object> controllersScenes = new HashMap<>();

    /** Un HashMap qui stocke les contrôleurs associés aux scènes. {@link Scene} */
    private static HashMap<Scene, Object> controllersScene = new HashMap<>();

    /** La fenêtre principale (ou scène principale) de l'application JavaFX. */
    private static Stage primaryStage;

    private static Stage popUp;

    /** Prend en charge le lien entre le controleur et le modèle */
    public static GestionDonnees gestionDonnees;

    /** Chemin du fichier de sauvegarde */
    private static final String CHEMIN_SAUVEGARDE = "src/ressources/gestionDonnees.ser";

    /**
     * Méthode de démarrage de l'application JavaFX.
     *
     * <p> Cette méthode est appelée automatiquement lors du lancement
     * de l'application. Elle initialise la fenêtre principale et charge
     * toutes les scènes définies dans l'énumération {@link Scenes}. </p>
     *
     * @param primaryStage la fenêtre principale de l'application où les scènes seront affichées
     */
    @Override
    public void start(Stage primaryStage) {
        chargerGestionDonnees();
        Saltistique.primaryStage = primaryStage;
        // Disable title bar
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Gestion de salles");
        primaryStage.getIcons().add(new Image("/iut/info2/saltistique/vue/logo.png"));
        loadScenes();

        if (estInstanceVide()) {
            changeScene(Scenes.ACCUEIL);
        } else {
            changeScene(Scenes.CONSULTER_DONNEES);
            new Notification("Chargement des données", "Les données précédentes ont été chargées.");
        }

    }

    public boolean estInstanceVide() {
        boolean reservationsVide;
        boolean sallesVide;
        boolean utilisateursVide;
        boolean activitesVide;

        reservationsVide = gestionDonnees.getReservations().isEmpty();
        sallesVide = gestionDonnees.getSalles().isEmpty();
        utilisateursVide = gestionDonnees.getUtilisateurs().isEmpty();
        activitesVide = gestionDonnees.getActivites().isEmpty();

        return reservationsVide && sallesVide && utilisateursVide && activitesVide;
    }

    /**
     * TODO: Implement the closing of pop-ups when the main window closes.
     */
    @Override
    public void stop(){
        sauvegarderGestionDonnees();
    }

    /**
     * Charge toutes les scènes définies dans l'énumération {@link Scenes} et
     * les stocke dans l'EnumMap {@link #scenes}.
     *
     * <p> Chaque fichier FXML est chargé une seule fois lors
     * de l'initialisation ensuite associée à son type {@link Scenes}.</p>
     */
    private void loadScenes() {
        for (Scenes sceneEnum : Scenes.values()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneEnum.getChemin()));
                Scene scene = new Scene(loader.load());
                scenes.put(sceneEnum, scene);
                controllersScenes.put(sceneEnum, loader.getController()); // Store the controller
                controllersScene.put(scene, loader.getController());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Change la scène actuellement affichée dans la fenêtre principale.
     *
     * <p>Cette méthode statique permet de changer la scène de l'application à partir
     * d'une autre classe, en fonction de la valeur passée en paramètre. La nouvelle
     * scène sera celle associée à la clé {@link Scenes} spécifiée dans l'EnumMap {@link #scenes}.
     * Si la scène correspondante n'est pas trouvée, un message d'erreur sera affiché.</p>
     *
     * @param sceneEnum la scène à afficher, définie par une valeur de l'énumération {@link Scenes}
     */
    public static void changeScene(Scenes sceneEnum) {
        Scene scene = scenes.get(sceneEnum);
        if (scene != null) {
            primaryStage.setScene(scene);
            primaryStage.show();
        } else {
            System.err.println("Impossible de charger la scène : " + sceneEnum.name());
        }
    }

    /**
     * Ouvre une fenêtre pop-up pour la scène spécifiée.
     *
     * @param sceneEnum la scène à afficher dans la fenêtre pop-up
     */
    public static void showPopUp(Scenes sceneEnum) {
        Scene scene = scenes.get(sceneEnum);
        popUp = new Stage();
        popUp.initStyle(StageStyle.UNDECORATED);
        popUp.setResizable(false);
        popUp.setScene(scene);
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.showAndWait();
    }

    /**
     * Méthode pour obtenir le contrôleur d'une scène.
     *
     * @param sceneEnum la scène dont vous souhaitez obtenir le contrôleur
     * @param <T> le type du contrôleur
     * @return le contrôleur associé à la scène
     */
    public static <T> T getController(Scenes sceneEnum) {
        return (T) controllersScenes.get(sceneEnum);
    }

    /**
     * Méthode pour obtenir le contrôleur d'une scène.
     *
     * @param scene la scène
     */
    public static <T> T getController(Scene scene) {
        return (T) controllersScene.get(scene);
    }

    /**
     * Méthode principale pour lancer l'application.
     *
     * <p>Cette méthode appelle la méthode {@link Application}
     * pour démarrer le cycle de vie JavaFX.</p>
     *
     * @param args les arguments de ligne de commande
     */
    public static void main(String[] args) {
        launch();
    }



    /**
     * Retourne la scène principale de l'application, qui sert de fenêtre principale.
     *
     * @return la scène principale de l'application
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Sauvegarde l'objet GestionDonnees dans un fichier.
     */
    private void sauvegarderGestionDonnees() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CHEMIN_SAUVEGARDE))) {
            oos.writeObject(gestionDonnees);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde de GestionDonnees : " + e.getMessage());
        }
    }

    /**
     * Charge l'objet GestionDonnees depuis un fichier, ou crée une nouvelle instance si le fichier n'existe pas.
     */
    private void chargerGestionDonnees() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CHEMIN_SAUVEGARDE))) {
            gestionDonnees = (GestionDonnees) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Aucun état précédent trouvé ou erreur lors du chargement. Nouvelle instance créée.");
            gestionDonnees = new GestionDonnees(); // Instance par défaut
        }
    }
}
