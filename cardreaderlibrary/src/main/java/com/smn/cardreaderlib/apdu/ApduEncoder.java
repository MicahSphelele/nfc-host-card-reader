package com.smn.cardreaderlib.apdu;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public class ApduEncoder {
    /**
     * Create an APDU command
     *
     * @param command The command type
     * @param data    The data to be sent
     * @param le      The expected response length
     */

    /**
     * PPSE directory "2PAY.SYS.DDF01"
     */
    private static final String PPSE = "2PAY.SYS.DDF01";

    /**
     * PSE directory "1PAY.SYS.DDF01"
     */
    private static final String PSE = "1PAY.SYS.DDF01";

    public static byte[] encodeCommand(@NotNull ApduCommand command, @NotNull String data, byte le) {
        return encodeCommand(command, data.getBytes(StandardCharsets.US_ASCII), le);
    }

    /**
     * Create an APDU command
     *
     * @param command The command type
     * @param data    The data to be sent
     * @param le      The expected response length
     */
    public static byte[] encodeCommand(@NotNull ApduCommand command, @NotNull byte[] data, byte le) {
        // 1 byte lc, 1 byte le
        byte[] bytes = new byte[command.getBytes().length + data.length + 1 + 1];
        int position = 0;

        System.arraycopy(command.getBytes(), 0, bytes, position, command.getBytes().length);
        position += command.getBytes().length;

        byte[] lc = new byte[]{(byte) data.length};
        System.arraycopy(lc, 0, bytes, position, 1);
        position++;

        System.arraycopy(data, 0, bytes, position, data.length);
        position += data.length;

        System.arraycopy(new byte[]{le}, 0, bytes, position, 1);
        return bytes;
    }
}
