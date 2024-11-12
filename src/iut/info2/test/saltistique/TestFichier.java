package iut.info2.test.saltistique;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import iut.info2.saltistique.modele.Fichier;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Classe de test de gestion des fichiers
 */
class TestFichier {

    /** sortieStandard du terminal */
    private final PrintStream sortieStandard = System.out;
    private final ByteArrayOutputStream capteurDeSortie = new ByteArrayOutputStream();

    /** Lien vers les fichiers */
    public static final String[] LIEN_FICHIERS = {
            "src/iut/info2/test/ressources/activites.csv",
            "src/iut/info2/test/ressources/employes.csv",
            "src/iut/info2/test/ressources/reservations.csv",
            "src/iut/info2/test/ressources/salles.csv",
    };

    /** Nom des fichiers sans leurs extentions */
    public static final String[] NOM_FICHIER = {
            "activites",
            "employes",
            "reservations",
            "salles"
    };

    /** Objets fichiers */
    public static final Fichier[] OBJET_FICHIER = new Fichier[LIEN_FICHIERS.length];

    /** Contenu des fichiers */
    public static final String[][] CONTENU_DE_FICHIERS = {
            {"ABCD"},
            {"Lorem", "", "Ipsum"},
            {"a", "", "", "",  "b"},
            {"", "", "",  ""},
            {"Lorem", "DELETE FROM mysql.user", "Ipsum",  "Dolor sit amet"},
            {}
    };

    /** Résultats des contenus des fichiers */
    public static final String[] RESULTAT_POUR_CONTENU = {
            "ABCD\r\n",
            "Lorem\r\n\r\nIpsum\r\n\r\n",
            "a\r\n\r\n\r\n\r\nb\r\n",
            "\r\n\r\n\r\n\r\n",
            "Lorem\r\nDELETE FROM mysql.user\r\nIpsum\r\nDolor sit amet\r\n",
            ""
    };

    /** Validité du statut des fichiers */
    public static final boolean[] EXTENSIONS_VALIDES = {
            true,
            true,
            true,
            true,
            true,
            true
    };

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        System.out.println("------TEST DE LA CLASSE Fichier------");

        for (int indiceTest = 0;
             indiceTest < LIEN_FICHIERS.length; indiceTest++) {

            OBJET_FICHIER[indiceTest] = new Fichier(LIEN_FICHIERS[indiceTest]);
        }

    }

    // TODO : FIX THE TESTS
    @AfterAll
    static void tearDownAfterClass() throws Exception {
        System.out.println("---FIN DE TEST DE LA CLASSE Fichier---");
    }

    @Test
    void testExtensionValide(){
        Fichier fichier;

        for (int indiceTest = 0;
             indiceTest < EXTENSIONS_VALIDES.length; indiceTest++) {

            fichier = OBJET_FICHIER[indiceTest];

            assertEquals(fichier.extensionValide(), EXTENSIONS_VALIDES[indiceTest]);
        }
    }

    @Test
    void testContenuFichier() throws IOException {
        Fichier fichier;

        for (int indiceTest = 0;
             indiceTest < OBJET_FICHIER.length; indiceTest++) {

            fichier = OBJET_FICHIER[indiceTest];

            assertArrayEquals(CONTENU_DE_FICHIERS[indiceTest], fichier.contenuFichier());

        }
    }

    @Test
    void testAffichageFichier() throws IOException {
        Fichier fichier;

        for (int indiceTest = 0;
             indiceTest < OBJET_FICHIER.length;
             indiceTest++) {

            fichier = OBJET_FICHIER[indiceTest];

            System.setOut(new PrintStream(capteurDeSortie));

            fichier.affichageFichier();

            assertEquals(RESULTAT_POUR_CONTENU[indiceTest],
                    capteurDeSortie.toString());

            System.setOut(sortieStandard);

            capteurDeSortie.reset();
        }
    }

    @Test
    void testGetFichierExploite() throws IOException {
        Fichier fichier;

        for (int indiceTest = 0;
             indiceTest < LIEN_FICHIERS.length; indiceTest++) {

            fichier = new Fichier(LIEN_FICHIERS[indiceTest]);

            assertEquals(fichier.getFichierExploite(), OBJET_FICHIER[indiceTest].getFichierExploite());

        }
    }

}