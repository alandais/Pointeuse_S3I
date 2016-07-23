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

package fr.s3i.pointeuse.persistance;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.s3i.pointeuse.domaine.pointages.entities.Pointage;
import fr.s3i.pointeuse.domaine.pointages.gateways.PointageRepository;

/**
 * Created by Adrien on 22/07/2016.
 */
public class DatabaseDummy implements PointageRepository {

    private final Map<Long, Pointage> repository = new HashMap<>();

    private long nextId = 0;

    @Override
    public List<Pointage> recupererEntre(Date debut, Date fin) {
        return null;
    }

    @Override
    public List<Pointage> recupererEnCours() {
        List<Pointage> retour = new ArrayList<>();
        if (nextId > 0 && repository.get(nextId-1).getFin() == null) {
            retour.add(repository.get(nextId-1));
        }
        return retour;
    }

    @Override
    public void persister(Pointage entity) {
        if (!repository.containsKey(entity.getId())) {
            entity.setId(nextId);
            nextId++;
        }
        repository.put(entity.getId(), entity);
    }

    @Override
    public void supprimer(Long id) {
        repository.remove(id);
    }

    @Override
    public Pointage recuperer(Long id) {
        return repository.get(id);
    }

    @Override
    public List<Pointage> recupererTout() {
        return new ArrayList<>(repository.values());
    }

}
