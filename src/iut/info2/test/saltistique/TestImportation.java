package iut.info2.test.saltistique;

import static org.junit.jupiter.api.Assertions.*;

import iut.info2.saltistique.modele.*;
import iut.info2.saltistique.modele.donnees.Importation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

import iut.info2.saltistique.modele.donnees.GestionDonnees;

class TestImportation {

    /**
     * Tableau contenant les types de fichiers attendus.
     */
    final String[] TYPE_FICHIERS = {
            "activites",
            "employes",
            "reservations",
            "salles"
    };

    /** Ligne activité passant la regex */
    final String LIGNE_VALIDE_ACTIVITES = "A0000001;entretien";

    /** Ligne activité ne passant pas la regex */
    final String LIGNE_INVALIDE_ACTIVITES = "A0000002;";

    /** Ligne employé passant la regex */
    final String LIGNE_VALIDE_EMPLOYES = "E123456;Dupont;Jean;1234";

    /** Ligne employé ne passant pas la regex */
    final String LIGNE_INVALIDE_EMPLOYES = "E12345;Dupont;Jean;1234";

    /** Ligne salle passant la regex */
    final String LIGNE_VALIDE_SALLES = "12345678;Salle A;30;oui;non;20;Loc1;1;oui";

    /** Ligne salle ne passant pas la regex */
    final String LIGNE_INVALIDE_SALLES = "1234567;Salle A;30;o;n;20;PC portable;1;o";

    /** Ligne reservation passant le regex et avec une date debut inférieure à la date fin */
    final String LIGNE_DATE_VALIDE_RESERVATIONS = "R123456;20240101;E123456;Dupont Jean;21/10/2024;10h00;12h00;;;;;";

    /** Ligne reservations passant le regex et avec une date debut supérieure à la date fin */
    final String LIGNE_DATE_INVALIDE_RESERVATIONS = "R123456;20240101;E123456;Dupont Jean;21/10/2024;14h00;12h00;;;;;";

    /** Ligne reservations ne passant pas le regex */
    final String LIGNE_INVALIDE_RESERVATIONS = "R12345;20240101;E123456;Dupont Jean;21/10/2024;10h00;12h00;;";


    /**
     * Tableau contenant des dates valides.
     */
    final String[][] DATES_VALIDE = {
            {"01/01/2023", "0h00"}, // Date normale et minuit
            {"01/01/2023", "24h00"}, // Date normale et minuit
            {"01/01/2023", "23h59"}, // Date normale et heure maximale
            {"29/02/2024", "0h00"} // Année bissextile
    };

    /**
     * Tableau contenant les dates attendus
     */
    final String[] DATES_ATTENDUES = {
            "2023-01-01T00:00",
            "2023-01-01T00:00",
            "2023-01-01T23:59",
            "2024-02-29T00:00"
    };

    /**
     * Tableau contenant des dates invalides.
     */
    final String[][] DATES_INVALIDE = {
            {"32/01/2023", "0h00"}, // Jour invalide
            {"01-01-2023", "0h00"}, // Format invalide pour la date
            {"01/01/2023", "12:30"}, // Format invalide pour l'heure
            {null, "0h00"}, // Date nulle
            {"01/01/2023", null}, // Heure nulle
            {"32/01/2023", "25h00"} // Jour et heure invalides
    };
    

    /**
     * Répertoire contenant les fichiers de test pour un cas valide :
     * 4 fichiers différents (employes, salles, activites, reservations).
     */
    public static final String REPERTOIRE_CAS_VALIDE_AJOUTERFICHIERS= "src/iut/info2/test/ressources/Importation/AjouterFichiers/cas_valide";

    /**
     * Répertoire contenant les fichiers de test pour le cas où le nombre de fichiers fournis est inférieur à 4.
     */
    public static final String REPERTOIRE_TROP_PEU_DE_FICHIERS_AJOUTERFICHIERS= "src/iut/info2/test/ressources/Importation/AjouterFichiers/cas_nombre_fichiers_incorrect/moins";

