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

package fr.s3i.pointeuse.persistance.contrats;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Adrien on 23/07/2016.
 */
public class TablePointage extends Table {

    public static final String TABLE = "POINTAGE";

    // colonnes
    public static final String COL_ID = "_ID";
    public static final String COL_DATE_DEBUT = "DATE_DEBUT";
    public static final String COL_DATE_FIN = "DATE_FIN";
    public static final String COL_COMMENTAIRE = "COMMENTAIRE";

    // structure table
    public static final String[] COLONNES = {COL_ID, COL_DATE_DEBUT, COL_DATE_FIN, COL_COMMENTAIRE};
    public static final String[] TYPE_COL = {TYPE_ID, TYPE_TEXT, TYPE_TEXT, TYPE_TEXT};

    @Override
    public String getNom() {
        return TABLE;
    }

    @Override
    public String[] getColonnesCle() {
        return new String[]{COL_ID};
    }

    @Override
    public String[] getColonnes() {
        return COLONNES;
    }

    @Override
    public String[] getTypeColonnes() {
        return TYPE_COL;
    }

    public Cursor select(SQLiteDatabase db, ContentValues filter) {
        return super.select(db, filter, COL_DATE_DEBUT);
    }

    public Cursor selectBetween(SQLiteDatabase db, String dateDebutPeriode, String dateFinPeriode) {
        StringBuilder where = new StringBuilder();
        where.append(COL_DATE_DEBUT);
        where.append(" BETWEEN '");
        where.append(dateDebutPeriode);
        where.append("' AND '");
        where.append(dateFinPeriode);
        where.append('\'');
        return super.select(db, where.toString(), COL_DATE_DEBUT);
    }

}
