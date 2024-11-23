package iut.info2.test.saltistique;

import iut.info2.saltistique.modele.Regex;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test de la classe Regex.
 * Voir justification des tests dans le document de conception.
 */
class TestRegex {

    public static String[][] ACTIVTE_VALIDE = {
            {"A0000000", "A"},
            {"A9999999", "réunion"},
    };
    public static String[][] ACTIVTE_IDENTIFIANT_INVALIDE = {
            {"B0000000", "A"},
            {"A000000", "réunion"},
            {"A00000000", "A"},
            {"", "réunion"},
            {" ", "A"},
    };

    public static String[][] ACTIVTE_ACTIVITE_INVALIDE = {
            {"A0000000", ""},
            {"A9999999", " "},
    };

    public static String[][] EMPLOYE_VALIDE = {
            {"E000000", "N", "P", "0000"},
            {"E999999", "Dupont", "Pierre", "9999"},
            {"E000000", "N", "P", ""},
            {"E999999", "Dupont", "Pierre", " "},
    };

    public static String[][] EMPLOYE_IDENTIFIANT_INVALIDE = {
            {"B000000", "N", "P", "0000"},
            {"E00000", "Dupont", "Pierre", "9999"},
            {"E0000000", "N", "P", ""},
            {"", "Dupont", "Pierre", " "},
            {" ", "N", "P", "0000"},
    };

    public static String[][] EMPLOYE_NOM_INVALIDE = {
            {"E000000", "", "P", "0000"},
            {"E999999", " ", "Pierre", "9999"},
            {"E000000", "", "P", ""},
            {"E999999", " ", "Pierre", " "}
    };

    public static String[][] EMPLOYE_PRENOM_INVALIDE = {
            {"E000000", "N", "", "0000"},
            {"E999999", "Dupont", " ", "9999"},
            {"E000000", "N", "", ""},
            {"E999999", "Dupont", " ", " "}
    };

    public static String[][] EMPLOYE_TELEPHONE_INVALIDE = {
            {"E000000", "N", "P", "123"},
            {"E999999", "Dupont", "Pierre", "12345"},
            {"E000000", "N", "P", "deux"}
    };

    public static String[][] SALLE_VALIDE = {
            {"00000000", "N", "0", "o", "o", "0", "", "", "o"},
            {"99999999", "Nom", "99", "O", "O", "99", " ", " ", "O"},
            {"00000000", "N", "0", "n", "n", "", "T", "L", "n"},
            {"99999999", "Nom", "99", "N", "N", " ", "Type", "Liste", "N"},
    };

    public static String[][] SALLE_IDENTIFIANT_INVALIDE = {
            {"0000000", "N", "0", "o", "o", "0", "", "", "o"},
            {"000000000", "Nom", "99", "O", "O", "99", " ", " ", "O"},
            {"deuxdeux", "N", "0", "n", "n", "", "T", "L", "n"},
            {"", "Nom", "99", "N", "N", " ", "Type", "Liste", "N"},
            {" ", "N", "0", "o", "o", "0", "", "", "o"},
    };

    public static String[][] SALLE_NOM_INVALIDE = {
            {"00000000", "", "0", "o", "o", "0", "", "", "o"},
            {"99999999", " ", "99", "O", "O", "99", " ", " ", "O"},
            {"00000000", "", "0", "n", "n", "", "T", "L", "n"},
            {"99999999", " ", "99", "N", "N", " ", "Type", "Liste", "N"},
    };

    public static String[][] SALLE_CAPACITE_INVALIDE = {
            {"00000000", "N", "", "o", "o", "0", "", "", "o"},
            {"99999999", "Nom", " ", "O", "O", "99", " ", " ", "O"},
            {"00000000", "N", "trois", "n", "n", "", "T", "L", "n"},
            {"99999999", "Nom", "", "N", "N", " ", "Type", "Liste", "N"},
    };

    public static String[][] SALLE_VIDEOPROJECTEUR_INVALIDE = {
            {"00000000", "N", "0", "", "o", "0", "", "", "o"},
            {"99999999", "Nom", "99", " ", "O", "99", " ", " ", "O"},
            {"00000000", "N", "0", "Vrai", "n", "", "T", "L", "n"},
            {"99999999", "Nom", "99", "", "N", " ", "Type", "Liste", "N"},
    };

