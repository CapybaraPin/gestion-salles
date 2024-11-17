package iut.info2.test.saltistique;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;
import java.util.HashMap;
import iut.info2.saltistique.modele.Filtre;
import iut.info2.saltistique.modele.Reservation;
import iut.info2.saltistique.modele.Utilisateur;
import iut.info2.saltistique.modele.Salle;
import iut.info2.saltistique.modele.GroupeOrdinateurs;
import iut.info2.saltistique.modele.Activite;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Collection;

/**
 * Classe de tests unitaires pour la classe  Filtre.
 * Les tests vérifient la fonctionnalité des filtres appliqués sur les réservations,
 * salles, activités et utilisateurs. Différents scénarios sont couverts,
 * comme les utilisateurs existants, inexistants et non définis.
 *
 * @author Jules Vialas, Néo Bécogné, Dorian Adams, Hugo Robles, Tom Gutierrez
 */
public class TestFiltre {

    private Filtre filtre;
    private Map<Integer, Reservation> reservations;
    private Utilisateur employe1;
    private Utilisateur employe2;
    private Utilisateur utilisateurNonExistant;
    private Reservation reservation1;
    private Reservation reservation2;
    private Reservation reservation3;

    /**
     * Méthode exécutée avant chaque test pour initialiser les objets nécessaires.
     * Crée des utilisateurs, des salles, des groupes d'ordinateurs et des réservations.
     */
    @BeforeEach
    void setUp() {
        employe1 = new Utilisateur("John", "Doe", "123456789", "john.doe@example.com");
        employe2 = new Utilisateur("Jane", "Smith", "987654321", "jane.smith@example.com");
        utilisateurNonExistant = new Utilisateur("Inconnu", "Inconnu", "000000000", "inconnu@example.com");

        GroupeOrdinateurs groupe1 = new GroupeOrdinateurs(10, "PC", new String[]{"Word", "Excel", "PowerPoint"});
        GroupeOrdinateurs groupe2 = new GroupeOrdinateurs(5, "Mac", new String[]{"Photoshop", "Illustrator"});
        GroupeOrdinateurs groupe3 = new GroupeOrdinateurs(8, "PC", new String[]{"AutoCAD", "Revit"});

        Salle salle1 = new Salle("Salle A", "Salle A", 50, true, true, true, groupe1);
        Salle salle2 = new Salle("Salle B", "Salle B", 30, true, false, true, groupe2);
        Salle salle3 = new Salle("Salle C", "Salle C", 40, false, true, true, groupe3);

        Activite activite1 = new Activite("Activité 1", "Activité Description 1");
        Activite activite2 = new Activite("Activité 2", "Activité Description 2");
        Activite activite3 = new Activite("Activité 3", "Activité Description 3");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        LocalDateTime dateDebut1 = LocalDateTime.parse("2024-11-17 08:00", formatter);
        LocalDateTime dateFin1 = LocalDateTime.parse("2024-11-17 12:00", formatter);

        LocalDateTime dateDebut2 = LocalDateTime.parse("2024-11-17 13:00", formatter);
        LocalDateTime dateFin2 = LocalDateTime.parse("2024-11-17 17:00", formatter);

        LocalDateTime dateDebut3 = LocalDateTime.parse("2024-11-17 08:00", formatter);
        LocalDateTime dateFin3 = LocalDateTime.parse("2024-11-17 12:00", formatter);

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
     * Teste l'application d'un filtre avec un utilisateur existant.
     * Le test vérifie que seules les réservations, salles et activités correspondant
     * à l'utilisateur spécifié sont retournées.
     */
    @Test
    void testFiltreAvecUtilisateurExistants() {
        filtre.setEmployeFiltre(employe1);

        Map<String, ObservableList<?>> resultats = filtre.appliquerFiltres(reservations);

        ObservableList<Reservation> reservationsFiltrees = FXCollections.observableArrayList(
                (Collection<Reservation>) resultats.get("reservations"));
        ObservableList<Salle> sallesFiltrees = FXCollections.observableArrayList(
                (Collection<Salle>) resultats.get("salles"));
        ObservableList<Activite> activitesFiltrees = FXCollections.observableArrayList(
                (Collection<Activite>) resultats.get("activites"));
        ObservableList<Utilisateur> utilisateursFiltrees = FXCollections.observableArrayList(
                (Collection<Utilisateur>) resultats.get("utilisateurs"));

        assertEquals(2, reservationsFiltrees.size());
        assertTrue(reservationsFiltrees.contains(reservation1));
        assertTrue(reservationsFiltrees.contains(reservation3));

        assertEquals(1, sallesFiltrees.size());
        assertTrue(sallesFiltrees.contains(reservation1.getSalle()));

        assertEquals(1, activitesFiltrees.size());
        assertTrue(activitesFiltrees.contains(reservation1.getActivite()));

        assertEquals(1, utilisateursFiltrees.size());
        assertTrue(utilisateursFiltrees.contains(employe1));
    }

    /**
     * Teste l'application d'un filtre avec un utilisateur inexistant.
     * Le test vérifie que lorsque l'utilisateur n'existe pas, aucune réservation,
     * salle, ou activité n'est retournée, sauf l'utilisateur inexistant.
     */
    @Test
    void testFiltreAvecUtilisateurInexistant() {
        filtre.setEmployeFiltre(utilisateurNonExistant);

        Map<String, ObservableList<?>> resultats = filtre.appliquerFiltres(reservations);

        ObservableList<Reservation> reservationsFiltrees = FXCollections.observableArrayList(
                (Collection<Reservation>) resultats.get("reservations"));
        ObservableList<Salle> sallesFiltrees = FXCollections.observableArrayList(
                (Collection<Salle>) resultats.get("salles"));
        ObservableList<Activite> activitesFiltrees = FXCollections.observableArrayList(
                (Collection<Activite>) resultats.get("activites"));
        ObservableList<Utilisateur> utilisateursFiltrees = FXCollections.observableArrayList(
                (Collection<Utilisateur>) resultats.get("utilisateurs"));

        assertTrue(reservationsFiltrees.isEmpty());
        assertTrue(sallesFiltrees.isEmpty());
        assertTrue(activitesFiltrees.isEmpty());
        assertEquals(1, utilisateursFiltrees.size());
        assertTrue(utilisateursFiltrees.contains(utilisateurNonExistant));
    }

    /**
     * Teste l'application du filtre sans utilisateur spécifié.
     * Le test vérifie que si aucun utilisateur n'est filtré, toutes les réservations,
     * salles et activités sont retournées.
     */
    @Test
    void testFiltreSansUtilisateur() {
        filtre.setEmployeFiltre(null);

        Map<String, ObservableList<?>> resultats = filtre.appliquerFiltres(reservations);

        ObservableList<Reservation> reservationsFiltrees = FXCollections.observableArrayList(
                (Collection<Reservation>) resultats.get("reservations"));
        ObservableList<Salle> sallesFiltrees = FXCollections.observableArrayList(
                (Collection<Salle>) resultats.get("salles"));
        ObservableList<Activite> activitesFiltrees = FXCollections.observableArrayList(
                (Collection<Activite>) resultats.get("activites"));
        ObservableList<Utilisateur> utilisateursFiltrees = FXCollections.observableArrayList(
                (Collection<Utilisateur>) resultats.get("utilisateurs"));

        assertEquals(3, reservationsFiltrees.size());
        assertTrue(reservationsFiltrees.contains(reservation1));
        assertTrue(reservationsFiltrees.contains(reservation2));
        assertTrue(reservationsFiltrees.contains(reservation3));

        assertEquals(3, sallesFiltrees.size());
        assertTrue(sallesFiltrees.contains(reservation1.getSalle()));
        assertTrue(sallesFiltrees.contains(reservation2.getSalle()));
        assertTrue(sallesFiltrees.contains(reservation3.getSalle()));

        assertEquals(3, activitesFiltrees.size());
        assertTrue(activitesFiltrees.contains(reservation1.getActivite()));
        assertTrue(activitesFiltrees.contains(reservation2.getActivite()));
        assertTrue(activitesFiltrees.contains(reservation3.getActivite()));

        assertEquals(2, utilisateursFiltrees.size());
        assertTrue(utilisateursFiltrees.contains(employe1));
        assertTrue(utilisateursFiltrees.contains(employe2));
    }

    /**
     * Teste l'application du filtre lorsque aucun utilisateur n'est défini.
     * Le test vérifie que les résultats contiennent bien toutes les réservations
     * sans restriction sur les utilisateurs.
     */
    @Test
    void testFiltreAvecUtilisateurNonDefini() {
        filtre.setEmployeFiltre(null);

        Map<String, ObservableList<?>> resultats = filtre.appliquerFiltres(reservations);

        ObservableList<Reservation> reservationsFiltrees = FXCollections.observableArrayList(
                (Collection<Reservation>) resultats.get("reservations"));
        ObservableList<Salle> sallesFiltrees = FXCollections.observableArrayList(
                (Collection<Salle>) resultats.get("salles"));
        ObservableList<Activite> activitesFiltrees = FXCollections.observableArrayList(
                (Collection<Activite>) resultats.get("activites"));
        ObservableList<Utilisateur> utilisateursFiltrees = FXCollections.observableArrayList(
                (Collection<Utilisateur>) resultats.get("utilisateurs"));

        assertEquals(3, reservationsFiltrees.size());
        assertTrue(reservationsFiltrees.contains(reservation1));
        assertTrue(reservationsFiltrees.contains(reservation2));
        assertTrue(reservationsFiltrees.contains(reservation3));
    }
    /**
     * Teste la méthode getEmployeFiltre() après avoir défini un utilisateur.
     * Ce test vérifie que l'utilisateur retourné par getEmployeFiltre() correspond
     * à celui qui a été défini avec setEmployeFiltre().
     */
    @Test
    void testGetEmployeFiltre() {
        // Définir un utilisateur à filtrer
        filtre.setEmployeFiltre(employe1);

        // Vérifier que getEmployeFiltre() retourne bien employe1
        assertEquals(employe1, filtre.getEmployeFiltre());
    }

    /**
     * Teste la méthode setEmployeFiltre() avec un utilisateur spécifique.
     * Ce test vérifie que setEmployeFiltre() modifie correctement l'utilisateur
     * de filtrage.
     */
    @Test
    void testSetEmployeFiltre() {
        // Initialiser un utilisateur différent pour vérifier le changement
        filtre.setEmployeFiltre(employe2);

        // Vérifier que l'utilisateur défini est bien employe2
        assertEquals(employe2, filtre.getEmployeFiltre());
    }

    /**
     * Teste la méthode setEmployeFiltre() avec un utilisateur nul.
     * Ce test vérifie que l'utilisateur peut être défini comme nul,
     * et que getEmployeFiltre() retourne null dans ce cas.
     */
    @Test
    void testSetEmployeFiltreNull() {
        // Définir employeFiltre à null
        filtre.setEmployeFiltre(null);

        // Vérifier que l'utilisateur est bien null
        assertNull(filtre.getEmployeFiltre());
    }
}
