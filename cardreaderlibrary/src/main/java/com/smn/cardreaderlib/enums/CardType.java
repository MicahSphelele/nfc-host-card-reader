package com.smn.cardreaderlib.enums;

public enum  CardType {

    Visa("Visa","A000000098", "A000000003"),
    Mastercard("Master Card","A000000004"),
    Maestro("Maestro Card"," A0000000043"),
    Unsupported("Unsupported Card");

    private final String[] aidPrefixes;

    public String getCardName() {
        return cardName;
    }

    private final String cardName;

    CardType(String cardName,String... aidPrefixes) {
        this.aidPrefixes = aidPrefixes;
        this.cardName = cardName;
    }

    public String[] getAidPrefixes() {
        return this.aidPrefixes;
    }
}