    public static String[][] SALLE_ECRANXXL_INVALIDE = {
            {"00000000", "N", "0", "o", "", "0", "", "", "o"},
            {"99999999", "Nom", "99", "O", " ", "99", " ", " ", "O"},
            {"00000000", "N", "0", "n", "Vrai", "", "T", "L", "n"},
            {"99999999", "Nom", "99", "N", "", " ", "Type", "Liste", "N"},
    };

    public static String[][] ORDINATEUR_VALIDE = {
            {"0", "T", "", "o"},
            {"99", "Type", " ", "O"},
            {"0", "T", "L", "n"},
            {"99", "Type", "Liste", "N"},
    };

    public static String[][] ORDINATEUR_NB_INVALIDE = {
            {"", "T", "", "o"},
            {" ", "Type", " ", "O"},
            {"un", "T", "L", "n"},
            {"", "Type", "Liste", "N"},
    };

    public static String[][] ORDINATEUR_TYPE_INVALIDE = {
            {"0", "", "", "o"},
            {"99", " ", " ", "O"},
            {"0", "", "L", "n"},
            {"99", " ", "Liste", "N"},
    };

    public static String[][] ORDINATEUR_IMPRIMANTE_INVALIDE = {
            {"0", "T", "", ""},
            {"99", "Type", " ", " "},
            {"0", "T", "L", "Vrai"},
            {"99", "Type", "Liste", ""},
    };

    public static String[][] RESERVATION_VALIDE = {
            {"R000000", "00000000", "E000000", "A", "00/00/0000", "00h00", "00h00", "", "", "", "", ""},
            {"R999999", "99999999", "E999999", "Réunion", "99/99/9999", "0h00", "0h00", " ", " ", " ", " ", " "},
            {"R000000", "00000000", "E000000", "A", "00/00/0000", "99h99", "99h99", "C", "N", "P", "0000000000", "S"},
            {"R999999", "99999999", "E999999", "Réunion", "99/99/9999", "00h00", "00h00", "Commentaire", "Nom", "Prenom", "9999999999", "Sujet"},
    };

    public static String[][] RESERVATION_IDENTIFIANT_INVALIDE = {
            {"B000000", "00000000", "E000000", "A", "00/00/0000", "00h00", "00h00", "", "", "", "", ""},
            {"R00000", "99999999", "E999999", "Réunion", "99/99/9999", "0h00", "0h00", " ", " ", " ", " ", " "},
            {"R9999999", "00000000", "E000000", "A", "00/00/0000", "99h99", "99h99", "C", "N", "P", "0000000000", "S"},
            {"", "99999999", "E999999", "Réunion", "99/99/9999", "00h00", "00h00", "Commentaire", "Nom", "Prenom", "9999999999", "Sujet"},
            {" ", "00000000", "E000000", "A", "00/00/0000", "00h00", "00h00", "", "", "", "", ""},
    };

    public static String[][] RESERVATION_SALLE_INVALIDE = {
            {"R000000", "0000000", "E000000", "A", "00/00/0000", "00h00", "00h00", "", "", "", "", ""},
            {"R999999", "000000000", "E999999", "Réunion", "99/99/9999", "0h00", "0h00", " ", " ", " ", " ", " "},
            {"R000000", "deuxdeux", "E000000", "A", "00/00/0000", "99h99", "99h99", "C", "N", "P", "0000000000", "S"},
            {"R999999", "", "E999999", "Réunion", "99/99/9999", "00h00", "00h00", "Commentaire", "Nom", "Prenom", "9999999999", "Sujet"},
            {"R000000", " ", "E000000", "A", "00/00/0000", "00h00", "00h00", "", "", "", "", ""},
    };

    public static String[][] RESERVATION_EMPLOYE_INVALIDE = {
            {"R000000", "00000000", "B000000", "A", "00/00/0000", "00h00", "00h00", "", "", "", "", ""},
            {"R999999", "99999999", "E00000", "Réunion", "99/99/9999", "0h00", "0h00", " ", " ", " ", " ", " "},
            {"R000000", "00000000", "E0000000", "A", "00/00/0000", "99h99", "99h99", "C", "N", "P", "0000000000", "S"},
            {"R999999", "99999999", "", "Réunion", "99/99/9999", "00h00", "00h00", "Commentaire", "Nom", "Prenom", "9999999999", "Sujet"},
            {"R000000", "00000000", " ", "A", "00/00/0000", "00h00", "00h00", "", "", "", "", ""}
    };

