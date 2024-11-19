package iut.info2.test.saltistique;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

import iut.info2.saltistique.modele.Reservation;
import iut.info2.saltistique.modele.Salle;
import iut.info2.saltistique.modele.Utilisateur;
import iut.info2.saltistique.modele.GroupeOrdinateurs;
import iut.info2.saltistique.modele.Activite;
import iut.info2.saltistique.modele.Filtre;

import javafx.collections.ObservableList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour la classe {@link Filtre}.
 * <p>
 * Cette classe teste les fonctionnalités de filtrage sur les réservations en utilisant divers critères,
 * notamment les salles, les employés, les activités et les dates. Elle inclut des tests pour vérifier
 * l'ajout, la suppression et l'application des filtres.
 * </p>
 *
 * @author Jules Vialas, Néo Bécogné, Dorian Adams, Hugo Robles, Tom Gutierrez
 */
class TestFiltre {

    private Filtre filtre;
    private Utilisateur employe1, employe2, utilisateurNonExistant;
    private Salle salle1, salle2, salle3;
    private Activite activite1, activite2, activite3;
    private Reservation reservation1, reservation2, reservation3;
    private Map<Integer, Reservation> reservations;

    /**
     * Configuration initiale avant chaque test.
     * <p>
     * Initialise des instances de {@link Utilisateur}, {@link Salle}, {@link Activite}, et {@link Reservation},
     * et prépare une instance de la classe {@link Filtre} utilisée pour les tests.
     * </p>
     */
    @BeforeEach
    void setUp() {
        employe1 = new Utilisateur("001", "John", "Doe", "123456789");
        employe2 = new Utilisateur("002", "Jane", "Smith", "987654321");
        utilisateurNonExistant = new Utilisateur("Inconnu", "Inconnu", "Inconnu", "000000000");

        GroupeOrdinateurs groupe1 = new GroupeOrdinateurs(10, "PC", new String[]{"Word", "Excel", "PowerPoint"});
        GroupeOrdinateurs groupe2 = new GroupeOrdinateurs(5, "Mac", new String[]{"Photoshop", "Illustrator"});
        GroupeOrdinateurs groupe3 = new GroupeOrdinateurs(8, "PC", new String[]{"AutoCAD", "Revit"});

        salle1 = new Salle("Salle A", "Salle A", 50, true, true, true, groupe1);
        salle2 = new Salle("Salle B", "Salle B", 30, true, false, true, groupe2);
        salle3 = new Salle("Salle C", "Salle C", 40, false, true, true, groupe3);

        activite1 = new Activite("1", "Activité 1");
        activite2 = new Activite("2", "Activité 2");
        activite3 = new Activite("3", "Activité 3");

        LocalDateTime dateDebut1 = LocalDateTime.parse("2024-11-17T08:00");
        LocalDateTime dateFin1 = LocalDateTime.parse("2024-11-17T12:00");

        LocalDateTime dateDebut2 = LocalDateTime.parse("2024-11-17T13:00");
        LocalDateTime dateFin2 = LocalDateTime.parse("2024-11-17T17:00");

        LocalDateTime dateDebut3 = LocalDateTime.parse("2024-11-17T08:00");
        LocalDateTime dateFin3 = LocalDateTime.parse("2024-11-17T12:00");

        reservation1 = new Reservation("R1", dateDebut1, dateFin1, "", salle1, activite1, employe1);
        reservation2 = new Reservation("R2", dateDebut2, dateFin2, "", salle2, activite2, employe2);
        reservation3 = new Reservation("R3", dateDebut3, dateFin3, "", salle3, activite3, employe1);

        reservations = new HashMap<>();
        reservations.put(1, reservation1);
        reservations.put(2, reservation2);
        reservations.put(3, reservation3);

        filtre = new Filtre();
    }

    /**
     * Teste l'ajout et la suppression de filtres basés sur les salles.
     */
    @Test
    void testAjouterEtSupprimerFiltreSalle() {
        filtre.ajouterFiltreSalle(salle1);
        assertTrue(filtre.getSallesFiltrees().contains(salle1));

        filtre.supprimerFiltreSalle(salle1);
        assertFalse(filtre.getSallesFiltrees().contains(salle1));
    }

    /**
     * Teste l'ajout et la suppression de filtres basés sur les employés.
     */
    @Test
    void testAjouterEtSupprimerFiltreEmploye() {
        filtre.ajouterFiltreEmploye(employe1);
        assertTrue(filtre.getEmployesFiltres().contains(employe1));

        filtre.supprimerFiltreEmploye(employe1);
        assertFalse(filtre.getEmployesFiltres().contains(employe1));
    }

    /**
     * Teste l'ajout et la suppression de filtres basés sur les activités.
     */
    @Test
    void testAjouterEtSupprimerFiltreActivite() {
        filtre.ajouterFiltreActivite(activite1);
        assertTrue(filtre.getActivitesFiltrees().contains(activite1));

        filtre.supprimerFiltreActivite(activite1);
        assertFalse(filtre.getActivitesFiltrees().contains(activite1));
    }

    /**
     * Teste l'application d'un filtre pour une salle spécifique.
     */
    @Test
    void testAppliquerFiltresAvecSalle() {
        filtre.ajouterFiltreSalle(salle1);
        ObservableList<Reservation> resultats = filtre.appliquerFiltres(new ArrayList<>(reservations.values()));
        assertEquals(1, resultats.size());
        assertTrue(resultats.contains(reservation1));
    }

    /**
     * Teste l'application d'un filtre pour un employé spécifique.
     */
    @Test
    void testAppliquerFiltresAvecEmploye() {
        filtre.ajouterFiltreEmploye(employe1);
        ObservableList<Reservation> resultats = filtre.appliquerFiltres(new ArrayList<>(reservations.values()));
        assertEquals(2, resultats.size());
        assertTrue(resultats.contains(reservation1));
        assertTrue(resultats.contains(reservation3));
    }

    /**
     * Teste l'application d'un filtre pour une activité spécifique.
     */
    @Test
    void testAppliquerFiltresAvecActivite() {
        filtre.ajouterFiltreActivite(activite2);
        ObservableList<Reservation> resultats = filtre.appliquerFiltres(new ArrayList<>(reservations.values()));
        assertEquals(1, resultats.size());
        assertTrue(resultats.contains(reservation2));
    }

    /**
     * Teste l'application de filtres combinés (salle et employé).
     */
    @Test
    void testAppliquerFiltresCombines() {
        filtre.ajouterFiltreSalle(salle1);
        filtre.ajouterFiltreEmploye(employe1);
        ObservableList<Reservation> resultats = filtre.appliquerFiltres(new ArrayList<>(reservations.values()));
        assertEquals(1, resultats.size());
        assertTrue(resultats.contains(reservation1));
    }

    /**
     * Teste l'application de filtres lorsqu'aucune réservation ne correspond.
     */
    @Test
    void testAppliquerFiltresAucuneCorrespondance() {
        filtre.ajouterFiltreSalle(salle1);
        filtre.ajouterFiltreEmploye(employe2);
        ObservableList<Reservation> resultats = filtre.appliquerFiltres(new ArrayList<>(reservations.values()));
        assertEquals(0, resultats.size());
    }

    /**
     * Teste l'ajout d'un filtre basé sur une date et vérifie son fonctionnement.
     */
    @Test
    void testAjouterFiltreDateEtAppliquer() {
        LocalDate date1 = reservation1.getDateDebut().toLocalDate();
        filtre.ajouterFiltreDate(date1);

        assertTrue(filtre.getDatesFiltrees().contains(date1));
    }
}
