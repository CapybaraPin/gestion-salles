/*
 * GestionDonnees.java              21/10/2024
 * Tom GUTTIEREZ Jules VIALAS, pas de copyrights
 */

package saltistique.modele;

import java.util.Date;

/**
 * Classe responsable de la gestion des données, y compris l'importation et l'exportation des fichiers.
 * Elle contient des expressions régulières pour valider les entrées des employés, salles, activités et réservations.
 */
public class GestionDonnees {

    /** Délimiteur utilisé dans les fichiers CSV */
    private static final String DELIMITEUR = ";";

    /** Expression régulière pour valider les employés */
    private static final String REGEX_EMPLOYES =
            "^E\\d{6}" // Identifiant : commence par 'E' suivi de 6 chiffres
                    + DELIMITEUR // Délimiteur
                    + "[A-Za-z]+" // Nom : une ou plusieurs lettres
                    + DELIMITEUR // Délimiteur
                    + "[A-Za-z]+" // Prénom : une ou plusieurs lettres
                    + DELIMITEUR // Délimiteur
                    + "\\d{4}$"; // Téléphone : exactement 4 chiffres

    /** Expression régulière pour valider les salles */
    private static final String REGEX_SALLES =
            "^([0-9]{8})" // Identifiant : exactement 8 chiffres
                    + DELIMITEUR // Délimiteur
                    + "([^" + DELIMITEUR + "]+)" // Nom de la salle : tout caractère sauf le délimiteur
                    + DELIMITEUR // Délimiteur
                    + "([0-9]+)" // Capacité : un ou plusieurs chiffres
                    + DELIMITEUR // Délimiteur
                    + "([onON][^" + DELIMITEUR + "]*)" // Vidéo : 'o' ou 'n'
                    + DELIMITEUR // Délimiteur
                    + "([onON][^" + DELIMITEUR + "]*)" // Tableau : 'o' ou 'n'
                    + DELIMITEUR // Délimiteur
                    + "([0-9]*)" // Ordinateurs : un ou plusieurs chiffres
                    + DELIMITEUR // Délimiteur
                    + "([^" + DELIMITEUR + "]*)" // Type : tout caractère sauf le délimiteur
                    + DELIMITEUR // Délimiteur
                    + "([^" + DELIMITEUR + "]*)" // Logiciels : tout caractère sauf le délimiteur
                    + DELIMITEUR // Délimiteur
                    + "([onON][^" + DELIMITEUR + "]*|)$"; // Climatisation : optionnelle

    /** Expression régulière pour valider les activités */
    private static final String REGEX_ACTIVITES =
            "^A[0-9]{7}" // Identifiant : commence par 'A' suivi de 7 chiffres
                    + DELIMITEUR // Délimiteur
                    + "[^" + DELIMITEUR + "]+$"; // Nom de l'activité : tout caractère sauf le délimiteur

    /** Expression régulière pour valider les réservations */
    private static final String REGEX_RESERVATIONS =
            "^(R\\d{6})" // Identifiant : 'R' suivi de 6 chiffres
                    + DELIMITEUR // Délimiteur
                    + "(\\d{8})" // Date : format YYYYMMDD
                    + DELIMITEUR // Délimiteur
                    + "(E\\d{6})" // Identifiant de l'employé : 'E' suivi de 6 chiffres
                    + DELIMITEUR // Délimiteur
                    + "([\\p{L} .'-]+)" // Nom : lettres, espaces, points, apostrophes ou tirets
                    + DELIMITEUR // Délimiteur
                    + "(\\d{2}/\\d{2}/\\d{4})" // Date : format JJ/MM/AAAA
                    + DELIMITEUR // Délimiteur
                    + "([01]?\\d|2[0-3])h[0-5]\\d" // Heure de début : format HHhMM
                    + DELIMITEUR // Délimiteur
                    + "([01]?\\d|2[0-3])h[0-5]\\d" // Heure de fin : format HHhMM
                    + DELIMITEUR // Délimiteur
                    + "([\\p{L} .'-]*)" // Commentaire : optionnel
                    + DELIMITEUR // Délimiteur
                    + "([\\p{L} .'-]*)?" // Deuxième commentaire : optionnel
                    + DELIMITEUR // Délimiteur
                    + "([\\p{L} .'-]*)?" // Troisième commentaire : optionnel
                    + DELIMITEUR // Délimiteur
                    + "(\\d{4})?" // Année : optionnelle
                    + DELIMITEUR // Délimiteur
                    + "([\\p{L} .'-]*)?$"; // Dernier champ : optionnel

    private Fichier[] fichiers;

    public GestionDonnees() {
    }

    /**
     * Importe les données depuis un tableau de chemins de fichiers.
     *
     * @param cheminFichiers les chemins des fichiers à importer
     */
    public void importerDonnees(String[] cheminFichiers) {
        // TODO: implémenter ici
    }

    /**
     * Importe les données depuis une adresse IP et un port spécifiés.
     *
     * @param ip l'adresse IP du serveur
     * @param port le port à utiliser pour l'importation
     */
    public void importerDonnees(String ip, int port) {
        // TODO: implémenter ici
    }

    /**
     * Exporte les données vers un port spécifié.
     *
     * @param port le port à utiliser pour l'exportation
     */
    public void exporterDonnees(int port) {
        // TODO: implémenter ici
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

    public Activite[] getActivites() {
        // TODO implement here
        return null;
    }

    public Reservation[] getReservations() {
        // TODO implement here
        return null;
    }
    */

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
            case "employes" -> REGEX_EMPLOYES;
            case "salles" -> REGEX_SALLES;
            case "activites" -> REGEX_ACTIVITES;
            case "reservations" -> REGEX_RESERVATIONS;
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