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

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import fr.s3i.pointeuse.PointageApplication;
import fr.s3i.pointeuse.R;
import fr.s3i.pointeuse.domaine.communs.services.Service;

/**
 * Created by Adrien on 31/07/2016.
 */
public class WidgetService implements Service {

    private final PointageApplication application;
    private final ComponentName widget;
    private final WidgetServicePresenter presenter;
    private final WidgetServiceControleur controleur;

    public WidgetService(PointageApplication context) {
        application = context;
        widget = new ComponentName(context, PointerWidget.class);
        presenter = new WidgetServicePresenter(this);
        controleur = new WidgetServiceControleur(context.getContexte(), presenter);

        context.getContexte().enregistrerService(WidgetServiceControleur.class, controleur);
    }

    public void update(RemoteViews views) {
        AppWidgetManager.getInstance(application).updateAppWidget(widget, views);
    }

    public RemoteViews getRemoteViews() {
        return new RemoteViews(application.getPackageName(), R.layout.pointer_widget);
    }

    public void executerFutur(Runnable action, long delai, TimeUnit unit) {
        controleur.executerFutur(action, delai, unit);
    }

    public void toast(String message) {
        Toast.makeText(application, message, Toast.LENGTH_LONG).show();
    }

}
