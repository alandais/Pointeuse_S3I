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

package fr.s3i.pointeuse;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fr.s3i.pointeuse.domaine.pointages.entities.Pointage;
import fr.s3i.pointeuse.persistance.dao.PointageDao;
import fr.s3i.pointeuse.persistance.database.DatabaseHelper;

import static org.junit.Assert.*;

/**
 * Created by Adrien on 29/07/2016.
 */
@RunWith(AndroidJUnit4.class)
public class PointageRepositoryTest {

    private PointageDao repository;

    private DatabaseHelper database;

    @Before
    public void setUp() throws Exception {
        InstrumentationRegistry.getTargetContext().deleteDatabase(DatabaseHelper.DATABASE_NAME);
        repository = new PointageDao(InstrumentationRegistry.getTargetContext());
        database = new DatabaseHelper(InstrumentationRegistry.getTargetContext());
    }

    @After
    public void tearDown() throws Exception {
        repository.close();
    }

    @Test
    public void testPersistance() {
        Pointage attendu = creerPointage(new Date(), new Date(), "Commentaire");

        repository.persister(attendu);
        assertNotNull(attendu.getId());

        Pointage reel = repository.recuperer(attendu.getId());
        assertPointageEquals(attendu, reel);
    }

    @Test
    public void testListerEntre() throws ParseException {
        Date date1 = creerDate("2016-07-29 23:59:59");
        Date date2 = creerDate("2016-07-30 00:00:00");
        Date date3 = creerDate("2016-07-30 12:00:00");
        Date date4 = creerDate("2016-07-30 23:59:59");
        Date date5 = creerDate("2016-07-31 00:00:00");

        Pointage pointage1 = creerPointage(date1, date1, "pointage1");
        Pointage pointage2 = creerPointage(date2, date2, "pointage2");
        Pointage pointage3 = creerPointage(date3, date3, "pointage3");
        Pointage pointage4 = creerPointage(date4, date4, "pointage4");
        Pointage pointage5 = creerPointage(date5, date5, "pointage5");

        repository.persister(pointage1);
        repository.persister(pointage2);
        repository.persister(pointage3);
        repository.persister(pointage4);
        repository.persister(pointage5);

        List<Pointage> attendus = new ArrayList<>();
        attendus.add(pointage2);
        attendus.add(pointage3);
        attendus.add(pointage4);

        List<Pointage> reels = repository.recupererEntre(creerDate("2016-07-30 00:00:00"), creerDate("2016-07-31 00:00:00"));
        assertPointageEquals(attendus, reels);
    }

    @Test
    public void testSupprimer() throws ParseException {
        Date date1 = creerDate("2016-07-29 23:59:59");
        Date date2 = creerDate("2016-07-30 00:00:00");
        Date date3 = creerDate("2016-07-30 12:00:00");
        Date date4 = creerDate("2016-07-30 23:59:59");
        Date date5 = creerDate("2016-07-31 00:00:00");

        Pointage pointage1 = creerPointage(date1, date1, "pointage1");
        Pointage pointage2 = creerPointage(date2, date2, "pointage2");
        Pointage pointage3 = creerPointage(date3, date3, "pointage3");
        Pointage pointage4 = creerPointage(date4, date4, "pointage4");
        Pointage pointage5 = creerPointage(date5, date5, "pointage5");

        repository.persister(pointage1);
        repository.persister(pointage2);
        repository.persister(pointage3);
        repository.persister(pointage4);
        repository.persister(pointage5);
        repository.supprimer(pointage3.getId());

        List<Pointage> attendus = new ArrayList<>();
        attendus.add(pointage1);
        attendus.add(pointage2);
        attendus.add(pointage4);
        attendus.add(pointage5);

        List<Pointage> reels = repository.recupererTout();
        assertPointageEquals(attendus, reels);
    }

    @Test
    public void testListerEnCours() throws ParseException {
        Date date1 = creerDate("2016-07-29 23:59:59");
        Date date2 = creerDate("2016-07-30 00:00:00");
        Date date3 = creerDate("2016-07-30 12:00:00");
        Date date4 = creerDate("2016-07-30 23:59:59");
        Date date5 = creerDate("2016-07-31 00:00:00");

        Pointage pointage1 = creerPointage(date1, date1, "pointage1");
        Pointage pointage2 = creerPointage(date2, date2, "pointage2");
        Pointage pointage3 = creerPointage(date3, null, "pointage3");
        Pointage pointage4 = creerPointage(date4, null, "pointage4");
        Pointage pointage5 = creerPointage(date5, date5, "pointage5");

        repository.persister(pointage1);
        repository.persister(pointage2);
        repository.persister(pointage3);
        repository.persister(pointage4);
        repository.persister(pointage5);

        List<Pointage> attendus = new ArrayList<>();
        attendus.add(pointage3);
        attendus.add(pointage4);

        List<Pointage> reels = repository.recupererEnCours();
        assertPointageEquals(attendus, reels);
    }

    private void assertDateEquals(Date attendue, Date reelle) {
        if (attendue == null || reelle == null) {
            assertEquals(attendue, reelle);
        }
        else {
            assertTrue(attendue.getTime() - reelle.getTime() < 1000);
        }
    }

    private void assertPointageEquals(Pointage attendu, Pointage reel) {
        assertDateEquals(attendu.getDebut(), reel.getDebut());
        assertDateEquals(attendu.getFin(), reel.getFin());
        assertEquals(attendu.getCommentaire(), reel.getCommentaire());
    }

    private void assertPointageEquals(List<Pointage> attendus, List<Pointage> reels) {
        assertEquals(attendus.size(), reels.size());
        for(int i = 0; i < attendus.size(); i++) {
            assertPointageEquals(attendus.get(i), reels.get(i));
        }
    }

    private Date creerDate(String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.parse(format);
    }

    private Pointage creerPointage(Date debut, Date fin, String commentaire) {
        Pointage pointage = new Pointage();
        pointage.setDebut(debut);
        pointage.setFin(fin);
        pointage.setCommentaire(commentaire);
        return pointage;
    }

}
