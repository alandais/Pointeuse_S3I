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

package fr.s3i.pointeuse.presentation.fragment.calendrier;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.s3i.pointeuse.R;
import fr.s3i.pointeuse.domaine.pointages.Chaines;
import fr.s3i.pointeuse.domaine.pointages.entities.Pointage;
import fr.s3i.pointeuse.domaine.pointages.interactors.lister.boundaries.out.model.PointageInfo;
import fr.s3i.pointeuse.presentation.fragment.calendrier.adaptateur.PointageInfoListeAdaptateur;
import fr.s3i.pointeuse.presentation.fragment.commun.Vue;
import fr.s3i.pointeuse.presentation.dialogue.DialoguePointageInfo;
import fr.s3i.pointeuse.presentation.dialogue.Listener;

/**
 * Created by Adrien on 30/07/2016.
 */
public class CalendrierVue extends Vue<CalendrierPresenter, CalendrierControleur> implements DatePicker.OnDateChangedListener, RadioGroup.OnCheckedChangeListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public static CalendrierVue getInstance(String titre) {
        CalendrierVue vue = new CalendrierVue();
        vue.setTitre(titre);
        return vue;
    }

    private enum FiltrePeriode {
        JOUR,
        SEMAINE,
        MOIS,
        ANNEE
    }

    private class FiltreCalendrier {
        private final FiltrePeriode filtrePeriode;
        private final Date dateReference;

        public FiltreCalendrier() {
            switch(calendrierFiltre.getCheckedRadioButtonId()) {
                default:
                case R.id.trijour:
                    this.filtrePeriode = FiltrePeriode.JOUR;
                    break;
                case R.id.trisemaine:
                    this.filtrePeriode = FiltrePeriode.SEMAINE;
                    break;
                case R.id.trimois:
                    this.filtrePeriode = FiltrePeriode.MOIS;
                    break;
                case R.id.triannee:
                    this.filtrePeriode = FiltrePeriode.ANNEE;
                    break;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, calendrier.getYear());
            calendar.set(Calendar.MONTH, calendrier.getMonth());
            calendar.set(Calendar.DAY_OF_MONTH, calendrier.getDayOfMonth());
            this.dateReference = calendar.getTime();
        }

        public FiltrePeriode getFiltrePeriode() {
            return filtrePeriode;
        }

        public Date getDateReference() {
            return dateReference;
        }
    }

    private DatePicker calendrier;
    private RadioGroup calendrierFiltre;
    private ListView calendrierListe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controleur = new CalendrierControleur<>(contexte, new CalendrierPresenter(this));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendrier_vue, container, false);

        calendrier = (DatePicker) view.findViewById(R.id.calendrier);
        calendrier.init(calendrier.getYear(), calendrier.getMonth(), calendrier.getDayOfMonth(), this);

        calendrierFiltre = (RadioGroup) view.findViewById(R.id.radiogroup);

        calendrierListe = (ListView) view.findViewById(R.id.calendrier_liste);
        calendrierListe.setLongClickable(true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // if (savedInstanceState != null) {
            // si un état est à restaurer (après rotation de l'écran par exemple), c'est ici qu'il faut le faire
        // }
    }

    @Override
    public void onResume() {
        super.onResume();
        calendrierFiltre.setOnCheckedChangeListener(this);
        calendrierListe.setOnItemClickListener(this);
        calendrierListe.setOnItemLongClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        calendrierFiltre.setOnCheckedChangeListener(null);
        calendrierListe.setOnItemClickListener(null);
        calendrierListe.setOnItemLongClickListener(null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // if (this.getView() != null) {
            // si un état est à sauvegarder (avant rotation de l'écran par exemple), c'est ici qu'il faut le faire
        // }
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int annee, int mois, int jour) {
        updateListeCalendrier();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int numero) {
        updateListeCalendrier();
    }

    public void updateListeCalendrier() {
        FiltreCalendrier filtre = new FiltreCalendrier();
        switch (filtre.getFiltrePeriode()) {
            case JOUR:
                controleur.listerJour(filtre.getDateReference());
                break;
            case SEMAINE:
                controleur.listerSemaine(filtre.getDateReference());
                break;
            case MOIS:
                controleur.listerMois(filtre.getDateReference());
                break;
            case ANNEE:
                controleur.listerAnnee(filtre.getDateReference());
                break;
        }
    }

    public void onExport() {
        FiltreCalendrier filtre = new FiltreCalendrier();
        switch (filtre.getFiltrePeriode()) {
            case JOUR:
                controleur.exporterJour(filtre.getDateReference());
                break;
            case SEMAINE:
                controleur.exporterSemaine(filtre.getDateReference());
                break;
            case MOIS:
                controleur.exporterMois(filtre.getDateReference());
                break;
            case ANNEE:
                controleur.exporterAnnee(filtre.getDateReference());
                break;
        }
    }

    public void onDureeTotaleUpdate(String dureeTotale) {
        if (this.getView() != null) {
            TextView textEnCours = (TextView) this.getView().findViewById(R.id.duree);
            textEnCours.setText(dureeTotale);
        }
    }

    public void onPointageListeUpdate(List<PointageInfo> listePointage) {
        if (this.getView() != null) {
            ListView liste = (ListView) this.getView().findViewById(R.id.calendrier_liste);
            liste.setAdapter(new PointageInfoListeAdaptateur(this.getContext(), listePointage));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        controleur.recupererPourModification(l);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, final long l) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        AlertDialog dialog = builder
            .setPositiveButton(Chaines.oui, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    controleur.supprimer(l);
                }
            })
            .setNegativeButton(Chaines.non, null)
            .setTitle(Chaines.demanderConfirmationSuppression((PointageInfo) adapterView.getItemAtPosition(i)))
            .create();
        dialog.show();
        return true;
    }

    public void onPointageRecuperePourModification(final Pointage pointage) {
        DialoguePointageInfo.Resultat valeursInitiales = new DialoguePointageInfo.Resultat(pointage.getDebut(), pointage.getFin(), pointage.getCommentaire());
        DialoguePointageInfo dialoguePointageInfo = new DialoguePointageInfo();
        dialoguePointageInfo.lancer(getActivity(), new Listener<DialoguePointageInfo.Resultat>() {
            @Override
            public void onSelected(DialoguePointageInfo.Resultat valeurSelectionnee) {
                onPointageInfoSaisiePourModification(pointage.getId(), valeurSelectionnee);
            }
        }, valeursInitiales);
    }

    private void onPointageInfoSaisiePourModification(Long id, DialoguePointageInfo.Resultat saisie) {
        controleur.modifier(id, saisie.getDebut(), saisie.getFin(), saisie.getCommentaire());
    }

}
