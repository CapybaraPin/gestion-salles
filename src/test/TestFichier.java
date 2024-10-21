package test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TestFichier {


    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        System.out.println("====== TEST DE LA CLASSE Fichier ======");
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        System.out.println("======= TEST DE Fichier TERMINE =======");
    }

    @Test
    void testFichier() {
        // TODO: implémenter ici
    }

    @Test
    void testContenuFichier() {
        // TODO: implémenter ici
    }
}