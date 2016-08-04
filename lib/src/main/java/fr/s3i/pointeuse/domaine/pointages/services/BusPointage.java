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

package fr.s3i.pointeuse.domaine.pointages.services;

import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.domaine.communs.services.BusService;
import fr.s3i.pointeuse.domaine.pointages.entities.Pointage;

/**
 * Created by Adrien on 31/07/2016.
 */
public class BusPointage extends BusService {

    public BusPointage(Contexte contexte) {
        super(contexte);
    }

    public static class RefreshStatutEvent extends BaseEvent<Pointage> {

        public RefreshStatutEvent(Object originator, Pointage data) {
            super(originator, RefreshStatutEvent.class.getName(), data);
        }

    }

    public static class RefreshRecapitulatifEvent extends BaseEvent<Pointage> {

        public RefreshRecapitulatifEvent(Object originator, Pointage data) {
            super(originator, RefreshRecapitulatifEvent.class.getName(), data);
        }

    }

    public static class RefreshListePointageEvent extends BaseEvent<Pointage> {

        public RefreshListePointageEvent(Object originator, Pointage data) {
            super(originator, RefreshListePointageEvent.class.getName(), data);
        }

    }

    public static class WakeupRecapitulatifEvent extends BaseEvent<Pointage> {

        public WakeupRecapitulatifEvent(Object originator, Pointage data) {
            super(originator, WakeupRecapitulatifEvent.class.getName(), data);
        }

    }

}
