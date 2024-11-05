package iut.info2.test.saltistique;

import iut.info2.saltistique.modele.Client;
import iut.info2.saltistique.modele.Fichier;
import iut.info2.saltistique.modele.Notification;
import iut.info2.saltistique.modele.Serveur;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TestServeur {

    private final int PORT = 50000;
    private final String HOST = "localhost";

    private final Fichier[] fichiers = {
            new Fichier("src/ressources/fichiers/activites.csv"),
            new Fichier("src/ressources/fichiers/employes.csv"),
            new Fichier("src/ressources/fichiers/reservations.csv"),
            new Fichier("src/ressources/fichiers/salles.csv")
    };

    private Serveur serveur;
    private Client client;

    TestServeur() throws IOException {
    }

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        System.out.println("====== TEST DE LA CLASSE Serveur ======");
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        System.out.println("======= TEST DE Serveur TERMINE =======");
    }

    @BeforeEach
    void setUp() {
        this.serveur = new Serveur(PORT, fichiers);
        try {
            this.client = new Client(HOST, PORT);
        } catch (Notification e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void run() {
        serveur.run();
    }

    @Test
    void arreter() {
        serveur.arreter();

    }
}