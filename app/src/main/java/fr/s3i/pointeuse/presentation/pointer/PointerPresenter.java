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

import fr.s3i.pointeuse.domaine.pointages.interactors.communs.boundaries.out.model.PointageInfo;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.PointerOut;
import fr.s3i.pointeuse.presentation.commun.Presenter;

/**
 * Created by Adrien on 23/07/2016.
 */
public class PointerPresenter extends Presenter<PointerVue> implements PointerOut {

    public PointerPresenter(PointerVue vue) {
        super(vue);
    }

    @Override
    public void onPointageRapide(PointageInfo pointage) {
        vue.updateInfoPointageEnCours(pointage.toInfoString());
    }

    @Override
    public void onPointageInsere(PointageInfo pointage) {
        vue.updateInfoPointageInsere(pointage.toInfoString());
    }

}
