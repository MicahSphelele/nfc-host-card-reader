package com.smn.cardreaderlib.models;

import java.util.List;

public class EmvResult {

    private final String aid;
    private final String cardNumber;
    private final String cardHolderName;
    private List<byte[]> commAppFileList;
    private List<byte[]> respAppFileList;

    public EmvResult(String aid, String cardNumber, String cardHolderName) {
        this.aid = aid;
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
    }

    public String getAid() {
        return this.aid;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public String getCardHolderName() {
        return this.cardHolderName;
    }

    public List<byte[]> getCommAppFileList() {
        return this.commAppFileList;
    }

    public void setCommAppFileList(List<byte[]> commAppFileList) {
        this.commAppFileList = commAppFileList;
    }

    public List<byte[]> getRespAppFileList() {
        return this.respAppFileList;
    }

    public void setRespAppFileList(List<byte[]> respAppFileList) {
        this.respAppFileList = respAppFileList;
    }
}
