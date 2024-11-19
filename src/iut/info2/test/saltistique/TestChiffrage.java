package iut.info2.test.saltistique;

import iut.info2.saltistique.modele.Chiffrage;
import iut.info2.saltistique.modele.Fichier;
import org.junit.jupiter.api.*;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class TestChiffrage {

    private final String CHEMIN_FICHIER_TEST = "test.csv";
    private final String CONTENUE_FICHIER_TEST = "Ident;Nom;Capacite;videoproj;ecranXXL;ordinateur;type;logiciels;imprimante";

    private Chiffrage chiffrage;

    @BeforeAll
    static void setUpBeforeClass() throws IOException{
        System.out.println("==== TEST DE LA CLASSE Chiffrage ====");
    }

    // TODO : FIX THE TESTS
    @AfterAll
    static void tearDownAfterClass() {
        System.out.println("==== FIN DE TEST DE LA CLASSE Chiffrage ====");
    }

    @BeforeEach
    void setUp() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CHEMIN_FICHIER_TEST))) {
            writer.write(CONTENUE_FICHIER_TEST);
        }
        chiffrage = new Chiffrage(CHEMIN_FICHIER_TEST);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void chiffrer() throws IOException {
        System.out.println("====TEST DE LA METHODE chiffrer====");

        chiffrage.calculeClePartager(new Chiffrage().getClePublic());
        chiffrage.genererCleVigenere();
        String cheminFichierChiffre = chiffrage.chiffrer();

        assertNotNull(cheminFichierChiffre, "Le chemin du fichier chiffré ne devrait pas être nul.");

        File fichierChiffre = new File(cheminFichierChiffre);
        assertTrue(fichierChiffre.exists(), "Le fichier chiffré devrait exister.");
        assertTrue(fichierChiffre.length() > 0, "Le fichier chiffré ne devrait pas être vide.");
        assertTrue(!fichierIdentique(fichierChiffre, new File(CHEMIN_FICHIER_TEST)), "Les fichier ne sont pas identique");

    }

    @Test
    void dechiffrer() throws IOException {
        System.out.println("====TEST DE LA METHODE dechiffrer====");
        Chiffrage dechiffrer = new Chiffrage();

        chiffrage.calculeClePartager(dechiffrer.getClePublic());
        chiffrage.genererCleVigenere();
        String cheminFichierChiffrer = chiffrage.chiffrer();
        dechiffrer.setCheminFichier(cheminFichierChiffrer);
    }

    @Test
    void getClePublic() {
    }

    @Test
    void getClePartager() {
    }

    @Test
    void calculeClePartager() {
    }

    @Test
    void genererCleVigenere() {
    }

    @Test
    void setCheminFichier() {
    }

    private static boolean fichierIdentique(File file1, File file2) throws IOException {
        // Vérifiez si les tailles des fichiers sont différentes
        if (file1.length() != file2.length()) {
            return false;
        }

        try (FileInputStream fis1 = new FileInputStream(file1);
             FileInputStream fis2 = new FileInputStream(file2)) {

            int byte1, byte2;

            // Comparez les octets un par un
            while ((byte1 = fis1.read()) != -1 && (byte2 = fis2.read()) != -1) {
                if (byte1 != byte2) {
                    return false; // Contenu différent
                }
            }
        }

        // Les fichiers sont identiques
        return true;
    }
}