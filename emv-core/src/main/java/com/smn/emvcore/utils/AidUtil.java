package com.smn.emvcore.utils;

import com.smn.emvcore.enums.SelectCommands;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;

public class AidUtil {
    
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
