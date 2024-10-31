/*
 * Fichier.java          21/10/2024
 * IUT DE RODEZ            Pas de copyrights
 */
package iut.info2.saltistique.modele;

import java.io.*;

/**
 * La classe Fichier gère les opérations de lecture
 * sur des fichiers texte. Elle permet de lire le contenu
 * d'un fichier ligne par ligne et d'afficher le contenu du fichier.
 * <br>
 * Les fichiers pris en charge doivent avoir une extension .csv.
 * La classe gère également les erreurs courantes liées à l'ouverture,
 * la lecture et la fermeture des fichiers.
 */
public class Fichier {

    /** fichier courant de l'instance */
    private File fichierExploite;

    /** lecteur du fichier courant de l'instance */
    private FileReader lecteurFichier;

    /** tampon du fichier courant de l'instance */
    private BufferedReader tamponFichier;

    /** suffixe des fichiers pris en charge */
    private static final String SUFFIXE_FICHIER = ".csv";

    /** message d'erreur pour l'ouverture d'un fichier */
    private static final String ERREUR_OUVERTURE_FICHIER = "Erreur lors de l'ouverture du fichier : ";

    /** message d'erreur pour la fermeture d'un fichier */
    private static final String ERREUR_FERMETURE_FICHIER = "Erreur lors de la fermeture du fichier.";

    /** message d'erreur pour la lecture du contenu d'un fichier */
    private static final String ERREUR_CONTENU_FICHIER = "Erreur lors de la lecture du contenu du fichier.";

    /** message d'erreur pour l'extension d'un fichier */
    private static final String ERREUR_EXTENSION_FICHIER = "Extension de fichier non valide.";

    /** message d'erreur pour le format des paramètres */
    private static final String ERREUR_FORMAT_PARAMETRE = "Format de paramètre non valide.";

    /**
     * Constructeur de la classe Fichier.
     * <br>
     * Initialise les objets nécessaires pour lire le fichier
     * spécifié par le chemin fourni.
     * <p>
     * Vérifie si le chemin du fichier est valide et si le
     * fichier a une extension correcte (.csv).
     *
     * @param cheminFichier : Le chemin du fichier à lire.
     *                        Ne doit pas être vide.
     *                        Doit être un fichier texte (.csv).
     *
     * @throws IOException si une erreur survient lors de l'ouverture du fichier.
     * @throws IllegalArgumentException si le chemin du fichier est vide ou si l'extension du fichier n'est pas valide.
     * @throws FileNotFoundException si le fichier spécifié n'existe pas.
     */
    public Fichier(String cheminFichier) throws IOException, IllegalArgumentException {
        if (cheminFichier == null || cheminFichier.isEmpty()) {
            throw new IllegalArgumentException(ERREUR_FORMAT_PARAMETRE);
        }

        fichierExploite = new File(cheminFichier);

        if (!extensionValide()) {
            throw new IllegalArgumentException(ERREUR_EXTENSION_FICHIER);
        }

        if (!fichierExploite.exists()) {
            throw new FileNotFoundException(ERREUR_OUVERTURE_FICHIER + cheminFichier);
        }

        try {
            lecteurFichier = new FileReader(cheminFichier);
            tamponFichier = new BufferedReader(lecteurFichier);
        } catch (IOException e) {
            throw new IOException(ERREUR_OUVERTURE_FICHIER + cheminFichier, e);
        }
    }



    /**
     * Vérifie si le fichier a une extension valide.
     *
     * @return true si l'extension du fichier est valide,
     * 	       false si l'extension du fichier n'est pas valide.
     */
    public boolean extensionValide(){
        if (fichierExploite.getName()
                .toLowerCase()
                .endsWith(SUFFIXE_FICHIER)) {
            return true;
        }

        return false;
    }

    /**
     * Lit le contenu d'un fichier texte ligne par ligne et retourne un tableau
     * de chaînes de caractères contenant chaque ligne du fichier.
     * <br>
     * La méthode commence par compter le nombre de lignes dans le fichier
     * afin de créer un tableau de la taille appropriée.
     * Ensuite, elle relit le fichier pour remplir ce tableau avec les lignes lues.
     * <br>
     * Si une erreur survient pendant la lecture du fichier,
     * une exception est levée.
     *
     * @return Un tableau de chaînes de caractères contenant les lignes du fichier
     *
     * @throws IOException si une erreur survient lors de la lecture du contenu du fichier ou de sa fermeture.
     *
     */
    public String[] contenuFichier() throws IOException {
        int nbLignes = 0;
        String ligneAct;

        // Première lecture : initialiser le nombre de lignes
        try {
            while (tamponFichier.readLine() != null) {
                nbLignes++;
            }
        } catch (IOException e) {
            throw new IOException(ERREUR_CONTENU_FICHIER, e);
        }
        if (nbLignes == 0) {
            throw new IOException("Le fichier est vide.");
        }

        // Deuxième lecture : remplir le tableau
        String[] contenu = new String[nbLignes];
        tamponFichier.close();
        lecteurFichier.close();
        try {
            lecteurFichier = new FileReader(fichierExploite);
            tamponFichier = new BufferedReader(lecteurFichier);
        } catch (IOException e) {
            throw new IOException(ERREUR_OUVERTURE_FICHIER + fichierExploite.getName(), e);
        }

        int index = 0;
        try {
            while ((ligneAct = tamponFichier.readLine()) != null) {
                contenu[index] = ligneAct;
                index++;
            }
        } catch (IOException e) {
            throw new IOException(ERREUR_CONTENU_FICHIER, e);
        }

        tamponFichier.close();
        lecteurFichier.close();

        try {
            lecteurFichier = new FileReader(fichierExploite);
            tamponFichier = new BufferedReader(lecteurFichier);
        } catch (IOException e) {
            throw new IOException(ERREUR_OUVERTURE_FICHIER + fichierExploite.getName(), e);
        }

        return contenu;
    }


    /**
     * Affiche le contenu d'un tableau de chaînes de caractères,
     * où chaque élément du tableau représente une ligne d'un fichier.
     * <br>
     * Les sauts de ligne vides sont conservés dans l'affichage.
     */
    public void affichageFichier() throws IOException {
        String[] contenu;
        try {
            contenu = contenuFichier();
        } catch (IOException pbContenu) {
            throw new IOException(ERREUR_CONTENU_FICHIER);
        }

        for (String ligne : contenu) {
            System.out.println(ligne);
        }
    }

    /**
     * Retourne l'objet File représentant le fichier exploité.
     *
     * @return fichier exploité.
     */
    public File getFichierExploite() {
        return fichierExploite;
    }
}