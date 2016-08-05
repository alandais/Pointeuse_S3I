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

package fr.s3i.pointeuse.presentation.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import fr.s3i.pointeuse.R;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.PointerOut;
import fr.s3i.pointeuse.domaine.pointages.interactors.recapitulatif.boundaries.out.RecapOut;
import fr.s3i.pointeuse.domaine.pointages.interactors.recapitulatif.boundaries.out.model.PointageRecapitulatif;

/**
 * Created by Adrien on 04/08/2016.
 */
public class PointerWidgetPresenter implements PointerOut, RecapOut {

    private final Context context;

    private final ComponentName widget;

    public PointerWidgetPresenter(Context context) {
        this.context = context;
        this.widget = new ComponentName(context, PointerWidget.class);
    }

    @Override
    public void onPointageRecapitulatifUpdate(PointageRecapitulatif pointage) {
        Log.d(PointerWidget.class.getSimpleName(), "Presenter : mise a jour du statut");
        RemoteViews views = getRemoteViews();
        StringBuilder recap = new StringBuilder();
        recap.append("Réalisé: ");
        recap.append(pointage.getDureeTotaleJour());
        recap.append('\n');
        recap.append("Sem.: ");
        recap.append(pointage.getDureeTotaleSemaine());
        //views.setTextViewText(R.id.monTextWidget, pointage.getRecapitulatif());
        views.setTextViewText(R.id.monTextWidget, recap.toString());
        update(views);
    }

    @Override
    public void onErreur(String message) {
        toast(message);
    }

    @Override
    public void toast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void executerFutur(Runnable action, long delai, TimeUnit unit) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent intent = PointerWidget.getPendingSelfIntent(context, "Refresh");
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(delai, unit), intent);
    }

    public RemoteViews getRemoteViews() {
        return new RemoteViews(context.getPackageName(), R.layout.pointer_widget);
    }

    public void update(RemoteViews views) {
        AppWidgetManager.getInstance(context).updateAppWidget(widget, views);
    }

}
