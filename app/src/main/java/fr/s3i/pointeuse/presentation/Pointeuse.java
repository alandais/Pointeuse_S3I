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

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import fr.s3i.pointeuse.R;
import fr.s3i.pointeuse.domaine.pointages.Chaines;
import fr.s3i.pointeuse.presentation.activity.FragmentContainer;
import fr.s3i.pointeuse.presentation.activity.NfcActivity;
import fr.s3i.pointeuse.presentation.activity.Preferences;
import fr.s3i.pointeuse.presentation.fragment.calendrier.CalendrierVue;
import fr.s3i.pointeuse.presentation.fragment.commun.Vue;
import fr.s3i.pointeuse.presentation.fragment.pointer.PointerVue;
import fr.s3i.pointeuse.presentation.widget.pointer.PointerWidgetProvider;

/**
 * C'est l'activité principale de l'application, elle affiche : le menu, les fragments 'pointage' et 'calendrier',
 * et écoute les évenement de TAG NFC découverts afin de pouvoir lancer un pointage si un tag répond au format
 * attendu par l'application, ou alors de proposer la création d'un tag au format attendu par l'application,
 * si ce dernier est vide ou non reconnu.
 */
public class Pointeuse extends NfcActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pointeuse);

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), new Vue[]{
                PointerVue.getInstance(Chaines.interacteur_pointer_nom),
                CalendrierVue.getInstance(Chaines.interacteur_calendrier_nom)
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        if (savedInstanceState == null) {
            Toast.makeText(this, Chaines.copyright, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNfcTag(Tag tagNfc) {
        Log.d("NFC", "onNfcTag(" + tagNfc.toString() + ")");
        onTagInconnuDecouvert();
    }

    @Override
    public void onNfcTechTag(Tag tagNfc) {
        Log.d("NFC", "onNfcTechTag(" + tagNfc.toString() + ")");
        onTagInconnuDecouvert();
    }

    @Override
    public void onNfcNdefTag(Tag tagNfc) {
        Log.d("NFC", "onNfcNdefTag(" + tagNfc.toString() + ")");
        onTagConnuDecouvert();
    }

    private void onTagInconnuDecouvert() {
        Toast.makeText(this, "La création de tag n'est pas encore développée", Toast.LENGTH_LONG).show();
    }

    private void onTagConnuDecouvert() {
        pointer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pointeuse, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, FragmentContainer.class);
        switch (item.getItemId()) {
            case R.id.menu_item_preferences:
                intent = new Intent(this, Preferences.class);
                break;
            case R.id.menu_item_export:
                exporter(); // c'est la vue Calendrier qui gère les exports
                return true;
            case R.id.menu_item_aide:
                //TODO
                break;
            case R.id.menu_item_licence:
                //TODO
                break;
            case R.id.menu_item_s3i:
                //TODO
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        startActivity(intent);
        return true;
    }

    private void exporter() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        PagerAdapter adapter = (PagerAdapter) viewPager.getAdapter();
        CalendrierVue calendrierVue = (CalendrierVue) adapter.getItem(1);
        calendrierVue.onExport();
    }

    private void pointer() {
        // c'est plus simple d'appeler le widget provider ici
        PointerWidgetProvider.pointer(this);
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
