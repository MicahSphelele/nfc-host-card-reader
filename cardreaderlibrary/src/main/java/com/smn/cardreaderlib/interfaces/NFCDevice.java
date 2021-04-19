package com.smn.cardreaderlib.interfaces;

import android.app.Activity;

public interface NFCDevice {

    void startCardReader(Activity activity, EmvResultsListener listener);

}
