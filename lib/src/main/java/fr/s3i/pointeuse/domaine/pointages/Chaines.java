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

import java.util.Date;

import fr.s3i.pointeuse.domaine.communs.R;
import fr.s3i.pointeuse.domaine.pointages.operations.requeter.model.PointageInfo;
import fr.s3i.pointeuse.domaine.pointages.services.model.PointageWrapper;
import fr.s3i.pointeuse.domaine.pointages.utils.Periode;

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

    public static final String oui = R.get("libelle_oui");
    public static final String non = R.get("libelle_non");
    public static final String ok = R.get("libelle_ok");
    public static final String annuler = R.get("libelle_annuler");
    public static final String jour = R.get("libelle_jour");
    public static final String semaine = R.get("libelle_semaine");
    public static final String mois = R.get("libelle_mois");
    public static final String annee = R.get("libelle_annee");
    public static final String ce_jour = R.get("libelle_ce(tte)_jour");
    public static final String cette_semaine = R.get("libelle_ce(tte)_semaine");
    public static final String ce_mois = R.get("libelle_ce(tte)_mois");
    public static final String cette_annee = R.get("libelle_ce(tte)_annee");
    public static final String realise = R.get("libelle_realise");
    public static final String en_cours = R.get("libelle_en_cours");

    public static String periodeAnnee(Date reference) {
        return R.get("periode_annee", reference);
    }
    public static String periodeMois(Date reference) {
        return R.get("periode_mois", reference);
    }
    public static String periodeSemaine(Date reference) {
        return R.get("periode_semaine", reference);
    }
    public static String periodeJour(Date reference) {
        return R.get("periode_jour", reference);
    }

    public static final String demander_date_debut = R.get("demander_date_debut");
    public static final String demander_date_fin = R.get("demander_date_fin");
    public static String demanderConfirmationSuppression(PointageInfo pointageInfo) {
        return R.get("demander_confirmation_suppression_pointage", pointageInfo.getDebut(), pointageInfo.getFin(), pointageInfo.getDuree());
    }
    public static final String demander_tagnfc_inconnu_titre = R.get("demander_tagnfc_inconnu_titre");
    public static final String demander_tagnfc_inconnu_message = R.get("demander_tagnfc_inconnu_message");

    public static String toastPointageComplet(PointageWrapper pointageWrapper) {
        return R.get("toast_pointage_complet", pointageWrapper.getHeureFin());
    }
    public static String toastPointagePartiel(PointageWrapper pointageWrapper) {
        return R.get("toast_pointage_partiel", pointageWrapper.getHeureDebut());
    }
    public static final String toast_pointage_insere = R.get("toast_pointage_insere");
    public static final String toast_pointage_supprime = R.get("toast_pointage_supprime");
    public static final String toast_pointage_modifie = R.get("toast_pointage_modifie");
    public static final String toast_export_termine = R.get("toast_export_termine");
    public static final String toast_tagnfc_initialise = R.get("toast_tagnfc_initialise");

    public static final String notification_titre = R.get("notification_titre");
    public static String notificationDebutTravail(PointageWrapper pointageWrapper) {
        return R.get("notification_debut_travail", pointageWrapper.getHeureDebut());
    }
    public static String notificationFinTravail(PointageWrapper pointageWrapper) {
        return R.get("notification_fin_travail", pointageWrapper.getHeureFin());
    }

    public static final String pointage_statut_aucun = R.get("pointage_statut_aucun");
    public static String pointageStatutEncours(PointageWrapper pointageWrapper) {
        return R.get("pointage_statut_en_cours", pointageWrapper.getHeureDebut());
    }
    public static String pointageStatutTermine(PointageWrapper pointageWrapper) {
        return R.get("pointage_statut_termine", pointageWrapper.getDuree());
    }

    public static final String mail_corps = R.get("mail_corps");
    public static String mailSujet(Periode periode, Date reference) {
        return R.get("mail_sujet", periode.toString(reference));
    }
    public static String mailNomPieceJointe(Periode periode, Date reference) {
        return R.get("mail_nom_piece_jointe", periode.toString(reference));
    }

    public static final String erreur_date_debut_non_renseignee = R.get("erreur_date_debut_non_renseignee");
    public static final String erreur_date_fin_invalide = R.get("erreur_date_fin_invalide");
    public static final String erreur_plusieurs_pointage_en_cours = R.get("erreur_plusieurs_pointage_en_cours");
    public static final String erreur_export_indisponible = R.get("erreur_export_indisponible");
    public static final String erreur_export_email_invalide = R.get("erreur_export_email_invalide");
    public static final String erreur_tagnfc_creation = R.get("erreur_tagnfc_creation");
    public static final String erreur_tagnfc_ecriture = R.get("erreur_tagnfc_ecriture");

}
