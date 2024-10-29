package iut.info2.test.saltistique;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import iut.info2.saltistique.modele.GestionDonnees;

import java.util.Date;

class TestGestionDonnees {

    final String DELIMITEUR = ";";

    /**
     * Entêtes valides pour les fichiers CSV
     */
    final String[] ENTETES_VALIDES = {
            "Ident;Nom;Capacité;videoproj;ecranXXL;ordinateur;type;logiciels;imprimante", // salles
            "Ident;Nom;Prenom;Telephone", // employes
            "Ident;Activité", // activites
            "Ident;salle;employe;activite;date;heuredebut;heurefin;;;;;" // reservations
    };

    /**
     * Résultats attendus pour les ENTETES_VALIDES
     * Correspond aussi aux types de fichiers
     */
    final String[] TYPE_FICHIERS = {
            "salles",
            "employes",
            "activites",
            "reservations"
    };

    /**
     * Lignes valides pour les activités
     */
    final String[] LIGNES_VALIDES_ACTIVITES = {
            "A0000001;réunion", // avec accent
            "A0000001;entretien" // sans accent

    };

    /**
     * Lignes invalides pour les activités
     */
    final String[] LIGNES_INVALIDES_ACTIVITES = {
            "A0000002;", // activité manquante
            ";réunion", // identifiant manquant
            ";" // les deux champs manquants
    };

    /**
     * Entêtes non valides pour les fichiers CSV salles
     */
    final String[] ENTETES_NON_VALIDES_SALLES = {
            "A0000001", // pas assez de champs
            "Ident;salle;employe;activite;date;heuredebut;heurefin;bonjour;", // trop de champs
    };

    /**
     * Entêtes non valides pour les fichiers CSV employes
     */
    final String[] ENTETES_NON_VALIDES_EMPLOYES = {
            "Ident;Nom;Prenom", // pas assez de champs
            "Ident;Nom;Prenom;Telephone;Adresse" // trop de champs
    };

    /**
     * Entêtes non valides pour les fichiers CSV activites
     */
    final String[] ENTETES_NON_VALIDES_ACTIVITES = {
            "Ident;Activité;Type", // trop de champs
            "Ident" // pas assez de champs
    };

    /**
     * Entêtes non valides pour les fichiers CSV reservations
     */
    final String[] ENTETES_NON_VALIDES_RESERVATIONS = {
            "Ident;Salle;Employe;Activite;Date;HeureDebut;HeureFin;Commentaire", // pas assez de champs
            "Ident;Salle;Employe;Activite;Date;HeureDebut;HeureFin;Commentaire;Commentaire;Commentaire;Commentaire;Commentaire" // trop de champs
    };

    final String[] LIGNES_VALIDES_EMPLOYES = {
            "E123456;Dupont;Jean;1234",
            "E654321;Martin;Claire;5678"
    };

    final String[] LIGNES_INVALIDES_EMPLOYES = {
            "E12345;Dupont;Jean;1234", // ID trop court
            "E123456;Dupont;Jean;",    // Téléphone manquant
            "E123456;Dupont;Jean;abcd", // Téléphone invalide (lettres)
            "E123456;Dupont;Jean;123", // Téléphone invalide (trop court)
            "E123456;Dupont;Jean;12345", // Téléphone invalide (trop long)
            "E123456;;Jean;1234", // champ prenom vide
            "E123456;Dupont;Jean;12345;12345", // Trop de champs
            "E123456;Dup" // Trop peu de champs

    };

    final String[] LIGNES_VALIDES_SALLES = {
            "12345678;Salle A;30;oui;non;20;Loc1;1;oui",
            "87654321;Salle B;50;non;non;25;Loc2;2;oui",
    };

    final String[] LIGNES_INVALIDES_SALLES = {
            "1234567;Salle A;30;o;n;20;PC portable;1;o", // ID trop court
            "12345678;Salle B;fifty;n;n;25;PC fixe;2;", // Capacité invalide (lettres)
            "12345678;Salle C;30;o;n;20;PC portable;2;o;10;10;10", // Trop de champs
            "12345678;Salle C;30;o;n;20;PC portable;2;o;10;10", // Trop peu de champs
            "12345678;;30;oui;non;20;PC portable;1;oui", // nom de salle vide
    };

