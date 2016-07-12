package fr.s3i.pointeuse.business.communs.entities;

import fr.s3i.pointeuse.business.communs.R;

public abstract class Entity<T>
{

    private T id = null;

    public T getId()
    {
        return id;
    }

    public void setId(T id)
    {
        if (this.id != null)
        {
            throw new IllegalStateException(R.get("erreur1"));
        }
        this.id = id;
    }

    public abstract String getErrorMessage();

}
