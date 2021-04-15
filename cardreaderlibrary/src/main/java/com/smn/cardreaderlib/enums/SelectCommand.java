package com.smn.cardreaderlib.enums;

public enum SelectCommand {

    PSE("1PAY.SYS.DDF01"),
    PPSE("2PAY.SYS.DDF01");

    private final String command;


    SelectCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
