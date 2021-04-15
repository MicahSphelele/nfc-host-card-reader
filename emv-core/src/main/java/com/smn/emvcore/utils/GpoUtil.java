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

public class GpoUtil {

    private static final byte GPO_P1 = (byte) 0x00, GPO_P2 = (byte) 0x00;

    public static boolean isGpoCommand(@NotNull byte[] commandApdu) {
        return (commandApdu.length > 4
                && commandApdu[0] == SelectCommands.GET_PROCESSING_OPTIONS.getBytes()[0]
                && commandApdu[1] == SelectCommands.GET_PROCESSING_OPTIONS.getBytes()[1]
                && commandApdu[2] == GPO_P1
                && commandApdu[3] == GPO_P2
        );
    }

    @Nullable
    public byte[] cGpo(@NotNull byte[] pdolConstructed) {
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
                byteArrayOutputStream.write(SelectCommands.GET_PROCESSING_OPTIONS.getBytes()); // Cla, Ins

                byteArrayOutputStream.write(new byte[]{
                        GPO_P1, // P1
                        GPO_P2, // P2
                        (byte) pdolConstructed.length // Lc
                });

                byteArrayOutputStream.write(pdolConstructed); // Data

                byteArrayOutputStream.write(new byte[]{
                        (byte) 0x00 // Le
                });

                byteArrayOutputStream.close();

                // Temporary result
                byte[] tempResult = byteArrayOutputStream.toByteArray();
                /// - Temporary result

                if (tempResult != null && isGpoCommand(tempResult)) {
                    result = tempResult;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
                System.err.println(e.toString());
            }
        }

        return result;
    }

    @Nullable
    public byte[] fillPdol(@Nullable byte[] pdol) {
        // Returning result
        byte[] result = null;
        // - Returning result

        int pdolLength = 0;

        ArrayList<TlvObject> tlvObjectArrayList = new ArrayList<>();

        if (pdol != null) {
            for (int i = 0; i < pdol.length; i++) {
                int goNext = i;

                byte[] tlvTag = {
                        pdol[goNext++]
                };

                if ((tlvTag[0] & 0x1F) == 0x1F) {
                    tlvTag = new byte[]{
                            tlvTag[0], pdol[goNext++]
                    };
                }

                TlvObject tlvObj = new TlvObject(tlvTag, pdol[goNext]);
                tlvObjectArrayList.add(tlvObj);

                i += tlvObj.getTlvTag().length;
            }

            for (TlvObject tlvObject : tlvObjectArrayList) {
                pdolLength += tlvObject.getTlvTagLength();
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
                byteArrayOutputStream.write(new byte[]{
                        (byte) 0x83,
                        (byte) pdolLength
                });

                if (pdol != null) {
                    for (TlvObject tlvObject : tlvObjectArrayList) {
                        byte[] generatePdolResult = new byte[tlvObject.getTlvTagLength()];

                        byte[] resultValue = null;

                        Date transactionDate = new Date();

                        // TTQ (Terminal Transaction Qualifiers); 9F66; 4 Byte(s)
                        if (Arrays.equals(tlvObject.getTlvTag(), EMVTags.TTQ.getBytes())) {
                            System.out.println("Generate PDOL -> TTQ (Terminal Transaction Qualifiers); " + "9F66" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                            byte[] data = new byte[4];

                            data[0] |= 1 << 5; // Contactless EMV mode supported (bit index (in the example: "5") <= 7)

                            resultValue = Arrays.copyOf(data, data.length);
                        }
                        // - TTQ (Terminal Transaction Qualifiers); 9F66; 4 Byte(s)

                        // Amount, Authorised (Numeric); 9F02; 6 Byte(s)
                        else if (Arrays.equals(tlvObject.getTlvTag(), EMVTags.AMOUNT_AUTHORISED.getBytes())) {

                            System.out.println("Generate PDOL -> Amount, Authorised (Numeric); " + "9F02" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

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
                            System.out.println("Generate PDOL -> Amount, Other (Numeric); " + "9F03" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

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

                            System.out.println("Generate PDOL -> Terminal Country Code; " + "9F1A" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

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

                            System.out.println( "Generate PDOL -> Transaction Currency Code; " + "5F2A" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

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

                           System.out.println("Generate PDOL -> TVR (Transaction Verification Results); " + "95" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

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
                            System.out.println( "Generate PDOL -> Transaction Date; " + "9A" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                            // "SimpleDateFormat" Reference: https://developer.android.com/reference/java/text/SimpleDateFormat.html
                            SimpleDateFormat simpleDateFormat = null;
                            try {
                                simpleDateFormat = new SimpleDateFormat("yyMMdd", Locale.getDefault()); // Format: Year, Month in year, Day in month
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.err.println(e.getMessage());
                                System.err.println(e.toString());;
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

                            System.out.println("Generate PDOL -> Transaction Type; " + "9C" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

                            resultValue = new byte[]{
                                    (byte) 0x00
                            };
                        }
                        // - Transaction Type; 9C, 1 Byte(s)

                        // Transaction Time; 9F21; 3 Byte(s)
                        else if (Arrays.equals(tlvObject.getTlvTag(), EMVTags.TRANSACTION_TIME.getBytes())) {

                            System.out.println("Generate PDOL -> Transaction Date; " + "9F21" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

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

                                if (dateFormat != null) {
                                    resultValue = HexUtil.hexadecimalToBytes(dateFormat);
                                }
                            }
                        }
                        // - Transaction Time; 9F21; 3 Byte(s)

                        // UN (Unpredictable Number); 9F37, 1 or 4 Byte(s)
                        else if (Arrays.equals(tlvObject.getTlvTag(), EMVTags.UN.getBytes())) {
                           System.out.println("Generate PDOL -> UN (Unpredictable Number); " + "9F37" + "; " + tlvObject.getTlvTagLength() + " Byte(s)");

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
                                    unSecureRandom.nextBytes(generatePdolResult);
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
                                System.arraycopy(resultValue, 0, generatePdolResult, 0, Math.min(resultValue.length, generatePdolResult.length));
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.err.println(e.getMessage());
                                System.err.println(e.toString());
                            }
                        }

                        byteArrayOutputStream.write(generatePdolResult); // Data
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
