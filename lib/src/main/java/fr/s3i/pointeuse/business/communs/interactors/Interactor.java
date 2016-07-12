package fr.s3i.pointeuse.business.communs.interactors;

import fr.s3i.pointeuse.business.communs.interactors.boundaries.in.InBoundary;
import fr.s3i.pointeuse.business.communs.interactors.boundaries.out.OutBoundary;

public abstract class Interactor<T extends OutBoundary> implements InBoundary
{

    protected final T out;

    public Interactor(T out)
    {
        this.out = out;
    }

}
