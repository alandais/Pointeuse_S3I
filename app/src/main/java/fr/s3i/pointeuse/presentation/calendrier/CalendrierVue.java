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

package fr.s3i.pointeuse.presentation.calendrier;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.s3i.pointeuse.R;
import fr.s3i.pointeuse.domaine.pointages.interactors.calendrier.boundaries.out.CalendrierOut;
import fr.s3i.pointeuse.presentation.commun.Vue;

/**
 * Created by Adrien on 30/07/2016.
 */
public class CalendrierVue extends Vue<CalendrierPresenter, CalendrierControleur> {

    public static CalendrierVue getInstance(String titre) {
        CalendrierVue vue = new CalendrierVue();
        vue.setTitre(titre);
        return vue;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CalendrierPresenter(this);
        contexte.enregistrerService(CalendrierOut.class, presenter);
        controleur = new CalendrierControleur(contexte);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendrier_vue, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // récupération de l'état de la vue en cas de changement de configuration (rotation de l'écran par exemple)
        if (savedInstanceState != null) {
            // TODO restaure état vue
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (this.getView() != null) {
            // TODO sauvegarde état vue
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        contexte.detruireService(CalendrierOut.class);
    }

}
