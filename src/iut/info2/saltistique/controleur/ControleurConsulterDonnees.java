/*
 * ControleurConsulterDonnees.java 02/11/2024
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
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Le contrôleur de la vue permettant de consulter les données.
 * Gère l'affichage, le filtrage, et l'interaction avec les tables de données
 * (réservations, activités, employés, salles) ainsi que le contrôle des boutons
 * et autres éléments interactifs de l'interface.
 * <p>
 * Ce contrôleur est responsable de :
 * <ul>
 *     <li>L'initialisation des tableaux pour afficher les données.</li>
 *     <li>L'application et la gestion des filtres pour les données visibles.</li>
 *     <li>La gestion de la navigation entre les différentes catégories de données.</li>
 * </ul>
 *
 * @author Jules VIALAS
 */
public class ControleurConsulterDonnees extends Controleur {

    /**
     * Bouton pour afficher les employés.
     */
    @FXML
    public Button employes;

    /**
     * Bouton pour afficher les activités.
     */
    @FXML
    public Button activites;

    /**
     * Bouton pour afficher les réservations.
     */
    @FXML
    public Button reservations;

    /**
     * Bouton pour afficher les salles.
     */
    @FXML
    public Button salles;

    /**
     * Ligne de sélection pour le bouton "Salles".
     */
    @FXML
    public Line SelectionSalles;

    /**
     * Ligne de sélection pour le bouton "Activités".
     */
    @FXML
    public Line SelectionActivites;

    /**
     * Ligne de sélection pour le bouton "Employés".
     */
    @FXML
    public Line SelectionEmployes;

    /**
     * Ligne de sélection pour le bouton "Réservations".
     */
    @FXML
    public Line SelectionReservations;

    /**
     * Colonne pour l'identifiant de l'employé.
     */
    @FXML
    public TableColumn<Utilisateur, String> IdentifiantEmploye;

    /**
     * Colonne pour le nom de l'employé.
     */
    @FXML
    public TableColumn<Utilisateur, String> NomEmploye;

    /**
     * Colonne pour le prénom de l'employé.
     */
    @FXML
    public TableColumn<Utilisateur, String> Prenom;

    /**
     * Colonne pour le numéro de téléphone de l'employé.
     */
    @FXML
    public TableColumn<Utilisateur, String> numeroDeTelephone;

    /**
     * Colonne pour l'identifiant de l'activité.
     */
    @FXML
    public TableColumn<Activite, String> IdentifiantActivite;

    /**
     * Colonne pour le nom de l'activité.
     */
    @FXML
    public TableColumn<Activite, String> Nom;

    /**
     * Colonne pour l'identifiant de la réservation.
     */
    @FXML
    public TableColumn<Reservation, String> IdentifiantReservation;

    /**
     * Colonne pour la date de début de la réservation.
     */
    @FXML
    public TableColumn<Reservation, LocalDateTime> dateDeDebut;

    /**
     * Colonne pour la date de fin de la réservation.
     */
    @FXML
    public TableColumn<Reservation, LocalDateTime> dateDeFin;

    /**
     * Colonne pour la description de la réservation.
     */
    @FXML
    public TableColumn<Reservation, String> Description;

    /**
     * Colonne pour la salle associée à la réservation.
     */
    @FXML
    public TableColumn<Reservation, Salle> Salle;

    /**
     * Colonne pour l'activité associée à la réservation.
     */
    @FXML
    public TableColumn<Reservation, Activite> Activite;

    /**
     * Colonne pour l'utilisateur associé à la réservation.
     */
    @FXML
    public TableColumn<Reservation, Utilisateur> Utilisateur;

    /**
     * TableView pour les réservations.
     */
    @FXML
    public TableView<Reservation> tableauReservations;

    /**
     * TableView pour les employés.
     */
    @FXML
    public TableView<Utilisateur> tableauEmployes;

    /**
     * TableView pour les activités.
     */
    @FXML
    public TableView<Activite> tableauActivites;

    /**
     * TableView pour les salles.
     */
    @FXML
    public TableView<Salle> tableauSalles;

    /**
     * Colonne pour l'identifiant de la salle.
     */
    @FXML
    public TableColumn<Salle, String> IdentifiantSalle;

    /**
     * Colonne pour le nom de la salle.
     */
    @FXML
    public TableColumn<Salle, String> NomSalle;

    /**
     * Colonne pour la capacité de la salle.
     */
    @FXML
    public TableColumn<Salle, Integer> Capacite;

    /**
     * Colonne pour la disponibilité d'une vidéo projecteur.
     */
    @FXML
    public TableColumn<Salle, Boolean> VideoProjecteur;

    /**
     * Colonne pour la disponibilité d'un écran XXL.
     */
    @FXML
    public TableColumn<Salle, Boolean> EcranXXL;

