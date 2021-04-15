package com.smn.emvcore.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HexUtil {

    @Nullable
    public static String bytesToHexadecimal(@NotNull byte[] bytesIn) {
        // Returning result
        String result = null;
        // - Returning result

        StringBuilder stringBuilder = null;

        try {

            stringBuilder = new StringBuilder();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.err.println(e.toString());
        }

        if (stringBuilder != null) {
            for (byte byteOut : bytesIn) {
                try {
                    stringBuilder.append(String.format("%02X", byteOut));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e.getMessage());
                    System.err.println(e.toString());
                }
            }

            try {
                result = stringBuilder.toString();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
                System.err.println(e.toString());
            }
        }

        return result;
    }

    @Nullable
    public static byte[] hexadecimalToBytes(@NotNull String hexadecimal) {
        // Returning result
        byte[] result = null;
        // - Returning result

        try {
            result = new byte[hexadecimal.length() / 2];
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.err.println(e.toString());
        }

        if (result != null) {
            for (int i = 0; i < hexadecimal.length(); i += 2) {
                try {
                    result[i / 2] = (byte) ((Character.digit(hexadecimal.charAt(i), 16) << 4) + Character.digit(hexadecimal.charAt(i + 1), 16));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e.getMessage());
                    System.err.println(e.toString());
                }
            }
        }

        return result;
    }

    @Nullable
    public static String hexadecimalToAscii(@NotNull String hexadecimal) {
        // Returning result
        String result = null;
        // - Returning result

        StringBuilder stringBuilder = null;
        try {
            stringBuilder = new StringBuilder();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.err.println(e.toString());
        }

        if (stringBuilder != null) {
            for (int i = 0; i < hexadecimal.length(); i += 2) {
                try {
                    stringBuilder.append((char) Integer.parseInt(hexadecimal.substring(i, i + 2), 16));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e.getMessage());
                    System.err.println(e.toString());
                }
            }

            try {
                result = stringBuilder.toString();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
                System.err.println(e.toString());
            }
        }

        return result;
    }

    @Nullable
    public static String bytesToAscii(@NotNull byte[] bytesIn) {
        // Returning result
        String result = null;
        // - Returning result

        String hexadecimal = bytesToHexadecimal(bytesIn);

        if (hexadecimal != null) {
            result = hexadecimalToAscii(hexadecimal);
        }

        return result;
    }
}
