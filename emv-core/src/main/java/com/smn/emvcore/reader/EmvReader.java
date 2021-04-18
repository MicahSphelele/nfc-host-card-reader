package com.smn.emvcore.reader;

import com.smn.emvcore.enums.EmvTags;
import com.smn.emvcore.enums.EmvCardType;
import com.smn.emvcore.interfaces.ResultsListener;
import com.smn.emvcore.interfaces.Transceiver;
import com.smn.emvcore.logger.EmvLogger;
import com.smn.emvcore.model.AflObject;
import com.smn.emvcore.utils.AflUtil;
import com.smn.emvcore.utils.AidUtil;
import com.smn.emvcore.utils.DolUtil;
import com.smn.emvcore.utils.EmvUtil;
import com.smn.emvcore.utils.GpoUtil;
import com.smn.emvcore.utils.HexUtil;
import com.smn.emvcore.utils.PseUtil;
import com.smn.emvcore.utils.TlvUtil;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;

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
        boolean aidNotSupported = false;

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

                byte[] aidTlvTagLength = new byte[EmvTags.AID.getBytes().length];

                while (byteArrayInputStream.read() != -1) {

                    index += 1;

                    if (index >= EmvTags.AID.getBytes().length) {
                        aidTlvTagLength = Arrays.copyOfRange(respPaymentSystemEnvironment,
                                index - EmvTags.AID.getBytes().length, index);
                    }

                    if (Arrays.equals(EmvTags.AID.getBytes(), aidTlvTagLength)) {

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
                                this.emvLogger.error("Cannot identify AID = " + HexUtil.bytesToHexadecimal(resultRes));
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

                byte[] aidTlvTagLength = new byte[EmvTags.AID.getBytes().length];

                while (byteArrayInputStream.read() != -1) {

                    i += 1;

                    if (i >= EmvTags.AID.getBytes().length) {
                        aidTlvTagLength = Arrays.copyOfRange(respProximityPaymentSystemEnvironment, i - EmvTags.AID.getBytes().length, i);
                    }

                    if (Arrays.equals(EmvTags.AID.getBytes(), aidTlvTagLength)) {
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
                                    aidNotSupported = true;
                                    this.emvLogger.error("Cannot identify AID = " + HexUtil.bytesToHexadecimal(resultRes));
                                }
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

        if (aidNotSupported) {
            listener.onError("Card AID type not supported");
            return;
        }

        // FCI (File Control Information)
        byte[] commFileControlInfor;
        byte[] respFileControlInfor = null;

        if (Arrays.equals(aid, EmvCardType.Mastercard.getAidPrefix())) {

            commFileControlInfor = AidUtil.selectAid(EmvCardType.Mastercard.getAidPrefix());

            if (commFileControlInfor != null) {

                try {

                    respFileControlInfor = this.transceiver.transceive(AidUtil.selectAid(EmvCardType.Mastercard.getAidPrefix()));

                } catch (Exception e) {
                    this.emvLogger.error(e);
                }
            }

        } else if (Arrays.equals(aid, EmvCardType.Maestro.getAidPrefix())) {
            commFileControlInfor = AidUtil.selectAid(EmvCardType.Maestro.getAidPrefix());

            if (commFileControlInfor != null) {

                try {

                    respFileControlInfor = this.transceiver.transceive(AidUtil.selectAid(EmvCardType.Maestro.getAidPrefix()));

                } catch (Exception e) {
                    this.emvLogger.error(e);
                }
            }
        } else if (Arrays.equals(aid, EmvCardType.Visa.getAidPrefix())) {
            commFileControlInfor = AidUtil.selectAid(EmvCardType.Visa.getAidPrefix());

            if (commFileControlInfor != null) {

                try {

                    respFileControlInfor = this.transceiver.transceive(AidUtil.selectAid(EmvCardType.Visa.getAidPrefix()));

                } catch (Exception e) {
                    this.emvLogger.error(e);
                }
            }
        } else if (Arrays.equals(aid, EmvCardType.VisaElectron.getAidPrefix())) {
            commFileControlInfor = AidUtil.selectAid(EmvCardType.VisaElectron.getAidPrefix());

            if (commFileControlInfor != null) {

                try {

                    respFileControlInfor = this.transceiver.transceive(AidUtil.selectAid(EmvCardType.VisaElectron.getAidPrefix()));

                } catch (Exception e) {
                    this.emvLogger.error(e);
                }
            }
        } else {
            commFileControlInfor = AidUtil.selectAid(aid);
            try {

                respFileControlInfor = this.transceiver.transceive(aid);

            } catch (Exception e) {
                this.emvLogger.error(e);
            }
            this.emvLogger.error("Unknown file control");
        }

        if (commFileControlInfor != null) {
            this.emvLogger.info("EMV (C-APDU) - Command: Select FCI (File Control Information); Data: FCI (File Control Information) Hexadecimal: " + HexUtil.bytesToHexadecimal(commFileControlInfor));
        }

        if (respFileControlInfor != null) {
            this.emvLogger.info("EMV (R-APDU) - Command: Select FCI (File Control Information); Data:  Hexadecimal: " + HexUtil.bytesToHexadecimal(respFileControlInfor));
        }

        if (!EmvUtil.isOk(respFileControlInfor)) {
            listener.onError("Invalid FCI data");
            emvLogger.error("Invalid File Control Information");
            return;
        }

        // Application Label (May be ASCII convertible)
        if (applicationLabel == null) {

            applicationLabel = new TlvUtil().getTlvValue(respFileControlInfor, EmvTags.APPLICATION_LABEL.getBytes());

            if (applicationLabel != null) {
                this.emvLogger.info("EMV (TLV) Application Label  [50] = " + HexUtil.bytesToHexadecimal(applicationLabel));
            }
        }

        // PDOL (Processing Options Data Object List)
        byte[] processingDataObjectList = null;
        byte[] tempProcessingDataObjectList = new TlvUtil().getTlvValue(respFileControlInfor, EmvTags.PDOL.getBytes());

        if (tempProcessingDataObjectList != null &&
                DolUtil.isValidDol(tempProcessingDataObjectList, EmvTags.PDOL.getBytes())) {

            processingDataObjectList = tempProcessingDataObjectList;
            emvLogger.info("EMV (TLV) - Data:  [9F38] Hexadecimal: " + HexUtil.bytesToHexadecimal(processingDataObjectList));
        }

        // PDOL Constructed
        byte[] processingDOListConstructed = new GpoUtil().fillPdol(processingDataObjectList);

        if (processingDOListConstructed != null) {
            this.emvLogger.info("EMV (TLV) - Data:  Constructed : " + Arrays.toString(processingDOListConstructed));
            this.emvLogger.info("EMV (TLV) - Data:  Constructed Hexadecimal : " + HexUtil.bytesToHexadecimal(processingDOListConstructed));
        }

        // GPO (Get Processing Options)
        byte[] commGetProcessingOptions = new GpoUtil().cGpo(processingDOListConstructed);
        byte[] respGetProcessingOptions = null; // C-APDU & R-APDU

        if (commGetProcessingOptions != null) {
            this.emvLogger.info("EMV (C-APDU) - Command: Get Data; GPO (Get Processing Options) Data: " +
                    Arrays.toString(commGetProcessingOptions));
            this.emvLogger.info("EMV (C-APDU) - Command: Get Data; GPO (Get Processing Options) Data: Hexadecimal: "
                    + HexUtil.bytesToHexadecimal(commGetProcessingOptions));
            try {
                respGetProcessingOptions = this.transceiver.transceive(commGetProcessingOptions);
            } catch (Exception e) {
                this.emvLogger.error(e);
            }
        }

        if (respGetProcessingOptions != null) {

            this.emvLogger.info("EMV (R-APDU) - Command: Get Data; GPO (Get Processing Options) Data: Hexidecimal " +
                    HexUtil.bytesToHexadecimal(respGetProcessingOptions));

            if (EmvUtil.isOk(respGetProcessingOptions)) {
                this.emvLogger.info("EMV (R-APDU) - Command: Get Data; GPO (Get Processing Options) Data: Succeeded");
            }
        }

        //PayWave Only
        if (isPayWave) {
            // Application PAN (Primary Account Number)
            if (applicationPan == null) {

                applicationPan = new TlvUtil().getTlvValue(respGetProcessingOptions, EmvTags.APPLICATION_PAN.getBytes());

                if (applicationPan != null) {
                    this.emvLogger.info("EMV (TLV) - Data: Application Account Number  [5A]: " +
                            Arrays.toString(applicationPan));
                    this.emvLogger.info("EMV (TLV) - Data: Application Account Number  [5A] Hexadecimal: " +
                            HexUtil.bytesToHexadecimal(applicationPan));
                }
            }
            // Application PAN (Primary Account Number)
            // Cardholder name
            if (cardholderName == null) {
                cardholderName = new TlvUtil().getTlvValue(respGetProcessingOptions, EmvTags.CARDHOLDER_NAME.getBytes());
                if (cardholderName != null) {
                    this.emvLogger.info("EMV (TLV) - Data: Card holder name [5F20] : " + Arrays.toString(cardholderName));

                    String cardholderNameHexadecimal = HexUtil.bytesToHexadecimal(cardholderName);
                    if (cardholderNameHexadecimal != null) {
                        this.emvLogger.info("EMV (TLV) - Data: Card holder name [5F20] Hexadecimal : " + cardholderNameHexadecimal);
                    }
                }
            }

            // Application Expiration Date
            if (applicationExpirationDate == null) {

                applicationExpirationDate = new TlvUtil().getTlvValue(processingDataObjectList, EmvTags.APPLICATION_EXPIRATION_DATE.getBytes());

                if (applicationExpirationDate != null) {
                    this.emvLogger.info("EMV (TLV) - Data: Application Expiration Date [5F24]: " + Arrays.toString(applicationExpirationDate));

                    String applicationExpirationDateHexadecimal = HexUtil.bytesToHexadecimal(applicationExpirationDate);
                    if (applicationExpirationDateHexadecimal != null) {
                        this.emvLogger.info("EMV (TLV) - Data: Application Expiration Date [5F24] Hexadecimal: " + applicationExpirationDateHexadecimal);
                    }
                }
            }
            // Application Expiration Date
        }
        // - PayWave Only

        // AFL (Application File Locator) [GPO] Data
        byte[] appFileLocatorData = null;

        // Response message template 1 (without tags and lengths)
        if (respGetProcessingOptions[0] == EmvTags.GPO_RMT1.getBytes()[0]) {
            this.emvLogger.info("GPO (Get Processing Options) Response message template 1");
            byte[] gpoData80 = null;
        }
        // - Response message 1 (without tags and lengths)

        // Response message template 2 (with tags and lengths)
        if (respGetProcessingOptions[0] == EmvTags.GPO_RMT2.getBytes()[0]) {
            this.emvLogger.info("GPO (Get Processing Options) Response message template 2");
            byte[] gpoData77 = new TlvUtil().getTlvValue(respGetProcessingOptions, EmvTags.GPO_RMT2.getBytes());
            if (gpoData77 != null) {
                // AFL (Application File Locator)
                byte[] afl; // TLV (Type-length-value) tag specified for AFL (Application File Locator) and result variable

                afl = new TlvUtil().getTlvValue(respGetProcessingOptions, EmvTags.AFL.getBytes());

                if (afl != null) {
                    appFileLocatorData = afl;
                }
                // - AFL (Application File Locator)
            }
        }
        // - Response message template 2 (with tags and lengths)

        // - AFL (Application File Locator) [GPO] Data
        if (appFileLocatorData != null) {
            this.emvLogger.info("EMV (TLV) - Data: AFL (Application File Locator) [94]: " + Arrays.toString(appFileLocatorData));

            String alfDataHexadecimal = HexUtil.bytesToHexadecimal(appFileLocatorData);
            if (alfDataHexadecimal != null) {
                this.emvLogger.info("EMV (TLV) - Data: AFL (Application File Locator) [94] Hexadecimal: " + alfDataHexadecimal);
            }
        }
        // - AFL (Application File Locator) [GPO] Data

        // CDOL1 (Card Risk Management Data Object List 1) & CDOL2 (Card Risk Management Data Object List 2)
        byte[] cardRiskDataObjectList_1 = null;
        byte[] cardRiskDataObjectList_2 = null;

        List<AflObject> appFileLocatorObjectList = new AflUtil().getAflDataRecords(appFileLocatorData);

        // AFL (Application File Locator) Record(s)
        if (!appFileLocatorObjectList.isEmpty() && appFileLocatorObjectList.size() > 0) {

            for (AflObject aflObject : appFileLocatorObjectList) {
                byte[] commReadRecord = aflObject.getReadCommand();
                byte[] respReadRecord = null;

                if (commReadRecord != null) {

                    this.emvLogger.info("EMV (C-APDU) - Command: \"Read Record\"; Data: \"Read Record\" Hexadecimal: " +
                            HexUtil.bytesToHexadecimal(commReadRecord));

                    try {
                        respReadRecord = this.transceiver.transceive(commReadRecord);
                    } catch (Exception e) {
                        this.emvLogger.error(e);
                    }
                }

                if (respReadRecord != null) {

                    boolean succeedLe = false;

                    this.emvLogger.info("EMV (R-APDU) - Command: \"Read Record\"; Data: \"Read Record\" Hexadecimal: " +
                            HexUtil.bytesToHexadecimal(respReadRecord));
                }
            }
        }

        if (aid != null) {
            listener.onSuccess(HexUtil.bytesToHexadecimal(aid));
            return;
        }
        listener.onError("Unable to get AID");
    }
}
