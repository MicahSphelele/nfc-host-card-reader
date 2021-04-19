package com.smn.emvcore.enums;

public enum EmvCardType {

    /*
    Mastercard (PayPass)
    RID: A000000004
    PIX: 1010
    AID (Application Identifier): A0000000041010
    */
    Mastercard("Mastercard (PayPass)", new byte[]{ (byte) 0xA0, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x04, (byte) 0x10,
            (byte) 0x10}),
    /*
    Maestro (PayPass)
    RID: A000000004
    PIX: 3060
    AID (Application Identifier): A0000000043060
    */
    Maestro("Maestro (PayPass)", new byte[]{ (byte) 0xA0, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x04, (byte) 0x30,
            (byte) 0x60}),
    /*
    Visa (PayWave)
    RID: A000000003
    PIX: 1010
    AID (Application Identifier): A0000000031010
    */
    Visa("Visa (PayWave)", new byte[]{ (byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x03, (byte) 0x10,
            (byte) 0x10}),
    /*
    Visa Electron (PayWave)
    RID: A000000003
    PIX: 2010
    AID (Application Identifier): A0000000032010
    */
    VisaElectron("Visa Electron (PayWave)", new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x03, (byte) 0x20,
            (byte) 0x10});

    private final String description;
    private final byte[] aidPrefix;

    EmvCardType(String description, byte[] aidPrefix) {
        this.description = description;
        this.aidPrefix = aidPrefix;
    }

    public String getDescription() {
        return this.description;
    }

    public byte[] getAidPrefix() {
        return aidPrefix;
    }

}
