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
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.PointerInteractor;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.in.PointerIn;
import fr.s3i.pointeuse.presentation.commun.Controleur;

/**
 * Created by Adrien on 23/07/2016.
 */
public class PointerControleur extends Controleur<PointerInteractor> implements PointerIn {

    public static Class<? extends InBoundary> getCasUtilisationClass() {
        return PointerInteractor.class;
    }

    public PointerControleur(Contexte contexte) {
        super(new PointerInteractor(contexte));
    }

    @Override
    public void pointer() {
        tacheDeFond.execute(new Runnable() {
            @Override
            public void run() {
                interactor.pointer();
            }
        });
    }

    @Override
    public void inserer(final Date debut, final Date fin, final String commentaire) {
        tacheDeFond.execute(new Runnable() {
            @Override
            public void run() {
                interactor.inserer(debut, fin, commentaire);
            }
        });
    }

}
