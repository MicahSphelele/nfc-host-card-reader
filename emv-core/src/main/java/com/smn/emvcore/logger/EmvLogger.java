package com.smn.emvcore.logger;

public interface EmvLogger {

    void info(String message);

    void warn(String message);

    void debug(String message);

    void error(String message);

    void error(Throwable throwable);

    void error(String message, Throwable throwable);

}
