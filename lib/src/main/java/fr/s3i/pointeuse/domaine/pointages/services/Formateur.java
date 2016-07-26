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

package fr.s3i.pointeuse.domaine.pointages.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.communs.services.Service;
import fr.s3i.pointeuse.domaine.pointages.gateways.PointagePreferences;

/**
 * Created by Adrien on 26/07/2016.
 */
public class Formateur implements Service {

    private final PointagePreferences preferences;

    public Formateur(Contexte contexte) {
        this.preferences = contexte.getService(PointagePreferences.class);
    }

    public String formatDuree(long duree) {
        String result;
        switch (preferences.getDelaiFormat()) {
            case DIXIEME_HEURE:
                result = String.format(Locale.getDefault(), "%02.1f", duree / 3600.00f);
                break;
            case HEURE_MINUTE:
                result = String.format(Locale.getDefault(), "%02dh%02d", duree / 3600, (duree % 3600) / 60);
                break;
            case JOUR_HEURE_MINUTE:
                result = String.format(Locale.getDefault(), "%dj. %02dh%02d", duree / 86400, (duree % 86400) / 3600, (duree % 3600) / 60);
                break;
            default:
                result = Long.toString(duree);
                break;
        }
        return result;
    }

    public String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(preferences.getDateFormat(), Locale.getDefault());
        return sdf.format(date);
    }

    public String formatHeure(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(preferences.getHeureFormat(), Locale.getDefault());
        return sdf.format(date);
    }

}
