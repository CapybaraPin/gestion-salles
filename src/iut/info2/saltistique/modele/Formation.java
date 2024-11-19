package iut.info2.saltistique.modele;

import java.time.LocalDateTime;

/**
 *Représente une formation avec le nom, le prénom et le numéros de téléphone du formateur.
 *Cette classe implémente Reservation afin de pouvoir récupérer des information de
 * résevation liée a une formation
 *
 * @author Jules Vialas
 */

public class Formation extends Reservation {

        /** Nom du Formateur */
        private String nomFormateur;

        /** Prénom du Formateur */
        private String prenomFormateur;

        /** Numéro de téléphone du Formateur */
        private String telephoneFormateur;

        /**
         * Constructeur de la classe Formation.
         * Ce constructeur initialise une réservation de formation en prenant en compte
         * l'identifiant, les dates de début et de fin, la description, la salle,
         * l'activité, l'utilisateur ainsi que les informations relatives au formateur.
         *
         * @param identifiant L'identifiant unique de la réservation de formation.
         * @param dateDebut La date et l'heure de début de la formation.
         * @param dateFin La date et l'heure de fin de la formation.
         * @param description Une description de la formation, du motif ou des objectifs.
         * @param salle La salle où se déroulera la formation.
         * @param activite L'activité associée à la formation.
         * @param utilisateur L'utilisateur ayant réservé la formation.
         * @param nomFormateur Le nom du formateur pour la formation.
         * @param prenomFormateur Le prénom du formateur pour la formation.
         * @param telephoneFormateur Le numéro de téléphone du formateur pour la formation.
         */
        public Formation(String identifiant, LocalDateTime dateDebut, LocalDateTime dateFin, String description,
                         Salle salle, Activite activite, Utilisateur utilisateur, String nomFormateur, String prenomFormateur,
                         String telephoneFormateur) {
            super(identifiant, dateDebut, dateFin, description, salle, activite, utilisateur);
            this.nomFormateur = nomFormateur;
            this.prenomFormateur = prenomFormateur;
            this.telephoneFormateur = telephoneFormateur;
        }

        /**
         * Obtient le nom du formateur.
         *
         * @return Le nom du formateur.
         */
        public String getNomFormateur() {
            return nomFormateur;
        }

        /**
         * Obtient le prénom du formateur.
         *
         * @return Le prénom du formateur.
         */
        public String getPrenomFormateur() {
            return prenomFormateur;
        }

        /**
         * Obtient le numéro de téléphone du formateur.
         *
         * @return Le le numéro de téléphone du formateur.
         */
        public String getTelephoneFormateur() {
            return telephoneFormateur;
        }

        /**
         * Cette méthode génère une description détaillée de la réservation de formation
         * incluant les informations telles que l'identifiant, les dates, la salle,
         * l'activité, l'utilisateur, le motif de formation, et les informations du formateur.
         *
         * @return Une chaîne de caractères représentant les informations complètes
         *         de la réservation de formation sous la forme d'une description.
         */
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
