package com.smn.emvcore.model;

public class AflObject {

    private int sfi;
    private int recordNumber;
    private byte[] readCommand;

    public int getSfi() {
        return sfi;
    }

    public void setSfi(int sfi) {
        this.sfi = sfi;
    }

    public int getRecordNumber() {
        return this.recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public byte[] getReadCommand() {
        return this.readCommand;
    }

    public void setReadCommand(byte[] readCommand) {
        this.readCommand = readCommand;
    }
}
