/**
 * Client.java                      SAE 3.A.01
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique.modele;

import java.io.IOException;
import java.io.ObjectInputStream;
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

    private String host;
    private int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Object connect() {
        Socket socket = null;
        try {
            socket = new Socket(host, port);
            try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
                Object objetRecu = ois.readObject();
                System.out.println("Objet reçu du serveur : " + objetRecu);
                return objetRecu;
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erreur lors de la réception de l'objet : " + e.getMessage());
                throw e;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if(socket != null && !socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return null;
    }

}
