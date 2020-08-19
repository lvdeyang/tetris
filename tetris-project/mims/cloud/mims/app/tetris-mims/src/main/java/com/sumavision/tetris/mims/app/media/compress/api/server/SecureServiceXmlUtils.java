package com.sumavision.tetris.mims.app.media.compress.api.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

//import com.sumavision.eb.YingJGBCALLDLL;

public class SecureServiceXmlUtils {
	private static boolean openJNTA1313_SUMA = false;
	private static boolean openJNTA1507_SUMA = false;
	
	private static Object obj = new Object();
	/**
	 * 
	 * @param xmlData 验签用的xml文件 例如EBDB_10415092100000001030101010000000000035942.xml
	 * @param signatureValue 签名 EBDS_EBDB_10415092100000001030101010000000000035942.xml里面的SignatureValue字段值
	 * @param secureType 0表示密码器， 1表示密码机
	 * @return
	 */
	public static String checkSignatureValueJnta(File xmlData, String signatureValue, Integer secureType) {
        if (null == xmlData || !xmlData.isFile()) {
            return "NOK";
        }

        if (signatureValue == null || signatureValue.isEmpty()) {
            return "NOK";
        }
        
        if(secureType == null || secureType < 0 || secureType > 1){
        	return "NOK";
        }

        Boolean result = false;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(xmlData);
            byte[] inData = new byte[inputStream.available()];
            inputStream.read(inData);
                        

            /** 调用密码器库接口获得签名字节数据 */
            
            try {
            	if(secureType == 0){
            		if(!openJNTA1313_SUMA){
            			synchronized (obj) {
            				if(!openJNTA1313_SUMA){
            					//YingJGBCALLDLL.closeDevice();
    		            		//YingJGBCALLDLL.openDevice(0);
    		            		openJNTA1313_SUMA = true;    		            		
            				}
						}
            			
            		}		            		
            	}else{
            		if(!openJNTA1507_SUMA){
            			synchronized (obj) {
            				if(!openJNTA1507_SUMA){
    	            			//com.sumavision.eb.YingJGBCALLDLL.closeDevice();
    	            			//com.sumavision.eb.YingJGBCALLDLL.openDevice(1);
    	            			openJNTA1507_SUMA = true;    	            			
    	            		}
            			}
            		}
            		
            	}
            	
			} catch (Exception e) {
			}
            /** 调用密码器库函数验签 */
            //result = YingJGBCALLDLL.platformVerifySignature(1, inData, signatureValue);          

        } catch (Exception e) {
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return result ? "OK" : "NOK";
    }
}
