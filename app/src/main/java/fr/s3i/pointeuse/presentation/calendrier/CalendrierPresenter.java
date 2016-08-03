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

package fr.s3i.pointeuse.presentation.calendrier;

import fr.s3i.pointeuse.domaine.pointages.entities.Pointage;
import fr.s3i.pointeuse.domaine.pointages.interactors.lister.boundaries.out.ListerOut;
import fr.s3i.pointeuse.domaine.pointages.interactors.lister.boundaries.out.model.PointageInfoListe;
import fr.s3i.pointeuse.domaine.pointages.interactors.modifier.boundaries.out.ModifierOut;
import fr.s3i.pointeuse.domaine.pointages.interactors.supprimer.boundaries.out.SupprimerOut;
import fr.s3i.pointeuse.presentation.commun.Presenter;

/**
 * Created by Adrien on 30/07/2016.
 */
public class CalendrierPresenter extends Presenter<CalendrierVue> implements ListerOut, ModifierOut, SupprimerOut {

    protected CalendrierPresenter(CalendrierVue vue) {
        super(vue);
    }

    @Override
    public void onPointageInfoListeUpdate(final PointageInfoListe pointageInfoListe) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                vue.onDureeTotaleUpdate(pointageInfoListe.getDureeTotale());
                vue.onPointageListeUpdate(pointageInfoListe.getListePointageInfo());
            }
        });
    }

    @Override
    public void refreshPointageInfoListe() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                vue.updateListeCalendrier();
            }
        });
    }

    @Override
    public void modifier(final Pointage pointage) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                vue.onPointageModification(pointage);
            }
        });
    }

}
