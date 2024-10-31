package iut.info2.saltistique.modele;

import java.time.LocalDateTime;

public class Formation extends Reservation {


        private String nomFormateur;
        private String prenomFormateur;
        private String telephoneFormateur;

        public Formation(String identifiant, LocalDateTime dateDebut, LocalDateTime dateFin, String description,
                         Salle salle, Activite activite, Utilisateur utilisateur, String nomFormateur, String prenomFormateur,
                         String telephoneFormateur) {
            super(identifiant, dateDebut, dateFin, description, salle, activite, utilisateur);
            this.nomFormateur = nomFormateur;
            this.prenomFormateur = prenomFormateur;
            this.telephoneFormateur = telephoneFormateur;
        }

        public String getNomFormateur() {
            return nomFormateur;
        }

        public String getPrenomFormateur() {
            return prenomFormateur;
        }

        public String getTelephoneFormateur() {
            return telephoneFormateur;
        }

        @Override
        public String toString() {

            return "Réservation - formation{" +
                    "identifiant='" + getIdentifiant() + '\'' +
                    ", dateDebut=" + getDateDebut() +
                    ", dateFin=" + getDateFin() +
                    ", salle=" + getSalle() +
                    ", activite=" + getActivite() +
                    ", utilisateur=" + getUtilisateur() +
                    ", motif de formation='" + getDescription() + '\'' +
                    ", nomFormateur='" + nomFormateur + '\'' +
                    ", prenomFormateur='" + prenomFormateur + '\'' +
                    ", telephoneFormateur='" + telephoneFormateur + '\'' +
                    '}';
        }
        }
