package com.smn.emvcore.utils;

import com.smn.emvcore.enums.SelectCommands;
import com.smn.emvcore.model.AflObject;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AflUtil {

    @Nullable
    public ArrayList<AflObject> getAflDataRecords(@NotNull byte[] aflData) {
        // Returning result
        ArrayList<AflObject> result = null;
        // - Returning result

        System.out.println("AFL Data Length: " + aflData.length);

        if (aflData.length < 4) { // At least 4 bytes length needed to go ahead

            try {

                throw new Exception("Cannot preform AFL data byte array actions, available bytes < 4; Length is " + aflData.length);

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
                System.err.println(e.toString());
            }

        } else {

            result = new ArrayList<>();

            for (int i = 0; i < aflData.length / 4; i++) {
                int firstRecordNumber = aflData[4 * i + 1], lastRecordNumber = aflData[4 * i + 2]; // First record number & final record number

                while (firstRecordNumber <= lastRecordNumber) {

                    AflObject aflObject = new AflObject();
                    aflObject.setSfi(aflData[4 * i] >> 3); // SFI (Short File Identifier)
                    aflObject.setRecordNumber(firstRecordNumber);

                    byte[] cReadRecord = null;

                    ByteArrayOutputStream readRecordByteArrayOutputStream = null;

                    try {

                        readRecordByteArrayOutputStream = new ByteArrayOutputStream();

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println(e.getMessage());
                        System.err.println(e.toString());
                    }

                    if (readRecordByteArrayOutputStream != null) {

                        try {

                            readRecordByteArrayOutputStream.write(SelectCommands.READ_RECORD.getBytes());

                            readRecordByteArrayOutputStream.write(new byte[]{
                                    (byte) aflObject.getRecordNumber(),
                                    (byte) ((aflObject.getSfi() << 0x03) | 0x04),
                            });

                            readRecordByteArrayOutputStream.write(new byte[]{
                                    (byte) 0x00 // Le
                            });

                            readRecordByteArrayOutputStream.close();

                            cReadRecord = readRecordByteArrayOutputStream.toByteArray();
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.err.println(e.getMessage());
                            System.err.println(e.toString());
                        }
                    }

                    if (cReadRecord != null) {
                        aflObject.setReadCommand(cReadRecord);
                    }

                    result.add(aflObject);

                    firstRecordNumber++;
                }
            }
        }

        return result;
    }
}
