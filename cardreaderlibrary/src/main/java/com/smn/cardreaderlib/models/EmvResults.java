package com.smn.cardreaderlib.models;

import java.util.List;

public class EmvResults  {

    private final String aid;
    private final String cardNumber;
    private List<byte[]> commAppFileList;
    private List<byte[]> respAppFileList;

    public EmvResults(String aid,String cardNumber) {
        this.aid = aid;
        this.cardNumber = cardNumber;
    }

    public String getAid() {
        return this.aid;
    }

    public String getCardNumber() {
        return this.cardNumber;
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
