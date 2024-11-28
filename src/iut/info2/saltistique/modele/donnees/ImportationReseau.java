/*
 * ImportationReseau.java          13/11/2024
 * IUT DE RODEZ            Pas de copyrights
 */
package iut.info2.saltistique.modele.donnees;

import iut.info2.saltistique.modele.Client;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;

/**
 * Classe permettant l'importation de données via une connexion réseau.
 *
 * <p>Elle se connecte à un serveur distant pour recevoir des fichiers de données,
 * les sauvegarde localement, puis les traite via la classe parente {@link Importation}.</p>
 */
public class ImportationReseau extends Importation {

    /** Chemin vers le dossier des fichiers sources. */
    private static final String DOSSIER_SOURCES = "src/ressources/fichiers";

    /** Répertoire où les fichiers importés sont sauvegardés. */
    private File dossierSauvegarde;

    /** Adresse IP du serveur d'importation. */
    private InetAddress host;

    /** Port du serveur d'importation. */
    private int port;

    /** Client permettant la communication via un socket. */
    private Client client;

    /**
     * Constructeur de la classe ImportationReseau.
     * Initialise la connexion réseau, vérifie la validité des paramètres,
     * et lance l'importation des données.
     *
     * @param host Adresse IP du serveur d'importation (au format chaîne).
     * @param port Port du serveur d'importation (doit être entre 1024 et 65535).
     * @param donnees Instance de {@link GestionDonnees} pour traiter les fichiers importés.
     * @throws IOException Si une erreur survient lors de la gestion des fichiers locaux.
     * @throws IllegalArgumentException Si l'adresse IP ou le port sont invalides.
     */
    public ImportationReseau(String host, int port, GestionDonnees donnees) throws IOException {
        super(donnees);

        if (port < 1024 || port > 65535) {
            throw new IllegalArgumentException("Port Invalide : Le numéro de port doit être compris entre 1024 et 65535.");
        }

        if (host == null || host.isEmpty()) {
            throw new IllegalArgumentException("Adresse IP Invalide : L'adresse IP du serveur ne peut pas être vide.");
        }

        try {
            this.host = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("Adresse IP Invalide : " + host);
        }

        this.port = port;
        this.dossierSauvegarde = new File(DOSSIER_SOURCES);

        if (!sourceEstVide()) {
            viderSources();
        }

        connexionClient();
    }

    /**
     * Initialise la connexion avec le client en utilisant l'adresse IP et le port fournis.
     * La connexion est gérée dans un thread distinct.
     */
    private void connexionClient() {
        String adresseIp = host.toString();
        client = new Client(adresseIp.replace("/", ""), port);
        Thread clientThread = new Thread(client);
        clientThread.start();
    }

    /**
     * Vérifie si le dossier source contenant les fichiers est vide.
     *
     * @return {@code true} si le dossier contient des fichiers, sinon {@code false}.
     */
    private boolean sourceEstVide() {
        File[] fichiersExistants = dossierSauvegarde.listFiles();
        if (fichiersExistants != null && fichiersExistants.length > 0) {
            return false;
        }
        return true;
    }

    /**
     * Vide le dossier source en supprimant tous les fichiers qu'il contient.
     *
     * @throws IOException Si un fichier ne peut pas être supprimé.
     */
    private void viderSources() throws IOException {
        File[] fichiersExistants = dossierSauvegarde.listFiles();

        if (fichiersExistants != null) {
            for (File fichier : fichiersExistants) {
                System.out.println(fichier.getAbsolutePath());
                try {
                    Files.deleteIfExists(fichier.toPath());
                } catch (IOException e) {
                    throw new IOException("Impossible de supprimer le fichier du dossier source : " + fichier.getName());
                }
            }
        }
    }
}
