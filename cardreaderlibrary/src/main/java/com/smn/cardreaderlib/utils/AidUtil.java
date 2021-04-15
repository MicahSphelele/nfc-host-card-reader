package com.smn.cardreaderlib.utils;

import com.payneteasy.tlv.HexUtil;
import com.smn.cardreaderlib.NFCReaderSDK;
import com.smn.cardreaderlib.apdu.ApduCommand;
import com.smn.cardreaderlib.enums.CardType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;

public class AidUtil {

    public static CardType classifyAid(byte[] aid) {
        String hexAid = HexUtil.toHexString(aid);
        for (CardType cardType : CardType.values()) {
            for (String prefix : cardType.getAidPrefixes()) {
                if (hexAid.startsWith(prefix)) {
                    return cardType;
                }
            }
        }
        return CardType.Unsupported;
    }

    @Nullable
    public static byte[] selectAid(@NotNull byte[] aid) {
        // Returning result
        byte[] result = null;
        // - Returning result

        ByteArrayOutputStream byteArrayOutputStream = null;

        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
            NFCReaderSDK.getInstance().getLogger().error(e.getMessage());
            NFCReaderSDK.getInstance().getLogger().error(e.toString());
        }

        if (byteArrayOutputStream != null) {
            try {
                byteArrayOutputStream.write(ApduCommand.SELECT.getBytes()); // Cla, Ins

                byteArrayOutputStream.write(new byte[]{
                        (byte) 0x04, // P1
                        (byte) 0x00, // P2
                        (byte) aid.length // Lc
                });

                byteArrayOutputStream.write(aid); // Data

                byteArrayOutputStream.write(new byte[]{
                        (byte) 0x00 // Le
                });

                byteArrayOutputStream.close();

                result = byteArrayOutputStream.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
                NFCReaderSDK.getInstance().getLogger().error(e.getMessage());
                NFCReaderSDK.getInstance().getLogger().error(e.toString());
            }
        }

        return result;
    }
}
