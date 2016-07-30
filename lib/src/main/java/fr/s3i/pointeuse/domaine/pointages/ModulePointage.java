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

package fr.s3i.pointeuse.domaine.pointages;

import java.util.Collection;

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.communs.R;
import fr.s3i.pointeuse.domaine.communs.composition.Composant;
import fr.s3i.pointeuse.domaine.communs.composition.Module;
import fr.s3i.pointeuse.domaine.communs.entities.CasUtilisationInfo;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.model.PointageRecapitulatifFactory;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.model.PointageStatutFactory;
import fr.s3i.pointeuse.domaine.pointages.services.model.PointageWrapperFactory;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.in.PointerIn;
import fr.s3i.pointeuse.domaine.pointages.services.Calculateur;
import fr.s3i.pointeuse.domaine.pointages.services.Formateur;

/**
 * Created by Adrien on 20/07/2016.
 */
public class ModulePointage extends Module {

    public ModulePointage(Contexte contexte) {
        super(contexte);
        enregistrerService(ModulePointage.class, this);
        enregistrerService(Calculateur.class, new Calculateur(contexte));
        enregistrerService(Formateur.class, new Formateur(contexte));
        enregistrerService(PointageWrapperFactory.class, new PointageWrapperFactory(contexte));
        enregistrerService(PointageStatutFactory.class, new PointageStatutFactory());
        enregistrerService(PointageRecapitulatifFactory.class, new PointageRecapitulatifFactory());
        enregistrerInteracteur(PointerIn.class, new CasUtilisationInfo(R.get("interactor_pointer_nom")));
    }

    @Override
    public Collection<Composant<?, ?>> getComposants() {
        return cache;
    }

}
