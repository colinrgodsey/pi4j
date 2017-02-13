package com.pi4j.io.v4l2;

public class IOCTLCodes {
    public static final int IOC_NONE       = 0x0;
    public static final int IOC_WRITE      = 0x1;
    public static final int IOC_READ       = 0x2;

    public static final int IOC_NRBITS     = 8;
    public static final int IOC_TYPEBITS   = 8;
    public static final int IOC_SIZEBITS   = 14;
    public static final int IOC_DIRBITS    = 2;

    public static final int IOC_NRMASK     = ((1 << IOC_NRBITS) - 1);
    public static final int IOC_TYPEMASK   = ((1 << IOC_TYPEBITS) - 1);
    public static final int IOC_SIZEMASK   = ((1 << IOC_SIZEBITS) - 1);
    public static final int IOC_DIRMASK    = ((1 << IOC_DIRBITS) - 1);
    public static final int IOC_NRSHIFT    = 0;
    public static final int IOC_TYPESHIFT  = IOC_NRSHIFT + IOC_NRBITS;
    public static final int IOC_SIZESHIFT  = IOC_TYPESHIFT + IOC_TYPEBITS;
    public static final int IOC_DIRSHIFT   = IOC_SIZESHIFT + IOC_SIZEBITS;

    public static long IOC(int dir, int type, int nr, int size) {
        return (((dir & IOC_DIRMASK) << IOC_DIRSHIFT) |
                ((type & IOC_TYPEMASK) << IOC_TYPESHIFT) |
                ((nr & IOC_NRMASK) << IOC_NRSHIFT) |
                ((size & IOC_SIZEMASK) << IOC_SIZESHIFT));
    }

    public static long IO(char type, int nr) {
        return IOC(IOC_NONE, type, nr, 0);
    }

    public static long IOR(char type, int nr, int size) {
        return IOC(IOC_READ, type, nr, size);
    }

    public static long IOW(char type, int nr, int size) {
        return IOC(IOC_WRITE, type, nr, size);
    }

    public static long IOWR(char type, int nr, int size) {
        return IOC(IOC_WRITE | IOC_READ, type, nr, size);
    }
}
