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
    private final BigInteger G = new BigInteger("43");

    /** Modulo public pour le calcul de Diffie-Hellman, entier premier. */
    private final BigInteger P = new BigInteger("27057181654137254867746283675268503801533112402711400619423975836717891477887419452579007515407107123751154604926619703236915966879142662310030359869325517827347770143352644079648091599154051855898441334648289811872951093197765275656709374685175387690147596009467314653079993138381556881685926676054564328977382738775701628006076004991834230449286333791868911835560143728719315932752389722258899053865173290869485796256656347807840959792904566617052960880677168930065771958327163216083097862048053464721014791115411212209325601837732230066421384139444625453340760096453351283393101656879623230905119916854857437704469");

    /** Limite minimal pour la génération de l'exposant Diffie-Hellman. */
    private final BigInteger EXPOSANT_MINIMUM = new BigInteger("16158503035655503650357438344334975980222051334857742016065172713762327569433945446598600705761456731844358980460949009747059779575245460547544076193224141560315438683650498045875098875194826053398028819192033784138396109321309878080919047169238085235290822926018152521443787945770532904303776199561965192760957166694834171210342487393282284747428088017663161029038902829665513096354230157075129296432088558362971801859230928678799175576150822952201848806616643615613562842355410104862578550863465661734839271290328348967522998634176499319107762583194718667771801067716614802322659239302476074096777926805529798115328");

    /** Limite maximale pour la génération de l'exposant Diffie-Hellman. */
    private final BigInteger EXPOSANT_MAXIMUM = new BigInteger("32317006071311007300714876688669951960444102669715484032130345427524655138867890893197201411522913463688717960921898019494119559150490921095088152386448283120630877367300996091750197750389652106796057638384067568276792218642619756161838094338476170470581645852036305042887575891541065808607552399123930385521914333389668342420684974786564569494856176035326322058077805659331026192708460314150258592864177116725943603718461857357598351152301645904403697613233287231227125684710820209725157101726931323469678542580656697935045997268352998638215525166389437335543602135433229604645318478604952148193555853611059596230655");

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

    /** Exposant générer pour le calcul de Diffie-Hellman */
    private BigInteger exposantChoisit;

    /**
     * Constructeur principal qui initialise le chemin du fichier et génère une clé publique Diffie-Hellman.
     *
     * @param cheminFichier chemin du fichier à crypter ou décrypter.
     */
    public Chiffrage(String cheminFichier) {
        this.cheminFichier = cheminFichier;
        exposantChoisit = genererExposant();
        this.clePublique = exponentiationModulaire(G, exposantChoisit, P);
    }

    /**
     * Constructeur sans chemin de fichier.
     * Génère une clé publique Diffie-Hellman.
     */
    public Chiffrage() {
        exposantChoisit = genererExposant();
        this.clePublique = exponentiationModulaire(G, exposantChoisit, P);
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
            this.clePartager = exponentiationModulaire(cle, exposantChoisit, P);
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
                int index = Character.getNumericValue(gabString.charAt(i));
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
