package com.smn.cardreaderlib.emv;

import com.payneteasy.tlv.BerTag;
import com.payneteasy.tlv.HexUtil;

public enum EMVTag {
    //AccountTypeType(new BerTag(0x5F, 0x57)),

    AID(new BerTag(0x4F)),
    APPLICATION_LABEL(new BerTag(0x50)),
    CARD_HOLDER_NAME(new BerTag( 0x5F, 0x20)),
    APPLICATION_EXPIRATION_DATE(new BerTag(0x5F,0x24)),
    APPLICATION_PAN(new BerTag(0x5A)),
    PROCESS_DATA_OBJECT_LIST(new BerTag( 0x9F, 0x38));

//    Amount(new BerTag(0x9F, 0x02)),
//    CountryCode(new BerTag(0x9F, 0x1A)),
//    CurrencyCode(new BerTag(0x5F, 0x2A)),
//    TerminalFloorLimit(new BerTag(0x9F, 0x1B)),
//    TerminalType(new BerTag(0x9F, 0x35)),
//    TransactionType(new BerTag(0x9C)),
//    AuthTerminalData(new BerTag(0xFF,0x81,0x11)),//Predefined Tag
//    TransactionTime(new BerTag(0x9F,0x21)),
//    POSEntryMode(new BerTag(0x9F,0x39)),
//    TerminalTransactionQualifiers(new BerTag(0x9F,0x66)),
//    TerminalCountryCode(new BerTag(0x9F,0x1A)),
//    MerchantNameAndLocation(new BerTag(0x9F,0x4E)),
//    MinimumCVMLimit(new BerTag(0xDF,0x01));

    private final BerTag berTag;

    EMVTag(BerTag berTag) {
        this.berTag = berTag;
    }

    public BerTag getBerTag() {
        return this.berTag;
    }

    public String getHexString() {
        return HexUtil.toHexString(this.berTag.bytes);
    }

//    public int getInt(){
//        return ByteUtils.byteArrayToInt(this.berTag.bytes);
//    }
}
