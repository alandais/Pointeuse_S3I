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

package fr.s3i.pointeuse.presentation.fragment.test;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.pointages.interactors.test.EmiterInteractor;
import fr.s3i.pointeuse.domaine.pointages.interactors.test.ReceiverInteractor;
import fr.s3i.pointeuse.domaine.pointages.interactors.test.in.EmiterIn;
import fr.s3i.pointeuse.domaine.pointages.interactors.test.in.ReceiverIn;

/**
 * Created by Adrien on 05/08/2016.
 */
public class TestControleur implements EmiterIn, ReceiverIn {

    private final EmiterIn emiter;
    private final ReceiverIn receiver;

    private ScheduledExecutorService tacheDeFond;

    public TestControleur(TestPresenter out, Contexte contexte) {
        this.emiter = new EmiterInteractor(out, contexte);
        this.receiver = new ReceiverInteractor(out, contexte);
    }

    @Override
    public void initialiser() {
        receiver.initialiser();
        emiter.initialiser();
        tacheDeFond = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public void close() throws IOException {
        emiter.close();
        receiver.close();
        tacheDeFond.shutdownNow();
    }

    @Override
    public void refresh() {
        tacheDeFond.execute(new Runnable() {
            @Override
            public void run() {
                receiver.refresh();
            }
        });
    }

    @Override
    public void autoRefresh() {
        tacheDeFond.execute(new Runnable() {
            @Override
            public void run() {
                receiver.autoRefresh();
            }
        });
    }

    @Override
    public void lancer() {
        tacheDeFond.execute(new Runnable() {
            @Override
            public void run() {
                emiter.lancer();
            }
        });
    }

    public void autoRefresh(int delay, TimeUnit unit) {
        tacheDeFond.schedule(new Runnable() {
            @Override
            public void run() {
                receiver.autoRefresh();
            }
        }, delay, unit);
    }

}
