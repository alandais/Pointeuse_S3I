package fr.s3i.pointeuse.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import fr.s3i.pointeuse.persistance.DatabaseHelper;
import fr.s3i.pointeuse.R;

public class Utilitaire {
    public final static String FILENAME = "PointeuseS3I.dat";


    public static void sendbymail(Context leContext) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(leContext);
        String email = "inconnu";
        try {
            email = preferences.getString("email", "inconnu");
        } catch (Exception All) {
            return;
            //Toast.makeText(this, "Echec=" + All.getMessage() , Toast.LENGTH_SHORT).show();
        }

        if (email.equals("inconnu")) {
            Toast.makeText(leContext, leContext.getString(R.string.err_email2), Toast.LENGTH_SHORT).show();
            return;
        }

        String[] arrayEmail = {email};

        if (genereLaListe(leContext) != 0) {
            Toast.makeText(leContext, leContext.getString(R.string.err_email5), Toast.LENGTH_SHORT).show();
            return;
        }

        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setType("text/plain");

        //emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse
        //       ("file://"+ leContext.getFilesDir().getAbsolutePath() +"/" + Utilitaire.FILENAME));

        try {
            copyFile(new File(leContext.getFilesDir(), Utilitaire.FILENAME), new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), Utilitaire.FILENAME));
            //emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse
            //           ("file://"+leContext.getFilesDir()+"/"+Utilitaire.FILENAME));
            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse
                    ("file://" + Environment.getExternalStorageDirectory().getAbsoluteFile() + "/" + Utilitaire.FILENAME));
        } catch (Exception e) {
            //   android.util.Log.w("erreur", "Erreur = " + e.getMessage() );
            //	return;
        }

        if (!email.equals("inconnu"))
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayEmail);

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, leContext.getString(R.string.savepointage));
        emailIntent.putExtra(Intent.EXTRA_TEXT, leContext.getString(R.string.savepointage));
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        leContext.startActivity(Intent.createChooser(emailIntent, leContext.getString(R.string.choisir)));

    }

    public static void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }

    public static int genereLaListe(Context leContext) {

        int erreur;
        String Separateur = ";";

        int numero = 1;
        erreur = 0;
        SQLiteDatabase db;
        Cursor constantsCursor = null;
        DatabaseHelper dbHelper = null;
        SimpleDateFormat newdateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat olddateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String debut = "", fin = "";
        Date date;
        FileOutputStream fos;
        String buffer;
        String commentaire = "";

        //String myPath = leContext.getFilesDir().getAbsolutePath();
        //Log.e("myPath",myPath);

        date = new Date();

        dbHelper = new DatabaseHelper(leContext);
        db = dbHelper.getWritableDatabase();

        constantsCursor = dbHelper.getAllPointage(db);
        if (constantsCursor == null) {
            erreur = 1;
            return erreur;
        }

        //android.util.Log.w("EXTRA_STREAM",leContext.getFilesDir() + "  "+  leContext.getFilesDir().getAbsolutePath()   );

        try {

            fos = leContext.openFileOutput(FILENAME, Context.MODE_WORLD_READABLE);

            //buffer = "\"Liste des pointages du " +  (String)newdateFormat.format(date) +"\""+Separateur ;
            buffer = "\"" + leContext.getString(R.string.listeP) + " " + (String) newdateFormat.format(date) + "\"" + Separateur;
            fos.write(buffer.getBytes());
            fos.write("\n".getBytes());
            buffer = "\"" +

                    leContext.getString(R.string.numero) + "\"" + Separateur + "\"" +
                    leContext.getString(R.string.debut) + "\"" + Separateur + "\"" +
                    leContext.getString(R.string.fin) + "\"" + Separateur + "\"" +
                    leContext.getString(R.string.commentaire)
                    + "\"";


            fos.write(buffer.getBytes());
            fos.write("\n".getBytes());

            numero = 1;

            while (constantsCursor.moveToNext()) {
                if (constantsCursor.getString(1).length() > 0) {
                    debut = "";
                    debut = constantsCursor.getString(1); // 0 is the first column
                    fin = "";
                    fin = constantsCursor.getString(2); // 0 is the first column
                    commentaire = "";
                    commentaire = constantsCursor.getString(3);

                    if (debut.length() > 0) {
                        date = olddateFormat.parse(debut);
                        debut = (String) newdateFormat.format(date);
                    }

                    if (fin.length() > 0) {
                        date = olddateFormat.parse(fin);
                        fin = (String) newdateFormat.format(date);
                    }
                    if (commentaire == null) {
                        commentaire = " ";
                    }

                    buffer = "\"" + String.valueOf(numero) + "\"" + Separateur + "\"" +
                            debut + "\"" + Separateur + "\"" + fin + "\"" + Separateur + "\"" + commentaire + "\"";

                    fos.write(buffer.getBytes());
                    fos.write("\n".getBytes());
                    numero++;
                }


            }
            fos.close();
            constantsCursor.close();
            dbHelper.close();
            db.close();
        } catch (Exception e) {
            constantsCursor.close();
            dbHelper.close();
            db.close();
            // TODO Auto-generated catch block
            //Log.e("Erreur de generation", "Erreur=" + e.getMessage());
            erreur = 2;
            return erreur;
        }
        return 0;
    }


}
