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

import java.util.Date;

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.communs.interactors.boundaries.in.InBoundary;
import fr.s3i.pointeuse.domaine.pointages.interactors.inserer.InsererInteractor;
import fr.s3i.pointeuse.domaine.pointages.interactors.inserer.boundaries.in.InsererIn;
import fr.s3i.pointeuse.domaine.pointages.interactors.inserer.boundaries.out.InsererOut;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.PointerInteractor;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.in.PointerIn;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.PointerOut;
import fr.s3i.pointeuse.domaine.pointages.interactors.recapitulatif.RecapitulatifInteractor;
import fr.s3i.pointeuse.domaine.pointages.interactors.recapitulatif.in.RecapIn;
import fr.s3i.pointeuse.domaine.pointages.interactors.recapitulatif.out.RecapOut;
import fr.s3i.pointeuse.domaine.pointages.interactors.statut.StatutInteractor;
import fr.s3i.pointeuse.domaine.pointages.interactors.statut.boundaries.in.StatutIn;
import fr.s3i.pointeuse.domaine.pointages.interactors.statut.boundaries.out.StatutOut;
import fr.s3i.pointeuse.presentation.commun.Controleur;

/**
 * Created by Adrien on 23/07/2016.
 */
public class PointerControleur<T extends PointerOut & InsererOut & RecapOut & StatutOut> extends Controleur implements PointerIn, InsererIn, RecapIn, StatutIn {

    public PointerControleur(Contexte contexte, T out) {
        super(
                new PointerInteractor(contexte, out),
                new InsererInteractor(contexte, out),
                new RecapitulatifInteractor(contexte, out),
                new StatutInteractor(contexte, out)
        );
    }

    @Override
    public void pointer() {
        tacheDeFond.execute(new Runnable() {
            @Override
            public void run() {
                getInteracteur(PointerInteractor.class).pointer();
            }
        });
    }

    @Override
    public void inserer(final Date debut, final Date fin, final String commentaire) {
        tacheDeFond.execute(new Runnable() {
            @Override
            public void run() {
                getInteracteur(InsererInteractor.class).inserer(debut, fin, commentaire);
            }
        });
    }

    @Override
    public void rafraichirStatut() {
        tacheDeFond.execute(new Runnable() {
            @Override
            public void run() {
                getInteracteur(StatutInteractor.class).rafraichirStatut();
            }
        });
    }
}
