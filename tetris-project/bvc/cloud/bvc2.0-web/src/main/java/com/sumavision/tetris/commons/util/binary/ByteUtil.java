package com.sumavision.tetris.commons.util.binary;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * byte数组 操作
 * lvdeyang 2017年3月28日
 */
public class ByteUtil {

	//byte[]转16进制
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
	
	//inputStream 转换 byte[]
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
	
	//字节长度
	private static String getB(long length){
		return new StringBufferWrapper().append(length).append("B").toString();
	}
	
	//字节长度转换KB
	private static String getKB(long length){
		return new StringBufferWrapper().append(length/1024).append("KB").toString();
	}
	
	//字节长度转换MB
	private static String getMB(long length){
		return new StringBufferWrapper().append(length/(1024*1024)).append("M").toString();
	}
	
	//字节长度转换GB
	private static String getGB(long length){
		return new StringBufferWrapper().append(length/(1024*1024*1024)).append("G").toString();
	}
	
	//获取字节长度
	public static String getSize(byte[] src){
		int length = src.length;
		return getSize(Long.valueOf(length));
	}
	
	/**
	 * 字节大小转换<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月21日 上午11:45:22
	 * @param long length 字节长度 
	 * @return String 转换后的长度
	 */
	public static String getSize(long length){
		if(length < 1024){
			return getB(length);
		}else if(length>=1024 && length<1024*1024){
			return getKB(length);
		}else if(length>=1024*1024 && length<1024*1024*1024){
			return getMB(length);
		}else{
			return getGB(length);
		}
	}
	
}
