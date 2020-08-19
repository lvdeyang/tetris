package com.sumavision.signal.bvc.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;

public abstract class HttpCallBack<T1, T2, T3, T4, T5, T6, T7> implements FutureCallback<Object>{

	private T1 param1;
	
	private T2 param2;
	
	private T3 param3;
	
	private T4 param4;
	
	private T5 param5;
	
	private T6 param6;
	
	private T7 param7;
	
	public HttpCallBack(T1 param1){
		this.param1 = param1;
	}
	
	public HttpCallBack(T1 param1, T2 param2){
		this.param1 = param1;
		this.param2 = param2;
	}
	
	public HttpCallBack(T1 param1, T2 param2, T3 param3){
		this.param1 = param1;
		this.param2 = param2;
		this.param3 = param3;
	}
	
	public HttpCallBack(T1 param1, T2 param2, T3 param3, T4 param4){
		this.param1 = param1;
		this.param2 = param2;
		this.param3 = param3;
		this.param4 = param4;
	}
	
	public HttpCallBack(T1 param1, T2 param2, T3 param3, T4 param4, T5 param5){
		this.param1 = param1;
		this.param2 = param2;
		this.param3 = param3;
		this.param4 = param4;
		this.param5 = param5;
	}
	
	public HttpCallBack(T1 param1, T2 param2, T3 param3, T4 param4, T5 param5, T6 param6){
		this.param1 = param1;
		this.param2 = param2;
		this.param3 = param3;
		this.param4 = param4;
		this.param5 = param5;
		this.param6 = param6;
	}
	
	public HttpCallBack(T1 param1, T2 param2, T3 param3, T4 param4, T5 param5, T6 param6, T7 param7){
		this.param1 = param1;
		this.param2 = param2;
		this.param3 = param3;
		this.param4 = param4;
		this.param5 = param5;
		this.param6 = param6;
		this.param7 = param7;
	}

	public T1 getParam1() {
		return param1;
	}

	public T2 getParam2() {
		return param2;
	}

	public T3 getParam3() {
		return param3;
	}
	
	public T4 getParam4() {
		return param4;
	}

	public T5 getParam5() {
		return param5;
	}

	public T6 getParam6() {
		return param6;
	}
	
	public T7 getParam7() {
		return param7;
	}

	protected String parseResponse(BasicHttpResponse response) throws Exception{
		
		BufferedReader bReader = null;
		try{
			HttpEntity entity = response.getEntity();
	 	    InputStream in = entity.getContent();
	 	    StringBuilder sBuilder = new StringBuilder();
	 	    String line = "";
	 	    bReader = new BufferedReader(new InputStreamReader(in, "utf-8"));
	 	    while ((line=bReader.readLine()) != null) {
	 	    	sBuilder.append(line);
	 		}

	 	    EntityUtils.consume(entity);
	 	    
	 	    return sBuilder.toString();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(bReader != null){
				try {
					bReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		return null;
	}
	
	public static void main(String[] args) throws Exception{
		
		File file = new File("d:/123.txt");
		BufferedInputStream bis = null;
		
		try{
			
			FileInputStream inputStream = new FileInputStream(file);
			bis = new BufferedInputStream(inputStream);
			
			byte[] b = new byte[100];
			int len = 0;
			while((len = bis.read(b, 0, 1)) != -1){
				System.out.println(len);
			}
			
			
		}catch(IOException e){
			e.printStackTrace();
		}finally {
			try {
				bis.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
	}
	
}
