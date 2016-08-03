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

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.communs.gateways.NotificationSystem;
import fr.s3i.pointeuse.domaine.communs.interactors.Interactor;
import fr.s3i.pointeuse.domaine.pointages.Chaines;
import fr.s3i.pointeuse.domaine.pointages.entities.Pointage;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.in.PointerIn;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.PointerOut;
import fr.s3i.pointeuse.domaine.pointages.operations.EnregistrerPointage;
import fr.s3i.pointeuse.domaine.pointages.operations.LirePointageRecent;
import fr.s3i.pointeuse.domaine.pointages.services.BusPointage;
import fr.s3i.pointeuse.domaine.pointages.services.model.PointageWrapper;
import fr.s3i.pointeuse.domaine.pointages.services.model.PointageWrapperFactory;

/**
 * Created by Adrien on 19/07/2016.
 */
public class PointerInteractor extends Interactor<PointerOut> implements PointerIn {

    private final BusPointage bus;

    private final NotificationSystem notificationSystem;

    private final PointageWrapperFactory pointageWrapperFactory;

    private final EnregistrerPointage enregistrerPointage;

    private final LirePointageRecent lirePointageRecent;

    public PointerInteractor(Contexte contexte, PointerOut out) {
        super(out);
        this.bus = contexte.getService(BusPointage.class);
        this.notificationSystem = contexte.getService(NotificationSystem.class);
        this.pointageWrapperFactory = contexte.getService(PointageWrapperFactory.class);
        this.enregistrerPointage = new EnregistrerPointage(contexte, out);
        this.lirePointageRecent = new LirePointageRecent(contexte, out);
    }

    @Override
    public void pointer() {
        Pointage pointage = lirePointageRecent.executer();
        if (pointage != null) {
            // pointage en cours
            pointage.setFin(new Date());
        } else {
            // nouveau pointage
            pointage = new Pointage();
            pointage.setDebut(new Date());
        }

        if(enregistrerPointage.executer(pointage)) {
            PointageWrapper pointageWrapper = pointageWrapperFactory.getPointageWrapper(pointage);
            if (pointageWrapper.isTermine()) {
                out.toast(Chaines.toastPointageComplet(pointageWrapper));
                notificationSystem.notifier(Chaines.notification_titre, Chaines.notificationFinTravail(pointageWrapper));
            } else {
                bus.post(this, BusPointage.LANCER_RECAP_REFRESH); // lancement du refresh auto du récapitulatif
                out.toast(Chaines.toastPointagePartiel(pointageWrapper));
                notificationSystem.notifier(Chaines.notification_titre, Chaines.notificationDebutTravail(pointageWrapper));
            }
        }
    }

}
