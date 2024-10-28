/**
 * Serveur.java                     SAE 3.A.01
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique.modele;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * La classe Serveur représente un serveur simple qui accepte des connexions
 * clients via un socket, puis envoie un objet au client connecté.
 * Cette classe implémente {@link Runnable} pour permettre l'exécution
 * du serveur dans un thread séparé. Cela permet une écoute continue
 * des connexions clients sans bloquer l'application principale.
 *
 * <p>Le serveur écoute sur un port spécifique, attend une connexion, puis
 * fournit des méthodes pour envoyer un objet et pour arrêter le serveur.</p>
 *
 * @author Dorian ADAMS, Hugo ROBLES
 */
public class Serveur implements Runnable {

    /** Port commun entre serveur et client */
    private int port;

    /** Socket de lancement du serveur */
    private ServerSocket serverSocket;

    /** Objet à envoyer aux clients */
    private Object objet;

    /**
     * Constructeur de la classe Serveur.
     *
     * @param port Le numéro de port sur lequel le serveur écoute.
     * @param objetAEnvoyer L'objet à envoyer aux clients connectés.
     * @throws IllegalArgumentException si l'objet n'implémente pas Serializable,
     *                                  si le port n'est pas compris entre 1024 et 65535.
     */
    public Serveur(int port, Object objetAEnvoyer) {
        if (port < 1024 || port > 65535) {
            throw new IllegalArgumentException("Le numéro de port doit être compris entre 1024 et 65535.");
        }
        if (!(objetAEnvoyer instanceof Serializable)) {
            throw new IllegalArgumentException("L'objet doit implémenter Serializable.");
        }
        this.port = port;
        this.objet = objetAEnvoyer;
    }

    /**
     * Méthode principale de la classe, exécutée dans un thread séparé.
     *
     * Cette méthode initialise le socket serveur et écoute en continu
     * les connexions client tant que isRunning est vrai.
     * Lorsqu'un client se connect, le serveur lui envoie automatiquement
     * l'objet spécifié.
     */
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Serveur démarrer sur le port : " + port);

            while (!serverSocket.isClosed()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                    oos.writeObject(this.objet);  // Envoie l'objet au client
                    System.out.println("Objet envoyé au client : " + objet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ferme le socket du serveur, arrêtant ainsi la connexion avec les clients.
     *
     * @throws IOException si une erreur d'entrée/sortie se produit lors de la fermeture du socket.
     */
    public void arreter() {
        if(serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                System.out.println("Serveur arreter");
            } catch (IOException e) {
                System.err.println("Erreur lors de la fermeture du serveur : " + e.getMessage());
            }
        }
    }

}
