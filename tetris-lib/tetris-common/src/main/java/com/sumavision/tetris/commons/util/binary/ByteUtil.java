package com.sumavision.tetris.commons.util.binary;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * byte操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年4月23日 上午10:50:34
 */
public class ByteUtil {

	/**
	 * byte[]转16进制字符串<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月23日 上午10:49:48
	 * @param byte[] src 
	 * @return String 16进制字符串
	 */
	public static String bytesToHexString(byte[] src){      
        StringBuilder stringBuilder = new StringBuilder();      
        if (src == null || src.length <= 0) {      
            return null;      
        }      
        for (int i = 0; i < src.length; i++) {      
            int v = src[i] & 0xFF;      
            String hv = Integer.toHexString(v);      
            if (hv.length() < 2) {      
                stringBuilder.append(0);      
            }      
            stringBuilder.append(hv);      
        }      
        return stringBuilder.toString();      
    } 
	
	/**
	 * 16进制字符串转byte[]<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月23日 上午10:49:09
	 * @param String hexString 16进制字符串
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
	
	/**
	 * char转byte<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月23日 上午10:47:38
	 * @param char c
	 * @return byte
	 */
	private static byte charToByte(char c) {  
	    return (byte) "0123456789ABCDEF".indexOf(c);  
	}  
	
	/**
	 * inputStream 转byte[]<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月23日 上午10:48:25
	 * @param InputStream in 输入流
	 * @return byte[]
	 */
	public static byte[] inputStreamToBytes(InputStream in) throws IOException{
		byte[] in2b;
		ByteArrayOutputStream swapStream = null;
		try{
			swapStream = new ByteArrayOutputStream();  
	        byte[] buff = new byte[100];  
	        int rc = 0;  
	        while ((rc = in.read(buff, 0, 100)) > 0) {  
	            swapStream.write(buff, 0, rc);  
	        }  
	        in2b = swapStream.toByteArray();  
		}finally{
			if(in != null) in.close();
			if(swapStream != null) swapStream.close();
		}
        return in2b;
	}
	
	/**
	 * 字节长度转换B<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月23日 上午10:51:03
	 * @param int length 字节长度
	 * @return String B
	 */
	private static String getB(long length){
		return new StringBufferWrapper().append(length).append("B").toString();
	}
	
	/**
	 * 字节长度转换KB<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月23日 上午10:51:03
	 * @param int length 字节长度
	 * @return String KB
	 */
	private static String getKB(long length){
		return new StringBufferWrapper().append(length/1024l).append("KB").toString();
	}
	
	/**
	 * 字节长度转换MB<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月23日 上午10:51:03
	 * @param int length 字节长度
	 * @return String MB
	 */
	private static String getMB(long length){
		return new StringBufferWrapper().append(length/(1024l*1024l)).append("MB").toString();
	}
	
	/**
	 * 字节长度转换GB<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月23日 上午10:51:03
	 * @param int length 字节长度
	 * @return String GB
	 */
	private static String getGB(long length){
		return new StringBufferWrapper().append(length/(1024l*1024l*1024l)).append("GB").toString();
	}
	
	/**
	 * 字节长度转换TB<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月23日 上午10:51:03
	 * @param int length 字节长度
	 * @return String TB
	 */
	private static String getTB(long length){
		return new StringBufferWrapper().append(length/(1024l*1024l*1024l*1024l)).append("TB").toString();
	}
	
	/**
	 * 字节长度转换PB<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月23日 上午10:51:03
	 * @param int length 字节长度
	 * @return String PB
	 */
	private static String getPB(long length){
		return new StringBufferWrapper().append(length/(1024l*1024l*1024l*1024l*1024l)).append("PB").toString();
	}
	
	/**
	 * 格式化字节长度<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月23日 上午10:51:03
	 * @param byte[] src 字节
	 * @return String 格式化长度
	 */
	public static String getSize(byte[] src){
		long length = src.length;
		return getSize(length);
	}
	
	/**
	 * 格式化字节长度<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月23日 上午10:51:03
	 * @param int length 字节长度
	 * @return String 格式化长度
	 */
	public static String getSize(long length){
		if(length < 1024l){
			return getB(length);
		}else if(length>=1024l && length<1024l*1024l){
			return getKB(length);
		}else if(length>=1024l*1024l && length<1024l*1024l*1024l){
			return getMB(length);
		}else if(length>=1024*1024l*1024l && length<1024l*1024l*1024l*1024l){
			return getGB(length);
		}else if(length>=1024*1024l*1024l*1024l && length<1024l*1024l*1024l*1024l*1024l){
			return getTB(length);
		}else{
			return getPB(length);
		}
	}
	
}
