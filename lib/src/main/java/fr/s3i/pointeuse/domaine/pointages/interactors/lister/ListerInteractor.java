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

package fr.s3i.pointeuse.domaine.pointages.interactors.lister;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.communs.interactors.Interactor;
import fr.s3i.pointeuse.domaine.communs.services.BusService;
import fr.s3i.pointeuse.domaine.communs.services.logger.Log;
import fr.s3i.pointeuse.domaine.pointages.entities.Pointage;
import fr.s3i.pointeuse.domaine.pointages.gateways.PointageRepository;
import fr.s3i.pointeuse.domaine.pointages.interactors.lister.boundaries.in.ListerIn;
import fr.s3i.pointeuse.domaine.pointages.interactors.lister.boundaries.out.ListerOut;
import fr.s3i.pointeuse.domaine.pointages.interactors.lister.boundaries.out.model.PointageInfoListe;
import fr.s3i.pointeuse.domaine.pointages.interactors.lister.boundaries.out.model.PointageInfoListeFactory;
import fr.s3i.pointeuse.domaine.pointages.services.BusPointage;
import fr.s3i.pointeuse.domaine.pointages.services.model.PointageWrapperFactory;
import fr.s3i.pointeuse.domaine.pointages.services.model.PointageWrapperListe;
import fr.s3i.pointeuse.domaine.pointages.utils.Periode;

/**
 * Created by Adrien on 19/07/2016.
 */
public class ListerInteractor extends Interactor<ListerOut> implements ListerIn, BusService.Listener {

    private final BusPointage bus;

    private final PointageRepository repository;

    private final PointageWrapperFactory pointageWrapperFactory;

    private final PointageInfoListeFactory pointageInfoListeFactory;

    public ListerInteractor(Contexte contexte, ListerOut out) {
        super(out);
        this.bus = contexte.getService(BusPointage.class);
        this.repository = contexte.getService(PointageRepository.class);
        this.pointageWrapperFactory = contexte.getService(PointageWrapperFactory.class);
        this.pointageInfoListeFactory = contexte.getService(PointageInfoListeFactory.class);
    }

    @Override
    public void initialiser() {
        super.initialiser();
        out.refreshPointageInfoListe();
        bus.subscribe(this);
    }

    @Override
    public void close() throws IOException {
        super.close();
        bus.unsuscribe(this);
    }

    @Override
    public boolean onEvent(BusService.Event event) {
        if (event instanceof BusPointage.PointageEvent) {
            Log.info(Log.EVENTS, "{0} ({1}) evenement {2} recu de {3}", this.getClass().getSimpleName(), this, event.getType(), event.getOriginator());
            out.refreshPointageInfoListe();
        }
        return true;
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

    @Override
    public void listerAnnee(Date reference) {
        lister(Periode.ANNEE, reference);
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
