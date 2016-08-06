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

package fr.s3i.pointeuse.domaine.pointages.interactors.inserer;

import java.util.Date;

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.communs.interactors.Interactor;
import fr.s3i.pointeuse.domaine.pointages.Chaines;
import fr.s3i.pointeuse.domaine.pointages.entities.Pointage;
import fr.s3i.pointeuse.domaine.pointages.interactors.inserer.boundaries.in.InsererIn;
import fr.s3i.pointeuse.domaine.pointages.interactors.inserer.boundaries.out.InsererOut;
import fr.s3i.pointeuse.domaine.pointages.operations.EnregistrerPointage;
import fr.s3i.pointeuse.domaine.pointages.services.BusPointage;

/**
 * Created by Adrien on 19/07/2016.
 */
public class InsererInteractor extends Interactor<InsererOut> implements InsererIn {

    private final EnregistrerPointage enregistrerPointage;

    public InsererInteractor(Contexte contexte, InsererOut out) {
        super(out);
        this.enregistrerPointage = new EnregistrerPointage(contexte, out);
    }

    @Override
    public void inserer(Date debut, Date fin, String commentaire) {
        Pointage pointage = new Pointage();
        pointage.setDebut(debut);
        pointage.setFin(fin);
        pointage.setCommentaire(commentaire);

        BusPointage.PointageEvent event = new BusPointage.PointageInsereEvent(this, pointage);
        if(enregistrerPointage.executer(pointage, event)) {
            out.toast(Chaines.toast_pointage_insere);
        }
    }

}
