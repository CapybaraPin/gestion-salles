/*
 * Importation.java              13/11/2024
 * Tom GUTIERREZ Hugo ROBLES, pas de copyrights
 */

package iut.info2.saltistique.modele.donnees;


import iut.info2.saltistique.modele.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;

/**
 * <p><b>Importation</b> est responsable de l'importation des données
 * dans l'application. Elle interagit directement avec l'objet GestionDonnees
 * pour y intégrer les données importées. Elle assure la validation
 * des données avant leur intégration. La classe Importation prend également
 * en charge la gestion des erreurs courantes liées à l'importation</p>
 * <br>
 * <p>Les fichiers pris en charge doivent avoir une extension .csv.</p>
 */
public class Importation {

    /** Message d'erreur affiché lorsque le nombre de fichiers fournis est incorrect. */
    private static final String ERREUR_NB_CHEMINS_FICHIERS =
            "Le nombre de fichiers à fournir n'est pas respecté";

    private static final String DELIMITEUR = ";";
    private Fichier[] fichiers;
    private Fichier fichierActivites;
    private Fichier fichierReservations;
    private Fichier fichierSalles;
    private Fichier fichierUtilisateurs;
    private GestionDonnees donnees;

    /**
     * Constructeur de la classe Importation.
     * Initialise l'objet GestionDonnees et les tableaux de fichiers.
     * @param donnees l'objet GestionDonnees
     */
    public Importation(GestionDonnees donnees) {
        this.donnees = donnees;
        this.fichiers = new Fichier[4];
    }

    /**
     * Importe les données à partir des fichiers spécifiés.
     * Les fichiers sont stockés dans les attributs de la classe.
     * Les données sont ensuite ajoutées à l'objet GestionDonnees.
     *
     * Les fichiers doivent être fournis par 4, et doivent être non
     * vide.
     *
     * @param cheminFichiers les chemins des fichiers à importer
     * @throws IOException si une erreur survient lors de la lecture des fichiers
     */
    public void importerDonnees(String[] cheminFichiers) throws IOException {
        donnees.viderDonnees();
        ajouterFichier(cheminFichiers);

        for (Fichier fichier : fichiers) {
            System.out.println(fichier.getFichierExploite().getName());
        }

        donnees.setFichiers(fichiers);

        ajouterDonnees(fichierActivites.contenuFichier(), this::ajouterActivite);
        ajouterDonnees(fichierSalles.contenuFichier(), this::ajouterSalle);
        ajouterDonnees(fichierUtilisateurs.contenuFichier(), this::ajouterUtilisateur);
        ajouterDonnees(fichierReservations.contenuFichier(), this::ajouterReservation);
    }


    private void ajouterDonnees(String[] contenuFichier, Consumer<String> ajouterLigne) {
        for (int i = 1; i < contenuFichier.length; i++) {
            ajouterLigne.accept(contenuFichier[i]);
        }
    }

