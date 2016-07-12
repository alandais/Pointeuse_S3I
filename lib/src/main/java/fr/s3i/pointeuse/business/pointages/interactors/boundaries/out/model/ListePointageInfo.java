package fr.s3i.pointeuse.business.pointages.interactors.boundaries.out.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListePointageInfo
{

    private List<PointageInfo> pointages = new ArrayList<>();

    private String dureeTotale;

    public ListePointageInfo(Collection<PointageInfo> pointages, String dureeTotale)
    {
        this.pointages.addAll(pointages);
    }

    public List<PointageInfo> getPointages()
    {
        return pointages;
    }

    public String getDureeTotale()
    {
        return dureeTotale;
    }

}
