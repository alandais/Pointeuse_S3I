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

package fr.s3i.pointeuse.domaine.pointages.interactors.statut;

import java.io.IOException;

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.communs.interactors.Interactor;
import fr.s3i.pointeuse.domaine.communs.services.BusService;
import fr.s3i.pointeuse.domaine.communs.services.logger.Log;
import fr.s3i.pointeuse.domaine.pointages.entities.Pointage;
import fr.s3i.pointeuse.domaine.pointages.interactors.statut.boundaries.out.model.PointageStatut;
import fr.s3i.pointeuse.domaine.pointages.interactors.statut.boundaries.out.model.PointageStatutFactory;
import fr.s3i.pointeuse.domaine.pointages.interactors.statut.boundaries.in.StatutIn;
import fr.s3i.pointeuse.domaine.pointages.interactors.statut.boundaries.out.StatutOut;
import fr.s3i.pointeuse.domaine.pointages.operations.LirePointageRecent;
import fr.s3i.pointeuse.domaine.pointages.services.BusPointage;
import fr.s3i.pointeuse.domaine.pointages.services.model.PointageWrapper;
import fr.s3i.pointeuse.domaine.pointages.services.model.PointageWrapperFactory;

/**
 * Created by Adrien on 19/07/2016.
 */
public class StatutInteractor extends Interactor<StatutOut> implements StatutIn, BusService.Listener {

    private final BusPointage bus;

    private final PointageWrapperFactory pointageWrapperFactory;

    private final PointageStatutFactory pointageStatutFactory;

    private final LirePointageRecent lirePointageRecent;

    public StatutInteractor(Contexte contexte, StatutOut out) {
        super(out);
        this.bus = contexte.getService(BusPointage.class);
        this.pointageWrapperFactory = contexte.getService(PointageWrapperFactory.class);
        this.pointageStatutFactory = contexte.getService(PointageStatutFactory.class);
        this.lirePointageRecent = new LirePointageRecent(contexte, out);
    }

    @Override
    public void initialiser() {
        Log.info(Log.LIFECYCLE, "{0} ({1}) initialisation", this.getClass().getSimpleName(), this);
        rafraichirStatut();
        bus.subscribe(this);
    }

    @Override
    public void close() throws IOException {
        super.close();
        bus.unsuscribe(this);
    }

    @Override
    public boolean onEvent(BusService.Event event) {
        if (event instanceof BusPointage.PointageChangedEvent) {
            Log.info(Log.EVENTS, "{0} ({1}) evenement {2} recu de {3}", this.getClass().getSimpleName(), this, event.getType(), event.getOriginator());
            rafraichirStatut();
        }
        return true;
    }

    @Override
    public void rafraichirStatut() {
        Pointage pointage = lirePointageRecent.executer();
        PointageWrapper pointageWrapper = pointageWrapperFactory.getPointageWrapper(pointage);
        PointageStatut pointageStatut = pointageStatutFactory.getStatut(pointageWrapper);
        out.onPointageStatutUpdate(pointageStatut);
    }

}
