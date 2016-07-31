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

package fr.s3i.pointeuse.domaine.pointages.interactors.pointer;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.communs.entities.CasUtilisationInfo;
import fr.s3i.pointeuse.domaine.communs.gateways.NotificationSystem;
import fr.s3i.pointeuse.domaine.communs.interactors.Interactor;
import fr.s3i.pointeuse.domaine.communs.services.BusService;
import fr.s3i.pointeuse.domaine.pointages.Chaines;
import fr.s3i.pointeuse.domaine.pointages.entities.Pointage;
import fr.s3i.pointeuse.domaine.pointages.gateways.PointageRepository;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.in.PointerIn;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.PointerOut;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.model.PointageRecapitulatif;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.model.PointageRecapitulatifFactory;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.model.PointageStatut;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.model.PointageStatutFactory;
import fr.s3i.pointeuse.domaine.pointages.services.BusPointage;
import fr.s3i.pointeuse.domaine.pointages.services.model.PointageWrapper;
import fr.s3i.pointeuse.domaine.pointages.services.model.PointageWrapperFactory;
import fr.s3i.pointeuse.domaine.pointages.services.model.PointageWrapperListe;
import fr.s3i.pointeuse.domaine.pointages.utils.Periode;

/**
 * Created by Adrien on 19/07/2016.
 */
public class PointerInteractor extends Interactor<PointerOut> implements PointerIn, BusService.Listener {

    private final PointageRepository repository;

    private final NotificationSystem notificationSystem;

    private final PointageWrapperFactory pointageWrapperFactory;

    private final PointageStatutFactory pointageStatutFactory;

    private final PointageRecapitulatifFactory pointageRecapitulatifFactory;

    private final BusPointage bus;

    public PointerInteractor(Contexte contexte) {
        super(contexte.getService(PointerOut.class));
        this.repository = contexte.getService(PointageRepository.class);
        this.pointageWrapperFactory = contexte.getService(PointageWrapperFactory.class);
        this.pointageStatutFactory = contexte.getService(PointageStatutFactory.class);
        this.pointageRecapitulatifFactory = contexte.getService(PointageRecapitulatifFactory.class);
        this.notificationSystem = contexte.getService(NotificationSystem.class);
        this.bus = contexte.getService(BusPointage.class);
    }

    @Override
    public void initialiser() {
        CasUtilisationInfo info = new CasUtilisationInfo(Chaines.interacteur_pointer_nom);
        out.onDemarrer(info);

        rafraichirStatut();
        lancerRafraichissementAuto();

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
            rafraichirStatut();
            rafraichirRecapitulatif();
        }
        return true;
    }

    @Override
    public void pointer() {
        Pointage pointage = lireDernierPointage();
        if (pointage != null) {
            // pointage en cours
            pointage.setFin(new Date());
        } else {
            // nouveau pointage
            pointage = new Pointage();
            pointage.setDebut(new Date());
        }

        if (persister(pointage)) {
            lancerRafraichissementAuto();
            PointageWrapper pointageWrapper = pointageWrapperFactory.getPointageWrapper(pointage);
            out.onPointageStatutUpdate(pointageStatutFactory.getStatut(pointageWrapper));
            if (pointageWrapper.isTermine()) {
                out.toast(Chaines.toastPointageComplet(pointageWrapper));
                notificationSystem.notifier(Chaines.notification_titre, Chaines.notificationFinTravail(pointageWrapper));
            } else {
                out.toast(Chaines.toastPointagePartiel(pointageWrapper));
                notificationSystem.notifier(Chaines.notification_titre, Chaines.notificationDebutTravail(pointageWrapper));
            }
        }
    }

    @Override
    public void inserer(Date debut, Date fin, String commentaire) {
        Pointage pointage = new Pointage();
        pointage.setDebut(debut);
        pointage.setFin(fin);
        pointage.setCommentaire(commentaire);

        if (persister(pointage)) {
            out.toast(Chaines.toast_pointage_insere);
            // on rafraichit le récapitulatif sur la vue
            rafraichirRecapitulatif();
        }
    }

    private boolean persister(Pointage pointage) {
        String erreur = pointage.getErrorMessage();
        if (erreur == null) {
            repository.persister(pointage);
            bus.post(this, BusPointage.RAFRAICHIR);
        } else {
            out.onErreur(erreur);
        }
        return erreur == null;
    }

    private Pointage lireDernierPointage() {
        Pointage pointage = null;
        List<Pointage> pointages = repository.recupererEnCours();
        if (pointages.size() > 1) {
            // Cas bizarre : il y a plusieurs pointages en cours, on ne plante pas et on corrige la base de données
            out.onErreur(Chaines.erreur_plusieurs_pointage_en_cours);
            for (int i = 0; i < pointages.size() - 1; i++) {
                repository.supprimer(pointages.get(i).getId());
            }
        }
        if (pointages.size() > 0) {
            pointage = pointages.get(pointages.size() - 1);
        }
        return pointage;
    }

    private void rafraichirStatut() {
        Pointage pointage = lireDernierPointage();
        PointageWrapper pointageWrapper = pointageWrapperFactory.getPointageWrapper(pointage);
        PointageStatut pointageStatut = pointageStatutFactory.getStatut(pointageWrapper);
        out.onPointageStatutUpdate(pointageStatut);
    }

    private boolean rafraichirRecapitulatif() {
        Date maintenant = new Date();

        List<Pointage> pointagesJour = repository.recupererEntre(Periode.JOUR.getDebutPeriode(maintenant), Periode.JOUR.getFinPeriode(maintenant));
        List<Pointage> pointagesSema = repository.recupererEntre(Periode.SEMAINE.getDebutPeriode(maintenant), Periode.SEMAINE.getFinPeriode(maintenant));

        PointageWrapperListe pointagesWrapperJour = pointageWrapperFactory.getPointageWrapper(pointagesJour);
        PointageWrapperListe pointagesWrapperSema = pointageWrapperFactory.getPointageWrapper(pointagesSema);

        PointageRecapitulatif recap = pointageRecapitulatifFactory.getRecapitulatif(pointagesWrapperJour, pointagesWrapperSema);
        out.onPointageRecapitulatifUpdate(recap);

        // retourne true si un rafraichissement auto est nécessaire (si il y a du pointage 'en cours')
        return pointagesWrapperSema.isEnCours() || pointagesWrapperJour.isEnCours();
    }

    private void lancerRafraichissementAuto() {
        // relancer toutes les 30 secondes (si nécessaire)
        if (rafraichirRecapitulatif()) {
            out.executerFutur(new Runnable() {
                @Override
                public void run() {
                    lancerRafraichissementAuto();
                }
            }, 30, TimeUnit.SECONDS);
        }
    }

}
