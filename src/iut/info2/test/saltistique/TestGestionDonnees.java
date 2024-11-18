package iut.info2.test.saltistique;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

class TestGestionDonnees {

    final String DELIMITEUR = ";";

    /**
     * Résultats attendus pour les ENTETES_VALIDES
     * Correspond aussi aux types de fichiers
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
     * Date valide pour les tests
     */
    final String[][] DATE_VALIDE = {
            {"21/10/2024", "10h00"}, // cas normal
            {"21/10/2024", "00h00"}, // heure limite basse
            {"21/10/2024", "23h59"}, // heure limite haute
            {"29/02/2024", "23h59"} // bissextile
    };

    /**
     * Date invalide pour les tests
     */
    final String[][] DATE_INVALIDE = {
            {"15/08/2023", "25h00"},   // Heure invalide
            {"15/08/2023", "1030"},    // Format de l'heure incorrect
            {"15/08/2023", ""},        // Heure nulle
            {"32/01/2023", "10h30"},   // Jour invalide
            {"32/01/2023", "25h00"},   // Date et heure invalides
            {"15-08-2023", "10h30"},   // Format de la date incorrect
            {"", "10h30"}              // Date vide
    };

    private static final String[] CHEMINS_VALIDES = {
            "src/iut/info2/test/ressources/salles.csv",
            "src/iut/info2/test/ressources/employes.csv",
            "src/iut/info2/test/ressources/activites.csv",
            "src/iut/info2/test/ressources/reservations.csv"
    };

    private static final String[] CHEMINS_INVALIDES = {
            "src/iut/info2/test/ressources/fichier_invalide.csv", // Données incorrectes
            "src/iut/info2/test/ressources/fichier_vide.csv", // Fichier vide
            "src/iut/info2/test/ressources/format_inconnu.csv" // Format non reconnu
    };

    /**
     * Répertoire contenant les fichiers de test pour un cas valide :
     * 4 fichiers différents (employes, salles, activites, reservations).
     */
    public static final String REPERTOIRE_CAS_VALIDE = "src/iut/info2/test/ressources/GestionDonnees/cas_valide";

    /**
     * Répertoire contenant les fichiers de test pour le cas où le nombre de fichiers fournis est inférieur à 4.
     */
    public static final String REPERTOIRE_TROP_PEU_DE_FICHIERS = "src/iut/info2/test/ressources/GestionDonnees/cas_nombre_fichiers_incorrect/moins";

    /**
     * Répertoire contenant les fichiers de test pour le cas où le nombre de fichiers fournis est supérieur à 4.
     */
    public static final String REPERTOIRE_TROP_DE_FICHIERS = "src/iut/info2/test/ressources/GestionDonnees/cas_nombre_fichiers_incorrect/plus";

    /**
     * Répertoire contenant les fichiers de test pour le cas d’un doublon de fichiers employés.
     */
    public static final String REPERTOIRE_DOUBLON_EMPLOYES = "src/iut/info2/test/ressources/GestionDonnees/cas_doublon/doublon_employes";

    /**
     * Répertoire contenant les fichiers de test pour le cas d’un doublon de fichiers salles.
     */
    public static final String REPERTOIRE_DOUBLON_SALLES = "src/iut/info2/test/ressources/GestionDonnees/cas_doublon/doublon_salles";

    /**
     * Répertoire contenant les fichiers de test pour le cas d’un doublon de fichiers activités.
     */
    public static final String REPERTOIRE_DOUBLON_ACTIVITES = "src/iut/info2/test/ressources/GestionDonnees/cas_doublon/doublon_activites";

    /**
     * Répertoire contenant les fichiers de test pour le cas d’un doublon de fichiers réservations.
     */
    public static final String REPERTOIRE_DOUBLON_RESERVATIONS = "src/iut/info2/test/ressources/GestionDonnees/cas_doublon/doublon_reservations";

    /**
     * Répertoire contenant les fichiers de test pour le cas où un fichier a un format non reconnu.
     */
    public static final String REPERTOIRE_CAS_NON_RECONNU = "src/iut/info2/test/ressources/GestionDonnees/cas_non_reconnu";

