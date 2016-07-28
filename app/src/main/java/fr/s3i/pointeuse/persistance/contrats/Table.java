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
import android.support.annotation.NonNull;

import java.util.Map;

/**
 * Created by Adrien on 23/07/2016.
 */
public abstract class Table {

    // types
    public static final String TYPE_ID = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final String TYPE_TEXT = "TEXT";

    public abstract String getNom();

    public abstract String[] getColonnesCle();

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
        requeteCreate.setLength(requeteCreate.length() - 1);
        requeteCreate.append(')');
        db.execSQL(requeteCreate.toString());
    }

    public void drop(SQLiteDatabase db) {
        StringBuilder requeteDrop = new StringBuilder("DROP TABLE IF EXISTS ");
        requeteDrop.append(getNom());
        db.execSQL(requeteDrop.toString());
    }

    public long insert(SQLiteDatabase db, ContentValues values) {
        long id = db.insert(getNom(), null, values);
        if (id == -1) {
            // TODO meilleure gestion des exceptions, il faut que ça puisse remonter dans l'interactor (PersitanceException ?)
            throw new IllegalStateException();
        }
        return id;
    }

    public int update(SQLiteDatabase db, ContentValues values, ContentValues filter) {
        return db.update(getNom(), values, getWhere(filter), null);
    }

    public int delete(SQLiteDatabase db, ContentValues filter) {
        return db.delete(getNom(), getWhere(filter), null);
    }

    public Cursor select(SQLiteDatabase db, ContentValues filter, String orderBy) {
        return select(db, getWhere(filter), orderBy);
    }

    public Cursor select(SQLiteDatabase db, String where, String orderBy) {
        return db.query(getNom(), getColonnes(), where, null, null, null, orderBy);
    }

    public int count(SQLiteDatabase db, ContentValues filter) {
        Cursor curseur = db.query(getNom(), getColonnesCle(), getWhere(filter), null, null, null, null);
        int count = curseur.getCount();
        curseur.close();
        return count;
    }

    @NonNull
    private String getWhere(ContentValues filter) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Object> entry : filter.valueSet()) {
            builder.append(entry.getKey());
            if (entry.getValue() != null) {
                builder.append("='");
                builder.append(entry.getValue());
                builder.append("'");
            } else {
                builder.append(" IS NULL");
            }
        }
        return builder.toString();
    }

}
