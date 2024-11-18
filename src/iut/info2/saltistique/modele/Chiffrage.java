package iut.info2.saltistique.modele;

/*
 * Chiffrage.java 								18/10/2023
 * IUT de Rodez, pas de copyrights ni copyleft
 */

import java.util.HashMap;

/**
 * La classe  Chiffrage fournit des méthodes pour chiffrer et déchiffrer des données
 * en utilisant deux algorithmes de cryptographie,le chiffrement de Vigenère
 * et l'échange de clés Diffie-Hellman.
 *
 * <p>Cette classe prend en charge le chiffrement et le déchiffrement de textes,
 * en appliquant des algorithmes de cryptographie.
 * Elle permet également de générer des clés sécurisées pour assurer la confidentialité des échanges de données.</p>
 *
 * <p>Les principales fonctionnalités incluent :</p>
 * <ul>
 *   <li>Chiffrement et déchiffrement de chaînes de caractères</li>
 *   <li>Échange de clés sécurisé avec l'algorithme Diffie-Hellman</li>
 * </ul>
 *
 * @author Jules Vialas, Néo Bécogné, Dorian Adams, Hugo Robles, Tom Gutierrez
 */
public class Chiffrage {


    /**Longueur minimale et maximale d'une clé de chiffrement*/
    private static final int LONGUEUR_CLE_MINIMUM = 40 ;
    private static final int LONGUEUR_CLE_MAXIMUM = 60 ;

    /**Alphabet personnalisé pouvant etre chiffré*/
    public static String CUSTOM_ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGH"
            + "IJKLMNOPQRSTUVWXYZ&~\"#'({[-|`_\\^@)]}/*.!?,;:<>1234567890$% +=\n";

    /**Mapping des caractères de l'alphabet vers des entiers*/
    public static final HashMap<Character, Integer> ALPHABET_TO_INT
            = new HashMap<>();

    /**Mapping des entiers vers des caractères de l'alphabet*/
    public static final HashMap<Integer, Character> INT_TO_ALPHABET
            = new HashMap<>();


    /**Paramètres pour l'algorithme de Diffie-Hellman*/
    public static final int P = 8291;
    public static final int G = 4148;
    private static final int PUISSANCE_MINI = 5000 ;
    private static final int PUISSANCE_MAXI = 9999 ;

    /**Valeur partagée dans l'algorithme de Diffie-Hellman*/
    private static int gab = 0;

    /**Initialisation des mappings entre caractères et entiers*/
    static {

        for (int i = 0; i < CUSTOM_ALPHABET.length(); i++) {
            char c = CUSTOM_ALPHABET.charAt(i);
            ALPHABET_TO_INT.put(c, i);
            INT_TO_ALPHABET.put(i, c);
        }
    }
    /**Nombre de lettres dans l'alphabet personnalisé*/
    final static int NOMBRE_LETTRE_ALPHABET = CUSTOM_ALPHABET.length();



    /**
     * Méthode de génération de clé
     * @return une cle de longueur comprise entre
     * LONGUEUR_CLE_MINIMUM et LONGUEUR_CLE_MAXIMUM
     * et comprenant uniquement des caractères de INT_TO_ALPHABET
     */
    public static String generationCle() {
        int longueurCle = (int)(Math.random()*
                (LONGUEUR_CLE_MAXIMUM + 1 - LONGUEUR_CLE_MINIMUM)
                + LONGUEUR_CLE_MINIMUM);
        StringBuilder cle = new StringBuilder();
        for (int i = 0 ; i < longueurCle ; i++) {
            char ajout = INT_TO_ALPHABET.get(
                    (int)(Math.random()*NOMBRE_LETTRE_ALPHABET));

            cle.append(ajout);
        }
        return cle.toString();
    }


