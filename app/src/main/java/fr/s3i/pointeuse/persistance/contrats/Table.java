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

import java.util.Map;

/**
 * Created by Adrien on 23/07/2016.
 */
public abstract class Table {

    // types
    public static final String TYPE_ID = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final String TYPE_TEXT = "TEXT";

    public abstract String getNom();

    public abstract String[] getColonnes();

    public abstract String[] getTypeColonnes();

    public void create(SQLiteDatabase db) {
        StringBuilder requeteCreate = new StringBuilder("CREATE TABLE ");
        requeteCreate.append(getNom());
        requeteCreate.append('(');
        for (int i = 0; i < getColonnes().length; i++) {
            requeteCreate.append(getColonnes()[i]);
            requeteCreate.append(' ');
            requeteCreate.append(getTypeColonnes()[i]);
            requeteCreate.append(',');
        }
        requeteCreate.setLength(requeteCreate.length()-1);
        requeteCreate.append(')');
        db.execSQL(requeteCreate.toString());
    }

    public void drop(SQLiteDatabase db) {
        StringBuilder requeteDrop = new StringBuilder("DROP TABLE IF EXISTS ");
        requeteDrop.append(getNom());
        db.execSQL(requeteDrop.toString());
    }

    public long insert(SQLiteDatabase db, ContentValues values) {
        return db.insert(getNom(), null, values);
    }

    public int delete(SQLiteDatabase db, ContentValues filters) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Object> filter : filters.valueSet()  ) {
            if (filter.getValue() != null) {
                builder.append(filter.getKey());
                builder.append('=');
                builder.append(filter.getValue());
            }
        }
        return db.delete(getNom(), builder.toString(), null);
    }

    public Cursor select(SQLiteDatabase db, ContentValues filter, String orderBy) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Object> entry : filter.valueSet()  ) {
            builder.append(entry.getKey());
            if (entry.getValue() != null) {
                builder.append('=');
                builder.append(entry.getValue());
            }
            else {
                builder.append(" IS NULL");
            }
        }
        return db.query(getNom(), getColonnes(), builder.toString(), null, null, null, orderBy);
    }

}