    /**
     * Répertoire contenant les fichiers de test pour le cas où le nombre de fichiers fournis est supérieur à 4.
     */
    public static final String REPERTOIRE_TROP_DE_FICHIERS_AJOUTERFICHIERS= "src/iut/info2/test/ressources/Importation/AjouterFichiers/cas_nombre_fichiers_incorrect/plus";

    /**
     * Répertoire contenant les fichiers de test pour le cas d’un doublon de fichiers employés.
     */
    public static final String REPERTOIRE_DOUBLON_EMPLOYES_AJOUTERFICHIERS= "src/iut/info2/test/ressources/Importation/AjouterFichiers/cas_doublon/doublon_employes";

    /**
     * Répertoire contenant les fichiers de test pour le cas d’un doublon de fichiers salles.
     */
    public static final String REPERTOIRE_DOUBLON_SALLES_AJOUTERFICHIERS= "src/iut/info2/test/ressources/Importation/AjouterFichiers/cas_doublon/doublon_salles";

    /**
     * Répertoire contenant les fichiers de test pour le cas d’un doublon de fichiers activités.
     */
    public static final String REPERTOIRE_DOUBLON_ACTIVITES_AJOUTERFICHIERS= "src/iut/info2/test/ressources/Importation/AjouterFichiers/cas_doublon/doublon_activites";

    /**
     * Répertoire contenant les fichiers de test pour le cas d’un doublon de fichiers réservations.
     */
    public static final String REPERTOIRE_DOUBLON_RESERVATIONS_AJOUTERFICHIERS= "src/iut/info2/test/ressources/Importation/AjouterFichiers/cas_doublon/doublon_reservations";

    /**
     * Répertoire contenant les fichiers de test pour le cas où un fichier a un format non reconnu.
     */
    public static final String REPERTOIRE_CAS_NON_RECONNU_AJOUTERFICHIERS= "src/iut/info2/test/ressources/Importation/AjouterFichiers/cas_non_reconnu";

    /**
     * Répertoire contenant 3 fichiers de tests un 4ème inexistant est ajouté pour lever une exception.
     */
    public static final String REPERTOIRE_CAS_EXCEPTION_IO_AJOUTERFICHIERS= "src/iut/info2/test/ressources/Importation/AjouterFichiers/cas_exception_io";

    /**
     * Répertoire contenant les fichiers de test pour tester ajouterActivite.
     */
    public static final String REPERTOIRE_AJOUTERACTIVITE= "src/iut/info2/test/ressources/Importation/AjouterActivite";

    /**
     * Resultat attendus pour lignesIncorrectesActivites
     */
    public static final String[][] LIGNES_INCORRECTES_ACTIVITES_AJOUTERACTIVITE = {
            {"A0000001;duplication", "Identifiant déjà utilisé"},
            {"A0000003a;erreur", "Ligne incorrecte"}
    };

    /**
     * Activités à ajouter pour le test ajouterActivite
     */
    public static final Activite[] ACTIVITES_AJOUTERACTIVITE = {
            new Activite("A0000001", "valide"),
    };

    /**
     * Répertoire contenant les fichiers de test pour tester ajouterSalle.
     */
    public static final String REPERTOIRE_AJOUTERSALLE= "src/iut/info2/test/ressources/Importation/AjouterSalle";

    /**
     * Resultat attendus pour lignesIncorrectesActivites
     */
    public static final String[][] LIGNES_INCORRECTES_ACTIVITES_AJOUTERSALLE = {
            {"00000002;doublon;18;oui;oui;;;;", "Identifiant déjà utilisé"},
            {"00000003;incorrect;;;;;;;", "Ligne incorrecte"}
    };

    /**
     * Activités à ajouter pour le test ajouterSalle
     */
    public static final Salle[] ACTIVITES_AJOUTERSALLE = {
            new Salle("00000001", "valide", 18, true, true, false, null),
            new Salle("00000002", "valide", 18, true, true, false, new GroupeOrdinateurs(1, "type", null)),
    };

