package com.pi4j.io.v4l2;

import com.pi4j.io.file.IOCTLBuilder;
import com.pi4j.io.file.LinuxFileException;
import jnr.constants.platform.Errno;

import java.io.IOException;

public class FormatDescription {
    public static int SIZE = 64; //v4l2_fmtdesc
    public static long COMMAND = IOCTLCodes.IOWR('V', 2, SIZE); //VIDIOC_ENUM_FMT

    public final V4L2BufferType type;
    public final int flags;
    public final String description;
    public final PixelFormat pixelFormat;

    public FormatDescription(IOCTLBuilder builder) {
        builder.advance(4); //index

        type = V4L2BufferType.fromValue(builder.dataBuffer.getInt());
        flags = builder.dataBuffer.getInt();
        description = builder.getString(32);
        pixelFormat = PixelFormat.fromValue(builder.dataBuffer.getInt());
    }

    public static FormatDescription ioctl(IOCTLBuilder builder, int index, V4L2BufferType type) throws IOException {
        builder.clear(SIZE);

        builder.dataBuffer.putInt(index);
        builder.dataBuffer.putInt(type.value);

        try {
            builder.ioctl(COMMAND);
        } catch (LinuxFileException e) {
            //this is an expected terminal point
            if(e.getErrno() == Errno.EINVAL)
                return null;
            else
                throw e;
        }

        return new FormatDescription(builder);
    }
}
