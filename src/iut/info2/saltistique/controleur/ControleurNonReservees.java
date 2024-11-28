package iut.info2.saltistique.controleur;

import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.modele.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

/**
 * Contrôleur de la vue de consultation des salles non réservées.
 * <p>
 *     Cette classe permet de filtrer les salles non réservées en fonction de critères spécifiques :
 *     - périodes de dates
 *     - horaires
 *     <br>
 *     Les salles sont affichés de la même manière que dans la vue de consultation des salles.
 * </p>
 */
public class ControleurNonReservees extends Controleur {

    /** Colonne pour l'identifiant de la salle. */
    @FXML
    public TableColumn<Salle, String> IdentifiantSalle;

    /** Colonne pour le nom de la salle. */
    @FXML
    public TableColumn<Salle, String> NomSalle;

    /** Colonne pour la capacité de la salle. */
    @FXML
    public TableColumn<Salle, Integer> Capacite;

    /** Colonne pour la disponibilité d'une vidéo projecteur. */
    @FXML
    public TableColumn<Salle, Boolean> VideoProjecteur;

    /** Colonne pour la disponibilité d'un écran XXL. */
    @FXML
    public TableColumn<Salle, Boolean> EcranXXL;

    /** Colonne pour les ordinateurs disponibles dans la salle. */
    @FXML
    public TableColumn<Salle, String> Ordinateurs;

    /** Colonne pour le type de la salle. */
    @FXML
    public TableColumn<Salle, String> Type;

    /** Colonne pour les logiciels disponibles dans la salle.  */
    @FXML
    public TableColumn<Salle, String> Logiciels;

    /** Colonne pour la disponibilité d'une imprimante. */
    @FXML
    public TableColumn<Salle, Boolean> Imprimante;

    /** Liste des salles non réservées */
    @FXML
    public ObservableList<Salle> listeSalles;

    /** Liste des réservations */
    @FXML
    public ObservableList<Reservation> listeReservations;

    /** Selection de la date de début */
    @FXML
    DatePicker filtreDateDebut = new DatePicker();

    /** Selection de la date de fin */
    @FXML
    DatePicker filtreDateFin = new DatePicker();

    /** Selection de l'heure de début */
    @FXML
    Spinner<Integer> heuresDebut = new Spinner<>();

    /** Selection des minutes du début  */
    @FXML
    Spinner<Integer> minutesDebut = new Spinner<>();

    /** Selection de l'heure de fin */
    @FXML
    Spinner<Integer> heuresFin = new Spinner<>();

    /** Selection des minutes de fin */
    @FXML
    Spinner<Integer> minutesFin = new Spinner<>();

    /** Tableau des salles non réservées */
    @FXML
    public TableView<Salle> tableauSalles;

    /** Conteneur des filtres appliqués */
    @FXML
    HBox hboxFiltresAppliques;

    /** Instance de la classe Filtre */
    Filtre filtre;

    /**
     * initialise la vue de consultation des salles non réservées.
     * <p>
     *     Cette méthode initialise les éléments de la vue :
     *     <ul>
     *         <li>les listes des salles et des réservations</li>
     *         <li>les filtres</li>
     *         <li>le tableau des salles</li>
     *         <li>l'action de fermeture associé au bouton de notification</li>
     *     </ul>
     * </p>
     */
    @FXML
    public void initialize() {
        listeSalles = FXCollections.observableArrayList();
        listeReservations = FXCollections.observableArrayList();


        filtre = new Filtre();

        initialiserTableauSalles();
        initialiserFiltres();
        clickBoutonNotification();
    }


    /**
    * Action associée au bouton filtrer pour filtrer les données en fonction du filtre demandé
    */
    @FXML
    void clickFiltrer() {
        actualiserReservations();
        creationFiltres();
    }

    /**
     * Actualise la liste des réservations.
     * <p>
     *     Cette méthode récupère les réservations depuis le modèle et les ajoute à la liste des réservations.
     * </p>
     */
    protected void actualiserReservations() {
        for (Map.Entry<Integer, Reservation> entry : Saltistique.gestionDonnees.getReservations().entrySet()) {
            listeReservations.add(entry.getValue());
        }
    }

    /**
     * Calcule les salles non réservées.
     * <p>
     *     Cette méthode calcule les salles non réservées en fonction des réservations filtrées.
     *     Elle ajoute les salles non réservées à la liste des salles.
     * </p>
     */
    private void calculerSallesNonReservees() {
        ObservableList<Reservation> listeReservationsFiltrees;

        listeReservationsFiltrees = filtre.appliquerFiltres(listeReservations);
        for (Map.Entry<Integer, Salle> entry : Saltistique.gestionDonnees.getSalles().entrySet()) {
            if (listeReservationsFiltrees.stream().noneMatch(reservation -> reservation.getSalle().equals(entry.getValue()))) {
                listeSalles.add(entry.getValue());
            }
        }
    }

