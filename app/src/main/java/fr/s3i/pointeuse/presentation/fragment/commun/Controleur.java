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

package fr.s3i.pointeuse.presentation.fragment.commun;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import fr.s3i.pointeuse.domaine.communs.interactors.Interactor;
import fr.s3i.pointeuse.domaine.communs.interactors.boundaries.in.InBoundary;

/**
 * Created by Adrien on 24/07/2016.
 */
public abstract class Controleur implements Closeable, InBoundary {

    protected ScheduledExecutorService tacheDeFond;

    private final Interactor[] interactors;

    public Controleur(Interactor... interactors) {
        this.interactors = interactors;
    }

    public void initialiser() {
        tacheDeFond = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
        for (Interactor interactor : interactors) {
            interactor.initialiser();
        }
    }

    public void executerFutur(Runnable action, long delai, TimeUnit unit) {
        tacheDeFond.schedule(action, delai, unit);
    }

    @Override
    public void close() throws IOException {
        tacheDeFond.shutdownNow();
        for (Interactor interactor : interactors) {
            interactor.close();
        }
    }

    protected <T extends Interactor> T getInteracteur(Class<T> type) {
        for (Interactor interactor : interactors) {
            if (interactor.getClass().isAssignableFrom(type)) {
                return (T) interactor;
            }
        }
        return null;
    }

}
