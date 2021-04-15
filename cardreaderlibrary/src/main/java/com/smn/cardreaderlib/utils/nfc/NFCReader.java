package com.smn.cardreaderlib.utils.nfc;

import android.app.Activity;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.smn.cardreaderlib.NFCReaderSDK;
import com.smn.cardreaderlib.factory.IsoDepFactory;
import com.smn.emvcore.interfaces.Transceiver;

import java.io.Closeable;
import java.io.IOException;

@RequiresApi(api = Build.VERSION_CODES.O)
public class NFCReader implements Cloneable, Transceiver {
    @NonNull
    private final NFCReader.ReaderImplementation impl;

    public NFCReader(@Nullable Vibrator vibrator, @NonNull IsoDepFactory isoDepFactory) {
        this.impl = new ReaderImplementation(vibrator, isoDepFactory);
    }


    @Override
    public byte[] transceive(byte[] data) throws IOException {

        return this.impl.transceive(data);
    }

    @Override
    public byte[] getHistoricalBytes() {

        return impl.getIsoDep().getHistoricalBytes();
    }

    @Override
    public byte[] getHiLayerResponse() {

        return impl.getIsoDep().getHiLayerResponse();
    }


    public void enableReader(Activity activity) {
        this.impl.enableReader(activity);
    }

    public void connect(CardConnectedListener listener) {
        this.impl.connect(listener);
    }


    public boolean isConnected() {
        return this.impl.isConnected();
    }

    public void waitForCardRemoval(CardRemovedListener cardRemovedListener) {
        this.impl.waitForCardRemoval(cardRemovedListener);
    }

    private static class ReaderImplementation implements NfcAdapter.ReaderCallback, Closeable {
        static final int READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A
                | NfcAdapter.FLAG_READER_NFC_B
                | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK
                | NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS;

        @NonNull
        private final NFCHelper nfcHelper;
        @NonNull
        private final IsoDepFactory isoDepFactory;
        @Nullable
        private final Vibrator vibrator;
        @NonNull
        private final MutableLiveData<IsoDep> isoDepMutableLiveData;

        private IsoDep isoDep;

        public ReaderImplementation(@Nullable Vibrator vibrator, @NonNull IsoDepFactory isoDepFactory) {
            this.nfcHelper = new NFCHelper(NFCReaderSDK.getInstance().getContext());
            this.vibrator = vibrator;
            this.isoDepFactory = isoDepFactory;
            this.isoDepMutableLiveData = new MutableLiveData<>();
        }

        public void connect(CardConnectedListener listener) {
            if (!nfcHelper.isNFCReaderEnabled()) {

                listener.nfcNotEnabled();

            }
            new Handler(Looper.getMainLooper()).post(() -> this.isoDepMutableLiveData.observe(ProcessLifecycleOwner.get(), isoDep -> {
                this.isoDep = isoDep;
                try {
                    if (!this.isoDep.isConnected()) {
                        this.isoDep.connect();
                        if (this.vibrator != null && this.vibrator.hasVibrator()) {
                            this.vibrator.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
                        }
                    }
                    listener.onCardConnected();
                } catch (IOException e) {
                    listener.onError(e);
                }
            }));

        }

        public byte[] transceive(byte[] data) throws IOException {
            return this.getIsoDep().transceive(data);
        }

        @Override
        public void onTagDiscovered(Tag tag) {
            this.isoDepMutableLiveData.postValue(this.isoDepFactory.get(tag));
        }

        public void enableReader(Activity activity) {
            nfcHelper.enableNFCReader(activity,this);
        }

        public boolean isConnected() {
            return this.getIsoDep().isConnected();
        }

        @Override
        public void close() throws IOException {
            this.getIsoDep().close();
        }

        public void waitForCardRemoval(CardRemovedListener cardRemovedListener) {
            NFCReaderSDK.getExecutor().submit(() -> {
                try {
                    IsoDep isoDep = this.getIsoDep();
                    if (!isoDep.isConnected()) {
                        cardRemovedListener.onCardRemoved();
                        return;
                    }
                    int disconnectedTime = 0;
                    while (disconnectedTime < 1000) {
                        if (!isoDep.isConnected()) {
                            disconnectedTime += 250;
                        } else {
                            disconnectedTime = 0;
                        }
                        Thread.sleep(250);
                    }
                    cardRemovedListener.onCardRemoved();
                } catch (InterruptedException e) {
                    cardRemovedListener.onError(new Exception(e));
                }
            });
        }

        @NonNull
        public IsoDep getIsoDep() {
            return this.isoDep;
        }

    }

    public interface CardRemovedListener {
        void onCardRemoved();

        void onError(Throwable throwable);
    }

    public interface CardConnectedListener {
        void onCardConnected();

        void onError(Throwable throwable);

        void nfcNotEnabled();
    }
}