    /**
     * Permet de stocker les fichiers dans les attributs de la classe
     * fichierActivites, fichierReservations, fichierSalles, fichierUtilisateurs
     * Si un fichier n'est pas reconnu alors une exception est levée et
     * tous les fichiers sont remis à null
     * @param cheminFichiers
     * @throws IllegalArgumentException si le nombre de fichiers n'est pas égal à 4
     * @throws IllegalArgumentException si le fichier n'est pas reconnu
     * @throws IOException si une erreur survient lors de la lecture du fichier
     */
    public void ajouterFichier(String[] cheminFichiers) throws IOException { // TODO : renommer la méthode ajouterFichiers
        String erreurSalles;
        String erreurActivites;
        String erreurReservations;
        String erreurUtilisateurs;

        if (cheminFichiers == null) {
            throw new IllegalArgumentException("Les chemins des fichiers ne peuvent pas être nuls.");
        }

        if (cheminFichiers.length != 4) {
            throw new IllegalArgumentException(ERREUR_NB_CHEMINS_FICHIERS);
        }

        for (int i = 0; i < 4; i++) {
            fichiers[i] = new Fichier(cheminFichiers[i]);
        }

        for (int i = 0; i < 4; i++) {
            switch (fichiers[i].contenuFichier()[0].split(DELIMITEUR).length) {
                case 4:
                    if (fichierUtilisateurs != null) {
                        erreurUtilisateurs = "Vous avez fourni plusieurs fichiers d'employés : "
                                + fichiers[i].getFichierExploite().getName() + " et "
                                + fichierUtilisateurs.getFichierExploite().getName();
                        donnees.viderDonnees();
                        throw new IllegalArgumentException(erreurUtilisateurs);
                    }
                    fichierUtilisateurs = fichiers[i];
                    break;
                case 9:
                    if (fichierSalles != null) {
                        erreurSalles = "Vous avez fourni plusieurs fichiers de salles : "
                                + fichiers[i].getFichierExploite().getName() + " et "
                                + fichierSalles.getFichierExploite().getName();
                        donnees.viderDonnees();
                        throw new IllegalArgumentException(erreurSalles);
                    }
                    fichierSalles = fichiers[i];
                    break;
                case 2:
                    if (fichierActivites != null) {
                        erreurActivites = "Vous avez fourni plusieurs fichiers d'activités : "
                                + fichiers[i].getFichierExploite().getName() + " et "
                                + fichierActivites.getFichierExploite().getName();
                        donnees.viderDonnees();
                        throw new IllegalArgumentException(erreurActivites);
                    }
                    fichierActivites = fichiers[i];
                    break;
                case 7:
                    if (fichierReservations != null) {
                        erreurReservations = "Vous avez fourni plusieurs fichiers de réservations : "
                                + fichiers[i].getFichierExploite().getName() + " et "
                                + fichierReservations.getFichierExploite().getName();
                        donnees.viderDonnees();
                        throw new IllegalArgumentException(erreurReservations);
                    }
                    fichierReservations = fichiers[i];
                    break;
                default:
                    donnees.viderDonnees(); // remise à zéro des fichiers
                    throw new IllegalArgumentException("\"" + fichiers[i].getFichierExploite().getName() + "\" non reconnu");
            }
        }
    }


    /**
     * Ajoute la ligne d'activité spécifiée aux activités.
     * Si la ligne est incorrecte, elle est ajoutée au tableau des lignes incorrectes.
     * Si l'identifiant est déjà utilisé, la ligne est ajoutée au tableau des lignes incorrectes.
     *
     * @param ligne la ligne à ajouter
     */
    private void ajouterActivite(String ligne) {
        String[] attributs;
        int identifiant;
        String messageErreur;
        HashMap<Integer, Activite> activites;

        messageErreur = null;
        attributs = ligne.split(DELIMITEUR, -1);
        identifiant = extraireNombre(attributs[0]);
        activites = donnees.getActivites();
        if (estLigneComplete(ligne, "activites")) {
            if (!activites.containsKey(identifiant)) {
                activites.put(identifiant, creerActivite(ligne));
            } else {
                messageErreur = "Identifiant déjà utilisé";
            }
        } else {
            for (int indiceAttribut = 0; indiceAttribut < attributs.length && messageErreur == null; indiceAttribut++) {
                if (!attributs[indiceAttribut].isEmpty()) {
                    messageErreur = "Ligne incorrecte";
                }
            }
        }

        if (messageErreur != null) {
            donnees.getLignesIncorrectesActivites().add(new String[]{ligne, messageErreur});
        }
    }

    /**
     * Crée une instance d'Activité à partir d'une ligne spécifiée considérée comme valide.
     * Exemple de ligne : "A0000001;Activité 1"
     * @param ligne la ligne contenant les attributs de l'activité
     * @return une nouvelle instance d'Activité
     */
    private Activite creerActivite(String ligne) {
        String[] attributs;
        Activite activite;
        String identifiant, nom;

        attributs = ligne.split(DELIMITEUR, -1);
        identifiant = attributs[0].trim();
        nom = attributs[1].trim();

        activite = new Activite(identifiant, nom);
        return activite;
    }


