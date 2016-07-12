package fr.s3i.pointeuse.business.pointages.gateways;

import java.util.Date;
import java.util.List;

import fr.s3i.pointeuse.business.communs.gateways.Repository;
import fr.s3i.pointeuse.business.pointages.entities.Pointage;

public interface PointageRepository extends Repository<Long, Pointage>
{

    List<Pointage> recuperer(Date debut, Date fin);

    Pointage recupererDernier();

}
