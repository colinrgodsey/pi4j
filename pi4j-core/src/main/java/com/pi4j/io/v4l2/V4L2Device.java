package com.pi4j.io.v4l2;

import com.pi4j.io.file.IOCTLBuilder;
import com.pi4j.io.file.LinuxFile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Iterator;

public class V4L2Device {
    public static final int DATA_BUFFER_SIZE = 1024;
    public static final int OFFSET_BUFFER_SIZE = 256;

    public static final int SIZE_v4l2_capability = 104;



    public final LinuxFile device;
    public final IOCTLBuilder builder;

    public final ByteBuffer dataBuffer;
    public final IntBuffer offsetBuffer;

    public V4L2Device(LinuxFile device) {
        this.device = device;

        dataBuffer = ByteBuffer.allocateDirect(DATA_BUFFER_SIZE).order(ByteOrder.nativeOrder());
        offsetBuffer = ByteBuffer.allocateDirect(OFFSET_BUFFER_SIZE).order(ByteOrder.nativeOrder()).asIntBuffer();

        this.builder = new IOCTLBuilder(device, dataBuffer, offsetBuffer);
    }

    public Iterator<FormatDescription> getFormats(V4L2BufferType type) throws IOException {
        return new FormatIterator(type);
    }

    class FormatIterator implements Iterator<FormatDescription> {
        private int index = 0;
        private FormatDescription currentNext = null;
        private final V4L2BufferType type;

        public FormatIterator(V4L2BufferType type) throws IOException {
            this.type = type;

            fetchNext();
        }

        void fetchNext() throws IOException {
            currentNext = FormatDescription.ioctl(builder, index, type);
            index++;
        }

        @Override
        public boolean hasNext() {
            return currentNext != null;
        }

        @Override
        public FormatDescription next() {
            return currentNext;
        }

        @Override
        public void remove() {
            try {
                fetchNext();
            } catch (IOException e) {
                //TODO: not sure what to do here
                currentNext = null;
            }
        }
    }
}
