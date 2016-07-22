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

package fr.s3i.pointeuse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Date;

import fr.s3i.pointeuse.business.communs.Contexte;
import fr.s3i.pointeuse.business.pointages.interactors.communs.boundaries.out.model.PointageInfo;
import fr.s3i.pointeuse.business.pointages.interactors.pointer.PointerInteractor;
import fr.s3i.pointeuse.business.pointages.interactors.pointer.boundaries.in.PointerIn;
import fr.s3i.pointeuse.business.pointages.interactors.pointer.boundaries.out.PointerOut;

public class Pointeuse extends AppCompatActivity {

    private Contexte contexte;

    private PointerIn controleur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pointeuse);

        contexte = ((PointageApplication)getApplication()).getContexte();
        contexte.enregistrerService(PointerOut.class, vue);
        contexte.enregistrerService(PointerIn.class, new PointerInteractor(contexte));

        controleur = contexte.getService(PointerIn.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        contexte.detruireService(PointerOut.class);
        contexte.detruireService(PointerIn.class);
    }

    public void pointer(View v) {
        controleur.pointer();
    }

    public void inserer(Date debut, Date fin, String commentaire) {

    }

    private class PointeuseVue implements PointerOut {

        @Override
        public void onPointageInsere(PointageInfo pointage) {
            Log.i(PointeuseVue.class.getSimpleName(), "Insertion pointage réussie.");
        }

        @Override
        public void onError(String message) {
            Log.e(PointeuseVue.class.getSimpleName(), message);
        }

    }

    private final PointerOut vue = new PointeuseVue();

}
