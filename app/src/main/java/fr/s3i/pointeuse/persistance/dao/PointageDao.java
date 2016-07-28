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

package fr.s3i.pointeuse.persistance.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import fr.s3i.pointeuse.domaine.pointages.entities.Pointage;
import fr.s3i.pointeuse.domaine.pointages.gateways.PointageRepository;
import fr.s3i.pointeuse.persistance.contrats.TablePointage;
import fr.s3i.pointeuse.persistance.database.DatabaseHelper;
import fr.s3i.pointeuse.persistance.mapper.PointageMapper;

/**
 * Created by Adrien on 23/07/2016.
 */
public class PointageDao implements Closeable, PointageRepository {

    private final DatabaseHelper database;
    private final TablePointage table;

    private final PointageMapper mapper = new PointageMapper();
    private final SQLiteDatabase db;

    public PointageDao(Context context) {
        this.database = new DatabaseHelper(context);
        db = database.getWritableDatabase();
        table = database.getTablePointage();
    }

    @Override
    public void persister(Pointage entity) {
        ContentValues values = mapper.mapper(entity);
        if (entity.getId() == null) {
            // insert
            long id = table.insert(db, values);
            entity.setId(id);
        } else {
            // update
            ContentValues filtre = mapper.getFiltre(entity);
            table.update(db, values, filtre);
        }
    }

    @Override
    public void supprimer(Long id) {
        ContentValues filtre = mapper.getFiltre(id);
        table.delete(db, filtre);
    }

    @Override
    public Pointage recuperer(Long id) {
        ContentValues filtre = mapper.getFiltre(id);
        Cursor resultat = table.select(db, filtre);
        resultat.moveToFirst();
        Pointage pointage = mapper.mapper(resultat);
        resultat.close();
        return pointage;
    }

    @Override
    public List<Pointage> recupererEntre(Date debut, Date fin) {
        String filtreDebut = mapper.formatDate(debut);
        String filtreFin = mapper.formatDate(fin);
        Cursor resultat = table.selectBetween(db, filtreDebut, filtreFin);
        resultat.moveToFirst();
        return mapper.mapperListe(resultat);
    }

    @Override
    public List<Pointage> recupererEnCours() {
        ContentValues filtre = mapper.getFiltreDernier();
        Cursor resultat = table.select(db, filtre);
        resultat.moveToFirst();
        List<Pointage> pointages = mapper.mapperListe(resultat);
        resultat.close();
        return pointages;
    }

    @Override
    public List<Pointage> recupererTout() {
        Cursor resultat = table.select(db, new ContentValues());
        List<Pointage> pointages = mapper.mapperListe(resultat);
        resultat.close();
        return pointages;
    }

    @Override
    public void close() throws IOException {
        db.close();
        database.close();
    }

}
