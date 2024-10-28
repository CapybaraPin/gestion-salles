/*
 * GestionDonnees.java              21/10/2024
 * Tom GUTTIEREZ Jules VIALAS, pas de copyrights
 */

package iut.info2.saltistique.modele;

import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.modele.Regex;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Classe responsable de la gestion des données, y compris l'importation et l'exportation des fichiers.
 * Elle contient des expressions régulières pour valider les entrées des employés, salles, activités et réservations.
 */
public class GestionDonnees implements Serializable {

    /** TODO : la javadoc */
    private static final long serialVersionUID = 1L;

    /** Délimiteur utilisé dans les fichiers CSV */
    private static final String DELIMITEUR = ";";

    /** Message d'erreur affiché lorsque le nombre de fichiers fournis est incorrect. */
    private static final String ERREUR_NB_CHEMINS_FICHIERS =
            "Erreur : Le nombre de fichiers à fournir n'est pas respecté";

    private Fichier[] fichiers;
    private String[] typeFichier;
    private String[][] contenu;
    private int[] index;
    private String[] donnees;

    private Salle[] tempSalles;
    private Activite[] tempActivites;
    private Utilisateur[] tempUtilisateurs;
    private Reservation[] tempReservations;

    private Salle[] salles;
    private Activite[] activites;
    private Utilisateur[] utilisateurs;
    private Reservation[] reservations;

    private int indexReservation;
    private int compteur;

    /**
     * Constructeur de la classe GestionDonnees.
     */
    public GestionDonnees() {
    }

