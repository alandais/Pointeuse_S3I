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

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.communs.interactors.Interactor;
import fr.s3i.pointeuse.domaine.communs.services.BusService;
import fr.s3i.pointeuse.domaine.pointages.interactors.test.in.ReceiverIn;
import fr.s3i.pointeuse.domaine.pointages.interactors.test.out.ReceiverOut;
import fr.s3i.pointeuse.domaine.pointages.services.BusPointage;

/**
 * Created by Adrien on 06/08/2016.
 */
public class ReceiverInteractor extends Interactor<ReceiverOut> implements ReceiverIn, BusService.Listener {

    private final BusPointage bus;

    public static class WakeupEvent extends BusService.BaseEvent<Void> {

        public WakeupEvent(Object originator) {
            super(originator, WakeupEvent.class.getSimpleName());
        }

    }

    public static class RefreshEvent extends BusService.BaseEvent<Void> {

        public RefreshEvent(Object originator) {
            super(originator, WakeupEvent.class.getSimpleName());
        }

    }

    public ReceiverInteractor(ReceiverOut out, Contexte contexte) {
        super(out);
        this.bus = contexte.getService(BusPointage.class);
    }

    @Override
    public void initialiser() {
        super.initialiser();
        bus.subscribe(this);
    }

    @Override
    public void close() throws IOException {
        super.close();
        bus.unsuscribe(this);
    }

    @Override
    public boolean onEvent(BusService.Event event) {
        if (event instanceof WakeupEvent) {
            autoRefresh();
        }
        if (event instanceof RefreshEvent) {
            update();
        }
        return true;
    }

    @Override
    public void autoRefresh() {
        if (update()) {
            out.onAutoRefresh(1, TimeUnit.SECONDS);
        }
    }

    @Override
    public void refresh() {
        update();
    }

    private boolean update() {
        long value = System.currentTimeMillis();
        out.onRefresh(value);
        return !(""+value).endsWith("0");
    }
}
