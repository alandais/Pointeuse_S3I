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

package fr.s3i.pointeuse.service.logging;

import android.util.Log;

import java.text.MessageFormat;

import fr.s3i.pointeuse.domaine.communs.services.logger.Logger;

/**
 * Created by Adrien on 01/08/2016.
 */
public class AndroidLogger implements Logger {

    @Override
    public void verbose(String tag, String message, Object... params) {
        Log.v(tag, getMessage(message, params));
    }

    @Override
    public void debug(String tag, String message, Object... params) {
        Log.d(tag, getMessage(message, params));
    }

    @Override
    public void info(String tag, String message, Object... params) {
        Log.i(tag, getMessage(message, params));
    }

    @Override
    public void warn(String tag, String message, Object... params) {
        Log.w(tag, getMessage(message, params));
    }

    @Override
    public void error(String tag, String message, Object... params) {
        Log.e(tag, getMessage(message, params));
    }

    private String getMessage(String message, Object... params) {
        return MessageFormat.format(message, params);
    }

}
