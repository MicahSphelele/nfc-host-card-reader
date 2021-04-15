package com.smn.emvcore.interfaces;

import org.jetbrains.annotations.NotNull;

public interface ResultsListener {

    void onSuccess(@NotNull String aid);

    void onError(String message);
}
