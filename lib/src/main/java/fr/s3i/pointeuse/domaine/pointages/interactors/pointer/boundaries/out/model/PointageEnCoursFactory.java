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

import java.util.Collection;

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.communs.R;
import fr.s3i.pointeuse.domaine.communs.services.Service;
import fr.s3i.pointeuse.domaine.pointages.entities.Pointage;
import fr.s3i.pointeuse.domaine.pointages.services.model.PointageWrapperFactory;

/**
 * Created by Adrien on 26/07/2016.
 */
public class PointageEnCoursFactory implements Service {

    private final PointageWrapperFactory pointageWrapperFactory;

    public PointageEnCoursFactory(Contexte contexte) {
        pointageWrapperFactory = contexte.getService(PointageWrapperFactory.class);
    }

    public PointageEnCours getPointageEnCours(Collection<Pointage> jour, Collection<Pointage> semaine, Collection<Pointage> mois) {
        String libelleJour = R.get("libelle_pointage_encours_duree_jour");
        String libelleSemaine = R.get("libelle_pointage_encours_duree_semaine");
        String libelleMois = R.get("libelle_pointage_encours_duree_mois");
        String dureeJour = pointageWrapperFactory.getPointageWrapper(jour).getDureeTotale();
        String dureeSemaine = pointageWrapperFactory.getPointageWrapper(semaine).getDureeTotale();
        String dureeMois = pointageWrapperFactory.getPointageWrapper(mois).getDureeTotale();
        return new PointageEnCours(libelleJour, libelleSemaine, libelleMois, dureeJour, dureeSemaine, dureeMois);
    }

}
