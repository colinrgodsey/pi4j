package com.pi4j.io.v4l2;

//v4l2_buf_type
public enum V4L2BufferType {
    V4L2_BUF_TYPE_VIDEO_CAPTURE(1),
    V4L2_BUF_TYPE_VIDEO_OUTPUT(2),
    V4L2_BUF_TYPE_VIDEO_OVERLAY(3),
    V4L2_BUF_TYPE_VBI_CAPTURE(4),
    V4L2_BUF_TYPE_VBI_OUTPUT(5),
    V4L2_BUF_TYPE_SLICED_VBI_CAPTURE(6),
    V4L2_BUF_TYPE_SLICED_VBI_OUTPUT(7),
    V4L2_BUF_TYPE_VIDEO_OUTPUT_OVERLAY(8),
    V4L2_BUF_TYPE_VIDEO_CAPTURE_MPLANE(9),
    V4L2_BUF_TYPE_VIDEO_OUTPUT_MPLANE(10),
    V4L2_BUF_TYPE_SDR_CAPTURE(11),
    V4L2_BUF_TYPE_SDR_OUTPUT(12),
    /* Deprecated, do not use */
    V4L2_BUF_TYPE_PRIVATE(0x80);

    public final int value;

    V4L2BufferType(int value) {
        this.value = value;
    }

    public static V4L2BufferType fromValue(int value) {
        for (V4L2BufferType item : values()) {
            if (item.value == value)
                return item;
        }

        return null;
    }
}
