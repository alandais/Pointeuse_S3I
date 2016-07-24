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

package fr.s3i.pointeuse.presentation.commun;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.s3i.pointeuse.domaine.communs.interactors.boundaries.in.InBoundary;

/**
 * Created by Adrien on 24/07/2016.
 * Fragment utilisé en développement, pour remplacer une vue pas encore développée
 */
public class VueTest extends Vue<VueTest.PresenterTest, VueTest.ControleurTest> {

    protected static class ControleurTest extends Controleur<InBoundary> {
        protected ControleurTest() {
            super(new InBoundary() {
                @Override
                public void initialiser() {
                    // rien
                }
            });
        }
    }

    protected static class PresenterTest extends Presenter<VueTest> {
        protected PresenterTest(VueTest vue) {
            super(vue);
        }
    }

    public static VueTest getInstance(String titre) {
        VueTest vue = new VueTest();
        vue.setTitre(titre);
        return vue;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PresenterTest(this);
        controleur = new ControleurTest();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        TextView tv = new TextView(getActivity());
        tv.setGravity(Gravity.CENTER);
        tv.setText(getTitre());
        return tv;
    }

    @Override
    public void onError(String message) {
        TextView tv = getTextView();
        tv.setText(message);
        tv.setTextColor(Color.RED);
    }

    private TextView getTextView() {
        return (TextView)getView();
    }

}
