package fr.s3i.pointeuse.activite;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.webkit.WebView;
import android.widget.TextView;

import fr.s3i.pointeuse.R;

/*
 * Classe Aide 
 * Afficher simplement un texte d'aide
 * 
 * idaide 	= 1 Aide générale 
 * 			= 2 Aide paramétres
 * 		    = 3 Info sur la license GPL
 * 		    = 4 Infos sur le syndicat S3I
 * 
 */
public class Aide extends Activity {
   // TextView messageAide;
    WebView webAide;
    int idAide = 1;

    public void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            String theme = preferences.getString("theme", "AppThemeNoir");

            if ("AppThemeNoir".equals(theme)) {
                setTheme(R.style.AppThemeNoir);
            }
        } catch (Exception All) {
            android.util.Log.w("Warning", All.getMessage());
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.aide);
        //messageAide = (TextView) findViewById(R.id.aide01);
        webAide = (WebView) findViewById(R.id.webaide);

        Intent data = this.getIntent();
        Bundle extras = data.getExtras();

        idAide = extras.getInt("NumAide");

        afficheAide();
       // messageAide.setSingleLine(false);

    }

    public void afficheAide() {
        switch (idAide) {
            case 1:
                //webAide.loadData(getString(R.string.txtaide1),"text/plain","utf-8");
                android.util.Log.d("Warning", "file:///android_asset/aide/"  + R.string.txtaide1);
                webAide.loadUrl("file:///android_asset/aide/"  + getString(R.string.txtaide1));
                break;
            case 2:
                webAide.loadUrl("file:///android_asset/aide/"  + getString(R.string.txtaide2));
                break;
            case 3:
                webAide.loadUrl("file:///android_asset/COPYING/gpl.txt");
                break;
            case 4:
                webAide.loadUrl("file:///android_asset/S3I/presentation.htm");
        }
    }
}

