package fr.s3i.pointeuse.activite;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import fr.s3i.pointeuse.R;
import fr.s3i.pointeuse.adaptaters.ListeDesPausesAdapter;
import fr.s3i.pointeuse.persistance.DatabaseHelper;

/**
 * Created by julien on 07/02/16.
 */
public class ReglagePauses extends ActionBarActivity {

    SQLiteDatabase db;
    private Cursor constantsCursor=null;
    private DatabaseHelper dbHelper=null;
    ListView maListe;
    ListeDesPausesAdapter adapter;
    private InterstitialAd interstitial;

    public void onCreate(Bundle savedInstanceState) {

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
        setContentView(R.layout.pauses);

        dbHelper = new DatabaseHelper(this) ;
        db = dbHelper.getWritableDatabase();

        maListe = (ListView)findViewById(R.id.MaListeViewPause);
        TextView textView = new TextView(this);
        textView.setText(getString(R.string.titre_pause));

        maListe.addHeaderView(textView);

        refresh();

        affiche_pub();
    }

    String debut_pause = "";
    String fin_pause = "";
    private TimePickerDialog.OnTimeSetListener valide_debut =  new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            debut_pause = String.format("%02d:%02d",hourOfDay,minute);
            TimePickerDialog temp = new TimePickerDialog(view.getContext() , valide_fin, 0, 0, true);
            temp.show();
        }
    };
    private TimePickerDialog.OnTimeSetListener valide_fin =  new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            fin_pause = String.format("%02d:%02d",hourOfDay,minute);
            dbHelper.insereNouvellePause(db, debut_pause, fin_pause);
            refresh();
        }
    };

    public void ajouterPause()
    {
        TimePickerDialog temp = new TimePickerDialog(this , valide_debut, 0, 0, true);
        temp.show();
    }
    public void refresh() {

        constantsCursor = dbHelper.getAllPause(db);
        if (constantsCursor == null)return;
        //android.util.Log.e("Return", "constantsCursor="+constantsCursor.getCount());

        adapter = new ListeDesPausesAdapter(this,this);
        adapter.setList(constantsCursor);
        maListe.setAdapter(adapter);

        constantsCursor.close();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        constantsCursor.close();
        dbHelper.close();
    }

    private static final int DELETE = Menu.FIRST;
    private static final int AJOUT = Menu.FIRST + 1;

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0, DELETE, 0, getString(R.string.confirmation_suppression_pause))
                .setIcon(android.R.drawable.ic_menu_delete);

        menu.add(0, AJOUT, 0, getString(R.string.ajouter_pause));

        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle all of the possible menu actions.
        switch (item.getItemId())
        {
            case DELETE:
                suppression();
                break;
            case AJOUT:
                ajouterPause();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void suppression() {
        long row ;

        //android.util.Log.e("Constants", "Taille="+adapter.ListeCheckBox.size());

        for(int i=0; i < adapter.listeEtat.size();i++)
        {

            if(adapter.listeEtat.get(i))
            {
                try
                {
                    row = Long.parseLong(adapter.listeId.get(i));

                    if (row >= 0)
                    {
                        dbHelper.deleteEnregistrementPause(db, row);
                    }
                }
                catch(Exception e)
                {
                   // android.util.Log.w("Constants", "Exception capture ="+e.getMessage());
                }
            }

        }
        refresh();
    }

    private void affiche_pub() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String masquer_pub = "0";
        try
        {
            masquer_pub = preferences.getString("supprimer_pub", "0");
        }
        catch(Exception All)
        {
            //Toast.makeText(this, "Echec=" + All.getMessage() , Toast.LENGTH_SHORT).show();
        }

        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId("XXXX");

        // Create ad request.
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("XXX")
                .addTestDevice("XX")
                .build();

        if("0".equals(masquer_pub)){
            // Begin loading your interstitial.
            interstitial.loadAd(adRequest);

            interstitial.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    displayInterstitial();
                }
            });
        }
    }

    public void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }
}
