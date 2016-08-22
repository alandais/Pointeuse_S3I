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

package fr.s3i.pointeuse.presentation.widget.pointer;

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.PointerInteractor;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.in.PointerIn;
import fr.s3i.pointeuse.domaine.pointages.interactors.recapitulatif.RecapitulatifInteractor;
import fr.s3i.pointeuse.domaine.pointages.interactors.recapitulatif.boundaries.in.RecapIn;
import fr.s3i.pointeuse.presentation.widget.commun.WidgetControler;

/**
 * Created by Adrien on 04/08/2016.
 */
public class PointerWidgetControler extends WidgetControler implements PointerIn, RecapIn {

    private final PointerInteractor pointer;

    private final RecapitulatifInteractor recapitulatif;

    public PointerWidgetControler(Contexte contexte, PointerWidgetPresenter presenter) {
        this.pointer = new PointerInteractor(contexte, presenter);
        this.recapitulatif = new RecapitulatifInteractor(contexte, presenter);
    }

    @Override
    public void pointer() {
        pointer.pointer();
    }

    @Override
    public void recalculerRecapitulatif() {
        recapitulatif.recalculerRecapitulatif();
    }

    @Override
    public void lancerCalculRecapitulatifAutomatique() {
        recapitulatif.lancerCalculRecapitulatifAutomatique();
    }
}
