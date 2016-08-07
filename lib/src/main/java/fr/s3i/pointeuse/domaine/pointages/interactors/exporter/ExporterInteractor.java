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

package fr.s3i.pointeuse.domaine.pointages.interactors.exporter;

import java.util.Date;
import java.util.List;

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.communs.interactors.Interactor;
import fr.s3i.pointeuse.domaine.pointages.Chaines;
import fr.s3i.pointeuse.domaine.pointages.entities.Pointage;
import fr.s3i.pointeuse.domaine.pointages.gateways.PointageEnvoiFichier;
import fr.s3i.pointeuse.domaine.pointages.gateways.PointagePreferences;
import fr.s3i.pointeuse.domaine.pointages.gateways.PointageRepository;
import fr.s3i.pointeuse.domaine.pointages.interactors.exporter.in.ExporterIn;
import fr.s3i.pointeuse.domaine.pointages.interactors.exporter.out.ExporterOut;
import fr.s3i.pointeuse.domaine.pointages.interactors.lister.boundaries.out.model.PointageInfo;
import fr.s3i.pointeuse.domaine.pointages.interactors.lister.boundaries.out.model.PointageInfoListe;
import fr.s3i.pointeuse.domaine.pointages.interactors.lister.boundaries.out.model.PointageInfoListeFactory;
import fr.s3i.pointeuse.domaine.pointages.services.model.PointageWrapperFactory;
import fr.s3i.pointeuse.domaine.pointages.services.model.PointageWrapperListe;
import fr.s3i.pointeuse.domaine.pointages.utils.Periode;

/**
 * Created by Adrien on 07/08/2016.
 */
public class ExporterInteractor extends Interactor<ExporterOut> implements ExporterIn {

    private final PointageRepository repository;

    private final PointagePreferences preferences;

    private final PointageWrapperFactory pointageWrapperFactory;

    private final PointageInfoListeFactory pointageInfoListeFactory;

    private final PointageEnvoiFichier envoiFichier;

    public ExporterInteractor(Contexte contexte, ExporterOut out) {
        super(out);
        this.repository = contexte.getService(PointageRepository.class);
        this.preferences = contexte.getService(PointagePreferences.class);
        this.pointageWrapperFactory = contexte.getService(PointageWrapperFactory.class);
        this.pointageInfoListeFactory = contexte.getService(PointageInfoListeFactory.class);
        this.envoiFichier = contexte.getService(PointageEnvoiFichier.class, null);
    }

    @Override
    public void exporterJour(Date reference) {
        exporter(Periode.JOUR, reference);
    }

    @Override
    public void exporterSemaine(Date reference) {
        exporter(Periode.SEMAINE, reference);
    }

    @Override
    public void exporterMois(Date reference) {
        exporter(Periode.MOIS, reference);
    }

    @Override
    public void exporterAnnee(Date reference) {
        exporter(Periode.ANNEE, reference);
    }

    private void exporter(Periode periode, Date reference) {
        if (envoiFichier != null) {
            Date debutPeriode = periode.getDebutPeriode(reference);
            Date finPeriode = periode.getFinPeriode(reference);

            List<Pointage> pointages = repository.recupererEntre(debutPeriode, finPeriode);
            PointageWrapperListe pointageWrapperListe = pointageWrapperFactory.getPointageWrapper(pointages);
            PointageInfoListe pointageInfoListe = pointageInfoListeFactory.getPointageInfoListe(pointageWrapperListe);

            StringBuilder export = new StringBuilder();
            for (PointageInfo pointageInfo : pointageInfoListe.getListePointageInfo())
            {
                export.append(pointageInfo.getId());
                export.append(preferences.getExportSeparateur());
                export.append(pointageInfo.getDebut());
                export.append(preferences.getExportSeparateur());
                export.append(pointageInfo.getFin());
                export.append(preferences.getExportSeparateur());
                export.append(pointageInfo.getDuree());
                export.append('\n');
            }
            envoiFichier.envoyer(preferences.getExportDestinataire(),
                    Chaines.mailSujet(periode, reference),
                    Chaines.mail_corps,
                    Chaines.mailNomPieceJointe(periode, reference),
                    export.toString().getBytes());
            out.toast(Chaines.toast_export_termine);
            out.onExportTermine();
        }
        else {
            out.onErreur(Chaines.erreur_export_indisponible);
        }
    }

}
