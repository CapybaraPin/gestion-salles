/*
 * Chiffrage.java                   18/11/2024
 * IUT de RODEZ, tous les droits sont réservés
 */

package iut.info2.saltistique.modele;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Classe représentant un système de chiffrement utilisant l'algorithme de Vigenère et
 * la génération de clés via Diffie-Hellman.
 * Cette classe permet de chiffrer et déchiffrer des fichiers texte en utilisant un alphabet personnalisé.
 *
 * La clé de Vigenère est générée à partir de la clé partagée issue de l'algorithme de Diffie-Hellman.
 * Les opérations de cryptage et de décryptage se basent sur des index dans un alphabet défini par
 * {@code CUSTOM_ALPHABET}.
 *
 * <p>
 * Utilisation typique :
 * <ul>
 *   <li>Créer une instance de la classe avec un chemin de fichier a chiffrer ou déchiffrer</li>
 *   <li>Envoyer la clé privée calculée</li>
 *   <li>Calculer la clé partagée et générer la clé de Vigenère à partir de Diffie-Hellman</li>
 *   <li>Crypter ou décrypter le fichier</li>
 * </ul>
 * </p>
 *
 * @author Dorian ADAMS, Néo BECOGNE
 */
public class Chiffrage {

    /** Base publique pour le calcul de Diffie-Hellman, entier générateur. */
    private final BigInteger G = new BigInteger("293420472449539282455733124980401731387348120353499102030073038693451021459432155986836198729889379129133276675643359344407061495518484882889255574219109091144354842690051056706764720316243236342474368038219273019275745144167424235176331112507920866365947957948345947545404084592870247787654918296581073234611");

    /** Modulo public pour le calcul de Diffie-Hellman, entier premier. */
    private final BigInteger P = new BigInteger("162259276829213363391578010288127");

    /** Limite minimal pour la génération de l'exposant Diffie-Hellman. */
    private final BigInteger EXPOSANT_MINIMUM = new BigInteger("5000000000000000");

    /** Limite maximale pour la génération de l'exposant Diffie-Hellman. */
    private final BigInteger EXPOSANT_MAXIMUM = new BigInteger("9999999999999999");

    /** Alphabet personnalisé utilisé pour le chiffrement et le déchiffrement de Vigenère. */
    private static final String ALPHABET_PERSONNALISE = "abcdefghijklmnopqrstuvwxyzABCDEFGH"
            + "IJKLMNOPQRSTUVWXYZ&~\"#'({[-|`_\\^@)]}/*.!?,;:<>1234567890$% +=\n";

    /** Chemin du fichier à crypter ou décrypter. */
    private String cheminFichier;

    /** Clé secrète générée pour le chiffrement Vigenère. */
    private String cleVigenere;

    /** Clé partagée calculée via Diffie-Hellman. */
    private BigInteger clePartager;

    /** Clé publique générée pour le calcul de Diffie-Hellman. */
    private BigInteger clePublique;

    /**
     * Constructeur principal qui initialise le chemin du fichier et génère une clé publique Diffie-Hellman.
     *
     * @param cheminFichier chemin du fichier à crypter ou décrypter.
     */
    public Chiffrage(String cheminFichier) {
        this.cheminFichier = cheminFichier;
        BigInteger exposant = genererExposant();
        this.clePublique = exponentiationModulaire(G, exposant, P);
    }

    /**
     * Constructeur sans chemin de fichier.
     * Génère une clé publique Diffie-Hellman.
     */
    public Chiffrage() {
        BigInteger exposant = genererExposant();
        this.clePublique = exponentiationModulaire(G, exposant, P);
    }