    /**
     * Ajoute la ligne de salle spécifiée aux salles.
     * Si la ligne est incorrecte, elle est ajoutée au tableau des lignes incorrectes.
     * Si l'identifiant est déjà utilisé, la ligne est ajoutée au tableau des lignes incorrectes.
     *
     * @param ligne la ligne à ajouter
     */
    private void ajouterSalle(String ligne) {
        String[] attributs;
        int identifiant;
        String messageErreur;
        HashMap<Integer, Salle> salles;

        messageErreur = null;
        attributs = ligne.split(DELIMITEUR, -1);
        if (estLigneComplete(ligne, "salles")) {
            identifiant = extraireNombre(attributs[0]);
            salles = donnees.getSalles();

            if (!salles.containsKey(identifiant)) { // Vérifie que la salle n'existe pas déjà
                salles.put(identifiant, creerSalle(ligne));
            } else {
                messageErreur = "Identifiant déjà utilisé";
            }
        }  else {
            for (int indiceAttribut = 0; indiceAttribut < attributs.length && messageErreur == null; indiceAttribut++) {
                // Vérifie qu'au moins un attribut n'est pas vide pour déterminer si la ligne est incorrecte
                // si tous les attributs sont vides, la ligne est ignorée
                if (!attributs[indiceAttribut].isEmpty()) {
                    messageErreur = "Ligne incorrecte";
                }
            }
        }

        if (messageErreur != null) {
            donnees.getLignesIncorrectesSalles().add(new String[]{ligne, messageErreur});
        }
    }


    /**
     * Crée une instance de Salle à partir d'une ligne spécifiée considérée comme valide.
     * Si la ligne contient des informations sur les ordinateurs, une instance de GroupeOrdinateurs est créée.
     *
     * Exemple de ligne pour une salle avec ordinateurs :
     * "000001;Salle 1;30;oui;oui;10;PC;Office 365, Adobe Photoshop;oui"
     * Exemple de ligne pour une salle sans ordinateurs :
     * "000002;Salle 2;50;non;non;;;;"
     *
     * @param ligne la ligne contenant les attributs de la salle
     * @return une nouvelle instance de Salle
     */
    private Salle creerSalle(String ligne) {
        String[] attributs;
        Salle salle;
        GroupeOrdinateurs ordinateurs;
        String identifiant, nom, type;
        String[] logiciels;
        int capacite, nombreOrdinateurs;
        boolean videoProjecteur, ecranXXL, imprimante;

        attributs = ligne.split(DELIMITEUR, -1);
        if (ligne.matches(Regex.ORDINATEURS.getRegex(DELIMITEUR))) {
            nombreOrdinateurs = Integer.parseInt(attributs[5]);
            type = attributs[6].trim();
            logiciels = attributs[7].split(", ");
            imprimante = attributs[8].toLowerCase().charAt(0) == 'o';
            ordinateurs = new GroupeOrdinateurs(nombreOrdinateurs, type, logiciels);
        } else {
            ordinateurs = null;
            imprimante = false;
        }
        identifiant = attributs[0].trim();
        nom = attributs[1].trim();
        capacite = Integer.parseInt(attributs[2].trim());
        videoProjecteur = attributs[3].toLowerCase().charAt(0) == 'o';
        ecranXXL = attributs[4].toLowerCase().charAt(0) == 'o';

        salle = new Salle(identifiant, nom, capacite, videoProjecteur, ecranXXL, imprimante, ordinateurs);
        return salle;
    }

