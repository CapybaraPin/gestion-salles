/*
 * ControleurFiltres.java           02/11/2024
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique.controleur;

import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.modele.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Contrôleur pour la gestion des filtres de recherche des réservations.
 * <p>
 *     Ce contrôleur gère l'interface utilisateur pour la définition et l'application de filtres
 * </p>
 *
 * @author Hugo ROBLES, Jules VIALAS
 */
public class ControleurFiltres {

    /** Champs pour rentrer le filtre que l'on veut appliquer */
    @FXML
    public TextField valeurFiltre;

    /** Permet de selectionner les données sur lesquelles appliqué le filtre */
    @FXML
    public ComboBox<String> Filtres;

    /** Bouton pour filtrer les données */
    @FXML
    public Button boutonFiltrer;

    /** Permet de selectionner la date de début de filtre */
    @FXML
    public DatePicker filtreDateDebut;

    /** Permet de selectionner la date de fin de filtre */
    @FXML
    public DatePicker filtreDateFin;

    /** Conteneur du champ de texte pour filtrer */
    @FXML
    public HBox hboxFiltreTexte;

    /** Conteneur de tous les filtres par périodes */
    @FXML
    public VBox vboxFiltreDate;

    /** Conteneur pour le changement de place du bouton Filtrer */
    @FXML
    public HBox hboxPourBoutonFiltrer;

    /** Conteneur pour les filtres appliqués */
    @FXML
    public HBox hboxFiltresAppliques;

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

    /**
     * Listes observables contenant les objets de chaques types.
     * Ces listes sont utilisées pour afficher et gérer les types de données disponibles
     * dans l'application, permettant la liaison de données pour des composants
     * de l'interface utilisateur, tels que des tableaux ou des listes.
     */
    private ObservableList<Salle> listeSalles;
    private ObservableList<Activite> listeActivites;
    private ObservableList<Utilisateur> listeEmployes;
    private ObservableList<Reservation> listeReservations;

    /** TableView pour les réservations. */
    @FXML
    protected TableView<Reservation> tableauReservations;

    /** Filtre contenant les différents filtres appliqués */
    private static Filtre filtre;

    /**
     * Permet de récupérer le filtre actuel.
     * @param filtre le filtre actuel
     */
    protected void setFiltre(Filtre filtre) {
        this.filtre = filtre;
    }

    /**
     * Intialise les valeurs des différents éléments de l'interface.
     */
    protected void initialiserFiltres() {
        Filtres.setItems(FXCollections.observableArrayList(
                "Salle",
                "Activité",
                "Employé",
                "Période"
        ));
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
        Filtres.getSelectionModel().selectFirst();
    }

    /**
     * Permet de changer l'affichage du bouton de filtrage en fonction de si l'affichage correspond aux périodes ou non
     */
    @FXML
    void changerFiltre() {
        boutonFiltrer.setOnAction(null);
        if (Filtres.getSelectionModel().getSelectedItem().equals("Période")) {
            vboxFiltreDate.setVisible(true);
            hboxFiltreTexte.setVisible(false);
            hboxFiltreTexte.setMouseTransparent(true);
            vboxFiltreDate.setMouseTransparent(false);
            hboxPourBoutonFiltrer.setMouseTransparent(false);
            hboxFiltreTexte.getChildren().remove(boutonFiltrer);
            hboxPourBoutonFiltrer.getChildren().remove(boutonFiltrer);
            hboxPourBoutonFiltrer.getChildren().add(boutonFiltrer);
            boutonFiltrer.setOnAction(_ -> clickFiltrerDate());
        } else {
            vboxFiltreDate.setVisible(false);
            hboxFiltreTexte.setVisible(true);
            vboxFiltreDate.setMouseTransparent(true);
            hboxFiltreTexte.setMouseTransparent(false);
            hboxPourBoutonFiltrer.setMouseTransparent(true);
            hboxPourBoutonFiltrer.getChildren().remove(boutonFiltrer);
            hboxFiltreTexte.getChildren().remove(boutonFiltrer);
            hboxFiltreTexte.getChildren().add(boutonFiltrer);
            boutonFiltrer.setOnAction(_ -> clickFiltrer());
        }
    }

