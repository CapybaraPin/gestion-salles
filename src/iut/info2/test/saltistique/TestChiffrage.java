package iut.info2.test.saltistique;

import iut.info2.saltistique.modele.Chiffrage;
import iut.info2.saltistique.modele.Fichier;
import org.junit.jupiter.api.*;

import java.io.*;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class TestChiffrage {

    private final String CHEMIN_FICHIER_TEST = "test.csv";
    private final String CONTENUE_FICHIER_TEST = "Ident;Nom;Capacite;videoproj;ecranXXL;ordinateur;type;logiciels;imprimante\n";

    private Chiffrage chiffrage;

    int nbTestTotal;
    int nbTestReussi;

    @BeforeAll
    static void setUpBeforeClass() throws IOException{
        System.out.println("========== TEST DE LA CLASSE Chiffrage ==========\n");
    }

    @AfterAll
    static void tearDownAfterClass() {
        System.out.println("\n========== FIN DE TEST DE LA CLASSE Chiffrage ==========");
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
        nbTestReussi = 0;
        nbTestTotal = 0;

        chiffrage.calculeClePartager(new Chiffrage().getClePublique());
        chiffrage.genererCleVigenere();
        String cheminFichierChiffre = chiffrage.chiffrer();

        assertNotNull(cheminFichierChiffre, "Le chemin du fichier chiffré ne devrait pas être nul.");
        nbTestTotal++;
        if(cheminFichierChiffre != null) { nbTestReussi++; }

        File fichierChiffre = new File(cheminFichierChiffre);
        assertTrue(fichierChiffre.exists(), "Le fichier chiffré devrait exister.");
        nbTestTotal++;
        if(fichierChiffre.exists()) { nbTestReussi++; }
        assertTrue(fichierChiffre.length() > 0, "Le fichier chiffré ne devrait pas être vide.");
        nbTestTotal++;
        if(fichierChiffre.length() > 0) { nbTestReussi++; }
        assertTrue(!fichierIdentique(fichierChiffre, new File(CHEMIN_FICHIER_TEST)), "Les fichier ne sont pas identique");
        nbTestTotal++;
        if(!fichierIdentique(fichierChiffre, new File(CHEMIN_FICHIER_TEST))) { nbTestReussi++; }

        System.out.println(nbTestReussi + " tests réussi sur " + nbTestTotal + " tests");
    }

    @Test
    void dechiffrer() throws IOException {
        System.out.println("====TEST DE LA METHODE dechiffrer====");
        nbTestReussi = 0;
        nbTestTotal = 0;
        Chiffrage dechiffrer = new Chiffrage();

        chiffrage.calculeClePartager(dechiffrer.getClePublique());
        chiffrage.genererCleVigenere();
        String cheminFichierChiffrer = chiffrage.chiffrer();
        dechiffrer.setCheminFichier(cheminFichierChiffrer);
        dechiffrer.calculeClePartager(chiffrage.getClePublique());
        dechiffrer.genererCleVigenere();
        String cheminFichierDechiffrer = dechiffrer.dechiffrer();

        assertNotNull(cheminFichierDechiffrer, "Le chemin du fichier déchiffré ne devrait pas être nul.");
        nbTestTotal++;
        if(cheminFichierDechiffrer != null) { nbTestReussi++; }
        File fichierDechiffrer = new File(cheminFichierDechiffrer);
        assertTrue(fichierDechiffrer.exists(), "Le fichier déchiffré devrait exister.");
        nbTestTotal++;
        if(fichierDechiffrer.exists()) { nbTestReussi++; }
        assertTrue(fichierDechiffrer.length() > 0, "Le fichier chiffré ne devrait pas être vide.");
        nbTestTotal++;
        if (fichierDechiffrer.length() > 0) { nbTestReussi++; }
        assertTrue(fichierIdentique(fichierDechiffrer, new File(CHEMIN_FICHIER_TEST)), "Les fichier devraient être identique");
        nbTestTotal++;
        if (fichierIdentique(fichierDechiffrer, new File(CHEMIN_FICHIER_TEST))) { nbTestReussi++; }

        System.out.println(nbTestReussi + " tests réussi sur " + nbTestTotal + " tests");
    }

    @Test
    void calculeClePartager() {
        System.out.println("====TEST DE LA METHODE calculeClePartager====");
        nbTestReussi = 0;
        nbTestTotal = 0;

        Chiffrage chiffrage1 = new Chiffrage();
        chiffrage1.calculeClePartager(chiffrage.getClePublique());
        chiffrage.calculeClePartager(chiffrage1.getClePublique());

        assertTrue(chiffrage1.getClePartager().equals(chiffrage.getClePartager()), "Les clés partager devraient être identique");
        nbTestTotal++;
        if(chiffrage1.getClePartager().equals(chiffrage.getClePartager())) { nbTestReussi++; }

        System.out.println(nbTestReussi + " tests réussi sur " + nbTestTotal + " tests");
    }

    @Test
    void genererCleVigenere() {
        System.out.println("====TEST DE LA METHODE genererCleVigenere====");
        nbTestReussi = 0;
        nbTestTotal = 0;

        assertThrows(NullPointerException.class, () -> chiffrage.chiffrer());
        nbTestTotal++;
        try {
            chiffrage.chiffrer();
        } catch (NullPointerException | IOException e) {
            nbTestReussi++;
        }

        chiffrage.calculeClePartager(new Chiffrage().getClePublique());
        chiffrage.genererCleVigenere();
        assertDoesNotThrow(() -> chiffrage.chiffrer());
        nbTestTotal++;
        try {
            chiffrage.chiffrer();
            nbTestReussi++;
        } catch (IOException e) {}

        System.out.println(nbTestReussi + " tests réussi sur " + nbTestTotal + " tests");

    }

    private boolean fichierIdentique(File file1, File file2) throws IOException {
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