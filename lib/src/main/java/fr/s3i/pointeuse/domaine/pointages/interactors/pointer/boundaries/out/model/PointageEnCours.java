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

package fr.s3i.pointeuse.domaine.pointages.interactors.pointer.boundaries.out.model;

/**
 * Created by Adrien on 28/07/2016.
 */
public class PointageEnCours {

    private final String libelleJour;

    private final String libelleSemaine;

    private final String libelleMois;

    private final String dureeTotaleJour;

    private final String dureeTotaleSemaine;

    private final String dureeTotaleMois;

    public PointageEnCours(String libelleJour, String libelleSemaine, String libelleMois, String dureeTotaleJour, String dureeTotaleSemaine, String dureeTotaleMois) {
        this.libelleJour = libelleJour;
        this.libelleSemaine = libelleSemaine;
        this.libelleMois = libelleMois;
        this.dureeTotaleJour = dureeTotaleJour;
        this.dureeTotaleSemaine = dureeTotaleSemaine;
        this.dureeTotaleMois = dureeTotaleMois;
    }

    public String getLibelleJour() {
        return libelleJour;
    }

    public String getLibelleSemaine() {
        return libelleSemaine;
    }

    public String getLibelleMois() {
        return libelleMois;
    }

    public String getDureeTotaleJour() {
        return dureeTotaleJour;
    }

    public String getDureeTotaleSemaine() {
        return dureeTotaleSemaine;
    }

    public String getDureeTotaleMois() {
        return dureeTotaleMois;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getLibelleJour());
        builder.append(' ');
        builder.append(getDureeTotaleJour());
        builder.append('\n');
        builder.append(getLibelleSemaine());
        builder.append(' ');
        builder.append(getDureeTotaleSemaine());
        builder.append('\n');
        builder.append(getLibelleMois());
        builder.append(' ');
        builder.append(getDureeTotaleMois());
        return builder.toString();
    }
}
