package com.smn.cardreaderlib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.smn.cardreaderlib.interfaces.NFCDevice;
import com.smn.cardreaderlib.logger.NFCReaderSDKLogger;
import com.smn.cardreaderlib.session.NFCDeviceSession;
import com.smn.emvcore.logger.EmvLogger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NFCReaderSDK {

    @SuppressLint("StaticFieldLeak")
    private static NFCReaderSDK instance = null;

    public static NFCReaderSDK.Builder configureSDK() throws Exception {
        if (instance != null) {
            throw new Exception("NFCReaderSDK has already been initialized");
        }
        return new Builder();
    }

    @NonNull
    private final Context context;

    private final EmvLogger logger;

    @NonNull
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    NFCReaderSDK(@NonNull Context context, NFCReaderSDKLogger logger) {
        this.context = context;
        this.logger = logger;
    }


    public static NFCReaderSDK getInstance() {
        return instance;
    }

    @NonNull
    public Context getContext() {
        return this.context;
    }

    @NonNull
    public EmvLogger getLogger() {
        return this.logger;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    public NFCDevice getNfcDevice() {

        return new NFCDeviceSession();
    }

    @NonNull
    public static ExecutorService getExecutor() {
        return executor;
    }

    public static class Builder {

        Context context;
        NFCReaderSDKLogger logger;

        Builder() {

        }

        public NFCReaderSDK.Builder withContext(@NonNull Context context) {
            this.context = context;
            return this;
        }

        public NFCReaderSDK.Builder withLogger(@NonNull NFCReaderSDKLogger logger) {
            this.logger = logger;
            return this;
        }

        public void apply() {

            NFCReaderSDK.instance = new NFCReaderSDK(this.context, this.logger);
        }
    }
}
