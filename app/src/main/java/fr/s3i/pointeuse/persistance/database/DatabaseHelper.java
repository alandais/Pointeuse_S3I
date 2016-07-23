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

package fr.s3i.pointeuse.persistance.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.s3i.pointeuse.business.pointages.entities.Pointage;
import fr.s3i.pointeuse.business.pointages.gateways.PointageRepository;
import fr.s3i.pointeuse.persistance.contrats.TablePause;
import fr.s3i.pointeuse.persistance.contrats.TablePointage;

/**
 * Created by Adrien on 17/07/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;

    private static final TablePointage tablePointage = new TablePointage();
    private static final TablePause tablePause = new TablePause();

    public DatabaseHelper(Context context) {
        super(context, "pointeuse.db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        tablePointage.drop(db);
        tablePointage.create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 3 && newVersion == 4) {
            tablePause.drop(db);
        }
    }

    public TablePointage getTablePointage() {
        return tablePointage;
    }

}
