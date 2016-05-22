package fr.s3i.pointeuse.persistance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseHelper extends SQLiteOpenHelper 
{
    private static final String DATABASE_NAME="POINTAGE";
    private static final int DATABASE_VERSION = 3;

    public static final String ID="_ID";
    public static final String DATE_DEBUT="DATE_DEBUT";
    public static final String DATE_FIN="DATE_FIN";
    public static final String COMMENTAIRE="COMMENTAIRE";

    public static final String TABLE_PAUSE = "TABLE_PAUSE";
    public static final String PAUSE_ID = "_ID";
    public static final String PAUSE_DEBUT = "_DEBUT";
    public static final String PAUSE_FIN = "_FIN";

    public Context context;

    public DatabaseHelper(Context leContext)
    {
        super(leContext, DATABASE_NAME, null,DATABASE_VERSION);
        this.context = leContext;
    }
	  
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        long id;
        //android.util.Log.w("Constants", "onCreate");
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);

        db.execSQL("CREATE TABLE "+DATABASE_NAME+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
        DATE_DEBUT + " text,"+
        DATE_FIN + " text,"+
        COMMENTAIRE + " text)");

        db.execSQL("DROP INDEX IF EXISTS INDEX_" + DATABASE_NAME);
        String requete = "CREATE INDEX INDEX_" + DATABASE_NAME +
        " ON " + DATABASE_NAME + "("+DATE_DEBUT+")";

        db.execSQL(requete);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAUSE);

        db.execSQL("CREATE TABLE "+TABLE_PAUSE+" ("+PAUSE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
              PAUSE_DEBUT + " text,"+
              PAUSE_FIN + " text)");

        id=  insereNouveauPointage(db, "", "");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //android.util.Log.w("Constants", "Maj de la base");
        if (oldVersion == 1 && newVersion == 2)
        {
            db.execSQL("ALTER TABLE POINTAGE ADD COMMENTAIRE TEXT");
        }
        if(oldVersion < 3)
        {
            db.execSQL("DROP INDEX IF EXISTS INDEX_" + DATABASE_NAME);
            String requete = "CREATE INDEX INDEX_" + DATABASE_NAME +
              " ON " + DATABASE_NAME + "("+DATE_DEBUT+")";
            db.execSQL(requete);

            db.execSQL("CREATE TABLE "+TABLE_PAUSE+" ("+PAUSE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                    PAUSE_DEBUT + " text,"+
                    PAUSE_FIN + " text)");
        }
    }
	
    //---opens the database---
    public SQLiteDatabase open() throws SQLException
    {
        return this.getWritableDatabase();
    }
		
    //---closes the database---
    public void close(SQLiteDatabase db)
    {
        db.close();
    }

            /*****************************************/
            /*********** TABLE POINTAGE **************/
            /*****************************************/

    //---updates a title---
    public boolean updateEnregistrementPointage(SQLiteDatabase db, long rowId, String colonne, String valeur)
    {
        ContentValues args = new ContentValues();
        args.put(colonne, valeur);
        return db.update(DATABASE_NAME, args,
                ID + "=" + rowId, null) > 0;
    }

    //---deletes a particular title---
    public boolean deleteEnregistrementPointage(SQLiteDatabase db, long rowId)
    {
        return db.delete(DATABASE_NAME, ID + "=" + rowId, null) > 0;
    }

    //---deletes a particular title---
    public boolean purge_pointage_vide(SQLiteDatabase db)
    {
        return db.delete(DATABASE_NAME, DATE_DEBUT + "='' AND " + DATE_FIN +" = ''", null) > 0;
    }

    //---retrieves all the titles---
    public Cursor getAllPointage(SQLiteDatabase db)
    {
        return db.query(DATABASE_NAME, new String[] {
            ID,
            DATE_DEBUT,
            DATE_FIN,
            COMMENTAIRE},
            null,
            null,
            null,
            null,
            null);
    }
	    
    //---retrieves all the titles---
    public Cursor getSomeDatePointage(SQLiteDatabase db, String conditions)
    {
        return  db.query(true,DATABASE_NAME, new String[] {
                ID,
                DATE_DEBUT,
                DATE_FIN,
                COMMENTAIRE},
                conditions,
                null,
                null,
                null,
                " DATE_DEBUT ",
                null);
    }
	    
  //---retrieves a particular title---
    public Cursor selectDatePointage(SQLiteDatabase db, long rowId) throws SQLException
    {
         Cursor mCursor =
             db.query(true, DATABASE_NAME, new String[] {
                     ID,
                     DATE_DEBUT,
                     DATE_FIN,
                     COMMENTAIRE},
                     ID + "=" + rowId,
                    null,
                    null,
                    null,
                    null,
                    null);

        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getLastEnregistrementPointage(SQLiteDatabase db) throws SQLException
    {
         Cursor mCursor =
             db.query(true, DATABASE_NAME, new String[] {
                     ID,
                     DATE_DEBUT,
                     DATE_FIN,
                     COMMENTAIRE},
                    null,
                    null,
                    null,
                    null,
                    ID + " DESC",
                    "1");

        if (mCursor != null)
        {
            mCursor.moveToFirst();
            return mCursor;
        }
        else
        {
            return null;
        }

    }

    //---insert a title into the database---
    public long insereNouveauPointage(SQLiteDatabase db, String date_debut, String date_fin)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(DATE_DEBUT, date_debut);
        initialValues.put(DATE_FIN, date_fin);
        initialValues.put(COMMENTAIRE, "");
        long retour = db.insert(DATABASE_NAME, null, initialValues);
        return retour;
    }

                /*****************************************/
                /*********** TABLE PAUSE **************/
                /*****************************************/

    public boolean updateEnregistrementPause(SQLiteDatabase db, long rowId, String colonne, String valeur)
    {
        ContentValues args = new ContentValues();
        args.put(colonne, valeur);
        return db.update(TABLE_PAUSE, args,
                PAUSE_ID + "=" + rowId, null) > 0;
    }

    public boolean deleteEnregistrementPause(SQLiteDatabase db, long rowId)
    {
        return db.delete(TABLE_PAUSE, PAUSE_ID + "=" + rowId, null) > 0;
    }

    public Cursor getAllPause(SQLiteDatabase db)
    {
        return db.query(TABLE_PAUSE, new String[] {
                        PAUSE_ID,
                        PAUSE_DEBUT,
                        PAUSE_FIN},
                null,
                null,
                null,
                null,
                null);
    }

    //---retrieves all the titles---
    public Cursor getSomeDatePause(SQLiteDatabase db, String conditions)
    {
        return  db.query(true,TABLE_PAUSE, new String[] {
                        PAUSE_ID,
                        PAUSE_DEBUT,
                        PAUSE_FIN},
                conditions,
                null,
                null,
                null,
                null,
                null);
    }

    //---retrieves a particular title---
    public Cursor selectDatePause(SQLiteDatabase db, long rowId) throws SQLException
    {
        Cursor mCursor =
                db.query(true, TABLE_PAUSE, new String[] {
                                PAUSE_ID,
                                PAUSE_DEBUT,
                                PAUSE_FIN},
                        PAUSE_ID + "=" + rowId,
                        null,
                        null,
                        null,
                        null,
                        null);

        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---insert a title into the database---
    public long insereNouvellePause(SQLiteDatabase db, String date_debut, String date_fin)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(PAUSE_DEBUT, date_debut);
        initialValues.put(PAUSE_FIN, date_fin);
        long retour = db.insert(TABLE_PAUSE, null, initialValues);
        //android.util.Log.w("Insere pause", "retour="+retour + "Debut="+date_debut+" Fin="+date_fin);
        return retour;
    }
}
