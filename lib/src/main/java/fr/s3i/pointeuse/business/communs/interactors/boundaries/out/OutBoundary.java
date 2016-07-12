package fr.s3i.pointeuse.business.communs.interactors.boundaries.out;

import com.aurya.communs.interactors.boundaries.Boundary;

public interface OutBoundary extends Boundary
{

    void onError(String message);

}
