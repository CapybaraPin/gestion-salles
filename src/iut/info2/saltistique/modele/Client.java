/**
 * Client.java                      SAE 3.A.01
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique.modele;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * La classe Client représente un client qui se connecte à un serveur pour
 * recevoir des objets. Elle utilise un socket pour se connecter au serveur
 * et des flux pour recevoir des données sérialisées.
 *
 * <p>Le client se connecte à l'adresse IP et au port spécifiés. Il peut
 * recevoir un objet depuis le serveur et arrêter proprement la connexion.</p>
 *
 * @author Dorian ADAMS, Hugo ROBLES
 */
public class Client {

    /** Adresse IP du serveur */
    private String adresseIp;

    /** Port du serveur */
    private int port;

    /** Socket de connexion au serveur */
    private Socket socket;

    /**
     * Constructeur de la classe Client.
     * Initialise un client qui se connecte à l'adresse IP et au port spécifiés.
     *
     * @param adresseIp l'adresse IP du serveur
     * @param port le port du serveur
     * @throws IllegalArgumentException si les paramètres d'adresse IP ou de port sont invalides.
     * @throws IOException              si une erreur d'entrée/sortie se produit lors de la connexion au serveur.
     */
    public Client(String adresseIp, int port) throws IOException {
        // Validation de l'adresse IP et du port
        if (adresseIp == null || adresseIp.isEmpty()) {
            throw new IllegalArgumentException("L'adresse IP ne peut pas être vide.");
        }
        if (port < 1024 || port > 65535) {
            throw new IllegalArgumentException("Le numéro de port doit être compris entre 1024 et 65535.");
        }

        this.adresseIp = adresseIp;
        this.port = port;

        // Connexion au serveur
        try {
            this.socket = new Socket(adresseIp, port);
            System.out.println("Client connecté au serveur à l'adresse " + adresseIp + " sur le port " + port);
        } catch (IOException e) {
            System.err.println("Erreur lors de la connexion au serveur : " + e.getMessage());
            throw e;
        }
    }

    /**
     * Reçoit un objet depuis le serveur.
     *
     * <p>Cette méthode utilise un flux d'entrée d'objet {@link ObjectInputStream}
     * pour recevoir des objets envoyés par le serveur.</p>
     *
     * @return l'objet reçu du serveur
     * @throws IOException si une erreur d'entrée/sortie se produit lors de la réception des données
     * @throws ClassNotFoundException si la classe de l'objet reçu n'est pas trouvée
     */
    public Object recevoir() throws IOException, ClassNotFoundException {
        if (this.socket != null && !this.socket.isClosed()) {
            try (ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream())) {
                Object objetRecu = ois.readObject();
                System.out.println("Objet reçu du serveur : " + objetRecu);
                return objetRecu;
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erreur lors de la réception de l'objet : " + e.getMessage());
                throw e;
            }
        } else {
            throw new IOException("Le socket est fermé ou non connecté.");
        }
    }

    /**
     * Ferme la connexion au serveur en fermant le socket.
     *
     * @throws IOException si une erreur d'entrée/sortie se produit lors de la fermeture du socket.
     */
    public void arreter() throws IOException {
        if (this.socket != null && !this.socket.isClosed()) {
            this.socket.close();
            System.out.println("Connexion avec le serveur fermée.");
        } else {
            System.err.println("Le socket est déjà fermé ou non ouvert.");
        }
    }
}