    /**
     * Initialise les filtres.
     * <p>
     *     Cette méthode initialise les éléments de sélection des filtres :
     *     <ul>
     *         <li>les dates de début et de fin</li>
     *         <li>les heures de début et de fin</li>
     *     </ul>
     *     Elle réinitialise les valeurs des filtres à {@code null} ou à 0.
     *     Elle initialise les valeurs des spinners pour les heures et les minutes
     *     avec des valeurs allant de 0 à 23 pour les heures et de 0 à 59 pour les minutes.
     * </p>
     */
    protected void initialiserFiltres() {
        filtreDateDebut.setValue(null);
        filtreDateFin.setValue(null);
        SpinnerValueFactory<Integer> heuresValueFactoryDebut =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
        heuresDebut.setValueFactory(heuresValueFactoryDebut);
        SpinnerValueFactory<Integer> minutesValueFactoryDebut =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        minutesDebut.setValueFactory(minutesValueFactoryDebut);
        SpinnerValueFactory<Integer> heuresValueFactoryFin =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
        heuresFin.setValueFactory(heuresValueFactoryFin);
        SpinnerValueFactory<Integer> minutesValueFactoryFin =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
        minutesFin.setValueFactory(minutesValueFactoryFin);
    }

    /**
     * Initialise le tableau des salles.
     */
    private void initialiserTableauSalles() {
        IdentifiantSalle.setCellValueFactory(new PropertyValueFactory<>("identifiant"));
        NomSalle.setCellValueFactory(new PropertyValueFactory<>("nom"));
        Capacite.setCellValueFactory(new PropertyValueFactory<>("capacite"));
        VideoProjecteur.setCellValueFactory(new PropertyValueFactory<>("videoProjecteur"));
        EcranXXL.setCellValueFactory(new PropertyValueFactory<>("ecranXXL"));
        Ordinateurs.setCellValueFactory(new PropertyValueFactory<>("ordinateurs"));
        Type.setCellValueFactory(new PropertyValueFactory<>("type"));
        Logiciels.setCellValueFactory(new PropertyValueFactory<>("logiciels"));
        Imprimante.setCellValueFactory(new PropertyValueFactory<>("imprimante"));
    }

    /**
     * Crée un filtre à partir des valeurs des éléments de sélection.
     * <p>
     *     Cette méthode crée un filtre à partir des valeurs des éléments de sélection :
     *     <ul>
     *         <li>les dates de début et de fin</li>
     *         <li>les heures de début et de fin</li>
     *     </ul>
     *     <br>
     *     Elle vérifie si les valeurs des dates et des heures sont valides.
     *     <br><br>
     *     Si une ou les dates ne sont pas renseignées,
     *     elle prend la date la plus ancienne si elle n'est pas renseignée
     *     et la date la plus récente si elle n'est pas renseignée.
     *     <br><br>
     *     Si une ou les heures ne sont pas renseignées,
     *     elle prend l'horaire de début de la date de début si elle n'est pas renseignée
     *     et l'horaire de fin de la date de fin si elle n'est pas renseignée.
     *     <br><br>
     *     Elle applique le filtre si la date de début est avant la date de fin.
     *     <br><br>
     *     Elle affiche les filtres appliqués.
     */
    protected void creationFiltres() {
        LocalDate debutDate = filtreDateDebut.getValue();
        LocalDate finDate = filtreDateFin.getValue();
        LocalDateTime dateLaPlusAncienne;
        LocalDateTime dateLaPlusRecente;
        Integer debutHeure = heuresDebut.getValue();
        Integer debutMinute = minutesDebut.getValue();
        Integer finHeure = heuresFin.getValue();
        Integer finMinute = minutesFin.getValue();
        boolean dateInscrite;
        boolean horaireInscrit;
        LocalDateTime debut;
        LocalDateTime fin;

        dateLaPlusAncienne = null;
        dateLaPlusRecente = null;
        for (Reservation reservation : listeReservations) {
            LocalDateTime dateDebut = reservation.getDateDebut();
            if (dateLaPlusAncienne == null || dateDebut.isBefore(dateLaPlusAncienne)) {
                dateLaPlusAncienne = dateDebut;
            }
            if (dateLaPlusRecente == null || dateDebut.isAfter(dateLaPlusRecente)) {
                dateLaPlusRecente = dateDebut;
            }
        }
        dateInscrite = debutDate == null || finDate == null;
        horaireInscrit = debutHeure != 0 || debutMinute != 0 || finHeure != 0 || finMinute != 0;

        // si au moins un champ de date n'est pas renseigné
        if (dateInscrite) {
            // si le champ de date de début n'est pas renseigné
            // on prend la date la plus ancienne des réservations
            if (debutDate == null) {
                debutDate = dateLaPlusAncienne.toLocalDate();
            }

            // si le champ de date de fin n'est pas renseigné
            // on prend la date la plus récente des réservations
            if (finDate == null) {
                finDate = dateLaPlusRecente.toLocalDate();
            }
        }
        // si au moins un champ d'horaire est renseigné
        if (horaireInscrit) {
            // si l'horaire de début n'est pas renseigné
            // on prend l'horaire de début de la date de début
            if (debutHeure == 0 && debutMinute == 0) {
                debut = debutDate.atStartOfDay();
            } else {
                // sinon on prend l'horaire renseigné
                debut = LocalDateTime.of(debutDate, LocalTime.of(debutHeure, debutMinute));
            }

            // si l'horaire de fin n'est pas renseigné
            // on prend l'horaire de fin de la date de fin
            if (finHeure == 0 && finMinute == 0) {
                 fin = finDate.atTime(23, 59);
            } else {
                 fin = LocalDateTime.of(finDate, LocalTime.of(finHeure, finMinute));
            }
        } else {
            debut = debutDate.atStartOfDay();
            fin = finDate.atTime(23, 59);
        }

        // Application du filtre si les valeurs sont valides
        if (debut.isBefore(fin)) {
            filtre.ajouterFiltreDate(debut, fin);
            initialiserFiltres();
            afficherFiltresAppliques();
            viderTableauSalles();
            calculerSallesNonReservees();
            tableauSalles.setItems(listeSalles);

            new Notification("Filtre", "Filtre appliqué");
        } else {
            // set les valeurs actuelles
            filtreDateDebut.setValue(debutDate);
            filtreDateFin.setValue(finDate);
            heuresDebut.getValueFactory().setValue(debutHeure);
            minutesDebut.getValueFactory().setValue(debutMinute);
            heuresFin.getValueFactory().setValue(finHeure);
            minutesFin.getValueFactory().setValue(finMinute);
            new Notification("Filtre", "Veuillez sélectionner une période valide.");
        }
    }

