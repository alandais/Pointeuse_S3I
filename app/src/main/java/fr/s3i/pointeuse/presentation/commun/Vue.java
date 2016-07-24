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

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.s3i.pointeuse.PointageApplication;
import fr.s3i.pointeuse.domaine.communs.Contexte;

/**
 * Created by Adrien on 24/07/2016.
 */
public abstract class Vue<P extends Presenter, C extends Controleur> extends Fragment {

    private String titre;

    protected Contexte contexte;

    protected P presenter;

    protected C controleur;

    @CallSuper
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        contexte = ((PointageApplication)getActivity().getApplication()).getContexte();
    }

    @CallSuper
    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
        controleur = null;
    }

    @CallSuper
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        controleur.initialiser();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public abstract void onError(String message);

    public void onShowProgress() {
        // rien par défaut, pas obligatoire
    }

    public void onHideProgress() {
        // rien par défaut, pas obligatoire
    }

    protected final void setTitre(String titre) {
        this.titre = titre;
    }

    public final String getTitre() {
        return titre;
    }

}
