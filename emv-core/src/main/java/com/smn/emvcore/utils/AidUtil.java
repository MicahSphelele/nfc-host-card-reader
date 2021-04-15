package com.smn.emvcore.utils;

import com.smn.emvcore.enums.SelectCommands;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;

public class AidUtil {
    /*
        Mastercard (PayPass)
        RID: A000000004
        PIX: 1010
        AID (Application Identifier): A0000000041010
     */
    public static byte[] A0000000041010 = {
            (byte) 0xA0,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x04,
            (byte) 0x10,
            (byte) 0x10
    };

    /*
        Maestro (PayPass)
        RID: A000000004
        PIX: 3060
        AID (Application Identifier): A0000000043060
     */
    public static byte[] A0000000043060 = {
            (byte) 0xA0,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x04,
            (byte) 0x30,
            (byte) 0x60
    };

    /*
        Visa (PayWave)
        RID: A000000003
        PIX: 1010
        AID (Application Identifier): A0000000031010
     */
    public static byte[] A0000000031010 = {
            (byte) 0xA0,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x03,
            (byte) 0x10,
            (byte) 0x10
    };

    /*
        Visa Electron (PayWave)
        RID: A000000003
        PIX: 2010
        AID (Application Identifier): A0000000032010
     */
    public static byte[] A0000000032010 = {
            (byte) 0xA0,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x03,
            (byte) 0x20,
            (byte) 0x10
    };

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
            System.err.println(e.getMessage());
            System.err.println(e.toString());
        }

        if (byteArrayOutputStream != null) {
            try {
                byteArrayOutputStream.write(SelectCommands.SELECT.getBytes()); // Cla, Ins

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
                System.err.println(e.getMessage());
                System.err.println(e.toString());
            }
        }

        return result;
    }
}
