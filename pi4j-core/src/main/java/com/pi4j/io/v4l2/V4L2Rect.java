package com.pi4j.io.v4l2;

import com.pi4j.io.file.IOCTLBuilder;

//v4l2_rect
public class V4L2Rect {
    public final int left;
    public final int top;
    public final int width;
    public final int height;

    public V4L2Rect(int left, int top, int width, int height) {
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
    }

    public V4L2Rect(IOCTLBuilder builder) {
        left = builder.dataBuffer.getInt();
        top = builder.dataBuffer.getInt();
        width = builder.dataBuffer.getInt();
        height = builder.dataBuffer.getInt();
    }

    public void write(IOCTLBuilder builder) {
        builder.dataBuffer.putInt(left);
        builder.dataBuffer.putInt(top);
        builder.dataBuffer.putInt(width);
        builder.dataBuffer.putInt(height);
    }
}
