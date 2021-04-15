package com.smn.emvcore.utils;

import com.smn.emvcore.enums.EMVTags;
import com.smn.emvcore.enums.SelectCommands;
import com.smn.emvcore.model.TlvObject;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class GacUtil {

    @Nullable
    public byte[] cGac(@NotNull byte[] cdolConstructed) {
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
                byteArrayOutputStream.write(SelectCommands.GENERATE_AC.getBytes()); // Cla, Ins

                byteArrayOutputStream.write(new byte[]{
                        (byte) 0x00, // P1
                        (byte) 0x00, // P2
                        (byte) cdolConstructed.length // Lc
                });

                byteArrayOutputStream.write(cdolConstructed); // Data

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

    @Nullable
    public byte[] fillCdol_1(@Nullable byte[] cdol_1) {
        // Returning result
        byte[] result = null;
        // - Returning result

        ArrayList<TlvObject> tlvObjectArrayList = new ArrayList<>();

        if (cdol_1 != null) {
            for (int i = 0; i < cdol_1.length; i++) {
                int goNext = i;

                byte[] tlvTag = {
                        cdol_1[goNext++]
                };

                if ((tlvTag[0] & 0x1F) == 0x1F) {
                    tlvTag = new byte[]{
                            tlvTag[0], cdol_1[goNext++]
                    };
                }

                TlvObject tlvObj = new TlvObject(tlvTag, cdol_1[goNext]);
                tlvObjectArrayList.add(tlvObj);

                i += tlvObj.getTlvTag().length;
            }
        }

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
                if (cdol_1 != null) {
                    for (TlvObject tlvObject : tlvObjectArrayList) {
                        byte[] generateCdol1Result = new byte[tlvObject.getTlvTagLength()];

                        byte[] resultValue = null;

                        Date transactionDate = new Date();

                        // TTQ (Terminal Transaction Qualifiers); 9F66; 4 Byte(s)
                        if (Arrays.equals(tlvObject.getTlvTag(), EMVTags.TTQ.getBytes())) {

                            System.out.println("Generate CDOL1 -> TTQ (Terminal Transaction Qualifiers); " + "9F66" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                            byte[] data = new byte[4];

                            data[0] |= 1 << 5; // Contactless EMV mode supported (bit index (in the example: "5") <= 7)

                            resultValue = Arrays.copyOf(data, data.length);
                        }
                        // - TTQ (Terminal Transaction Qualifiers); 9F66; 4 Byte(s)

                        // Amount, Authorised (Numeric); 9F02; 6 Byte(s)
                        else if (Arrays.equals(tlvObject.getTlvTag(), EMVTags.AMOUNT_AUTHORISED.getBytes())) {

                            System.out.println("Generate CDOL1 -> Amount, Authorised (Numeric); " + "9F02" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                            resultValue = new byte[tlvObject.getTlvTagLength()];

                            /*resultValue = new byte[]{
                                    (byte) 0x00,
                                    (byte) 0x00,
                                    (byte) 0x00,
                                    (byte) 0x00,
                                    (byte) 0x00,
                                    (byte) 0x00
                            };*/
                        }
                        // - Amount, Authorised (Numeric); 9F02; 6 Byte(s)

                        // Amount, Other (Numeric); 9F03; 6 Byte(s)
                        else if (Arrays.equals(tlvObject.getTlvTag(), EMVTags.AMOUNT_OTHER.getBytes())) {
                            System.out.println("Generate CDOL1 -> Amount, Other (Numeric); " + "9F03" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                            resultValue = new byte[tlvObject.getTlvTagLength()];

                            /*resultValue = new byte[]{
                                    (byte) 0x00,
                                    (byte) 0x00,
                                    (byte) 0x00,
                                    (byte) 0x00,
                                    (byte) 0x00,
                                    (byte) 0x00
                            };*/
                        }
                        // - Amount, Other (Numeric); 9F03; 6 Byte(s)

                        // Terminal Country Code; 9F1A; 2 Byte(s)
                        else if (Arrays.equals(tlvObject.getTlvTag(), EMVTags.TERMINAL_COUNTRY_CODE.getBytes())) {

                            System.out.println("Generate CDOL1 -> Terminal Country Code; " + "9F1A" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                            resultValue = new byte[]{
                                    (byte) 0x01,
                                    (byte) 0x00
                            };

                            // https://en.wikipedia.org/wiki/ISO_3166-1

                            // Example: Bulgaria: 100 (Hexadecimal representation: 0100); Reference: https://en.wikipedia.org/wiki/ISO_3166-1
                        }
                        // - Terminal Country Code; 9F1A; 2 Byte(s)

                        // Transaction Currency Code; 5F2A, 2 Byte(s)
                        else if (Arrays.equals(tlvObject.getTlvTag(), EMVTags.TRANSACTION_CURRENCY_CODE.getBytes())) {
                           System.out.println("Generate CDOL1 -> Transaction Currency Code; " + "5F2A" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                            resultValue = new byte[]{
                                    (byte) 0x09,
                                    (byte) 0x75
                            };

                            // https://en.wikipedia.org/wiki/ISO_4217

                            // Example: Bulgaria (BGN; Bulgarian lev): 975 (Hexadecimal representation: 0975)
                        }
                        // - Transaction Currency Code; 5F2A, 2 Byte(s)

                        // TVR (Transaction Verification Results); 95; 5 Byte(s)
                        else if (Arrays.equals(tlvObject.getTlvTag(), EMVTags.TVR.getBytes())) {

                            System.out.println("Generate CDOL1 -> TVR (Transaction Verification Results); " + "95" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                            resultValue = new byte[tlvObject.getTlvTagLength()];

                            /*resultValue = new byte[]{
                                    (byte) 0x00,
                                    (byte) 0x00,
                                    (byte) 0x00,
                                    (byte) 0x00,
                                    (byte) 0x00
                            };*/
                        }
                        // - TVR (Transaction Verification Results); 95; 5 Byte(s)

                        // Transaction Date; 9A, 3 Byte(s)
                        else if (Arrays.equals(tlvObject.getTlvTag(), EMVTags.TRANSACTION_DATE.getBytes())) {

                            System.out.println("Generate CDOL1 -> Transaction Date; " + "9A" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                            // "SimpleDateFormat" Reference: https://developer.android.com/reference/java/text/SimpleDateFormat.html
                            SimpleDateFormat simpleDateFormat = null;
                            try {
                                simpleDateFormat = new SimpleDateFormat("yyMMdd", Locale.getDefault()); // Format: Year, Month in year, Day in month
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.err.println(e.getMessage());
                                System.err.println(e.toString());
                            }

                            if (simpleDateFormat != null) {
                                String dateFormat = null;
                                try {
                                    dateFormat = simpleDateFormat.format(transactionDate);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    System.err.println(e.getMessage());
                                    System.err.println(e.toString());
                                }

                                if (dateFormat != null) {
                                    resultValue = HexUtil.hexadecimalToBytes(dateFormat);
                                }
                            }
                        }
                        // - Transaction Date; 9A, 3 Byte(s)

                        // Transaction Type; 9C, 1 Byte(s)
                        else if (Arrays.equals(tlvObject.getTlvTag(), EMVTags.TRANSACTION_TYPE.getBytes())) {

                            System.out.println("Generate CDOL1 -> Transaction Type; " + "9C" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                            resultValue = new byte[]{
                                    (byte) 0x00
                            };
                        }
                        // - Transaction Type; 9C, 1 Byte(s)

                        // Transaction Time; 9F21; 3 Byte(s)
                        else if (Arrays.equals(tlvObject.getTlvTag(), EMVTags.TRANSACTION_TIME.getBytes())) {

                            System.out.println("Generate CDOL1 -> Transaction Date; " + "9F21" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                            // "SimpleDateFormat" Reference: https://developer.android.com/reference/java/text/SimpleDateFormat.html
                            SimpleDateFormat simpleDateFormat = null;
                            try {
                                simpleDateFormat = new SimpleDateFormat("HHmmss", Locale.getDefault()); // Format: Hour in day (0-23), Minute in hour, Second in minute
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.err.println(e.getMessage());
                                System.err.println(e.toString());
                            }

                            if (simpleDateFormat != null) {
                                String dateFormat = null;
                                try {
                                    dateFormat = simpleDateFormat.format(transactionDate);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    System.err.println(e.getMessage());
                                    System.err.println(e.toString());
                                }

                                if (dateFormat != null) resultValue = HexUtil.hexadecimalToBytes(dateFormat);
                            }
                        }
                        // - Transaction Time; 9F21; 3 Byte(s)

                        // UN (Unpredictable Number); 9F37, 1 or 4 Byte(s)
                        else if (Arrays.equals(tlvObject.getTlvTag(), EMVTags.UN.getBytes())) {
                            System.out.println("Generate CDOL1 -> UN (Unpredictable Number); " + "9F37" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                            // Generate random unpredictable number
                            SecureRandom unSecureRandom = null;

                            try {

                                unSecureRandom = new SecureRandom();

                            } catch (Exception e) {
                                e.printStackTrace();
                                System.err.println(e.getMessage());
                                System.err.println(e.toString());
                            }

                            if (unSecureRandom != null) {

                                try {
                                    unSecureRandom.nextBytes(generateCdol1Result);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    System.err.println(e.getMessage());
                                    System.err.println(e.toString());
                                }
                            }
                            // - Generate random unpredictable number
                        }
                        // - UN (Unpredictable Number); 9F37, 1 or 4 Byte(s)

                        if (resultValue != null) {
                            try {
                                System.arraycopy(resultValue, 0, generateCdol1Result, 0, Math.min(resultValue.length, generateCdol1Result.length));
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.err.println(e.getMessage());
                                System.err.println(e.toString());
                            }
                        }

                        byteArrayOutputStream.write(generateCdol1Result); // Data
                    }
                }

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
