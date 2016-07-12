package fr.s3i.pointeuse.business.pointages.interactors.boundaries.out.model;

public class PointageInfo
{

    private final long id;

    private final String debut;

    private final String fin;

    private final String duree;

    public PointageInfo(long id, String debut, String fin, String duree)
    {
        this.id = id;
        this.debut = debut;
        this.fin = fin;
        this.duree = duree;
    }

    public long getId()
    {
        return id;
    }

    public String getDebut()
    {
        return debut;
    }

    public String getFin()
    {
        return fin;
    }

    public boolean isComplete()
    {
        return fin != null & !"".equals(fin);
    }

    public String getDuree()
    {
        return duree;
    }

}
