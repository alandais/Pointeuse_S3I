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

package fr.s3i.pointeuse.domaine.communs.services.logger;

/**
 * Created by Adrien on 01/08/2016.
 */
public final class Log {

    private Log() {
        // rien
    }

    private static Logger LOGGER = new DefaultLogger();

    public static final String DATAFLOW = "Dataflow";
    public static final String LIFECYCLE = "Lifecycle";
    public static final String STATE = "State";
    public static final String EVENTS = "Events";

    public static void setLogger(Logger logger) {
        LOGGER = logger;
    }

    public static void verbose(String tag, String message, Object... params) {
        LOGGER.verbose(tag, message, params);
    }

    public static void debug(String tag, String message, Object... params) {
        LOGGER.debug(tag, message, params);
    }

    public static void info(String tag, String message, Object... params) {
        LOGGER.info(tag, message, params);
    }

    public static void warn(String tag, String message, Object... params) {
        LOGGER.warn(tag, message, params);
    }

    public static void error(String tag, String message, Object... params) {
        LOGGER.error(tag, message, params);
    }

}