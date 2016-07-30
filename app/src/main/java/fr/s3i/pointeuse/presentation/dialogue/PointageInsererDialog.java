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
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import fr.s3i.pointeuse.R;

/**
 * Created by Adrien on 29/07/2016.
 */
public class PointageInsererDialog extends DialogFragment implements DialogInterface.OnClickListener {

    public static class Resultat {
        private final String commentaire;
        private final int annee;
        private final int mois;
        private final int jour;
        private final int heure;
        private final int minute;

        public Resultat(String commentaire, int annee, int mois, int jour, int heure, int minute) {
            this.commentaire = commentaire;
            this.annee = annee;
            this.mois = mois;
            this.jour = jour;
            this.heure = heure;
            this.minute = minute;
        }

        public String getCommentaire() {
            return commentaire;
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

        public int getHeure() {
            return heure;
        }

        public int getMinute() {
            return minute;
        }
    }

    private SelectionListener<Resultat> listener;

    private String titre;

    private View view;

    private TimePicker timePicker;

    private DatePicker datePicker;

    private EditText commentaire;

    private Resultat valeursParDefaut;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_choix_heure, null);
        commentaire = (EditText) view.findViewById(R.id.txtCommentaire);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        if (valeursParDefaut != null) {
            commentaire.setText(valeursParDefaut.getCommentaire());
            datePicker.updateDate(valeursParDefaut.getAnnee(), valeursParDefaut.getMois(), valeursParDefaut.getJour());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timePicker.setHour(valeursParDefaut.getHeure());
                timePicker.setMinute(valeursParDefaut.getMinute());
            }
            else {
                timePicker.setCurrentHour(valeursParDefaut.getHeure());
                timePicker.setCurrentMinute(valeursParDefaut.getMinute());
            }
        }
        builder
            .setTitle(titre)
            .setView(view)
            .setPositiveButton(R.string.ok, this)
            .setNegativeButton(R.string.annuler, null);

        // and return it
        return builder.create();
    }

    public void afficher(FragmentActivity parent, String titre, Resultat valeursParDefaut, SelectionListener<Resultat> listener) {
        this.titre = titre;
        this.listener = listener;
        this.valeursParDefaut = valeursParDefaut;
        show(parent.getSupportFragmentManager(), this.getClass().getSimpleName());
    }

    public void afficher(FragmentActivity parent, String titre, SelectionListener<Resultat> listener) {
        afficher(parent, titre, null, listener);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        int annee, mois, jour, heure, minute;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            heure = timePicker.getHour();
            minute = timePicker.getMinute();
        }
        else {
            heure = timePicker.getCurrentHour();
            minute = timePicker.getCurrentMinute();
        }
        annee = datePicker.getYear();
        mois = datePicker.getMonth();
        jour = datePicker.getDayOfMonth();
        String commentaire = this.commentaire.getText().toString();
        Resultat resultat = new Resultat(commentaire, annee, mois, jour, heure, minute);
        listener.onSelected(resultat);
    }
}