    /**
     * Répertoire contenant les fichiers de test pour tester ajouterUtilisateur.
     */
    public static final String REPERTOIRE_AJOUTERUTILISATEUR= "src/iut/info2/test/ressources/Importation/AjouterUtilisateur";

    /**
     * Resultat attendus pour lignesIncorrectesActivites
     */
    public static final String[][] LIGNES_INCORRECTES_ACTIVITES_AJOUTERUTILISATEUR = {
            {"E000001;doublon;Pierre;2614", "Identifiant déjà utilisé"},
            {"E000002;incorrect;Pierre;2614a", "Ligne incorrecte"}
    };

    /**
     * Utilisateurs à ajouter pour le test ajouterUtilisateur
     */
    public static final Utilisateur[] UTILISATEURS_AJOUTERUTILISATEUR = {
            new Utilisateur("E000001", "valide", "Pierre", "2614"),
    };

    /**
     * Répertoire contenant les fichiers de test pour tester ajouterReservation.
     */
    public static final String REPERTOIRE_AJOUTERRESERVATION= "src/iut/info2/test/ressources/Importation/AjouterReservation";

    /**
     * Resultat attendus pour lignesIncorrectesActivites
     */
    public static final String[][] LIGNES_INCORRECTES_ACTIVITES_AJOUTERRESERVATION = {
            {"R000004;00000001;E000001;autre;07/10/2024;17h00;19h00;doublon;;;;", "Identifiant déjà utilisé"},
            {"R000005;00000002;E000001;autre;07/10/2024;17h00;19h00;salle incorrecte;;;;", "Erreur lors de la création de la réservation"},
            {"R000006;00000001;E000002;autre;07/10/2024;17h00;19h00;employe incorrect;;;;", "Erreur lors de la création de la réservation"},
            {"R000007;00000001;E000001;incorrect;07/10/2024;17h00;19h00;activité incorrecte;;;;", "Erreur lors de la création de la réservation"},
            {"R000008;00000001;E000001;incorrect;07/10/2024;20h00;19h00;date incorrecte;;;;", "Ligne incorrecte"},
            {"R00000neuf;00000001;E000001;incorrect;07/10/2024;20h00;19h00;ligne incorrecte;;;;", "Ligne incorrecte"}
    };

    /**
     * Activités à ajouter pour le test ajouterReservation
     */
    public static final Activite[] ACTIVITES_AJOUTERRESERVATION = {
            new Activite("00000001", "prêt"),
            new Activite("00000002", "formation"),
            new Activite("00000003", "location"),
            new Activite("00000004", "autre")
    };

    /**
     * Salles à ajouter pour le test ajouterReservation
     */
    public static final Salle SALLE_AJOUTERRESERVATION = new Salle("00000001", "valide", 18, true, true, false, null);

    /**
     * Utilisateurs à ajouter pour le test ajouterReservation
     */
    public static final Utilisateur UTILISATEUR_AJOUTERRESERVATION = new Utilisateur("E000001", "valide", "Pierre", "2614");

