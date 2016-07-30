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

import java.util.Calendar;
import java.util.Date;

import fr.s3i.pointeuse.R;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.PointerOut;
import fr.s3i.pointeuse.presentation.commun.Vue;
import fr.s3i.pointeuse.presentation.dialogue.PointageInsererDialog;
import fr.s3i.pointeuse.presentation.dialogue.SelectionListener;

public class PointerVue extends Vue<PointerPresenter, PointerControleur> implements View.OnClickListener {

    private static final String STATE_POINTAGE_STATUT = "POINTAGE_STATUT";
    private static final String STATE_POINTAGE_RECAP_JOUR = "POINTAGE_RECAP_JOUR";
    private static final String STATE_POINTAGE_RECAP_SEMAINE = "POINTAGE_RECAP_SEMAINE";

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // récupération de l'état de la vue en cas de changement de configuration (rotation de l'écran par exemple)
        if (savedInstanceState != null) {
            updatePointageStatut(savedInstanceState.getString(STATE_POINTAGE_STATUT));
            updatePointageRecapJour(savedInstanceState.getString(STATE_POINTAGE_RECAP_JOUR));
            updatePointageRecapSemaine(savedInstanceState.getString(STATE_POINTAGE_RECAP_SEMAINE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (this.getView() != null) {
            TextView textStatut = (TextView) this.getView().findViewById(R.id.txtPointageStatut);
            outState.putString(STATE_POINTAGE_STATUT, textStatut.getText().toString());
            TextView textRecapJour = (TextView) this.getView().findViewById(R.id.txtPointageRecapJour);
            outState.putString(STATE_POINTAGE_RECAP_JOUR, textRecapJour.getText().toString());
            TextView textRecapSemaine = (TextView) this.getView().findViewById(R.id.txtPointageRecapSemaine);
            outState.putString(STATE_POINTAGE_RECAP_SEMAINE, textRecapSemaine.getText().toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        contexte.detruireService(PointerOut.class);
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

    public void onPointerPressed() {
        controleur.pointer();
    }

    public void onInsererPressed() {
        PointageInsererDialog test = new PointageInsererDialog();
        test.afficher(getActivity(), "Choisir l'heure de début", new SelectionListener<PointageInsererDialog.Resultat>() {
            @Override
            public void onSelected(PointageInsererDialog.Resultat valeurSelectionnee) {
                onDateDebutChoisie(valeurSelectionnee);
            }
        });
    }

    public void onDateDebutChoisie(final PointageInsererDialog.Resultat choixDateDebut) {
        PointageInsererDialog test = new PointageInsererDialog();
        test.afficher(getActivity(), "Choisir l'heure de fin", choixDateDebut, new SelectionListener<PointageInsererDialog.Resultat>() {
            @Override
            public void onSelected(PointageInsererDialog.Resultat valeurSelectionnee) {
                onDateFinChoisie(choixDateDebut, valeurSelectionnee);
            }
        });
    }

    public void onDateFinChoisie(PointageInsererDialog.Resultat choixDateDebut, PointageInsererDialog.Resultat choixDateFin) {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, choixDateDebut.getAnnee());
        cal.set(Calendar.MONTH, choixDateDebut.getMois());
        cal.set(Calendar.DAY_OF_MONTH, choixDateDebut.getJour());
        cal.set(Calendar.HOUR_OF_DAY, choixDateDebut.getHeure());
        cal.set(Calendar.MINUTE, choixDateDebut.getMinute());
        Date debut = cal.getTime();

        cal.set(Calendar.YEAR, choixDateFin.getAnnee());
        cal.set(Calendar.MONTH, choixDateFin.getMois());
        cal.set(Calendar.DAY_OF_MONTH, choixDateFin.getJour());
        cal.set(Calendar.HOUR_OF_DAY, choixDateFin.getHeure());
        cal.set(Calendar.MINUTE, choixDateFin.getMinute());
        Date fin = cal.getTime();

        controleur.inserer(debut, fin, choixDateFin.getCommentaire());
    }

    public void updatePointageStatut(String texte) {
        if (this.getView() != null) {
            TextView textRapide = (TextView) this.getView().findViewById(R.id.txtPointageStatut);
            textRapide.setText(texte);
        }
    }

    public void updatePointageRecapJour(String texte) {
        if (this.getView() != null) {
            TextView textEnCours = (TextView) this.getView().findViewById(R.id.txtPointageRecapJour);
            textEnCours.setText(texte);
        }
    }

    public void updatePointageRecapSemaine(String texte) {
        if (this.getView() != null) {
            TextView textEnCours = (TextView) this.getView().findViewById(R.id.txtPointageRecapSemaine);
            textEnCours.setText(texte);
        }
    }

}