    public static String[][] RESERVATION_ACTIVITE_INVALIDE = {
            {"R000000", "00000000", "E000000", "", "00/00/0000", "00h00", "00h00", "", "", "", "", ""},
            {"R999999", "99999999", "E999999", " ", "99/99/9999", "0h00", "0h00", " ", " ", " ", " ", " "},
            {"R000000", "00000000", "E000000", "", "00/00/0000", "99h99", "99h99", "C", "N", "P", "0000000000", "S"},
            {"R999999", "99999999", "E999999", "", "99/99/9999", "00h00", "00h00", "Commentaire", "Nom", "Prenom", "9999999999", "Sujet"},
    };

    public static String[][] RESERVATION_DATE_INVALIDE = {
            {"R000000", "00000000", "E000000", "A", "01/01/24", "00h00", "00h00", "", "", "", "", ""},
            {"R999999", "99999999", "E999999", "Réunion", "123/01/2024", "0h00", "0h00", " ", " ", " ", " ", " "},
            {"R000000", "00000000", "E000000", "A", "01-01-2024", "99h99", "99h99", "C", "N", "P", "0000000000", "S"},
            {"R999999", "99999999", "E999999", "Réunion", "AA/BB/CCCC", "00h00", "00h00", "Commentaire", "Nom", "Prenom", "9999999999", "Sujet"},
    };

    public static String[][] RESERVATION_HEURE_DEBUT_INVALIDE = {
            {"R000000", "00000000", "E000000", "A", "00/00/0000", "01h1", "00h00", "", "", "", "", ""},
            {"R999999", "99999999", "E999999", "Réunion", "99/99/9999", "01h123", "0h00", " ", " ", " ", " ", " "},
            {"R000000", "00000000", "E000000", "A", "00/00/0000", "99h99", "01H12", "C", "N", "P", "0000000000", "S"},
            {"R999999", "99999999", "E999999", "Réunion", "99/99/9999", "unhun", "00h00", "Commentaire", "Nom", "Prenom", "9999999999", "Sujet"},
    };

