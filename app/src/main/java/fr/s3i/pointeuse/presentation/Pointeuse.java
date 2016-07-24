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

package fr.s3i.pointeuse.presentation;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import fr.s3i.pointeuse.PointageApplication;
import fr.s3i.pointeuse.R;
import fr.s3i.pointeuse.presentation.commun.Vue;
import fr.s3i.pointeuse.presentation.commun.VueTest;

public class Pointeuse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pointeuse);

        PointageApplication application = ((PointageApplication)getApplication());

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), new Vue[] {
                application.creerVuePointer(),
                VueTest.getInstance("Calendrier") // pas encore développée
        });
        ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    static class PagerAdapter extends FragmentStatePagerAdapter {

        private final Vue[] vues;

        public PagerAdapter(FragmentManager fm, Vue[] vues) {
            super(fm);
            this.vues = vues;
        }

        @Override
        public Fragment getItem(int position) {
            return vues[position];
        }

        @Override
        public int getCount() {
            return vues.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return vues[position].getTitre();
        }
    }

}