    /**
     * Chiffre un message donné en utilisant une clé fournie.
     *
     * @param message Le message à chiffrer.
     * @param cle     La clé de chiffrement.
     * @return Le message chiffré.
     */
    public static String chiffrement(String message, String cle) {
        StringBuilder aCrypter = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char currentChar = message.charAt(i);
            Integer messageI = ALPHABET_TO_INT.get(currentChar);

            if (messageI == null) {
                // Ignorer les caractères qui ne sont pas dans l'alphabet
                aCrypter.append(currentChar);
                continue; // Passe à la prochaine itération
            }

            // Valeur du caractère de la clé
            int cleI = ALPHABET_TO_INT.get(cle.charAt(i % cle.length()));

            char crypter = INT_TO_ALPHABET.get((messageI + cleI) % NOMBRE_LETTRE_ALPHABET);
            aCrypter.append(crypter);
        }
        return aCrypter.toString();
    }


    /**
     * Déchiffre un message donné en utilisant une clé fournie.
     *
     * @param message Le message à déchiffrer.
     * @param cle     La clé de déchiffrement.
     * @return Le message déchiffré.
     */
    public static String dechiffrement(String message, String cle) {
        StringBuilder aCrypter = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char currentChar = message.charAt(i);
            Integer messageI = ALPHABET_TO_INT.get(currentChar);

            if (messageI == null) {
                // Ignorer les caractères qui ne sont pas dans l'alphabet
                aCrypter.append(currentChar);
                continue; // Passe à la prochaine itération
            }

            // Valeur du caractère de la clé
            int cleI = ALPHABET_TO_INT.get(cle.charAt(i % cle.length()));

            int positionReelle = (messageI - cleI) % NOMBRE_LETTRE_ALPHABET;
            positionReelle = positionReelle < 0 ? positionReelle + NOMBRE_LETTRE_ALPHABET : positionReelle;
            char crypter = INT_TO_ALPHABET.get(positionReelle);
            aCrypter.append(crypter);
        }
        return aCrypter.toString();
    }


    /**
     * Calcule le résultat de (a^exp) % modulo.
     *
     * @param a      La base.
     * @param exp    L'exposant.
     * @param modulo Le modulo.
     * @return Le résultat de (a^exp) % modulo.
     */
    public static int exposantModulo(int a, int exp, int modulo) {
        int result = 1;
        a = a % modulo;

        while (exp > 0) {
            if (exp % 2 == 1) {
                result = (result * a) % modulo;
            }
            exp = exp / 2;
            a = (a * a) % modulo;
        }

        return result;
    }

    /**
     * @return Une puissance aléatoire  dans la plage spécifiée
     * [PUISSANCE_MINI, PUISSANCE_MAXI].
     */
    public static int genererPuissance() {
        return (int)(Math.random()*
                (PUISSANCE_MAXI + 1 - PUISSANCE_MINI)+PUISSANCE_MINI);

    }

    /**
     * Génère une clé secrète basée sur l'algorithme
     * d'échange de clés Diffie-Hellman.
     *
     * @return La clé secrète générée.
     */
    public static String cleDepuisDiffie() {
        String gabString = "" + gab;
        int actuelle ;
        StringBuilder cle = new StringBuilder();
        for (int  i  = 0 ; i < gabString.length() ; i++ ) {
            for (int j = 0 ; j < gabString.length() ; j++) {
                actuelle = Integer.parseInt("" + gabString.charAt(i)
                        + gabString.charAt(j));
                actuelle = actuelle%NOMBRE_LETTRE_ALPHABET;
                cle.append(INT_TO_ALPHABET.get(actuelle));
            }
        }
        return cle.toString();
    }

    /**
     * Définit la valeur de la clé partagée (gab)
     * utilisée dans l'échange de clés Diffie-Hellman.
     *
     * @param nouveau La nouvelle valeur de la clé partagée.
     */
    public static void setGab(int nouveau) {
        gab = nouveau;
    }

    /**
     * Obtient la valeur de la clé partagée (gab)
     * utilisée dans l'échange de clés Diffie-Hellman.
     *
     * @return La valeur de la clé partagée.
     */
    public static int getGab() {
        return gab;
    }
}
