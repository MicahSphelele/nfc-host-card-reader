package com.smn.cardreaderlib.interfaces;

import com.smn.cardreaderlib.enums.NfcState;
import com.smn.cardreaderlib.models.EmvResults;

import org.jetbrains.annotations.NotNull;

public interface EmvResultsListener {

    void onEmvResults(@NotNull EmvResults emvResults);

    void onNfcState(@NotNull NfcState nfcState);

    void onError(@NotNull String message);

}
