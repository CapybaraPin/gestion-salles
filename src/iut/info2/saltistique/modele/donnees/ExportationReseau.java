package iut.info2.saltistique.modele.donnees;

import iut.info2.saltistique.modele.Fichier;
import iut.info2.saltistique.modele.Serveur;

public class ExportationReseau {

    /** Port de communication entre les deux machines */
    private int port;

    /** Serveur permettant la connexion au ServerSocket */
    private Serveur serveur;

    /** Liste des fichiers transmis lors de la communication réseau */
    private Fichier[] fichiers;

    /**
     * Constructeur de l'exportation des données via le réseau
     * @param port Port de communication entre les deux machines
     * @param fichiers Liste des fichiers transmis lors de la communication réseau
     */
    public ExportationReseau(int port, Fichier[] fichiers){
        if (port < 1024 || port > 65535) {
            throw new IllegalArgumentException("Le numéro de port doit être compris entre 1024 et 65535.");
        }

        this.port = port;

        // TODO : Vérification de la variable fichiers
        this.fichiers = fichiers;

        // Liste des fichiers à exporter
        for (Fichier fichier : fichiers) {
            System.out.println(fichier.getFichierExploite().getName());
        }

        exportationDonnees();
    }

    /**
     * Exporte les données via le réseau au travers de la classe {@link Serveur}
     */
    private void exportationDonnees(){
        this.serveur = new Serveur(port, this.fichiers);
        Thread serveurThread = new Thread(serveur);
        serveurThread.start();
    }

    /**
     * Retourne le serveur associé à l'exportation des données
     * @return Serveur associé à l'exportation des données
     */
    public Serveur getServeur() {
        return serveur;
    }

    /**
     * Modifie le serveur associé à l'exportation des données
     * @param serveur Serveur associé à l'exportation des données
     */
    public void setServeur(Serveur serveur) {
        this.serveur = serveur;
    }
}
