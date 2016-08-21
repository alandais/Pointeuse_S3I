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

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import fr.s3i.pointeuse.R;
import fr.s3i.pointeuse.domaine.pointages.Chaines;
import fr.s3i.pointeuse.presentation.activity.FragmentContainer;
import fr.s3i.pointeuse.presentation.activity.NfcActivity;
import fr.s3i.pointeuse.presentation.activity.Preferences;
import fr.s3i.pointeuse.presentation.fragment.aide.APropos;
import fr.s3i.pointeuse.presentation.fragment.aide.Aide;
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

    private static final String URI_POINTER = "pointeuse.s3i.fr/pointer";

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
    }

    @Override
    public void onNfcTag(Tag tagNfc, NdefMessage[] msgs) {
        Log.d("NFC", "tagNfc(" + tagNfc.toString() + ")");
        if (msgs.length > 0) {
            byte[] payload = msgs[0].getRecords()[0].getPayload();
            if(payload.length > 1 && payload[0] == 3 && new String(payload).substring(1).equals(URI_POINTER)) {
                // il semblerait que cela soit notre tag
                onTagConnuDecouvert();
                return;
            }
        }
        onTagInconnuDecouvert(tagNfc);
    }

    private void onTagInconnuDecouvert(final Tag tagNfc) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(Chaines.demander_tagnfc_inconnu_titre)
                .setMessage(Chaines.demander_tagnfc_inconnu_message)
                .setPositiveButton(Chaines.oui, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NdefRecord record = creerNdefUri(URI_POINTER);
                        if (record != null) {
                            ecrireTagNfc(tagNfc, record);
                        }
                    }
                })
                .setNegativeButton(Chaines.non, null)
                .show();
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
                intent.putExtra(FragmentContainer.FRAGMENT_ID, Aide.newInstance("file:///android_asset/aide_" + Chaines.langue + ".html"));
                break;
            case R.id.menu_item_licence:
                intent.putExtra(FragmentContainer.FRAGMENT_ID, Aide.newInstance("file:///android_asset/licence_" + Chaines.langue + ".html"));
                break;
            case R.id.menu_item_s3i:
                intent.putExtra(FragmentContainer.FRAGMENT_ID, Aide.newInstance("file:///android_asset/S3I/presentation_" + Chaines.langue + ".html"));
                break;
            case R.id.menu_item_apropos:
                intent.putExtra(FragmentContainer.FRAGMENT_ID, APropos.newInstance());
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
