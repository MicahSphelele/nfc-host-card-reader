package com.smn.emvcore.interfaces;

import com.smn.emvcore.model.EmvResponse;

import org.jetbrains.annotations.NotNull;

public interface ResultsListener {

    void onSuccess(@NotNull EmvResponse emvResponse);

    void onError(String message);
}
