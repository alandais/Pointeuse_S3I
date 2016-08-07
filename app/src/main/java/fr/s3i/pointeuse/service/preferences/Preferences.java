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

package fr.s3i.pointeuse.service.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import fr.s3i.pointeuse.R;
import fr.s3i.pointeuse.domaine.pointages.gateways.PointagePreferences;

/**
 * Created by Adrien on 18/07/2016.
 */
public class Preferences implements PointagePreferences {

    private final Context context;

    private final SharedPreferences preferences;

    private static final String PREF_DATE_FORMAT = "DATE_FORMAT";
    private static final String PREF_HEURE_FORMAT = "HEURE_FORMAT";
    //private static final String PREF_DELAI_FORMAT = "DELAI_FORMAT";
    private static final String PREF_PRECISION = "PRECISION";
    private static final String PREF_ARRONDI = "ARRONDI";
    private static final String PREF_EXPORT_SEPARATEUR = "EXPORT_SEPARATEUR";
    private static final String PREF_EXPORT_DESTINATAIRE = "EXPORT_DESTINATAIRE";
    private static final String PREF_TEMPS_TRAVAIL_MINIMUM_PAR_SEMAINE_EN_HEURES = "TEMPS_TRAVAIL_MINIMUM_PAR_SEMAINE_EN_HEURES";
    private static final String PREF_TEMPS_TRAVAIL_MAXIMUM_PAR_SEMAINE_EN_HEURES = "TEMPS_TRAVAIL_MAXIMUM_PAR_SEMAINE_EN_HEURES";

    public Preferences(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public String getDateFormat() {
        return preferences.getString(PREF_DATE_FORMAT, "yyyy-MM-dd HH:mm");
    }

    @Override
    public String getHeureFormat() {
        return preferences.getString(PREF_HEURE_FORMAT, "HH:mm");
    }

    @Override
    public DelaiFormat getDelaiFormat() {
        return DelaiFormat.valueOf(preferences.getString(context.getString(R.string.pref_delai_format), DelaiFormat.HEURE_MINUTE.name()));
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
