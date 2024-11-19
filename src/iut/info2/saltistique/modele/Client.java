/*
 * Client.java           SAE 3.A.01
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique.modele;

import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.controleur.ControleurImporterReseau;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;

/**
 * La classe Client gère la connexion à un serveur pour recevoir et sauvegarder plusieurs fichiers CSV.
 * Chaque fichier reçu est chiffré, déchiffré, et sauvegardé dans un répertoire local spécifié.
 *
 * @author Hugo ROBLES, Dorian ADAMS
 */
public class Client implements Runnable{

    /** Adresse IP du serveur */
    private final String host;

    /** Port du serveur */
    private final int port;

    /** Dossier de sauvegarde des fichiers reçus */
    private final String dossierSauvegarde = "src/ressources/fichiers";

    /** Taille du buffer de lecture (1024 octets) */
    private static final int BUFFER_SIZE = 1024;

    /** Progression de l'envoi des fichiers */
    private double progression;

    /** Taille totale des fichiers */
    private long tailleTotale;

    /**
     * Constructeur du Client.
     *
     * @param host Adresse IP du serveur.
     * @param port Port du serveur.
     */
    public Client(String host, int port) {
        this.host = host;
        this.port = port;

        // Vérification des données
        if (host == null || host.isEmpty()) {
            throw new IllegalArgumentException("Adresse IP Invalide : L'adresse IP du serveur ne peut pas être vide.");
        }
        if (port < 1024 || port > 65535) {
            throw new IllegalArgumentException("Port Invalide : Le numéro de port doit être compris entre 1024 et 65535.");
        }
    }

    /**
     * Reçoit un fichier unique depuis le serveur et le sauvegarde dans le répertoire local.
     *
     * @param dis    Flux d'entrée de données du serveur.
     * @param socket Socket connecté au serveur.
     * @throws IOException En cas d'erreur de réception ou d'écriture du fichier.
     */
    private void recevoirFichier(DataInputStream dis, Socket socket) throws IOException {
        String nomFichier = "";
        long tailleFichier;
        int tailleTableauCle;
        String cheminFichier;
        BigInteger receptionGrosEntier;
        BigInteger envoiGrosEntier;
        BufferedOutputStream bos;
        DataOutputStream dos;
        FileOutputStream fos;
        Chiffrage chiffrage;

        try{
            chiffrage = new Chiffrage();

            // Reception d'un BigInteger
            tailleTableauCle = dis.readInt();
            byte[] bytes = new byte[tailleTableauCle];
            dis.readFully(bytes);
            receptionGrosEntier = new BigInteger(bytes);

            // Préparation de l'envoi d'un BigInteger
            bos = new BufferedOutputStream(socket.getOutputStream());
            dos = new DataOutputStream(bos);

            // Envoi d'un BigInteger
            envoiGrosEntier = chiffrage.getClePublique();
            dos.writeInt(envoiGrosEntier.toByteArray().length);
            dos.write(envoiGrosEntier.toByteArray());
            dos.flush();

            // Préparation chiffrage avec la clé reçue
            chiffrage.calculeClePartager(receptionGrosEntier);
            chiffrage.genererCleVigenere();

            // Réception du nom et de la taille du fichier + Création de celui-ci
            nomFichier = dis.readUTF();
            tailleFichier = dis.readLong();
            File sauvegardeFichier = new File(this.dossierSauvegarde, nomFichier);

            // Réception des données du fichier
            fos = new FileOutputStream(sauvegardeFichier);
            bos = new BufferedOutputStream(fos);

            recevoirDonnees(dis, bos, tailleFichier);
            fos.close();

            // Déchiffrage du fichier
            chiffrage.setCheminFichier(sauvegardeFichier.getAbsolutePath());
            cheminFichier = chiffrage.dechiffrer();

            //Supression du fichier chiffrer
            sauvegardeFichier.delete();

            System.out.println("Fichier reçu et sauvegardé : " + cheminFichier);

        } catch (IOException e) {
            System.err.println("Erreur lors de la réception du fichier " + nomFichier + " : " + e.getMessage());
        }

    }

    /**
     * Reçoit les données d'un fichier et les écrit dans un flux de sortie.
     *
     * @param dis           Flux de données du serveur.
     * @param bos           Flux de sortie pour écrire le fichier.
     * @param tailleFichier Taille totale du fichier à recevoir.
     * @throws IOException En cas d'erreur de lecture ou d'écriture.
     */
    private void recevoirDonnees(DataInputStream dis, BufferedOutputStream bos, long tailleFichier) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        long totalRead = 0;

        while (totalRead < tailleFichier) {
            int bytesToRead = (int) Math.min(BUFFER_SIZE, tailleFichier - totalRead);
            int bytesRead = dis.read(buffer, 0, bytesToRead);

            if (bytesRead == -1) {
                throw new EOFException("Fin de fichier inattendue");
            }

            bos.write(buffer, 0, bytesRead);
            totalRead += bytesRead;

            this.progression += bytesRead;

            ControleurImporterReseau controleurImporterReseau = Saltistique.getController(Scenes.IMPORTATION_RESEAU);
            controleurImporterReseau.miseAJourProgression(this.progression / this.tailleTotale);

        }
        bos.flush();
    }

    /**
     * Lance la connexion au serveur et gère la réception des fichiers.
     */
    @Override
    public void run() {
        Socket socket;

        try {
            socket = new Socket(host, port);

            System.out.println("Connecté au serveur sur " + host + ":" + port);

            new Notification("Importation des données", "Importation via " + host + ":" + port);

            try{
                BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
                DataInputStream dis = new DataInputStream(bis);

                int nbFichiers = dis.readInt();
                this.tailleTotale = dis.readLong();

                System.out.println("Nombre de fichiers à recevoir : " + nbFichiers);
                System.out.println("Taille totale des fichiers à recevoir : " + tailleTotale);

                for (int i = 0; i < nbFichiers; i++) {
                    recevoirFichier(dis, socket);

                    Thread.sleep(1000);
                }

                ControleurImporterReseau controleurImporterReseau = Saltistique.getController(Scenes.IMPORTATION_RESEAU);
                controleurImporterReseau.finInmportationReseau(dossierSauvegarde);


            } catch (IOException e) {
                new Notification("Erreur de lecture du fichier", "Accès au fichiers distants impossible.");

            } catch (InterruptedException e) {
                System.out.println("Erreur");
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            new Notification("Erreur de connexion au serveur", "Impossible de se connecter au serveur.");
        }
    }
}