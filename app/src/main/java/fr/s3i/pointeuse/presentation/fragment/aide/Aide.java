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

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import fr.s3i.pointeuse.R;

/**
 * Created by Adrien on 14/08/2016.
 */
public class Aide extends Fragment implements Parcelable {

    public static Aide newInstance(Parcel parcel) {
        String url = parcel.readString();
        return newInstance(url);
    }

    public static Aide newInstance(String url) {
        Aide aide = new Aide();
        Bundle args = new Bundle();
        args.putString("url", url);
        aide.setArguments(args);
        return aide;
    }

    public static final Parcelable.Creator<Aide> CREATOR = new Creator<Aide>() {
        @Override
        public Aide createFromParcel(Parcel parcel) {
            return newInstance(parcel);
        }

        @Override
        public Aide[] newArray(int i) {
            return new Aide[0];
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aide, container, false);

        Bundle args = getArguments();
        String url = args.getString("url");

        WebView webView = (WebView) view.findViewById(R.id.aide_webview);
        webView.loadUrl(url);

        return view;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Bundle args = getArguments();
        String url = args.getString("url");
        parcel.writeString(url);
    }

}
