package com.smn.cardreaderlib.utils.nfc;

import android.app.Activity;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;

public class NFCHelper {

    private final NfcAdapter nfcAdapter;
    private final Context context;

    public NFCHelper(Context context) {

        this.context = context;

        NfcManager nfcManager = (NfcManager) context.getSystemService(Context.NFC_SERVICE);
        this.nfcAdapter = nfcManager != null ? nfcManager.getDefaultAdapter() : NfcAdapter.getDefaultAdapter(context);
    }

    public boolean isNFCReaderAvailable() {
        return this.nfcAdapter != null;
    }

    public boolean isNFCReaderEnabled() {
        return this.nfcAdapter.isEnabled();
    }

    public void enableNFCReader(Activity activity,NfcAdapter.ReaderCallback readerCallback) {

        this.nfcAdapter.enableReaderMode(activity,readerCallback, NfcAdapter.FLAG_READER_NFC_A
                | NfcAdapter.FLAG_READER_NFC_B
                | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK
                | NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS, null);
    }

    public void disableNFCReader() {
        this.nfcAdapter.disableReaderMode((Activity) this.context);
    }


}
