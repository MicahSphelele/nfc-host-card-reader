package com.smn.emvcore.model;

import java.util.List;

public class EmvResults {

    private String aid;
    private String cardNumber;
    private String cardHolderName;
    private List<byte[]> commAppFileList;
    private List<byte[]> respAppFileList;


    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolderName() {
        return this.cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public List<byte[]>  getCommAppFileList() {
        return this.commAppFileList;
    }

    public void setCommAppFileList(List<byte[]>  commAppFileList) {
        this.commAppFileList = commAppFileList;
    }

    public List<byte[]> getRespAppFileList() {
        return respAppFileList;
    }

    public void setRespAppFileList(List<byte[]> respAppFileList) {
        this.respAppFileList = respAppFileList;
    }
}
