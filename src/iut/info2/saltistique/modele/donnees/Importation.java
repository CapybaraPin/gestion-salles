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
 * <p>Les fichiers pris en charge doivent avoir une extension .csv.</p>
 */
public class Importation {

    /** Message d'erreur affiché lorsque le nombre de fichiers fournis est incorrect. */
    private static final String ERREUR_NB_CHEMINS_FICHIERS =
            "Le nombre de fichiers à fournir n'est pas respecté";

    /** Délimiteur utilisé pour séparer les attributs dans les fichiers .csv. */
    private static final String DELIMITEUR = ";";

    /** Stocke les fichiers fournis pour l'importation. */
    private Fichier[] fichiers;

    /**
     * Stocke le fichier d'activités fourni pour l'importation.
     * Permet de retrouver facilement le fichier d'activités parmi les fichiers fournis.
     */
    private Fichier fichierActivites;

    /**
     * Stocke le fichier de réservations fourni pour l'importation.
     * Permet de retrouver facilement le fichier de réservations parmi les fichiers fournis.
     */
    private Fichier fichierReservations;

    /**
     * Stocke le fichier de salles fourni pour l'importation.
     * Permet de retrouver facilement le fichier de salles parmi les fichiers fournis.
     */
    private Fichier fichierSalles;

    /**
     * Stocke le fichier d'utilisateurs fourni pour l'importation.
     * Permet de retrouver facilement le fichier d'utilisateurs parmi les fichiers fournis.
     */
    private Fichier fichierUtilisateurs;

    /**
     * Stocke l'objet GestionDonnees pour l'importation des données.
     */
    private GestionDonnees donnees;

    /**
     * Constructeur de la classe Importation.
     * Initialise l'objet GestionDonnees et les tableaux de fichiers.
     * @param donnees l'objet GestionDonnees
     * @author Tom GUTIERREZ
     */
    public Importation(GestionDonnees donnees) {
        this.donnees = donnees;
        this.fichiers = new Fichier[4];
    }

    /**
     * Importe les données à partir des fichiers spécifiés.
     * <br>
     * Les fichiers sont stockés dans les attributs de la classe.
     * Les données sont ensuite ajoutées à l'objet GestionDonnees.
     * <br><br>
     * Les fichiers doivent être fournis par 4, et doivent être valide
     * et non vide.
     *
     * @param cheminFichiers les chemins des fichiers à importer
     * @throws IllegalArgumentException si le nombre de fichiers n'est pas égal à 4
     *                                  ou si un fichier n'est pas reconnu
     * @throws IOException si une erreur survient lors de la lecture du fichier
     * @author Tom GUTIERREZ
     */
    public void importerDonnees(String[] cheminFichiers) throws IOException {
        donnees.viderDonnees();
        ajouterFichiers(cheminFichiers);

        donnees.setFichiers(fichiers);

        ajouterDonnees(fichierActivites.contenuFichier(), this::ajouterActivite);
        ajouterDonnees(fichierSalles.contenuFichier(), this::ajouterSalle);
        ajouterDonnees(fichierUtilisateurs.contenuFichier(), this::ajouterUtilisateur);
        ajouterDonnees(fichierReservations.contenuFichier(), this::ajouterReservation);
    }