    /**
     * Ajoute la ligne de réservation spécifiée aux réservations.
     * Si la ligne est incorrecte, elle est ajoutée au tableau des lignes incorrectes.
     * Si la salle ou l'employé n'existe pas, la réservation est ajoutée au tableau des lignes incorrectes.
     * @param ligne la ligne à ajouter
     */
    private void ajouterReservation(String ligne) {
        String[] attributs;
        int identifiant;
        Reservation reservation;
        String messageErreur;
        HashMap<Integer, Reservation> reservations;

        messageErreur = null;
        attributs = ligne.split(DELIMITEUR, -1);

        if (estLigneComplete(ligne, "reservations")) {
            identifiant = extraireNombre(attributs[0]);
            reservations = donnees.getReservations();
            if (!reservations.containsKey(identifiant)) {  // Vérifie que la réservation n'existe pas déjà
                reservation = switch (attributs[3]) {
                    case "formation" -> creerFormation(ligne);
                    case "location", "prêt" -> creerLocation(ligne);
                    default -> creerReservation(ligne);
                };
                if (reservation != null) {
                    reservations.put(identifiant, reservation);
                } else {
                    messageErreur = "Erreur lors de la création de la réservation"; // TODO : Ajouter un message d'erreur plus précis
                }
            } else {
                messageErreur = "Identifiant déjà utilisé";
            }
        }  else {
            for (int indiceAttribut = 0; indiceAttribut < attributs.length && messageErreur == null; indiceAttribut++) {
                // Vérifie qu'au moins un attribut n'est pas vide pour déterminer si la ligne est incorrecte
                // si tous les attributs sont vides, la ligne est ignorée
                if (!attributs[indiceAttribut].isEmpty()) {
                    messageErreur = "Ligne incorrecte";
                }
            }
        }

        if (messageErreur != null) {
            donnees.getLignesIncorrectesReservations().add(new String[]{ligne, messageErreur});
        }
    }

    /**
     * Crée une instance de Réservation à partir d'une ligne spécifiée considérée comme valide.
     * Exemple de ligne : "R000001;000001;E123456;réunion;01/01/2024;08h00;12h00;Réunion de travail;;;;"
     *
     * Il récupère les salles, utilisateurs et activités correspondant aux identifiants fournis.
     * Si l'un des identifiants n'existe pas, la réservation n'est pas créée.
     * et null est retourné.
     *
     * @param ligne la ligne contenant les attributs de la réservation
     * @return une nouvelle instance de Réservation
     */
    private Reservation creerReservation(String ligne) {
        String[] attributs;
        Reservation reservation;
        String identifiant, description;
        LocalDateTime dateDebut, dateFin;
        Salle salle;
        Activite activite;
        Utilisateur utilisateur;
        Collection<Activite> activites;

        attributs = ligne.split(DELIMITEUR, -1);
        salle = donnees.getSalles().get(extraireNombre(attributs[1]));
        utilisateur = donnees.getUtilisateurs().get(extraireNombre(attributs[2]));
        activites = donnees.getActivites().values();
        activite = null;
        for (Activite a : activites) {
            if (a.getNom().toLowerCase().contains(attributs[3].toLowerCase())) {
                activite = a;
                break; // TODO : Chercher une alternative pour sortir de la boucle
            }
        }

        if (salle == null || utilisateur == null || activite == null) {
            return null; // TODO : Ajouter un message d'erreur
        }

        try {
            dateDebut = construireDate(attributs[4].trim(), attributs[5].trim());
            dateFin = construireDate(attributs[4].trim(), attributs[6].trim());
        } catch (IllegalArgumentException e) {
            return null; // TODO : Ajouter un message d'erreur
        }

        identifiant = attributs[0].trim();
        description = attributs[7].trim();

        reservation = new Reservation(identifiant, dateDebut, dateFin, description, salle, activite, utilisateur);
        return reservation;
    }

