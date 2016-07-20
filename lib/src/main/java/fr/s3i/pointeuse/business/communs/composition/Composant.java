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

package fr.s3i.pointeuse.business.communs.composition;

/**
 * Created by Adrien on 20/07/2016.
 */
public class Composant<I, C extends I> {

    public static <I, C extends I> Composant<I, C> creer(Class<I> type, C implementation) {
        return new Composant<>(type, implementation);
    }

    private final Class<I> type;

    private final C composant;

    private Composant(Class<I> type, C composant) {
        this.type = type;
        this.composant = composant;
    }

    public Class<I> getType() {
        return type;
    }

    public C getComposant() {
        return composant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Composant<?, ?> composant = (Composant<?, ?>) o;

        return type != null ? type.equals(composant.type) : composant.type == null;
    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }
}
