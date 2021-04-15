package com.smn.cardreaderlib.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.smn.cardreaderlib.NFCReaderSDK;
import com.smn.emvcore.interfaces.KeyUtil;
import com.smn.emvcore.logger.EmvLogger;
import com.smn.emvcore.utils.HexUtil;

import java.util.Arrays;

public class KeyUtils implements KeyUtil {

    private static final String SHARED_PREF = "EncryptionPrefs";
    private final EmvLogger logger = NFCReaderSDK.getInstance().getLogger();
    private final Context context = NFCReaderSDK.getInstance().getContext();


    @Override
    public byte[] getEncryptionKey() {

        byte[] result = null;

        SharedPreferences sharedPreferences = null;

        try {

            sharedPreferences = this.context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);

        } catch (Exception ex) {
            logger.error("Error on getEncryptionKey, Trying to initialize sharedPreferences", ex);
        }

        if (sharedPreferences != null) {

            String encryptionKeyHexadecimal = null;

            try {

                encryptionKeyHexadecimal = sharedPreferences.getString("Key", "Key");

            } catch (Exception e) {
                this.logger.error(e);
            }

            if (encryptionKeyHexadecimal != null && !encryptionKeyHexadecimal.equals("Key")) {

                result = HexUtil.hexadecimalToBytes(encryptionKeyHexadecimal);

            }
        }

        return result;
    }

    @Override
    public boolean putEncryptionKey(byte[] encryptionKey) {

        boolean result = false;
        String encryptionKeyHexadecimal = HexUtil.bytesToHexadecimal(encryptionKey);
        SharedPreferences sharedPreferences = null;

        try {

            sharedPreferences = this.context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);

        } catch (Exception e) {
          this.logger.error("Error on putEncryptionKey, Trying to initialize sharedPreferences",e);
        }

        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Key", encryptionKeyHexadecimal);
            editor.apply();

            if (sharedPreferences.contains("Key")) {
                String getEncryptionKeyHexadecimal = null;
                try {
                    getEncryptionKeyHexadecimal = sharedPreferences.getString("Key", "Key");
                } catch (Exception e) {
                    this.logger.error(e);
                }

                if (getEncryptionKeyHexadecimal != null) {
                    byte[] getEncryptionKey = HexUtil.hexadecimalToBytes(getEncryptionKeyHexadecimal);

                    if (getEncryptionKey != null && Arrays.equals(encryptionKey, getEncryptionKey)) {
                        result = true;
                    }
                }
            }
        }
        return result;
    }
}
