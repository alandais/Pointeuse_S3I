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

package fr.s3i.pointeuse.presentation.fragment.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import fr.s3i.pointeuse.PointageApplication;
import fr.s3i.pointeuse.R;
import fr.s3i.pointeuse.domaine.communs.Contexte;
import fr.s3i.pointeuse.presentation.widget.test.TestWidgetProvider;

/**
 * Created by Adrien on 05/08/2016.
 */
public class TestVue extends Fragment implements View.OnClickListener {

    private TestControleur controleur;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Contexte contexte = ((PointageApplication) getActivity().getApplication()).getContexte();
        controleur = new TestControleur(new TestPresenter(this), contexte);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pointer_vue, container, false);

        Button b = (Button) view.findViewById(R.id.btnPointer);
        b.setOnClickListener(this);
        b = (Button) view.findViewById(R.id.btnInserer);
        b.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        controleur.initialiser();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            controleur.close();
        } catch (IOException e) {
            Toast.makeText(this.getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPointer:
                onPointerPressed();
                break;
            case R.id.btnInserer:
                onInsererPressed();
                break;
        }
    }

    public void updateTextView(String texte) {
        if (this.getView() != null) {
            TextView textRapide = (TextView) this.getView().findViewById(R.id.txtPointageStatut);
            textRapide.setText(texte);
        }
    }

    private void onPointerPressed() {
        controleur.refresh();
    }

    private void onInsererPressed() {
        controleur.lancer();
    }

    public void onAutoRefresh(int delay, TimeUnit unit) {
        controleur.autoRefresh(delay, unit);
    }

    public void onLancement() {
        TestWidgetProvider.wakeUp(getContext());
    }
}