    /**
     * Chiffre le fichier spécifié en utilisant le chiffrement de Vigenère.
     *
     * <p>
     * Le fichier chiffré sera enregistré avec le même nom que le fichier original,
     * suivi du suffixe "-c.csv".
     * </p>
     *
     * @return le chemin du fichier chiffré.
     * @throws RuntimeException si une erreur d'entrée/sortie survient.
     */
    public String chiffrer() throws IOException {
        if (cleVigenere == null) {
            throw new NullPointerException("La clé de Vigenère doit d'abord être calculée.");
        }
        try {
            // Lecture du fichier à chiffrer
            String cheminFichierChiffrer = cheminFichier.replace(".csv", "-c.csv");
            StringBuilder contenu = new StringBuilder();
            try (BufferedReader lecteur = new BufferedReader(new FileReader(cheminFichier))) {
                String ligne;
                while ((ligne = lecteur.readLine()) != null) {
                    contenu.append(ligne).append("\n");
                }
            }
            String contenuFichier = contenu.toString();

            // Chiffrage du fichier
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichierChiffrer))) {
                StringBuilder textChiffer = new StringBuilder();
                int k = 0;
                for (int i = 0; i < contenuFichier.length(); i++) {
                    int indexCaractereTexte = ALPHABET_PERSONNALISE.indexOf(contenuFichier.charAt(i));
                    int indexCaractereCle = ALPHABET_PERSONNALISE.indexOf(cleVigenere.charAt(k));
                    if (indexCaractereTexte == -1) {
                        textChiffer.append(contenuFichier.charAt(i));
                    } else {
                        if (k == cleVigenere.length() - 1) {
                            k = 0;
                        } else {
                            k++;
                        }
                        int valeurTotal = indexCaractereTexte + indexCaractereCle;
                        textChiffer.append(ALPHABET_PERSONNALISE.charAt(valeurTotal % ALPHABET_PERSONNALISE.length()));
                    }
                }
                writer.write(textChiffer.toString());
            }

            return cheminFichierChiffrer;
        } catch (IOException e) {
            throw new IOException("Erreur lors du chiffrement : " + e.getMessage(), e);
        }
    }

    /**
     * Déchiffre le fichier spécifié en utilisant le chiffrement de Vigenère.
     *
     * <p>
     * Le fichier déchiffré sera enregistré avec le même nom que le fichier chiffré,
     * suivi du suffixe "-dc.csv".
     * </p>
     *
     * @return le chemin du fichier déchiffré.
     * @throws RuntimeException si une erreur d'entrée/sortie survient.
     */
    public String dechiffrer() throws IOException {
        if (cleVigenere == null) {
            throw new NullPointerException("La clé de Vigenère doit d'abord être calculée.");
        }
        try {
            //Lecture du fichier à déchiffrer
            String cheminFichierDechiffrer = cheminFichier.replace("-c.csv", "-dc.csv");
            StringBuilder contenu = new StringBuilder();
            try (BufferedReader lecteur = new BufferedReader(new FileReader(cheminFichier))) {
                String ligne;
                while ((ligne = lecteur.readLine()) != null) {
                    contenu.append(ligne).append("\n");
                }
            }
            String contenuFichier = contenu.toString();

            //Déchiffrage du fichier
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichierDechiffrer))) {
                StringBuilder textDechiffrer = new StringBuilder();
                int k = 0;
                for (int i = 0; i < contenuFichier.length() - 1; i++) {
                    int indexCaractereTexte = ALPHABET_PERSONNALISE.indexOf(contenuFichier.charAt(i));
                    int indexCaractereCle = ALPHABET_PERSONNALISE.indexOf(cleVigenere.charAt(k));

                    if (indexCaractereTexte == -1) {
                        textDechiffrer.append(contenuFichier.charAt(i));
                    } else {
                        if (k == cleVigenere.length() - 1) {
                            k = 0;
                        } else {
                            k++;
                        }

                        int valeurTotal = (indexCaractereTexte - indexCaractereCle) % ALPHABET_PERSONNALISE.length();
                        if (valeurTotal < 0) {
                            valeurTotal += ALPHABET_PERSONNALISE.length();
                        }

                        textDechiffrer.append(ALPHABET_PERSONNALISE.charAt(valeurTotal));
                    }
                }
                writer.write(textDechiffrer.toString());
            }

            return cheminFichierDechiffrer;
        } catch (IOException e) {
            throw new IOException("Erreur lors du chiffrement : " + e.getMessage(), e);
        }
    }

    /**
     * Génère une puissance aléatoire utilisée comme exposant pour Diffie-Hellman.
     *
     * @return une puissance aléatoire comprise entre {@link #EXPOSANT_MINIMUM} et {@link #EXPOSANT_MAXIMUM}.
     */
    private BigInteger genererExposant() {
        return new BigInteger(EXPOSANT_MAXIMUM.bitLength(), new SecureRandom())
                .mod(EXPOSANT_MAXIMUM.subtract(EXPOSANT_MINIMUM)).add(EXPOSANT_MINIMUM);
    }

    /**
     * Effectue une exponentiation modulaire.
     *
     * @param base la base à élever.
     * @param exponent l'exposant.
     * @param modulo le modulo.
     * @return le résultat de (base^exponent) mod modulo.
     */
    private BigInteger exponentiationModulaire(BigInteger base, BigInteger exponent, BigInteger modulo) {
        if (modulo.equals(BigInteger.ONE)) {
            return BigInteger.ZERO; // Dans ce cas, tout nombre modulo 1 est 0
        }
        return base.modPow(exponent, modulo);
    }


    /**
     * Retourne la clé privée de Diffie-Hellman.
     *
     * @return la clé privée.
     */
    public BigInteger getClePublique() {
        return clePublique;
    }

    /**
     * Calcule la clé partagée à partir de la clé publique distante.
     *
     * @param cle clé publique de l'autre participant au protocole Diffie-Hellman.
     */
    public void calculeClePartager(BigInteger cle) {
        if(cle != null) {
            this.clePartager = exponentiationModulaire(G, clePublique.multiply(cle), P);
        } else {
            throw new IllegalArgumentException("Le gros entier ne peut être nul.");
        }
    }

    /**
     * Génère une clé de chiffrement Vigenère à partir de la clé partagée.
     */
    public void genererCleVigenere() {
        if(clePartager != null) {
            StringBuilder secretKey = new StringBuilder();
            String gabString = clePartager.toString();
            for (int i = 0; i < gabString.length(); i++) {
                int index = Character.getNumericValue(gabString.charAt(i)) % ALPHABET_PERSONNALISE.length();
                secretKey.append(ALPHABET_PERSONNALISE.charAt(index));
            }
            this.cleVigenere = secretKey.toString();
        } else {
            throw new NullPointerException("La clé partagée doit d'abord être calculée.");
        }
    }

    /**
     * Modifie le chemin du fichier à crypter ou décrypter.
     *
     * @param cheminFichier nouveau chemin du fichier.
     */
    public void setCheminFichier(String cheminFichier) {
        this.cheminFichier = cheminFichier;
    }

    /**
     * Retourne la cle partagée de Diffie-Hellman
     *
     * @return la clé partagée
     */
    public BigInteger getClePartager() {
        return clePartager;
    }
}
