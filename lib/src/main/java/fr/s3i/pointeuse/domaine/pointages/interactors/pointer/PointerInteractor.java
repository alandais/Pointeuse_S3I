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

package fr.s3i.pointeuse.domaine.pointages.interactors.pointer;

import java.util.Date;

import fr.s3i.pointeuse.domaine.communs.R;
import fr.s3i.pointeuse.domaine.communs.gateways.ToastSystem;
import fr.s3i.pointeuse.domaine.pointages.interactors.communs.boundaries.out.model.PointageInfo;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.in.PointerIn;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.PointerOut;

/**
 * Created by Adrien on 19/07/2016.
 */
public class PointerInteractor extends fr.s3i.pointeuse.domaine.communs.interactors.Interactor<PointerOut> implements PointerIn {

    private final fr.s3i.pointeuse.domaine.pointages.gateways.PointageRepository repository;

    private final fr.s3i.pointeuse.domaine.pointages.interactors.communs.boundaries.out.translator.PointageInfoTranslator translator;

    private final ToastSystem toastSystem;

    private final fr.s3i.pointeuse.domaine.communs.gateways.NotificationSystem notificationSystem;

    public PointerInteractor(fr.s3i.pointeuse.domaine.communs.Contexte contexte) {
        super(contexte.getService(PointerOut.class));
        this.repository = contexte.getService(fr.s3i.pointeuse.domaine.pointages.gateways.PointageRepository.class);
        this.translator = contexte.getService(fr.s3i.pointeuse.domaine.pointages.interactors.communs.boundaries.out.translator.PointageInfoTranslator.class);
        this.toastSystem = contexte.getService(ToastSystem.class);
        this.notificationSystem = contexte.getService(fr.s3i.pointeuse.domaine.communs.gateways.NotificationSystem.class);
    }

    @Override
    public void pointer() {
        fr.s3i.pointeuse.domaine.pointages.entities.Pointage pointage = repository.recupererDernier();
        if (pointage != null && pointage.getFin() == null) {
            pointage.setFin(new Date());
        } else {
            pointage = new fr.s3i.pointeuse.domaine.pointages.entities.Pointage(new Date());
        }

        PointageInfo pointageInfo = persister(pointage);
        if (pointageInfo != null) {
            if (pointageInfo.isComplete()) {
                toastSystem.notifier(R.get("toast_pointage_complet", pointageInfo.getHeureFin()));
                notificationSystem.notifier(R.get("notification_titre"), R.get("notification_fin_travail", pointageInfo.getHeureFin(), pointageInfo.getDuree()));
            } else {
                toastSystem.notifier(R.get("toast_pointage_partiel", pointageInfo.getHeureDebut()));
                notificationSystem.notifier(R.get("notification_titre"), R.get("notification_debut_travail", pointageInfo.getHeureDebut()));
            }
        }
    }

    @Override
    public void inserer(Date debut, Date fin, String commentaire) {
        fr.s3i.pointeuse.domaine.pointages.entities.Pointage pointage = new fr.s3i.pointeuse.domaine.pointages.entities.Pointage(debut);
        pointage.setFin(fin);
        pointage.setCommentaire(commentaire);

        PointageInfo pointageInfo = persister(pointage);
        if (pointageInfo != null) {
            toastSystem.notifier(R.get("toast_pointage_insere"));
        }
    }

    private PointageInfo persister(fr.s3i.pointeuse.domaine.pointages.entities.Pointage pointage) {
        PointageInfo pointageInfo = null;
        String erreur = pointage.getErrorMessage();
        if (erreur == null) {
            repository.persister(pointage);
            pointageInfo = translator.translate(pointage);
            out.onPointageInsere(pointageInfo);
        } else {
            out.onError(erreur);
            toastSystem.notifier(erreur);
        }
        return pointageInfo;
    }

}
