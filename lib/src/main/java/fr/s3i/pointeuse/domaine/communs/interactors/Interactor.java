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

package fr.s3i.pointeuse.domaine.communs.interactors;

import java.io.IOException;

import fr.s3i.pointeuse.domaine.communs.interactors.boundaries.in.InBoundary;
import fr.s3i.pointeuse.domaine.communs.interactors.boundaries.out.OutBoundary;

public abstract class Interactor<T extends OutBoundary> implements InBoundary {

    protected final T out;

    public Interactor(T out) {
        this.out = out;
    }

    @Override
    public void close() throws IOException {

    }
}
