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

import java.util.Date;

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.pointages.interactors.lister.ListerInteractor;
import fr.s3i.pointeuse.domaine.pointages.interactors.lister.boundaries.in.ListerIn;
import fr.s3i.pointeuse.domaine.pointages.interactors.lister.boundaries.out.ListerOut;
import fr.s3i.pointeuse.domaine.pointages.interactors.modifier.ModifierInteractor;
import fr.s3i.pointeuse.domaine.pointages.interactors.modifier.boundaries.in.ModifierIn;
import fr.s3i.pointeuse.domaine.pointages.interactors.modifier.boundaries.out.ModifierOut;
import fr.s3i.pointeuse.domaine.pointages.interactors.supprimer.SupprimerInteractor;
import fr.s3i.pointeuse.domaine.pointages.interactors.supprimer.boundaries.in.SupprimerIn;
import fr.s3i.pointeuse.domaine.pointages.interactors.supprimer.boundaries.out.SupprimerOut;
import fr.s3i.pointeuse.presentation.fragment.commun.Controleur;

/**
 * Created by Adrien on 30/07/2016.
 */
public class CalendrierControleur<T extends ListerOut & ModifierOut & SupprimerOut> extends Controleur implements ListerIn, ModifierIn, SupprimerIn {

    protected CalendrierControleur(Contexte contexte, T out) {
        super(
                new ListerInteractor(contexte, out),
                new ModifierInteractor(contexte, out),
                new SupprimerInteractor(contexte, out)
        );
    }

    @Override
    public void supprimer(final long id) {
        tacheDeFond.execute(new Runnable() {
            @Override
            public void run() {
                getInteracteur(SupprimerInteractor.class).supprimer(id);
            }
        });
    }

    @Override
    public void recupererPourModification(final long id) {
        tacheDeFond.execute(new Runnable() {
            @Override
            public void run() {
                getInteracteur(ModifierInteractor.class).recupererPourModification(id);
            }
        });
    }

    @Override
    public void modifier(final long id, final Date debut, final Date fin, final String commentaire) {
        tacheDeFond.execute(new Runnable() {
            @Override
            public void run() {
                getInteracteur(ModifierInteractor.class).modifier(id, debut, fin, commentaire);
            }
        });
    }

    @Override
    public void listerJour(final Date reference) {
        tacheDeFond.execute(new Runnable() {
            @Override
            public void run() {
                getInteracteur(ListerInteractor.class).listerJour(reference);
            }
        });
    }

    @Override
    public void listerSemaine(final Date reference) {
        tacheDeFond.execute(new Runnable() {
            @Override
            public void run() {
                getInteracteur(ListerInteractor.class).listerSemaine(reference);
            }
        });
    }

    @Override
    public void listerMois(final Date reference) {
        tacheDeFond.execute(new Runnable() {
            @Override
            public void run() {
                getInteracteur(ListerInteractor.class).listerMois(reference);
            }
        });
    }

    @Override
    public void listerAnnee(final Date reference) {
        tacheDeFond.execute(new Runnable() {
            @Override
            public void run() {
                getInteracteur(ListerInteractor.class).listerAnnee(reference);
            }
        });
    }
}