    @FXML
    void clickFiltrer() {
        creationFiltres();

        ControleurConsulterDonnees controleurConsulterDonnees = Saltistique.getController(Scenes.CONSULTER_DONNEES);
        if (tableauReservations == null) {
            controleurConsulterDonnees.afficherTempsReservationsTotal(listeReservations);
        } else {
            controleurConsulterDonnees.afficherTempsReservationsTotal(tableauReservations.getItems());
        }
    }

    /**
     * Permet de filtrer les données en fonction des filtres appliqués
     */
    protected void creationFiltres() {
        if (valeurFiltre != null && !valeurFiltre.getText().isEmpty()) {
            String critere = Filtres.getValue();
            String valeur = valeurFiltre.getText().toLowerCase();
            boolean correspondanceTrouvee = false;
            boolean filtreDejaApplique = false;

            switch (critere) {
                case "Salle":
                    correspondanceTrouvee = listeSalles.stream()
                            .anyMatch(salle -> salle.getNom() != null
                                    && salle.getNom().toLowerCase().equals(valeur));
                    filtreDejaApplique = correspondanceTrouvee &&
                            filtre.getSallesFiltrees().stream()
                                    .anyMatch(salle -> salle.getNom() != null
                                            && salle.getNom().toLowerCase().equals(valeur));

                    if (correspondanceTrouvee && !filtreDejaApplique) {
                        listeSalles.stream()
                                .filter(salle -> salle.getNom() != null
                                        && salle.getNom().toLowerCase().equals(valeur))
                                .forEach(salle -> filtre.ajouterFiltreSalle(salle));
                    }
                    break;

                case "Employé":
                    String[] motsRecherche = valeur.split("\\s+");
                    if (motsRecherche.length > 1) {
                        correspondanceTrouvee = listeEmployes.stream()
                                .anyMatch(employe -> employe.getNom() != null && employe.getPrenom() != null &&
                                        ((employe.getPrenom().toLowerCase() + " "
                                                + employe.getNom().toLowerCase()).equals(valeur)
                                                || (employe.getNom().toLowerCase() + " "
                                                + employe.getPrenom().toLowerCase()).equals(valeur)));

                        filtreDejaApplique = correspondanceTrouvee &&
                                filtre.getEmployesFiltres().stream()
                                        .anyMatch(employe -> employe.getNom() != null
                                                && employe.getPrenom() != null
                                                && ((employe.getPrenom().toLowerCase() + " "
                                                + employe.getNom().toLowerCase()).equals(valeur)
                                                || (employe.getNom().toLowerCase() + " "
                                                + employe.getPrenom().toLowerCase()).equals(valeur)));

                        if (correspondanceTrouvee && !filtreDejaApplique) {
                            listeEmployes.stream()
                                    .filter(employe -> employe.getNom() != null && employe.getPrenom() != null
                                            && ((employe.getPrenom().toLowerCase() + " " + employe.getNom()
                                            .toLowerCase()).equals(valeur)
                                            || (employe.getNom().toLowerCase() + " " + employe
                                            .getPrenom().toLowerCase()).equals(valeur)))
                                    .forEach(employe -> filtre.ajouterFiltreEmploye(employe));
                        }
                    } else {
                        correspondanceTrouvee = listeEmployes.stream()
                                .anyMatch(employe -> employe.getNom() != null && employe.getPrenom() != null
                                        && java.util.Arrays.stream(motsRecherche)
                                        .anyMatch(mot -> employe.getNom().toLowerCase().contains(mot)
                                                || employe.getPrenom().toLowerCase().contains(mot)));

                        filtreDejaApplique = correspondanceTrouvee &&
                                filtre.getEmployesFiltres().stream()
                                        .anyMatch(employe -> employe.getNom() != null
                                                && employe.getPrenom() != null
                                                && java.util.Arrays.stream(motsRecherche)
                                                .anyMatch(mot -> employe.getNom()
                                                        .toLowerCase().contains(mot)
                                                        || employe.getPrenom().toLowerCase().contains(mot)));

                        if (correspondanceTrouvee && !filtreDejaApplique) {
                            listeEmployes.stream()
                                    .filter(employe -> employe.getNom() != null && employe.getPrenom() != null
                                            && java.util.Arrays.stream(motsRecherche)
                                            .anyMatch(mot -> employe.getNom().toLowerCase().contains(mot)
                                                    || employe.getPrenom().toLowerCase().contains(mot)))
                                    .forEach(employe -> filtre.ajouterFiltreEmploye(employe));
                        }
                    }
                    break;


                case "Activité":
                    correspondanceTrouvee = listeActivites.stream()
                            .anyMatch(activite -> activite.getNom() != null
                                    && activite.getNom().toLowerCase().equals(valeur));
                    filtreDejaApplique = correspondanceTrouvee &&
                            filtre.getActivitesFiltrees().stream()
                                    .anyMatch(activite -> activite.getNom() != null
                                            && activite.getNom().toLowerCase().equals(valeur));

                    if (correspondanceTrouvee && !filtreDejaApplique) {
                        listeActivites.stream()
                                .filter(activite -> activite.getNom() != null
                                        && activite.getNom().toLowerCase().equals(valeur))
                                .forEach(activite -> filtre.ajouterFiltreActivite(activite));
                    }
                    break;
            }

            if (!correspondanceTrouvee) {
                new Notification("Filtre inconnu", "Le filtre que vous avez rentré ne correspond à " +
                        "aucune donnée.");
            } else if (filtreDejaApplique) {
                new Notification("Filtre déjà appliqué", "Le filtre que vous tentez d'appliqué est " +
                        "déjà actif.");
            } else {
                appliquerFiltres();
                afficherFiltresAppliques();
            }
        } else {
            new Notification("Aucun filtre", "Vous n'avez rentré aucun filtre.");
        }
    }

