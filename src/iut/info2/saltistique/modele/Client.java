package iut.info2.saltistique.modele;

import java.io.*;
import java.net.Socket;

/**
 * La classe Client permet de se connecter à un serveur pour recevoir et sauvegarder
 * plusieurs fichiers CSV. Chaque fichier est sauvegardé dans un répertoire spécifié.
 */
public class Client {

    /** Adresse IP du serveur */
    private final String host;

    /** Port du serveur */
    private final int port;

    /** Dossier de sauvegarde des fichiers reçus */
    private final String dossierSauvegarde = "src/ressources/fichiers/reception";

    /** Taille du buffer de lecture (4096 octets) pour améliorer la lisibilité et la flexibilité */
    private static final int BUFFER_SIZE = 4096;

    /**
     * Constructeur du Client.
     *
     * @param host Adresse IP du serveur.
     * @param port Port du serveur.
     */
    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Se connecte au serveur et reçoit les fichiers un par un.
     */
    public void reception() {
        try {
            Socket socket = new Socket(host, port);

            System.out.println("Connecté au serveur sur " + host + ":" + port);

            try{
                BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
                DataInputStream dis = new DataInputStream(bis);

                int nbFichiers = dis.readInt();
                System.out.println("Nombre de fichiers à recevoir : " + nbFichiers);

                for (int i = 0; i < nbFichiers; i++) {
                    recevoirFichier(dis);
                }
            } catch (IOException e) {
                System.err.println("Erreur de lecture du fichier : " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Erreur de connexion au serveur : " + e.getMessage());
        }
    }

    /**
     * Reçoit un fichier unique depuis le serveur et le sauvegarde dans le répertoire spécifié.
     *
     * @param dis DataInputStream utilisé pour lire les données du serveur.
     * @throws IOException En cas de problème de réception du fichier.
     */
    private void recevoirFichier(DataInputStream dis) throws IOException {
        String nomFichier = dis.readUTF();
        long tailleFichier = dis.readLong();
        File sauvegardeFichier = new File(this.dossierSauvegarde, nomFichier);

        try{
            FileOutputStream fos = new FileOutputStream(sauvegardeFichier);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            recevoirDonnees(dis, bos, tailleFichier);

            System.out.println("Fichier reçu et sauvegardé : " + sauvegardeFichier.getAbsolutePath());

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
        }
        bos.flush();
    }
}
