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

    @FXML
    public TableView<Salle> tableauSalles;

    @FXML
    HBox hboxFiltresAppliques;


    Filtre filtre;




    /**
    * Action associée au bouton filtrer pour filtrer les données en fonction du filtre demandé
    */
    @FXML
    void clickFiltrer() {
        actualiserReservations();
        creationFiltres();
    }

    protected void actualiserReservations() {
        for (Map.Entry<Integer, Reservation> entry : Saltistique.gestionDonnees.getReservations().entrySet()) {
            listeReservations.add(entry.getValue());
        }
    }

    private void calculerSallesNonReservees() {
        ObservableList<Reservation> listeReservationsFiltrees;

        listeReservationsFiltrees = filtre.appliquerFiltres(listeReservations);
        for (Map.Entry<Integer, Salle> entry : Saltistique.gestionDonnees.getSalles().entrySet()) {
            if (listeReservationsFiltrees.stream().noneMatch(reservation -> reservation.getSalle().equals(entry.getValue()))) {
                listeSalles.add(entry.getValue());
            }
        }

        tableauSalles.setVisible(true);
        tableauSalles.setItems(listeSalles);

    }

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

    @FXML
    public void initialize() {
        listeSalles = FXCollections.observableArrayList();
        listeReservations = FXCollections.observableArrayList();


        filtre = new Filtre();

        initialiserTableauSalles();
        initialiserFiltres();

    }

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

    protected void creationFiltres() {
        LocalDate debutDate = filtreDateDebut.getValue();
        LocalDate finDate = filtreDateFin.getValue();
        LocalDateTime dateLaPlusAncienne = null;
        LocalDateTime dateLaPlusRecente = null;
        Integer debutHeure = heuresDebut.getValue();
        Integer debutMinute = minutesDebut.getValue();
        Integer finHeure = heuresFin.getValue();
        Integer finMinute = minutesFin.getValue();

        LocalDateTime debut = null;
        LocalDateTime fin = null;

        // Cas 1 : Filtrage par dates uniquement
        if (debutDate != null && finDate != null &&
                debutHeure == 0 && debutMinute == 0 &&
                finHeure == 0 && finMinute == 0) {

            debut = debutDate.atStartOfDay();
            fin = finDate.atTime(23, 59);

            // Cas 2 : Filtrage par dates et horaires complets
        } else if (debutDate != null && finDate != null &&
                debutHeure != 0 || debutMinute != 0 &&
                finHeure != 0 || finMinute != 0) {

            debut = LocalDateTime.of(debutDate, LocalTime.of(debutHeure, debutMinute));
            fin = LocalDateTime.of(finDate, LocalTime.of(finHeure, finMinute));

            // Cas 3 : Filtrage par horaires uniquement (sans dates explicites)
        } else { // TODO : regarder ces conditions louche
            if (finMinute == 0 && finHeure == 0) {
                finHeure = 23;
                finMinute = 59;
            }
            for (Reservation reservation : listeReservations) {
                LocalDateTime dateDebut = reservation.getDateDebut();
                if (dateLaPlusAncienne == null || dateDebut.isBefore(dateLaPlusAncienne)) {
                    dateLaPlusAncienne = dateDebut;
                }
                if (dateLaPlusRecente == null || dateDebut.isAfter(dateLaPlusRecente)) {
                    dateLaPlusRecente = dateDebut;
                }
            }
            if (dateLaPlusAncienne != null && dateLaPlusRecente != null) {
                debut = LocalDateTime.of(
                        dateLaPlusAncienne.toLocalDate(),
                        LocalTime.of(debutHeure, debutMinute));
                fin = LocalDateTime.of(
                        dateLaPlusRecente.toLocalDate(),
                        LocalTime.of(finHeure, finMinute));
            }
        }
        // Application du filtre si les valeurs sont valides
        if (debut != null && fin != null && debut.isBefore(fin)) {
            filtre.ajouterFiltreDate(debut, fin);
            initialiserFiltres();
            afficherFiltresAppliques();
            viderTableauSalles();
            calculerSallesNonReservees();
            new Notification("Filtre", "Filtre appliqué");
        } else {
            new Notification("Filtre", "Veuillez sélectionner une période valide.");
        }
    }

    private void viderTableauSalles() {
        listeSalles.clear();
        tableauSalles.setVisible(false);
        tableauSalles.setItems(listeSalles);
    }

    protected void afficherFiltresAppliques() {
        System.out.println("Affichage des filtres appliqués");
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
}

