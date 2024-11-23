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

    public static String[][] ACTIVTE_VALIDE;

    public static String[][] ACTIVTE_IDENTIFIANT_INVALIDE;

    public static String[][] ACTIVTE_ACTIVITE_INVALIDE;

    public static String[][] EMPLOYE_VALIDE;

    public static String[][] EMPLOYE_IDENTIFIANT_INVALIDE;

    public static String[][] EMPLOYE_NOM_INVALIDE;

    public static String[][] EMPLOYE_PRENOM_INVALIDE;

    public static String[][] EMPLOYE_TELEPHONE_INVALIDE;

    public static String[][] SALLE_VALIDE;

    public static String[][] SALLE_IDENTIFIANT_INVALIDE;

    public static String[][] SALLE_NOM_INVALIDE;

    public static String[][] SALLE_CAPACITE_INVALIDE;

    public static String[][] SALLE_VIDEOPROJECTEUR_INVALIDE;

    public static String[][] SALLE_ECRANXXL_INVALIDE;

    public static String[][] ORDINATEUR_VALIDE;

    public static String[][] ORDINATEUR_NB_INVALIDE;

    public static String[][] ORDINATEUR_TYPE_INVALIDE;

    public static String[][] ORDINATEUR_IMPRIMANTE_INVALIDE;

    public static String[][] RESERVATION_VALIDE;

    public static String[][] RESERVATION_IDENTIFIANT_INVALIDE;

    public static String[][] RESERVATION_SALLE_INVALIDE;

    public static String[][] RESERVATION_EMPLOYE_INVALIDE;

    public static String[][] RESERVATION_ACTIVITE_INVALIDE;

    public static String[][] RESERVATION_DATE_INVALIDE;

    public static String[][] RESERVATION_HEURE_DEBUT_INVALIDE;

    public static String[][] RESERVATION_HEURE_FIN_INVALIDE;





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