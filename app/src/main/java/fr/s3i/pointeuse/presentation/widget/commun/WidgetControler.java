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

package fr.s3i.pointeuse.presentation.widget.commun;

import java.io.IOException;

import fr.s3i.pointeuse.domaine.communs.interactors.boundaries.in.InBoundary;

/**
 * Created by Adrien on 06/08/2016.
 */
public abstract class WidgetControler implements InBoundary {

    @Override
    public final void initialiser() {
        throw new IllegalStateException("Le widget doit être stateless (interdiction d'appeler les méthodes initialiser() et close() des interacteurs)");
    }

    @Override
    public final void close() throws IOException {
        throw new IllegalStateException("Le widget doit être stateless (interdiction d'appeler les méthodes initialiser() et close() des interacteurs)");
    }

}
