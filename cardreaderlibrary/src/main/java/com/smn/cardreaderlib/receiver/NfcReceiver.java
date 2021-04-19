package com.smn.cardreaderlib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;

public class NfcReceiver extends BroadcastReceiver {

    private final NfcListener listener;

    public NfcReceiver(NfcListener listener){
        this.listener = listener;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED)) {

            switch (intent.getIntExtra(NfcAdapter.EXTRA_ADAPTER_STATE, NfcAdapter.STATE_OFF)) {
                case NfcAdapter.STATE_ON:
                    listener.onNfcStateChanged(true);
                    break;
                case NfcAdapter.STATE_OFF:
                    listener.onNfcStateChanged(false);
                    break;
            }
        }
    }

   public interface NfcListener{
        void onNfcStateChanged(Boolean isTurnedOn);
    }
}