    final String[] LIGNES_VALIDES_RESERVATIONS = {
            "R123456;20240101;E123456;Dupont Jean;21/10/2024;10h00;12h00;;;;;",
            "R654321;20240102;E654321;Martin Claire;21/10/2024;14h00;15h00;Commentaire;;;;"
    };

    final String[] LIGNES_INVALIDES_RESERVATIONS = {
            "R12345;20240101;E123456;Dupont Jean;21/10/2024;10h00;12h00;;", // ID de réservation trop court
            "R123456;20240101;E123456;Dupont Jean;21/10/2024;12h00;10h00;;", // Heure fin avant début
            "R123456;20240101;E123456;Dupont Jean;21/10/2024;10h00;12h00;Commentaire;Commentaire;Commentaire;", // Trop de champs
            "R123456;20240101;E123456;Dupont Jean;21/10/2024;10h00;12h00", // Trop peu de champs
            "R123456;20240101;E123456;Dupont Jean;21/10/2024;10h00;12h00;Commentaire;Commentaire;Commentaire;Commentaire" // Trop de champs
    };

    private static final String[] CHEMINS_VALIDES = {
            "iut/info2/test/resources/salles.csv",
            "iut/info2/test/resources/employes.csv",
            "iut/info2/test/resources/activites.csv",
            "iut/info2/test/resources/reservations.csv"
    };

