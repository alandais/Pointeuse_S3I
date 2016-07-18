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

package fr.s3i.pointeuse.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import fr.s3i.pointeuse.business.pointages.gateways.PointagePreferences;

/**
 * Created by Adrien on 18/07/2016.
 */
public class Preferences implements PointagePreferences {

    private final SharedPreferences preferences;

    private static final String PREF_DATE_FORMAT = "";
    private static final String PREF_DELAI_FORMAT = "";
    private static final String PREF_PRECISION = "";
    private static final String PREF_ARRONDI = "";
    private static final String PREF_EXPORT_SEPARATEUR = "";
    private static final String PREF_EXPORT_DESTINATAIRE = "";
    private static final String PREF_TEMPS_TRAVAIL_MINIMUM_PAR_SEMAINE_EN_HEURES = "";
    private static final String PREF_TEMPS_TRAVAIL_MAXIMUM_PAR_SEMAINE_EN_HEURES = "";

    public Preferences(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public String getDateFormat() {
        return preferences.getString(PREF_DATE_FORMAT, "yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public DelaiFormat getDelaiFormat() {
        return DelaiFormat.valueOf(preferences.getString(PREF_DELAI_FORMAT, DelaiFormat.HEURE_MINUTE.name()));
    }

    @Override
    public boolean getPrecision() {
        return preferences.getBoolean(PREF_PRECISION, true);
    }

    @Override
    public Arrondi getArrondi() {
        return Arrondi.valueOf(preferences.getString(PREF_ARRONDI, Arrondi.AUCUN.name()));
    }

    @Override
    public String getExportSeparateur() {
        return preferences.getString(PREF_EXPORT_SEPARATEUR, ";");
    }

    @Override
    public String getExportDestinataire() {
        return preferences.getString(PREF_EXPORT_DESTINATAIRE, null);
    }

    @Override
    public int getTempsTravailMinimumParSemaineEnHeures() {
        return preferences.getInt(PREF_TEMPS_TRAVAIL_MINIMUM_PAR_SEMAINE_EN_HEURES, 35);
    }

    @Override
    public int getTempsTravailMaximumParSemaineEnHeures() {
        return preferences.getInt(PREF_TEMPS_TRAVAIL_MAXIMUM_PAR_SEMAINE_EN_HEURES, 35);
    }
}
