package com.smn.emvcore.interfaces;

import com.smn.emvcore.model.EmvResults;

import org.jetbrains.annotations.NotNull;

public interface ResultsListener {

    void onSuccess(@NotNull EmvResults emvResults);

    void onError(String message);
}