    /**
     * Colonne pour les ordinateurs disponibles dans la salle.
     */
    @FXML
    public TableColumn<Salle, String> Ordinateurs;

    /**
     * Colonne pour le type de la salle.
     */
    @FXML
    public TableColumn<Salle, String> Type;

    /**
     * Colonne pour les logiciels disponibles dans la salle.
     */
    @FXML
    public TableColumn<Salle, String> Logiciels;

    /**
     * Colonne pour la disponibilité d'une imprimante.
     */
    @FXML
    public TableColumn<Salle, Boolean> Imprimante;

    /**
     * Champs pour rentrer le filtre que l'on veut appliquer
     */
    @FXML
    public TextField valeurFiltre;

    /**
     * Permet de selectionner les données sur lesquelles appliqué le filtre
     */
    @FXML
    public ComboBox<String> Filtres;

    /**
     * Bouton pour filtrer les données
     */
    @FXML
    public Button boutonFiltrer;

    /**
     * Permet de selectionner la date de début de filtre
     */
    @FXML
    public DatePicker filtreDateDebut;

    /**
     * Permet de selectionner la date de fin de filtre
     */
    @FXML
    public DatePicker filtreDateFin;

    /**
     * Conteneur du champ de texte pour filtrer
     */
    @FXML
    public HBox hboxFiltreTexte;

    /**
     * Conteneur de tous les filtres par périodes
     */
    @FXML
    public VBox vboxFiltreDate;

    /**
     * Conteneur pour le changement de place du bouton Filtrer
     */
    @FXML
    public HBox hboxPourBoutonFiltrer;

    /**
     * Selection de l'heure de début
     */
    @FXML
    Spinner<Integer> heuresDebut = new Spinner<>();

    /**
     * Selection des minutes du début
     */
    @FXML
    Spinner<Integer> minutesDebut = new Spinner<>();

    /**
     * Selection de l'heure de fin
     */
    @FXML
    Spinner<Integer> heuresFin = new Spinner<>();

    /**
     * Selection des minutes de fin
     */
    @FXML
    Spinner<Integer> minutesFin = new Spinner<>();

    /**
     * Filtre contenant les différents filtres appliqués
     */
    @FXML
    private Filtre filtre;

    /**
     * HBox contenant les filtres appliqués
     */
    @FXML
    private HBox hboxFiltresAppliques;

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

