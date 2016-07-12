package fr.s3i.pointeuse.business.communs.interactors.boundaries.out.model;

import java.util.Collection;
import java.util.List;

import fr.s3i.pointeuse.business.communs.entities.Entity;
import com.aurya.communs.interactors.boundaries.Translator;

public interface OutTranslator<T extends Entity, U> extends Translator<T, U>
{

    U translate(T entity);

    List<U> translate(Collection<T> entities);

}
