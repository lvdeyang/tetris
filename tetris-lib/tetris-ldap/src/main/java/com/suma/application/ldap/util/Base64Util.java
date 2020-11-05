package com.suma.application.ldap.util;

import java.io.UnsupportedEncodingException;

import org.springframework.util.Base64Utils;

public class Base64Util {
	
	public static String encode(String str){
		try {
			return Base64Utils.encodeToString(str.getBytes("utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static String decode(String str){
		try {
			return new String(Base64Utils.decodeFromString(str), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args){
		System.out.println(encode("123456"));
		
		System.out.println(decode("MTIzNDU2"));
	}
}
