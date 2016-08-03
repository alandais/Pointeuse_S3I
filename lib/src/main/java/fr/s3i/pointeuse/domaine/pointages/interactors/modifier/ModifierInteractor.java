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

package fr.s3i.pointeuse.domaine.pointages.interactors.modifier;

import java.util.Date;

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.communs.interactors.Interactor;
import fr.s3i.pointeuse.domaine.pointages.Chaines;
import fr.s3i.pointeuse.domaine.pointages.entities.Pointage;
import fr.s3i.pointeuse.domaine.pointages.gateways.PointageRepository;
import fr.s3i.pointeuse.domaine.pointages.interactors.modifier.boundaries.in.ModifierIn;
import fr.s3i.pointeuse.domaine.pointages.interactors.modifier.boundaries.out.ModifierOut;
import fr.s3i.pointeuse.domaine.pointages.operations.EnregistrerPointage;

/**
 * Created by Adrien on 30/07/2016.
 */
public class ModifierInteractor extends Interactor<ModifierOut> implements ModifierIn {

    private final PointageRepository repository;

    private final EnregistrerPointage enregistrerPointage;

    public ModifierInteractor(Contexte contexte, ModifierOut out) {
        super(out);
        this.repository = contexte.getService(PointageRepository.class);
        this.enregistrerPointage = new EnregistrerPointage(contexte, out);
    }

    @Override
    public void modifier(long id) {
        Pointage pointage = repository.recuperer(id);
        out.modifier(pointage);
    }

    @Override
    public void modifier(long id, Date debut, Date fin, String commentaire) {
        Pointage pointage = repository.recuperer(id);
        pointage.setDebut(debut);
        pointage.setFin(fin);
        pointage.setCommentaire(commentaire);

        if(enregistrerPointage.executer(pointage)) {
            out.toast(Chaines.toast_pointage_modifie);
        }
    }

}