    /**
     * Réservation à ajouter pour le test ajouterReservation
     */
    public static final Reservation[] RESERVATIONS_AJOUTERRESERVATION = {
            new Location("R000001", LocalDateTime.parse("2024-10-07T17:00"), LocalDateTime.parse("2024-10-07T19:00"), "reunion", SALLE_AJOUTERRESERVATION, ACTIVITES_AJOUTERRESERVATION[0], UTILISATEUR_AJOUTERRESERVATION, "club gym", "Legendre", "Noémie", "0600000000"),
            new Formation("R000002", LocalDateTime.parse("2024-10-07T17:00"), LocalDateTime.parse("2024-10-07T19:00"), "Bureautique", SALLE_AJOUTERRESERVATION, ACTIVITES_AJOUTERRESERVATION[1], UTILISATEUR_AJOUTERRESERVATION, "Leroux", "Jacques", "0600000001"),
            new Location("R000003", LocalDateTime.parse("2024-10-07T17:00"), LocalDateTime.parse("2024-10-07T19:00"), "", SALLE_AJOUTERRESERVATION, ACTIVITES_AJOUTERRESERVATION[2], UTILISATEUR_AJOUTERRESERVATION, "Département", "Tournefeuille", "Michel", "0655555555"),
            new Reservation("R000004", LocalDateTime.parse("2024-10-07T17:00"), LocalDateTime.parse("2024-10-07T19:00"), "tests candidats", SALLE_AJOUTERRESERVATION, ACTIVITES_AJOUTERRESERVATION[3], UTILISATEUR_AJOUTERRESERVATION)
    };



    @BeforeAll
    static void setUpBeforeClass() {
        System.out.println("====== TEST DE LA CLASSE GestionDonnees ======");
    }

    @AfterAll
    static void tearDownAfterClass() {
        System.out.println("======= TEST DE GestionDonnees TERMINE =======");
    }

    @Test
    void testImporterDonnees() throws IOException {
        testAjouterFichiers();
        testAjouterActivite();
        testAjouterSalle();
        testAjouterUtilisateur();
        testAjouterReservation();
    }

    void testAjouterReservation() {
        GestionDonnees donnees = new GestionDonnees();
        Importation importation = new Importation(donnees);
        try {
            importation.importerDonnees(listerFichiers(REPERTOIRE_AJOUTERRESERVATION));
            for (int i = 0; i < RESERVATIONS_AJOUTERRESERVATION.length; i++) {
                assertEquals(RESERVATIONS_AJOUTERRESERVATION[i].toString(), donnees.getReservations().get(i+1).toString(), "Erreur pour testAjouterReservation");
            }

            for (int i = 0; i < LIGNES_INCORRECTES_ACTIVITES_AJOUTERRESERVATION.length; i++) {
                assertArrayEquals(LIGNES_INCORRECTES_ACTIVITES_AJOUTERRESERVATION[i], donnees.getLignesIncorrectesReservations().get(i), "Erreur pour testAjouterReservation");
            }

            // Vérification de la taille de la liste des lignes incorrectes pour le cas vide (";").
            assertEquals(4, donnees.getReservations().size(), "Erreur pour testAjouterReservation");
            assertEquals(6, donnees.getLignesIncorrectesReservations().size(), "Erreur pour testAjouterReservation");
        } catch (Exception e) {
            fail("Erreur inattendue pour testAjouterReservation : " + e.getMessage());
        }
    }

    void testAjouterUtilisateur() {
        GestionDonnees donnees = new GestionDonnees();
        Importation importation = new Importation(donnees);
        try {
            importation.importerDonnees(listerFichiers(REPERTOIRE_AJOUTERUTILISATEUR));
            for (int i = 0; i < UTILISATEURS_AJOUTERUTILISATEUR.length; i++) {
                assertEquals(UTILISATEURS_AJOUTERUTILISATEUR[i].toString(), donnees.getUtilisateurs().get(i+1).toString(), "Erreur pour testAjouterUtilisateur");
            }

            for (int i = 0; i < LIGNES_INCORRECTES_ACTIVITES_AJOUTERUTILISATEUR.length; i++) {
                assertArrayEquals(LIGNES_INCORRECTES_ACTIVITES_AJOUTERUTILISATEUR[i], donnees.getLignesIncorrectesUtilisateurs().get(i), "Erreur pour testAjouterUtilisateur");
            }

            // Vérification de la taille de la liste des lignes incorrectes pour le cas vide (";").
            assertEquals(1, donnees.getUtilisateurs().size(), "Erreur pour testAjouterUtilisateur");
            assertEquals(2, donnees.getLignesIncorrectesUtilisateurs().size(), "Erreur pour testAjouterUtilisateur");
        } catch (Exception e) {
            fail("Erreur inattendue pour testAjouterUtilisateur : " + e.getMessage());
        }
    }

