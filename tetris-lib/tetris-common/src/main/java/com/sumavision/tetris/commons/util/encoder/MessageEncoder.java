package com.sumavision.tetris.commons.util.encoder;

import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import com.sumavision.tetris.commons.util.binary.ByteUtil;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 消息文本编码器<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年3月5日 下午5:35:23
 */
public class MessageEncoder {
	
	@Component
	public static class Md5Encoder{
		
		public String encode(String message) throws Exception{
			return DigestUtils.md5DigestAsHex(message.getBytes());
		}
		
	}

	/**
	 * Sha256编码器<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月5日 下午5:32:14
	 */
	@Component
	public static class Sha256Encoder{
		
		/**
		 * 字符串编码<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2019年3月5日 下午5:32:58
		 * @param String message 待编码字符串
		 * @return String 编码后十六进制
		 */
		public String encode(String message) throws Exception{
			if(message == null) return null;
			MessageDigest messageDigest = null;
			String encodeMessage = "";
			messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(message.getBytes("UTF-8"));
			encodeMessage = ByteUtil.bytesToHexString(messageDigest.digest());
			return encodeMessage;
		}
		
	}
	
	/**
	 * AES对称加密<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月22日 上午9:19:37
	 */
	@Component
	@SuppressWarnings("restriction")
	public static class AES{
		
		private BASE64Decoder decoder = new BASE64Decoder();
		
		private BASE64Encoder encoder = new BASE64Encoder();
		
		/**
		 * 编码<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2019年4月22日 上午9:20:06
		 * @param String encodeRules 公钥
		 * @param String message 待编码的内容
		 * @return String 编码结果
		 */
		public String encode(String encodeRules, String message) throws Exception{
			if(message == null) return null;
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(encodeRules.getBytes());
            keygen.init(128, secureRandom);
            SecretKey original_key = keygen.generateKey();
            byte [] raw = original_key.getEncoded();
            SecretKey key = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte [] byte_encode = message.getBytes("utf-8");
            byte [] byte_AES = cipher.doFinal(byte_encode);
            String AES_encode = new String(this.encoder.encode(byte_AES));
            return AES_encode;     
		}
		
		/**
		 * 解码<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2019年4月22日 上午9:21:45
		 * @param String rules 公钥
		 * @param String message 待解码的内容
		 * @return String 解码结果
		 */
		public String decode(String encodeRules, String message) throws Exception{
			if(message == null) return null;
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(encodeRules.getBytes());
            keygen.init(128, secureRandom);
            SecretKey original_key = keygen.generateKey();
            byte [] raw = original_key.getEncoded();
            SecretKey key = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte [] byte_content = this.decoder.decodeBuffer(message);
            byte [] byte_decode = cipher.doFinal(byte_content);
            String AES_decode = new String(byte_decode,"utf-8");
            return AES_decode;      
		}
		
	}
	
	/**
	 * base64编解码器<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月22日 上午9:19:37
	 */
	@Component
	@SuppressWarnings("restriction")
	public static class Base64{
		
		private BASE64Decoder decoder = new BASE64Decoder();
		
		private BASE64Encoder encoder = new BASE64Encoder();
		
		/**
		 * 编码<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2020年1月8日 上午11:16:33
		 * @param String message 待编码内容
		 * @return String base64编码后内容
		 */
		public String encode(String message){
			return this.encoder.encode(message.getBytes());
		}
		
		/**
		 * 解码<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2020年1月8日 上午11:16:33
		 * @param String message 待解码内容
		 * @return String base64解码后内容
		 */
		public String decode(String message) throws Exception{
			return new String(this.decoder.decodeBuffer(message));
		}
	}
	
}
