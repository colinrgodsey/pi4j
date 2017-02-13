package com.pi4j.io.v4l2;

import com.pi4j.io.file.IOCTLBuilder;

public class CropCapabilities {
    public static int SIZE = 44; //v4l2_cropcap
    public static long COMMAND = IOCTLCodes.IOWR('V', 58, SIZE); //VIDIOC_CROPCAP

    public final V4L2BufferType type;
    public final V4L2Rect bounds;
    public final V4L2Rect defrect;
    public final V4L2Fract pixelaspect;

    /*
    __u32			type;	// enum v4l2_buf_type
    struct v4l2_rect        bounds;
    struct v4l2_rect        defrect;
    struct v4l2_fract       pixelaspect;
     */

    public CropCapabilities(IOCTLBuilder builder) {
        type = V4L2BufferType.fromValue(builder.dataBuffer.getInt());
        bounds = new V4L2Rect(builder);
        defrect = new V4L2Rect(builder);
    }
}
