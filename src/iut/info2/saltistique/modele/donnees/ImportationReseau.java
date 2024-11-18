/*
 * ImportationReseau.java          13/11/2024
 * IUT DE RODEZ            Pas de copyrights
 */
package iut.info2.saltistique.modele.donnees;

import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.modele.Client;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;

/**
 *
 */
public class ImportationReseau extends Importation {

    /** Dossier des sources des fichiers */
    private static final String DOSSIER_SOURCES = "src/ressources/fichiers";

    /** Dossier de sauvegarde */
    private File dossierSauvegarde;

    /** Adresse IP du serveur d'importation */
    private InetAddress host;

    /** Port du serveur d'importation */
    private int port;

    /** Client permettant la connexion au socket */
    private Client client;

    /**
     * Constructeur d'importation des données via le réseau
     * @param host Adresse IP du serveur d'importation
     * @param port Port du serveur d'importation
     */
    public ImportationReseau(String host, int port, GestionDonnees donnees) throws IOException {
        super(donnees);

        if (port < 1024 || port > 65535) {
            throw new IllegalArgumentException("Port Invalide : Le numéro de port doit être compris entre 1024 et 65535.");
        }

        if (host == null || host.isEmpty()) {
            throw new IllegalArgumentException("Adresse IP Invalide : L'adresse IP du serveur ne peut pas être vide.");
        }

        // Vérification que l'adresse IP est valide (sous le bon format)
        try {
            this.host = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Adresse IP Invalide : " + host);
        }

        this.port = port;
        this.dossierSauvegarde = new File(DOSSIER_SOURCES);

        // Vérification que le dossier des sources est vide
        if (!sourceEstVide()){
            viderSources();
        }

        importationDonnees();
    }

    /**
     * Gère toute la connexion au client
     * et le transfert de données.
     */
    private void connexionClient(){
        System.out.println("Connexion avec le client...");

        String adresseIp = host.toString();

        client = new Client(adresseIp, port);
        Thread clientThread = new Thread(client);
        clientThread.start();
    }

    /**
     * Récupère les données transmises par le client dans le dossier
     * de source, et l'envoi à la classe {@link Importation} d'importation
     * via les fichiers.
     */
    private void importationDonnees() throws IOException {
        System.out.println("Importation des données via le client...");
        connexionClient();

        File[] fichiersExistants = dossierSauvegarde.listFiles();
        String[] cheminFichiers = new String[4];

        if (sourceEstVide()){
            for (int i = 0; i < 4; i++) {
                cheminFichiers[i] = fichiersExistants[i].getAbsolutePath();
            }
            importerDonnees(cheminFichiers);
        }
    }

    /**
     * Vérifie que le dossier source est vide.
     * <ul>
     *      <li>Vrai, si le dossier de sources est vide</li>
     *      <li>Faux, si le dossier de sources est non vide</li>
     * </ul>
     * @return boolean
     */
    private boolean sourceEstVide(){
        File[] fichiersExistants = dossierSauvegarde.listFiles();
        return fichiersExistants != null && fichiersExistants.length > 0;
    }

    /**
     * Vide le dossier de sources s'il y a encore
     * d'anciens fichiers dans celui-ci.
     */
    private void viderSources() throws IOException {
        File[] fichiersExistants = dossierSauvegarde.listFiles();

        if (fichiersExistants != null) {
            for (File fichier : fichiersExistants) {
                try {
                    Files.deleteIfExists(fichier.toPath());
                } catch (IOException e) {
                    throw new IOException("Impossible de supprimer le fichier du dossier source : "+ fichier.getName());
                }
            }
        }
    }
}
