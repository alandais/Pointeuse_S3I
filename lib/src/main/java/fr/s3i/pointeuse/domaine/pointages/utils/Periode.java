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

package fr.s3i.pointeuse.domaine.pointages.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import fr.s3i.pointeuse.domaine.communs.services.logger.Log;
import fr.s3i.pointeuse.domaine.pointages.Chaines;

/**
 * Created by Adrien on 24/07/2016.
 */
public enum Periode {

    // @formatter:off
    ANNEE(Calendar.DAY_OF_YEAR, Calendar.YEAR),
    MOIS(Calendar.DAY_OF_MONTH, Calendar.MONTH),
    SEMAINE(Calendar.DAY_OF_WEEK, Calendar.WEEK_OF_YEAR),
    JOUR(Calendar.HOUR_OF_DAY, Calendar.DAY_OF_MONTH);
    // @formatter:on

    private final int toReset;
    private final int toIncrease;

    Periode(int toReset, int toIncrease) {
        this.toReset = toReset;
        this.toIncrease = toIncrease;
    }

    public Date getDebutPeriode(Date reference) {
        Calendar calendar = getCalendar(reference);
        reset(calendar);
        Log.verbose(Log.DATAFLOW, "getDebutPeriode({0}, {1}) == {2}", this, reference, calendar.getTime());
        return calendar.getTime();
    }

    public Date getFinPeriode(Date reference) {
        Calendar calendar = getCalendar(reference);
        reset(calendar);
        increase(calendar);
        Log.verbose(Log.DATAFLOW, "getFinPeriode({0}, {1}) == {2}", this, reference, calendar.getTime());
        return calendar.getTime();
    }

    public String toString(Date reference) {
        switch(this) {
            case ANNEE:
                return Chaines.periodeAnnee(reference);
            case MOIS:
                return Chaines.periodeMois(reference);
            case SEMAINE:
                return Chaines.periodeSemaine(reference);
            case JOUR:
                return Chaines.periodeJour(reference);
            default:
                throw new IllegalStateException("Periode inconnue : " + this.name());
        }
    }

    private void reset(Calendar calendar) {
        if (toReset == Calendar.DAY_OF_WEEK) {
            calendar.set(toReset, calendar.getFirstDayOfWeek());
        } else {
            calendar.set(toReset, calendar.getActualMinimum(toReset));
        }
    }

    private void increase(Calendar calendar) {
        calendar.add(toIncrease, 1);
    }

    private Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

}
