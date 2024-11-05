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
     * Test for Diffie-Hellman key generation, Vigenère key creation, and encryption/decryption.
     */
    @Test
    void testDiffieHellmanToVigenereChiffrement() {
        // Step 1: Simulate Diffie-Hellman key exchange
        int puissanceA = Chiffrage.genererPuissance();
        int puissanceB = Chiffrage.genererPuissance();

        int partieA = Chiffrage.exposantModulo(Chiffrage.G, puissanceA, Chiffrage.P);
        int partieB = Chiffrage.exposantModulo(Chiffrage.G, puissanceB, Chiffrage.P);

        Chiffrage.setGab(Chiffrage.exposantModulo(partieB, puissanceA, Chiffrage.P));
        String cleSecrete = Chiffrage.cleDepuisDiffie();

        // Step 2: Use the Diffie-Hellman key to generate a Vigenère key
        String cleVigenere = Chiffrage.generationCle();

        // Step 3: Encrypt and decrypt a test message
        String messageOriginal = "Bonjour tout le monde!";
        String messageChiffre = Chiffrage.chiffrement(messageOriginal, cleVigenere);
        String messageDechiffre = Chiffrage.dechiffrement(messageChiffre, cleVigenere);

        // Assert the decrypted message matches the original
        assertEquals(messageOriginal, messageDechiffre, "Le message déchiffré devrait être identique au message original.");
    }

    /**
     * Test for consistent Vigenère key generation within expected length range.
     */
    @Test
    void testGenerationCle() {
        for (int i = 0; i < 10000; i++) {
            String cle = Chiffrage.generationCle();
            assertTrue(cle.length() >= 40 && cle.length() <= 60, "La longueur de la clé doit être entre 40 et 60.");
        }
    }

    /**
     * Test for specific encryption and decryption process.
     */
    @Test
    void testChiffrementDechiffrement() {
        String cle = "SimpleKey";
        String message = "TestMessage";

        // Encrypt and then decrypt the message
        String messageChiffre = Chiffrage.chiffrement(message, cle);
        String messageDechiffre = Chiffrage.dechiffrement(messageChiffre, cle);

        // Validate that decrypting the encrypted message returns the original
        assertEquals(message, messageDechiffre, "Le message déchiffré devrait correspondre au message original.");
    }

    /**
     * Test for Diffie-Hellman exponentiation function.
     */
    @Test
    void testExposantModulo() {
        assertEquals(1, Chiffrage.exposantModulo(6, 0, 11));
        assertEquals(6, Chiffrage.exposantModulo(6, 1, 11));
        assertEquals(3, Chiffrage.exposantModulo(6, 2, 11));
    }

    /**
     * Test for power generation within the specified Diffie-Hellman range.
     */
    @Test
    void testGenererPuissance() {
        for (int i = 0; i < 10000; i++) {
            int puissance = Chiffrage.genererPuissance();
            assertTrue(puissance >= 5000 && puissance <= 9999, "La puissance doit être entre 5000 et 9999.");
        }
    }
}

