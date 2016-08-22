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

package fr.s3i.pointeuse.domaine.pointages.services.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fr.s3i.pointeuse.domaine.pointages.entities.Pointage;
import fr.s3i.pointeuse.domaine.pointages.services.Calculateur;
import fr.s3i.pointeuse.domaine.pointages.services.Formateur;

/**
 * Created by Adrien on 28/07/2016.
 */
public class PointageWrapperListe {

    private final PointageWrapperFactory pointageWrapperFactory;

    private final Calculateur calculateur;

    private final Formateur formateur;

    private final List<Pointage> pointages = new ArrayList<>();

    public PointageWrapperListe(PointageWrapperFactory pointageWrapperFactory, Calculateur calculateur, Formateur formateur) {
        this.pointageWrapperFactory = pointageWrapperFactory;
        this.calculateur = calculateur;
        this.formateur = formateur;
    }

    public PointageWrapperListe(PointageWrapperFactory pointageWrapperFactory, Calculateur calculateur, Formateur formateur, Collection<Pointage> pointages) {
        this(pointageWrapperFactory, calculateur, formateur);
        addAll(pointages);
    }

    public void add(Pointage pointage) {
        pointages.add(pointage);
    }

    public void addAll(Collection<Pointage> pointage) {
        pointages.addAll(pointage);
    }

    public String getDureeTotale() {
        return formateur.formatDuree(calculateur.calculDureeTotale(pointages));
    }

    public boolean isEnCours() {
        for (Pointage pointage : pointages) {
            if (pointageWrapperFactory.getPointageWrapper(pointage).isEnCours()) {
                return true;
            }
        }
        return false;
    }

    public List<PointageWrapper> getPointages() {
        List<PointageWrapper> resultat = new ArrayList<>();
        for (Pointage pointage : pointages) {
            resultat.add(pointageWrapperFactory.getPointageWrapper(pointage));
        }
        return resultat;
    }

}
