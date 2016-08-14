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

package fr.s3i.pointeuse.presentation.widget.pointer;

import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.concurrent.TimeUnit;

import fr.s3i.pointeuse.R;
import fr.s3i.pointeuse.domaine.pointages.Chaines;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.PointerOut;
import fr.s3i.pointeuse.domaine.pointages.interactors.recapitulatif.boundaries.out.RecapOut;
import fr.s3i.pointeuse.domaine.pointages.interactors.recapitulatif.boundaries.out.model.PointageRecapitulatif;
import fr.s3i.pointeuse.presentation.widget.commun.WidgetPresenter;

/**
 * Created by Adrien on 04/08/2016.
 */
public class PointerWidgetPresenter extends WidgetPresenter implements PointerOut, RecapOut {

    public PointerWidgetPresenter(Context context) {
        super(context, PointerWidgetProvider.class, R.layout.pointer_widget);
    }

    @Override
    public void onPointageRecapitulatifRecalcule(PointageRecapitulatif pointage) {
        Log.d(PointerWidgetProvider.class.getSimpleName(), "Presenter : mise a jour du statut");

        RemoteViews views = getRemoteViews();
        views.setTextViewText(R.id.txtWidgetStatut, pointage.isEnCours() ? Chaines.en_cours : Chaines.realise);
        views.setTextViewText(R.id.txtWidgetLibelleJour, Chaines.jour);
        views.setTextViewText(R.id.txtWidgetLibelleSemaine, Chaines.semaine);
        views.setTextViewText(R.id.txtWidgetValeurJour, pointage.getDureeTotaleJour());
        views.setTextViewText(R.id.txtWidgetValeurSemaine, pointage.getDureeTotaleSemaine());
        updateRemoteViews(views);
    }

    @Override
    public void onPointageDemarre() {
        PointerWidgetProvider.lancerRefreshAuto(context);
    }

    @Override
    public void onPointageTermine() {
        PointerWidgetProvider.arreterRefreshAuto(context);
    }

    @Override
    public void onPointageRecapitulatifRecalculAutomatiqueDemande(int delay, TimeUnit unit) {
        PointerWidgetProvider.planifierRefreshAuto(context, delay, unit);
    }

}
