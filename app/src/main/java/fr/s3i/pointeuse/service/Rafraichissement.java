package fr.s3i.pointeuse.service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

import fr.s3i.pointeuse.adaptaters.ListeDesPointagesAdapter;
import fr.s3i.pointeuse.persistance.DatabaseHelper;
import fr.s3i.pointeuse.utils.Utilitaire;
import fr.s3i.pointeuse.widget.PointageWidgetProvider;
import fr.s3i.pointeuse.R;
import fr.s3i.pointeuse.utils.Calcul;

public class Rafraichissement extends Service {

    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    private final IBinder mBinder = new LocalBinder();


    public class LocalBinder extends Binder {
        Rafraichissement getService() {
            android.util.Log.d("LocalBinder","LocalBinder");
            return Rafraichissement.this;
        }
    }

    @Override
    public void onCreate() {
        android.util.Log.d("onCreate","OK");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        //android.util.Log.w("onDestroy","OK");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        MonRefresh();
        //1.8
        try {
            AlarmManager alarmManager = (AlarmManager) this.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getService(this.getApplicationContext(), 0, intent, 0);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + PointageWidgetProvider.PERIODE * 1000, pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + PointageWidgetProvider.PERIODE * 1000, pendingIntent);
            }
        } catch (Exception e) {

        }
        return START_STICKY;
    }

    public void MonRefresh() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String affichage = getString(R.string.debuter);

        Date date = new Date();
        Date debut, fin = null;

        String date1 = dateFormat.format(date);
        android.util.Log.d("Fin=", date1);
        try {
            fin = dateFormat.parse(date1);
        } catch (ParseException e1) {
            android.util.Log.w("Exception e1", e1.getMessage());
        }

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widgetlayout);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        Date maintenant = new Date();
        android.util.Log.d("DATE", "Dateformat="+dateFormat.format(maintenant));
        String conditions = "( strftime('%j',DATE_DEBUT) = strftime('%j','" + dateFormat.format(maintenant) + "') " +
                " and strftime('%Y',DATE_DEBUT) = strftime('%Y','" + dateFormat.format(maintenant) + "') ) " +

                " or ( strftime('%j',DATE_FIN) = strftime('%j','" + dateFormat.format(maintenant) + "') " +
                " and strftime('%Y',DATE_FIN) = strftime('%Y','" + dateFormat.format(maintenant) + "') ) ";
        Cursor curseurEnreg = dbHelper.getSomeDatePointage(db, conditions);

        if (curseurEnreg != null) {
            ArrayList listeDebut = new ArrayList<String>();
            ArrayList listeFin = new ArrayList<String>();
            affichage = getString(R.string.travailfait);
            while (curseurEnreg.moveToNext()) {
                listeDebut.add(curseurEnreg.getString(1)); // 0 is the first column
                listeFin.add(curseurEnreg.getString(2)); // 0 is the first column
                if("".equals(curseurEnreg.getString(2))){
                    affichage = getString(R.string.travailencours);
                }
                android.util.Log.d("Enregistrement parcouru", curseurEnreg.getString(1) + " / " + curseurEnreg.getString(2));
            }
            Calcul calcul = new Calcul(this);
            Calcul.Spointage s = calcul.somme(listeDebut, listeFin, 0);
            long temps = s.temps_pointage / 60;
            affichage = affichage.concat(Utilitaire.formatAffichage(this, temps));
        }
        conditions = "( strftime('%W',DATE_DEBUT) = strftime('%W','" + dateFormat.format(maintenant) + "') " +
                " and strftime('%Y',DATE_DEBUT) = strftime('%Y','" + dateFormat.format(maintenant) + "') ) " +

                " or ( strftime('%W',DATE_FIN) = strftime('%W','" + dateFormat.format(maintenant) + "') " +
                " and strftime('%Y',DATE_FIN) = strftime('%Y','" + dateFormat.format(maintenant) + "') ) ";

        Cursor curseurEnregSemaine = dbHelper.getSomeDatePointage(db, conditions);
        if (curseurEnregSemaine != null) {
            ArrayList listeDebut = new ArrayList<String>();
            ArrayList listeFin = new ArrayList<String>();
            affichage = affichage.concat("\n");
            affichage = affichage.concat(getString(R.string.travailsemaine));
            while (curseurEnregSemaine.moveToNext()) {
                listeDebut.add(curseurEnregSemaine.getString(1)); // 0 is the first column
                listeFin.add(curseurEnregSemaine.getString(2)); // 0 is the first column
                android.util.Log.d("Enregistrement parcouru", curseurEnregSemaine.getString(1) + " / " + curseurEnregSemaine.getString(2));
            }
            Calcul calcul = new Calcul(this);
            Calcul.Spointage s = calcul.somme(listeDebut, listeFin, 0);
            long temps = s.temps_pointage / 60;
            affichage = affichage.concat(Utilitaire.formatAffichage(this, temps));
        }

        remoteViews.setTextViewText(R.id.monTextWidget, affichage);
        db.close();
        dbHelper.close();

        // Push update for this widget to the home screen


        ComponentName thisWidget = new ComponentName(this, PointageWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(thisWidget, remoteViews);

        Intent clickintent = new Intent(this, PointageWidgetProvider.class);
        clickintent.setAction(PointageWidgetProvider.ACTION_WIDGET_RECEIVER);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, clickintent, 0);
        remoteViews.setOnClickPendingIntent(R.id.monbouttonwidget, pendingIntent);
        manager.updateAppWidget(thisWidget, remoteViews);
    }


    @Override
    public IBinder onBind(Intent intent) {
        android.util.Log.d("Service", "MyAlarmService.onBind()");
        return mBinder;
    }


}
