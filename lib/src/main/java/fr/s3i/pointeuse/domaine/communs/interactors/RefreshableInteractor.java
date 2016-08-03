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

package fr.s3i.pointeuse.domaine.communs.interactors;

import java.util.concurrent.TimeUnit;

import fr.s3i.pointeuse.domaine.communs.interactors.boundaries.out.OutBoundary;
import fr.s3i.pointeuse.domaine.communs.services.logger.Log;

/**
 * Created by Adrien on 03/08/2016.
 */
public abstract class RefreshableInteractor<T extends OutBoundary> extends Interactor<T> {

    public static class Delay {

        public static final Delay NO_REFRESH = new Delay(-1, null);

        private final long delayQuantity;

        private final TimeUnit delayUnit;

        public Delay(long delayQuantity, TimeUnit delayUnit) {
            this.delayQuantity = delayQuantity;
            this.delayUnit = delayUnit;
        }

        public long getDelayQuantity() {
            return delayQuantity;
        }

        public TimeUnit getDelayUnit() {
            return delayUnit;
        }

        public boolean isActive() {
            return delayQuantity > 0 && delayUnit != null;
        }

    }

    public RefreshableInteractor(T out) {
        super(out);
    }

    public void refresh() {
        Delay delay = update();
        if (delay.isActive()) {
            out.executerFutur(new Runnable() {
                @Override
                public void run() {
                    refresh();
                }
            }, delay.getDelayQuantity(), delay.getDelayUnit());
        }
        else {
            Log.info(Log.STATE, "{0} ({1}) rafraichissement automatique ARRET", this.getClass().getSimpleName(), this);
        }
    }

    protected abstract Delay update();

}
