/*
 * Oburo.O est un programme destinée à saisir son temps de travail sur un support Android.
 *
 *     This file is part of Oburo.O
 *     Oburo.O is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package fr.s3i.pointeuse.domaine.communs.composition;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.communs.entities.CasUtilisationInfo;
import fr.s3i.pointeuse.domaine.communs.interactors.boundaries.in.InBoundary;

/**
 * Created by Adrien on 20/07/2016.
 */
public abstract class Module extends Contexte {

    private final Contexte contexte;

    private final Map<Class<? extends InBoundary>, CasUtilisationInfo> interacteurs = new HashMap<>();

    public Module(Contexte contexte) {
        this.contexte = contexte;
    }

    public Collection<Composant<?, ?>> getComposants() {
        return cache;
    }

    public CasUtilisationInfo getInteracteurInfo(Class<? extends InBoundary> type) {
        for (Map.Entry<Class<? extends InBoundary>, CasUtilisationInfo> entry : interacteurs.entrySet()) {
            if (entry.getKey().isAssignableFrom(type)) {
                return entry.getValue();
            }
        }
        return null;
    }

    protected void enregistrerInteracteur(Class<? extends InBoundary> type, CasUtilisationInfo info) {
        interacteurs.put(type, info);
    }

    @Override
    public <I, C extends I> void enregistrerService(Class<I> type, C implementation) {
        super.enregistrerService(type, implementation);
        contexte.enregistrerService(type, implementation);
    }

}
