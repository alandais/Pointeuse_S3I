package fr.s3i.pointeuse.activite;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import fr.s3i.pointeuse.R;

/*
 * Classe Aide 
 * Afficher simplement un texte d'aide
 * 
 * idaide 	= 1 Aide générale 
 * 			= 2 Aide paramétres
 * 
 */
public class Aide extends Activity 
{
	    TextView messageAide;
	    int idAide = 1;
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

	        setContentView(R.layout.aide);
	        messageAide = (TextView) findViewById(R.id.aide01 );
	      
	        Intent data = this.getIntent();
	        Bundle extras = data.getExtras();
	        
	        idAide =  extras.getInt("NumAide");	
	        
	        afficheAide();
	        messageAide.setSingleLine(false);
	        
	    }
	    
	    public void afficheAide()
	    {
	    	if (idAide == 1)
	    		messageAide.setText(getString(R.string.txtaide1));
	    	
	    	else if  (idAide == 2)
	    		messageAide.setText(getString(R.string.txtaide2));
	    }
	    
	    
	  
}

