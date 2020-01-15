package com.sumavision.bvc.communication.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.message.BasicHttpResponse;

public abstract class HttpCallBack<T1, T2, T3> implements FutureCallback<Object>{

	private T1 param1;
	
	private T2 param2;
	
	private T3 param3;
	
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

	public T1 getParam1() {
		return param1;
	}

	public T2 getParam2() {
		return param2;
	}

	public T3 getParam3() {
		return param3;
	}
	
	protected String parseResponse(BasicHttpResponse response) throws Exception{
		HttpEntity entity = response.getEntity();
		InputStream in = entity.getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
		String res = "";
		String line = reader.readLine();
		while (line != null) {
			res += line;
			line = reader.readLine();
		}
		return res;
	}
	
}
