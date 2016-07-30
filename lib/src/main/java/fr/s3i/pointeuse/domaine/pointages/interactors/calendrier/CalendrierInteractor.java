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

package fr.s3i.pointeuse.domaine.pointages.interactors.calendrier;

import java.util.Date;
import java.util.List;

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.communs.R;
import fr.s3i.pointeuse.domaine.communs.entities.CasUtilisationInfo;
import fr.s3i.pointeuse.domaine.communs.interactors.Interactor;
import fr.s3i.pointeuse.domaine.pointages.entities.Pointage;
import fr.s3i.pointeuse.domaine.pointages.gateways.PointageRepository;
import fr.s3i.pointeuse.domaine.pointages.interactors.calendrier.boundaries.in.CalendrierIn;
import fr.s3i.pointeuse.domaine.pointages.interactors.calendrier.boundaries.out.CalendrierOut;
import fr.s3i.pointeuse.domaine.pointages.interactors.calendrier.boundaries.out.model.PointageInfoListe;
import fr.s3i.pointeuse.domaine.pointages.interactors.calendrier.boundaries.out.model.PointageInfoListeFactory;
import fr.s3i.pointeuse.domaine.pointages.services.model.PointageWrapperFactory;
import fr.s3i.pointeuse.domaine.pointages.services.model.PointageWrapperListe;
import fr.s3i.pointeuse.domaine.pointages.utils.Periode;

/**
 * Created by Adrien on 30/07/2016.
 */
public class CalendrierInteractor extends Interactor<CalendrierOut> implements CalendrierIn {

    private final PointageRepository repository;

    private final PointageWrapperFactory pointageWrapperFactory;

    private final PointageInfoListeFactory pointageInfoListeFactory;

    public CalendrierInteractor(Contexte contexte) {
        super(contexte.getService(CalendrierOut.class));
        this.repository = contexte.getService(PointageRepository.class);
        this.pointageWrapperFactory = contexte.getService(PointageWrapperFactory.class);
        this.pointageInfoListeFactory = contexte.getService(PointageInfoListeFactory.class);
    }

    @Override
    public void initialiser() {
        CasUtilisationInfo info = new CasUtilisationInfo(R.get("interactor_calendrier_nom"));
        out.onDemarrer(info);
    }

    @Override
    public void supprimer(long id) {
        repository.supprimer(id);
        out.toast(R.get("toast_pointage_supprime"));
    }

    @Override
    public void listerJour(Date reference) {
        lister(Periode.JOUR, reference);
    }

    @Override
    public void listerSemaine(Date reference) {
        lister(Periode.SEMAINE, reference);
    }

    @Override
    public void listerMois(Date reference) {
        lister(Periode.MOIS, reference);
    }

    private void lister(Periode periode, Date reference) {
        Date debutPeriode = periode.getDebutPeriode(reference);
        Date finPeriode = periode.getFinPeriode(reference);

        List<Pointage> pointages = repository.recupererEntre(debutPeriode, finPeriode);
        PointageWrapperListe pointageWrapperListe = pointageWrapperFactory.getPointageWrapper(pointages);
        PointageInfoListe pointageInfoListe = pointageInfoListeFactory.getPointageInfoListe(pointageWrapperListe);

        out.onPointageInfoListeUpdate(pointageInfoListe);
    }

}
