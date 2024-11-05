package iut.info2.saltistique.modele;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * La classe Serveur gère les connexions client pour envoyer une liste de fichiers CSV.
 * Chaque fichier est transmis individuellement au client connecté.
 */
public class Serveur implements Runnable {

    /** Port d'écoute du serveur */
    private final int port;

    /** Socket du serveur pour accepter les connexions */
    private ServerSocket serverSocket;

    /** Liste des fichiers à envoyer aux clients */
    private final Fichier[] fichiersCSV;

    /** Taille du buffer pour la lecture et l'envoi de fichiers (4096 octets) */
    private static final int BUFFER_SIZE = 4096;

    /**
     * Constructeur de la classe Serveur.
     *
     * @param port        le port d'écoute du serveur (doit être entre 1024 et 65535).
     * @param fichiersCSV la liste des fichiers CSV à envoyer.
     * @throws IllegalArgumentException si le port est hors limites ou si la liste de fichiers est vide.
     */
    public Serveur(int port, Fichier[] fichiersCSV) {
        if (port < 1024 || port > 65535) {
            throw new IllegalArgumentException("Le numéro de port doit être compris entre 1024 et 65535.");
        }
        if (fichiersCSV == null || fichiersCSV.length == 0) {
            throw new IllegalArgumentException("La liste des fichiers CSV ne peut pas être vide.");
        }
        this.port = port;
        this.fichiersCSV = fichiersCSV;
    }

    /**
     * Démarre le serveur, accepte les connexions clients et envoie les fichiers.
     */
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Serveur démarré sur le port : " + port);

            while (!serverSocket.isClosed()) {
                try{
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connecté. Envoi des fichiers...");

                    envoyerFichiers(clientSocket);
                } catch (IOException e) {
                    System.err.println("Erreur lors de la connexion avec un client : " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du démarrage du serveur : " + e.getMessage());
        }
    }

    /**
     * Envoie tous les fichiers de la liste au client connecté.
     *
     * @param clientSocket le socket de la connexion client.
     * @throws IOException si une erreur d'entrée/sortie se produit.
     */
    private void envoyerFichiers(Socket clientSocket) throws IOException {
        try{
            BufferedOutputStream bos = new BufferedOutputStream(clientSocket.getOutputStream());
            DataOutputStream dos = new DataOutputStream(bos);

            // Envoie le nombre total de fichiers à envoyer
            dos.writeInt(fichiersCSV.length);

            // Envoie de la taille totale des fichiers
            long tailleTotale = 0;
            for (Fichier fichier : fichiersCSV) {
                tailleTotale += fichier.getFichierExploite().length();
            }

            dos.writeLong(tailleTotale);

            // Envoie chaque fichier individuellement
            for (Fichier fichier : fichiersCSV) {
                envoyerFichier(dos, fichier);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'envoi des fichiers au client : " + e.getMessage());
        }
    }

    /**
     * Envoie un fichier individuel au client via le flux de données.
     *
     * @param dos le flux de données pour l'envoi au client.
     * @param fichier le fichier à envoyer.
     * @throws IOException si une erreur de lecture/écriture se produit.
     */
    private void envoyerFichier(DataOutputStream dos, Fichier fichier) throws IOException {
        File fichierExploite = fichier.getFichierExploite();
        dos.writeUTF(fichierExploite.getName());
        dos.writeLong(fichierExploite.length());

        try{
            FileInputStream fis = new FileInputStream(fichierExploite);
            BufferedInputStream bis = new BufferedInputStream(fis);

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                dos.write(buffer, 0, bytesRead);
            }
            dos.flush(); // Assure l'envoi complet du fichier
            System.out.println("Fichier envoyé : " + fichierExploite.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Erreur lors de l'envoi du fichier " + fichierExploite.getName() + " : " + e.getMessage());
        }
    }

    /**
     * Arrête le serveur en fermant le socket principal.
     */
    public void arreter() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                System.out.println("Serveur arrêté");
            } catch (IOException e) {
                System.err.println("Erreur lors de la fermeture du serveur : " + e.getMessage());
            }
        }
    }
}
