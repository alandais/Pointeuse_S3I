/*
 * Oburo.O  un programme dinée à saisir son temps de travail sur un support Android.
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

    private final Long id;

    private final String debut;

    private final String fin;

    private final String heureDebut;

    private final String heureFin;

    private final String duree;

    private final boolean isDemarre;

    private final boolean isArrete;

    private final boolean isVide;

    private final boolean isEnCours;

    private final boolean isTermine;

    private final boolean isValide;

    PointageInfo(Long id, String debut, String fin, String heureDebut, String heureFin, String duree,
                        boolean isDemarre, boolean isArrete, boolean isVide, boolean isEnCours, boolean isTermine, boolean isValide) {
        this.id = id;
        this.debut = debut;
        this.fin = fin;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.duree = duree;
        this.isDemarre = isDemarre;
        this.isArrete = isArrete;
        this.isVide = isVide;
        this.isEnCours = isEnCours;
        this.isTermine = isTermine;
        this.isValide = isValide;
    }

    public Long getId() {
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

    public String getDuree() {
        return duree;
    }

    public boolean isDemarre() {
        return isDemarre;
    }

    public boolean isArrete() {
        return isArrete;
    }

    public boolean isVide() {
        return isVide;
    }

    public boolean isEnCours() {
        return isEnCours;
    }

    public boolean isTermine() {
        return isTermine;
    }

    public boolean isValide() {
        return isValide;
    }

    @Override
    public String toString() {
        return "PointageInfo{" +
                "id=" + id +
                ", debut='" + debut + '\'' +
                ", fin='" + fin + '\'' +
                ", heureDebut='" + heureDebut + '\'' +
                ", heureFin='" + heureFin + '\'' +
                ", duree='" + duree + '\'' +
                ", isDemarre=" + isDemarre +
                ", isArrete=" + isArrete +
                ", isVide=" + isVide +
                ", isEnCours=" + isEnCours +
                ", isTermine=" + isTermine +
                ", isValide=" + isValide +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PointageInfo that = (PointageInfo) o;

        if (isDemarre != that.isDemarre) return false;
        if (isArrete != that.isArrete) return false;
        if (isVide != that.isVide) return false;
        if (isEnCours != that.isEnCours) return false;
        if (isTermine != that.isTermine) return false;
        if (isValide != that.isValide) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (debut != null ? !debut.equals(that.debut) : that.debut != null) return false;
        if (fin != null ? !fin.equals(that.fin) : that.fin != null) return false;
        if (heureDebut != null ? !heureDebut.equals(that.heureDebut) : that.heureDebut != null)
            return false;
        if (heureFin != null ? !heureFin.equals(that.heureFin) : that.heureFin != null)
            return false;
        return duree != null ? duree.equals(that.duree) : that.duree == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (debut != null ? debut.hashCode() : 0);
        result = 31 * result + (fin != null ? fin.hashCode() : 0);
        result = 31 * result + (heureDebut != null ? heureDebut.hashCode() : 0);
        result = 31 * result + (heureFin != null ? heureFin.hashCode() : 0);
        result = 31 * result + (duree != null ? duree.hashCode() : 0);
        result = 31 * result + (isDemarre ? 1 : 0);
        result = 31 * result + (isArrete ? 1 : 0);
        result = 31 * result + (isVide ? 1 : 0);
        result = 31 * result + (isEnCours ? 1 : 0);
        result = 31 * result + (isTermine ? 1 : 0);
        result = 31 * result + (isValide ? 1 : 0);
        return result;
    }
}
