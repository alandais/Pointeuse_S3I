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

package fr.s3i.pointeuse.presentation.widget.test;

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
public class TestWidgetProvider extends WidgetProvider<TestWidgetPresenter, TestWidgetControler> {

    private static final String INTENT_ACTION_WAKEUP = "WAKEUP";

    @Override
    protected void onRefresh(Context context, TestWidgetPresenter presenter, TestWidgetControler controler) {
        Log.e("TestWidget", "--- onRefresh");

        RemoteViews views = presenter.getRemoteViews();
        views.setOnClickPendingIntent(R.id.monbouttonwidget, getPendingSelfIntent(context, INTENT_ACTION_WAKEUP));
        presenter.updateRemoteViews(views);

        controler.refresh();
    }

    @Override
    protected void onReceive(Context context, TestWidgetPresenter presenter, TestWidgetControler controler, Intent intent) {
        Log.e("TestWidget", "--- onReceive(intent = " + intent + ")");

        if (INTENT_ACTION_WAKEUP.equals(intent.getAction())) {
            controler.autoRefresh();
        }
    }

    @Override
    protected void onDisabled(Context context, TestWidgetPresenter presenter, TestWidgetControler controler) {
        Log.e("TestWidget", "--- onDisabled");
        cancelWakeUp(context);
    }

    @Override
    protected TestWidgetPresenter getPresenter(Context context) {
        return new TestWidgetPresenter(context);
    }

    @Override
    protected TestWidgetControler getControler(Context context, TestWidgetPresenter presenter) {
        return new TestWidgetControler(presenter, getApplicationContexte(context));
    }

    public static void wakeUp(Context context) {
        new TestWidgetProvider().sendIntent(context, INTENT_ACTION_WAKEUP);
    }

    public static void wakeUpIn(Context context, int delay, TimeUnit unit) {
        new TestWidgetProvider().sendIntent(context, INTENT_ACTION_WAKEUP, delay, unit);
    }

    public static void cancelWakeUp(Context context) {
        new TestWidgetProvider().cancelIntent(context, INTENT_ACTION_WAKEUP);
    }

}

