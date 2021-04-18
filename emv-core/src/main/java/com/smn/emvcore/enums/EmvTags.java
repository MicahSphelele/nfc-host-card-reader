package com.smn.emvcore.enums;

public enum EmvTags {

    AID("Application Identifier", new byte[]{(byte) 0x4F}),
    PDOL("Processing Options Data Object List",
            new byte[]{(byte) 0x9F, (byte) 0x38}),
    CDOL1("Processing Options Data Object List 1",
            new byte[]{(byte) 0x8C}),
    CDOL2("Processing Options Data Object List 2",
            new byte[]{(byte) 0x8D}),
    GPO_RMT1("GPO Response message template 1", new byte[]{(byte) 0x80}),
    GPO_RMT2("GPO Response message template 2", new byte[]{0x77}),
    AFL("Application File Locator", new byte[]{(byte) 0x94}),
    P_UN_ATC_TRACK1("UN ATC Track 1 data", new byte[]{(byte) 0x9F, (byte) 0x63}),
    N_ATC_TRACK1("N ATC Track 1 data", new byte[]{(byte) 0x9F, (byte) 0x64}),
    P_UN_ATC_TRACK2("N ATC Track 2 data", new byte[]{(byte) 0x9F, (byte) 0x66}),
    N_ATC_TRACK2("N ATC Track 2 data", new byte[]{(byte) 0x9F, (byte) 0x67}),
    APPLICATION_LABEL("Application Label", new byte[]{(byte) 0x50}),
    APPLICATION_PAN("Application (Primary Account Number)", new byte[]{0x5A}),
    CARDHOLDER_NAME("Cardholder Name", new byte[]{(byte) 0x5F, (byte) 0x20}),
    APPLICATION_EXPIRATION_DATE("Application Expiration Date", new byte[]{(byte) 0x5F, (byte) 0x24}),
    TTQ("Terminal Transaction Qualifiers", new byte[]{(byte) 0x9F, (byte) 0x66}),
    AMOUNT_AUTHORISED("Amount, Authorised (Numeric)", new byte[]{(byte) 0x9F, (byte) 0x02}),
    AMOUNT_OTHER("Amount, Other (Numeric)", new byte[]{(byte) 0x9F, (byte) 0x03}),
    TERMINAL_COUNTRY_CODE("Terminal Country Code", new byte[]{(byte) 0x9F, 0x1A}),
    TRANSACTION_CURRENCY_CODE("Transaction Currency Code", new byte[]{0x5F, 0x2A}),
    TVR("Transaction Verification Results", new byte[]{(byte) 0x95}),
    TRANSACTION_DATE("Transaction Date", new byte[]{(byte) 0x9A}),
    TRANSACTION_TIME("Transaction Time", new byte[]{(byte) 0x9F, (byte) 0x21}),
    TRANSACTION_TYPE("Transaction Type", new byte[]{(byte) 0x9C}),
    UN("Unpredictable Number", new byte[]{(byte) 0x9F, (byte) 0x37});

    private final byte[] bytes;
    private final String description;

    EmvTags(String description, byte[] bytes) {
        this.bytes = bytes;
        this.description = description;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public String getDescription() {
        return description;
    }
}
