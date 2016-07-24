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

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.communs.R;
import fr.s3i.pointeuse.domaine.communs.entities.CasUtilisationInfo;
import fr.s3i.pointeuse.domaine.communs.gateways.NotificationSystem;
import fr.s3i.pointeuse.domaine.communs.gateways.ToastSystem;
import fr.s3i.pointeuse.domaine.communs.interactors.Interactor;
import fr.s3i.pointeuse.domaine.pointages.entities.Pointage;
import fr.s3i.pointeuse.domaine.pointages.gateways.PointageRepository;
import fr.s3i.pointeuse.domaine.pointages.interactors.communs.boundaries.out.model.PointageInfo;
import fr.s3i.pointeuse.domaine.pointages.interactors.communs.boundaries.out.model.PointageInfoListe;
import fr.s3i.pointeuse.domaine.pointages.interactors.communs.boundaries.out.translator.PointageInfoListeTranslator;
import fr.s3i.pointeuse.domaine.pointages.interactors.communs.boundaries.out.translator.PointageInfoTranslator;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.in.PointerIn;
import fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.PointerOut;
import fr.s3i.pointeuse.domaine.pointages.services.Periode;

/**
 * Created by Adrien on 19/07/2016.
 */
public class PointerInteractor extends Interactor<PointerOut> implements PointerIn {

    private final PointageRepository repository;

    private final PointageInfoTranslator translator;

    private final PointageInfoListeTranslator listeTranslator;

    private final ToastSystem toastSystem;

    private final NotificationSystem notificationSystem;

    public PointerInteractor(Contexte contexte) {
        super(contexte.getService(PointerOut.class));
        this.repository = contexte.getService(PointageRepository.class);
        this.translator = contexte.getService(PointageInfoTranslator.class);
        this.listeTranslator = contexte.getService(PointageInfoListeTranslator.class);
        this.toastSystem = contexte.getService(ToastSystem.class);
        this.notificationSystem = contexte.getService(NotificationSystem.class);
    }

    @Override
    public void initialiser() {
        CasUtilisationInfo info = new CasUtilisationInfo(R.get("interactor_pointer_nom"));
        out.onDemarrer(info);

        Pointage pointage = lireDernierPointage();
        if (pointage != null) {
            PointageInfo pointageInfo = translator.translate(pointage);
            out.onPointageRapide(pointageInfo);
        }
        else {
            // afficher le résumé de la journée en cours
            // Date today = new Date();
            // List<Pointage> pointages = repository.recupererEntre(Periode.JOUR.getDebutPeriode(today), Periode.JOUR.getFinPeriode(today));
            // PointageInfoListe pointageInfo = listeTranslator.translate(pointages);
            // toastSystem.notifier("Réalisé aujourd'hui : " + pointageInfo.getDureeTotale());
            // TODO : à faire
        }
    }

    @Override
    public void pointer() {
        Pointage pointage;
        List<Pointage> pointages = repository.recupererEnCours();
        if (pointages.size() > 1) {
            // Cas bizarre : il y a plusieurs pointages en cours, on ne plante pas et on corrige la base de données
            out.onErreur("Plusieurs pointages sont en cours, conservation et mise à jour du plus récent uniquement");
            for (int i = 0; i < pointages.size() - 1; i++) {
                repository.supprimer(pointages.get(i).getId());
            }
            pointage = pointages.get(pointages.size() - 1);
        }
        else if (pointages.size() == 1) {
            // Cas normal pointage en cours
            pointage = pointages.get(0);
            pointage.setFin(new Date());
        }
        else {
            // Cas normal nouveau pointage
            pointage = new Pointage();
            pointage.setDebut(new Date());
        }

        PointageInfo pointageInfo = persister(pointage);
        if (pointageInfo != null) {
            out.onPointageRapide(pointageInfo);
            if (pointageInfo.isComplete()) {
                toastSystem.notifier(R.get("toast_pointage_complet", pointageInfo.getHeureFin()));
                notificationSystem.notifier(R.get("notification_titre"), R.get("notification_fin_travail", pointageInfo.getHeureFin(), pointageInfo.getDuree()));
            } else {
                toastSystem.notifier(R.get("toast_pointage_partiel", pointageInfo.getHeureDebut()));
                notificationSystem.notifier(R.get("notification_titre"), R.get("notification_debut_travail", pointageInfo.getHeureDebut()));
            }
        }
    }

    @Override
    public void inserer(Date debut, Date fin, String commentaire) {
        Pointage pointage = new Pointage();
        pointage.setDebut(debut);
        pointage.setFin(fin);
        pointage.setCommentaire(commentaire);

        PointageInfo pointageInfo = persister(pointage);
        if (pointageInfo != null) {
            out.onPointageInsere(pointageInfo);
            toastSystem.notifier(R.get("toast_pointage_insere"));
        }
    }

    private Pointage lireDernierPointage() {
        Pointage pointage = null;
        List<Pointage> pointages = repository.recupererEnCours();
        if (pointages.size() > 1) {
            // Cas bizarre : il y a plusieurs pointages en cours, on ne plante pas et on corrige la base de données
            out.onErreur("Plusieurs pointages sont en cours, conservation et mise à jour du plus récent uniquement");
            for (int i = 0; i < pointages.size() - 1; i++) {
                repository.supprimer(pointages.get(i).getId());
            }
        }
        if (pointages.size() > 0) {
            pointage = pointages.get(pointages.size() - 1);
        }
        return pointage;
    }

    private PointageInfo persister(Pointage pointage) {
        PointageInfo pointageInfo = null;
        String erreur = pointage.getErrorMessage();
        if (erreur == null) {
            repository.persister(pointage);
            pointageInfo = translator.translate(pointage);
        } else {
            out.onErreur(erreur);
            toastSystem.notifier(erreur);
        }
        return pointageInfo;
    }

}
