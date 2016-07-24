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

package fr.s3i.pointeuse.domaine.communs.entities;

/**
 * Created by Adrien on 24/07/2016.
 * Entité non persistable.
 */
public class CasUtilisationInfo extends Entity<Void> {

    private final String nom;

    public CasUtilisationInfo(String nom) {
        this.nom = nom;
    }

    @Override
    public String getErrorMessage() {
        return null;
    }

    public String getNom() {
        return nom;
    }

}
