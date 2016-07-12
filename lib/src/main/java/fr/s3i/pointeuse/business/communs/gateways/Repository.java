package fr.s3i.pointeuse.business.communs.gateways;

import java.util.List;

import fr.s3i.pointeuse.business.communs.entities.Entity;

public interface Repository<T, U extends Entity<T>>
{

    void persister(U entity);

    U recuperer(T id);

    List<U> recupererTout();

}
