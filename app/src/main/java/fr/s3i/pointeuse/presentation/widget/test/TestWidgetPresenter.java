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
import android.widget.RemoteViews;

import java.util.concurrent.TimeUnit;

import fr.s3i.pointeuse.R;
import fr.s3i.pointeuse.domaine.pointages.interactors.test.out.EmiterOut;
import fr.s3i.pointeuse.domaine.pointages.interactors.test.out.ReceiverOut;
import fr.s3i.pointeuse.presentation.widget.commun.WidgetPresenter;

/**
 * Created by Adrien on 05/08/2016.
 */
public class TestWidgetPresenter extends WidgetPresenter implements EmiterOut, ReceiverOut {

    public TestWidgetPresenter(Context context) {
        super(context, TestWidgetProvider.class, R.layout.pointer_widget);
    }

    @Override
    public void onRefresh(long timestamp) {
        RemoteViews views = getRemoteViews();
        views.setTextViewText(R.id.txtWidgetStatut, "Timestamp : " + timestamp);
        updateRemoteViews(views);
    }

    @Override
    public void onAutoRefresh(int delay, TimeUnit unit) {
        TestWidgetProvider.wakeUpIn(context, delay, unit);
    }

    @Override
    public void onLancement() {
        // rien (utile pour gérer une action nécessaire au lancement du traitement, dans le fragement par exemple pour lancer le widget)
    }

}
