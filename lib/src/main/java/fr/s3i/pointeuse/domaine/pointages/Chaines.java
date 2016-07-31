/*
 * Oburo.O est un programme destinée à saisir son temps de travail sur un support Android.
 *
 *     This file is part of Oburo.O
 *     Oburo.O is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package fr.s3i.pointeuse.domaine.pointages;

import fr.s3i.pointeuse.domaine.communs.R;
import fr.s3i.pointeuse.domaine.pointages.interactors.calendrier.boundaries.out.model.PointageInfo;
import fr.s3i.pointeuse.domaine.pointages.services.model.PointageWrapper;

/**
 * Expose les chaînes de caractères constantes, et les méthodes constantes générant des chaînes
 * destinées à être affichées à l'écran.
 */
public final class Chaines {

    private Chaines() {
        // constructeur privé
    }

    public static final String copyright = R.get("copyright");

    public static final String interacteur_pointer_nom = R.get("interacteur_pointer_nom");
    public static final String interacteur_calendrier_nom = R.get("interacteur_calendrier_nom");

    public static final String oui = R.get("oui");
    public static final String non = R.get("non");
    public static final String ok = R.get("ok");
    public static final String annuler = R.get("annuler");

    public static final String demander_date_debut = R.get("demander_date_debut");
    public static final String demander_date_fin = R.get("demander_date_fin");
    public static String demanderConfirmationSuppression(PointageInfo pointageInfo) {
        return R.get("demander_confirmation_suppression_pointage", pointageInfo.getDebut(), pointageInfo.getFin(), pointageInfo.getDuree());
    }

    public static String toastPointageComplet(PointageWrapper pointageWrapper) {
        return R.get("toast_pointage_complet", pointageWrapper.getHeureFin());
    }
    public static String toastPointagePartiel(PointageWrapper pointageWrapper) {
        return R.get("toast_pointage_partiel", pointageWrapper.getHeureDebut());
    }
    public static final String toast_pointage_insere = R.get("toast_pointage_insere");
    public static final String toast_pointage_supprime = R.get("toast_pointage_supprime");
    public static final String toast_pointage_modifie = R.get("toast_pointage_modifie");

    public static final String notification_titre = R.get("notification_titre");
    public static String notificationDebutTravail(PointageWrapper pointageWrapper) {
        return R.get("notification_debut_travail", pointageWrapper.getHeureDebut());
    }
    public static String notificationFinTravail(PointageWrapper pointageWrapper) {
        return R.get("notification_fin_travail", pointageWrapper.getHeureFin(), pointageWrapper.getDuree());
    }

    public static final String statut_pointage_aucun = R.get("statut_pointage_aucun");
    public static String statutPointageEncours(PointageWrapper pointageWrapper) {
        return R.get("statut_pointage_en_cours", pointageWrapper.getHeureDebut());
    }
    public static String statutPointageTermine(PointageWrapper pointageWrapper) {
        return R.get("statut_pointage_termine", pointageWrapper.getDuree());
    }

    public static final String mail_corps = R.get("mail_corps");
    public static final String mail_sujet = R.get("mail_sujet");
    public static final String mail_nom_piece_jointe = R.get("mail_nom_piece_jointe");

    public static final String erreur_date_debut_non_renseignee = R.get("erreur_date_debut_non_renseignee");
    public static final String erreur_date_fin_invalide = R.get("erreur_date_fin_invalide");
    public static final String erreur_plusieurs_pointage_en_cours = R.get("erreur_plusieurs_pointage_en_cours");
    public static final String erreur_export_indisponible = R.get("erreur_export_indisponible");

}
