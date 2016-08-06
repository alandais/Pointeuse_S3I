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

package fr.s3i.pointeuse.presentation.fragment.pointer;

import java.util.concurrent.TimeUnit;

import fr.s3i.pointeuse.domaine.pointages.interactors.inserer.boundaries.out.InsererOut;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.PointerOut;
import fr.s3i.pointeuse.domaine.pointages.interactors.recapitulatif.boundaries.out.model.PointageRecapitulatif;
import fr.s3i.pointeuse.domaine.pointages.interactors.statut.boundaries.out.model.PointageStatut;
import fr.s3i.pointeuse.domaine.pointages.interactors.recapitulatif.boundaries.out.RecapOut;
import fr.s3i.pointeuse.domaine.pointages.interactors.statut.boundaries.out.StatutOut;
import fr.s3i.pointeuse.presentation.fragment.commun.Presenter;
import fr.s3i.pointeuse.presentation.widget.pointer.PointerWidgetProvider;

/**
 * Created by Adrien on 23/07/2016.
 */
public class PointerPresenter extends Presenter<PointerVue> implements PointerOut, InsererOut, RecapOut, StatutOut {

    public PointerPresenter(PointerVue vue) {
        super(vue);
    }

    @Override
    public void onPointageStatutUpdate(final PointageStatut pointage) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                vue.updatePointageStatut(pointage.getStatut());
            }
        });
    }

    @Override
    public void onPointageRecapitulatifRecalcule(final PointageRecapitulatif pointage) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                vue.updatePointageRecapJour(pointage.getDureeTotaleJour());
                vue.updatePointageRecapSemaine(pointage.getDureeTotaleSemaine());
            }
        });
    }

    @Override
    public void onPointageDemarre() {
        PointerWidgetProvider.lancerRefreshAuto(vue.getContext());
    }

    @Override
    public void onPointageTermine() {
        PointerWidgetProvider.arreterRefreshAuto(vue.getContext());
    }

    @Override
    public void onPointageRecapitulatifRecalculAutomatiqueDemande(final int delay, final TimeUnit unit) {
        vue.getControleur().executerFutur(new Runnable() {
            @Override
            public void run() {
                vue.getControleur().lancerCalculRecapitulatifAutomatique();
            }
        }, delay, unit);
    }
}
