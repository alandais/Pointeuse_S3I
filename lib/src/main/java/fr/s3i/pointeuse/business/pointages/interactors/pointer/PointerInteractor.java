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

package fr.s3i.pointeuse.business.pointages.interactors.pointer;

import java.util.Date;

import fr.s3i.pointeuse.business.communs.Contexte;
import fr.s3i.pointeuse.business.communs.interactors.Interactor;
import fr.s3i.pointeuse.business.pointages.entities.Pointage;
import fr.s3i.pointeuse.business.pointages.gateways.PointageRepository;
import fr.s3i.pointeuse.business.pointages.interactors.communs.boundaries.out.translator.PointageInfoTranslator;
import fr.s3i.pointeuse.business.pointages.interactors.pointer.boundaries.in.PointerIn;
import fr.s3i.pointeuse.business.pointages.interactors.pointer.boundaries.out.PointerOut;

/**
 * Created by Adrien on 19/07/2016.
 */
public class PointerInteractor extends Interactor<PointerOut> implements PointerIn {

    private final PointageRepository repository;

    private final PointageInfoTranslator translator;

    public PointerInteractor(Contexte contexte) {
        super(contexte.getService(PointerOut.class));
        this.repository = contexte.getService(PointageRepository.class);
        this.translator = contexte.getService(PointageInfoTranslator.class);
    }

    @Override
    public void pointer() {
        Pointage pointage = repository.recupererDernier();
        if (pointage != null && pointage.getFin() == null) {
            pointage.setFin(new Date());
        } else {
            pointage = new Pointage(new Date());
        }
        persister(pointage);
    }

    @Override
    public void inserer(Date debut, Date fin, String commentaire) {
        Pointage pointage = new Pointage(debut);
        pointage.setFin(fin);
        pointage.setCommentaire(commentaire);
        persister(pointage);
    }

    private void persister(Pointage pointage) {
        String erreur = pointage.getErrorMessage();
        if (erreur == null) {
            repository.persister(pointage);
            out.onPointageInsere(translator.translate(pointage));
        } else {
            out.onError(erreur);
        }
    }

}
