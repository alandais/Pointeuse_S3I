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

package fr.s3i.pointeuse.presentation.fragment.aide;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.s3i.pointeuse.R;
import fr.s3i.pointeuse.domaine.pointages.Chaines;

/**
 * Created by Adrien on 14/08/2016.
 */
public class APropos extends Fragment implements Parcelable {

    public static APropos newInstance(Parcel parcel) {
        return newInstance();
    }

    public static APropos newInstance() {
        APropos aPropos = new APropos();
        Bundle args = new Bundle();
        aPropos.setArguments(args);
        return aPropos;
    }

    public static final Creator<APropos> CREATOR = new Creator<APropos>() {
        @Override
        public APropos createFromParcel(Parcel parcel) {
            return newInstance(parcel);
        }

        @Override
        public APropos[] newArray(int i) {
            return new APropos[0];
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apropos, container, false);

        TextView copyright = (TextView) view.findViewById(R.id.apropos_copyright);
        copyright.setText(Chaines.copyright);

        ItemAdapter adapter = new ItemAdapter(getContext());
        ListView liste = (ListView) view.findViewById(R.id.apropos_items);
        liste.setAdapter(adapter);

        adapter.add(getString(R.string.version), R.drawable.ic_version);
        adapter.add(Chaines.contact_libelle, R.drawable.ic_contact, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Chaines.contact_url));
                startActivity(i);
            }
        });

        return view;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        // rien
    }

    private static class ItemAdapter extends BaseAdapter {

        private final Context context;

        private final List<View> views = new ArrayList<>();

        public ItemAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public Object getItem(int i) {
            return views.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return views.get(i);
        }

        public void add(String texte, @DrawableRes int image) {
            TextView v = getTextView();
            v.setText(texte);
            v.setCompoundDrawablesWithIntrinsicBounds(image, 0, 0, 0);
            views.add(v);
        }

        public void add(String texte, @DrawableRes int image, View.OnClickListener onClickListerner) {
            TextView v = getTextView();
            v.setText(texte);
            v.setCompoundDrawablesWithIntrinsicBounds(image, 0, 0, 0);
            v.setOnClickListener(onClickListerner);
            views.add(v);
        }

        private TextView getTextView() {
            TextView v = new TextView(context);
            v.setCompoundDrawablePadding(20);
            v.setPadding(20, 20, 20, 20);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                v.setPaddingRelative(20, 20, 20, 20);
            }
            return v;
        }

    }

}
