package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import saltistic.modele.GestionDonnees;

import java.util.Date;

class TestGestionDonnees {

    final String DELIMITEUR = ";";

    final String[] ENTETES_VALIDES = {
            "Ident;Nom;Capacité;videoproj;ecranXXL;ordinateur;type;logiciels;imprimante",
            "Ident;Nom;Prenom;Telephone",
            "Ident;Activité",
            "Ident;salle;employe;activite;date;heuredebut;heurefin;;;;;",
    };

    final String[] RESULTAT_RECONNAITRE_ENTETES_VALIDES = {
            "salles",
            "employes",
            "activites",
            "reservations",
    };

    final String[] ENTETES_NON_VALIDES = {
            "Ident;Nom;Capacité;videoproj;ecranXXL;ordinateur;type;logiciels;imprimante;telephone",
            "Ident;Nom;Prenom;",
            "Ident",
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
            "12345678;Salle D;30;oui;non;;PC portable;1;oui", // nombre de pc vide
            "12345678;Salle D;30;oui;non;;;1;oui", // nombre de pc vide et type de pc vide
    };

    final String[] LIGNES_VALIDES_RESERVATIONS = {
            "R123456;20240101;E123456;Dupont Jean;21/10/2024;10h00;12h00;;;;;",
            "R654321;20240102;E654321;Martin Claire;21/10/2024;14h00;15h00;Commentaire;;;;"
    };

    final String[] LIGNES_INVALIDES_RESERVATIONS = {
            "R12345;20240101;E123456;Dupont Jean;21/10/2024;10h00;12h00;;", // ID de réservation trop court
            "R123456;20240101;E123456;Dupont Jean;21/10/2024;12h00;10h00;;", // Heure fin avant début
            "R123456;20240101;E123456;Dupont Jean;21/10/2024;10h00;12h00;Commentaire;Commentaire;Commentaire;" // Trop de champs
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
            assertEquals(RESULTAT_RECONNAITRE_ENTETES_VALIDES[i], GestionDonnees.reconnaitreEntete(ENTETES_VALIDES[i], DELIMITEUR));
        }
        for (String entete : ENTETES_NON_VALIDES) {
            assertNull(GestionDonnees.reconnaitreEntete(entete, DELIMITEUR));
        }
    }

    @Test
    void testEstLigneComplete() {
        // Tests valides
        for (String ligne : LIGNES_VALIDES_EMPLOYES) {
            assertTrue(GestionDonnees.estLigneComplete(ligne, DELIMITEUR, "employes"));
        }
        for (String ligne : LIGNES_VALIDES_SALLES) {
            assertTrue(GestionDonnees.estLigneComplete(ligne, DELIMITEUR, "salles"));
        }
        for (String ligne : LIGNES_VALIDES_RESERVATIONS) {
            assertTrue(GestionDonnees.estLigneComplete(ligne, DELIMITEUR, "reservations"));
        }

        // Tests invalides
        for (String ligne : LIGNES_INVALIDES_EMPLOYES) {
            assertFalse(GestionDonnees.estLigneComplete(ligne, DELIMITEUR, "employes"));
        }
        for (String ligne : LIGNES_INVALIDES_SALLES) {
            assertFalse(GestionDonnees.estLigneComplete(ligne, DELIMITEUR, "salles"));
        }
        for (String ligne : LIGNES_INVALIDES_RESERVATIONS) {
            assertFalse(GestionDonnees.estLigneComplete(ligne, DELIMITEUR, "reservations"));
        }
    }

    // TODO : à refaire
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
}