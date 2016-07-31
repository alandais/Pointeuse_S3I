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

import android.support.v4.app.FragmentActivity;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Adrien on 31/07/2016.
 */
public class DialoguePointageInfo {

    public static class Resultat {

        private final Date debut;

        private final Date fin;

        private final String commentaire;

        public Resultat(Date debut, Date fin, String commentaire) {
            this.debut = debut;
            this.fin = fin;
            this.commentaire = commentaire;
        }

        public Date getDebut() {
            return debut;
        }

        public Date getFin() {
            return fin;
        }

        public String getCommentaire() {
            return commentaire;
        }

    }

    public void lancer(FragmentActivity parent, Listener<Resultat> listener) {
        lancer(parent, listener, null);
    }

    public void lancer(final FragmentActivity parent, final Listener<Resultat> listener, final Resultat valeursInitiales) {
        DialogueDateEtCommentaire.Resultat dateEtCommmentaireInitiaux = null;
        if (valeursInitiales != null) {
            dateEtCommmentaireInitiaux = getDateEtCommentaire(valeursInitiales.getDebut(), valeursInitiales.getCommentaire());
        }
        DialogueDateEtCommentaire dateCommentaireDialog = new DialogueDateEtCommentaire();
        dateCommentaireDialog.afficher(parent, "Choisir l'heure de début", dateEtCommmentaireInitiaux, new Listener<DialogueDateEtCommentaire.Resultat>() {
            @Override
            public void onSelected(DialogueDateEtCommentaire.Resultat choixDateDebut) {
                onDateDebutChoisie(parent, listener, valeursInitiales, choixDateDebut);
            }
        });
    }

    private void onDateDebutChoisie(final FragmentActivity parent, final Listener<Resultat> listener, final Resultat valeursInitiales, final DialogueDateEtCommentaire.Resultat choixDateDebut) {
        DialogueDateEtCommentaire.Resultat dateEtCommmentaireInitiaux;
        if (valeursInitiales != null) {
            dateEtCommmentaireInitiaux = getDateEtCommentaire(valeursInitiales.getFin(), choixDateDebut.getCommentaire());
        }
        else {
            dateEtCommmentaireInitiaux = choixDateDebut;
        }

        DialogueDateEtCommentaire dateCommentaireDialog = new DialogueDateEtCommentaire();
        dateCommentaireDialog.afficher(parent, "Choisir l'heure de fin", dateEtCommmentaireInitiaux, new Listener<DialogueDateEtCommentaire.Resultat>() {
            @Override
            public void onSelected(DialogueDateEtCommentaire.Resultat choixDateFin) {
                onDateFinChoisie(listener, choixDateDebut, choixDateFin);
            }
        });
    }

    private void onDateFinChoisie(Listener<Resultat> listener, DialogueDateEtCommentaire.Resultat choixDateDebut, DialogueDateEtCommentaire.Resultat choixDateFin) {
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

        listener.onSelected(new Resultat(debut, fin, choixDateFin.getCommentaire()));
    }

    private DialogueDateEtCommentaire.Resultat getDateEtCommentaire(Date date, String commentaire) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        return getDateEtCommentaire(calendar, commentaire);
    }

    private DialogueDateEtCommentaire.Resultat getDateEtCommentaire(Calendar calendar, String commentaire) {
        int annee = calendar.get(Calendar.YEAR);
        int mois = calendar.get(Calendar.MONTH);
        int jour = calendar.get(Calendar.DAY_OF_MONTH);
        int heure = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new DialogueDateEtCommentaire.Resultat(commentaire, annee, mois, jour, heure, minute);
    }

}
