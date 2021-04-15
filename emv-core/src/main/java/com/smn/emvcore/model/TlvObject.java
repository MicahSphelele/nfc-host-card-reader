package com.smn.emvcore.model;

public class TlvObject {

    private final byte[] tlvTag;
    private final int tlvTagLength;

    public TlvObject(byte[] tlvTag, int tlvTagLength) {
        this.tlvTag = tlvTag;
        this.tlvTagLength = tlvTagLength;
    }

    public byte[] getTlvTag() {
        return tlvTag;
    }

    public int getTlvTagLength() {
        return tlvTagLength;
    }
}