    /**
     * Crée une instance de Location à partir d'une ligne spécifiée considérée comme valide.
     * Exemple de ligne : "R000001;000001;E123456;location;01/01/2024;08h00;12h00;Réunion de travail;DUPONT;Jean;0123456789"
     *
     * Il instancie une réservation à partir de la ligne spécifiée.
     * Il récupère les salles, utilisateurs et activités correspondant aux identifiants fournis via
     * les méthodes getSalles(), getUtilisateurs() et getActivites().
     *
     * @param ligne la ligne contenant les attributs de la location
     * @return une nouvelle instance de Location
     */
    private Reservation creerLocation(String ligne) {
        String[] attributs;
        Reservation reservation;
        Reservation location;
        String identifiant, description, nomOrganisme, nomInterlocuteur, prenomInterlocuteur, telephoneInterlocuteur;
        LocalDateTime dateDebut, dateFin;
        Salle salle;
        Activite activite;
        Utilisateur utilisateur;

        attributs = ligne.split(DELIMITEUR, -1);
        reservation = creerReservation(ligne);
        if (reservation == null) {
            return null;
        }
        salle = reservation.getSalle();
        utilisateur = reservation.getUtilisateur();
        activite = reservation.getActivite();
        identifiant = reservation.getIdentifiant();
        dateDebut = reservation.getDateDebut();
        dateFin = reservation.getDateFin();
        description = attributs[11].trim();
        nomOrganisme = attributs[7].trim();
        nomInterlocuteur = attributs[8].trim();
        prenomInterlocuteur = attributs[9].trim();
        telephoneInterlocuteur = attributs[10].trim();

        location = new Location(identifiant, dateDebut, dateFin, description, salle, activite, utilisateur,
                nomOrganisme, nomInterlocuteur, prenomInterlocuteur, telephoneInterlocuteur);
        return location;
    }

    /** TODO : la javadoc */
    private Reservation creerFormation(String ligne) {
        String[] attributs;
        Reservation reservation;
        Reservation formation;
        String identifiant, description, nomFormateur, prenomFormateur, telephoneFormateur;
        LocalDateTime dateDebut, dateFin;
        Salle salle;
        Activite activite;
        Utilisateur utilisateur;

        attributs = ligne.split(DELIMITEUR, -1);
        reservation = creerReservation(ligne);
        if (reservation == null) {
            return null;
        }
        salle = reservation.getSalle();
        utilisateur = reservation.getUtilisateur();
        activite = reservation.getActivite();
        identifiant = reservation.getIdentifiant();
        dateDebut = reservation.getDateDebut();
        dateFin = reservation.getDateFin();
        description = attributs[7].trim();
        nomFormateur = attributs[8].trim();
        prenomFormateur = attributs[9].trim();
        telephoneFormateur = attributs[10].trim();

        formation = new Formation(identifiant, dateDebut, dateFin, description, salle, activite, utilisateur,
                nomFormateur, prenomFormateur, telephoneFormateur);
        return formation;
    }


    /**
     * Ajoute la ligne d'utilisateur spécifiée aux utilisateurs.
     * Si la ligne est incorrecte, elle est ajoutée au tableau des lignes incorrectes.
     * @param ligne la ligne à ajouter
     */
    private void ajouterUtilisateur(String ligne) {
        String[] attributs;
        int identifiant;
        Utilisateur utilisateur;
        String messageErreur;
        HashMap<Integer, Utilisateur> utilisateurs;

        messageErreur = null;
        attributs = ligne.split(DELIMITEUR, -1);
        if (estLigneComplete(ligne, "employes")) {
            identifiant = extraireNombre(attributs[0]);
            utilisateurs = donnees.getUtilisateurs();
            if (!utilisateurs.containsKey(identifiant)) {  // Vérifie que l'utilisateur n'existe pas déjà
                utilisateur = creerUtilisateur(ligne);
                utilisateurs.put(identifiant, utilisateur);
            } else {
                messageErreur = "Identifiant déjà utilisé";
            }
        }  else {
            for (int indiceAttribut = 0; indiceAttribut < attributs.length && messageErreur == null; indiceAttribut++) {
                // Vérifie qu'au moins un attribut n'est pas vide pour déterminer si la ligne est incorrecte
                // si tous les attributs sont vides, la ligne est ignorée
                if (!attributs[indiceAttribut].isEmpty()) {
                    messageErreur = "Ligne incorrecte";
                }
            }
        }

        if (messageErreur != null) {
            donnees.getLignesIncorrectesUtilisateurs().add(new String[]{ligne, messageErreur});
        }
    }

