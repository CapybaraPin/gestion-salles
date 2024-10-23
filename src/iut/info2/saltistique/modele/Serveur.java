/**
 * Serveur.java                     SAE 3.A.01
 * IUT de RODEZ, tous les droits sont réservés
 */
package iut.info2.saltistique.modele;

import java.net.ServerSocket;
import java.net.Socket;

public class Serveur extends Thread {

    /** port commun entre serveur et client */
    private int port;

    /** stocket de lancement du serveur */
    private Socket socket;

    /**
     * Constructeur de la classe Serveur
     * @param port
     */
    public Serveur(int port) {

        try {
            ServerSocket socketServeur = new ServerSocket(port);
            System.out.println("Lancement du serveur");

            while (true) {
                Socket socketClient = socketServeur.accept();
                this.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
