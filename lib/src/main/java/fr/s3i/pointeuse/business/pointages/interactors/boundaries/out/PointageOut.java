package fr.s3i.pointeuse.business.pointages.interactors.boundaries.out;

import fr.s3i.pointeuse.business.communs.interactors.boundaries.out.OutBoundary;
import fr.s3i.pointeuse.business.pointages.interactors.boundaries.out.model.ListePointageInfo;
import fr.s3i.pointeuse.business.pointages.interactors.boundaries.out.model.PointageInfo;

public interface PointageOut extends OutBoundary
{

    void pointage(PointageInfo pointage);

    void listePointages(ListePointageInfo pointages);

    void export(String data);

}