    /**
     *
     * @param cheminFichiers
     */
    private void initialiserDonnees(String[] cheminFichiers) {
        fichiers = new Fichier[cheminFichiers.length];
        typeFichier = new String[3];

        for(int rang = 0; rang < cheminFichiers.length; rang++) {
            try {
                fichiers[rang] = new Fichier(cheminFichiers[rang]);
                contenu[rang] = fichiers[rang].contenuFichier();
                typeFichier[rang] = reconnaitreEntete(contenu[rang][0],DELIMITEUR);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int rang = 0; rang < typeFichier.length; rang++) {
            for (int secondRang = rang + 1; secondRang < typeFichier.length; secondRang++) {
                if (typeFichier[rang] != null && typeFichier[rang].equals(typeFichier[secondRang])) {
                    throw new IllegalArgumentException("Vous donnez plusieurs fois le même type de fichier.");
                }
            }
        }
    }

    /**
     * Importe les données depuis les fichiers spécifiés et les stocke dans les tableaux finaux
     * des employés, salles, activités, et réservations après avoir vérifié leur validité.
     * @param cheminFichiers Un tableau de chaînes de caractères représentant les chemins vers les fichiers à importer.
     */
    public void importerDonnees(String[] cheminFichiers) {

        if(cheminFichiers.length != 4) {
            throw new IllegalArgumentException("Vous n'avez pas fourni tout les fichiers requis");
        }

        try {
            initialiserDonnees(cheminFichiers);
        } catch (IllegalArgumentException exception) {
            exception.getMessage();
        }

        for (int rang = 0; rang < typeFichier.length; rang++) {

            switch (typeFichier[rang]) {
                case "employes":
                    tempUtilisateurs = new Utilisateur[contenu[rang].length - 1];
                    int indexEmployes = 0;
                    for (int secondRang = 1; secondRang < contenu[rang].length; secondRang++) {
                        if (estLigneComplete(contenu[rang][secondRang], DELIMITEUR, typeFichier[rang])) {
                            donnees = contenu[rang][secondRang].split(DELIMITEUR);
                            tempUtilisateurs[indexEmployes++] = new Utilisateur(donnees[0], donnees[1], donnees[2], donnees[3]);
                        }
                    }
                    utilisateurs = Arrays.copyOf(tempUtilisateurs, indexEmployes);
                    break;

                case "salles":
                    tempSalles = new Salle[contenu[rang].length - 1];
                    int indexSalles = 0;
                    for (int secondRang = 1; secondRang < contenu[rang].length; secondRang++) {
                        if (estLigneComplete(contenu[rang][secondRang], DELIMITEUR, typeFichier[rang])) {
                            donnees = contenu[rang][secondRang].split(DELIMITEUR);
                            String[] logiciels = donnees[7].split(DELIMITEUR);
                            tempSalles[indexSalles++] = new Salle(donnees[0], donnees[1], Integer.valueOf(donnees[2]),
                                    Boolean.valueOf(donnees[3]), Boolean.valueOf(donnees[4]), Boolean.valueOf(donnees[8]),
                                    new GroupeOrdinateurs(Integer.valueOf(donnees[5]), donnees[6], logiciels));
                        }
                    }
                    salles = Arrays.copyOf(tempSalles, indexSalles);
                    break;

                case "activites":
                    tempActivites = new Activite[contenu[rang].length - 1];
                    int indexActivites = 0;
                    for (int secondRang = 1; secondRang < contenu[rang].length; secondRang++) {
                        if (estLigneComplete(contenu[rang][secondRang], DELIMITEUR, typeFichier[rang])) {
                            donnees = contenu[rang][secondRang].split(DELIMITEUR);
                            tempActivites[indexActivites++] = new Activite(donnees[0], donnees[1]);
                        }
                    }
                    activites = Arrays.copyOf(tempActivites, indexActivites);
                    break;

                case "reservations":
                    indexReservation = rang;
                    break;
            }
        }
        tempReservations = new Reservation[contenu[indexReservation].length - 1];
        int indexReservations = 0;
        for (int secondRang = 1; secondRang < contenu[indexReservation].length; secondRang++) {
            if (estLigneComplete(contenu[indexReservation][secondRang], DELIMITEUR, typeFichier[indexReservation])) {
                donnees = contenu[indexReservation][secondRang].split(DELIMITEUR);
                // tempReservations[indexReservations++] = new Reservation(donnees); // TODO: Créer la réservation
            }
        }
        reservations = Arrays.copyOf(tempReservations, indexReservations);

    }


    /**
     * Importe les données depuis une adresse IP et un port spécifiés.
     *
     * @param ip l'adresse IP du serveur
     * @param port le port à utiliser pour l'importation
     */
    public void importerDonnees(String ip, int port) {
        GestionDonnees gestionDonnee;
        try {
            Client client = new Client(ip, port);
            gestionDonnee = (GestionDonnees) client.recevoir();
            activites = gestionDonnee.getActivites();
            client.arreter();
        } catch (IOException | ClassNotFoundException e) {
            //TODO gestion des exception (popup notification ?)
            e.printStackTrace();
        }
    }

    /**
     * Exporte les données vers un port spécifié.
     *
     * @param port le port à utiliser pour l'exportation
     */
    public void exporterDonnees(int port) {
        try {
            Serveur serveur = new Serveur(port);
            serveur.envoyer(this);
            serveur.arreter();
        } catch (IOException e) {
            //TODO gestion des exception (popup notification ?)
            e.printStackTrace();
        }
    }

    /*
    public Salle[] getSalles() {
        // TODO implement here
        return null;
    }

    public Utilisateur[] getUtilisateurs() {
        // TODO implement here
        return null;
    }

    */
    public Activite[] getActivites() {
        // TODO implement here
        return this.activites;
    }

    public Reservation[] getReservations() {
        // TODO implement here
        return null;
    }

    /**
     * Reconnaît le type de fichier à partir de l'en-tête et du délimiteur fournis.
     *
     * @param entete l'en-tête du fichier
     * @param delimiteur le délimiteur utilisé dans le fichier
     * @return le type de fichier correspondant à l'en-tête
     * @throws IllegalArgumentException si l'en-tête ou le délimiteur est nul ou vide
     */
    public static String reconnaitreEntete(String entete, String delimiteur) {
        if (entete == null || delimiteur == null) {
            throw new IllegalArgumentException("Les arguments ne peuvent pas être nuls");
        }
        if (entete.isEmpty()) {
            throw new IllegalArgumentException("L'en-tête ne peut pas être vide");
        }

        return switch (entete.split(delimiteur).length) {
            case 4 -> "employes";
            case 9 -> "salles";
            case 2 -> "activites";
            case 7 -> "reservations";
            default -> null;
        };
    }

    /**
     * Vérifie si une ligne donnée est complète selon le type de fichier spécifié.
     *
     * @param ligne la ligne à valider
     * @param delimiteur le délimiteur utilisé dans la ligne
     * @param typeFichier le type de fichier (employes, salles, activites, reservations)
     * @return true si la ligne est valide, sinon false
     * @throws IllegalArgumentException si le type de fichier est inconnu
     */
    public static boolean estLigneComplete(String ligne, String delimiteur, String typeFichier) {
        if (ligne == null || delimiteur == null || typeFichier == null) {
            throw new IllegalArgumentException("Les arguments ne peuvent pas être nuls.");
        }

        String regex = switch (typeFichier) {
            case "employes" -> Regex.EMPLOYES.getRegex(";");
            case "salles" -> Regex.SALLES.getRegex(";");
            case "activites" -> Regex.ACTIVITES.getRegex(";");
            case "reservations" -> Regex.RESERVATIONS.getRegex(";");
            default -> throw new IllegalArgumentException("Type de fichier inconnu : " + typeFichier);
        };

        if (ligne.matches(regex)) {
            if ("reservations".equals(typeFichier)) {
                String[] attributs = ligne.split(delimiteur);
                Date dateDebut = construireDate(attributs[4], attributs[5]); // Indice dateDebut
                Date dateFin = construireDate(attributs[4], attributs[6]); // Indice dateFin
                return dateDebut.before(dateFin);
            }
            return true;
        }

        // todo : verifier pour salles si ordinateur alors tous les champs doivent etre remplis
        //      : de même pour reservations

        return false;
    }

    /**
     * Construit une date à partir d'une chaîne de caractères représentant une date et une heure.
     *
     * @param date la date au format "JJ/MM/AAAA"
     * @param heureMinutes l'heure au format "XXhXX"
     * @return un objet Date correspondant à la date et l'heure fournies
     * @throws IllegalArgumentException si le format est incorrect ou si la date n'est pas valide
     */
    public static Date construireDate(String date, String heureMinutes) {
        if (date == null || heureMinutes == null) {
            throw new IllegalArgumentException("Les arguments ne peuvent pas être nuls.");
        }

        if (!date.matches("^\\d{2}/\\d{2}/\\d{4}$") || !heureMinutes.matches("^\\d{2}h\\d{2}$")) {
            throw new IllegalArgumentException("Le format de la date ou de l'heure est incorrect.");
        }

        String[] dateDecoupe = date.split("/");
        String[] heureDecoupe = heureMinutes.split("h");

        int jour = Integer.parseInt(dateDecoupe[0]);
        int mois = Integer.parseInt(dateDecoupe[1]) - 1; // Les mois commencent à 0 en Java
        int annee = Integer.parseInt(dateDecoupe[2]);
        int heures = Integer.parseInt(heureDecoupe[0]);
        int minutes = Integer.parseInt(heureDecoupe[1]);

        Date dateTransformee = new Date(annee - 1900, mois, jour, heures, minutes); // Les années commencent à 1900

        if (dateTransformee.getDate() != jour || dateTransformee.getMonth() != mois ||
                dateTransformee.getYear() != (annee - 1900) || dateTransformee.getHours() != heures ||
                dateTransformee.getMinutes() != minutes) {
            throw new IllegalArgumentException("La date n'est pas valide.");
        }

        return dateTransformee;
    }
}
