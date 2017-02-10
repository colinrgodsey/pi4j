package com.pi4j.io.file;

/*-
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  LinuxFile.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2017 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class IOCTLBuilder {
    public final ByteBuffer dataBuffer;
    public final IntBuffer offsetBuffer;

    public IOCTLBuilder(ByteBuffer dataBuffer, IntBuffer offsetBuffer) {
        if(!dataBuffer.isDirect() || !offsetBuffer.isDirect())
            throw new IllegalArgumentException("Direct buffers required for ioctl");

        if(dataBuffer.order() != ByteOrder.nativeOrder()
                || offsetBuffer.order() != ByteOrder.nativeOrder())
            throw new IllegalArgumentException("Buffers must be native ByteOrder");

        this.dataBuffer = dataBuffer;
        this.offsetBuffer = offsetBuffer;
    }

    public String getString(int length) {
        int i;
        int startPosition = dataBuffer.position();
        byte[] tmp = new byte[length];

        for(i = 0 ; i < length ; i++) {
            tmp[i] = dataBuffer.get();

            if(tmp[i] == 0)
                break;
        }

        dataBuffer.position(startPosition + length);

        try {
            return new String(tmp, 0, i, "UTF8");
        } catch (UnsupportedEncodingException e) {
            throw new InternalError(e.getMessage());
        }
    }

    public void advance(int bytes) {
        dataBuffer.position(index() + bytes);
    }

    public int index() {
        return dataBuffer.position();
    }

    public void reset() {
        dataBuffer.clear();
        offsetBuffer.clear();
    }

    public void clear(int length) {
        reset();

        for(int i = 0 ; i < length ; i++)
            dataBuffer.put((byte)0);

        reset();
    }

    public int putPointer() {
        final int oldIndex = index();

        advance(LinuxFile.wordSize);

        return oldIndex;
    }

    public void setPointer(int pointerLocation, int dataOffset) {
        offsetBuffer.put(pointerLocation).put(dataOffset);
    }
}
