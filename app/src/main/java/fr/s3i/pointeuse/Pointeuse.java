package fr.s3i.pointeuse;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import fr.s3i.pointeuse.activite.Aide;
import fr.s3i.pointeuse.framents.Calendrier;
import fr.s3i.pointeuse.utils.Utilitaire;

/*
 * 
 * Page générale
 *
 * 1.9 -
 * 1.8 - Bandeau params
 *      - Fragment
 *      - Arrondi a la second
 *      - Suppression d'un pointeuse sur un long click
 *
 *
 */


//http://www.hidroh.com/2015/02/25/support-multiple-themes-android-app-part-2/

@SuppressWarnings("deprecation")
public class Pointeuse extends ActionBarActivity
{

    FragmentTabHost mTabHost;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        try
        {
            String theme = preferences.getString("theme", "AppThemeNoir");

            if("AppThemeNoir".equals(theme) )
            {
                setTheme(R.style.AppThemeNoir);
            }
        }
        catch(Exception All)
        {
            //Toast.makeText(this, "Echec=" + All.getMessage() , Toast.LENGTH_SHORT).show();
        }

        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.activity_main);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        Resources res = getResources(); // Resource object to get Durables


         mTabHost.addTab(
                mTabHost.newTabSpec("tab1").setIndicator(getString(R.string.pointer), res.getDrawable(R.drawable.ipointage)),
                Pointer.class, null);

         mTabHost.addTab(
                mTabHost.newTabSpec("tab2").setIndicator(getString(R.string.calendrier), res.getDrawable(R.drawable.icalendrier)),
                Calendrier.class, null);

    }


    private static final int DELETE = Menu.FIRST;
    private static final int PARAMETRE = Menu.FIRST + 1;
    private static final int BACKUP = Menu.FIRST +2;
    private static final int HELP = Menu.FIRST + 3;
    private static final int PAUSE = Menu.FIRST + 4;
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.clear();
        menu.add(0, DELETE, 0, getString(R.string.suppression))
                .setIcon(android.R.drawable.ic_menu_delete);
        menu.add(0, PARAMETRE, 0, getString(R.string.param))
                .setIcon(android.R.drawable.ic_menu_preferences);
        menu.add(0, BACKUP, 0, getString(R.string.save))
                .setIcon(android.R.drawable.ic_dialog_email);
        menu.add(0, HELP, 0,getString(R.string.help))
                .setIcon(android.R.drawable.ic_menu_help);
        menu.add(0, PAUSE, 0, getString(R.string.reglages_pauses));

        //return true;
        super.onCreateOptionsMenu(menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle all of the possible menu actions.
        switch (item.getItemId())
        {
            case DELETE:
                suppression();
                break;
            case PARAMETRE:
                parametre();
                break;
            case HELP:
                aide();
                break;
            case PAUSE:
                pause();
                break;
            case BACKUP:
                //Utilitaire.verifyStoragePermissions(this);
                Utilitaire.sendbymail(this);
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    public void suppression()
    {
        Intent con = new Intent(this, Suppression.class);
        this.startActivity(con);
    }
    public void parametre()
    {
        Intent con = new Intent(this, Parametre.class);
        this.startActivity(con);
    }

    public void aide()
    {
        Intent con = new Intent(this, Aide.class);
        con.putExtra("NumAide", 1);
        this.startActivity(con);
    }
    public void pause()
    {
        Intent con = new Intent(this, ReglagePauses.class);
        this.startActivity(con);
    }
}