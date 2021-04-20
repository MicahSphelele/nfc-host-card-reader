package com.smn.emvcore.handler;

import com.smn.emvcore.enums.EmvCardType;
import com.smn.emvcore.logger.EmvLogger;
import com.smn.emvcore.model.EmvCardTypeObject;
import com.smn.emvcore.utils.HexUtil;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class MiscHandler {

    private final EmvLogger emvLogger;

    public MiscHandler(EmvLogger emvLogger) {
        this.emvLogger = emvLogger;
    }

    @NotNull
    public synchronized EmvCardTypeObject getCardType(byte[] results) {

        byte[] aid;
        EmvCardType emvCardType;
        boolean isPayWave = false;
        boolean isPayPass = false;

        if (Arrays.equals(results, EmvCardType.Mastercard.getAidBytes())) {
            isPayPass = true;
            aid = results;
            emvCardType = EmvCardType.Mastercard;
            this.emvLogger.info("AID Mastercard = " + HexUtil.bytesToHexadecimal(aid));

        } else if (Arrays.equals(results, EmvCardType.Maestro.getAidBytes())) {
            isPayPass = true;
            aid = results;
            emvCardType = EmvCardType.Maestro;
            this.emvLogger.info("AID Maestro = " + HexUtil.bytesToHexadecimal(aid));
        } else if (Arrays.equals(results, EmvCardType.Visa.getAidBytes())) {
            isPayWave = true;
            aid = results;
            emvCardType = EmvCardType.Visa;
            this.emvLogger.info("AID Visa = " + HexUtil.bytesToHexadecimal(aid));
        } else if (Arrays.equals(results, EmvCardType.VisaElectron.getAidBytes())) {
            isPayWave = true;
            aid = results;
            emvCardType = EmvCardType.VisaElectron;
            this.emvLogger.info("AID Visa Electron = " + HexUtil.bytesToHexadecimal(aid));
        } else {
            aid = results;
            emvCardType = EmvCardType.UnsupportedCardType;
            this.emvLogger.error("Cannot identify AID = " + HexUtil.bytesToHexadecimal(aid));
        }

        return new EmvCardTypeObject(emvCardType,isPayPass,isPayWave,aid);
    }
}
