package com.smn.emvcore.mocks;

import com.smn.emvcore.interfaces.Transceiver;

import java.io.IOException;

public class MockTransceiver implements Transceiver {
    @Override
    public byte[] transceive(byte[] command) throws IOException {
        return command;
    }

    @Override
    public byte[] getHistoricalBytes() {
        return new byte[0];
    }

    @Override
    public byte[] getHiLayerResponse() {
        return new byte[0];
    }
}
