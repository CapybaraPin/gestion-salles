/**
 * Saltistique.java                 SAE 3.A.01
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.EnumMap;
import java.io.IOException;

import iut.info2.saltistique.modele.Scenes;

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
    private static EnumMap<Scenes, Scene> scenes = new EnumMap<>(Scenes.class);

    /** La fenêtre principale (ou scène principale) de l'application JavaFX. */
    private static Stage primaryStage;

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
        Saltistique.primaryStage = primaryStage;
        loadScenes();
        changeScene(Scenes.ACCUEIL);
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
     * TODO faire la javadoc
     * @param sceneEnum
     */
    public static void showPopUp(Scenes sceneEnum) {
        Scene scene = scenes.get(sceneEnum);
        Stage popUp = new Stage();
        popUp.initStyle(StageStyle.UNDECORATED);
        popUp.setResizable(false);
        popUp.setScene(scene);
        popUp.show();
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
}
