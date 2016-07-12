package fr.s3i.pointeuse.business.pointages.interactors;

import java.util.Date;
import java.util.List;

import fr.s3i.pointeuse.business.communs.R;
import fr.s3i.pointeuse.business.communs.interactors.Interactor;
import fr.s3i.pointeuse.business.pointages.entities.Pointage;
import fr.s3i.pointeuse.business.pointages.gateways.PointageEnvoiFichier;
import fr.s3i.pointeuse.business.pointages.gateways.PointagePreferences;
import fr.s3i.pointeuse.business.pointages.gateways.PointageRepository;
import fr.s3i.pointeuse.business.pointages.interactors.boundaries.in.PointageIn;
import fr.s3i.pointeuse.business.pointages.interactors.boundaries.out.PointageOut;
import fr.s3i.pointeuse.business.pointages.interactors.boundaries.out.model.ListePointageInfo;
import fr.s3i.pointeuse.business.pointages.interactors.boundaries.out.model.PointageInfo;
import fr.s3i.pointeuse.business.pointages.interactors.boundaries.out.model.PointageTranslator;
import fr.s3i.pointeuse.business.pointages.interactors.internal.Periode;

public class PointageInteractor extends Interactor<PointageOut> implements PointageIn
{

    private final PointageRepository repository;

    private final PointagePreferences preferences;

    private final PointageTranslator translator;

    /**
     * Ce module est optionnel et doit être positionné avec {@link #setPointageEnvoi(PointageEnvoiFichier)}.
     */
    private PointageEnvoiFichier envoi = null;

    public PointageInteractor(PointageOut out, PointageRepository repository, PointagePreferences preferences)
    {
        super(out);
        this.repository = repository;
        this.preferences = preferences;
        this.translator = new PointageTranslator(this.preferences);
    }

    @Override
    public void pointer()
    {
        Pointage pointage = repository.recupererDernier();
        if (pointage != null && pointage.getFin() == null)
        {
            pointage.setFin(new Date());
        }
        else
        {
            pointage = new Pointage(new Date());
        }
        persister(pointage);
    }

    @Override
    public void listerJour(Date date)
    {
        lister(date, Periode.JOUR);
    }

    @Override
    public void listerSemaine(Date date)
    {
        lister(date, Periode.SEMAINE);
    }

    @Override
    public void listerMois(Date date)
    {
        lister(date, Periode.MOIS);
    }

    @Override
    public void listerAnnee(Date date)
    {
        lister(date, Periode.ANNEE);
    }

    @Override
    public void modifierCommentaire(long id, String commentaire)
    {
        Pointage pointage = repository.recuperer(id);
        pointage.setCommentaire(commentaire);
        persister(pointage);
    }

    @Override
    public void modifierPointage(long id, Date debut, Date fin)
    {
        Pointage pointage = repository.recuperer(id);
        pointage.setDebut(debut);
        pointage.setFin(fin);
        persister(pointage);
    }

    @Override
    public void modifierDebut(long id, Date debut)
    {
        Pointage pointage = repository.recuperer(id);
        pointage.setDebut(debut);
        persister(pointage);
    }

    @Override
    public void modifierFin(long id, Date fin)
    {
        Pointage pointage = repository.recuperer(id);
        pointage.setFin(fin);
        persister(pointage);
    }

    @Override
    public void modifier(long id, Date debut, Date fin, String commentaire)
    {
        Pointage pointage = repository.recuperer(id);
        pointage.setDebut(debut);
        pointage.setFin(fin);
        pointage.setCommentaire(commentaire);
        persister(pointage);
    }

    @Override
    public void exporter()
    {
        if (envoi != null)
        {
            StringBuilder export = new StringBuilder();
            List<Pointage> pointages = repository.recupererTout();
            for (Pointage pointage : pointages)
            {
                PointageInfo infos = translator.translate(pointage);
                export.append(infos.getId());
                export.append(preferences.getExportSeparateur());
                export.append(infos.getDebut());
                export.append(preferences.getExportSeparateur());
                export.append(infos.getFin());
                export.append(preferences.getExportSeparateur());
                export.append(infos.getDuree());
                export.append(System.lineSeparator());
            }
            envoi.envoyer(preferences.getExportDestinataire(), R.get("mail_sujet"), R.get("mail_corps"), R.get("mail_nom_piece_jointe"),
                    export.toString().getBytes());
            out.export(export.toString());
        }
        else
        {
            out.onError(R.get("erreur4"));
        }
    }

    public void setPointageEnvoi(PointageEnvoiFichier envoi)
    {
        this.envoi = envoi;
    }

    private void lister(Date reference, Periode periode)
    {
        Date debut = periode.getDebutPeriode(reference);
        Date fin = periode.getFinPeriode(reference);
        lister(debut, fin);
    }

    private void lister(Date debut, Date fin)
    {
        List<Pointage> pointages = repository.recuperer(debut, fin);
        ListePointageInfo pointageInfos = translator.translateListe(pointages);
        out.listePointages(pointageInfos);
    }

    private void persister(Pointage pointage)
    {
        String erreur = pointage.getErrorMessage();
        if (erreur == null)
        {
            repository.persister(pointage);
            out.pointage(translator.translate(pointage));
        }
        else
        {
            out.onError(erreur);
        }
    }

}
