package com.smn.cardreaderlib.session;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Vibrator;

import androidx.annotation.RequiresApi;

import com.smn.cardreaderlib.NFCReaderSDK;
import com.smn.cardreaderlib.factory.IsoDepFactory;
import com.smn.cardreaderlib.interfaces.CardResultsListener;
import com.smn.cardreaderlib.interfaces.NFCDevice;
import com.smn.cardreaderlib.utils.nfc.NFCReader;
import com.smn.emvcore.interfaces.ResultsListener;
import com.smn.emvcore.logger.EmvLogger;
import com.smn.emvcore.task.EmvReader;

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
    public void startCardReader(Activity activity,CardResultsListener listener) {

        this.nfcReader.enableReader(activity);

        this.nfcReader.connect(new NFCReader.CardConnectedListener() {
            @Override
            public void onCardConnected() {
                activity.runOnUiThread(() -> new Thread(new EmvReader(nfcReader, logger, new ResultsListener() {

                    @Override
                    public void onSuccess(@NotNull String aid) {
                        listener.onEMVCardData(aid);
                    }

                    @Override
                    public void onError(String message) {
                        listener.onError(message);
                    }
                })).start());
            }

            @Override
            public void nfcNotEnabled() {
                listener.onNfcNotEnabled();
                logger.error("NFC not enabled");
            }

            @Override
            public void onError(Throwable throwable) {
                listener.onError(throwable.getMessage());
                logger.error("startCardReader Error", throwable);
            }

        });
    }
}
