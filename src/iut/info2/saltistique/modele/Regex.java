/* Regex.java                           28/10/2024
 *
 */

package iut.info2.saltistique.modele;

public enum Regex {


    EMPLOYES {
        /**
         * Retourne l'expression régulière permettant de vérifier la validité d'une ligne d'employé.
         * @param delimiteur le délimiteur utilisé dans le fichier
         * @return l'expression régulière
         */
        @Override
        public String getRegex(String delimiteur) {
            return "^E\\d{6}" + delimiteur // Identifiant : commence par 'E' suivi de 6 chiffres
                    + "[A-Za-z]+" + delimiteur // Nom : une ou plusieurs lettres
                    + "[A-Za-z]+" // Prénom : une ou plusieurs lettres
                    + delimiteur // Délimiteur
                    + "\\d{4}$"; // Téléphone : exactement 4 chiffres
        }
    },
    SALLES {
        /**
         * Retourne l'expression régulière permettant de vérifier la validité d'une ligne de salle.
         * @param delimiteur le délimiteur utilisé dans le fichier
         * @return l'expression régulière
         */
        @Override
        public String getRegex(String delimiteur) {
            return "^([0-9]{8})" + delimiteur // Identifiant : exactement 8 chiffres
                    + "([^" + delimiteur + "]+)" + delimiteur // Nom de la salle : tout caractère sauf le délimiteur
                    + "([0-9]+)"+ delimiteur // Capacité : un ou plusieurs chiffres
                    + "([onON][^" + delimiteur + "]*)" + delimiteur // Vidéoprojecteur : commence par 'o' ou 'n'
                                                                    // suivi de tout caractère sauf le délimiteur
                    + "([onON][^" + delimiteur + "]*)" + delimiteur // ecranXXL : commence par 'o' ou 'n'
                                                                    // suivi de tout caractère sauf le délimiteur
                    + "([0-9]*)"  + delimiteur // Délimiteur
                    + "([^" + delimiteur + "]*)" + delimiteur // Type : tout caractère sauf le délimiteur
                    + "([^" + delimiteur + "]*)" + delimiteur // Logiciels : tout caractère sauf le délimiteur
                    + "([onON][^" + delimiteur + "]*|)$"; // Imprimante : commence par 'o' ou 'n'
                                                          // suivi de tout caractère sauf le délimiteur ou vide
        }
    },
    ACTIVITES {
        /**
         * Retourne l'expression régulière permettant de vérifier la validité d'une ligne d'activité.
         * @param delimiteur le délimiteur utilisé dans le fichier
         * @return l'expression régulière
         */
        @Override
        public String getRegex(String delimiteur) {
            return "^A[0-9]{7}" + delimiteur // Identifiant : commence par 'A' suivi de 7 chiffres
                    + "[^" + delimiteur + "]+$"; // Nom de l'activité : tout caractère sauf le délimiteur
        }
    },
    RESERVATIONS {
        /**
         * Retourne l'expression régulière permettant de vérifier la validité d'une ligne de réservation.
         * @param delimiteur le délimiteur utilisé dans le fichier
         * @param delimiteur
         * @return
         */
        @Override
        public String getRegex(String delimiteur) {
            return "^R\\d{6}" + delimiteur // Identifiant : 'R' suivi de 6 chiffres
                    + "\\d{8}" + delimiteur // Salle : 8 chiffres
                    + "E\\d{6}" + delimiteur // Employé : 'E' suivi de 6 chiffres
                    + "[^" + delimiteur + "]+" + delimiteur // Activité : tout sauf le délimiteur
                    + "\\d{2}/\\d{2}/\\d{4}" + delimiteur // Date : JJ/MM/AAAA
                    + "\\d{1,2}h\\d{2}" + delimiteur // Heure de début : HHhMM
                    + "\\d{1,2}h\\d{2}" + delimiteur // Heure de fin : HHhMM
                    + "(?:[^" + delimiteur + "]*)" + delimiteur // Commentaire ou organisation : tout sauf le délimiteur ou vide
                    + "[A-Za-z]*" + delimiteur // Nom de l'employé : une ou plusieurs lettres ou vide
                    + "[A-Za-z]*" + delimiteur // Prénom de l'employé : une ou plusieurs lettres ou vide
                    + "(?:\\d{10}|)" + delimiteur // Téléphone de l'employé : 10 chiffres ou vide
                    + "(?:[^" + delimiteur + "]*)$"; // Activité : tout sauf le délimiteur ou vide
        }
    },
    ORDINATEURS {
        /**
         * Retourne l'expression régulière permettant de vérifier la validité d'une ligne d'ordinateur.
         * @param delimiteur le délimiteur utilisé dans le fichier
         * @return l'expression régulière
         */
        @Override
        public String getRegex(String delimiteur) {
            return "^([^;]*;){5}" + // 5 champs avant le champ ordinateur
                    "([0-9]+);" + // nb ordinateurs (au moins un chiffre)
                    "([^" + delimiteur + "]+);" + // type d'ordinateur (au moins un caractère)
                    "([^" + delimiteur + "]*);" + // logiciels (vide ou au moins un caractère)
                    "([onON][^" + delimiteur + "]*)$"; // imprimante (commence par 'o' ou 'n'
            // suivi de tout caractère sauf le délimiteur)
        }
    };

    public abstract String getRegex(String delimiteur);

}

