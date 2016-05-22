package fr.s3i.pointeuse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import fr.s3i.pointeuse.persistance.DatabaseHelper;
import fr.s3i.pointeuse.utils.Calcul;
import fr.s3i.pointeuse.widget.PointageWidgetProvider;


public class Pointer extends Fragment
{
	int heure;
	int minute;
	int sec;
	int annee,mois,jour;

	public Button actionPointer,btnAddPointage;
	public TimePicker nouvelleHeure;
	public DatePicker nouvelleDate;
	public TextView etatEnCours;
	
	DatabaseHelper dbHelper;
	SQLiteDatabase db;
	private String TAG = "Pointer";
	private static final int DELETE = Menu.FIRST;
    private static final int PARAMETRE = Menu.FIRST + 1;
    private static final int BACKUP = Menu.FIRST +2;
    private static final int HELP = Menu.FIRST + 3;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.pagepointage, container, false);

		etatEnCours = (TextView)v.findViewById(R.id.encours);

		actionPointer  = (Button)v.findViewById(R.id.btnPointer);
		actionPointer.setSingleLine(false);
		actionPointer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Pointe();
			}

		});
		btnAddPointage= (Button)v.findViewById(R.id.btnAddPointage);
		btnAddPointage.setSingleLine(false);
		btnAddPointage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				creerFenetreDateHeure();
				//AjoutePointage();
			}

		});

		dbHelper = new DatabaseHelper(this.getContext());
		db = dbHelper.getWritableDatabase();

		refresh_etat();

		return v;
	}
    
    public void refresh_etat()
    {
    	Cursor dernierEnregistrement;
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    	
    	Date date = new Date();
    	Date debut, fin = null ;
    	
		 String date1  = dateFormat.format(date) ;
		 //android.util.Log.w("Fin=", date1);
		 try 
		 {
			 fin  = dateFormat.parse(date1);
		 } 
		 catch (ParseException e1) 
		 {
			//android.util.Log.w("Exception e1", e1.getMessage());
		 }
		 
         dernierEnregistrement = dbHelper.getLastEnregistrementPointage(db);
        // android.util.Log.w("Constants", "Refresh");

         if (dernierEnregistrement!=null)
         {
         	try
         	{
         		if(dernierEnregistrement.getString(2).length()>0 )
         		{
         			//Creation d'un nouvel enregistrement
         			dbHelper.insereNouveauPointage(db, "", "");
         			etatEnCours.setText(getString(R.string.aucunpointage));
         		}
         		else
         		{
         			//android.util.Log.w("debut=", (String)dernierEnregistrement.getString(1));
         			debut  = dateFormat.parse((String)dernierEnregistrement.getString(1));

					GregorianCalendar c_debut = new GregorianCalendar();
					c_debut.setTime(debut);
					GregorianCalendar c_fin = new GregorianCalendar();
					c_fin.setTime(fin);

					Calcul calcul = new Calcul(this.getContext());
					Calcul.Spointage s = calcul.CalculTemps(c_debut,c_fin,0);
					long temps = s.temps_pointage / 60;

         			//etatEnCours.setText(getString(R.string.tempstravail1) + " "+temps/60+"H"+temps%60+"Min");
	     			
         			String format = "0";
    			    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
    			      try
    			      {
    			    	  format = preferences.getString("formataffichage", "0");
    			      }
    			      catch(Exception All)
    			      {
    			    	  //Toast.makeText(this, "Echec=" + All.getMessage() , Toast.LENGTH_SHORT).show(); 
    			      }
         			if (temps<60)
	     		      {
         				etatEnCours.setText(getString(R.string.tempstravail1) + temps + "min");
	     			  }
	     			  else if(temps < 1440) 
	     			  {
	     				 etatEnCours.setText(getString(R.string.tempstravail1) + (int)(temps/60) + "h " + (int)(temps%60) + "min" );
	     			  }
	     			  else
	     			  {
	     				  if(format.equals("0") )
	     				  {
	     					 etatEnCours.setText(getString(R.string.tempstravail1) + (int)(temps/60) + "h " + (int)(temps%60) + "min" );
	     				  }
	     				  else
	     				  {
		     				  int min = (int) (temps % 1440);
		     				  int nbjour =  (int) (temps/ 1440);
		     				  etatEnCours.setText(getString(R.string.tempstravail1) +
		     						  (int)(nbjour) + getString(R.string.jourarrondi) + " " +
		     						  (int)(min/60) + "h " +
	     						 	(int)(min%60) + "min" );
	     				  }
	     			  }
         			 
         		}
         	}
         	catch(Exception e )
         	{
         	//	Toast.makeText(context, "exception=" + e.getMessage() , Toast.LENGTH_SHORT).show();
         		dbHelper.insereNouveauPointage(db, "", "");
         		etatEnCours.setText(getString(R.string.aucunpointage));
                //android.util.Log.w("Exception Refresh", e.getMessage());
         	}
         }
         //   Toast.makeText(this, "Update!", Toast.LENGTH_SHORT).show();
         
         dernierEnregistrement.close();
         dernierEnregistrement.deactivate();
         if (isMyServiceRunning())
         {
	         AlarmManager alarmManager =  (AlarmManager)this.getContext().getSystemService(Context.ALARM_SERVICE);
	         Intent intent = new Intent(this.getContext(), PointageWidgetProvider.class);
	         intent.setAction(PointageWidgetProvider.ACTION_START_REFRESH_WIDGET);
	         PendingIntent pi = PendingIntent.getBroadcast(this.getContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	         alarmManager.cancel(pi);
	         alarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, pi);
         }
         else
         {
        	// android.util.Log.w(TAG, "service stop");
         }
    }

    private boolean isMyServiceRunning() 
    {
        ActivityManager manager = (ActivityManager) this.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.picca.pointeuse.Rafraichissement".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void creerFenetreDateHeure()
    {
    	AlertDialog.Builder alert = new AlertDialog.Builder(this.getContext());
		alert.setTitle(getString(R.string.ajouter_pointage));
		String titre;
		titre = "Ajouter un pointeuse";
		
		alert.setMessage(titre);


		LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View dialoglayout = inflater.inflate(R.layout.body, (ViewGroup)getView().findViewById(R.id.framealertedateheure));

		nouvelleHeure = (TimePicker)dialoglayout. findViewById(R.id.heure);
		nouvelleHeure.setIs24HourView(true);
		nouvelleHeure.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));

		nouvelleHeure.setIs24HourView(true);

		nouvelleDate = (DatePicker)dialoglayout. findViewById(R.id.date);
		alert.setView(dialoglayout);
		 	    
	    alert.setNegativeButton(getString(R.string.cancel), null);
	    alert.setPositiveButton(getString(R.string.oui1), new DialogInterface.OnClickListener() 
   		{
	   	      public void onClick(DialogInterface dialog, int which) 
	   	      {
	   	    	insereNouveauPointage();
	   	      } 
	   	    }); 
	    alert.show();
	    
    }
    
    public void insereNouveauPointage()
    {
    	//Recupere la date et l'heure
    	Cursor constantsCursor=null;
    	int jour,mois,annee,heure,minute;
    	String conditions;
    	String oldDebut="",oldFin="";
    	boolean first;
    	jour = nouvelleDate.getDayOfMonth();
    	mois = nouvelleDate.getMonth();//Commence a 0
    	annee = nouvelleDate.getYear();
    	heure = nouvelleHeure.getCurrentHour();
    	minute = nouvelleHeure.getCurrentMinute();
    	
    	// converting the datestring from the picker to a long:
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, jour);
        c.set(Calendar.MONTH, mois);
        c.set(Calendar.YEAR, annee);
        c.set(Calendar.HOUR_OF_DAY, heure);
        c.set(Calendar.MINUTE, minute);
        
        Date LaNouvelleDate = c.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //android.util.Log.w("DATE", "Dateformat="+dateFormat.format(LaNouvelleDate));
        
        conditions= "strftime('%s',DATE_DEBUT) >= strftime('%s','"+dateFormat.format(LaNouvelleDate)+"') " + 
		 " or strftime('%s',DATE_FIN) >= strftime('%s','"+dateFormat.format(LaNouvelleDate)+"') " ;

        constantsCursor=dbHelper.getSomeDatePointage(db, conditions) ;
        if(constantsCursor==null)
        {
        	//android.util.Log.w("constantsCursor", "==null");
        	refresh_etat();
        	return ;
        }
    	    	
    	if(constantsCursor.getCount()==0)
    	{
    		constantsCursor.close();
    		 constantsCursor=dbHelper.getLastEnregistrementPointage(db);
    		 if (constantsCursor.getCount() > 0)
    		 {
    			 if(constantsCursor.getString(1).length()==0 )
    	    	{
    				 //android.util.Log.w("constantsCursor", "==DATE_DEBUT");
    				 dbHelper.updateEnregistrementPointage(db, constantsCursor.getLong(0), dbHelper.DATE_DEBUT, dateFormat.format(LaNouvelleDate));
    	    	}
    			 else
    			 {
    				 //android.util.Log.w("constantsCursor", "==DATE_FIN");
    				 dbHelper.updateEnregistrementPointage(db, constantsCursor.getLong(0), dbHelper.DATE_FIN, dateFormat.format(LaNouvelleDate));
    				 dbHelper.insereNouveauPointage(db, "", "");
    			 }
    			 constantsCursor.close();
    			 String message = getString(R.string.insertiontermine);
    			 Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
        		 return;
    		 }
    		 dbHelper.insereNouveauPointage(db, "", "");
    		 constantsCursor=dbHelper.getLastEnregistrementPointage(db);
    		 if(constantsCursor==null)return ;
    	       
    		 dbHelper.updateEnregistrementPointage(db, constantsCursor.getLong(0), dbHelper.DATE_DEBUT, dateFormat.format(LaNouvelleDate));
    		 constantsCursor.close();
    		 
    		 String message = getString(R.string.insertiontermine);
			 Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
			 refresh_etat();
    		 return;
    	}
    	
    	first = true;
    	 while (constantsCursor.moveToNext() ) 
	     {
    		
	    	if(first)
	    	{
	    		oldDebut = constantsCursor.getString(1);
		    	oldFin = constantsCursor.getString(2);
		    	
	    		Date date;
				try 
				{
					date = dateFormat.parse(oldDebut);
				} 
				catch (ParseException e) 
				{
					constantsCursor.close();
					return;
				}
				
	    		if(date.getTime()<LaNouvelleDate.getTime())//Insertion de fin de pointeuse
	    		{
	    			dbHelper.updateEnregistrementPointage(db, constantsCursor.getLong(0), dbHelper.DATE_FIN, dateFormat.format(LaNouvelleDate));
	    				    		}
	    		else //Insertion d'un debut
	    		{
	    			dbHelper.updateEnregistrementPointage(db, constantsCursor.getLong(0), dbHelper.DATE_FIN, oldDebut);
	    			dbHelper.updateEnregistrementPointage(db, constantsCursor.getLong(0), dbHelper.DATE_DEBUT, dateFormat.format(LaNouvelleDate));
	    		}
	    		first = false;
	    	}
	    	else
	    	{
	    		dbHelper.updateEnregistrementPointage(db, constantsCursor.getLong(0), dbHelper.DATE_FIN, constantsCursor.getString(1));
	    		dbHelper.updateEnregistrementPointage(db, constantsCursor.getLong(0), dbHelper.DATE_DEBUT, oldFin);
	    		
	    		oldDebut = constantsCursor.getString(1);
		    	oldFin = constantsCursor.getString(2);		
	    	}
    		
    		 
	     }
    	 if(oldDebut.length()>0)
    	 {
    		 dbHelper.insereNouveauPointage(db, oldFin, "");
    	 }
    	 String message = getString(R.string.insertiontermine);
		 Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    	 constantsCursor.close();
    	 refresh_etat();
    }

    public void Pointe()
    {
    	//1  On recupere le dernier pointeuse
    	Cursor dernierEnregistrement = dbHelper.getLastEnregistrementPointage(db);
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    	Date date = new Date();
    	String message;
	    
    	/*Toast.makeText(this, "dernierEnregistrement =  " + dernierEnregistrement.getLong(0) +
    				" - Debut = " + dernierEnregistrement.getString(2) + " Fin= " + 
    				dernierEnregistrement.getString(3), Toast.LENGTH_SHORT).show();*/
    	
    	try
    	{
    		if(dernierEnregistrement.getString(1).length()==0 )
    		{
    			dbHelper.updateEnregistrementPointage(db, dernierEnregistrement.getLong(0), dbHelper.DATE_DEBUT, dateFormat.format(date));
    			dateFormat = new SimpleDateFormat("HH:mm"); 
    			message = getString(R.string.debutpointage) + " " + dateFormat.format(date);
    			Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    		}
    		else
    		{
    			dbHelper.updateEnregistrementPointage(db, dernierEnregistrement.getLong(0), dbHelper.DATE_FIN, dateFormat.format(date));
    			dbHelper.insereNouveauPointage(db, "", "");
    			dateFormat = new SimpleDateFormat("HH:mm"); 
    			message = getString(R.string.finpointage) + " " + dateFormat.format(date);
    			Toast.makeText(this.getContext()	, message, Toast.LENGTH_SHORT).show();
    		}
    	}
    	catch(CursorIndexOutOfBoundsException e)
    	{
    		dbHelper.insereNouveauPointage(db, "", "");
    	}
    	 refresh_etat();
    }
  
    @Override
    public void onDestroy() 
    {
		if(dbHelper != null)
        	dbHelper.close();
        super.onDestroy();
    }

    @Override
    public void onResume() 
    {
        super.onResume();
        refresh_etat();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