    public static String[][] RESERVATION_HEURE_FIN_INVALIDE = {
            {"R000000", "00000000", "E000000", "A", "00/00/0000", "00h00", "01h1", "", "", "", "", ""},
            {"R999999", "99999999", "E999999", "Réunion", "99/99/9999", "0h00", "01h123", " ", " ", " ", " ", " "},
            {"R000000", "00000000", "E000000", "A", "00/00/0000", "99h99", "99h99", "C", "N", "P", "0000000000", "S"},
            {"R999999", "99999999", "E999999", "Réunion", "99/99/9999", "00h00", "unhun", "Commentaire", "Nom", "Prenom", "9999999999", "Sujet"},
    };

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        System.out.println("Début des tests");
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        System.out.println("Fin des tests");
    }

    @Test
    void testRegexActivite() {
        for (String[] s : ACTIVTE_VALIDE) {
            String ligne = String.join(";", s);
            assertTrue(ligne.matches(Regex.ACTIVITES.getRegex(";")));
        }

        for (String[] s : ACTIVTE_IDENTIFIANT_INVALIDE) {
            String ligne = String.join(";", s);
            assertFalse(ligne.matches(Regex.ACTIVITES.getRegex(";")));
        }

        for (String[] s : ACTIVTE_ACTIVITE_INVALIDE) {
            String ligne = String.join(";", s);
            assertFalse(ligne.matches(Regex.ACTIVITES.getRegex(";")));
        }
    }

    @Test
    void testRegexSalle() {
        for (String[] s : SALLE_VALIDE) {
            String ligne = String.join(";", s);
            assertTrue(ligne.matches(Regex.SALLES.getRegex(";")));
        }

        for (String[] s : SALLE_IDENTIFIANT_INVALIDE) {
            String ligne = String.join(";", s);
            assertFalse(ligne.matches(Regex.SALLES.getRegex(";")));
        }

        for (String[] s : SALLE_NOM_INVALIDE) {
            String ligne = String.join(";", s);
            assertFalse(ligne.matches(Regex.SALLES.getRegex(";")));
        }

        for (String[] s : SALLE_CAPACITE_INVALIDE) {
            String ligne = String.join(";", s);
            assertFalse(ligne.matches(Regex.SALLES.getRegex(";")));
        }

        for (String[] s : SALLE_VIDEOPROJECTEUR_INVALIDE) {
            String ligne = String.join(";", s);
            assertFalse(ligne.matches(Regex.SALLES.getRegex(";")));
        }

        for (String[] s : SALLE_ECRANXXL_INVALIDE) {
            String ligne = String.join(";", s);
            assertFalse(ligne.matches(Regex.SALLES.getRegex(";")));
        }
    }

    @Test
    void testRegexEmploye() {
        for (String[] s : EMPLOYE_VALIDE) {
            String ligne = String.join(";", s);
            assertTrue(ligne.matches(Regex.EMPLOYES.getRegex(";")));
        }

        for (String[] s : EMPLOYE_IDENTIFIANT_INVALIDE) {
            String ligne = String.join(";", s);
            assertFalse(ligne.matches(Regex.EMPLOYES.getRegex(";")));
        }

        for (String[] s : EMPLOYE_NOM_INVALIDE) {
            String ligne = String.join(";", s);
            assertFalse(ligne.matches(Regex.EMPLOYES.getRegex(";")));
        }

        for (String[] s : EMPLOYE_PRENOM_INVALIDE) {
            String ligne = String.join(";", s);
            assertFalse(ligne.matches(Regex.EMPLOYES.getRegex(";")));
        }

        for (String[] s : EMPLOYE_TELEPHONE_INVALIDE) {
            String ligne = String.join(";", s);
            assertFalse(ligne.matches(Regex.EMPLOYES.getRegex(";")));
        }
    }

    @Test
    void testRegexReservation() {
        for (String[] s : RESERVATION_VALIDE) {
            String ligne = String.join(";", s);
            assertTrue(ligne.matches(Regex.RESERVATIONS.getRegex(";")));
        }

        for (String[] s : RESERVATION_IDENTIFIANT_INVALIDE) {
            String ligne = String.join(";", s);
            assertFalse(ligne.matches(Regex.RESERVATIONS.getRegex(";")));
        }

        for (String[] s : RESERVATION_SALLE_INVALIDE) {
            String ligne = String.join(";", s);
            assertFalse(ligne.matches(Regex.RESERVATIONS.getRegex(";")));
        }

        for (String[] s : RESERVATION_EMPLOYE_INVALIDE) {
            String ligne = String.join(";", s);
            assertFalse(ligne.matches(Regex.RESERVATIONS.getRegex(";")));
        }

        for (String[] s : RESERVATION_ACTIVITE_INVALIDE) {
            String ligne = String.join(";", s);
            assertFalse(ligne.matches(Regex.RESERVATIONS.getRegex(";")));
        }

        for (String[] s : RESERVATION_DATE_INVALIDE) {
            String ligne = String.join(";", s);
            assertFalse(ligne.matches(Regex.RESERVATIONS.getRegex(";")));
        }

        for (String[] s : RESERVATION_HEURE_DEBUT_INVALIDE) {
            String ligne = String.join(";", s);
            assertFalse(ligne.matches(Regex.RESERVATIONS.getRegex(";")));
        }

        for (String[] s : RESERVATION_HEURE_FIN_INVALIDE) {
            String ligne = String.join(";", s);
            assertFalse(ligne.matches(Regex.RESERVATIONS.getRegex(";")));
        }
    }

    @Test
    void testRegexOrdinateur() {
        for (String[] s : ORDINATEUR_VALIDE) {
            String ligne = ";;"; // TODO vérifier le nombre de ; à ajouter
            ligne += String.join(";", s);
            assertTrue(ligne.matches(Regex.ORDINATEURS.getRegex(";")));
        }

        for (String[] s : ORDINATEUR_NB_INVALIDE) {
            String ligne = ";;"; // TODO vérifier le nombre de ; à ajouter
            ligne += String.join(";", s);
            assertFalse(ligne.matches(Regex.ORDINATEURS.getRegex(";")));
        }

        for (String[] s : ORDINATEUR_TYPE_INVALIDE) {
            String ligne = ";;"; // TODO vérifier le nombre de ; à ajouter
            ligne += String.join(";", s);
            assertFalse(ligne.matches(Regex.ORDINATEURS.getRegex(";")));
        }

        for (String[] s : ORDINATEUR_IMPRIMANTE_INVALIDE) {
            String ligne = ";;"; // TODO vérifier le nombre de ; à ajouter
            ligne += String.join(";", s);
            assertFalse(ligne.matches(Regex.ORDINATEURS.getRegex(";")));
        }
    }
}