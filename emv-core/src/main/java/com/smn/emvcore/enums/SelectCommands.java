package com.smn.emvcore.enums;

public enum SelectCommands {

    PSE("1PAY.SYS.DDF01".getBytes()),
    PPSE( "2PAY.SYS.DDF01".getBytes()),
    SELECT(new byte[]{ 0x00, (byte) 0xA4}),
    COMPUTE_CRYPTOGRAPHIC_CHECKSUM(new byte[]{(byte) 0x80, 0x2A}),
    GENERATE_AC(new byte[]{ (byte) 0x80, (byte) 0xAE}),
    GET_PROCESSING_OPTIONS(new byte[]{(byte) 0x80,(byte) 0xA8}),
    READ_RECORD(new byte[]{0x00, (byte) 0xB2});

    private final byte[] bytes;

     SelectCommands(byte [] bytes){
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return this.bytes;
    }
}
