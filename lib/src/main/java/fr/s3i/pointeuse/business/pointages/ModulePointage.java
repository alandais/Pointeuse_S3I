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

package fr.s3i.pointeuse.business.pointages;

import java.util.Collection;

import fr.s3i.pointeuse.business.communs.Contexte;
import fr.s3i.pointeuse.business.communs.composition.Composant;
import fr.s3i.pointeuse.business.communs.composition.Module;
import fr.s3i.pointeuse.business.pointages.interactors.communs.boundaries.out.translator.PointageInfoListeTranslator;
import fr.s3i.pointeuse.business.pointages.interactors.communs.boundaries.out.translator.PointageInfoTranslator;
import fr.s3i.pointeuse.business.pointages.interactors.communs.boundaries.out.utils.Calculs;

/**
 * Created by Adrien on 20/07/2016.
 */
public class ModulePointage extends Module {

    public ModulePointage(Contexte contexte) {
        super(contexte,
                Composant.creer(Calculs.class, new Calculs(contexte)),
                Composant.creer(PointageInfoTranslator.class, new PointageInfoTranslator(contexte)),
                Composant.creer(PointageInfoListeTranslator.class, new PointageInfoListeTranslator(contexte)));
    }

    @Override
    public Collection<Composant<?, ?>> getComposants() {
        return cache;
    }
}