    void testAjouterActivite() {
        GestionDonnees donnees = new GestionDonnees();
        Importation importation = new Importation(donnees);
        try {
            // Importation de tout le jeu de données
            importation.importerDonnees(listerFichiers(REPERTOIRE_AJOUTERACTIVITE));
            // Vérification des activités ajoutées
            for (int i = 0; i < ACTIVITES_AJOUTERACTIVITE.length; i++) {
                assertEquals(ACTIVITES_AJOUTERACTIVITE[i].toString(), donnees.getActivites().get(i+1).toString(), "Erreur pour testAjouterActivite");
            }

            // Vérification des lignes incorrectes
            for (int i = 0; i < LIGNES_INCORRECTES_ACTIVITES_AJOUTERACTIVITE.length; i++) {
                assertArrayEquals(LIGNES_INCORRECTES_ACTIVITES_AJOUTERACTIVITE[i], donnees.getLignesIncorrectesActivites().get(i), "Erreur pour testAjouterActivite");
            }

            // Vérification de la taille de la liste des lignes incorrectes pour le cas vide (";").
            assertEquals(1, donnees.getActivites().size(), "Erreur pour testAjouterActivite");
            assertEquals(2, donnees.getLignesIncorrectesActivites().size(), "Erreur pour testAjouterActivite");
        } catch (Exception e) {
            fail("Erreur inattendue pour testAjouterActivite : " + e.getMessage());
        }
    }

    void testAjouterSalle() {
        GestionDonnees donnees = new GestionDonnees();
        Importation importation = new Importation(donnees);
        try {
            importation.importerDonnees(listerFichiers(REPERTOIRE_AJOUTERSALLE));
            for (int i = 0; i < ACTIVITES_AJOUTERSALLE.length; i++) {
                assertEquals(ACTIVITES_AJOUTERSALLE[i].toString(), donnees.getSalles().get(i+1).toString(), "Erreur pour testAjouterSalle");
            }

            for (int i = 0; i < LIGNES_INCORRECTES_ACTIVITES_AJOUTERSALLE.length; i++) {
                assertArrayEquals(LIGNES_INCORRECTES_ACTIVITES_AJOUTERSALLE[i], donnees.getLignesIncorrectesSalles().get(i), "Erreur pour testAjouterSalle");
            }

            // Vérification de la taille de la liste des lignes incorrectes pour le cas vide (";").
            assertEquals(2, donnees.getSalles().size(), "Erreur pour testAjouterSalle");
            assertEquals(2, donnees.getLignesIncorrectesSalles().size(), "Erreur pour testAjouterSalle");
        } catch (Exception e) {
            fail("Erreur inattendue pour testAjouterSalle : " + e.getMessage());
        }
    }



