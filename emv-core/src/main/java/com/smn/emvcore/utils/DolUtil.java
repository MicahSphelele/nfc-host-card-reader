package com.smn.emvcore.utils;


import com.smn.emvcore.enums.EMVTags;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class DolUtil {

    private static final byte[] TTQ = {
            EMVTags.TTQ.getBytes()[0],
            EMVTags.TTQ.getBytes()[1],
            (byte) 0x04
    }; // TTQ (Terminal Transaction Qualifiers); 2 Byte Tlv Tag with 4 Byte(s)

    private static final byte[] AMOUNT_AUTHORISED = {
            EMVTags.AMOUNT_AUTHORISED.getBytes()[0],
            EMVTags.AMOUNT_AUTHORISED.getBytes()[1],
            (byte) 0x06
    }; // Amount, Authorised (Numeric); 2 Byte Tlv Tag with 6 Byte(s)

    private static final byte[] AMOUNT_OTHER = {
            EMVTags.AMOUNT_OTHER.getBytes()[0],
            EMVTags.AMOUNT_OTHER.getBytes()[1],
            (byte) 0x06
    }; // Amount, Other (Numeric); 2 Byte Tlv Tag with 6 Byte(s)

    private static final byte[] TERMINAL_COUNTRY_CODE = {
            EMVTags.TERMINAL_COUNTRY_CODE.getBytes()[0],
            EMVTags.TERMINAL_COUNTRY_CODE.getBytes()[1],
            (byte) 0x02
    }; // Terminal Country Code; 2 Byte Tlv Tag with 2 Byte(s)

    private static final byte[] TRANSACTION_CURRENCY_CODE = {
            EMVTags.TRANSACTION_CURRENCY_CODE.getBytes()[0],
            EMVTags.TRANSACTION_CURRENCY_CODE.getBytes()[1],
            (byte) 0x02
    }; // Transaction Currency Code; 2 Byte Tlv Tag with 2 Byte(s)

    private static final byte[] TVR = {
            EMVTags.TVR.getBytes()[0],
            (byte) 0x05
    }; // TVR (Transaction Verification Results); 1 Byte Tlv Tag with 5 Byte(s)

    private static final byte[] TRANSACTION_DATE = {
            EMVTags.TRANSACTION_DATE.getBytes()[0],
            (byte) 0x03
    }; // Transaction Date; 1 Byte Tlv Tag with 3 Byte(s)

    private static final byte[] TRANSACTION_TYPE = {
            EMVTags.TRANSACTION_TYPE.getBytes()[0],
            (byte) 0x01
    }; // Transaction Type; 1 Byte Tlv Tag with 1 Byte(s)

    private static final byte[] TRANSACTION_TIME = {
            EMVTags.TRANSACTION_TIME.getBytes()[0],
            EMVTags.TRANSACTION_TIME.getBytes()[1],
            (byte) 0x03
    }; // Transaction Time; 2 Byte Tlv Tag with 3 Byte(s)

    private static final byte[] UN_LENGTH_1 = {
            EMVTags.UN.getBytes()[0],
            EMVTags.UN.getBytes()[1],
            (byte) 0x01
    }, UN_LENGTH_4 = {
            EMVTags.UN.getBytes()[0],
            EMVTags.UN.getBytes()[1],
            (byte) 0x04
    }; // UN (Unpredictable Number); 2 Byte Tlv Tag with (1 Byte(s) / 4 Byte(s))

    public static boolean isValidDol(@Nullable byte[] dol, @NotNull byte[] dolTypeTlvTag) {
        // Returning result
        boolean result = false;
        // - Returning result

        if (dol != null) {
            if (Arrays.equals(EMVTags.PDOL.getBytes(), dolTypeTlvTag)) {
                result = isValidPdol(dol);
            } else if (Arrays.equals(EMVTags.CDOL1.getBytes(), dolTypeTlvTag)) {
                result = isValidCdol_1(dol);
            } else if (Arrays.equals(EMVTags.CDOL2.getBytes(), dolTypeTlvTag)) {
                result = isValidCdol_2(dol);
            }
        }

        return result;
    }

    private static boolean isValidPdol(@NotNull byte[] pdol) {
        // Returning result
        boolean result = false;
        // - Returning result

        String pdolHexadecimal = HexUtil.bytesToHexadecimal(pdol);

        // Go to hexadecimal then check with String.contains()
        if (!result && pdol.length >= TTQ.length) {
            String ttqHexadecimal = HexUtil.bytesToHexadecimal(TTQ);

            if (pdolHexadecimal != null && ttqHexadecimal != null && pdolHexadecimal.contains(ttqHexadecimal)) {
                result = true;
            }
        }

        if (!result && pdol.length >= AMOUNT_AUTHORISED.length) {
            String amountAuthorisedHexadecimal = HexUtil.bytesToHexadecimal(AMOUNT_AUTHORISED);

            if (pdolHexadecimal != null && amountAuthorisedHexadecimal != null && pdolHexadecimal.contains(amountAuthorisedHexadecimal)) {
                result = true;
            }
        }

        if (!result && pdol.length >= AMOUNT_OTHER.length) {
            String amountOtherHexadecimal = HexUtil.bytesToHexadecimal(AMOUNT_OTHER);

            if (pdolHexadecimal != null && amountOtherHexadecimal != null && pdolHexadecimal.contains(amountOtherHexadecimal)) {
                result = true;
            }
        }

        if (!result && pdol.length >= TERMINAL_COUNTRY_CODE.length) {
            String terminalCountryCodeHexadecimal = HexUtil.bytesToHexadecimal(TERMINAL_COUNTRY_CODE);

            if (pdolHexadecimal != null && terminalCountryCodeHexadecimal != null && pdolHexadecimal.contains(terminalCountryCodeHexadecimal)) {
                result = true;
            }
        }

        if (!result && pdol.length >= TRANSACTION_CURRENCY_CODE.length) {
            String transactionCurrencyCode = HexUtil.bytesToHexadecimal(TRANSACTION_CURRENCY_CODE);

            if (pdolHexadecimal != null && transactionCurrencyCode != null && pdolHexadecimal.contains(transactionCurrencyCode)) {
                result = true;
            }
        }

        if (!result && pdol.length >= TVR.length) {
            String tvrHexadecimal = HexUtil.bytesToHexadecimal(TVR);

            if (pdolHexadecimal != null && tvrHexadecimal != null && pdolHexadecimal.contains(tvrHexadecimal)) {
                result = true;
            }
        }

        if (!result && pdol.length >= TRANSACTION_DATE.length) {
            String transactionDateHexadecimal = HexUtil.bytesToHexadecimal(TRANSACTION_DATE);

            if (pdolHexadecimal != null && transactionDateHexadecimal != null && pdolHexadecimal.contains(transactionDateHexadecimal)) {
                result = true;
            }
        }

        if (!result && pdol.length >= TRANSACTION_TYPE.length) {
            String transactionTypeHexadecimal = HexUtil.bytesToHexadecimal(TRANSACTION_TYPE);

            if (pdolHexadecimal != null && transactionTypeHexadecimal != null && pdolHexadecimal.contains(transactionTypeHexadecimal)) {
                result = true;
            }
        }

        if (!result && pdol.length >= TRANSACTION_TIME.length) {
            String transactionTimeHexadecimal = HexUtil.bytesToHexadecimal(TRANSACTION_TIME);

            if (pdolHexadecimal != null && transactionTimeHexadecimal != null && pdolHexadecimal.contains(transactionTimeHexadecimal)) {
                result = true;
            }
        }

        if (!result && pdol.length >= UN_LENGTH_1.length) {
            String un_1Hexadecimal = HexUtil.bytesToHexadecimal(UN_LENGTH_1);

            if (pdolHexadecimal != null && un_1Hexadecimal != null && pdolHexadecimal.contains(un_1Hexadecimal)) {
                result = true;
            }
        }
        if (!result && pdol.length >= UN_LENGTH_4.length) {
            String un_4Hexadecimal = HexUtil.bytesToHexadecimal(UN_LENGTH_4);

            if (pdolHexadecimal != null && un_4Hexadecimal != null && pdolHexadecimal.contains(un_4Hexadecimal)) {
                result = true;
            }
        }

        return result;
    }

    private static boolean isValidCdol_1(@NotNull byte[] cdol_1) {
        return isValidPdol(cdol_1);
    }

    private static boolean isValidCdol_2(@NotNull byte[] cdol_2) {
        return isValidPdol(cdol_2);
    }
}
