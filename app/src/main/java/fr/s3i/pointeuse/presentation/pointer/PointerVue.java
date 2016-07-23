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

package fr.s3i.pointeuse.presentation.pointer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import fr.s3i.pointeuse.PointageApplication;
import fr.s3i.pointeuse.R;
import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.pointages.interactors.communs.boundaries.out.model.PointageInfo;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.PointerOut;

public class PointerVue extends Fragment implements View.OnClickListener {

    private Contexte contexte;

    private PointerPresenter presenter;

    private PointerControleur controleur;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PointerPresenter(this);
        contexte = ((PointageApplication)getActivity().getApplication()).getContexte();
        contexte.enregistrerService(PointerOut.class, presenter);
        controleur = new PointerControleur(contexte, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        contexte.detruireService(PointerOut.class);
        presenter = null;
        controleur = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pointer_vue, container, false);

        Button b = (Button) view.findViewById(R.id.btnPointer);
        b.setOnClickListener(this);

        b = (Button) view.findViewById(R.id.btnInserer);
        b.setOnClickListener(this);

        return view;
    }

    public void onPointerPressed(View v) {
        controleur.pointer();
    }

    public void onInsererPressed(View v) {
        // TODO inputs utilisateur.
        controleur.inserer(null, null, null);
    }

    public void onUpdateEnCours(String texte) {
        if (this.getView() != null) {
            TextView textEnCours = (TextView) this.getView().findViewById(R.id.txtEnCours);
            textEnCours.setText(texte);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPointer:
                onPointerPressed(view);
                break;
            case R.id.btnInserer:
                onInsererPressed(view);
                break;
        }
    }

}
