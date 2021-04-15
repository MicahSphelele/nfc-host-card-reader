package com.smn.emvcore.utils;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

public class TlvUtil {

    @Nullable
    public byte[] getTlvValue(@NotNull byte[] dataBytes, @NotNull byte[] tlvTag) {
        // Returning result
        byte[] result = null;
        // - Returning result

        ByteArrayInputStream byteArrayInputStream = null;
        try {

            byteArrayInputStream = new ByteArrayInputStream(dataBytes);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.err.println(e.toString());
        }

        if (byteArrayInputStream != null) {

            if (byteArrayInputStream.available() < 2) {
                try {
                    throw new Exception("Cannot preform TLV byte array stream actions, available bytes < 2; Length is " + byteArrayInputStream.available());
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    System.err.println(e.toString());
                }

            } else {
                int i = 0, resultSize;

                byte[] tlvTagLength = new byte[tlvTag.length];
                // let tlvTagLength: ByteArray? = ByteArray(tlvTag.size) // Kotlin

                while (byteArrayInputStream.read() != -1) {
                    i += 1;

                    if (i >= tlvTag.length) {
                        try {
                            tlvTagLength = Arrays.copyOfRange(dataBytes, i - tlvTag.length, i);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println(e.getMessage());
                            System.err.println(e.toString());
                        }
                    }

                    if (Arrays.equals(tlvTag, tlvTagLength)) {
                        resultSize = byteArrayInputStream.read();

                        if (resultSize > byteArrayInputStream.available()) {
                            continue;
                        }

                        if (resultSize != -1) {
                            byte[] resultRes = new byte[resultSize];
                            // let resultRes: ByteArray? = ByteArray(resultSize) // Kotlin

                            if (byteArrayInputStream.read(resultRes, 0, resultSize) != 0) {
                                String checkResponse = HexUtil.bytesToHexadecimal(dataBytes), checkResult = HexUtil.bytesToHexadecimal(resultRes);

                                if (checkResponse != null && checkResult != null && checkResponse.contains(checkResult)) {
                                    result = resultRes;
                                }
                            }
                        }
                    }
                }
            }

            try {
                byteArrayInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
                System.err.println(e.toString());
            }
        }

        return result;
    }
}
