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

package fr.s3i.pointeuse.presentation.dialogue;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;

import fr.s3i.pointeuse.R;
import fr.s3i.pointeuse.domaine.pointages.Chaines;

/**
 * Ce dialogue n'est pas utilisé pour le moment car la vue Calendrier inclu déjà le composant.
 */
public class DialogueChoixPeriode extends DialogFragment implements DialogInterface.OnClickListener {

    public enum TypePeriode {
        JOUR,
        SEMAINE,
        MOIS,
        ANNEE
    }

    public static class Resultat {

        private final TypePeriode periode;
        private final int annee;
        private final int mois;
        private final int jour;

        public Resultat(TypePeriode periode, int annee, int mois, int jour) {
            this.periode = periode;
            this.annee = annee;
            this.mois = mois;
            this.jour = jour;
        }

        public TypePeriode getPeriode() {
            return periode;
        }

        public int getAnnee() {
            return annee;
        }

        public int getMois() {
            return mois;
        }

        public int getJour() {
            return jour;
        }

    }

    private Listener<Resultat> listener;

    private String titre;

    private DatePicker calendrier;

    private RadioGroup calendrierFiltre;

    private Resultat valeursParDefaut;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_choix_periode, null);
        calendrier = (DatePicker) view.findViewById(R.id.calendrier);
        calendrierFiltre = (RadioGroup) view.findViewById(R.id.radiogroup);
        if (valeursParDefaut != null) {
            calendrier.updateDate(valeursParDefaut.getAnnee(), valeursParDefaut.getMois(), valeursParDefaut.getJour());
        }

        builder
                .setTitle(titre)
                .setView(view)
                .setPositiveButton(Chaines.ok, this)
                .setNegativeButton(Chaines.annuler, null);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        int annee, mois, jour;
        TypePeriode periode;
        annee = calendrier.getYear();
        mois = calendrier.getMonth();
        jour = calendrier.getDayOfMonth();
        switch(calendrierFiltre.getCheckedRadioButtonId()) {
            default:
            case R.id.trijour:
                periode = TypePeriode.JOUR;
                break;
            case R.id.trisemaine:
                periode = TypePeriode.SEMAINE;
                break;
            case R.id.trimois:
                periode = TypePeriode.MOIS;
                break;
            case R.id.triannee:
                periode = TypePeriode.ANNEE;
                break;
        }
        Resultat resultat = new Resultat(periode, annee, mois, jour);
        listener.onSelected(resultat);
    }

    public void afficher(FragmentActivity parent, String titre, Listener<Resultat> listener, Resultat valeursParDefaut) {
        this.titre = titre;
        this.listener = listener;
        this.valeursParDefaut = valeursParDefaut;
        show(parent.getSupportFragmentManager(), this.getClass().getSimpleName());
    }

    public void afficher(FragmentActivity parent, String titre, Listener<Resultat> listener) {
        afficher(parent, titre, listener, null);
    }

}
