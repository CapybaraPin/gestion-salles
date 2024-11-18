package iut.info2.saltistique.modele;

import iut.info2.saltistique.Saltistique;
import iut.info2.saltistique.controleur.ControleurImporterReseau;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * La classe Client permet de se connecter à un serveur pour recevoir et sauvegarder
 * plusieurs fichiers CSV. Chaque fichier est sauvegardé dans un répertoire spécifié.
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

    private double progression;

    private long tailleTotale;

    private Socket socket;

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
     * Reçoit un fichier unique depuis le serveur et le sauvegarde dans le répertoire spécifié.
     *
     * @param dis    DataInputStream utilisé pour lire les données du serveur.
     * @param socket
     * @throws IOException En cas de problème de réception du fichier.
     */
    private void recevoirFichier(DataInputStream dis, Socket socket) throws IOException {
        String nomFichier = "";
        try{
            Chiffrage chiffrage = new Chiffrage();

            //Reception d'un BigInteger
            int lenght = dis.readInt();
            byte[] bytes = new byte[lenght];
            dis.readFully(bytes);
            BigInteger bigIntReceived = new BigInteger(bytes);
            System.out.println("BigInteger reçu du serveur : " + bigIntReceived);

            //Envoi d'un BigInteger
            BufferedOutputStream boss = new BufferedOutputStream(socket.getOutputStream());
            DataOutputStream dos = new DataOutputStream(boss);
            BigInteger bigIntToSend = chiffrage.getClePublic();
            dos.writeInt(bigIntToSend.toByteArray().length);
            dos.write(bigIntToSend.toByteArray());
            dos.flush();
            System.out.println("BigInteger envoyé au serveur.");

            chiffrage.calculeClePartager(bigIntReceived);
            chiffrage.generateKeyFromDiffie();

            nomFichier = dis.readUTF();
            long tailleFichier = dis.readLong();
            File sauvegardeFichier = new File(this.dossierSauvegarde, nomFichier);

            FileOutputStream fos = new FileOutputStream(sauvegardeFichier);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            recevoirDonnees(dis, bos, tailleFichier);
            fos.close();

            System.out.println("Fichier reçu et sauvegardé : " + sauvegardeFichier.getAbsolutePath());
            chiffrage.setCheminFichier(sauvegardeFichier.getAbsolutePath());
            String chemin = chiffrage.decrypter();
            sauvegardeFichier.delete();
            System.out.println("Fichier décrypter : " + chemin);

        } catch (IOException e) {
            System.err.println("Erreur lors de la réception du fichier " + nomFichier + " : " + e.getMessage());
        }

    }

    /**
     * Lit les données du flux d'entrée et les écrit dans le flux de sortie jusqu'à atteindre la taille spécifiée.
     *
     * @param dis le flux de données d'entrée du serveur
     * @param bos le flux de sortie vers le fichier de sauvegarde
     * @param tailleFichier taille totale du fichier à recevoir
     * @throws IOException en cas d'erreur de lecture ou d'écriture
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
            controleurImporterReseau.onProgressUpdate(this.progression / this.tailleTotale);

        }
        bos.flush();
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(host, port);
            this.socket = socket;

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


                Saltistique.gestionDonnees.finInmportationReseau(dossierSauvegarde);

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