    private static final String[] CHEMINS_INVALIDES = {
            "iut/info2/test/resources/fichier_invalide.csv", // Données incorrectes
            "iut/info2/test/resources/fichier_vide.csv", // Fichier vide
            "iut/info2/test/resources/format_inconnu.csv" // Format non reconnu
    };

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        System.out.println("====== TEST DE LA CLASSE GestionDonnees ======");
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        System.out.println("======= TEST DE GestionDonnees TERMINE =======");
    }

    @Test
    void testReconnaitreEntete() {
        for (int i = 0; i < ENTETES_VALIDES.length; i++) {
            assertEquals(TYPE_FICHIERS[i], GestionDonnees.reconnaitreEntete(ENTETES_VALIDES[i], DELIMITEUR));
        }
        for (int i = 0; i < ENTETES_NON_VALIDES_SALLES.length; i++) {
            assertNotEquals(TYPE_FICHIERS[0],
                         GestionDonnees.reconnaitreEntete(ENTETES_NON_VALIDES_SALLES[i], DELIMITEUR));
        }
        for (int i = 0; i < ENTETES_NON_VALIDES_EMPLOYES.length; i++) {
            assertNotEquals(TYPE_FICHIERS[1],
                         GestionDonnees.reconnaitreEntete(ENTETES_NON_VALIDES_EMPLOYES[i], DELIMITEUR));
        }
        for (int i = 0; i < ENTETES_NON_VALIDES_ACTIVITES.length; i++) {
            assertNotEquals(TYPE_FICHIERS[2],
                         GestionDonnees.reconnaitreEntete(ENTETES_NON_VALIDES_ACTIVITES[i], DELIMITEUR));
        }
        for (int i = 0; i < ENTETES_NON_VALIDES_RESERVATIONS.length; i++) {
            assertNotEquals(TYPE_FICHIERS[3],
                         GestionDonnees.reconnaitreEntete(ENTETES_NON_VALIDES_RESERVATIONS[i], DELIMITEUR));
        }
    }

    @Test
    void testEstLigneComplete() {
        // Tests valides
        for (String ligne : LIGNES_VALIDES_EMPLOYES) {
            assertTrue(GestionDonnees.estLigneComplete(ligne, DELIMITEUR, TYPE_FICHIERS[1]));
        }
        for (String ligne : LIGNES_VALIDES_SALLES) {
            assertTrue(GestionDonnees.estLigneComplete(ligne, DELIMITEUR, TYPE_FICHIERS[0]));
        }
        for (String ligne : LIGNES_VALIDES_ACTIVITES) {
            assertTrue(GestionDonnees.estLigneComplete(ligne, DELIMITEUR, TYPE_FICHIERS[2]));
        }
        for (String ligne : LIGNES_VALIDES_RESERVATIONS) {
            assertTrue(GestionDonnees.estLigneComplete(ligne, DELIMITEUR, TYPE_FICHIERS[3]));
        }

        // Tests invalides
        for (String ligne : LIGNES_INVALIDES_EMPLOYES) {
            assertFalse(GestionDonnees.estLigneComplete(ligne, DELIMITEUR, TYPE_FICHIERS[1]));
        }
        for (String ligne : LIGNES_INVALIDES_SALLES) {
            assertFalse(GestionDonnees.estLigneComplete(ligne, DELIMITEUR, TYPE_FICHIERS[0]));
        }
        for (String ligne : LIGNES_INVALIDES_ACTIVITES) {
            assertFalse(GestionDonnees.estLigneComplete(ligne, DELIMITEUR, TYPE_FICHIERS[2]));
        }
        for (String ligne : LIGNES_INVALIDES_RESERVATIONS) {
            assertFalse(GestionDonnees.estLigneComplete(ligne, DELIMITEUR, TYPE_FICHIERS[3]));
        }
    }

    @Test
    void testConstruireDate() {
        // Cas valides
        try {
            Date dateValide = GestionDonnees.construireDate("21/10/2024", "10h00");
            assertNotNull(dateValide);
        } catch (IllegalArgumentException e) {
            fail("L'exception ne devrait pas être levée pour une date valide.");
        }

        // Cas invalides
        assertThrows(IllegalArgumentException.class, () -> {
            GestionDonnees.construireDate("21/10/2024", "10h");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            GestionDonnees.construireDate("21/10/2024", "25h00");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            GestionDonnees.construireDate("32/10/2024", "10h00");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            GestionDonnees.construireDate("21/10/2024", "10h60");
        });
    }

    @Test
    void testImporterDonneesAvecFichiersValides() {
        GestionDonnees gestion = new GestionDonnees();

        try {
            gestion.importerDonnees(CHEMINS_VALIDES);

            assertNotNull(gestion.getSalles(), "Les salles doivent être importées.");
            assertNotNull(gestion.getEmployes(), "Les employés doivent être importés.");
            assertNotNull(gestion.getActivites(), "Les activités doivent être importées.");
            assertNotNull(gestion.getReservations(), "Les réservations doivent être importées.");

            assertEquals(2, gestion.getSalles().size(), "Nombre de salles incorrect.");
            assertEquals(2, gestion.getEmployes().size(), "Nombre d'employés incorrect.");
            assertEquals(2, gestion.getActivites().size(), "Nombre d'activités incorrect.");
            assertEquals(2, gestion.getReservations().size(), "Nombre de réservations incorrect.");
        } catch (Exception e) {
            e.getMessage());
        }
    }

    @Test
    void testImporterDonneesAvecFichiersInvalides() {
        GestionDonnees gestion = new GestionDonnees();

        try {
            gestion.importerDonnees(CHEMINS_INVALIDES);

            assertEquals(0, gestion.getSalles().size(), "Pas de salles pour fichier invalide.");
            assertEquals(0, gestion.getEmployes().size(), "Pas d'employés pour fichier invalide.");
            assertEquals(0, gestion.getActivites().size(), "Pas d'activités pour fichier invalide.");
            assertEquals(0, gestion.getReservations().size(), "Pas de réservations pour fichier invalide.");

        } catch (Exception e) {
            e.getMessage());
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

            assertNotNull(gestion.getSalles(), "Les salles doivent être importées.");
            assertNotNull(gestion.getEmployes(), "Les employés doivent être importés.");

            assertEquals(2, gestion.getSalles().size(), "Nombre de salles incorrect.");
            assertEquals(2, gestion.getEmployes().size(), "Nombre d'employés incorrect.");

            assertEquals(0, gestion.getActivites().size(), "Aucune activité ne doit être importée.");
            assertEquals(0, gestion.getReservations().size(), "Aucune réservation ne doit être importée.");

        } catch (Exception e) {
            fail("Erreur inattendue pour fichiers mixtes : " + e.getMessage());
        }
    }
}