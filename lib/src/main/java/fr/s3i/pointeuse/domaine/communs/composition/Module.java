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

import fr.s3i.pointeuse.domaine.communs.Contexte;

/**
 * Created by Adrien on 20/07/2016.
 */
public abstract class Module extends Contexte {

    private final Contexte contexte;

    public Module(Contexte contexte) {
        this.contexte = contexte;
    }

    public Collection<Composant<?, ?>> getComposants() {
        return cache;
    }

    @Override
    public <I, C extends I> void enregistrerService(Class<I> type, C implementation) {
        super.enregistrerService(type, implementation);
        contexte.enregistrerService(type, implementation);
    }

}