    /**
     * Initialise le contrôleur après le chargement de la vue FXML.
     * Configure les filtres, les listes observables, et initialise les tableaux.
     */
    public void initialize() {
        filtre = new Filtre();
        listeSalles = FXCollections.observableArrayList();
        listeActivites = FXCollections.observableArrayList();
        listeEmployes = FXCollections.observableArrayList();
        listeReservations = FXCollections.observableArrayList();
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
        initialiserTableaux();
        initialiserTableauSalles();
        initialiserTableauActivites();
        initialiserTableauEmployes();
        initialiserTableauReservations();
        clickBoutonNotification();
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



    /**
     * Rafraîchit les données affichées dans les tableaux.
     * Recharge les données depuis la source et met à jour les tableaux liés.
     */
    public void rafraichirTableaux() {
        listeSalles.clear();
        listeActivites.clear();
        listeEmployes.clear();
        listeReservations.clear();
        initialiserTableaux();
        tableauSalles.setItems(listeSalles);
        tableauActivites.setItems(listeActivites);
        tableauEmployes.setItems(listeEmployes);
        tableauReservations.setItems(listeReservations);
    }

    /**
     * Initialise les listes de salles, d'activités, d'employés et de réservations
     * en les remplissant avec les données fournies par le gestionnaire de données.
     * Cette méthode parcourt les entrées des collections de données et les ajoute
     * aux listes correspondantes.
     */
    void initialiserTableaux() {
        for (Map.Entry<Integer, Salle> entry : Saltistique.gestionDonnees.getSalles().entrySet()) {
            listeSalles.add(entry.getValue());
        }
        for (Map.Entry<Integer, Activite> entry : Saltistique.gestionDonnees.getActivites().entrySet()) {
            listeActivites.add(entry.getValue());
        }
        for (Map.Entry<Integer, Utilisateur> entry : Saltistique.gestionDonnees.getUtilisateurs().entrySet()) {
            listeEmployes.add(entry.getValue());
        }
        for (Map.Entry<Integer, Reservation> entry : Saltistique.gestionDonnees.getReservations().entrySet()) {
            listeReservations.add(entry.getValue());
        }
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
        tableauSalles.setItems(listeSalles);
    }

    /**
     * Initialise le tableau des activités.
     */
    private void initialiserTableauActivites() {
        IdentifiantActivite.setCellValueFactory(new PropertyValueFactory<>("identifiant"));
        Nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        tableauActivites.setItems(listeActivites);
    }

    /**
     * Initialise le tableau des employés.
     */
    private void initialiserTableauEmployes() {
        IdentifiantEmploye.setCellValueFactory(new PropertyValueFactory<>("identifiant"));
        NomEmploye.setCellValueFactory(new PropertyValueFactory<>("nom"));
        Prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        numeroDeTelephone.setCellValueFactory(new PropertyValueFactory<>("numeroTelephone"));
        tableauEmployes.setItems(listeEmployes);
    }

    /**
     * Initialise le tableau des réservations.
     */
    private void initialiserTableauReservations() {
        IdentifiantReservation.setCellValueFactory(new PropertyValueFactory<>("identifiant"));
        dateDeDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateDeFin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        Description.setCellValueFactory(new PropertyValueFactory<>("description"));
        Salle.setCellValueFactory(new PropertyValueFactory<>("salle"));
        Activite.setCellValueFactory(new PropertyValueFactory<>("activite"));
        Utilisateur.setCellValueFactory(new PropertyValueFactory<>("utilisateur"));
        tableauReservations.setItems(listeReservations);
    }

    /**
     * Affiche uniquement le tableau et la sélection spécifiés.
     * Masque les autres tableaux et sélections.
     *
     * @param tableau   Le tableau a affiché.
     * @param selection La sélection a affiché.
     */
    @FXML
    private void afficherTableau(Node tableau, Node selection) {
        masquerTousLesTableauxEtSelections();
        tableau.setVisible(true);
        selection.setVisible(true);
        boolean afficherFiltres = tableau == tableauReservations;
        valeurFiltre.setVisible(afficherFiltres);
        Filtres.setVisible(afficherFiltres);
        boutonFiltrer.setVisible(afficherFiltres);
        if (Filtres.getSelectionModel().getSelectedItem().equals("Période")) {
            vboxFiltreDate.setVisible(afficherFiltres);
        } else {
            hboxFiltreTexte.setVisible(afficherFiltres);
        }

        hboxFiltresAppliques.setVisible(afficherFiltres);


    }

    /**
     * Masque tous les tableaux et les sélections dans l'interface utilisateur.
     * Cette méthode est utilisée pour garantir qu'aucun tableau ou sélection
     * n'est affiché avant de montrer les éléments spécifiques requis.
     */
    @FXML
    private void masquerTousLesTableauxEtSelections() {
        tableauSalles.setVisible(false);
        tableauReservations.setVisible(false);
        tableauActivites.setVisible(false);
        tableauEmployes.setVisible(false);
        SelectionSalles.setVisible(false);
        SelectionActivites.setVisible(false);
        SelectionEmployes.setVisible(false);
        SelectionReservations.setVisible(false);
    }

    /**
     * Affiche le tableau des salles et la sélection associée dans l'interface utilisateur.
     * Cette méthode rend visible le tableau des salles et la sélection des salles,
     * tout en masquant les autres tableaux et sélections.
     */
    @FXML
    void afficherTableauSalles() {
        afficherTableau(tableauSalles, SelectionSalles);
    }

    /**
     * Affiche le tableau des reservations et la sélection associée dans l'interface utilisateur.
     * Cette méthode rend visible le tableau des reservations et la sélection des reservations,
     * tout en masquant les autres tableaux et sélections.
     */
    @FXML
    void afficherTableauReservations() {
        afficherTableau(tableauReservations, SelectionReservations);
    }

    /**
     * Affiche le tableau des activites et la sélection associée dans l'interface utilisateur.
     * Cette méthode rend visible le tableau des activites et la sélection des activites,
     * tout en masquant les autres tableaux et sélections.
     */
    @FXML
    void afficherTableauActivites() {
        afficherTableau(tableauActivites, SelectionActivites);
    }

    /**
     * Affiche le tableau des employes et la sélection associée dans l'interface utilisateur.
     * Cette méthode rend visible le tableau des employes et la sélection des employes,
     * tout en masquant les autres tableaux et sélections.
     */
    @FXML
    void afficherTableauEmployes() {
        afficherTableau(tableauEmployes, SelectionEmployes);
    }

    /**
     * Gère le clic sur le bouton "Filtrer".
     * Applique les filtres en fonction du critère et de la valeur sélectionnés.
     */
    @FXML
    void clickFiltrer() {
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
        tableauReservations.setItems(FXCollections.observableArrayList(reservationsFiltrees));
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
        tableauReservations.setItems(FXCollections.observableArrayList(reservationsFiltrees));
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
     *
     * @FXML Indique que cette méthode est liée à un élément de l'interface via JavaFX.
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
}