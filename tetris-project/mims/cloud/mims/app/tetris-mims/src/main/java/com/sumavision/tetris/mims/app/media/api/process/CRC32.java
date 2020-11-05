package com.sumavision.tetris.mims.app.media.api.process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 西研所注入媒资时需要的字段(copy by lzp)
 * <pre>
 * CRC32计算
 * </pre>
 *
 * @author	ManerFan 2015年8月4日
 */
public class CRC32 {
	/**
     * CRC32 = X32 + X26 + X23 + X22 + X16 + X12 + X11 + X10 + X8 + X7 + X5 + X4 + X2 + X1 + X0 
     */

    private static final int CN = 0x04C11DB7;

    private static int[] ptiTable = new int[256];

    static {
        build(CN);
    }

    private static void build(int cn) {
        int nData = 0;
        int nAccum = 0;

        for (int i = 0; i < 256; i++) {
            nData = i << 24;
            nAccum = 0;
            for (int j = 0; j < 8; j++) {
                if (0 != ((nData ^ nAccum) & 0x80000000)) {
                    nAccum = (nAccum << 1) ^ cn;
                } else {
                    nAccum <<= 1;
                }
                nData <<= 1;
            }
            ptiTable[i] = nAccum;
        }
    }

    public static int calculate(byte[] datas) {
        int crc32 = 0xFFFFFFFF;

        if (null == datas || datas.length < 1) {
            return crc32;
        }

        for (byte data : datas) {
            crc32 = (crc32 << 8) ^ ptiTable[(crc32 >>> 24) ^ (data & 0xFF)];
        }

        return crc32;
    }

    /**
     * 获取文件的crc（主要是大文件）
     * @param file
     * @return
     */
	public static int calculate(File file) {
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			byte[] buffer = new byte[8192];
	        int crc32 = 0xFFFFFFFF;
			int iTimes = fileInputStream.available() / 8192;
			int iRemainder = fileInputStream.available() % 8192;
			for (int i=0;i<iTimes;i++){
				fileInputStream.read(buffer, 0, buffer.length);
		        for (byte data : buffer) {
		            crc32 = (crc32 << 8) ^ ptiTable[(crc32 >>> 24) ^ (data & 0xFF)];
		        }
			}
			if (iRemainder != 0){
				byte[] byt = new byte[iRemainder];
				fileInputStream.read(byt, 0, iRemainder);
		        for (byte data : byt) {
		            crc32 = (crc32 << 8) ^ ptiTable[(crc32 >>> 24) ^ (data & 0xFF)];
		        }
			}
			return crc32;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		} finally {
			try {
				if (fileInputStream != null)
					fileInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
}
