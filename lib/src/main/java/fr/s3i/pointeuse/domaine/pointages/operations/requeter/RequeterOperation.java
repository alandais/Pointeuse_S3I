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

package fr.s3i.pointeuse.domaine.pointages.operations.requeter;

import java.util.Date;
import java.util.List;

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.communs.interactors.boundaries.out.OutBoundary;
import fr.s3i.pointeuse.domaine.communs.operations.Operation2;
import fr.s3i.pointeuse.domaine.pointages.entities.Pointage;
import fr.s3i.pointeuse.domaine.pointages.gateways.PointageRepository;
import fr.s3i.pointeuse.domaine.pointages.operations.requeter.model.PointageInfoListe;
import fr.s3i.pointeuse.domaine.pointages.operations.requeter.model.PointageInfoListeFactory;
import fr.s3i.pointeuse.domaine.pointages.services.model.PointageWrapperFactory;
import fr.s3i.pointeuse.domaine.pointages.services.model.PointageWrapperListe;
import fr.s3i.pointeuse.domaine.pointages.utils.Periode;

/**
 * Created by Adrien on 07/08/2016.
 */
public class RequeterOperation extends Operation2<PointageInfoListe, Periode, Date> {

    private final PointageRepository repository;

    private final PointageWrapperFactory pointageWrapperFactory;

    private final PointageInfoListeFactory pointageInfoListeFactory;

    public RequeterOperation(Contexte contexte, OutBoundary out) {
        super(out);
        this.repository = contexte.getService(PointageRepository.class);
        this.pointageWrapperFactory = contexte.getService(PointageWrapperFactory.class);
        this.pointageInfoListeFactory = contexte.getService(PointageInfoListeFactory.class);
    }

    @Override
    public PointageInfoListe executer(Periode periode, Date reference) {
        Date debutPeriode = periode.getDebutPeriode(reference);
        Date finPeriode = periode.getFinPeriode(reference);

        List<Pointage> pointages = repository.recupererEntre(debutPeriode, finPeriode);
        PointageWrapperListe pointageWrapperListe = pointageWrapperFactory.getPointageWrapper(pointages);
        return pointageInfoListeFactory.getPointageInfoListe(pointageWrapperListe);
    }

}
