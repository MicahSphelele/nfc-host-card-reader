package com.smn.emvcore.model;

import com.smn.emvcore.enums.EmvCardType;
import com.smn.emvcore.utils.HexUtil;

import java.util.Arrays;

public class EmvCardTypeObject {

    private final EmvCardType emvCardType;
    private final byte[] aid;
    private final boolean isPayWave;
    private final boolean isPayPass;

    public EmvCardTypeObject(EmvCardType emvCardType,boolean isPayPass,boolean isPayWave, byte[] aid) {
        this.emvCardType = emvCardType;
        this.isPayPass = isPayPass;
        this.isPayWave = isPayWave;
        this.aid = aid;
    }

    public EmvCardType getEmvCardType() {
        return this.emvCardType;
    }

    public byte[] getAid() {
        return this.aid;
    }

    public boolean isPayWave() {
        return this.isPayWave;
    }

    public boolean isPayPass() {
        return this.isPayPass;
    }

    @Override
    public String toString() {
        return "EmvCardTypeObject{" +
                "emvCardType=" + emvCardType +
                ", aid=" + HexUtil.bytesToHexadecimal(aid) +
                ", isPayWave=" + isPayWave +
                ", isPayPass=" + isPayPass +
                '}';
    }
}
