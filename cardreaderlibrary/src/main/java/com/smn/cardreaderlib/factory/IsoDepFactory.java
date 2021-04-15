package com.smn.cardreaderlib.factory;

import android.nfc.Tag;
import android.nfc.tech.IsoDep;

// this exists purely to facilitate mocking IsoDep. Thanks Android
public class IsoDepFactory {
    public IsoDep get(Tag tag) {
        return IsoDep.get(tag);
    }
}
