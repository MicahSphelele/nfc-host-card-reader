package com.smn.emvcore.interfaces;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public interface Transceiver {

    byte[] transceive(@NotNull byte[] command) throws IOException;

    byte[] getHistoricalBytes();

    byte[] getHiLayerResponse();

}
