package com.pi4j.io.v4l2;

import com.pi4j.io.file.IOCTLBuilder;
import com.pi4j.io.file.LinuxFile;
import com.pi4j.io.file.LinuxFileException;
import jnr.constants.platform.Errno;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class V4L2Device {
    public static final int DATA_BUFFER_SIZE = 1024;
    public static final int OFFSET_BUFFER_SIZE = 256;

    public static final int SIZE_v4l2_fmtdesc = 64;
    public static final int SIZE_v4l2_capability = 104;

    public static final int V4L2_BUF_TYPE_VIDEO_CAPTURE = 1;

    public static final int VIDIOC_ENUM_FMT(int size) {
        return 0;
    }

    public final LinuxFile device;
    public final IOCTLBuilder builder;

    public final ByteBuffer dataBuffer;
    public final IntBuffer offsetBuffer;

    V4L2Device(LinuxFile device) {
        this.device = device;

        dataBuffer = ByteBuffer.allocateDirect(DATA_BUFFER_SIZE).order(ByteOrder.nativeOrder());
        offsetBuffer = ByteBuffer.allocateDirect(OFFSET_BUFFER_SIZE).order(ByteOrder.nativeOrder()).asIntBuffer();

        this.builder = new IOCTLBuilder(dataBuffer, offsetBuffer);
    }

    enum v4l2_buf_type {
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

        v4l2_buf_type(int value) {
            this.value = value;
        }

        public static v4l2_buf_type fromValue(int value) {
            for(v4l2_buf_type item : values()) {
                if(item.value == value)
                    return item;
            }

            return null;
        }
    };
    
    enum PixelFormat {
        /*      Pixel format         FOURCC                          depth  Description  */

        /* RGB formats */
        V4L2_PIX_FMT_RGB332  ('R', 'G', 'B', '1'), /*  8  RGB-3-3-2     */
        V4L2_PIX_FMT_RGB444  ('R', '4', '4', '4'), /* 16  xxxxrrrr ggggbbbb */
        V4L2_PIX_FMT_ARGB444 ('A', 'R', '1', '2'), /* 16  aaaarrrr ggggbbbb */
        V4L2_PIX_FMT_XRGB444 ('X', 'R', '1', '2'), /* 16  xxxxrrrr ggggbbbb */
        V4L2_PIX_FMT_RGB555  ('R', 'G', 'B', 'O'), /* 16  RGB-5-5-5     */
        V4L2_PIX_FMT_ARGB555 ('A', 'R', '1', '5'), /* 16  ARGB-1-5-5-5  */
        V4L2_PIX_FMT_XRGB555 ('X', 'R', '1', '5'), /* 16  XRGB-1-5-5-5  */
        V4L2_PIX_FMT_RGB565  ('R', 'G', 'B', 'P'), /* 16  RGB-5-6-5     */
        V4L2_PIX_FMT_RGB555X ('R', 'G', 'B', 'Q'), /* 16  RGB-5-5-5 BE  */
        V4L2_PIX_FMT_ARGB555X ('A', 'R', '1', '5', true), /* 16  ARGB-5-5-5 BE */
        V4L2_PIX_FMT_XRGB555X ('X', 'R', '1', '5', true), /* 16  XRGB-5-5-5 BE */
        V4L2_PIX_FMT_RGB565X ('R', 'G', 'B', 'R'), /* 16  RGB-5-6-5 BE  */
        V4L2_PIX_FMT_BGR666  ('B', 'G', 'R', 'H'), /* 18  BGR-6-6-6	  */
        V4L2_PIX_FMT_BGR24   ('B', 'G', 'R', '3'), /* 24  BGR-8-8-8     */
        V4L2_PIX_FMT_RGB24   ('R', 'G', 'B', '3'), /* 24  RGB-8-8-8     */
        V4L2_PIX_FMT_BGR32   ('B', 'G', 'R', '4'), /* 32  BGR-8-8-8-8   */
        V4L2_PIX_FMT_ABGR32  ('A', 'R', '2', '4'), /* 32  BGRA-8-8-8-8  */
        V4L2_PIX_FMT_XBGR32  ('X', 'R', '2', '4'), /* 32  BGRX-8-8-8-8  */
        V4L2_PIX_FMT_RGB32   ('R', 'G', 'B', '4'), /* 32  RGB-8-8-8-8   */
        V4L2_PIX_FMT_ARGB32  ('B', 'A', '2', '4'), /* 32  ARGB-8-8-8-8  */
        V4L2_PIX_FMT_XRGB32  ('B', 'X', '2', '4'), /* 32  XRGB-8-8-8-8  */

        /* Grey formats */
        V4L2_PIX_FMT_GREY    ('G', 'R', 'E', 'Y'), /*  8  Greyscale     */
        V4L2_PIX_FMT_Y4      ('Y', '0', '4', ' '), /*  4  Greyscale     */
        V4L2_PIX_FMT_Y6      ('Y', '0', '6', ' '), /*  6  Greyscale     */
        V4L2_PIX_FMT_Y10     ('Y', '1', '0', ' '), /* 10  Greyscale     */
        V4L2_PIX_FMT_Y12     ('Y', '1', '2', ' '), /* 12  Greyscale     */
        V4L2_PIX_FMT_Y16     ('Y', '1', '6', ' '), /* 16  Greyscale     */
        V4L2_PIX_FMT_Y16_BE  ('Y', '1', '6', ' ', true), /* 16  Greyscale BE  */

        /* Grey bit-packed formats */
        V4L2_PIX_FMT_Y10BPACK    ('Y', '1', '0', 'B'), /* 10  Greyscale bit-packed */

        /* Palette formats */
        V4L2_PIX_FMT_PAL8    ('P', 'A', 'L', '8'), /*  8  8-bit palette */

        /* Chrominance formats */
        V4L2_PIX_FMT_UV8     ('U', 'V', '8', ' '), /*  8  UV 4:4 */

        /* Luminance+Chrominance formats */
        V4L2_PIX_FMT_YUYV    ('Y', 'U', 'Y', 'V'), /* 16  YUV 4:2:2     */
        V4L2_PIX_FMT_YYUV    ('Y', 'Y', 'U', 'V'), /* 16  YUV 4:2:2     */
        V4L2_PIX_FMT_YVYU    ('Y', 'V', 'Y', 'U'), /* 16 YVU 4:2:2 */
        V4L2_PIX_FMT_UYVY    ('U', 'Y', 'V', 'Y'), /* 16  YUV 4:2:2     */
        V4L2_PIX_FMT_VYUY    ('V', 'Y', 'U', 'Y'), /* 16  YUV 4:2:2     */
        V4L2_PIX_FMT_Y41P    ('Y', '4', '1', 'P'), /* 12  YUV 4:1:1     */
        V4L2_PIX_FMT_YUV444  ('Y', '4', '4', '4'), /* 16  xxxxyyyy uuuuvvvv */
        V4L2_PIX_FMT_YUV555  ('Y', 'U', 'V', 'O'), /* 16  YUV-5-5-5     */
        V4L2_PIX_FMT_YUV565  ('Y', 'U', 'V', 'P'), /* 16  YUV-5-6-5     */
        V4L2_PIX_FMT_YUV32   ('Y', 'U', 'V', '4'), /* 32  YUV-8-8-8-8   */
        V4L2_PIX_FMT_HI240   ('H', 'I', '2', '4'), /*  8  8-bit color   */
        V4L2_PIX_FMT_HM12    ('H', 'M', '1', '2'), /*  8  YUV 4:2:0 16x16 macroblocks */
        V4L2_PIX_FMT_M420    ('M', '4', '2', '0'), /* 12  YUV 4:2:0 2 lines y, 1 line uv interleaved */

        /* two planes -- one Y, one Cr + Cb interleaved  */
        V4L2_PIX_FMT_NV12    ('N', 'V', '1', '2'), /* 12  Y/CbCr 4:2:0  */
        V4L2_PIX_FMT_NV21    ('N', 'V', '2', '1'), /* 12  Y/CrCb 4:2:0  */
        V4L2_PIX_FMT_NV16    ('N', 'V', '1', '6'), /* 16  Y/CbCr 4:2:2  */
        V4L2_PIX_FMT_NV61    ('N', 'V', '6', '1'), /* 16  Y/CrCb 4:2:2  */
        V4L2_PIX_FMT_NV24    ('N', 'V', '2', '4'), /* 24  Y/CbCr 4:4:4  */
        V4L2_PIX_FMT_NV42    ('N', 'V', '4', '2'), /* 24  Y/CrCb 4:4:4  */

        /* two non contiguous planes - one Y, one Cr + Cb interleaved  */
        V4L2_PIX_FMT_NV12M   ('N', 'M', '1', '2'), /* 12  Y/CbCr 4:2:0  */
        V4L2_PIX_FMT_NV21M   ('N', 'M', '2', '1'), /* 21  Y/CrCb 4:2:0  */
        V4L2_PIX_FMT_NV16M   ('N', 'M', '1', '6'), /* 16  Y/CbCr 4:2:2  */
        V4L2_PIX_FMT_NV61M   ('N', 'M', '6', '1'), /* 16  Y/CrCb 4:2:2  */
        V4L2_PIX_FMT_NV12MT  ('T', 'M', '1', '2'), /* 12  Y/CbCr 4:2:0 64x32 macroblocks */
        V4L2_PIX_FMT_NV12MT_16X16 ('V', 'M', '1', '2'), /* 12  Y/CbCr 4:2:0 16x16 macroblocks */

        /* three planes - Y Cb, Cr */
        V4L2_PIX_FMT_YUV410  ('Y', 'U', 'V', '9'), /*  9  YUV 4:1:0     */
        V4L2_PIX_FMT_YVU410  ('Y', 'V', 'U', '9'), /*  9  YVU 4:1:0     */
        V4L2_PIX_FMT_YUV411P ('4', '1', '1', 'P'), /* 12  YVU411 planar */
        V4L2_PIX_FMT_YUV420  ('Y', 'U', '1', '2'), /* 12  YUV 4:2:0     */
        V4L2_PIX_FMT_YVU420  ('Y', 'V', '1', '2'), /* 12  YVU 4:2:0     */
        V4L2_PIX_FMT_YUV422P ('4', '2', '2', 'P'), /* 16  YVU422 planar */

        /* three non contiguous planes - Y, Cb, Cr */
        V4L2_PIX_FMT_YUV420M ('Y', 'M', '1', '2'), /* 12  YUV420 planar */
        V4L2_PIX_FMT_YVU420M ('Y', 'M', '2', '1'), /* 12  YVU420 planar */
        V4L2_PIX_FMT_YUV422M ('Y', 'M', '1', '6'), /* 16  YUV422 planar */
        V4L2_PIX_FMT_YVU422M ('Y', 'M', '6', '1'), /* 16  YVU422 planar */
        V4L2_PIX_FMT_YUV444M ('Y', 'M', '2', '4'), /* 24  YUV444 planar */
        V4L2_PIX_FMT_YVU444M ('Y', 'M', '4', '2'), /* 24  YVU444 planar */

        /* Bayer formats - see http://www.siliconimaging.com/RGB%20Bayer.htm */
        V4L2_PIX_FMT_SBGGR8  ('B', 'A', '8', '1'), /*  8  BGBG.. GRGR.. */
        V4L2_PIX_FMT_SGBRG8  ('G', 'B', 'R', 'G'), /*  8  GBGB.. RGRG.. */
        V4L2_PIX_FMT_SGRBG8  ('G', 'R', 'B', 'G'), /*  8  GRGR.. BGBG.. */
        V4L2_PIX_FMT_SRGGB8  ('R', 'G', 'G', 'B'), /*  8  RGRG.. GBGB.. */
        V4L2_PIX_FMT_SBGGR10 ('B', 'G', '1', '0'), /* 10  BGBG.. GRGR.. */
        V4L2_PIX_FMT_SGBRG10 ('G', 'B', '1', '0'), /* 10  GBGB.. RGRG.. */
        V4L2_PIX_FMT_SGRBG10 ('B', 'A', '1', '0'), /* 10  GRGR.. BGBG.. */
        V4L2_PIX_FMT_SRGGB10 ('R', 'G', '1', '0'), /* 10  RGRG.. GBGB.. */
            /* 10bit raw bayer packed, 5 bytes for every 4 pixels */
        V4L2_PIX_FMT_SBGGR10P ('p', 'B', 'A', 'A'),
        V4L2_PIX_FMT_SGBRG10P ('p', 'G', 'A', 'A'),
        V4L2_PIX_FMT_SGRBG10P ('p', 'g', 'A', 'A'),
        V4L2_PIX_FMT_SRGGB10P ('p', 'R', 'A', 'A'),
            /* 10bit raw bayer a-law compressed to 8 bits */
        V4L2_PIX_FMT_SBGGR10ALAW8 ('a', 'B', 'A', '8'),
        V4L2_PIX_FMT_SGBRG10ALAW8 ('a', 'G', 'A', '8'),
        V4L2_PIX_FMT_SGRBG10ALAW8 ('a', 'g', 'A', '8'),
        V4L2_PIX_FMT_SRGGB10ALAW8 ('a', 'R', 'A', '8'),
            /* 10bit raw bayer DPCM compressed to 8 bits */
        V4L2_PIX_FMT_SBGGR10DPCM8 ('b', 'B', 'A', '8'),
        V4L2_PIX_FMT_SGBRG10DPCM8 ('b', 'G', 'A', '8'),
        V4L2_PIX_FMT_SGRBG10DPCM8 ('B', 'D', '1', '0'),
        V4L2_PIX_FMT_SRGGB10DPCM8 ('b', 'R', 'A', '8'),
        V4L2_PIX_FMT_SBGGR12 ('B', 'G', '1', '2'), /* 12  BGBG.. GRGR.. */
        V4L2_PIX_FMT_SGBRG12 ('G', 'B', '1', '2'), /* 12  GBGB.. RGRG.. */
        V4L2_PIX_FMT_SGRBG12 ('B', 'A', '1', '2'), /* 12  GRGR.. BGBG.. */
        V4L2_PIX_FMT_SRGGB12 ('R', 'G', '1', '2'), /* 12  RGRG.. GBGB.. */
        V4L2_PIX_FMT_SBGGR16 ('B', 'Y', 'R', '2'), /* 16  BGBG.. GRGR.. */
        V4L2_PIX_FMT_SGBRG16 ('G', 'B', '1', '6'), /* 16  GBGB.. RGRG.. */
        V4L2_PIX_FMT_SGRBG16 ('G', 'R', '1', '6'), /* 16  GRGR.. BGBG.. */
        V4L2_PIX_FMT_SRGGB16 ('R', 'G', '1', '6'), /* 16  RGRG.. GBGB.. */

        /* HSV formats */
        V4L2_PIX_FMT_HSV24 ('H', 'S', 'V', '3'),
        V4L2_PIX_FMT_HSV32 ('H', 'S', 'V', '4'),

        /* compressed formats */
        V4L2_PIX_FMT_MJPEG    ('M', 'J', 'P', 'G'), /* Motion-JPEG   */
        V4L2_PIX_FMT_JPEG     ('J', 'P', 'E', 'G'), /* JFIF JPEG     */
        V4L2_PIX_FMT_DV       ('d', 'v', 's', 'd'), /* 1394          */
        V4L2_PIX_FMT_MPEG     ('M', 'P', 'E', 'G'), /* MPEG-1/2/4 Multiplexed */
        V4L2_PIX_FMT_H264     ('H', '2', '6', '4'), /* H264 with start codes */
        V4L2_PIX_FMT_H264_NO_SC ('A', 'V', 'C', '1'), /* H264 without start codes */
        V4L2_PIX_FMT_H264_MVC ('M', '2', '6', '4'), /* H264 MVC */
        V4L2_PIX_FMT_H263     ('H', '2', '6', '3'), /* H263          */
        V4L2_PIX_FMT_MPEG1    ('M', 'P', 'G', '1'), /* MPEG-1 ES     */
        V4L2_PIX_FMT_MPEG2    ('M', 'P', 'G', '2'), /* MPEG-2 ES     */
        V4L2_PIX_FMT_MPEG4    ('M', 'P', 'G', '4'), /* MPEG-4 part 2 ES */
        V4L2_PIX_FMT_XVID     ('X', 'V', 'I', 'D'), /* Xvid           */
        V4L2_PIX_FMT_VC1_ANNEX_G ('V', 'C', '1', 'G'), /* SMPTE 421M Annex G compliant stream */
        V4L2_PIX_FMT_VC1_ANNEX_L ('V', 'C', '1', 'L'), /* SMPTE 421M Annex L compliant stream */
        V4L2_PIX_FMT_VP8      ('V', 'P', '8', '0'), /* VP8 */
        V4L2_PIX_FMT_VP9      ('V', 'P', '9', '0'), /* VP9 */

        /*  Vendor-specific formats   */
        V4L2_PIX_FMT_CPIA1    ('C', 'P', 'I', 'A'), /* cpia1 YUV */
        V4L2_PIX_FMT_WNVA     ('W', 'N', 'V', 'A'), /* Winnov hw compress */
        V4L2_PIX_FMT_SN9C10X  ('S', '9', '1', '0'), /* SN9C10x compression */
        V4L2_PIX_FMT_SN9C20X_I420 ('S', '9', '2', '0'), /* SN9C20x YUV 4:2:0 */
        V4L2_PIX_FMT_PWC1     ('P', 'W', 'C', '1'), /* pwc older webcam */
        V4L2_PIX_FMT_PWC2     ('P', 'W', 'C', '2'), /* pwc newer webcam */
        V4L2_PIX_FMT_ET61X251 ('E', '6', '2', '5'), /* ET61X251 compression */
        V4L2_PIX_FMT_SPCA501  ('S', '5', '0', '1'), /* YUYV per line */
        V4L2_PIX_FMT_SPCA505  ('S', '5', '0', '5'), /* YYUV per line */
        V4L2_PIX_FMT_SPCA508  ('S', '5', '0', '8'), /* YUVY per line */
        V4L2_PIX_FMT_SPCA561  ('S', '5', '6', '1'), /* compressed GBRG bayer */
        V4L2_PIX_FMT_PAC207   ('P', '2', '0', '7'), /* compressed BGGR bayer */
        V4L2_PIX_FMT_MR97310A ('M', '3', '1', '0'), /* compressed BGGR bayer */
        V4L2_PIX_FMT_JL2005BCD ('J', 'L', '2', '0'), /* compressed RGGB bayer */
        V4L2_PIX_FMT_SN9C2028 ('S', 'O', 'N', 'X'), /* compressed GBRG bayer */
        V4L2_PIX_FMT_SQ905C   ('9', '0', '5', 'C'), /* compressed RGGB bayer */
        V4L2_PIX_FMT_PJPG     ('P', 'J', 'P', 'G'), /* Pixart 73xx JPEG */
        V4L2_PIX_FMT_OV511    ('O', '5', '1', '1'), /* ov511 JPEG */
        V4L2_PIX_FMT_OV518    ('O', '5', '1', '8'), /* ov518 JPEG */
        V4L2_PIX_FMT_STV0680  ('S', '6', '8', '0'), /* stv0680 bayer */
        V4L2_PIX_FMT_TM6000   ('T', 'M', '6', '0'), /* tm5600/tm60x0 */
        V4L2_PIX_FMT_CIT_YYVYUY ('C', 'I', 'T', 'V'), /* one line of Y then 1 line of VYUY */
        V4L2_PIX_FMT_KONICA420  ('K', 'O', 'N', 'I'), /* YUV420 planar in blocks of 256 pixels */
        V4L2_PIX_FMT_JPGL	('J', 'P', 'G', 'L'), /* JPEG-Lite */
        V4L2_PIX_FMT_SE401      ('S', '4', '0', '1'), /* se401 janggu compressed rgb */
        V4L2_PIX_FMT_S5C_UYVY_JPG ('S', '5', 'C', 'I'), /* S5C73M3 interleaved UYVY/JPEG */
        V4L2_PIX_FMT_Y8I      ('Y', '8', 'I', ' '), /* Greyscale 8-bit L/R interleaved */
        V4L2_PIX_FMT_Y12I     ('Y', '1', '2', 'I'), /* Greyscale 12-bit L/R interleaved */
        V4L2_PIX_FMT_Z16      ('Z', '1', '6', ' '), /* Depth data 16-bit */
        V4L2_PIX_FMT_MT21C    ('M', 'T', '2', '1'), /* Mediatek compressed block mode  */

        /* SDR formats - used only for Software Defined Radio devices */
        V4L2_SDR_FMT_CU8          ('C', 'U', '0', '8'), /* IQ u8 */
        V4L2_SDR_FMT_CU16LE       ('C', 'U', '1', '6'), /* IQ u16le */
        V4L2_SDR_FMT_CS8          ('C', 'S', '0', '8'), /* complex s8 */
        V4L2_SDR_FMT_CS14LE       ('C', 'S', '1', '4'), /* complex s14le */
        V4L2_SDR_FMT_RU12LE       ('R', 'U', '1', '2'), /* real u12le */

        /* Touch formats - used for Touch devices */
        V4L2_TCH_FMT_DELTA_TD16	('T', 'D', '1', '6'), /* 16-bit signed deltas */
        V4L2_TCH_FMT_DELTA_TD08	('T', 'D', '0', '8'), /* 8-bit signed deltas */
        V4L2_TCH_FMT_TU16	('T', 'U', '1', '6'), /* 16-bit unsigned touch data */
        V4L2_TCH_FMT_TU08	('T', 'U', '0', '8'); /* 8-bit unsigned touch data */

        public final int intCode;
        public final String stringCode;

        PixelFormat(char a, char b, char c, char d) {
            this(a, b, c, d, false);
        }

        PixelFormat(char a, char b, char c, char d, boolean be) {
            long code = (a) | (b << 8) | (c << 16) | (d << 24);
            byte[] stringBuf = {(byte)a, (byte)b, (byte)c, (byte)d};

            if(be)
                code = code | (1 << 31);

            intCode = (int)code;
            stringCode = new String(stringBuf);
        }

        public static PixelFormat fromValue(int code) {
            for(PixelFormat item : values()) {
                if(item.intCode == code)
                    return item;
            }

            return null;
        }
    }

    class FormatDescription {
        public final v4l2_buf_type type;
        public final int flags;
        public final String description;
        public final PixelFormat pixelFormat;

        FormatDescription(IOCTLBuilder builder) {
            builder.advance(4); //index

            type = v4l2_buf_type.fromValue(builder.dataBuffer.getInt());
            flags = builder.dataBuffer.getInt();
            description = builder.getString(32);
            pixelFormat = PixelFormat.fromValue(builder.dataBuffer.getInt());
        }
    }

    public String getFormat(int index) throws IOException {
        builder.clear(SIZE_v4l2_fmtdesc);
        dataBuffer.putInt(index);
        dataBuffer.putInt(V4L2_BUF_TYPE_VIDEO_CAPTURE);

        try {
            ioctl(VIDIOC_ENUM_FMT(SIZE_v4l2_fmtdesc));
        } catch (LinuxFileException e) {
            if(e.getErrno() == Errno.EINVAL)
                return null;
            else
                throw e;
        }

        builder.advance(12);

        String desc = builder.getString(32);
        String code = builder.getString(4);

        builder.advance(-4);

        int intCode = dataBuffer.getInt();

        return desc + " " + code;
    }

    private void ioctl(long command, int entryOffset) throws IOException {
        dataBuffer.position(entryOffset);
        offsetBuffer.flip();

        device.ioctl(command, dataBuffer, offsetBuffer);
    }

    private void ioctl(long command) throws IOException {
        ioctl(command, 0);
    }

}
