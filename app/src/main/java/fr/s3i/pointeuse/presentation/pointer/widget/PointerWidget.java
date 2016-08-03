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

package fr.s3i.pointeuse.presentation.pointer.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.concurrent.TimeUnit;

import fr.s3i.pointeuse.PointageApplication;
import fr.s3i.pointeuse.R;
import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.PointerInteractor;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.PointerOut;

/**
 * Implementation of App Widget functionality.
 */
public class PointerWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        PointageApplication application = (PointageApplication) context.getApplicationContext();
        Contexte contexte = application.getContexte();

        WidgetServiceControleur controleur = contexte.getService(WidgetServiceControleur.class);
        controleur.initialiser();

        WidgetService service = contexte.getService(WidgetService.class);
        RemoteViews views = service.getRemoteViews();
        PendingIntent intent = getPendingSelfIntent(context, "Pointer");
        views.setOnClickPendingIntent(R.id.monbouttonwidget, intent);
        service.update(views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if ("Pointer".equals(intent.getAction())) {
            PointageApplication application = (PointageApplication) context.getApplicationContext();
            Contexte contexte = application.getContexte();
            PointerInteractor pointer = new PointerInteractor(contexte, new PointerOut() {
                @Override
                public void onErreur(String message) {
                    // rien
                }

                @Override
                public void executerFutur(Runnable action, long delai, TimeUnit unit) {
                    // rien
                }

                @Override
                public void toast(String message) {
                    //rien
                }
            });
            pointer.pointer();
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

}