    /**
     * Permet l'appel des différentes méthodes d'ajout de données pour les fichiers
     * spécifiés dans l'objet {@link GestionDonnees} :
     * {@code fichierActivites}, {@code fichierSalles}, {@code fichierUtilisateurs},
     * {@code fichierReservations}.
     * <br><br>
     * Cette méthode effectue les actions suivantes :
     * <ul>
     *   <li>Itère à travers chaque ligne du fichier passé en paramètre.</li>
     *   <li>Appelle la méthode {@link Consumer#accept(Object)} spécifiée par le paramètre
     *       {@code ajouterLigne} pour chaque ligne du fichier.</li>
     *   <li>Permet l'ajout des données dans la collection appropriée (activités, salles, utilisateurs, réservations).</li>
     * </ul>
     *
     * <br>
     * Cas gérés par la méthode :
     * <ol>
     *   <li>Le paramètre {@code contenuFichier} contient les données du fichier sous forme de tableau de chaînes.</li>
     *   <li>Chaque ligne de ce tableau est passée à la méthode appropriée via le {@code Consumer}
     *       pour ajouter les données dans l'objet {@link GestionDonnees}.</li>
     * </ol>
     *
     * @param contenuFichier le contenu du fichier à ajouter sous forme de tableau de chaînes de caractères.
     *                       Chaque élément du tableau représente une ligne du fichier à traiter.
     * @param ajouterLigne la méthode à appeler pour ajouter une ligne au fichier correspondant.
     *                     Elle est implémentée en utilisant un {@link Consumer} qui accepte une chaîne
     *                     de caractères représentant une ligne du fichier.
     * @author Tom Gutierrez
     */
    private void ajouterDonnees(String[] contenuFichier, Consumer<String> ajouterLigne) {
        for (int i = 1; i < contenuFichier.length; i++) {
            ajouterLigne.accept(contenuFichier[i]);
        }
    }

