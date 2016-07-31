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

/**
 * Expose les chaînes de caractères constantes destinées à être affichées à l'écran,
 * et les méthodes constantes permettant de générer des chaînes dynamiques destinées à être
 * affichées à l'écran.
 */
public final class Chaines {

    private Chaines() {
        // constructeur privé
    }

    public static final String copyright = R.get("copyright");

    public static final String oui = R.get("oui");
    public static final String non = R.get("non");
    public static final String ok = R.get("ok");
    public static final String annuler = R.get("annuler");

    public static final String demander_date_debut = R.get("demander_date_debut");
    public static final String demander_date_fin = R.get("demander_date_fin");

    public static String demandeConfirmationSuppression(PointageInfo pointageInfo) {
        return R.get("demande_confirmation_suppression_pointage", pointageInfo.getDebut(), pointageInfo.getFin(), pointageInfo.getDuree());
    }

}
