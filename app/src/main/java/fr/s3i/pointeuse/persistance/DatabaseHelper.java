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

package fr.s3i.pointeuse.persistance;

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

/**
 * Created by Adrien on 17/07/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper implements PointageRepository {

    private static final String DATABASE_NAME = "POINTAGE";
    private static final int DATABASE_VERSION = 4;

    public static final String ID = "_ID";
    public static final String DATE_DEBUT = "DATE_DEBUT";
    public static final String DATE_FIN = "DATE_FIN";
    public static final String COMMENTAIRE = "COMMENTAIRE";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //android.util.Log.w("Constants", "onCreate");
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);

        db.execSQL("CREATE TABLE " + DATABASE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DATE_DEBUT + " TEXT," +
                DATE_FIN + " TEXT," +
                COMMENTAIRE + " TEXT)");

        db.execSQL("DROP INDEX IF EXISTS INDEX_" + DATABASE_NAME);
        String requete = "CREATE INDEX INDEX_" + DATABASE_NAME +
                " ON " + DATABASE_NAME + "(" + DATE_DEBUT + ")";

        db.execSQL(requete);

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //android.util.Log.w("Constants", "Maj de la base");
        if (oldVersion == 3 && newVersion == 4) {
            db.execSQL("DROP TABLE TABLE_PAUSE");
        }

    }

    @Override
    public void persister(Pointage entity) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(DATE_DEBUT, formatDate(entity.getDebut()));
        initialValues.put(DATE_FIN, formatDate(entity.getFin()));
        initialValues.put(COMMENTAIRE, entity.getCommentaire());
        entity.setId(db.insert(DATABASE_NAME, null, initialValues));
    }

    @Override
    public void supprimer(Long id) {
        db.delete(DATABASE_NAME, ID + "=" + id, null);
    }

    @Override
    public List<Pointage> recupererTout() {
        return requeter(null, null);
    }

    @Override
    public Pointage recuperer(Long id) {
        return requeter(ID + "=?", id.toString()).get(0);
    }

    @Override
    public Pointage recupererDernier() {
        return requeter(null, null).get(0);
    }

    @Override
    public List<Pointage> recuperer(Date debut, Date fin) {
        String formatDebut = formatDate(debut);
        String formatFin = formatDate(fin);
        return requeter(DATE_DEBUT + " BETWEEN ? AND ?", formatDebut, formatFin);
    }

    private List<Pointage> requeter(String where, String... values) {
        Cursor curseur = db.query(DATABASE_NAME,
                new String[]{ID, DATE_DEBUT, DATE_FIN, COMMENTAIRE},
                where,
                values,
                null,
                null,
                DATE_DEBUT);
        return curseurToList(curseur);
    }

    private List<Pointage> curseurToList(Cursor curseur) {
        List<Pointage> resultat = new ArrayList<>();
        while (curseur.moveToNext()) {
            resultat.add(parsePointage(curseur));
        }
        return resultat;
    }

    private Pointage parsePointage(Cursor curseur) {
        Long id = curseur.getLong(0);
        Date dateDebut = parseDate(curseur.getString(1));
        Date dateFin = parseDate(curseur.getString(2));
        String commentaire = curseur.getString(3);
        return getPointage(id, dateDebut, dateFin, commentaire);
    }

    private Date parseDate(String format) {
        Date date = null;
        try {
            date = "".equals(format) ? null : DATE_FORMAT.parse(format);
        } catch (ParseException e) {
            // rien, c'est normal pour les dates non renseignées
        }
        return date;
    }

    private String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    private Pointage getPointage(Long id, Date dateDebut, Date dateFin, String commentaire) {
        Pointage pointage = new Pointage(dateDebut);
        pointage.setFin(dateFin);
        pointage.setCommentaire(commentaire);
        pointage.setId(id);
        return pointage;
    }

}
