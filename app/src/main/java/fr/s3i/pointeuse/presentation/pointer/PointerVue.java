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
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import fr.s3i.pointeuse.R;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.PointerOut;
import fr.s3i.pointeuse.presentation.commun.Vue;

public class PointerVue extends Vue<PointerPresenter, PointerControleur> implements View.OnClickListener {

    private static final String STATE_POINTAGE_RAPIDE_TEXTE = "POINTAGE_RAPIDE_TEXTE";
    private static final String STATE_POINTAGE_EN_COURS_TEXTE = "POINTAGE_EN_COURS_TEXTE";

    public static PointerVue getInstance(String titre) {
        PointerVue vue = new PointerVue();
        vue.setTitre(titre);
        return vue;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PointerPresenter(this);
        contexte.enregistrerService(PointerOut.class, presenter);
        controleur = new PointerControleur(contexte);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // récupération de l'état de la vue en cas de changement de configuration (rotation de l'écran par exemple)
        if (savedInstanceState != null) {
            updateInfoPointageEnCours(savedInstanceState.getString(STATE_POINTAGE_EN_COURS_TEXTE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (this.getView() != null) {
            TextView textRapide = (TextView) this.getView().findViewById(R.id.txtPointageRapide);
            outState.putString(STATE_POINTAGE_RAPIDE_TEXTE, textRapide.getText().toString());
            TextView textEnCours = (TextView) this.getView().findViewById(R.id.txtPointageEnCours);
            outState.putString(STATE_POINTAGE_EN_COURS_TEXTE, textEnCours.getText().toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        contexte.detruireService(PointerOut.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pointer_vue, container, false);

        Button b = (Button) view.findViewById(R.id.btnPointer);
        b.setOnClickListener(this);

        b = (Button) view.findViewById(R.id.btnInserer);
        b.setOnClickListener(this);

        return view;
    }

    public void onPointerPressed() {
        controleur.pointer();
    }

    public void onInsererPressed() {
        // TODO inputs utilisateur.
        controleur.inserer(null, null, null);
    }

    @Override
    public void onError(String message) {
        // TODO améliorer la présentation des erreurs
        toast(message);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPointer:
                onPointerPressed();
                break;
            case R.id.btnInserer:
                onInsererPressed();
                break;
        }
    }

    public void updateInfoPointageRapide(String texte) {
        if (this.getView() != null) {
            TextView textRapide = (TextView) this.getView().findViewById(R.id.txtPointageRapide);
            textRapide.setText(texte);
        }
    }

    public void updateInfoPointageInsere(String texte) {
        // TODO
    }

    public void updateInfoPointageEnCours(String texte) {
        if (this.getView() != null) {
            TextView textEnCours = (TextView) this.getView().findViewById(R.id.txtPointageEnCours);
            textEnCours.setText(texte);
        }
    }

}
