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

package fr.s3i.pointeuse.service.mail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import fr.s3i.pointeuse.domaine.pointages.gateways.PointageEnvoiFichier;

/**
 * Created by Adrien on 07/08/2016.
 */
public class MailService implements PointageEnvoiFichier {

    private final Context context;

    public MailService(Context context) {
        this.context = context;
    }

    @Override
    public void envoyer(String destinataire, String sujet, String corps, String nomFichier, byte[] pieceJointe) {
        try {
            File directory = new File(context.getCacheDir(), "pointages_exportes");
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    throw new IOException("Impossible de créer le dossier " + directory);
                }
            }
            File file = new File(directory, nomFichier);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(pieceJointe);
            fos.close();

            Uri exportUri = FileProvider.getUriForFile(context, context.getPackageName(), file);

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{destinataire});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, sujet);
            emailIntent.putExtra(Intent.EXTRA_TEXT, corps);
            emailIntent.putExtra(Intent.EXTRA_STREAM, exportUri);
            emailIntent.setData(exportUri);
            emailIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Intent emailChooser = Intent.createChooser(emailIntent, "TODO");
            emailChooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(emailChooser);
        }
        catch (IOException ex) {
            Log.e(MailService.class.getSimpleName(), "L'export a échoué", ex);
        }
    }

}
