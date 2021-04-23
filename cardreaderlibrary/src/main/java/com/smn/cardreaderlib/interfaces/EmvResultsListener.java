package com.smn.cardreaderlib.interfaces;

import com.smn.cardreaderlib.enums.NfcState;
import com.smn.cardreaderlib.models.EmvResult;

import org.jetbrains.annotations.NotNull;

public interface EmvResultsListener {

    void onEmvResults(@NotNull EmvResult emvResult);

    void onNfcState(@NotNull NfcState nfcState);

    void onError(@NotNull String message);

}
