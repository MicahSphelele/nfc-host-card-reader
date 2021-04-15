package com.smn.emvcore.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class EmvUtil {

    // APDU (Application Protocol Data Unit) - Get response status words (bytes)
    @Nullable
    public static byte[] getSwBytes(@NotNull byte[] bytesIn) {
        // Returning result
        byte[] result = null;
        // - Returning result

        if (bytesIn.length < 2) {
            try {
                throw new Exception("Invalid response bytes");
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println( e.getMessage());
                System.err.println( e.toString());
            }
        }

        try {
            result = Arrays.copyOfRange(
                    bytesIn, // Original
                    bytesIn.length - 2, // From (to retrieve SW1 & SW2)
                    bytesIn.length // To
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println( e.getMessage());
            System.err.println( e.toString());
        }

        return result;
    }
    // - APDU (Application Protocol Data Unit) - Get response status words (bytes)

    // APDU (Application Protocol Data Unit) - Get response status words (hexadecimal)
    @Nullable
    public static String getSwHexadecimal(@NotNull byte[] bytesIn) {
        // Returning result
        String result = null;
        // - Returning result

        byte[] bytesResult = getSwBytes(bytesIn);

        if (bytesResult != null) {
            try {
                result = HexUtil.bytesToHexadecimal(bytesResult);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println( e.getMessage());
                System.err.println( e.toString());
            }
        }

        return result;
    }
    // - APDU (Application Protocol Data Unit) - Get response status words (hexadecimal)

    // APDU (Application Protocol Data Unit) - Check if response succeed
    @Nullable
    public static boolean isOk(@NotNull byte[] bytesIn) {
        // Returning result
        boolean result = false;
        // - Returning result

        byte[] bytesResult = getSwBytes(bytesIn);

        if (bytesResult != null) {
            try {
                result = Arrays.equals(
                        bytesResult,
                        new byte[]{
                                (byte) 0x90,
                                (byte) 0x00
                        }
                );
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println( e.getMessage());
                System.err.println( e.toString());
            }
        }

        return result;
    }
    // - APDU (Application Protocol Data Unit) - Check if response succeed
}
