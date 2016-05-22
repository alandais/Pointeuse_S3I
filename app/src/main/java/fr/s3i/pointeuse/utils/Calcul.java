package fr.s3i.pointeuse.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import fr.s3i.pointeuse.persistance.DatabaseHelper;

public class Calcul
{
    public class Spointage
    {
        public long temps_pause;
        public long temps_pointage;
        public  Spointage(long temps_pointage,long temps_pause)
        {
            this.temps_pause = temps_pause;
            this.temps_pointage = temps_pointage;
        }
    }

    static List pause_debut_brute  = new LinkedList();
    static List pause_fin_brute    = new LinkedList();
    Context leContext;


    public Calcul(Context leContext)
    {
        this.leContext = leContext;
        init_pauses();
    }

    public Spointage somme(List listeDebut,List listeFin,int arrondi)
    {
        Date debut,fin;

        SimpleDateFormat olddateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.leContext);
        String option_a_la_seconde = "0";

        Spointage retour = new Spointage(0,0);

        try
        {
            option_a_la_seconde = preferences.getString("pointagealaseconde", "0");
        }
        catch(Exception All)
        {
            //Toast.makeText(this, "Echec=" + All.getMessage() , Toast.LENGTH_SHORT).show();
        }
        //android.util.Log.w("refresh", "option_a_la_seconde="+option_a_la_seconde);

        if("0".equals(option_a_la_seconde))
        {
            olddateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        }

