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

package fr.s3i.pointeuse.domaine.pointages.operations;

import java.util.List;

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.communs.interactors.boundaries.out.OutBoundary;
import fr.s3i.pointeuse.domaine.communs.operations.Operation0;
import fr.s3i.pointeuse.domaine.pointages.Chaines;
import fr.s3i.pointeuse.domaine.pointages.entities.Pointage;
import fr.s3i.pointeuse.domaine.pointages.gateways.PointageRepository;

/**
 * Created by Adrien on 03/08/2016.
 */
public class LirePointageRecent extends Operation0<Pointage> {

    private final PointageRepository repository;

    public LirePointageRecent(Contexte contexte, OutBoundary out) {
        super(out);
        this.repository = contexte.getService(PointageRepository.class);
    }

    @Override
    public Pointage executer() {
        Pointage pointage = null;
        List<Pointage> pointages = repository.recupererEnCours();
        if (pointages.size() > 1) {
            // Cas bizarre : il y a plusieurs pointages en cours, on ne plante pas et on corrige la base de données
            out.onErreur(Chaines.erreur_plusieurs_pointage_en_cours);
            for (int i = 0; i < pointages.size() - 1; i++) {
                repository.supprimer(pointages.get(i).getId());
            }
        }
        if (pointages.size() > 0) {
            pointage = pointages.get(pointages.size() - 1);
        }
        return pointage;
    }

}
