package com.smn.cardreaderlib.session;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Vibrator;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.smn.cardreaderlib.NFCReaderSDK;
import com.smn.cardreaderlib.enums.NfcState;
import com.smn.cardreaderlib.factory.IsoDepFactory;
import com.smn.cardreaderlib.interfaces.EmvResultsListener;
import com.smn.cardreaderlib.interfaces.NFCDevice;
import com.smn.cardreaderlib.utils.nfc.NFCReader;
import com.smn.emvcore.interfaces.ResultsListener;
import com.smn.emvcore.logger.EmvLogger;
import com.smn.emvcore.model.EmvResults;
import com.smn.emvcore.reader.EmvReader;

import org.jetbrains.annotations.NotNull;

@RequiresApi(api = Build.VERSION_CODES.O)
public class NFCDeviceSession implements NFCDevice {

    public NFCReader nfcReader;
    public EmvLogger logger;

    public NFCDeviceSession() {

        Vibrator vibrator = (Vibrator) NFCReaderSDK
                .getInstance()
                .getContext()
                .getSystemService(Context.VIBRATOR_SERVICE);

        this.nfcReader = new NFCReader(vibrator, new IsoDepFactory());
        this.logger = NFCReaderSDK.getInstance().getLogger();
    }

    @Override
    public void startCardReader(Activity activity, EmvResultsListener listener) {

        NfcDevice nfcDevice = new NfcDevice(activity,listener);
        nfcDevice.startCardReader();
    }

    private class NfcDevice implements DefaultLifecycleObserver {

        private final Activity activity;
        private final EmvResultsListener listener;

        public NfcDevice(Activity activity, EmvResultsListener listener) {
            this.activity = activity;
            this.listener = listener;
            ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        }

        @Override
        public void onResume(@NonNull LifecycleOwner owner) {
            NFCDeviceSession.this.logger.info("onResume State");
        }

        @Override
        public void onPause(@NonNull LifecycleOwner owner) {
            NFCDeviceSession.this.logger.info("onPause State");
        }

        public void startCardReader() {
            NFCDeviceSession.this.nfcReader.enableReader(this.activity);
            NFCDeviceSession.this.nfcReader.connect(new NFCReader.CardConnectedListener() {
                @Override
                public void onCardConnected() {
                    activity.runOnUiThread(() -> new Thread(new EmvReader(nfcReader, logger, new ResultsListener() {

                        @Override
                        public void onSuccess(@NotNull EmvResults emvResults) {

                            listener.onEmvResults(new com.smn.cardreaderlib.models.EmvResults(
                                    emvResults.getAid()
                            ));
                        }

                        @Override
                        public void onError(String message) {
                            listener.onError(message);
                        }
                    })).start());
                }

                @Override
                public void onNfcState(@NotNull NfcState nfcState) {
                    listener.onNfcState(nfcState);
                    logger.error("NFC State = " + nfcState.toString());
                }

                @Override
                public void onError(Throwable throwable) {
                    if (throwable.getMessage() != null) {
                        listener.onError(throwable.getMessage());
                    }
                    logger.error("startCardReader Error", throwable);
                }

            });
        }
    }
}
