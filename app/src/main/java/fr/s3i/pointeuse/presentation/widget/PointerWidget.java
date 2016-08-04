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
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import fr.s3i.pointeuse.PointageApplication;
import fr.s3i.pointeuse.R;
import fr.s3i.pointeuse.domaine.communs.Contexte;

/**
 * Implementation of App Widget functionality.
 */
public class PointerWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        PointageApplication application = (PointageApplication) context.getApplicationContext();
        Contexte contexte = application.getContexte();

        PointerWidgetPresenter presenter = new PointerWidgetPresenter(context);
        PointerWidgetControleur controleur = new PointerWidgetControleur(contexte, presenter);

        controleur.refresh();

        RemoteViews views = presenter.getRemoteViews();
        PendingIntent intent = getPendingSelfIntent(context, "Pointer");
        views.setOnClickPendingIntent(R.id.monbouttonwidget, intent);
        presenter.update(views);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent intent = PointerWidget.getPendingSelfIntent(context, "Refresh");
        alarmManager.cancel(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if ("Pointer".equals(intent.getAction())) {
            PointageApplication application = (PointageApplication) context.getApplicationContext();
            Contexte contexte = application.getContexte();
            PointerWidgetPresenter presenter = new PointerWidgetPresenter(context);
            PointerWidgetControleur controleur = new PointerWidgetControleur(contexte, presenter);
            controleur.pointer();
        }

        if ("Refresh".equals(intent.getAction())) {
            PointageApplication application = (PointageApplication) context.getApplicationContext();
            Contexte contexte = application.getContexte();
            PointerWidgetPresenter presenter = new PointerWidgetPresenter(context);
            PointerWidgetControleur controleur = new PointerWidgetControleur(contexte, presenter);
            controleur.refresh();
        }
    }

    public static PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, PointerWidget.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

}

