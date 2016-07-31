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

import java.io.IOException;
import java.util.Date;
import java.util.List;

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.communs.R;
import fr.s3i.pointeuse.domaine.communs.entities.CasUtilisationInfo;
import fr.s3i.pointeuse.domaine.communs.interactors.Interactor;
import fr.s3i.pointeuse.domaine.communs.services.BusService;
import fr.s3i.pointeuse.domaine.pointages.entities.Pointage;
import fr.s3i.pointeuse.domaine.pointages.gateways.PointageRepository;
import fr.s3i.pointeuse.domaine.pointages.interactors.calendrier.boundaries.in.CalendrierIn;
import fr.s3i.pointeuse.domaine.pointages.interactors.calendrier.boundaries.out.CalendrierOut;
import fr.s3i.pointeuse.domaine.pointages.interactors.calendrier.boundaries.out.model.PointageInfoListe;
import fr.s3i.pointeuse.domaine.pointages.interactors.calendrier.boundaries.out.model.PointageInfoListeFactory;
import fr.s3i.pointeuse.domaine.pointages.services.BusPointage;
import fr.s3i.pointeuse.domaine.pointages.services.model.PointageWrapperFactory;
import fr.s3i.pointeuse.domaine.pointages.services.model.PointageWrapperListe;
import fr.s3i.pointeuse.domaine.pointages.utils.Periode;

/**
 * Created by Adrien on 30/07/2016.
 */
public class CalendrierInteractor extends Interactor<CalendrierOut> implements CalendrierIn, BusService.Listener {

    private final PointageRepository repository;

    private final PointageWrapperFactory pointageWrapperFactory;

    private final PointageInfoListeFactory pointageInfoListeFactory;

    private final BusPointage bus;

    public CalendrierInteractor(Contexte contexte) {
        super(contexte.getService(CalendrierOut.class));
        this.repository = contexte.getService(PointageRepository.class);
        this.pointageWrapperFactory = contexte.getService(PointageWrapperFactory.class);
        this.pointageInfoListeFactory = contexte.getService(PointageInfoListeFactory.class);
        this.bus = contexte.getService(BusPointage.class);
    }

    @Override
    public void initialiser() {
        CasUtilisationInfo info = new CasUtilisationInfo(R.get("interactor_calendrier_nom"));
        out.onDemarrer(info);
        out.updatePointageInfoListe();

        bus.subscribe(this);
    }

    @Override
    public void close() throws IOException {
        super.close();
        bus.unsuscribe(this);
    }

    @Override
    public boolean onEvent(BusService.Event event) {
        if (BusPointage.RAFRAICHIR.equals(event.getType())) {
            out.updatePointageInfoListe();
        }
        return true;
    }

    @Override
    public void supprimer(long id) {
        repository.supprimer(id);
        out.toast(R.get("toast_pointage_supprime"));
        out.updatePointageInfoListe();
        bus.post(this, BusPointage.RAFRAICHIR);
    }

    @Override
    public void modifier(long id) {
        Pointage pointage = repository.recuperer(id);
        out.onPointageModication(pointage);
    }

    @Override
    public void modifier(long id, Date debut, Date fin, String commentaire) {
        Pointage pointage = repository.recuperer(id);
        pointage.setDebut(debut);
        pointage.setFin(fin);
        pointage.setCommentaire(commentaire);
        if (persister(pointage)) {
            out.toast(R.get("toast_pointage_modifie"));
            out.updatePointageInfoListe();
            bus.post(this, BusPointage.RAFRAICHIR);
        }
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

    private boolean persister(Pointage pointage) {
        String erreur = pointage.getErrorMessage();
        if (erreur == null) {
            repository.persister(pointage);
        } else {
            out.onErreur(erreur);
        }
        return erreur == null;
    }

}
