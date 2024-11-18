package iut.info2.test.saltistique;

/*
 * TestChiffrage.java                                    21 nov. 2023
 * IUT de Rodez, info1 2022-2023, aucun copyright ni copyleft
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import iut.info2.saltistique.modele.Chiffrage;

class TestChiffrage {

    /**
     * Test de la génération de clé Diffie-Hellman, de la création de clé Vigenère,
     * ainsi que du chiffrement et déchiffrement d'un message.
     */
    @Test
    void testDiffieHellmanToVigenereChiffrement() {
        // Étape 1 : Simuler l'échange de clés Diffie-Hellman
        int puissanceA = Chiffrage.genererPuissance();
        int puissanceB = Chiffrage.genererPuissance();

        int partieA = Chiffrage.exposantModulo(Chiffrage.G, puissanceA, Chiffrage.P);
        int partieB = Chiffrage.exposantModulo(Chiffrage.G, puissanceB, Chiffrage.P);

        Chiffrage.setGab(Chiffrage.exposantModulo(partieB, puissanceA, Chiffrage.P));
        String cleSecrete = Chiffrage.cleDepuisDiffie();

        // Étape 2 : Utiliser la clé Diffie-Hellman pour générer une clé Vigenère
        String cleVigenere = Chiffrage.generationCle();

        // Étape 3 : Chiffrer et déchiffrer un message de test
        String messageOriginal = "Bonjour tout le monde!";
        String messageChiffre = Chiffrage.chiffrement(messageOriginal, cleVigenere);
        String messageDechiffre = Chiffrage.dechiffrement(messageChiffre, cleVigenere);

        // Vérifier que le message déchiffré correspond au message original
        assertEquals(messageOriginal, messageDechiffre, "Le message déchiffré devrait être identique au message original.");
    }

    /**
     * Test de la génération de clés Vigenère pour s'assurer qu'elles respectent
     * la plage de longueur attendue.
     */
    @Test
    void testGenerationCle() {
        for (int i = 0; i < 10000; i++) {
            String cle = Chiffrage.generationCle();
            assertTrue(cle.length() >= 40 && cle.length() <= 60, "La longueur de la clé doit être entre 40 et 60.");
        }
    }

    /**
     * Test du processus de chiffrement et déchiffrement spécifique
     * pour s'assurer qu'ils fonctionnent correctement ensemble.
     */
    @Test
    void testChiffrementDechiffrement() {
        String cle = "SimpleKey";
        String message = "TestMessage";

        // Chiffrer puis déchiffrer le message
        String messageChiffre = Chiffrage.chiffrement(message, cle);
        String messageDechiffre = Chiffrage.dechiffrement(messageChiffre, cle);

        // Vérifier que le message déchiffré correspond au message original
        assertEquals(message, messageDechiffre, "Le message déchiffré devrait correspondre au message original.");
    }

    /**
     * Test de la fonction d'exponentiation modulo utilisée dans l'algorithme
     * Diffie-Hellman pour valider le comportement correct de cette fonction.
     */
    @Test
    void testExposantModulo() {
        assertEquals(1, Chiffrage.exposantModulo(6, 0, 11));
        assertEquals(6, Chiffrage.exposantModulo(6, 1, 11));
        assertEquals(3, Chiffrage.exposantModulo(6, 2, 11));
    }

    /**
     * Test de la génération de puissance pour s'assurer que les valeurs générées
     * se situent dans la plage spécifiée pour l'échange de clés Diffie-Hellman.
     */
    @Test
    void testGenererPuissance() {
        for (int i = 0; i < 10000; i++) {
            int puissance = Chiffrage.genererPuissance();
            assertTrue(puissance >= 5000 && puissance <= 9999, "La puissance doit être entre 5000 et 9999.");
        }
    }
}
