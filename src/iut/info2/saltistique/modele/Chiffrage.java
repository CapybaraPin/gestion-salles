package iut.info2.saltistique.modele;

import java.io.*;
import java.security.SecureRandom;

/**
 * TODO : utilisation de BigInteger pour une meilleur protection
 * @author Dorian ADAMS, Néo BECOGNE
 */
public class Chiffrage {

    private final int G = 4148;
    private final int P = 8291;

    /** Clé commune calculée */
    private int clePartager;

    /** Clé privée calculée */
    private int clePrivee;

    /** Puissance minimal et maximal */
    private final int PUISSANCE_MINI = 5000000;
    private final int PUISSANCE_MAXI = 9999999;

    private String cheminFichier;
    private String cle_vigenere;

    private static String CUSTOM_ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGH"
            + "IJKLMNOPQRSTUVWXYZ&~\"#'({[-|`_\\^@)]}/*.!?,;:<>1234567890$% +=\n";

    public Chiffrage(String cheminFichier) {
        this.cheminFichier = cheminFichier;
        this.clePrivee = modExponentiation(G, generateExponent(), P);
    }

    /** Retourne le message crypter */
    public String crypter() {
        try {
            String cheminFichierCrypte = cheminFichier.replace(".csv", "-c.csv");
            StringBuilder contenu = new StringBuilder();
            try (BufferedReader lecteur = new BufferedReader(new FileReader(cheminFichier))) {
                String ligne;
                while ((ligne = lecteur.readLine()) != null) {
                    contenu.append(ligne).append("\n"); // Ajoute un saut de ligne après chaque ligne lue
                }
            }
            String contenuFichier = contenu.toString();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichierCrypte))) {
                System.out.println(contenuFichier);
                StringBuilder ligneCrypter = new StringBuilder();
                int k = 0;
                for (int i = 0; i < contenuFichier.length(); i++) {
                    int valeur_m = CUSTOM_ALPHABET.indexOf(contenuFichier.charAt(i));
                    int valeur_c = CUSTOM_ALPHABET.indexOf(cle_vigenere.charAt(k));
                    if (valeur_m == -1) { // Caractère non trouvé dans l'alphabet
                        ligneCrypter.append(contenuFichier.charAt(i)); // Ajout direct du caractère non crypté
                    } else {
                        if (k == cle_vigenere.length() - 1) {
                            k = 0;
                        } else {
                            k++;
                        }
                        int valeur_total = valeur_m + valeur_c;
                        ligneCrypter.append(CUSTOM_ALPHABET.charAt(valeur_total % CUSTOM_ALPHABET.length()));
                    }
                }
                System.out.println(ligneCrypter.toString());
                writer.write(ligneCrypter.toString());
            }


            // Utilisation de try-with-resources pour assurer la fermeture du BufferedWriter
            /*try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichierCrypte))) {
                for (String line : lignes) {
                    System.out.println(line);
                    StringBuilder ligneCrypter = new StringBuilder();
                    int k = 0;
                    for (int i = 0; i < line.length(); i++) {
                        int valeur_m = CUSTOM_ALPHABET.indexOf(line.charAt(i));
                        int valeur_c = CUSTOM_ALPHABET.indexOf(cle_vigenere.charAt(k));
                        if (valeur_m == -1) { // Caractère non trouvé dans l'alphabet
                            ligneCrypter.append(line.charAt(i)); // Ajout direct du caractère non crypté
                        } else {
                            if (k == cle_vigenere.length() - 1) {
                                k = 0;
                            } else {
                                k++;
                            }
                            int valeur_total = valeur_m + valeur_c;
                            ligneCrypter.append(CUSTOM_ALPHABET.charAt(valeur_total % CUSTOM_ALPHABET.length()));
                        }
                    }
                    System.out.println(ligneCrypter.toString());
                    writer.write(ligneCrypter.toString());
                    writer.newLine();
                }
            }*/

            return cheminFichierCrypte;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Retourne le message décrypter */
    public String decrypter() {
        try {
            String cheminFichierDecrypte = cheminFichier.replace("-c.csv", "-dc.csv");
            StringBuilder contenu = new StringBuilder();
            try (BufferedReader lecteur = new BufferedReader(new FileReader(cheminFichier))) {
                String ligne;
                while ((ligne = lecteur.readLine()) != null) {
                    contenu.append(ligne).append("\n"); // Ajoute un saut de ligne après chaque ligne lue
                }
            }
            String contenuFichier = contenu.toString();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichierDecrypte))) {
                StringBuilder ligneDecrypter = new StringBuilder();
                int k = 0;
                for (int i = 0; i < contenuFichier.length()-1; i++) {
                    int valeur_m = CUSTOM_ALPHABET.indexOf(contenuFichier.charAt(i));
                    int valeur_c = CUSTOM_ALPHABET.indexOf(cle_vigenere.charAt(k));

                    // Vérifie si le caractère est dans l'alphabet
                    if (valeur_m == -1) {
                        ligneDecrypter.append(contenuFichier.charAt(i)); // Ajoute le caractère non crypté
                    } else {
                        if (k == cle_vigenere.length() - 1) {
                            k = 0;
                        } else {
                            k++;
                        }

                        int valeur_total = (valeur_m - valeur_c) % CUSTOM_ALPHABET.length();
                        if (valeur_total < 0) {
                            valeur_total += CUSTOM_ALPHABET.length(); // Corrige pour éviter les valeurs négatives
                        }

                        ligneDecrypter.append(CUSTOM_ALPHABET.charAt(valeur_total));
                    }
                }

                writer.write(ligneDecrypter.toString());
            }

            // Utilisation de try-with-resources pour assurer la fermeture du BufferedWriter
            /*try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichierDecrypte))) {
                for (String line : lignes) {
                    StringBuilder ligneDecrypter = new StringBuilder();
                    int k = 0;
                    for (int i = 0; i < line.length(); i++) {
                        int valeur_m = CUSTOM_ALPHABET.indexOf(line.charAt(i));
                        int valeur_c = CUSTOM_ALPHABET.indexOf(cle_vigenere.charAt(k));

                        // Vérifie si le caractère est dans l'alphabet
                        if (valeur_m == -1) {
                            ligneDecrypter.append(line.charAt(i)); // Ajoute le caractère non crypté
                        } else {
                            if (k == cle_vigenere.length() - 1) {
                                k = 0;
                            } else {
                                k++;
                            }

                            int valeur_total = (valeur_m - valeur_c) % CUSTOM_ALPHABET.length();
                            if (valeur_total < 0) {
                                valeur_total += CUSTOM_ALPHABET.length(); // Corrige pour éviter les valeurs négatives
                            }

                            ligneDecrypter.append(CUSTOM_ALPHABET.charAt(valeur_total));
                        }
                    }

                    writer.write(ligneDecrypter.toString());
                    writer.newLine();
                }
            }*/

            return cheminFichierDecrypte;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setCle_vigenere(String cleVigenere) {
        this.cle_vigenere = cleVigenere;
    }

    /**
     * Génère un exposant aléatoire dans la plage définie.
     * @return Un exposant aléatoire.
     */
    private int generateExponent() {
        return new SecureRandom().nextInt(PUISSANCE_MAXI - PUISSANCE_MINI + 1) + PUISSANCE_MINI;
    }

    /**
     * Calcule (a^exp) % modulo de manière efficace en utilisant l'exponentiation modulaire.
     * Utilisé pour générer des clés partagées en Diffie-Hellman.
     * @param a La base.
     * @param exp L'exposant.
     * @param modulo Le modulo.
     * @return Le résultat de (a^exp) % modulo.
     */
    private int modExponentiation(int a, int exp, int modulo) {
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

    public int getClePrivee() {
        return clePrivee;
    }

    public int getClePartager() {
        return clePartager;
    }

    public void calculeClePartager(int cle) {
        this.clePartager = modExponentiation(G, clePrivee * cle, P);

    }

    /**
     * Génère une clé secrète basée sur Diffie-Hellman en utilisant gab.
     * @return La clé Diffie-Hellman générée.
     */
    public void generateKeyFromDiffie() {
        StringBuilder secretKey = new StringBuilder();
        String gabString = String.valueOf(clePartager);
        System.out.println(gabString);

        for (int i = 0; i < gabString.length(); i++) {
            int index = Character.getNumericValue(gabString.charAt(i)) % CUSTOM_ALPHABET.length();
            secretKey.append(CUSTOM_ALPHABET.charAt(index));
        }
        this.cle_vigenere = secretKey.toString();
    }

}
