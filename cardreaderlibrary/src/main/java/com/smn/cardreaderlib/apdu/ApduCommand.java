package com.smn.cardreaderlib.apdu;

public enum ApduCommand {
    SELECT(new byte[] {0x00, (byte) 0xA4, 0x04, 0x00});

    private final byte[] bytes;

    ApduCommand(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return this.bytes;
    }
}
