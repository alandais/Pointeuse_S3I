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
import fr.s3i.pointeuse.widget.PointageWidgetProvider;
import fr.s3i.pointeuse.R;
import fr.s3i.pointeuse.utils.Calcul;

public class Rafraichissement extends Service {

    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    Cursor dernierEnregistrement;
    private final IBinder mBinder = new LocalBinder();


    public class LocalBinder extends Binder {
        Rafraichissement getService() {
            // android.util.Log.w("LocalBinder","LocalBinder");
            return Rafraichissement.this;
        }
    }

    @Override
    public void onCreate() {
        //android.util.Log.w("onCreate","OK");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        //android.util.Log.w("onDestroy","OK");
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
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
    }

    public void MonRefresh() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = new Date();
        Date debut, fin = null;

        String date1 = dateFormat.format(date);
        //android.util.Log.w("Fin=", date1);
        try {
            fin = dateFormat.parse(date1);
        } catch (ParseException e1) {
            //android.util.Log.w("Exception e1", e1.getMessage());
        }

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widgetlayout);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        ;
        dernierEnregistrement = dbHelper.getLastEnregistrementPointage(db);
//			if ("".equals(dernierEnregistrement.getString(2))){
//				remoteViews.setTextViewText(R.id.monbouttonwidget, getString(R.string.depointer));
//			}else{
//				remoteViews.setTextViewText(R.id.monbouttonwidget, getString(R.string.pointer));
//			}
        Date maintenant = new Date();
        //android.util.Log.w("DATE", "Dateformat="+dateFormat.format(LaNouvelleDate));
        String conditions = "( strftime('%j',DATE_DEBUT) = strftime('%j','" + dateFormat.format(maintenant) + "') " +
                " and strftime('%Y',DATE_DEBUT) = strftime('%Y','" + dateFormat.format(maintenant) + "') ) " +

                " or ( strftime('%j',DATE_FIN) = strftime('%j','" + dateFormat.format(maintenant) + "') " +
                " and strftime('%Y',DATE_FIN) = strftime('%Y','" + dateFormat.format(maintenant) + "') ) ";
        Cursor curseurEnreg = dbHelper.getSomeDatePointage(db, conditions);
        // android.util.Log.w("Constants", "Refresh");

        if (curseurEnreg != null) {
/*	         	try
                 {
	         		if(! (dernierEnregistrement.getString(2).length()>0 ))
	         		{
	         			//Creation d'un nouvel enregistrement
	         			dbHelper.insereNouveauPointage(db, "", "");
	         			remoteViews.setTextViewText(R.id.monTextWidget, getString(R.string.debuter));
	         		}
	         		else
	         		{
	         			//android.util.Log.w("debut=", (String)dernierEnregistrement.getString(1));
	         			debut  = dateFormat.parse((String)dernierEnregistrement.getString(1));
*/

//						GregorianCalendar c_debut = new GregorianCalendar();
//						c_debut.setTime(debut);
//						GregorianCalendar c_fin = new GregorianCalendar();
//						c_fin.setTime(fin);
            ArrayList listeDebut = new ArrayList<String>();
            ArrayList listeFin = new ArrayList<String>();

            while (curseurEnreg.moveToNext()) {
                listeDebut.add(curseurEnreg.getString(1)); // 0 is the first column
                listeFin.add(curseurEnreg.getString(2)); // 0 is the first column
                //Toast.makeText(this, "Enregistrement parcouru =" + curseurEnreg.getString(1) + " / " +curseurEnreg.getString(2) , Toast.LENGTH_SHORT).show();
            }
            Calcul calcul = new Calcul(this);
            //Calcul.Spointage s = calcul.CalculTemps(c_debut,c_fin,0);
            Calcul.Spointage s = calcul.somme(listeDebut, listeFin, 0);
            long temps = s.temps_pointage / 60;
            Double tempsReel = new Double(temps);
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2); //arrondi à 2 chiffres apres la virgules
            df.setMinimumFractionDigits(2);
            df.setDecimalSeparatorAlwaysShown(true);
            //remoteViews.setTextViewText(R.id.monTextWidget, getString(R.string.tempstravail)+temps/60+"H"+temps%60+"Min");
            String format = "0";

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            try {
                format = preferences.getString("formataffichage", "0");
            } catch (Exception All) {
                //Toast.makeText(this, "Echec=" + All.getMessage() , Toast.LENGTH_SHORT).show();
            }

            if (temps < 60) {
                if (format.equals("2")) {
                    remoteViews.setTextViewText(R.id.monTextWidget, getString(R.string.tempstravail) + (df.format(tempsReel / 60)) + " H");
                } else {
                    remoteViews.setTextViewText(R.id.monTextWidget,
                            getString(R.string.tempstravail) + temps + "Min");

                }

            } else if (temps < 1440) {
                if (format.equals("2")) {
                    remoteViews.setTextViewText(R.id.monTextWidget, getString(R.string.tempstravail) + (df.format(tempsReel / 60)) + " H");
                } else {
                    remoteViews.setTextViewText(R.id.monTextWidget, getString(R.string.tempstravail) + temps / 60 + "H" + temps % 60 + "Min");
                }
            } else {
                System.out.println("format=" + format);

                if (format.equals("0")) {
                    remoteViews.setTextViewText(R.id.monTextWidget, getString(R.string.tempstravail) + temps / 60 + "H" + temps % 60 + "Min");
                } else if (format.equals("1")) {
                    int min = (int) (temps % 1440);
                    int nbjour = (int) (temps / 1440);
                    remoteViews.setTextViewText(R.id.monTextWidget,
                            getString(R.string.tempstravail) + (int) (nbjour) + getString(R.string.jourarrondi) + " " +
                                    (int) (min / 60) + "h " +
                                    (int) (min % 60) + "min");
                } else {
                    remoteViews.setTextViewText(R.id.monTextWidget, getString(R.string.tempstravail) + (df.format(tempsReel / 60)) + " H");
                }
            }


//	         		}
//	         	}
//	         	catch(Exception e )
//	         	{
//	         	//	Toast.makeText(context, "exception=" + e.getMessage() , Toast.LENGTH_SHORT).show();
//	         		dbHelper.insereNouveauPointage(db, "", "");
//	            	remoteViews.setTextViewText(R.id.monTextWidget,getString(R.string.debuter));
//	               // android.util.Log.w("Exception Refresh", e.getMessage());
//	         	}
        }
        //   Toast.makeText(this, "Update!", Toast.LENGTH_SHORT).show();

        dernierEnregistrement.close();
        dernierEnregistrement.deactivate();
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
        // TODO Auto-generated method stub
        //Toast.makeText(this, "MyAlarmService.onBind()", Toast.LENGTH_LONG).show();
        return mBinder;
    }


}
