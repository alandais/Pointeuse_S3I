package fr.s3i.pointeuse.business.communs.interactors.boundaries.in.model;

import java.util.Collection;
import java.util.List;

import fr.s3i.pointeuse.business.communs.entities.Entity;
import com.aurya.communs.interactors.boundaries.Translator;

public interface InTranslator<T extends Entity, U> extends Translator<T, U>
{

    T translate(U model);

    List<T> translate(Collection<U> models);

}
