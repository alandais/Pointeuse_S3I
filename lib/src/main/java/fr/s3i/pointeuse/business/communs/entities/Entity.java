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
