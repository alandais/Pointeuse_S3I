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

package fr.s3i.pointeuse.presentation.widget.commun;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import fr.s3i.pointeuse.domaine.communs.interactors.boundaries.out.OutBoundary;

/**
 * Created by Adrien on 06/08/2016.
 */
public abstract class WidgetPresenter implements OutBoundary {

    protected final Context context;
    private final ComponentName widget;
    private final int layoutId;

    public WidgetPresenter(Context context, Class<? extends WidgetProvider> providerClass, @LayoutRes int layoutId) {
        this.context = context;
        this.widget = new ComponentName(context, providerClass);
        this.layoutId = layoutId;
    }

    @NonNull
    public RemoteViews getRemoteViews() {
        return new RemoteViews(context.getPackageName(), layoutId);
    }

    public void updateRemoteViews(RemoteViews views) {
        AppWidgetManager.getInstance(context).updateAppWidget(widget, views);
    }

    @Override
    public void onErreur(String message) {
        // par défaut, un toast
        toast(message);
    }

    @Override
    public void toast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public final void executerFutur(Runnable action, long delai, TimeUnit unit) {
        // le widget est stateless et ne peut pas implémenter cette méthode
    }

}