    @Test
    void testEstLigneComplete() {
        // cas 1 : le type de fichier est "activites" et la ligne est valide
        Importation importation = new Importation(new GestionDonnees());
        try {
            assertTrue(importation.estLigneComplete(LIGNE_VALIDE_ACTIVITES, TYPE_FICHIERS[0]), "Erreur cas 1 : ligne valide");
        } catch (Exception e) {
            fail("Erreur inattendue pour testEstLigneComplete cas 1 : " + e.getMessage());
        }

        // cas 2 : le type de fichier est "activites" et la ligne est invalide
        try {
            assertFalse(importation.estLigneComplete(LIGNE_INVALIDE_ACTIVITES, TYPE_FICHIERS[0]), "Erreur cas 2 : ligne invalide");
        } catch (Exception e) {
            fail("Erreur inattendue pour testEstLigneComplete cas 2 : " + e.getMessage());
        }

        // cas 3 : le type de fichier est "employes" et la ligne est valide
        try {
            assertTrue(importation.estLigneComplete(LIGNE_VALIDE_EMPLOYES, TYPE_FICHIERS[1]), "Erreur cas 3 : ligne valide");
        } catch (Exception e) {
            fail("Erreur inattendue pour testEstLigneComplete cas 3 : " + e.getMessage());
        }

        // cas 4 : le type de fichier est "employes" et la ligne est invalide
        try {
            assertFalse(importation.estLigneComplete(LIGNE_INVALIDE_EMPLOYES, TYPE_FICHIERS[1]), "Erreur cas 4 : ligne invalide");
        } catch (Exception e) {
            fail("Erreur inattendue pour testEstLigneComplete cas 4 : " + e.getMessage());
        }

        // cas 5 : le type de fichier est "reservations" et la ligne est valide
        // et la date de début est inférieure à la date de fin
        try {
            assertTrue(importation.estLigneComplete(LIGNE_DATE_VALIDE_RESERVATIONS, TYPE_FICHIERS[2]), "Erreur cas 7 : ligne valide");
        } catch (Exception e) {
            fail("Erreur inattendue pour testEstLigneComplete cas 5 : " + e.getMessage());
        }

        // cas 6 : le type de fichier est "reservations" et la ligne est invalide
        // et la date de début est supérieure à la date de fin
        try {
            assertFalse(importation.estLigneComplete(LIGNE_DATE_INVALIDE_RESERVATIONS, TYPE_FICHIERS[2]), "Erreur cas 8 : ligne invalide");
        } catch (Exception e) {
            fail("Erreur inattendue pour testEstLigneComplete cas 6 : " + e.getMessage());
        }

        // cas 7 : le type de fichier est "reservations" et la ligne est invalide
        try {
            assertFalse(importation.estLigneComplete(LIGNE_INVALIDE_RESERVATIONS, TYPE_FICHIERS[2]), "Erreur cas 6 : ligne invalide");
        } catch (Exception e) {
            fail("Erreur inattendue pour testEstLigneComplete cas 7 : " + e.getMessage());
        }

        // cas 8 : le type de fichier est "salles" et la ligne est valide
        try {
            assertTrue(importation.estLigneComplete(LIGNE_VALIDE_SALLES, TYPE_FICHIERS[3]), "Erreur cas 5 : ligne valide");
        } catch (Exception e) {
            fail("Erreur inattendue pour testEstLigneComplete cas 8 : " + e.getMessage());
        }

        // cas 9 : le type de fichier est "salles" et la ligne est invalide
        try {
            assertFalse(importation.estLigneComplete(LIGNE_INVALIDE_SALLES, TYPE_FICHIERS[3]), "Erreur cas 6 : ligne invalide");
        } catch (Exception e) {
            fail("Erreur inattendue pour testEstLigneComplete cas 9 : " + e.getMessage());
        }

        // cas 10 : le type de fichier est inconnu
        try {
            importation.estLigneComplete("ligne", "inconnu");
            fail("Exception attendue pour testEstLigneComplete cas 10 : type de fichier inconnu");
        } catch (Exception e) {
            assertEquals("Type de fichier inconnu : inconnu", e.getMessage(), "Message d'erreur incorrect");
        }

        // cas 11 : un des paramètres est null
        try {
            importation.estLigneComplete(null, TYPE_FICHIERS[0]);
            fail("Exception attendue pour testEstLigneComplete cas 11 : ligne null");
        } catch (Exception e) {
            assertEquals("Les arguments ne peuvent pas être nuls.", e.getMessage(), "Message d'erreur incorrect");
        }
    }

    @Test
    void testConstruireDate() {
        // Cas valides
        for (int i = 0; i < DATES_VALIDE.length; i++) {
            try {
                assertEquals(DATES_ATTENDUES[i], Importation.construireDate(DATES_VALIDE[i][0], DATES_VALIDE[i][1]).toString(), "Erreur pour testConstruireDate cas " + i);
            } catch (Exception e) {
                fail("Erreur inattendue pour testConstruireDate cas " + i + " : " + e.getMessage());
            }
        }

        // Cas invalides
        for (String[] date : DATES_INVALIDE) {
            assertThrows(IllegalArgumentException.class, () -> Importation.construireDate(date[0], date[1]));
        }
    }

