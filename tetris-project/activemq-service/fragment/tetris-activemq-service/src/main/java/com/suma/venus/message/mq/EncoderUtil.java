package com.suma.venus.message.mq;

import com.google.common.base.Strings;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class EncoderUtil {
	/**
	 * 定义加密方式
	 */
	private final static String KEY_DES = "DES";
	private final static String KEY_AES = "AES"; // 测试
	
	public final static String KEY = "+Oye1q097xw=";

	/**
	 * 全局数组
	 */
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * 初始化密钥
	 * 
	 * @return
	 */
	public static String init() {
		return init(null);
	}

	/**
	 * 初始化密钥
	 * 
	 * @param seed
	 *            初始化参数
	 * @return
	 */
	public static String init(String seed) {
		SecureRandom secure = null;
		String str = "";
		try {
			if (null != secure) {
				// 带参数的初始化
				secure = new SecureRandom(decryptBase64(seed));
			} else {
				// 不带参数的初始化
				secure = new SecureRandom();
			}

			KeyGenerator generator = KeyGenerator.getInstance(KEY_DES);
			generator.init(secure);

			SecretKey key = generator.generateKey();
			str = encryptBase64(key.getEncoded());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 转换密钥
	 * 
	 * @param key
	 *            密钥的字节数组
	 * @return
	 */
	private static Key byteToKey(byte[] key) {
		SecretKey secretKey = null;
		try {
			DESKeySpec dks = new DESKeySpec(key);
			SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_DES);
			secretKey = factory.generateSecret(dks);

			// 当使用其他对称加密算法时，如AES、Blowfish等算法时，用下述代码替换上述三行代码
			// secretKey = new SecretKeySpec(key, KEY_DES);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return secretKey;
	}

	/**
	 * DES 解密
	 * 
	 * @param data
	 *            需要解密的字符串
	 * @param key
	 *            密钥
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String decryptDES(String data, String key)  {
		// 验证传入的字符串
		if (Strings.isNullOrEmpty(data)) {
			return "";
		}
		// 调用解密方法完成解密
		byte[] bytes = decryptDES(hexString2Bytes(data), key);
		// 将得到的字节数组变成字符串返回
		try {
			return new String(bytes,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * DES 解密
	 * 
	 * @param data
	 *            需要解密的字节数组
	 * @param key
	 *            密钥
	 * @return
	 */
	public static byte[] decryptDES(byte[] data, String key) {
		byte[] bytes = null;
		try {
			Key k = byteToKey(decryptBase64(key));
			Cipher cipher = Cipher.getInstance(KEY_DES);
			cipher.init(Cipher.DECRYPT_MODE, k);
			bytes = cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bytes;
	}

	/**
	 * DES 加密
	 * 
	 * @param data
	 *            需要加密的字符串
	 * @param key
	 *            密钥
	 * @return
	 */
	public static String encryptDES(String data, String key) {
		// 验证传入的字符串
		if (Strings.isNullOrEmpty(data)) {
			return "";
		}
		try{
			// 调用加密方法完成加密
			byte[] bytes = encryptDES(data.getBytes("utf-8"), key);
			// 将得到的字节数组变成字符串返回
			return byteArrayToHexString(bytes);
		}catch(Exception e){
			
		}
		return "";
	}

	/**
	 * DES 加密
	 * 
	 * @param data
	 *            需要加密的字节数组
	 * @param key
	 *            密钥
	 * @return
	 */
	public static byte[] encryptDES(byte[] data, String key) {
		byte[] bytes = null;
		try {
			Key k = byteToKey(decryptBase64(key));
			Cipher cipher = Cipher.getInstance(KEY_DES);
			cipher.init(Cipher.ENCRYPT_MODE, k);
			bytes = cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bytes;
	}

	/**
	 * BASE64 解密
	 * 
	 * @param key
	 *            需要解密的字符串
	 * @return 字节数组
	 * @throws Exception
	 */
	public static byte[] decryptBase64(String key) throws Exception {
		return (new BASE64Decoder()).decodeBuffer(key);
	}

	/**
	 * BASE64 加密
	 * 
	 * @param key
	 *            需要加密的字节数组
	 * @return 字符串
	 * @throws Exception
	 */
	public static String encryptBase64(byte[] key) throws Exception {
		return (new BASE64Encoder()).encodeBuffer(key);
	}

	/**
	 * 将一个字节转化成十六进制形式的字符串
	 * 
	 * @param b
	 *            字节数组
	 * @return 字符串
	 */
	private static String byteToHexString(byte b) {
		int ret = b;
		// System.out.println("ret = " + ret);
		if (ret < 0) {
			ret += 256;
		}
		int m = ret / 16;
		int n = ret % 16;
		return hexDigits[m] + hexDigits[n];
	}

	/**
	 * 转换字节数组为十六进制字符串
	 * 
	 * @param bytes
	 *            字节数组
	 * @return 十六进制字符串
	 */
	private static String byteArrayToHexString(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(byteToHexString(bytes[i]));
		}
		return sb.toString();
	}

	/**
	 * 转换十六进制字符串为字节数组
	 * 
	 * @param hexstr
	 *            十六进制字符串
	 * @return
	 */
	public static byte[] hexString2Bytes(String hexstr) {
		byte[] b = new byte[hexstr.length() / 2];
		int j = 0;
		for (int i = 0; i < b.length; i++) {
			char c0 = hexstr.charAt(j++);
			char c1 = hexstr.charAt(j++);
			b[i] = (byte) ((parse(c0) << 4) | parse(c1));
		}
		return b;
	}

	/**
	 * 转换字符类型数据为整型数据
	 * 
	 * @param c
	 *            字符
	 * @return
	 */
	private static int parse(char c) {
		if (c >= 'a')
			return (c - 'a' + 10) & 0x0f;
		if (c >= 'A')
			return (c - 'A' + 10) & 0x0f;
		return (c - '0') & 0x0f;
	}

	/**
	 * 测试方法
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
//		String key = EncoderUtil.init();
//		System.out.println("DES密钥:\n" + key);
      try{
    	  String word = "123为什么中文有问题？！@##&*（）——*……&……";

//  		String encWord = EncoderUtil.encryptDES(word, KEY);
  		String encWord = "8f181187f1a83912d9a7ff1a775ab95922d5e4eae11e4b3c6073dad1417b8870f77367d2fe091b37921eecd05bb616812f180271120ededc";

  		System.out.println(word + "\n加密后：\n" + encWord);
  		System.out.println(word + "\n解密后：\n" + EncoderUtil.decryptDES(encWord, KEY));
      }catch(Exception e){
    	  
      }
		
	}
}
