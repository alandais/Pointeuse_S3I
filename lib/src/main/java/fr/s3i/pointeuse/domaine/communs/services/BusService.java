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

package fr.s3i.pointeuse.domaine.communs.services;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrien on 31/07/2016.
 */
public abstract class BusService implements Service {

    public interface Listener {

        boolean onEvent(Event event);

    }

    public interface Event<T> {

        Object getOriginator();

        String getType();

        T getData();

    }

    public abstract static class BaseEvent<T> implements Event<T> {

        private final Object originator;

        private final String type;

        private final T data;

        public BaseEvent(Object originator, String type) {
            this(originator, type, null);
        }

        public BaseEvent(Object originator, String type, T data) {
            this.originator = originator;
            this.type = type;
            this.data = data;
        }

        @Override
        public Object getOriginator() {
            return originator;
        }

        @Override
        public String getType() {
            return type;
        }

        @Override
        public T getData() {
            return data;
        }

    }

    public static class NoDataEvent extends BaseEvent<Void> {

        public NoDataEvent(Object originator, String type) {
            super(originator, type);
        }

    }

    private final List<Listener> listeners = new ArrayList<>();

    public void subscribe(Listener listener) {
        listeners.add(listener);
    }

    public void unsuscribe(Listener listener) {
        listeners.remove(listener);
    }

    public void post(Object originator, String typeEvent) {
        post(new NoDataEvent(originator, typeEvent));
    }

    public void post(Event event) {
        for (Listener listener : listeners) {
            if (event.getOriginator() != listener && !listener.onEvent(event)) {
                return;
            }
        }
    }

}
