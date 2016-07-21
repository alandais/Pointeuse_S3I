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

package fr.s3i.pointeuse.business.pointages.services;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import fr.s3i.pointeuse.business.communs.Contexte;
import fr.s3i.pointeuse.business.pointages.entities.Pointage;
import fr.s3i.pointeuse.business.pointages.gateways.PointagePreferences;

/**
 * Created by Adrien on 19/07/2016.
 */
public class PointageService {

    private final PointagePreferences preferences;

    public PointageService(Contexte contexte) {
        this.preferences = contexte.getService(PointagePreferences.class);
    }

    public long calculDuree(Pointage pointage) {
        if (pointage.getDebut() == null) {
            return 0;
        } else if (pointage.getFin() == null) {
            return calculDuree(pointage.getDebut(), new Date());
        } else {
            return calculDuree(pointage.getDebut(), pointage.getFin());
        }
    }

    public long calculDuree(Date debut, Date fin) {
        Calendar calendar = Calendar.getInstance();
        if (!preferences.getPrecision()) {
            calendar.setTime(debut);
            calendar.set(Calendar.SECOND, 0);
            debut = calendar.getTime();
            calendar.setTime(fin);
            calendar.set(Calendar.SECOND, 0);
            fin = calendar.getTime();
        }
        return TimeUnit.MILLISECONDS.toSeconds(fin.getTime() - debut.getTime());
    }

    public long calculDureeTotale(Collection<Pointage> pointages) {
        long dureeTotale = 0;
        for (Pointage pointage : pointages) {
            dureeTotale += calculDuree(pointage);
        }
        return gererArrondi(dureeTotale);
    }

    public long gererArrondi(long dureeTotale) {
        // TODO : gestion de l'arrondi dans le calcul
        switch (preferences.getArrondi()) {
            case AUCUN:
                break;
            case _10_MINUTES:
                break;
            case _15_MINUTES:
                break;
            case _30_MINUTES:
                break;
            case _1HEURE:
                break;
            default:
                break;
        }
        return dureeTotale;
    }

    public String formatDuree(long duree) {
        String result;
        switch (preferences.getDelaiFormat()) {
            case DIXIEME_HEURE:
                result = String.format("%02.1f", duree / 3600.00f);
                break;
            case HEURE_MINUTE:
                result = String.format("%02dh%02d", duree / 3600, (duree % 3600) / 60);
                break;
            case JOUR_HEURE_MINUTE:
                result = String.format("%dj. %02dh%02d", duree / 86400, (duree % 86400) / 3600, (duree % 3600) / 60);
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
        SimpleDateFormat sdf = new SimpleDateFormat(preferences.getDateFormat());
        return sdf.format(date);
    }

    public String formatHeure(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(preferences.getHeureFormat());
        return sdf.format(date);
    }

}