    /**
     * Crée une instance d'Utilisateur à partir d'une ligne spécifiée considérée comme valide.
     * Exemple de ligne : "E123456;DUPONT;Jean;0123456789"
     * @param ligne la ligne contenant les attributs de l'utilisateur
     * @return une nouvelle instance d'Utilisateur
     */
    private Utilisateur creerUtilisateur(String ligne) {
        String[] attributs;
        Utilisateur utilisateur;
        String identifiant, nom, prenom, telephone;

        attributs = ligne.split(DELIMITEUR, -1);
        identifiant = attributs[0].trim();
        nom = attributs[1].trim();
        prenom = attributs[2].trim();
        telephone = attributs[3].trim();

        utilisateur = new Utilisateur(identifiant, nom, prenom, telephone);
        return utilisateur;
    }


    /**
     * Vérifie si une ligne donnée est complète selon le type de fichier spécifié.
     * Si le type de fichier est "reservations", la méthode vérifie également que la date de début est antérieure à la date de fin.
     *
     * @param ligne la ligne à valider
     * @param typeFichier le type de fichier (employes, salles, activites, reservations)
     * @return true si la ligne est valide, sinon false
     * @throws IllegalArgumentException si le type de fichier est inconnu
     */
    public boolean estLigneComplete(String ligne, String typeFichier) {
        if (ligne == null || typeFichier == null) {
            throw new IllegalArgumentException("Les arguments ne peuvent pas être nuls.");
        }

        String regex = switch (typeFichier) {
            case "employes" -> Regex.EMPLOYES.getRegex(DELIMITEUR);
            case "salles" -> Regex.SALLES.getRegex(DELIMITEUR);
            case "activites" -> Regex.ACTIVITES.getRegex(DELIMITEUR);
            case "reservations" -> Regex.RESERVATIONS.getRegex(DELIMITEUR);
            default -> throw new IllegalArgumentException("Type de fichier inconnu : " + typeFichier);
        };

        if (ligne.matches(regex)) {
            if (typeFichier.equals("reservations")) {
                String[] attributs = ligne.split(DELIMITEUR);
                LocalDateTime dateDebut = construireDate(attributs[4], attributs[5]); // Indice dateDebut
                LocalDateTime dateFin = construireDate(attributs[4], attributs[6]); // Indice dateFin
                return dateDebut.isBefore(dateFin);
            }
            return true;
        }

        return false;
    }


    /**
     * Construit une date à partir d'une chaîne de caractères représentant une date et une heure.
     *
     * @param date la date au format "JJ/MM/AAAA"
     * @param heureMinutes l'heure au format "XXhXX"
     * @return un objet LocalDateTime correspondant à la date et l'heure fournies
     * @throws IllegalArgumentException si le format est incorrect ou si la date n'est pas valide
     */
    public static LocalDateTime construireDate(String date, String heureMinutes) {
        DateTimeFormatter formatDate;
        DateTimeFormatter formatHeure;
        LocalDate dateConvertie;
        LocalTime heureConvertie;


        // Vérifie que les arguments ne sont pas nuls
        if (date == null || heureMinutes == null) {
            throw new IllegalArgumentException("Les arguments ne peuvent pas être nuls.");
        }

        // Définit les formats attendus pour la date et l'heure
        formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        formatHeure = DateTimeFormatter.ofPattern("H'h'mm");
        try {
            // convertit les chaînes de caractères en LocalDate et LocalTime
            dateConvertie = LocalDate.parse(date, formatDate);
            heureConvertie = LocalTime.parse(heureMinutes, formatHeure);


        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Le format de la date ou de l'heure est incorrect.", e);
        }

        return LocalDateTime.of(dateConvertie, heureConvertie);
    }

    /**
     * Récupère le nombre d'une chaines de caractères
     * Exemple : "E123456" -> 123456
     *         : "000012" -> 12
     * @param identifiantComplet la chaine de caractères
     * @return l'identifiant sous forme d'entier
     */
    public static int extraireNombre(String identifiantComplet) {
        int nb;
        String nbStr;

        nbStr = identifiantComplet.replaceAll("[^0-9]", "");
        nb = -1;
        if (!nbStr.isEmpty()) {
            nb = Integer.parseInt(nbStr);
        }

        return nb;
    }

    public Fichier[] getFichiers() {
        return fichiers;
    }
}
