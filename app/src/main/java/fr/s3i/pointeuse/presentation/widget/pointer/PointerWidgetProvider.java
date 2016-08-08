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
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.concurrent.TimeUnit;

import fr.s3i.pointeuse.R;
import fr.s3i.pointeuse.presentation.widget.commun.WidgetProvider;

/**
 * Implementation of App Widget functionality.
 */
public class PointerWidgetProvider extends WidgetProvider<PointerWidgetPresenter, PointerWidgetControler> {

    private static final String INTENT_ACTION_LANCER_REFRESH_AUTO = "LANCER_REFRESH_AUTO";
    private static final String INTENT_ACTION_REFRESH = "REFRESH";
    private static final String INTENT_ACTION_POINTER = "POINTER";

    public static void planifierRefreshAuto(Context context, int delay, TimeUnit unit) {
        WidgetProvider.sendIntent(context, INTENT_ACTION_LANCER_REFRESH_AUTO, delay, unit, PointerWidgetProvider.class);
    }

    public static void lancerRefreshAuto(Context context) {
        WidgetProvider.sendIntent(context, INTENT_ACTION_LANCER_REFRESH_AUTO, PointerWidgetProvider.class);
    }

    public static void arreterRefreshAuto(Context context) {
        WidgetProvider.cancelIntent(context, INTENT_ACTION_LANCER_REFRESH_AUTO, PointerWidgetProvider.class);
    }

    public static void refresh(Context context) {
        WidgetProvider.sendIntent(context, INTENT_ACTION_REFRESH, PointerWidgetProvider.class);
    }

    public static void pointer(Context context) {
        WidgetProvider.sendIntent(context, INTENT_ACTION_POINTER, PointerWidgetProvider.class);
    }

    @Override
    protected PointerWidgetPresenter getPresenter(Context context) {
        return new PointerWidgetPresenter(context);
    }

    @Override
    protected PointerWidgetControler getControler(Context context, PointerWidgetPresenter presenter) {
        return new PointerWidgetControler(getApplicationContexte(context), presenter);
    }

    @Override
    protected void onRefresh(Context context, PointerWidgetPresenter presenter, PointerWidgetControler controler) {
        Log.d("PointerWidgetProvider", "--- onRefresh");

        RemoteViews views = presenter.getRemoteViews();
        views.setOnClickPendingIntent(R.id.monbouttonwidget, WidgetProvider.getPendingIntent(context, INTENT_ACTION_POINTER, getClass()));
        presenter.updateRemoteViews(views);

        controler.lancerCalculRecapitulatifAutomatique();
    }

    @Override
    protected void onDisabled(Context context, PointerWidgetPresenter presenter, PointerWidgetControler controler) {
        Log.d("PointerWidgetProvider", "--- onDisabled");
        arreterRefreshAuto(context);
    }

    @Override
    protected void onReceive(Context context, PointerWidgetPresenter presenter, PointerWidgetControler controler, Intent intent) {
        Log.d("PointerWidgetProvider", "--- onReceive(intent = " + intent + ")");

        if (INTENT_ACTION_LANCER_REFRESH_AUTO.equals(intent.getAction())) {
            controler.lancerCalculRecapitulatifAutomatique();
        }

        if (INTENT_ACTION_POINTER.equals(intent.getAction())) {
            controler.pointer();
        }

        if (INTENT_ACTION_REFRESH.equals(intent.getAction())) {
            controler.recalculerRecapitulatif();
        }
    }

}

