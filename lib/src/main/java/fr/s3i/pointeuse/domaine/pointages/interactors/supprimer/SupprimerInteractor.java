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

package fr.s3i.pointeuse.domaine.pointages.interactors.supprimer;

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.communs.interactors.Interactor;
import fr.s3i.pointeuse.domaine.communs.services.logger.Log;
import fr.s3i.pointeuse.domaine.pointages.Chaines;
import fr.s3i.pointeuse.domaine.pointages.gateways.PointageRepository;
import fr.s3i.pointeuse.domaine.pointages.interactors.supprimer.boundaries.in.SupprimerIn;
import fr.s3i.pointeuse.domaine.pointages.interactors.supprimer.boundaries.out.SupprimerOut;
import fr.s3i.pointeuse.domaine.pointages.services.BusPointage;

/**
 * Created by Adrien on 30/07/2016.
 */
public class SupprimerInteractor extends Interactor<SupprimerOut> implements SupprimerIn {

    private final PointageRepository repository;

    private final BusPointage bus;

    public SupprimerInteractor(Contexte contexte, SupprimerOut out) {
        super(out);
        this.repository = contexte.getService(PointageRepository.class);
        this.bus = contexte.getService(BusPointage.class);
    }

    @Override
    public void supprimer(long id) {
        repository.supprimer(id);
        out.toast(Chaines.toast_pointage_supprime);
        //out.onPointageSuppression(pointage);
        bus.post(this, BusPointage.RAFRAICHIR);
    }

}
