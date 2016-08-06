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

package fr.s3i.pointeuse.domaine.pointages.interactors.test;

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.communs.interactors.Interactor;
import fr.s3i.pointeuse.domaine.communs.services.BusService;
import fr.s3i.pointeuse.domaine.pointages.interactors.test.in.EmiterIn;
import fr.s3i.pointeuse.domaine.pointages.interactors.test.out.EmiterOut;
import fr.s3i.pointeuse.domaine.pointages.services.BusPointage;

/**
 * Created by Adrien on 05/08/2016.
 */
public class EmiterInteractor extends Interactor<EmiterOut> implements EmiterIn {

    private final BusPointage bus;

    public EmiterInteractor(EmiterOut out, Contexte contexte) {
        super(out);
        this.bus = contexte.getService(BusPointage.class);
    }

    @Override
    public void initialiser() {
        super.initialiser();
        lancer();
    }

    @Override
    public void lancer() {
        // envoyer un message WAKEUP
        BusService.Event wakeupEvent = new ReceiverInteractor.WakeupEvent(this);
        bus.post(wakeupEvent);
        out.onLancement();
    }

}
