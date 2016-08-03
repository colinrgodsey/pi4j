package com.pi4j.io.i2c;

import java.io.IOException;

public class I2CIOException extends IOException {
    int rawCode;

    /**
     * Gets the POSIX code associated with this IO error
     *
     * @return POSIX error code
     */
    public int getCode() {
        return rawCode & 0xFF;
    }

    public int getType() {
        return rawCode / 10000;
    }

    public boolean isIOCTL() {
        return getType() == 1;
    }

    public boolean isWrite() {
        return getType() == 2;
    }

    public boolean isRead() {
        return getType() == 3;
    }

    /**
     * @param msg Exception message
     * @param rawCode POSIX error code with pi4j offsets
     */
    public I2CIOException(String msg, int rawCode) {
        super(msg);

        this.rawCode = rawCode;
    }
}