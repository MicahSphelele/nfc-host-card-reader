package com.smn.emvcore.mocks;

import com.smn.emvcore.logger.EmvLogger;

public class MockEmvLogger implements EmvLogger {
    @Override
    public void info(String message) {
        System.out.println(message);
    }

    @Override
    public void warn(String message) {
        System.out.println(message);
    }

    @Override
    public void debug(String message) {
        System.out.println(message);
    }

    @Override
    public void error(String message) {
        System.err.println(message);
    }

    @Override
    public void error(Throwable throwable) {
        System.err.println(throwable.getMessage());
    }

    @Override
    public void error(String message, Throwable throwable) {
        System.err.println("Before throwable" + message);
        System.err.println(throwable.getMessage());
    }
}
