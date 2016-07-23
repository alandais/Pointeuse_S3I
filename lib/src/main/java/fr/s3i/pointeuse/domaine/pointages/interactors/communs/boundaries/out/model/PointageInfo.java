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

package fr.s3i.pointeuse.domaine.pointages.interactors.communs.boundaries.out.model;

/**
 * Created by Adrien on 19/07/2016.
 */
public class PointageInfo {

    private final long id;

    private final String debut;

    private final String fin;

    private final String heureDebut;

    private final String heureFin;

    private final String duree;

    public PointageInfo(long id, String debut, String fin, String heureDebut, String heureFin, String duree) {
        this.id = id;
        this.debut = debut;
        this.fin = fin;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.duree = duree;
    }

    public long getId() {
        return id;
    }

    public String getDebut() {
        return debut;
    }

    public String getFin() {
        return fin;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public boolean isComplete() {
        return fin != null & !"".equals(fin);
    }

    public String getDuree() {
        return duree;
    }

    @Override
    public String toString() {
        return String.format("debut : %s ; fin : %s", debut, fin);
    }

}