    /**
     * Applique les filtres définis sur la liste des réservations.
     * <p>
     * Cette méthode récupère les filtres définis dans l'objet {@link Filtre},
     * applique ces filtres à la liste des réservations originales,
     * et met à jour la TableView des réservations avec les résultats filtrés.
     */
    @FXML
    private void appliquerFiltres() {
        List<Reservation> reservationsFiltrees = filtre.appliquerFiltres(new ArrayList<>(listeReservations));

        if (tableauReservations != null) {
            tableauReservations.setItems(FXCollections.observableArrayList(reservationsFiltrees));
        } else {
            ControleurConsultationSalle consultationSalle = Saltistique.getController(Scenes.CONSULTER_SALLE);
            consultationSalle.actualiserStats();
        }
    }

    /**
     * Affiche les filtres actuellement appliqués sous forme de boutons interactifs.
     */
    private void afficherFiltresAppliques() {
        hboxFiltresAppliques.getChildren().clear();
        hboxFiltresAppliques.setSpacing(10);
        if (filtre.getSallesFiltrees() != null) {
            for (Salle salle : filtre.getSallesFiltrees()) {
                Button boutonSalle = creerBoutonFiltre("Salle : " + salle.getNom(), _ -> {
                    filtre.supprimerFiltreSalle(salle);
                    mettreAJourFiltres();
                });
                hboxFiltresAppliques.getChildren().add(boutonSalle);
            }
        }

        if (filtre.getEmployesFiltres() != null) {
            for (Utilisateur employe : filtre.getEmployesFiltres()) {
                Button boutonEmploye = creerBoutonFiltre("Employé : " + employe.getPrenom() + " "
                        + employe.getNom(), _ -> {
                    filtre.supprimerFiltreEmploye(employe);
                    mettreAJourFiltres();
                });
                hboxFiltresAppliques.getChildren().add(boutonEmploye);
            }
        }

        if (filtre.getActivitesFiltrees() != null) {
            for (Activite activite : filtre.getActivitesFiltrees()) {
                Button boutonActivite = creerBoutonFiltre("Activité : " + activite.getNom(), _ -> {
                    filtre.supprimerFiltreActivite(activite);
                    mettreAJourFiltres();
                });
                hboxFiltresAppliques.getChildren().add(boutonActivite);
            }
        }

        LocalDateTime[] datesFiltrees = filtre.getFiltreDate();
        if (datesFiltrees[0] != null && datesFiltrees[1] != null) {
            String texteFiltreDate = "Période : " + datesFiltrees[0] + " - " + datesFiltrees[1];
            Button boutonDate = creerBoutonFiltre(texteFiltreDate, _ -> {
                filtre.supprimerFiltreDate();
                mettreAJourFiltres();
                afficherFiltresAppliques();
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
     * Met à jour la liste des réservations affichées en fonction des filtres appliqués.
     *
     * <p>Elle est utilisée chaque fois qu'un filtre est ajouté, supprimé ou modifié afin
     * d'assurer que la TableView reflète correctement l'état actuel des filtres.</p>
     */
    private void mettreAJourFiltres() {
        List<Reservation> reservationsFiltrees = filtre.appliquerFiltres(listeReservations);

        ControleurConsulterDonnees controleurConsulterDonnees = Saltistique.getController(Scenes.CONSULTER_DONNEES);
        controleurConsulterDonnees.afficherTempsReservationsTotal(tableauReservations.getItems());

        if (tableauReservations != null) {
            tableauReservations.setItems(FXCollections.observableArrayList(reservationsFiltrees));
        } else {
            ControleurConsultationSalle consultationSalle = Saltistique.getController(Scenes.CONSULTER_SALLE);
            consultationSalle.actualiserStats();
        }

        afficherFiltresAppliques();
    }

    /**
     * Gestionnaire d'événements appelé lorsque l'utilisateur clique sur le bouton pour filtrer les
     * réservations par période.
     * <p>
     * Cette méthode récupère les dates et heures spécifiées dans les champs correspondants,
     * crée des objets {@link LocalDateTime} pour la période de début et de fin,
     * et applique un filtre sur les réservations en fonction de cette plage.
     * Si l'une des dates est invalide ou non définie, une notification d'erreur est affichée.
     * </p>
     */
    @FXML
    void clickFiltrerDate() {
        if (filtreDateDebut.getValue() != null && filtreDateFin.getValue() != null
                && filtreDateDebut.getValue().isBefore(filtreDateFin.getValue())) {
            LocalDateTime debut = LocalDateTime.of(filtreDateDebut.getValue(), LocalTime.of(heuresDebut.getValue(),
                    minutesDebut.getValue()));
            LocalDateTime fin = LocalDateTime.of(filtreDateFin.getValue(), LocalTime.of(heuresFin.getValue(),
                    minutesFin.getValue()));
            filtre.ajouterFiltreDate(debut, fin);
            mettreAJourFiltres();
        } else {
            new Notification("Périodes invalides", "Les périodes rentrées ne sont pas valides.");
        }
    }

    /**
     * Permet de récupérer la liste des salles.
     */
    protected void setListeSalles(ObservableList<Salle> listeSalles) {
        this.listeSalles = listeSalles;
    }

    /**
     * Permet de récupérer la liste des activités.
     */
    protected void setListeActivites(ObservableList<Activite> listeActivites) {
        this.listeActivites = listeActivites;
    }

    /**
     * Permet de récupérer la liste des employés.
     */
    protected void setListeEmployes(ObservableList<Utilisateur> listeEmployes) {
        this.listeEmployes = listeEmployes;
    }

    /**
     * Permet de récupérer la liste des réservations.
     */
    protected void setListeReservations(ObservableList<Reservation> listeReservations) {
        this.listeReservations = listeReservations;
    }
}