        for(int i=0 ; i<listeDebut.size(); i++)
        {
            try
            {
                debut  = olddateFormat.parse((String)listeDebut.get(i));
                fin  = olddateFormat.parse((String)listeFin.get(i));

                GregorianCalendar c_debut = new GregorianCalendar();
                c_debut.setTime(debut);
                GregorianCalendar c_fin = new GregorianCalendar();
                c_fin.setTime(fin);

                Spointage s = CalculTemps(c_debut,c_fin,arrondi);
                retour.temps_pointage += s.temps_pointage;
                retour.temps_pause += s.temps_pause;
                //	Toast.makeText(LeContext, debut.toString() + "+" +fin.toString()+"="+cumul, Toast.LENGTH_LONG).show();
            }
            catch (ParseException e)
            {
                break;
                //Toast.makeText(LeContext, "Erreur=" +e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        return retour;
    }

    public void init_pauses() {

        pause_debut_brute.clear();
        pause_fin_brute.clear();

        SQLiteDatabase db;
        Cursor constantsCursor=null;
        DatabaseHelper dbHelper=null;

        dbHelper = new DatabaseHelper(leContext) ;
        db = dbHelper.getWritableDatabase();

        constantsCursor = dbHelper.getAllPause(db);
        if (constantsCursor == null)return;

        try {
            while (constantsCursor.moveToNext()) {

                if (constantsCursor.getString(1).length() == 5 && constantsCursor.getString(2).length() == 5) {

                    pause_debut_brute.add(Integer.parseInt(constantsCursor.getString(1).substring(3,5)) +
                                    Integer.parseInt(constantsCursor.getString(1).substring(0,2))*60
                    );

                    pause_fin_brute.add(Integer.parseInt(constantsCursor.getString(2).substring(3, 5)) +
                                    Integer.parseInt(constantsCursor.getString(2).substring(0, 2)) * 60
                    );


                    //android.util.Log.e("debut", "" + pause_debut_brute.get(pause_debut_brute.size() - 1));
                    //android.util.Log.e("fin",""+ pause_fin_brute.get(pause_fin_brute.size()-1));
                }
            }
        }
        catch (Exception e)
        {
            pause_debut_brute.clear();
            pause_fin_brute.clear();
        }
    }

    public Spointage CalculTemps(GregorianCalendar debut,GregorianCalendar fin,int arrondi)	{

        long diff;

        if( pause_debut_brute.size() > 0 || pause_fin_brute.size() >0 || pause_fin_brute.size() != pause_debut_brute.size() ) {
            return this.calculTempAvecPauses(debut, fin, arrondi);
        }
        else
        {
            diff = fin.getTimeInMillis() - debut.getTimeInMillis();
        }

        //diff = diff / (60 * 1000);//Conversion en minutes
        diff = diff / (1000);//Conversion en secondes

        //Utilisation de l'arrondi
        if (arrondi > 0)
        {
            long arrondissement = diff % (arrondi*60);
            diff = diff - arrondissement;

            //android.util.Log.w("Arrondissement", "Val=" + arrondi + " diff="+ diff + " arrondissement = "+arrondissement);
        }

        //android.util.Log.w("CalculTemps", "Val=" +diff);
        Spointage s = new Spointage(diff,0);
        return s;
    }


    private  Spointage  calculTempAvecPauses(GregorianCalendar debut,GregorianCalendar fin,int arrondi)
    {

        List pause_debut =new LinkedList();
        List pause_fin =new LinkedList();

        /*

        //Admettons que ce soit dans l'ordre
        pause_debut_brute.add(10*60);
        pause_fin_brute.add(10*60+15);

        pause_debut_brute.add(12*60);
        pause_fin_brute.add(14*60);

        pause_debut_brute.add(16*60);
        pause_fin_brute.add(16*60+15);

        pause_debut_brute.add(23*60);
        pause_fin_brute.add(60);*/

        GregorianCalendar butees_debut = new GregorianCalendar(debut.get(Calendar.YEAR),
                debut.get(Calendar.MONTH),debut.get(Calendar.DAY_OF_MONTH),0,0,0);

        while(butees_debut.before(fin))
        {
            for(int i = 0 ;i < pause_debut_brute.size() ; i++ )
            {

                GregorianCalendar le_debut = new GregorianCalendar( butees_debut.get(Calendar.YEAR),
                        butees_debut.get(Calendar.MONTH),butees_debut.get(Calendar.DAY_OF_MONTH),
                        (Integer)pause_debut_brute.get(i) /60 , (Integer)pause_debut_brute.get(i) %60,0 );

                GregorianCalendar la_fin = new GregorianCalendar( butees_debut.get(Calendar.YEAR),
                        butees_debut.get(Calendar.MONTH),butees_debut.get(Calendar.DAY_OF_MONTH),
                        (Integer)pause_fin_brute.get(i) /60 , (Integer)pause_fin_brute.get(i) %60 ,0);

                //Au cas ou il y ai un chevochement a minuit par exemple
                if(le_debut.after(la_fin)){

                    la_fin.add(Calendar.HOUR, 24);
                }


                pause_debut.add(le_debut);
                pause_fin.add(la_fin);

            }
            butees_debut.add(Calendar.HOUR_OF_DAY, 24);

        }


        GregorianCalendar derniere_valeur = debut;

        long total = fin.getTimeInMillis() - debut.getTimeInMillis();
        total = total / 1000;

        List new_list_debut = new LinkedList();
        List new_list_fin = new LinkedList();
        new_list_debut.add(debut);
        int pos = new_list_debut.size();
        for(int x = 0 ; x < pause_debut.size() ; x++)
        {

            //Si la pause est apres le debut
            if(((GregorianCalendar)pause_debut.get(x)).getTimeInMillis()>= fin.getTimeInMillis())
            {
                //System.out.println("Je saute 1");
            }
            else if(((GregorianCalendar)pause_debut.get(x)).getTimeInMillis()>= debut.getTimeInMillis())
            {
                //System.out.println("Cas 1");
                new_list_debut.add(pause_fin.get(x));//Inversion
                new_list_fin.add(pause_debut.get(x));
            }
            //Si le debut est au milieu (Je change le debut par la fin de la pause
            else if(((GregorianCalendar)pause_fin.get(x)).getTimeInMillis()>= debut.getTimeInMillis())
            {
                // System.out.println("Cas 2");
                new_list_debut.remove(0);
                new_list_debut.add(pause_fin.get(x));
            }
            else
            {
                //System.out.println("Je saute 2 ");
                //Sinon la pause est avant donc on s'en fou
            }

        }
        new_list_fin.add(fin);



        long valeur = 0;
        for(int x = 0 ; x < new_list_debut.size() ; x++)
        {
            GregorianCalendar debut_temp = (GregorianCalendar)new_list_debut.get(x);
            //System.out.println("debut_temp = " +debut_temp);
            //System.out.println("derniere_valeur = " +derniere_valeur);
            if(debut_temp.getTimeInMillis() >= derniere_valeur.getTimeInMillis())
            {

                long temp = ((GregorianCalendar)new_list_fin.get(x)).getTimeInMillis() - ((GregorianCalendar)new_list_debut.get(x)).getTimeInMillis();
                if(temp>0)
                    valeur += temp / 1000;
                derniere_valeur = (GregorianCalendar)new_list_fin.get(x);
            }
        }

        if (arrondi > 0)
        {
            long arrondissement = valeur % (arrondi*60);
            valeur = valeur - arrondissement;

            //android.util.Log.w("Arrondissement", "Val=" + arrondi + " diff="+ diff + " arrondissement = "+arrondissement);
        }
        Spointage s = new Spointage(valeur,total-valeur);

        //android.util.Log.e("CalculTemps", "Temps calcule = " +String.format("%02d:%02d",valeur / 60,valeur % 60));
        //android.util.Log.e("CalculTemps", "Temps de pause  = " +String.format("%02d:%02d",(total-valeur) / 60,(total-valeur) % 60));
        //System.out.println("Temps calcule = " +String.format("%02d:%02d",valeur / 60,valeur % 60));
        //System.out.println("temps de pause  = " +String.format("%02d:%02d",(total-valeur) / 60,(total-valeur) % 60));
        return s;
    }

}

