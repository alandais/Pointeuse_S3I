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

package fr.s3i.pointeuse.presentation.commun;

import android.os.Handler;
import android.support.annotation.CallSuper;

import java.util.concurrent.TimeUnit;

import fr.s3i.pointeuse.domaine.communs.interactors.boundaries.out.OutBoundary;

/**
 * Created by Adrien on 24/07/2016.
 */
public abstract class Presenter<V extends Vue> implements OutBoundary {

    protected final V vue;

    protected final Handler handler = new Handler();

    protected Presenter(V vue) {
        this.vue = vue;
    }

    @CallSuper
    @Override
    public void onErreur(final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                vue.onError(message);
            }
        });
    }

    @Override
    public void toast(final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                vue.toast(message);
            }
        });
    }

    @Override
    public void executerFutur(Runnable action, long delai, TimeUnit unit) {
        vue.executerFutur(action, delai, unit);
    }

}