    /**
     * Répertoire contenant 3 fichiers de tests un 4ème inexistant est ajouté pour lever une exception.
     */
    public static final String REPERTOIRE_CAS_EXCEPTION_IO = "src/iut/info2/test/ressources/GestionDonnees/cas_exception_io";


    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        System.out.println("====== TEST DE LA CLASSE GestionDonnees ======");
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        System.out.println("======= TEST DE GestionDonnees TERMINE =======");
    }

    @Test
    void testImporterDonnees() {
        GestionDonnees gestion = new GestionDonnees();

        // test fonctionnel de la méthode importerDonnees
        // à refaire entièrement avec les tests unitaires
        try {
            gestion.importerDonnees(CHEMINS_VALIDES);

            // Vérification que chaque classe d'équivalence valide est bien remplie
            assertNotNull(gestion.getSalles(), "Les salles doivent être importées.");
            assertNotNull(gestion.getUtilisateurs(), "Les employés doivent être importés.");
            assertNotNull(gestion.getActivites(), "Les activités doivent être importées.");
            assertNotNull(gestion.getReservations(), "Les réservations doivent être importées.");

            // Nombre d'éléments
            assertEquals(9, gestion.getSalles().size(), "Nombre de salles incorrect.");
            assertEquals(8, gestion.getUtilisateurs().size(), "Nombre d'employés incorrect.");
            assertEquals(6, gestion.getActivites().size(), "Nombre d'activités incorrect.");
            assertEquals(18, gestion.getReservations().size(), "Nombre de réservations incorrect.");
        } catch (Exception e) {
            fail("Erreur inattendue : " + e.getMessage());
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

    @Test
    void testAjouterFichier() throws IOException {
        String[] cheminsFichierCourant;

        // cas 1 : fichier valide
        GestionDonnees gestion = new GestionDonnees();
        cheminsFichierCourant = listerFichiers(REPERTOIRE_CAS_VALIDE);

        try {
            gestion.ajouterFichier(cheminsFichierCourant);
            assertEquals(gestion.getFichierActivites().getFichierExploite(), new File(cheminsFichierCourant[0]), "Fichier activités incorrect");
            assertEquals(gestion.getFichierUtilisateurs().getFichierExploite(), new File(cheminsFichierCourant[1]), "Fichier employés incorrect");
            assertEquals(gestion.getFichierReservations().getFichierExploite(), new File(cheminsFichierCourant[2]), "Fichier réservations incorrect");
            assertEquals(gestion.getFichierSalles().getFichierExploite(), new File(cheminsFichierCourant[3]), "Fichier salles incorrect");
        } catch (Exception e) {
            fail("Erreur inattendue pour testAjouterFichier cas valide : " + e.getMessage());
        }

        // cas 2 : trop peu de fichiers
        gestion = new GestionDonnees();
        cheminsFichierCourant = listerFichiers(REPERTOIRE_TROP_PEU_DE_FICHIERS);

        try {
            gestion.ajouterFichier(cheminsFichierCourant);
            fail("Exception attendue pour testAjouterFichier cas nombre de fichiers incorrect");
        } catch (Exception e) {
            assertEquals("Le nombre de fichiers à fournir n'est pas respecté", e.getMessage(), "Message d'erreur incorrect");
        }

        // cas 3 : trop de fichiers
        gestion = new GestionDonnees();
        cheminsFichierCourant = listerFichiers(REPERTOIRE_TROP_DE_FICHIERS);

        try {
            gestion.ajouterFichier(cheminsFichierCourant);
            fail("Exception attendue pour testAjouterFichier cas nombre de fichiers incorrect");
        } catch (Exception e) {
            assertEquals("Le nombre de fichiers à fournir n'est pas respecté", e.getMessage(), "Message d'erreur incorrect");
        }

        // cas 4 : doublon de fichiers activités
        gestion = new GestionDonnees();
        cheminsFichierCourant = listerFichiers(REPERTOIRE_DOUBLON_ACTIVITES);

        try {
            gestion.ajouterFichier(cheminsFichierCourant);
            fail("Exception attendue pour testAjouterFichier cas doublon activités");
        } catch (Exception e) {
            String messageAttendu = "Vous avez fourni plusieurs fichiers d'activités : " +
                    "activites2.csv et activites1.csv";

            assertEquals(messageAttendu, e.getMessage(), "Message d'erreur incorrect");
        }

        // cas 5 : doublon de fichiers employés
        gestion = new GestionDonnees();
        cheminsFichierCourant = listerFichiers(REPERTOIRE_DOUBLON_EMPLOYES);

        try {
            gestion.ajouterFichier(cheminsFichierCourant);
            fail("Exception attendue pour testAjouterFichier cas doublon employés");
        } catch (Exception e) {
            String messageAttendu = "Vous avez fourni plusieurs fichiers d'employés : " +
                    "employes2.csv et employes1.csv";

            assertEquals(messageAttendu, e.getMessage(), "Message d'erreur incorrect");
        }

        // cas 6 : doublon de fichiers réservations
        gestion = new GestionDonnees();
        cheminsFichierCourant = listerFichiers(REPERTOIRE_DOUBLON_RESERVATIONS);

        try {
            gestion.ajouterFichier(cheminsFichierCourant);
            fail("Exception attendue pour testAjouterFichier cas doublon réservations");
        } catch (Exception e) {
            String messageAttendu = "Vous avez fourni plusieurs fichiers de réservations : " +
                    "reservations2.csv et reservations1.csv";

            assertEquals(messageAttendu, e.getMessage(), "Message d'erreur incorrect");
        }

        // cas 7 : doublon de fichiers salles
        gestion = new GestionDonnees();
        cheminsFichierCourant = listerFichiers(REPERTOIRE_DOUBLON_SALLES);

        try {
            gestion.ajouterFichier(cheminsFichierCourant);
            fail("Exception attendue pour testAjouterFichier cas doublon salles");
        } catch (Exception e) {
            String messageAttendu = "Vous avez fourni plusieurs fichiers de salles : " +
                    "salles2.csv et salles1.csv";

            assertEquals(messageAttendu, e.getMessage(), "Message d'erreur incorrect");
        }

        // cas 8 : format non reconnu
        gestion = new GestionDonnees();
        cheminsFichierCourant = listerFichiers(REPERTOIRE_CAS_NON_RECONNU);

        try {
            gestion.ajouterFichier(cheminsFichierCourant);
            fail("Exception attendue pour testAjouterFichier cas format non reconnu");
        } catch (Exception e) {
            String messageAttendu = "\"format_inconnu.csv\" non reconnu";
            assertEquals(messageAttendu, e.getMessage(), "Message d'erreur incorrect");
        }

        // cas 9 : fichier avec un fichier inexistant
        gestion = new GestionDonnees();
        cheminsFichierCourant = listerFichiers(REPERTOIRE_CAS_EXCEPTION_IO);
        // ajout d'un fichier inexistant pour lever une exception
        String[] cheminsFichierInexistant = Arrays.copyOf(cheminsFichierCourant, cheminsFichierCourant.length + 1);
        cheminsFichierInexistant[cheminsFichierInexistant.length - 1] = REPERTOIRE_CAS_EXCEPTION_IO + "/fichier_inexistant.csv";

        try {
            gestion.ajouterFichier(cheminsFichierInexistant);
            fail("Exception attendue pour testAjouterFichier cas format non reconnu");
        } catch (IOException e) {
            String messageAttendu = "Erreur lors de l'ouverture du fichier : " + REPERTOIRE_CAS_EXCEPTION_IO + "/fichier_inexistant.csv";
            assertEquals(messageAttendu, e.getMessage(), "Message d'erreur incorrect");
        }

        // cas 10 : fichier vide
        gestion = new GestionDonnees();
        try {
            gestion.ajouterFichier(null);
            fail("Exception attendue pour testAjouterFichier cas fichier vide");
        } catch (Exception e) {
            assertEquals("Les chemins des fichiers ne peuvent pas être nuls.", e.getMessage(), "Message d'erreur incorrect");
        }
    }


    @Test
    void testEstLigneComplete() {
        // cas 1 : le type de fichier est "activites" et la ligne est valide
        try {
            assertTrue(GestionDonnees.estLigneComplete(LIGNE_VALIDE_ACTIVITES, DELIMITEUR, TYPE_FICHIERS[0]), "Erreur cas 1 : ligne valide");
        } catch (Exception e) {
            fail("Erreur inattendue pour testEstLigneComplete cas 1 : " + e.getMessage());
        }

        // cas 2 : le type de fichier est "activites" et la ligne est invalide
        try {
            assertFalse(GestionDonnees.estLigneComplete(LIGNE_INVALIDE_ACTIVITES, DELIMITEUR, TYPE_FICHIERS[0]), "Erreur cas 2 : ligne invalide");
        } catch (Exception e) {
            fail("Erreur inattendue pour testEstLigneComplete cas 2 : " + e.getMessage());
        }

        // cas 3 : le type de fichier est "employes" et la ligne est valide
        try {
            System.out.println(TYPE_FICHIERS[1]);
            assertTrue(GestionDonnees.estLigneComplete(LIGNE_VALIDE_EMPLOYES, DELIMITEUR, TYPE_FICHIERS[1]), "Erreur cas 3 : ligne valide");
        } catch (Exception e) {
            fail("Erreur inattendue pour testEstLigneComplete cas 3 : " + e.getMessage());
        }

        // cas 4 : le type de fichier est "employes" et la ligne est invalide
        try {
            assertFalse(GestionDonnees.estLigneComplete(LIGNE_INVALIDE_EMPLOYES, DELIMITEUR, TYPE_FICHIERS[1]), "Erreur cas 4 : ligne invalide");
        } catch (Exception e) {
            fail("Erreur inattendue pour testEstLigneComplete cas 4 : " + e.getMessage());
        }

        // cas 5 : le type de fichier est "reservations" et la ligne est valide
        // et la date de début est inférieure à la date de fin
        try {
            assertTrue(GestionDonnees.estLigneComplete(LIGNE_DATE_VALIDE_RESERVATIONS, DELIMITEUR, TYPE_FICHIERS[2]), "Erreur cas 7 : ligne valide");
        } catch (Exception e) {
            fail("Erreur inattendue pour testEstLigneComplete cas 5 : " + e.getMessage());
        }

        // cas 6 : le type de fichier est "reservations" et la ligne est invalide
        // et la date de début est supérieure à la date de fin
        try {
            assertFalse(GestionDonnees.estLigneComplete(LIGNE_DATE_INVALIDE_RESERVATIONS, DELIMITEUR, TYPE_FICHIERS[2]), "Erreur cas 8 : ligne invalide");
        } catch (Exception e) {
            fail("Erreur inattendue pour testEstLigneComplete cas 6 : " + e.getMessage());
        }

        // cas 7 : le type de fichier est "reservations" et la ligne est invalide
        try {
            assertFalse(GestionDonnees.estLigneComplete(LIGNE_INVALIDE_RESERVATIONS, DELIMITEUR, TYPE_FICHIERS[2]), "Erreur cas 6 : ligne invalide");
        } catch (Exception e) {
            fail("Erreur inattendue pour testEstLigneComplete cas 7 : " + e.getMessage());
        }

        // cas 8 : le type de fichier est "salles" et la ligne est valide
        try {
            assertTrue(GestionDonnees.estLigneComplete(LIGNE_VALIDE_SALLES, DELIMITEUR, TYPE_FICHIERS[3]), "Erreur cas 5 : ligne valide");
        } catch (Exception e) {
            fail("Erreur inattendue pour testEstLigneComplete cas 8 : " + e.getMessage());
        }

        // cas 9 : le type de fichier est "salles" et la ligne est invalide
        try {
            assertFalse(GestionDonnees.estLigneComplete(LIGNE_INVALIDE_SALLES, DELIMITEUR, TYPE_FICHIERS[3]), "Erreur cas 6 : ligne invalide");
        } catch (Exception e) {
            fail("Erreur inattendue pour testEstLigneComplete cas 9 : " + e.getMessage());
        }

        // cas 10 : le type de fichier est inconnu
        try {
            GestionDonnees.estLigneComplete("ligne", DELIMITEUR, "inconnu");
            fail("Exception attendue pour testEstLigneComplete cas 10 : type de fichier inconnu");
        } catch (Exception e) {
            assertEquals("Type de fichier inconnu : inconnu", e.getMessage(), "Message d'erreur incorrect");
        }

        // cas 11 : un des paramètres est null
        try {
            GestionDonnees.estLigneComplete(null, DELIMITEUR, TYPE_FICHIERS[0]);
            fail("Exception attendue pour testEstLigneComplete cas 11 : ligne null");
        } catch (Exception e) {
            assertEquals("Les arguments ne peuvent pas être nuls.", e.getMessage(), "Message d'erreur incorrect");
        }
    }

    @Test
    void testConstruireDate() {
        // Cas valides
        for (String[] date : DATE_VALIDE) {
            LocalDateTime d = GestionDonnees.construireDate(date[0], date[1]);
            assertNotNull(d);
        }

        // Cas invalides
        for (String[] date : DATE_INVALIDE) {
            assertThrows(IllegalArgumentException.class, () -> GestionDonnees.construireDate(date[0], date[1]));
        }
    }

    @Test
    void testImporterDonneesAvecFichiersValides() {
        GestionDonnees gestion = new GestionDonnees();

        try {
            gestion.importerDonnees(CHEMINS_VALIDES);

            // Vérification que chaque classe d'équivalence valide est bien remplie
            assertNotNull(gestion.getSalles(), "Les salles doivent être importées.");
            assertNotNull(gestion.getUtilisateurs(), "Les employés doivent être importés.");
            assertNotNull(gestion.getActivites(), "Les activités doivent être importées.");
            assertNotNull(gestion.getReservations(), "Les réservations doivent être importées.");

            // Nombre d'éléments (utiliser des valeurs ajustées selon le contenu de chaque fichier)
            assertEquals(2, gestion.getSalles().size(), "Nombre de salles incorrect.");
            assertEquals(2, gestion.getUtilisateurs().size(), "Nombre d'employés incorrect.");
            assertEquals(2, gestion.getActivites().size(), "Nombre d'activités incorrect.");
            assertEquals(2, gestion.getReservations().size(), "Nombre de réservations incorrect.");
        } catch (Exception e) {
            fail("Exception pour fichiers valides : " + e.getMessage());
        }
    }

    @Test
    void testImporterDonneesAvecFichiersInvalides() {
        GestionDonnees gestion = new GestionDonnees();

        try {
            gestion.importerDonnees(CHEMINS_INVALIDES);

            // Vérifie que les fichiers invalides n'ont pas ajouté de données
            assertEquals(0, gestion.getSalles().size(), "Pas de salles pour fichier invalide.");
            assertEquals(0, gestion.getUtilisateurs().size(), "Pas d'employés pour fichier invalide.");
            assertEquals(0, gestion.getActivites().size(), "Pas d'activités pour fichier invalide.");
            assertEquals(0, gestion.getReservations().size(), "Pas de réservations pour fichier invalide.");

        } catch (Exception e) {
            fail("Erreur inattendue avec fichiers invalides : " + e.getMessage());
        }
    }

    @Test
    void testImporterDonneesFichierInexistant() {
        GestionDonnees gestion = new GestionDonnees();
        String[] cheminInexistant = { "src/test/resources/inexistant.csv" };

        assertThrows(Exception.class, () -> {
            gestion.importerDonnees(cheminInexistant);
        }, "Une exception doit être levée pour un fichier inexistant.");
    }

    @Test
    void testImporterDonneesMixtes() {
        GestionDonnees gestion = new GestionDonnees();
        String[] cheminsMixtes = {
                CHEMINS_VALIDES[0],
                CHEMINS_INVALIDES[0],
                CHEMINS_VALIDES[1]
        };

        try {
            gestion.importerDonnees(cheminsMixtes);

            // Les fichiers valides doivent être importés
            assertNotNull(gestion.getSalles(), "Les salles doivent être importées.");
            assertNotNull(gestion.getUtilisateurs(), "Les employés doivent être importés.");

            // Vérification du nombre d'éléments importés
            assertEquals(2, gestion.getSalles().size(), "Nombre de salles incorrect.");
            assertEquals(2, gestion.getUtilisateurs().size(), "Nombre d'employés incorrect.");

            // Vérification des catégories non affectées
            assertEquals(0, gestion.getActivites().size(), "Aucune activité ne doit être importée.");
            assertEquals(0, gestion.getReservations().size(), "Aucune réservation ne doit être importée.");

        } catch (Exception e) {
            fail("Erreur inattendue pour fichiers mixtes : " + e.getMessage());
        }
    }
}