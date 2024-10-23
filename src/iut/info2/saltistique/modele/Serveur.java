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
 * La classe Serveur représente un serveur simple qui accepte une connexion
 * client via un socket, puis envoie des objets au client connecté.
 * Cette classe étend {@link Thread} afin de pouvoir gérer des opérations
 * réseau de manière parallèle.
 *
 * <p>Le serveur écoute sur un port spécifique, attend qu'un client se connecte,
 * puis fournit des méthodes pour envoyer des objets au client et pour arrêter
 * le serveur.</p>
 * 
 * @author Dorian ADAMS, Hugo ROBLES
 */
public class Serveur extends Thread {

    /** Port commun entre serveur et client */
    private int port;

    /** Socket de lancement du serveur */
    private Socket socket;

    /** Timeout en millisecondes pour l'acceptation des connexions */
    private static final int TIMEOUT = 10000; // 10 secondes

    /**
     * Constructeur de la classe Serveur.
     * Initialise le serveur sur le port spécifié et attend qu'un client
     * se connecte.
     *
     * @param port le numéro de port sur lequel le serveur écoute.
     */
    public Serveur(int port) {
        if (port < 1024 || port > 65535) {
            throw new IllegalArgumentException("Le numéro de port doit être compris entre 1024 et 65535.");
        }

        this.port = port;

        try {
            ServerSocket serverSocket = new ServerSocket(this.port);
            serverSocket.setSoTimeout(TIMEOUT);

            System.out.println("En attente de connexion client sur le port : " + this.port);
            socket = serverSocket.accept();

        } catch (IOException e) {
            System.err.println("Erreur lors de la connexion au client : " + e.getMessage());
        }
    }

    /**
     * Envoie un objet au client connecté.
     *
     * <p>Cette méthode utilise un flux de sortie d'objet {@link ObjectOutputStream}
     * pour transmettre un objet à travers le socket vers le client.</p>
     *
     * @param donnees l'objet à envoyer au client. Cet objet doit être sérialisable.
     * @throws IOException si une erreur d'entrée/sortie se produit lors de l'envoi des données.
     * @throws IllegalArgumentException si l'objet à envoyer n'est pas sérialisable.
     */
    public void envoyer(Object donnees) throws IOException {
        if (!(donnees instanceof Serializable)) {
            throw new IllegalArgumentException("L'objet doit implémenter Serializable.");
        }

        if (this.socket != null && !this.socket.isClosed()) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());
                oos.writeObject(donnees);  // Envoie l'objet au client
                System.out.println("Objet envoyé au client : " + donnees);
            } catch (IOException e) {
                System.err.println("Erreur lors de l'envoi de l'objet au client : " + e.getMessage());
            }
        } else {
            System.err.println("Aucun client n'est connecté pour recevoir des données.");
        }
    }

    /**
     * Ferme le socket du serveur, arrêtant ainsi la connexion avec le client.
     *
     * @throws IOException si une erreur d'entrée/sortie se produit lors de la fermeture du socket.
     */
    public void arreter() throws IOException {
        if (this.socket != null && !this.socket.isClosed()) {
            this.socket.close();
            System.out.println("Connexion fermée avec le client.");
        } else {
            System.err.println("Le socket est déjà fermé ou n'a pas encore été ouvert.");
        }
    }

}
