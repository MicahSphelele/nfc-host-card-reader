package com.smn.emvcore.interfaces;

public interface KeyUtil {

    byte[] getEncryptionKey();

    boolean putEncryptionKey(byte[] encryptionKey);
}