    /**
     * Permet de stocker les fichiers dans les attributs de la classe
     * {@code fichierActivites}, {@code fichierReservations}, {@code fichierSalles},
     * {@code fichierUtilisateurs}.<br><br>
     * Si un fichier n'est pas reconnu ou si un fichier est fourni plusieurs fois,
     * une exception est levée et tous les fichiers sont remis à {@code null}.
     * <br><br>
     * Cette méthode effectue les actions suivantes :
     * <ul>
     *   <li>Vérifie si le nombre de fichiers fournis est bien égal à 4.</li>
     *   <li>Vérifie si chaque fichier est valide en fonction du nombre d'attributs de chaque ligne.</li>
     *   <li>Assigne chaque fichier à son attribut correspondant ({@link #fichierActivites},
     *       {@link #fichierReservations}, {@link #fichierSalles}, {@link #fichierUtilisateurs}).</li>
     *   <li>Si un fichier n'est pas reconnu, tous les fichiers sont réinitialisés à {@code null}.</li>
     *   <li>Si plusieurs fichiers du même type sont fournis, une exception est levée et les fichiers sont réinitialisés.</li>
     * </ul>
     * <br>
     * Cas gérés par la méthode :
     * <ol>
     *   <li>Si le nombre de fichiers n'est pas égal à 4, une exception {@link IllegalArgumentException} est levée.</li>
     *   <li>Si un fichier contient un nombre d'attributs incorrect (ne correspondant pas à l'un des formats reconnus),
     *       une exception {@link IllegalArgumentException} est levée.</li>
     *   <li>Si plusieurs fichiers du même type sont fournis, une exception est levée et les fichiers sont réinitialisés.</li>
     *   <li>Si un fichier n'est pas reconnu, tous les fichiers sont réinitialisés à {@code null}.</li>
     * </ol>
     *
     * @param cheminFichiers les chemins des fichiers à importer. Doit contenir exactement 4 chemins pour
     *        les fichiers d'activités, réservations, salles et utilisateurs.
     * @throws IllegalArgumentException si le nombre de fichiers n'est pas égal à 4 ou si un fichier
     *                                  n'est pas reconnu ou est fourni plusieurs fois.
     * @throws IOException si une erreur survient lors de la lecture du fichier.
     * @author Tom Gutierrez
     */
    private void ajouterFichiers(String[] cheminFichiers) throws IOException {
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
     * Ajoute la ligne d'{@link Activite} spécifiée aux activités.
     * <br><br>
     * La ligne doit suivre le format attendu pour une activité, incluant l'identifiant et le nom de l'activité.
     * <br><br>
     * Cette méthode effectue les actions suivantes :
     * <ul>
     *   <li>Vérifie si la ligne est complète et correctement formatée à l'aide de la méthode {@link #estLigneComplete(String, String)}.</li>
     *   <li>Extrait l'identifiant de l'activité et vérifie qu'il n'est pas déjà utilisé dans la collection d'activités.</li>
     *   <li>Crée une activité spécifique via la méthode {@link #creerActivite(String)}.</li>
     *   <li>Ajoute l'activité à la collection des activités dans l'objet {@link GestionDonnees} si elle est valide.</li>
     *   <li>Si la ligne est incorrecte ou l'identifiant est déjà utilisé, elle est ajoutée au tableau des lignes incorrectes avec un message d'erreur approprié.</li>
     * </ul>
     * <br>
     * Cas gérés par la méthode :
     * <ol>
     *   <li>Si l'identifiant d'activité existe déjà, un message d'erreur est généré et la ligne est marquée comme incorrecte.</li>
     *   <li>Si la création de l'activité échoue, la ligne est ajoutée au tableau des lignes incorrectes.</li>
     *   <li>Si la ligne est incomplète ou mal formatée, elle est ignorée et signalée comme incorrecte.</li>
     *   <li>Si la ligne est vide, elle est ignorée.</li>
     * </ol>
     *
     * @param ligne la ligne contenant les attributs de l'activité, séparés par un délimiteur
     * @author Tom Gutierrez
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
     * Crée une instance d'{@link Activite} à partir d'une ligne spécifiée considérée comme valide.
     * <br><br>
     * La ligne doit suivre le format attendu pour une activité, incluant l'identifiant et le nom de l'activité.
     * <br><br>
     * Cette méthode effectue les actions suivantes :
     * <ul>
     *   <li>Extrait les attributs de la ligne fournie (identifiant et nom).</li>
     *   <li>Crée une nouvelle instance d'{@link Activite} avec ces attributs.</li>
     *   <li>Retourne l'objet {@link Activite} nouvellement créé.</li>
     * </ul>
     * <br>
     * Cas gérés par la méthode :
     * <ol>
     *   <li>Si la ligne est mal formatée, une exception {@link IllegalArgumentException} peut être lancée.</li>
     *   <li>Si un attribut requis (comme l'identifiant ou le nom) est absent ou mal formé, la méthode échouera.</li>
     * </ol>
     *
     * @param ligne la ligne à ajouter, contenant les attributs de l'activité séparés par un délimiteur
     * @return une nouvelle instance d'{@link Activite}
     * @throws NullPointerException si {@code ligne} est {@code null}
     * @author Tom Gutierrez
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
     * Ajoute une salle à partir d'une ligne spécifiée.
     * <br><br>
     * La ligne doit suivre le format attendu pour une salle.
     * <br><br>
     * Cette méthode effectue les actions suivantes :
     * <ul>
     *   <li>Vérifie si la ligne est complète et correctement formatée.</li>
     *   <li>Extrait l'identifiant de la salle et vérifie qu'il n'est pas déjà utilisé.</li>
     *   <li>Crée une instance de {@link Salle} en fonction des informations extraites de la ligne.</li>
     *   <li>Si la ligne contient des informations sur les ordinateurs, une instance de {@link GroupeOrdinateurs} est créée.</li>
     *   <li>Ajoute la salle à la collection des salles de l'objet {@link GestionDonnees} si elle est valide.</li>
     *   <li>Si la ligne est incorrecte (format ou données invalides), elle est ajoutée au tableau des lignes incorrectes
     *       avec un message d'erreur approprié.</li>
     * </ul>
     * <br>
     * Cas gérés par la méthode :
     * <ol>
     *   <li>Si l'identifiant de la salle existe déjà, un message d'erreur est généré et la ligne est marquée comme incorrecte.</li>
     *   <li>Si la création de la salle échoue (par exemple, attributs manquants ou mal formatés), la ligne est ajoutée au tableau des lignes incorrectes.</li>
     *   <li>Si la ligne est incomplète ou mal formatée, elle est ignorée et signalée comme incorrecte.</li>
     *   <li>Si la ligne est vide, elle est ignorée.</li>
     * </ol>
     *
     * @param ligne la ligne à ajouter, contenant les attributs de la salle séparés par un délimiteur
     * @throws NullPointerException si {@code ligne} est {@code null}
     * @author Tom Gutierrez
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
        } else {
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
     * Crée une instance de {@link Salle} à partir d'une ligne spécifiée considérée comme valide.
     * <br><br>
     * Cette méthode analyse la ligne fournie pour extraire les informations relatives à la salle, telles que :
     * <ul>
     *   <li><b>Identifiant :</b> un identifiant unique pour la salle (par exemple, {@code "000001"}).</li>
     *   <li><b>Nom :</b> le nom de la salle (par exemple, {@code "Salle 1"}).</li>
     *   <li><b>Capacité :</b> la capacité d'accueil de la salle (par exemple, {@code "30"}).</li>
     *   <li><b>Équipements :</b> des informations sur la présence d'équipements comme un vidéoprojecteur,
     *   un écran XXL, et une imprimante.</li>
     * </ul>
     * <br>
     * Si la ligne contient des informations sur des ordinateurs, une instance de {@link GroupeOrdinateurs} est créée
     * pour associer à la salle le nombre d'ordinateurs, leur type, et les logiciels installés.
     * <br><br>
     * Exemple de ligne pour une salle avec ordinateurs :
     * {@code "000001;Salle 1;30;oui;oui;10;PC;Office 365, Adobe Photoshop;oui"}
     * <br><br>
     * Exemple de ligne pour une salle sans ordinateurs :
     * {@code "000002;Salle 2;50;non;non;;;;"}
     * <br><br>
     * Éléments extraits de la ligne :
     * <ul>
     *   <li><b>Identifiant :</b> {@code attributs[0]}</li>
     *   <li><b>Nom :</b> {@code attributs[1]}</li>
     *   <li><b>Capacité :</b> {@code attributs[2]}</li>
     *   <li><b>Vidéoprojecteur :</b> {@code attributs[3]} (valeur "oui" ou "non")</li>
     *   <li><b>Écran XXL :</b> {@code attributs[4]} (valeur "oui" ou "non")</li>
     *   <li><b>Nombre d'ordinateurs :</b> {@code attributs[5]} (seulement pour les salles avec ordinateurs)</li>
     *   <li><b>Type d'ordinateurs :</b> {@code attributs[6]} (seulement pour les salles avec ordinateurs)</li>
     *   <li><b>Logiciels installés :</b> {@code attributs[7]} (seulement pour les salles avec ordinateurs, séparés par ",")</li>
     *   <li><b>Imprimante :</b> {@code attributs[8]} (valeur "oui" ou "non")</li>
     * </ul>
     * <br>
     * Comportement en cas de salle avec ou sans ordinateurs : <br>
     * <ul>
     *     <li>Si la ligne contient des informations sur les ordinateurs (en fonction du format défini dans
     *     {@link Regex#ORDINATEURS}), un objet {@link GroupeOrdinateurs} est créé.
     *     <li>Si la salle ne contient pas d'ordinateurs, le groupe d'ordinateurs est {@code null}.</li>
     * </ul>
     *
     * @param ligne la ligne contenant les attributs de la salle, séparés par un délimiteur
     * @return une nouvelle instance de {@link Salle} initialisée avec les attributs extraits
     * @throws NumberFormatException si la capacité ou le nombre d'ordinateurs n'est pas un nombre valide
     * @throws NullPointerException si {@code ligne} est {@code null}
     * @author Tom GUTIERREZ
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
     * Ajoute une réservation à partir d'une ligne spécifiée.
     * <br><br>
     * La ligne doit suivre le format attendu pour une réservation, incluant les identifiants et détails nécessaires.
     * <br><br>
     * Cette méthode effectue les actions suivantes :
     * <ul>
     *   <li>Vérifie si la ligne est complète et correctement formatée.</li>
     *   <li>Extrait l'identifiant de la réservation et vérifie qu'il n'est pas déjà utilisé.</li>
     *   <li>Crée une réservation spécifique en fonction du type
     *       ({@link Reservation}, {@link Location}, {@link Formation}).</li>
     *   <li>Ajoute la réservation à la collection des réservations de l'objet {@link GestionDonnees}
     *   si elle est valide.</li>
     *   <li>Si la ligne est incorrecte (format ou données invalides), elle est ajoutée au tableau des lignes incorrectes
     *       avec un message d'erreur approprié.</li>
     * </ul>
     * <br>
     * Cas gérés par la méthode :
     * <ol>
     *   <li>Si l'identifiant de réservation existe déjà, un message d'erreur est généré
     *   et la ligne est marquée comme incorrecte.</li>
     *   <li>Si la création de la réservation échoue (par exemple, salle ou utilisateur introuvable),
     *   la ligne est ajoutée au tableau des lignes incorrectes.</li>
     *   <li>Si la ligne est incomplète ou mal formatée, elle est ignorée et signalée comme incorrecte.</li>
     *   <li>Si la ligne est vide, elle est ignorée.</li>
     * </ol>
     *
     * @param ligne la ligne contenant les détails de la réservation à ajouter
     * @throws NullPointerException si {@code ligne} est {@code null}
     * @author Tom GUTIERREZ
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
        } else {
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
     * Crée une instance de la classe {@link Reservation} à partir d'une ligne de texte valide.
     *
     * La ligne doit suivre le format :
     * {@code "R000001;000001;E123456;réunion;01/01/2024;08h00;12h00;Réunion de travail;;;;"}
     * où :
     * <ul>
     *   <li>L'identifiant de la réservation est {@code R000001}.</li>
     *   <li>L'identifiant de la salle est {@code 000001}.</li>
     *   <li>L'identifiant de l'utilisateur est {@code E123456}.</li>
     *   <li>Le type d'activité est {@code réunion}.</li>
     *   <li>La date de la réservation est {@code 01/01/2024}.</li>
     *   <li>L'horaire de début est {@code 08h00}.</li>
     *   <li>L'horaire de fin est {@code 12h00}.</li>
     *   <li>La description est {@code Réunion de travail}.</li>
     * </ul>
     *
     * Cette méthode effectue les étapes suivantes :
     * <ol>
     *   <li>Parse la ligne d'entrée pour extraire les attributs.</li>
     *   <li>Récupère les objets associés (salle, utilisateur, activité) à partir des identifiants.</li>
     *   <li>Construit les dates de début et de fin de la réservation.</li>
     *   <li>Instancie une nouvelle réservation si tous les éléments requis sont valides.</li>
     * </ol>
     *
     * Si l'un des identifiants (salle, utilisateur ou activité) est invalide
     * ou si les dates ne peuvent pas être correctement parsées, la méthode retourne {@code null}.
     *
     * @param ligne la ligne contenant les attributs de la réservation
     * @return une instance de {@link Reservation} si la ligne est valide, {@code null} sinon
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
     * Crée une instance de la classe {@link Location} à partir d'une ligne de texte valide.
     * <br><br>
     * La ligne doit suivre le format :
     * {@code "R000017;00000001;E000008;location;18/10/2024;8h00;13h00;Mairie;Marin;Hector;0666666666;réunion"}
     * où :
     * <ul>
     *   <li>L'identifiant de la réservation est {@code R000017}.</li>
     *   <li>L'identifiant de la salle est {@code 00000001}.</li>
     *   <li>L'identifiant de l'utilisateur est {@code E000008}.</li>
     *   <li>Le type d'activité est {@code location}.</li>
     *   <li>La date de la réservation est {@code 18/10/2024}.</li>
     *   <li>L'horaire de début est {@code 08h00}.</li>
     *   <li>L'horaire de fin est {@code 13h00}.</li>
     *   <li>Le nom de l'organisme est {@code Mairie}.</li>
     *   <li>Le nom de l'interlocuteur est {@code Marin}.</li>
     *   <li>Le prénom de l'interlocuteur est {@code Hector}.</li>
     *   <li>Le téléphone de l'interlocuteur est {@code 0666666666}.</li>
     *   <li>La description est {@code réunion}.</li>
     * </ul>
     * <br>
     * Cette méthode effectue les étapes suivantes :
     * <ol>
     *   <li>Utilise la méthode {@link #creerReservation(String)} pour créer une réservation de base à partir de la ligne.</li>
     *   <li>Récupère les détails supplémentaires nécessaires pour une location, tels que le nom de l'organisme,
     *       le nom, le prénom et le numéro de téléphone de l'interlocuteur.</li>
     *   <li>Instancie et retourne une nouvelle instance de {@code Location} avec tous les attributs nécessaires.</li>
     * </ol>
     * <br>
     * Si la ligne est invalide ou si la réservation de base ne peut pas être créée (par exemple, en raison d'erreurs
     * dans les identifiants ou dans le format des dates), la méthode retourne {@code null}.
     *
     * @param ligne la ligne contenant les attributs de la location
     * @return une instance de {@link Location} si la ligne est valide, {@code null} sinon
     * @author Tom GUTIERREZ
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

    /**
     * Crée une instance de la classe {@link Formation} à partir d'une ligne de texte valide.
     * <br><br>
     * La ligne doit suivre le format :
     * {@code "R000001;000001;E123456;formation;01/01/2024;08h00;12h00;Formation sur le logiciel X;DUPONT;Jean;0123456789"}
     * où :
     * <ul>
     *   <li>L'identifiant de la réservation est {@code R000001}.</li>
     *   <li>L'identifiant de la salle est {@code 000001}.</li>
     *   <li>L'identifiant de l'utilisateur est {@code E123456}.</li>
     *   <li>Le type d'activité est {@code formation}.</li>
     *   <li>La date de la réservation est {@code 01/01/2024}.</li>
     *   <li>L'horaire de début est {@code 08h00}.</li>
     *   <li>L'horaire de fin est {@code 12h00}.</li>
     *   <li>La description est {@code Formation sur le logiciel X}.</li>
     *   <li>Le nom du formateur est {@code DUPONT}.</li>
     *   <li>Le prénom du formateur est {@code Jean}.</li>
     *   <li>Le téléphone du formateur est {@code 0123456789}.</li>
     * </ul>
     * <br>
     * Cette méthode effectue les étapes suivantes :
     * <ol>
     *   <li>Utilise la méthode {@link #creerReservation(String)} pour créer une réservation de base.</li>
     *   <li>Récupère les détails supplémentaires spécifiques à une formation, tels que le nom, le prénom et le numéro de téléphone du formateur.</li>
     *   <li>Instancie et retourne une nouvelle instance de {@link Formation} avec tous les attributs nécessaires.</li>
     * </ol>
     * <br>
     * Si la ligne est invalide ou si la réservation de base ne peut pas être créée (par exemple, en raison d'erreurs
     * dans les identifiants ou dans le format des dates), la méthode retourne {@code null}.
     *
     * @param ligne la ligne contenant les attributs de la formation
     * @return une instance de {@link Formation} si la ligne est valide, {@code null} sinon
     * @author Tom GUTIERREZ
     */
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
     * Ajoute une ligne d'utilisateur spécifiée à la collection des utilisateurs.
     * <br><br>
     * La méthode effectue les étapes suivantes :
     * <ol>
     *   <li>Vérifie si la ligne est complète et respecte le format attendu pour un utilisateur
     *       (via {@link #estLigneComplete(String, String)}).</li>
     *   <li>Si la ligne est valide et si l'identifiant n'est pas déjà utilisé :
     *       <ul>
     *         <li>Une instance de {@link Utilisateur} est créée à partir de la ligne en utilisant
     *         la méthode {@link #creerUtilisateur(String)}.</li>
     *         <li>L'utilisateur est ajouté à la collection des utilisateurs.</li>
     *       </ul>
     *   </li>
     *   <li>Sinon, un message d'erreur est généré et la ligne est ajoutée à la liste des lignes incorrectes.</li>
     * </ol>
     * <br>
     * Validation et erreurs :
     * <ul>
     *   <li>La méthode vérifie si l'identifiant extrait de la ligne n'est pas déjà présent dans la collection des utilisateurs.</li>
     *   <li>Les lignes contenant des attributs vides sont considérées comme incorrectes.</li>
     *   <li>Les lignes totalement vides sont ignorées.</li>
     * </ul>
     * <br>
     * Gestion des erreurs :
     * <ul>
     *   <li>Si l'identifiant est déjà utilisé, un message d'erreur est généré : {@code "Identifiant déjà utilisé"}.</li>
     *   <li>Si la ligne est invalide ou incomplète, un message d'erreur est généré : {@code "Ligne incorrecte"}.</li>
     *   <li>Les lignes incorrectes et leurs messages d'erreur associés sont ajoutés à la liste accessible via
     *       {@link GestionDonnees#getLignesIncorrectesUtilisateurs()}.</li>
     * </ul>
     *
     * @param ligne la ligne contenant les attributs de l'utilisateur à ajouter
     * @throws NullPointerException si {@code ligne} est {@code null}
     * @author Tom GUTIERREZ
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
     * Crée une instance de {@link Utilisateur} à partir d'une ligne spécifiée considérée comme valide.
     * <br><br>
     * Cette méthode extrait les attributs nécessaires d'une ligne formatée selon le modèle suivant :
     * <ul>
     *   <li><b>Exemple de ligne :</b> {@code "E123456;DUPONT;Jean;0123456789"}</li>
     *   <li>Les attributs sont séparés par un délimiteur défini (par exemple, {@code ";"}) et correspondent respectivement à :</li>
     *   <ul>
     *     <li><b>Identifiant :</b> une chaîne unique pour l'utilisateur (par exemple, {@code "E123456"}).</li>
     *     <li><b>Nom :</b> le nom de famille de l'utilisateur (par exemple, {@code "DUPONT"}).</li>
     *     <li><b>Prénom :</b> le prénom de l'utilisateur (par exemple, {@code "Jean"}).</li>
     *     <li><b>Téléphone :</b> le numéro de téléphone de l'utilisateur (par exemple, {@code "0123456789"}).</li>
     *   </ul>
     * </ul>
     * Cas pris en charge :
     * <ul>
     *   <li>Les espaces superflus autour des attributs sont automatiquement supprimés.</li>
     *   <li>Les attributs vides sont autorisés, mais pas les lignes vides.</li>
     * </ul>
     * <br>
     * Cas d'erreur :
     * On considère que la ligne est vérifiée avant d'être passée à cette méthode
     * cependant les exceptions suivantes peuvent être levées :
     * <ul>
     *     <li>Si la ligne contient moins de 4 attributs, une exception {@link ArrayIndexOutOfBoundsException} est levée.</li>
     *     <li>Si la ligne est {@code null}, une exception {@link NullPointerException} est levée.</li>
     * </ul>
     *
     * @param ligne la ligne contenant les attributs de l'utilisateur, séparés par le délimiteur
     * @return une nouvelle instance de {@link Utilisateur} initialisée avec les attributs extraits
     * @throws ArrayIndexOutOfBoundsException si la ligne contient moins de 4 attributs
     * @throws NullPointerException si {@code ligne} est {@code null}
     * @author Tom GUTIERREZ
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
     * Vérifie si une ligne donnée est complète et valide en fonction du type de fichier spécifié.
     * <br><br>
     * Cette méthode utilise une expression régulière associée au type de fichier pour valider la structure de la ligne.
     * Si le type de fichier est {@code "reservations"}, elle effectue des validations supplémentaires :
     * <ul>
     *   <li>La date de début (extraite de la ligne) doit être antérieure à la date de fin.</li>
     * </ul>
     * <br>
     * Cas pris en charge :
     * <ul>
     *   <li>employes : Vérifie si la ligne suit le format attendu pour un fichier d'employés.</li>
     *   <li>salles : Vérifie si la ligne suit le format attendu pour un fichier de salles.</li>
     *   <li>activites : Vérifie si la ligne suit le format attendu pour un fichier d'activités.</li>
     *   <li>reservations : Vérifie si la ligne suit le format attendu pour un fichier de réservations et que
     *       la date de début est antérieure à la date de fin.</li>
     * </ul>
     * <br>
     * Cas d'erreur :
     * <ul>
     *   <li>Si l'un des arguments est {@code null}, une exception {@link IllegalArgumentException} est levée.</li>
     *   <li>Si le type de fichier est inconnu, une exception {@link IllegalArgumentException} est levée avec un message explicite.</li>
     *   <li>Si la ligne ne correspond pas à l'expression régulière définie pour le type de fichier, elle est considérée comme invalide.</li>
     * </ul>
     *
     * @param ligne la ligne à valider, correspondant à une entrée dans le fichier
     * @param typeFichier le type de fichier auquel appartient la ligne (parmi {@code "employes"}, {@code "salles"}, {@code "activites"}, {@code "reservations"})
     * @return {@code true} si la ligne est valide et complète, {@code false} sinon
     * @throws IllegalArgumentException si l'un des arguments est {@code null} ou si le type de fichier est inconnu
     * @author Tom GUTIERREZ
     */
    public boolean estLigneComplete(String ligne, String typeFichier) {
        boolean estValide;
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

        estValide = ligne.matches(regex);
        if (estValide) {
            if (typeFichier.equals("reservations")) {
                String[] attributs = ligne.split(DELIMITEUR);
                LocalDateTime dateDebut = construireDate(attributs[4], attributs[5]); // Indice dateDebut
                LocalDateTime dateFin = construireDate(attributs[4], attributs[6]); // Indice dateFin
                estValide = dateDebut.isBefore(dateFin);
            } else {
                estValide = true;
            }
        }

        return estValide;
    }

    /**
     * Construit un objet {@link LocalDateTime} à partir de chaînes de caractères représentant une date et une heure.
     * <br><br>
     * Cette méthode prend en entrée :
     * <ul>
     *   <li>Une date au format {@code "JJ/MM/AAAA"}, par exemple {@code "01/01/2024"}.</li>
     *   <li>Une heure au format {@code "XhXX"} ou {@code "XXhXX"}, par exemple {@code "8h05"} ou {@code "08h30"}.</li>
     * </ul>
     * <br>
     * Elle convertit ces chaînes en un objet {@link LocalDateTime}.
     * <br>
     * Cas pris en charge :
     * <ul>
     *   <li>Les heures peuvent être exprimées avec ou sans un chiffre initial (par exemple, {@code "8h05"} ou {@code "08h05"}).</li>
     *   <li>Les minutes doivent toujours comporter deux chiffres (par exemple, {@code "8h05"}, mais pas {@code "8h5"}).</li>
     * </ul>
     * <br>
     * Cas d'erreur :
     * <ul>
     *   <li>Si l'un des arguments est {@code null}, une exception {@code IllegalArgumentException} est levée.</li>
     *   <li>Si le format de la date ou de l'heure est incorrect, une exception {@code IllegalArgumentException} est levée.</li>
     *   <li>Si les valeurs de la date ou de l'heure ne sont pas valides (par exemple, 32/01/2024 ou 25h00), une exception est également levée.</li>
     * </ul>
     *
     * @param date la chaîne représentant la date au format {@code "JJ/MM/AAAA"}
     * @param heureMinutes la chaîne représentant l'heure au format {@code "XhXX"} ou {@code "XXhXX"}
     * @return un objet {@link LocalDateTime} correspondant à la combinaison de la date et de l'heure fournies
     * @throws IllegalArgumentException si l'un des arguments est {@code null}, si le format est incorrect ou si la date/heure est invalide
     * @author Tom GUTIERREZ
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
     * @author Tom GUTIERREZ
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

    /**
     * Récupère les instances de fichiers
     * @return les instances de fichiers
     */
    public Fichier[] getFichiers() {
        return fichiers;
    }

    /**
     * Récupère l'instance de fichier des utilisateurs
     */
    public Fichier getFichierUtilisateurs() {
        return fichierUtilisateurs;
    }

    /**
     * Récupère l'instance de fichier des salles
     */
    public Fichier getFichierSalles() {
        return fichierSalles;
    }

    /**
     * Récupère l'instance de fichier des activités
     */
    public Fichier getFichierActivites() {
        return fichierActivites;
    }

    /**
     * Récupère l'instance de fichier des réservations
     */
    public Fichier getFichierReservations() {
        return fichierReservations;
    }
}