    /**
     * Vide le tableau des salles.
     */
    private void viderTableauSalles() {
        listeSalles.clear();
        tableauSalles.setItems(listeSalles);
    }

    /**
     * Affiche les filtres appliqués.
     */
    protected void afficherFiltresAppliques() {
        hboxFiltresAppliques.getChildren().clear();
        hboxFiltresAppliques.setSpacing(10);
        LocalDateTime[] datesFiltrees = filtre.getFiltreDate();
        if (datesFiltrees[0] != null && datesFiltrees[1] != null) {
            String texteFiltreDate = "Période : " + datesFiltrees[0] + " - " + datesFiltrees[1];
            Button boutonDate = creerBoutonFiltre(texteFiltreDate, _ -> {
                filtre.supprimerFiltreDate();
                initialiserFiltres();
                afficherFiltresAppliques();
                viderTableauSalles();
            });
            hboxFiltresAppliques.getChildren().add(boutonDate);
        }
    }

    /**
     * Crée un bouton de filtre avec un texte et un graphique (croix).
     *
     * @param texte  Le texte a affiché sur le bouton.
     * @param action L'action a exécuté lors du clic sur le bouton.
     * @return Le bouton créé.
     */
    private Button creerBoutonFiltre(String texte, EventHandler<ActionEvent> action) {
        Button bouton = new Button(texte);
        bouton.setOnAction(action);
        Pane croix = new Pane();
        croix.setPrefSize(6, 6);
        croix.setMaxSize(6, 6);
        croix.setMinSize(6, 6);
        croix.getStyleClass().add("ico-close");
        bouton.setGraphic(croix);
        bouton.setContentDisplay(ContentDisplay.RIGHT);
        bouton.getStyleClass().add("btn-filtre");
        return bouton;
    }

    /**
     * Affiche les statistiques globales.
     * Appelée lors du clic sur le bouton "Statistiques globales".
     */
    public void afficherStatistiquesGlobales() {
        Saltistique.changeScene(Scenes.CONSULTER_STATISTIQUES);
    }

    /**
     * Affiche la consultation de données.
     */
    public void afficherConsultationDonnees() {
        Saltistique.changeScene(Scenes.CONSULTER_DONNEES);
    }

    /**
     * Retour au menu principal.
     */
    @Override
    @FXML
    public void handlerRetourMenu() {
        Saltistique.changeScene(Scenes.CONSULTER_DONNEES);
        menuClick();
    }
}

