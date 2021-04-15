package com.smn.emvcore.utils;

import com.smn.emvcore.enums.SelectCommands;

import org.jetbrains.annotations.NotNull;

public class CccUtil {

    private static final byte CCC_P1 = (byte) 0x8E, CCC_P2 = (byte) 0x80;

    public static boolean isCccCommand(@NotNull byte[] commandApdu) {

        return (commandApdu.length > 4
                && commandApdu[0] == SelectCommands.COMPUTE_CRYPTOGRAPHIC_CHECKSUM.getBytes()[0]
                && commandApdu[1] == SelectCommands.COMPUTE_CRYPTOGRAPHIC_CHECKSUM.getBytes()[1]
                && commandApdu[2] == CCC_P1
                && commandApdu[3] == CCC_P2
        );
    }
}
