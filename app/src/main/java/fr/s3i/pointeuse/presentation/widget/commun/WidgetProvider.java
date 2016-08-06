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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import java.util.concurrent.TimeUnit;

import fr.s3i.pointeuse.PointageApplication;
import fr.s3i.pointeuse.domaine.communs.Contexte;

/**
 * Created by Adrien on 06/08/2016.
 */
public abstract class WidgetProvider<P extends WidgetPresenter, C extends WidgetControler> extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        P presenter = getPresenter(context);
        C controler = getControler(context, presenter);

        onRefresh(context, presenter, controler);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        P presenter = getPresenter(context);
        C controler = getControler(context, presenter);

        onReceive(context, presenter, controler, intent);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);

        P presenter = getPresenter(context);
        C controler = getControler(context, presenter);

        onDisabled(context, presenter, controler);
    }

    public Intent getSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return intent;
    }

    public PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = getSelfIntent(context, action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    public void sendIntent(Context context, String action) {
        Intent intent = getSelfIntent(context, action);
        context.sendBroadcast(intent);
    }

    public void sendIntent(Context context, String action, int delay, TimeUnit unit) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent intent = getPendingSelfIntent(context, action);
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(delay, unit), intent);
    }

    public void cancelIntent(Context context, String action) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent intent = getPendingSelfIntent(context, action);
        alarmManager.cancel(intent);
    }

    protected Contexte getApplicationContexte(Context context) {
        PointageApplication application = (PointageApplication) context.getApplicationContext();
        return application.getContexte();
    }

    protected abstract P getPresenter(Context context);

    protected abstract C getControler(Context context, P presenter);

    protected abstract void onRefresh(Context context, P presenter, C controler);

    protected abstract void onDisabled(Context context, P presenter, C controler);

    protected abstract void onReceive(Context context, P presenter, C controler, Intent intent);

}
