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

import java.util.Date;

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.communs.interactors.boundaries.in.InBoundary;
import fr.s3i.pointeuse.domaine.pointages.interactors.calendrier.CalendrierInteractor;
import fr.s3i.pointeuse.domaine.pointages.interactors.calendrier.boundaries.in.CalendrierIn;
import fr.s3i.pointeuse.presentation.commun.Controleur;

/**
 * Created by Adrien on 30/07/2016.
 */
public class CalendrierControleur extends Controleur<CalendrierInteractor> implements CalendrierIn {

    protected CalendrierControleur(Contexte contexte) {
        super(new CalendrierInteractor(contexte));
    }

    public static Class<? extends InBoundary> getCasUtilisationClass() {
        return CalendrierInteractor.class;
    }

    @Override
    public void supprimer(final long id) {
        tacheDeFond.execute(new Runnable() {
            @Override
            public void run() {
                interactor.supprimer(id);
            }
        });
    }

    @Override
    public void modifier(final long id) {
        tacheDeFond.execute(new Runnable() {
            @Override
            public void run() {
                interactor.modifier(id);
            }
        });
    }

    @Override
    public void modifier(final long id, final Date debut, final Date fin, final String commentaire) {
        tacheDeFond.execute(new Runnable() {
            @Override
            public void run() {
                interactor.modifier(id, debut, fin, commentaire);
            }
        });
    }

    @Override
    public void listerJour(final Date reference) {
        tacheDeFond.execute(new Runnable() {
            @Override
            public void run() {
                interactor.listerJour(reference);
            }
        });
    }

    @Override
    public void listerSemaine(final Date reference) {
        tacheDeFond.execute(new Runnable() {
            @Override
            public void run() {
                interactor.listerSemaine(reference);
            }
        });
    }

    @Override
    public void listerMois(final Date reference) {
        tacheDeFond.execute(new Runnable() {
            @Override
            public void run() {
                interactor.listerMois(reference);
            }
        });
    }

    @Override
    public void listerAnnee(final Date reference) {
        tacheDeFond.execute(new Runnable() {
            @Override
            public void run() {
                interactor.listerAnnee(reference);
            }
        });
    }
}
