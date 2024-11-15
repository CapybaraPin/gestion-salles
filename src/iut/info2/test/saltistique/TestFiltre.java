/*
 * FiltreTest.java              15/11/2024
 * IUT de Rodez, pas de copyrights
 */

package iut.info2.test.saltistique;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TestFiltre {

    /**
     * Test les différents cas possibles pour le filtrage des données dans l'application Saltistique
     * Les différents filtres possibles sont :
     * Filtrer par Salles
     * Filtrer par Employes
     * Filtrer par Activites
     * Filtrer par Reservations
     *
     * @author Jules VIALAS
     */

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        System.out.println("====== TEST DE LA CLASSE Filtre ======");
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
        System.out.println("======= TEST DE Filtre TERMINE =======");
    }

    @Test
    void getEmployeFiltre() {

    }

    @Test
    void setEmployeFiltre() {
    }

    @Test
    void appliquerFiltres() {
    }
}