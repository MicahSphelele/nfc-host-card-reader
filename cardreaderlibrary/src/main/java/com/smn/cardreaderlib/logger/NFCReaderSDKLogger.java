package com.smn.cardreaderlib.logger;

import android.util.Log;

import com.smn.emvcore.logger.EmvLogger;

public class NFCReaderSDKLogger implements EmvLogger {

    private static final String TAG = "EmvLogger";

    @Override
    public void info(String message) {
        Log.i(TAG,message);
    }

    @Override
    public void warn(String message) {
        Log.w(TAG,message);
    }

    @Override
    public void debug(String message) {
        Log.d(TAG,message);
    }

    @Override
    public void error(String message) {
        Log.e(TAG,message);
    }

    @Override
    public void error(Throwable throwable) {
        Log.i(TAG,"Unknown error",throwable);
    }

    @Override
    public void error(String message, Throwable throwable) {
        Log.e(TAG,message,throwable);
    }
}
