package com.pi4j.io.v4l2;

import com.pi4j.io.file.IOCTLBuilder;

import java.io.IOException;

class Capabilities {
    static final int SIZE = 104; //v4l2_capability
    static final long COMMAND = IOCTLCodes.IOR('V', 0, SIZE); //VIDIOC_QUERYCAP

    public static final int V4L2_CAP_VIDEO_CAPTURE          = 0x00000001;  /* Is a video capture device */
    public static final int V4L2_CAP_VIDEO_OUTPUT           = 0x00000002;  /* Is a video output device */
    public static final int V4L2_CAP_VIDEO_OVERLAY          = 0x00000004;  /* Can do video overlay */
    public static final int V4L2_CAP_VBI_CAPTURE            = 0x00000010;  /* Is a raw VBI capture device */
    public static final int V4L2_CAP_VBI_OUTPUT             = 0x00000020;  /* Is a raw VBI output device */
    public static final int V4L2_CAP_SLICED_VBI_CAPTURE     = 0x00000040;  /* Is a sliced VBI capture device */
    public static final int V4L2_CAP_SLICED_VBI_OUTPUT      = 0x00000080;  /* Is a sliced VBI output device */
    public static final int V4L2_CAP_RDS_CAPTURE            = 0x00000100;  /* RDS data capture */
    public static final int V4L2_CAP_VIDEO_OUTPUT_OVERLAY   = 0x00000200;  /* Can do video output overlay */
    public static final int V4L2_CAP_HW_FREQ_SEEK           = 0x00000400;  /* Can do hardware frequency seek  */
    public static final int V4L2_CAP_RDS_OUTPUT             = 0x00000800;  /* Is an RDS encoder */
    
    /* Is a video capture device that supports multiplanar formats */
    public static final int V4L2_CAP_VIDEO_CAPTURE_MPLANE   = 0x00001000;
    /* Is a video output device that supports multiplanar formats */
    public static final int V4L2_CAP_VIDEO_OUTPUT_MPLANE    = 0x00002000;
    /* Is a video mem-to-mem device that supports multiplanar formats */
    public static final int V4L2_CAP_VIDEO_M2M_MPLANE       = 0x00004000;
    /* Is a video mem-to-mem device */
    public static final int V4L2_CAP_VIDEO_M2M              = 0x00008000;
    
    public static final int V4L2_CAP_TUNER                  = 0x00010000;  /* has a tuner */
    public static final int V4L2_CAP_AUDIO                  = 0x00020000;  /* has audio support */
    public static final int V4L2_CAP_RADIO                  = 0x00040000;  /* is a radio device */
    public static final int V4L2_CAP_MODULATOR              = 0x00080000;  /* has a modulator */
    
    public static final int V4L2_CAP_SDR_CAPTURE            = 0x00100000;  /* Is a SDR capture device */
    public static final int V4L2_CAP_EXT_PIX_FORMAT         = 0x00200000;  /* Supports the extended pixel format */
    public static final int V4L2_CAP_SDR_OUTPUT             = 0x00400000;  /* Is a SDR output device */
    
    public static final int V4L2_CAP_READWRITE              = 0x01000000;  /* read/write systemcalls */
    public static final int V4L2_CAP_ASYNCIO                = 0x02000000;  /* async I/O */
    public static final int V4L2_CAP_STREAMING              = 0x04000000;  /* streaming I/O ioctls */
    
    public static final int V4L2_CAP_TOUCH                  = 0x10000000;  /* Is a touch device */

    public final String driver;
    public final String card;
    public final String busInfo;
    public final int version;
    public final int capabilities;
    public final int deviceCaps;

    public Capabilities(IOCTLBuilder builder) {
        driver = builder.getString(16);
        card = builder.getString(32);
        busInfo = builder.getString(32);
        version = builder.dataBuffer.getInt();
        capabilities = builder.dataBuffer.getInt();
        deviceCaps = builder.dataBuffer.getInt();
    }

    public boolean hasCapability(int cap) {
        return (capabilities & cap) != 0;
    }

    public static Capabilities ioctl(IOCTLBuilder builder) throws IOException {
        builder.clear(SIZE);
        builder.ioctl(COMMAND);

        return new Capabilities(builder);
    }
}
