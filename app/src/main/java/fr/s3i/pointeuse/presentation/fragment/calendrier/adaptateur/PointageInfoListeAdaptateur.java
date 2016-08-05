/*
 * Oburo.O est un programme destinée à saisir son temps de travail sur un support Android.
 *
 *     This file is part of Oburo.O
 *     Oburo.O is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package fr.s3i.pointeuse.presentation.fragment.calendrier.adaptateur;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import fr.s3i.pointeuse.R;
import fr.s3i.pointeuse.domaine.pointages.interactors.lister.boundaries.out.model.PointageInfo;

/**
 * Created by Adrien on 30/07/2016.
 */
public class PointageInfoListeAdaptateur extends BaseAdapter {

    private final Context parentContext;

    private final List<PointageInfo> pointageInfoListe;

    public PointageInfoListeAdaptateur(Context parentContext, List<PointageInfo> pointageInfoListe) {
        this.parentContext = parentContext;
        this.pointageInfoListe = pointageInfoListe;
    }

    @Override
    public int getCount() {
        return pointageInfoListe.size();
    }

    @Override
    public Object getItem(int i) {
        return pointageInfoListe.get(i);
    }

    @Override
    public long getItemId(int i) {
        return pointageInfoListe.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parentContext);
            view = inflater.inflate(R.layout.fragment_calendrier_liste_item, null);
        }
        TextView textDebut = (TextView) view.findViewById(R.id.calendrier_liste_debut);
        if (textDebut != null) {
            textDebut.setText(pointageInfoListe.get(i).getDebut());
        }
        TextView textFin = (TextView) view.findViewById(R.id.calendrier_liste_fin);
        if (textFin != null) {
            textFin.setText(pointageInfoListe.get(i).getFin());
        }
        TextView textDuree = (TextView) view.findViewById(R.id.calendrier_liste_duree);
        if (textDuree != null) {
            textDuree.setText(pointageInfoListe.get(i).getDuree());
        }
        return view;
    }

}
