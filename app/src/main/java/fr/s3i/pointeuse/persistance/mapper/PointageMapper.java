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

package fr.s3i.pointeuse.persistance.mapper;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.s3i.pointeuse.business.pointages.entities.Pointage;
import fr.s3i.pointeuse.persistance.contrats.TablePointage;

/**
 * Created by Adrien on 23/07/2016.
 */
public class PointageMapper extends Mapper<Pointage> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public ContentValues mapper(Pointage pointage) {
        ContentValues retour = new ContentValues();
        retour.put(TablePointage.COL_ID, pointage.getId().toString());
        retour.put(TablePointage.COL_DATE_DEBUT, formatDate(pointage.getDebut()));
        retour.put(TablePointage.COL_DATE_FIN, formatDate(pointage.getFin()));
        retour.put(TablePointage.COL_COMMENTAIRE, pointage.getCommentaire());
        return retour;
    }

    @Override
    public Pointage mapper(Cursor curseur) {
        Long id = curseur.getLong(0);
        Date dateDebut = parseDate(curseur.getString(1));
        Date dateFin = parseDate(curseur.getString(2));
        String commentaire = curseur.getString(3);
        return getPointage(id, dateDebut, dateFin, commentaire);
    }

    public ContentValues getFiltre(long id) {
        ContentValues retour = new ContentValues();
        retour.put(TablePointage.COL_ID, String.valueOf(id));
        return retour;
    }

    public ContentValues getFiltre(Date date) {
        ContentValues retour = new ContentValues();
        retour.put(TablePointage.COL_DATE_DEBUT, formatDate(date));
        return retour;
    }

    public ContentValues getFiltreDernier() {
        ContentValues retour = new ContentValues();
        retour.putNull(TablePointage.COL_DATE_FIN);
        return retour;
    }

    @NonNull
    private Pointage getPointage(Long id, Date dateDebut, Date dateFin, String commentaire) {
        Pointage pointage = new Pointage(dateDebut);
        pointage.setId(id);
        pointage.setFin(dateFin);
        pointage.setCommentaire(commentaire);
        return pointage;
    }

    private String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    private Date parseDate(String format) {
        if (format == null || "".equals(format)) {
            return null;
        }
        try {
            return DATE_FORMAT.parse(format);
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }

}
