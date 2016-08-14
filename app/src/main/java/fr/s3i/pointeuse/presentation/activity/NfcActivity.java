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

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import fr.s3i.pointeuse.domaine.pointages.Chaines;

/**
 * Created by Adrien on 08/08/2016.
 */
public abstract class NfcActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;

    @Override
    public void onResume() {
        super.onResume();

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    public abstract void onNfcTag(Tag tagNfc, NdefMessage[] msgs);

    @Override
    public void onNewIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action) ||
            NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) ||
            NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = new NdefMessage[0];
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }

            onNfcTag(tagFromIntent, msgs);
        }
    }

    protected NdefRecord creerNdefUri(String url) {
        final NdefRecord record;
        try {
            final byte[] uriBytes = url.getBytes("UTF-8");
            final byte[] recordBytes = new byte[uriBytes.length + 1];
            recordBytes[0] = (byte) 0x03; // 3 = http://
            System.arraycopy(uriBytes, 0, recordBytes, 1, uriBytes.length);
            record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_URI, new byte[0], recordBytes);
        }
        catch(Exception e) {
            Log.e("NFC", Chaines.erreur_tagnfc_creation, e);
            Toast.makeText(this, Chaines.erreur_tagnfc_creation, Toast.LENGTH_LONG).show();
            return null;
        }
        return record;
    }

    protected void ecrireTagNfc(Tag tagNfc, NdefRecord record) {
        try {
            NdefRecord[] records = {record};
            NdefMessage message = new NdefMessage(records);
            Ndef ndef = Ndef.get(tagNfc);
            ndef.connect();
            ndef.writeNdefMessage(message);
            ndef.close();
            Toast.makeText(this, Chaines.toast_tagnfc_initialise, Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Log.e("NFC", Chaines.erreur_tagnfc_ecriture, e);
            Toast.makeText(this, Chaines.erreur_tagnfc_ecriture, Toast.LENGTH_LONG).show();
        }
    }

}

