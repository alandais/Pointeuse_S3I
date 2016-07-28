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

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.communs.R;
import fr.s3i.pointeuse.domaine.communs.entities.CasUtilisationInfo;
import fr.s3i.pointeuse.domaine.communs.gateways.NotificationSystem;
import fr.s3i.pointeuse.domaine.communs.interactors.Interactor;
import fr.s3i.pointeuse.domaine.pointages.entities.Pointage;
import fr.s3i.pointeuse.domaine.pointages.gateways.PointageRepository;
import fr.s3i.pointeuse.domaine.pointages.interactors.communs.boundaries.out.model.PointageInfo;
import fr.s3i.pointeuse.domaine.pointages.interactors.communs.boundaries.out.model.PointageInfoFactory;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.in.PointerIn;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.PointerOut;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.model.PointageEnCours;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.model.PointageEnCoursFactory;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.model.PointageRapide;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.model.PointageRapideFactory;
import fr.s3i.pointeuse.domaine.pointages.services.model.PointageWrapper;
import fr.s3i.pointeuse.domaine.pointages.services.model.PointageWrapperFactory;
import fr.s3i.pointeuse.domaine.pointages.utils.Periode;

/**
 * Created by Adrien on 19/07/2016.
 */
public class PointerInteractor extends Interactor<PointerOut> implements PointerIn {

    private final PointageRepository repository;

    private final NotificationSystem notificationSystem;

    private final PointageWrapperFactory pointageWrapperFactory;

    private final PointageRapideFactory pointageRapideFactory;

    private final PointageEnCoursFactory pointageEnCoursFactory;

    private final PointageInfoFactory pointageInfoFactory;

    public PointerInteractor(Contexte contexte) {
        super(contexte.getService(PointerOut.class));
        this.repository = contexte.getService(PointageRepository.class);
        this.pointageWrapperFactory = contexte.getService(PointageWrapperFactory.class);
        this.pointageRapideFactory = contexte.getService(PointageRapideFactory.class);
        this.pointageEnCoursFactory = contexte.getService(PointageEnCoursFactory.class);
        this.pointageInfoFactory = contexte.getService(PointageInfoFactory.class);
        this.notificationSystem = contexte.getService(NotificationSystem.class);
    }

    @Override
    public void initialiser() {
        CasUtilisationInfo info = new CasUtilisationInfo(R.get("interactor_pointer_nom"));
        out.onDemarrer(info);

        Pointage pointage = lireDernierPointage();
        PointageRapide pointageRapide = pointageRapideFactory.getPointageRapide(pointage);
        out.onPointageRapide(pointageRapide);

        rafraichirEnCours();
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

        PointageWrapper pointageWrapper = persister(pointage);
        if (pointageWrapper != null) {
            PointageRapide pointageRapide = pointageRapideFactory.getPointageRapide(pointageWrapper);
            out.onPointageRapide(pointageRapide);
            if (pointageWrapper.isTermine()) {
                out.toast(R.get("toast_pointage_complet", pointageWrapper.getHeureFin()));
                notificationSystem.notifier(R.get("notification_titre"), R.get("notification_fin_travail", pointageWrapper.getHeureFin(), pointageWrapper.getDuree()));
            } else {
                out.toast(R.get("toast_pointage_partiel", pointageWrapper.getHeureDebut()));
                notificationSystem.notifier(R.get("notification_titre"), R.get("notification_debut_travail", pointageWrapper.getHeureDebut()));
            }
        }
    }

    @Override
    public void inserer(Date debut, Date fin, String commentaire) {
        Pointage pointage = new Pointage();
        pointage.setDebut(debut);
        pointage.setFin(fin);
        pointage.setCommentaire(commentaire);

        PointageWrapper pointageWrapper = persister(pointage);
        if (pointageWrapper != null) {
            PointageInfo pointageInfo = pointageInfoFactory.getPointageInfo(pointageWrapper);
            out.onPointageInsere(pointageInfo);
            out.toast(R.get("toast_pointage_insere"));
        }
    }

    private Pointage lireDernierPointage() {
        Pointage pointage = null;
        List<Pointage> pointages = repository.recupererEnCours();
        if (pointages.size() > 1) {
            // Cas bizarre : il y a plusieurs pointages en cours, on ne plante pas et on corrige la base de données
            out.onErreur(R.get("erreur6"));
            for (int i = 0; i < pointages.size() - 1; i++) {
                repository.supprimer(pointages.get(i).getId());
            }
        }
        if (pointages.size() > 0) {
            pointage = pointages.get(pointages.size() - 1);
        }
        return pointage;
    }

    private PointageWrapper persister(Pointage pointage) {
        PointageWrapper pointageWrapper = null;
        String erreur = pointage.getErrorMessage();
        if (erreur == null) {
            repository.persister(pointage);
            pointageWrapper = pointageWrapperFactory.getPointageWrapper(pointage);
        } else {
            out.onErreur(erreur);
        }
        return pointageWrapper;
    }

    private void rafraichirEnCours() {
        Date maintenant = new Date();
        List<Pointage> pointagesJour = repository.recupererEntre(Periode.JOUR.getDebutPeriode(maintenant), Periode.JOUR.getFinPeriode(maintenant));
        List<Pointage> pointagesSema = repository.recupererEntre(Periode.SEMAINE.getDebutPeriode(maintenant), Periode.SEMAINE.getFinPeriode(maintenant));
        List<Pointage> pointagesMois = repository.recupererEntre(Periode.MOIS.getDebutPeriode(maintenant), Periode.MOIS.getFinPeriode(maintenant));

        PointageEnCours enCours = pointageEnCoursFactory.getPointageEnCours(pointagesJour, pointagesSema, pointagesMois);
        out.onPointageEnCours(enCours);
        // on relance le rafraichissement dans 1 minute
        out.executerFutur(new Runnable() {
            @Override
            public void run() {
                rafraichirEnCours();
            }
        }, 1, TimeUnit.MINUTES);
    }

}
