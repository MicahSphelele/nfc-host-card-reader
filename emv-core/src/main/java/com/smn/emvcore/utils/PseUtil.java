package com.smn.emvcore.utils;


import com.smn.emvcore.enums.SelectCommands;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;

public class PseUtil {


    // PSE (Payment System Environment)
    @Nullable
    public static byte[] selectPse(@Nullable byte[] pse) {
        // Returning result
        byte[] result = null;
        // - Returning result

        ByteArrayOutputStream pseByteArrayOutputStream = null;

        try {

            pseByteArrayOutputStream = new ByteArrayOutputStream();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.err.println(e.toString());
        }

        if (pseByteArrayOutputStream != null) {

            try {

                pseByteArrayOutputStream.write(SelectCommands.SELECT.getBytes()); // Cla, Ins

                pseByteArrayOutputStream.write(new byte[]{
                        (byte) 0x04, // P1
                        (byte) 0x00 // P2
                });

                if (pse != null) {
                    pseByteArrayOutputStream.write(new byte[]{
                            (byte) pse.length // Lc
                    });

                    pseByteArrayOutputStream.write(pse); // Data
                } else {
                    pseByteArrayOutputStream.write(new byte[]{
                            (byte) SelectCommands.PSE.getBytes().length // Lc
                    });

                    pseByteArrayOutputStream.write(SelectCommands.PSE.getBytes()); // Data
                }

                pseByteArrayOutputStream.write(new byte[]{
                        (byte) 0x00 // Le
                });

                pseByteArrayOutputStream.close();

                result = pseByteArrayOutputStream.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
                System.err.println(e.toString());
            }
        }

        return result;
    }
    // - PSE (Payment System Environment)

    // PPSE (Proximity Payment System Environment)
    @Nullable
    public static byte[] selectPpse(@Nullable byte[] ppse) {
        // Returning result
        byte[] result = null;
        // - Returning result

        ByteArrayOutputStream ppseByteArrayOutputStream = null;
        try {
            ppseByteArrayOutputStream = new ByteArrayOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.err.println(e.toString());
        }

        if (ppseByteArrayOutputStream != null) {

            try {

                ppseByteArrayOutputStream.write(SelectCommands.SELECT.getBytes()); // Cla, Ins

                ppseByteArrayOutputStream.write(new byte[]{
                        (byte) 0x04, // P1
                        (byte) 0x00 // P2
                });

                if (ppse != null) {
                    ppseByteArrayOutputStream.write(new byte[]{
                            (byte) ppse.length // Lc
                    });

                    ppseByteArrayOutputStream.write(ppse); // Data
                } else {
                    ppseByteArrayOutputStream.write(new byte[]{
                            (byte) SelectCommands.PPSE.getBytes().length // Lc
                    });

                    ppseByteArrayOutputStream.write(SelectCommands.PPSE.getBytes()); // Data
                }

                ppseByteArrayOutputStream.write(new byte[]{
                        (byte) 0x00 // Le
                });

                ppseByteArrayOutputStream.close();

                result = ppseByteArrayOutputStream.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
                System.err.println(e.toString());
            }
        }

        return result;
    }
    // - PPSE (Proximity Payment System Environment)
}