    /**
     * Permet de lister les fichiers CSV dans un répertoire.
     * @param repertoire le répertoire à explorer
     * @return un tableau de chaînes de caractères contenant les chemins des fichiers CSV
     */
    private String[] listerFichiers(String repertoire) {
        File dir = new File(repertoire);
        String[] fichiers;
        if (!dir.exists() || !dir.isDirectory()) {
            return new String[]{};
        }
        fichiers = dir.list();
        for (int i = 0; i < fichiers.length; i++) {
            fichiers[i] = repertoire + "/" + fichiers[i];
        }
        return fichiers;
    }

    /** Test de la méthode privée grâce à la méthode publique importerDonnees. */
    void testAjouterFichiers() throws IOException {
        String[] cheminsFichierCourant;
            Importation importation;
                // cas 1 : fichier valide
        importation = new Importation(new GestionDonnees());
        cheminsFichierCourant = listerFichiers(REPERTOIRE_CAS_VALIDE_AJOUTERFICHIERS);

        try {
            importation.importerDonnees(cheminsFichierCourant);
            assertEquals(importation.getFichierActivites().getFichierExploite(), new File(cheminsFichierCourant[0]), "Fichier activités incorrect");
            assertEquals(importation.getFichierUtilisateurs().getFichierExploite(), new File(cheminsFichierCourant[1]), "Fichier employés incorrect");
            assertEquals(importation.getFichierReservations().getFichierExploite(), new File(cheminsFichierCourant[2]), "Fichier réservations incorrect");
            assertEquals(importation.getFichierSalles().getFichierExploite(), new File(cheminsFichierCourant[3]), "Fichier salles incorrect");
        } catch (Exception e) {
            fail("Erreur inattendue pour testAjouterFichier cas valide : " + e.getMessage());
        }

        // cas 2 : trop peu de fichiers
        importation = new Importation(new GestionDonnees());
        cheminsFichierCourant = listerFichiers(REPERTOIRE_TROP_PEU_DE_FICHIERS_AJOUTERFICHIERS);

        try {
            importation.importerDonnees(cheminsFichierCourant);
            fail("Exception attendue pour testAjouterFichier cas nombre de fichiers incorrect");
        } catch (Exception e) {
            assertEquals("Le nombre de fichiers à fournir n'est pas respecté", e.getMessage(), "Message d'erreur incorrect");
        }

        // cas 3 : trop de fichiers
        importation = new Importation(new GestionDonnees());
        cheminsFichierCourant = listerFichiers(REPERTOIRE_TROP_DE_FICHIERS_AJOUTERFICHIERS);

        try {
            importation.importerDonnees(cheminsFichierCourant);
            fail("Exception attendue pour testAjouterFichier cas nombre de fichiers incorrect");
        } catch (Exception e) {
            assertEquals("Le nombre de fichiers à fournir n'est pas respecté", e.getMessage(), "Message d'erreur incorrect");
        }

        // cas 4 : doublon de fichiers activités
        importation = new Importation(new GestionDonnees());
        cheminsFichierCourant = listerFichiers(REPERTOIRE_DOUBLON_ACTIVITES_AJOUTERFICHIERS);

        try {
            importation.importerDonnees(cheminsFichierCourant);
            fail("Exception attendue pour testAjouterFichier cas doublon activités");
        } catch (Exception e) {
            String messageAttendu = "Vous avez fourni plusieurs fichiers d'activités : " +
                    "activites2.csv et activites1.csv";

            assertEquals(messageAttendu, e.getMessage(), "Message d'erreur incorrect");
        }

        // cas 5 : doublon de fichiers employés
        importation = new Importation(new GestionDonnees());
        cheminsFichierCourant = listerFichiers(REPERTOIRE_DOUBLON_EMPLOYES_AJOUTERFICHIERS);

        try {
            importation.importerDonnees(cheminsFichierCourant);
            fail("Exception attendue pour testAjouterFichier cas doublon employés");
        } catch (Exception e) {
            String messageAttendu = "Vous avez fourni plusieurs fichiers d'employés : " +
                    "employes2.csv et employes1.csv";

            assertEquals(messageAttendu, e.getMessage(), "Message d'erreur incorrect");
        }

        // cas 6 : doublon de fichiers réservations
        importation = new Importation(new GestionDonnees());
        cheminsFichierCourant = listerFichiers(REPERTOIRE_DOUBLON_RESERVATIONS_AJOUTERFICHIERS);

        try {
            importation.importerDonnees(cheminsFichierCourant);
            fail("Exception attendue pour testAjouterFichier cas doublon réservations");
        } catch (Exception e) {
            String messageAttendu = "Vous avez fourni plusieurs fichiers de réservations : " +
                    "reservations2.csv et reservations1.csv";

            assertEquals(messageAttendu, e.getMessage(), "Message d'erreur incorrect");
        }

        // cas 7 : doublon de fichiers salles
        importation = new Importation(new GestionDonnees());
        cheminsFichierCourant = listerFichiers(REPERTOIRE_DOUBLON_SALLES_AJOUTERFICHIERS);

        try {
            importation.importerDonnees(cheminsFichierCourant);
            fail("Exception attendue pour testAjouterFichier cas doublon salles");
        } catch (Exception e) {
            String messageAttendu = "Vous avez fourni plusieurs fichiers de salles : " +
                    "salles2.csv et salles1.csv";

            assertEquals(messageAttendu, e.getMessage(), "Message d'erreur incorrect");
        }

        // cas 8 : format non reconnu
        importation = new Importation(new GestionDonnees());
        cheminsFichierCourant = listerFichiers(REPERTOIRE_CAS_NON_RECONNU_AJOUTERFICHIERS);

        try {
            importation.importerDonnees(cheminsFichierCourant);
            fail("Exception attendue pour testAjouterFichier cas format non reconnu");
        } catch (Exception e) {
            String messageAttendu = "\"format_inconnu.csv\" non reconnu";
            assertEquals(messageAttendu, e.getMessage(), "Message d'erreur incorrect");
        }

        // cas 9 : fichier avec un fichier inexistant
        importation = new Importation(new GestionDonnees());
        cheminsFichierCourant = listerFichiers(REPERTOIRE_CAS_EXCEPTION_IO_AJOUTERFICHIERS);
        // ajout d'un fichier inexistant pour lever une exception
        String[] cheminsFichierInexistant = Arrays.copyOf(cheminsFichierCourant, cheminsFichierCourant.length + 1);
        cheminsFichierInexistant[cheminsFichierInexistant.length - 1] = REPERTOIRE_CAS_EXCEPTION_IO_AJOUTERFICHIERS + "/fichier_inexistant.csv";
        try {
            importation.importerDonnees(cheminsFichierInexistant);
            fail("Exception attendue pour testAjouterFichier cas fichier non existant");
        } catch (IOException e) {
            String messageAttendu = "Erreur lors de l'ouverture du fichier : " + REPERTOIRE_CAS_EXCEPTION_IO_AJOUTERFICHIERS + "/fichier_inexistant.csv";
            assertEquals(messageAttendu, e.getMessage(), "Message d'erreur incorrect");
        }

        // cas 10 : fichier vide
        importation = new Importation(new GestionDonnees());
        try {
            importation.importerDonnees(null);
            fail("Exception attendue pour testAjouterFichier cas fichier vide");
        } catch (Exception e) {
            assertEquals("Les chemins des fichiers ne peuvent pas être nuls.", e.getMessage(), "Message d'erreur incorrect");
        }
    }
}