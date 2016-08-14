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

package fr.s3i.pointeuse.presentation.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fr.s3i.pointeuse.R;
import fr.s3i.pointeuse.presentation.fragment.commun.Vue;
import fr.s3i.pointeuse.presentation.fragment.commun.VueTest;

public class FragmentContainer extends AppCompatActivity {

    public static final String FRAGMENT_ID = "FRAGMENT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        Fragment content;
        Object argument = getIntent().hasExtra(FRAGMENT_ID) ? getIntent().getExtras().get(FRAGMENT_ID) : null;
        if (argument instanceof Fragment) {
            content = (Fragment) argument;
        }
        else if (argument instanceof String) {
            content = VueTest.getInstance((String) argument);
        }
        else {
            content = VueTest.getInstance("TODO");
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.suppression_content, content).commit();
        }
    }

}
