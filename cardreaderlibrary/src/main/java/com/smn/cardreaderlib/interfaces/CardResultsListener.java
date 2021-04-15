package com.smn.cardreaderlib.interfaces;

import com.smn.cardreaderlib.models.EMVCard;

public interface CardResultsListener {

    void onEMVCardData(String aid);

    void onNfcNotEnabled();

    void onError(String message);
}
