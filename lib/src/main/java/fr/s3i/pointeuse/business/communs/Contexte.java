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

package fr.s3i.pointeuse.business.communs;

import java.util.HashSet;
import java.util.Set;

import fr.s3i.pointeuse.business.communs.composition.Composant;
import fr.s3i.pointeuse.business.communs.composition.Module;

/**
 * Created by Adrien on 20/07/2016.
 */
public class Contexte {

    protected final Set<Composant<?, ?>> cache = new HashSet<>();

    public void enregistrerModule(Module module) {
        for (Composant<?, ?> composant : module.getComposants()) {
            ajouterComposant(composant);
        }
    }

    public <I, C extends I> void enregistrerService(Class<I> type, C implementation) {
        ajouterComposant(Composant.creer(type, implementation));
    }

    public <I> I getService(Class<I> type) {
        Composant<I, ?> composant = retrouverComposant(type);
        if (composant == null) {
            throw new IllegalStateException("Le contexte doit contenir une implémentation de " + type.getSimpleName());
        }
        return composant.getComposant();
    }

    protected void ajouterComposant(Composant<?, ?> composant) {
        if (!cache.add(composant)) {
            throw new IllegalStateException("Le contexte contient déjà une implémentation de " + composant.getType().getSimpleName());
        }
    }

    protected <I> Composant<I, ? extends I> retrouverComposant(Class<I> type) {
        for (Composant<?, ?> composant : cache) {
            if (composant.getType().equals(type)) {
                return (Composant<I, ? extends I>) composant;
            }
        }
        return null;
    }

}
