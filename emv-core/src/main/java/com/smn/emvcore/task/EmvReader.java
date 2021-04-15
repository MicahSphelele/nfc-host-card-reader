package com.smn.emvcore.task;

import com.smn.emvcore.enums.EMVTags;
import com.smn.emvcore.enums.EmvCardType;
import com.smn.emvcore.interfaces.ResultsListener;
import com.smn.emvcore.interfaces.Transceiver;
import com.smn.emvcore.logger.EmvLogger;
import com.smn.emvcore.utils.EmvUtil;
import com.smn.emvcore.utils.HexUtil;
import com.smn.emvcore.utils.PseUtil;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

public class EmvReader implements Runnable {

    private final Transceiver transceiver;
    private final EmvLogger emvLogger;
    private final ResultsListener listener;

    public EmvReader(Transceiver transceiver, EmvLogger emvLogger, ResultsListener listener) {
        this.transceiver = transceiver;
        this.emvLogger = emvLogger;
        this.listener = listener;
    }

    @Override
    public void run() {

        boolean isPayPass = false;
        boolean isPayWave = false;

        // Thread relative
        // ATS (Answer To Select)
        // NfcA (ISO 14443-3A)
        byte[] historicalBytes;

        try {

            historicalBytes = this.transceiver.getHistoricalBytes();

        } catch (Exception ex) {
            this.emvLogger.error(ex);
        }

        // - NfcA (ISO 14443-3A)
        // NfcB (ISO 14443-3B)
        byte[] hiLayerResponse;

        try {

            hiLayerResponse = this.transceiver.getHiLayerResponse();

        } catch (Exception e) {
            this.emvLogger.error(e);
        }

        // PSE (Payment System Environment)
        byte[] commPaymentSystemEnvironment = PseUtil.selectPse(null);
        byte[] respPaymentSystemEnvironment = null;
        boolean pseSucceeded = false;

        if (commPaymentSystemEnvironment != null) {

            this.emvLogger.info("EMV (C-APDU) - Command: Select Data PSE (Payment System Environment) Hexadecimal: "
                    + HexUtil.bytesToHexadecimal(commPaymentSystemEnvironment));
            try {
                respPaymentSystemEnvironment = this.transceiver.transceive(commPaymentSystemEnvironment);
            } catch (Exception e) {
                this.emvLogger.error(e);
            }

            if (respPaymentSystemEnvironment != null) {

                this.emvLogger.info("EMV (R-APDU) - Command: Select Data PSE (Payment System Environment) Hexadecimal: "
                        + HexUtil.bytesToHexadecimal(respPaymentSystemEnvironment));

                if (EmvUtil.isOk(respPaymentSystemEnvironment)) {
                    pseSucceeded = true;
                    this.emvLogger.info("EMV (R-APDU) - Command: Select Data PSE (Payment System Environment) has succeeded");
                }

            }

        }

        // PPSE (Proximity Payment System Environment)
        byte[] commProximityPaymentSystemEnvironment = PseUtil.selectPpse(null);
        byte[] respProximityPaymentSystemEnvironment = null;
        boolean ppseSucceeded = false;

        if (commProximityPaymentSystemEnvironment != null) {
            this.emvLogger.info("EMV (C-APDU) - Command: Select Data PPSE (Proximity Payment System Environment) Hexadecimal: "
                    + HexUtil.bytesToHexadecimal(commProximityPaymentSystemEnvironment));

            try {
                respProximityPaymentSystemEnvironment = this.transceiver.transceive(PseUtil.selectPpse(null));
            } catch (Exception e) {
                this.emvLogger.error(e);
            }

            if (respProximityPaymentSystemEnvironment != null) {
                this.emvLogger.info("EMV (R-APDU) - Command: Select Data PPSE (Proximity Payment System Environment) Hexadecimal: "
                        + HexUtil.bytesToHexadecimal(commPaymentSystemEnvironment));

                if (EmvUtil.isOk(respProximityPaymentSystemEnvironment)) {
                    ppseSucceeded = true;
                    this.emvLogger.info("EMV (R-APDU) - Command: Select Data PPSE (Payment System Environment) has succeeded");
                }
            }
        }

        if (!pseSucceeded && !ppseSucceeded) {
            this.emvLogger.error("Cannot read card");
            listener.onError("Cannot read card");
            return;
        }

        // TLV Extractable Data
        byte[] aid = null; // AID (Application Identifier)
        byte[] applicationLabel = null; // Application Label
        String applicationLabelAscii = null; // Application Label ASCII
        byte[] applicationPan = null; // Application PAN (Primary Account Number)
        byte[] cardholderName = null; // Cardholder Name
        String cardholderNameAscii = null; // Cardholder Name ASCII
        byte[] applicationExpirationDate = null; // Application Expiration Date
        // - TLV Extractable Data

        // AID (Application Identifier)
        if (aid == null && pseSucceeded) {


            ByteArrayInputStream byteArrayInputStream = null;

            try {
                byteArrayInputStream = new ByteArrayInputStream(respPaymentSystemEnvironment);
            } catch (Exception e) {
                this.emvLogger.error(e);
            }

            if (byteArrayInputStream != null) {

                if (byteArrayInputStream.available() < 2) {
                    this.emvLogger.error("Cannot preform TLV byte array stream actions");
                    listener.onError("Cannot preform TLV byte array stream actions");
                    return;
                }

                int index = 0;
                int resultSize = 0;

                byte[] aidTlvTagLength = new byte[EMVTags.AID.getBytes().length];

                while (byteArrayInputStream.read() != -1) {

                    index += 1;

                    if (index >= EMVTags.AID.getBytes().length) {
                        aidTlvTagLength = Arrays.copyOfRange(respPaymentSystemEnvironment,
                                index - EMVTags.AID.getBytes().length, index);
                    }

                    if (Arrays.equals(EMVTags.AID.getBytes(), aidTlvTagLength)) {

                        resultSize = byteArrayInputStream.read();
                    }

                    if (resultSize > byteArrayInputStream.available()) {
                        this.emvLogger.info("Continue");
                        continue;
                    }

                    if (resultSize != -1) {

                        byte[] resultRes = new byte[resultSize];

                        if (byteArrayInputStream.read(resultRes, 0, resultSize) != 0) {

                            if (Arrays.equals(resultRes, EmvCardType.Mastercard.getAidPrefix())) {
                                isPayPass = true;
                                aid = resultRes;
                                this.emvLogger.info("AID Mastercard= " + HexUtil.bytesToHexadecimal(aid));

                            } else if (Arrays.equals(resultRes, EmvCardType.Maestro.getAidPrefix())) {
                                isPayPass = true;
                                aid = resultRes;
                                this.emvLogger.info("AID Maestro= " + HexUtil.bytesToHexadecimal(aid));
                            } else if (Arrays.equals(resultRes, EmvCardType.Visa.getAidPrefix())) {
                                isPayWave = true;
                                aid = resultRes;
                                this.emvLogger.info("AID Visa = " + HexUtil.bytesToHexadecimal(aid));
                            } else if (Arrays.equals(resultRes, EmvCardType.VisaElectron.getAidPrefix())) {
                                isPayWave = true;
                                aid = resultRes;
                                this.emvLogger.info("AID Visa Electron= " + HexUtil.bytesToHexadecimal(aid));
                            } else {
                                this.emvLogger.error("Cannot identify card = " + HexUtil.bytesToHexadecimal(resultRes));
                            }
                        }
                    }
                }

                try {
                    byteArrayInputStream.close();
                    emvLogger.debug("byteArrayInputStream.close()");
                } catch (Exception e) {
                    this.emvLogger.error(e);
                }
            }
        }

        if (aid == null && ppseSucceeded) {

            ByteArrayInputStream byteArrayInputStream = null;

            try {

                byteArrayInputStream = new ByteArrayInputStream(respProximityPaymentSystemEnvironment);

            } catch (Exception e) {
                this.emvLogger.error(e);
            }

            if (byteArrayInputStream != null) {

                if (byteArrayInputStream.available() < 2) {
                    this.emvLogger.error("Cannot preform TLV byte array stream actions");
                    listener.onError("Cannot preform TLV byte array stream actions");
                    return;
                }

                int i = 0, resultSize;

                byte[] aidTlvTagLength = new byte[EMVTags.AID.getBytes().length];

                while (byteArrayInputStream.read() != -1) {

                    i += 1;

                    if (i >= EMVTags.AID.getBytes().length) {
                        aidTlvTagLength = Arrays.copyOfRange(respProximityPaymentSystemEnvironment, i - EMVTags.AID.getBytes().length, i);
                    }

                    if (Arrays.equals(EMVTags.AID.getBytes(), aidTlvTagLength)) {
                        resultSize = byteArrayInputStream.read();

                        if (resultSize > byteArrayInputStream.available()) {
                            continue;
                        }

                        if (resultSize != -1) {

                            byte[] resultRes = new byte[resultSize];

                            if (byteArrayInputStream.read(resultRes, 0, resultSize) != 0) {

                                if (Arrays.equals(resultRes, EmvCardType.Mastercard.getAidPrefix())) {
                                    isPayPass = true;
                                    aid = resultRes;
                                    this.emvLogger.info("AID Mastercard = " + HexUtil.bytesToHexadecimal(aid));

                                } else if (Arrays.equals(resultRes, EmvCardType.Maestro.getAidPrefix())) {
                                    isPayPass = true;
                                    aid = resultRes;
                                    this.emvLogger.info("AID Maestro = " + HexUtil.bytesToHexadecimal(aid));
                                } else if (Arrays.equals(resultRes, EmvCardType.Visa.getAidPrefix())) {
                                    isPayWave = true;
                                    aid = resultRes;
                                    this.emvLogger.info("AID Visa = " + HexUtil.bytesToHexadecimal(aid));
                                } else if (Arrays.equals(resultRes, EmvCardType.VisaElectron.getAidPrefix())) {
                                    isPayWave = true;
                                    aid = resultRes;
                                    this.emvLogger.info("AID Visa Electron = " + HexUtil.bytesToHexadecimal(aid));
                                } else {
                                    aid = resultRes;
                                    this.emvLogger.error("Cannot identify card = " + HexUtil.bytesToHexadecimal(resultRes));
                                }

                                if (aid != null) {
                                    listener.onSuccess(HexUtil.bytesToHexadecimal(aid));
                                    return;
                                }
                                listener.onError("Unable to get AID");
                            }
                        }
                    }
                }

                try {
                    byteArrayInputStream.close();
                    this.emvLogger.debug("byteArrayInputStream.close()");
                } catch (Exception e) {
                    this.emvLogger.error(e);
                }
            }
        }
    }
}
