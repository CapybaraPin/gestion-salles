package iut.info2.saltistique.modele;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * TODO : utilisation de BigInteger pour une meilleure protection
 * @author Dorian ADAMS, Néo BECOGNE
 */
public class Chiffrage {

    private final BigInteger G = new BigInteger("293420472449539282455733124980401731387348120353499102030073038693451021459432155986836198729889379129133276675643359344407061495518484882889255574219109091144354842690051056706764720316243236342474368038219273019275745144167424235176331112507920866365947957948345947545404084592870247787654918296581073234611");
    private final BigInteger P = new BigInteger("162259276829213363391578010288127");

    /** Clé commune calculée */
    private BigInteger clePartager;

    /** Clé privée calculée */
    private BigInteger clePrivee;

    /** Puissance minimale et maximale */
    private final BigInteger PUISSANCE_MINI = new BigInteger("5000000");
    private final BigInteger PUISSANCE_MAXI = new BigInteger("9999999");

    private String cheminFichier;
    private String cle_vigenere;

    private static final String CUSTOM_ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGH"
            + "IJKLMNOPQRSTUVWXYZ&~\"#'({[-|`_\\^@)]}/*.!?,;:<>1234567890$% +=\n";

    public Chiffrage(String cheminFichier) {
        this.cheminFichier = cheminFichier;
        BigInteger exposant = generateExponent();
        this.clePrivee = modExponentiation(G, exposant, P);
        //System.out.println(clePrivee);
    }

    public Chiffrage() {
        BigInteger exposant = generateExponent();
        this.clePrivee = modExponentiation(G, exposant, P);
    }

    /** Retourne le message crypté */
    public String crypter() {
        try {
            String cheminFichierCrypte = cheminFichier.replace(".csv", "-c.csv");
            StringBuilder contenu = new StringBuilder();
            try (BufferedReader lecteur = new BufferedReader(new FileReader(cheminFichier))) {
                String ligne;
                while ((ligne = lecteur.readLine()) != null) {
                    contenu.append(ligne).append("\n");
                }
            }
            String contenuFichier = contenu.toString();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichierCrypte))) {
                StringBuilder ligneCrypter = new StringBuilder();
                int k = 0;
                for (int i = 0; i < contenuFichier.length(); i++) {
                    int valeur_m = CUSTOM_ALPHABET.indexOf(contenuFichier.charAt(i));
                    int valeur_c = CUSTOM_ALPHABET.indexOf(cle_vigenere.charAt(k));
                    if (valeur_m == -1) {
                        ligneCrypter.append(contenuFichier.charAt(i));
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
                writer.write(ligneCrypter.toString());
            }

            return cheminFichierCrypte;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Retourne le message décrypté */
    public String decrypter() {
        try {
            String cheminFichierDecrypte = cheminFichier.replace("-c.csv", "-dc.csv");
            StringBuilder contenu = new StringBuilder();
            try (BufferedReader lecteur = new BufferedReader(new FileReader(cheminFichier))) {
                String ligne;
                while ((ligne = lecteur.readLine()) != null) {
                    contenu.append(ligne).append("\n");
                }
            }
            String contenuFichier = contenu.toString();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichierDecrypte))) {
                StringBuilder ligneDecrypter = new StringBuilder();
                int k = 0;
                for (int i = 0; i < contenuFichier.length() - 1; i++) {
                    int valeur_m = CUSTOM_ALPHABET.indexOf(contenuFichier.charAt(i));
                    int valeur_c = CUSTOM_ALPHABET.indexOf(cle_vigenere.charAt(k));

                    if (valeur_m == -1) {
                        ligneDecrypter.append(contenuFichier.charAt(i));
                    } else {
                        if (k == cle_vigenere.length() - 1) {
                            k = 0;
                        } else {
                            k++;
                        }

                        int valeur_total = (valeur_m - valeur_c) % CUSTOM_ALPHABET.length();
                        if (valeur_total < 0) {
                            valeur_total += CUSTOM_ALPHABET.length();
                        }

                        ligneDecrypter.append(CUSTOM_ALPHABET.charAt(valeur_total));
                    }
                }
                writer.write(ligneDecrypter.toString());
            }

            return cheminFichierDecrypte;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setCle_vigenere(String cleVigenere) {
        this.cle_vigenere = cleVigenere;
    }

    private BigInteger generateExponent() {
        return new BigInteger(PUISSANCE_MAXI.bitLength(), new SecureRandom())
                .mod(PUISSANCE_MAXI.subtract(PUISSANCE_MINI)).add(PUISSANCE_MINI);
    }

    private BigInteger modExponentiation(BigInteger base, BigInteger exponent, BigInteger modulo) {
        if (modulo.equals(BigInteger.ONE)) {
            return BigInteger.ZERO; // Dans ce cas, tout nombre modulo 1 est 0
        }
        return base.modPow(exponent, modulo);
    }


    public BigInteger getClePrivee() {
        return clePrivee;
    }

    public BigInteger getClePartager() {
        return clePartager;
    }

    public void calculeClePartager(BigInteger cle) {
        this.clePartager = modExponentiation(G, clePrivee.multiply(cle), P);
    }

    public void generateKeyFromDiffie() {
        StringBuilder secretKey = new StringBuilder();
        String gabString = clePartager.toString();
        for (int i = 0; i < gabString.length(); i++) {
            int index = Character.getNumericValue(gabString.charAt(i)) % CUSTOM_ALPHABET.length();
            secretKey.append(CUSTOM_ALPHABET.charAt(index));
        }
        this.cle_vigenere = secretKey.toString();
    }

    public void setCheminFichier(String cheminFichier) {
        this.cheminFichier = cheminFichier;
    }
}
