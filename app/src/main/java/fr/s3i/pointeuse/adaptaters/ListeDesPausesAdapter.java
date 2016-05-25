package fr.s3i.pointeuse.adaptaters;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

import fr.s3i.pointeuse.R;
import fr.s3i.pointeuse.activite.ReglagePauses;
import fr.s3i.pointeuse.persistance.DatabaseHelper;


public class ListeDesPausesAdapter extends BaseAdapter
{

    Context context;
    public ArrayList<String> listeDebut;
    public ArrayList<String> listeFin;
    public ArrayList<String> listeId;
    public ArrayList<CheckBox> listeCheckBox;
    public ArrayList<Boolean> listeEtat;

    private LayoutInflater mInflater;
    int positionEnCours = -1;
    int debutFinEnCours = -1;

    Time t_debut;
    Time t_fin;

    ReglagePauses parent;

    public ListeDesPausesAdapter(Context context,ReglagePauses pauses)
    {
        this.context = context;
        parent = pauses ;
    }

    @Override
    public int getCount() {
        return listeId.size() ;
    }

    @Override
    public Object getItem(int position) {
        return listeId.get(position) ;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void coche(int position,boolean isChecked)
    {
        listeEtat.set(position, isChecked);
    }

    public void setList(Cursor CursorBase)
    {
        listeFin = new ArrayList<String>();
        listeDebut = new ArrayList<String>();
        listeId = new ArrayList<String>();
        listeCheckBox = new ArrayList<CheckBox>();
        listeEtat = new ArrayList<Boolean>();

        while (CursorBase.moveToNext() )
        {
            if (CursorBase.getString(1).length()>0)
            {
                listeId.add(CursorBase.getString(0)); // 0 is the first column
                listeDebut.add(CursorBase.getString(1)); // 0 is the first column
                listeFin.add(CursorBase.getString(2)); // 0 is the first column
            }
        }
        mInflater = LayoutInflater.from(context);
    }

    public void creerFenetreModification(Context context,int position,int debutFin)//1 debut 2 fin
    {
        Cursor constantsCursor=null;
        DatabaseHelper dbHelper=null;
        SQLiteDatabase db;
        String debut , fin;
        int iheureDebut=-1,iheureFin=-1,iminDebut=-1,iminFin=-1;

        positionEnCours = position;
        debutFinEnCours = debutFin;

        dbHelper = new DatabaseHelper(context) ;
        db = dbHelper.getWritableDatabase();


        constantsCursor=dbHelper.selectDatePause(db, position) ;
        if(constantsCursor==null)
        {
            //android.util.Log.w("Return", "constantsCursor is null");
            //constantsCursor.close();
            dbHelper.close();
            return ;
        }
        if(constantsCursor.getCount()==0)
        {
            //android.util.Log.w("Return", "constantsCursor == 0");
            constantsCursor.close();
            dbHelper.close();
            return;
        }
        debut = constantsCursor.getString(1); // 0 is the first column
        fin = constantsCursor.getString(2); // 0 is the first column

        iheureDebut = 12;
        iminDebut = 0;
        iheureFin = 12;
        iminFin = 0;

        constantsCursor.close();
        dbHelper.close();
        try {
            iheureDebut = Integer.parseInt(debut.charAt(0) + debut.charAt(1) +"" );
            iminDebut = Integer.parseInt(debut.charAt(3) + debut.charAt(4) +"" );

            iheureFin = Integer.parseInt(fin.charAt(0) + fin.charAt(1) +"" );
            iminFin = Integer.parseInt(fin.charAt(3) + fin.charAt(4) +"" );
        }
        catch (Exception e)
        {

        }
        Dialog timepick;

        t_debut = new  Time(iheureDebut,iminDebut,0);
        t_fin = new  Time(iheureFin,iminFin,0);

        if(debutFin==1)timepick = onCreateDialogTime(context,iheureDebut,iminDebut);
        else timepick = onCreateDialogTime(context,iheureFin,iminFin);

        timepick.show();
    }

    private TimePickerDialog.OnTimeSetListener mTimeSetListener =  new TimePickerDialog.OnTimeSetListener()
    {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute)
        {
            Date dateEnCours;

            Time nouvelle_heure = new Time(hourOfDay,minute,0);

            Cursor constantsCursor=null;
            DatabaseHelper dbHelper=null;
            SQLiteDatabase db;

            dbHelper = new DatabaseHelper(view.getContext()) ;
            db = dbHelper.getWritableDatabase();

            constantsCursor=dbHelper.selectDatePause(db, positionEnCours) ;
            if(constantsCursor==null)
            {
                dbHelper.close();
                return ;
            }
            if(constantsCursor.getCount()==0)
            {
                constantsCursor.close();
                dbHelper.close();
                return;
            }
            String s_nouvelle_heure = String.format("%02d:%02d",hourOfDay,minute);
            if (debutFinEnCours==1)
                dbHelper.updateEnregistrementPause(db, positionEnCours, dbHelper.PAUSE_DEBUT, s_nouvelle_heure);
            else
                dbHelper.updateEnregistrementPause(db, positionEnCours, dbHelper.PAUSE_FIN, s_nouvelle_heure);

            constantsCursor.close();
            dbHelper.close();
            parent.refresh();
        }
    };

    protected Dialog onCreateDialogTime(Context context,int heure,int min)
    {
        TimePickerDialog temp = new TimePickerDialog(context , mTimeSetListener, heure, min, true);
        return temp;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LinearLayout layoutItem;

        layoutItem = (LinearLayout) mInflater.inflate(R.layout.listepause, parent, false);

        TextView debut = (TextView)layoutItem.findViewById(R.id.debutPause);
        TextView fin = (TextView)layoutItem.findViewById(R.id.finPause);
        CheckBox checkBox = (CheckBox)layoutItem.findViewById(R.id.checkboxPause);
        checkBox.setTag(position);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                coche(Integer.parseInt(buttonView.getTag().toString()), isChecked);
            }
        });

        if(listeCheckBox.size()>position)
        {
            checkBox.setChecked(listeEtat.get(position));
        }
        else
        {
            listeEtat.add(false);
            listeCheckBox.add(checkBox);
        }

        debut.setText(listeDebut.get(position));
        fin.setText(listeFin.get(position));

        int Id = -1;
        Id = Integer.parseInt(listeId.get(position)) ;

        debut.setTag(Id);
        debut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer position = (Integer) v.getTag();
                creerFenetreModification(v.getContext(), position, 1);
            }
        });

        fin.setTag(Id);
        fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer position = (Integer) v.getTag();
                creerFenetreModification(v.getContext(), position, 2);
            }
        });

        fin.setTag(position);
        debut.setTag(position);

        return layoutItem;
    }

}
