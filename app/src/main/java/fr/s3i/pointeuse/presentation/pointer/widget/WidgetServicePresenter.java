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

import android.widget.RemoteViews;

import java.util.concurrent.TimeUnit;

import fr.s3i.pointeuse.R;
import fr.s3i.pointeuse.domaine.communs.entities.CasUtilisationInfo;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.PointerOut;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.model.PointageRecapitulatif;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.model.PointageStatut;

/**
 * Created by Adrien on 31/07/2016.
 */
public class WidgetServicePresenter implements PointerOut {

    private final WidgetService service;

    public WidgetServicePresenter(WidgetService service) {
        this.service = service;
    }

    @Override
    public void onPointageStatutUpdate(PointageStatut pointage) {
        // le statut n'est pas affiché sur le widget
    }

    @Override
    public void onPointageRecapitulatifUpdate(PointageRecapitulatif pointage) {
        StringBuilder recap = new StringBuilder();
        recap.append("Réalisé: ");
        recap.append(pointage.getDureeTotaleJour());
        recap.append('\n');
        recap.append("Sem.: ");
        recap.append(pointage.getDureeTotaleSemaine());

        RemoteViews views = service.getRemoteViews();
        views.setTextViewText(R.id.monTextWidget, recap.toString());
        service.update(views);
    }

    @Override
    public void onDemarrer(CasUtilisationInfo info) {
        // rien
    }

    @Override
    public void onErreur(String message) {
        // rien
    }

    @Override
    public void executerFutur(Runnable action, long delai, TimeUnit unit) {
        service.executerFutur(action, delai, unit);
    }

    @Override
    public void toast(String message) {
        service.toast(message);
    }

}
