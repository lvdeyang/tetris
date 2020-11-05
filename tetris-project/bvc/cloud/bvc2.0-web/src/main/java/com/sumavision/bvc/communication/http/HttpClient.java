package com.sumavision.bvc.communication.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

//import platform.communication.http.ssl.SSLClient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.communication.http.ssl.SSLClient;



public class HttpClient {
	
	public static void main(String[] args) throws Exception{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", 123);
		String resultString = HttpClient.post("http://192.165.56.102:12345/zkapp", jsonObject.toJSONString());
		System.out.println(resultString);
	}
	
	
	/****************
	 *  http POST方法
	 ****************/
	
	//post
	public static String post(String url, String params) throws Exception{
		return post(url, params, Locale.ENGLISH);
	}
	
	//post
	public static String post(String url, String params, Locale locale) throws Exception{
		return post(url, params, locale, null);
	}
	
	public static String post(String url, String params, Locale locale, RequestConfig config) throws Exception{
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		BufferedReader bReader = null;
		StringBuilder sBuilder = null;
		try{
			httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			if(config!=null) httpPost.setConfig(config);
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setHeader("Accept-Charset", "utf-8");
			httpPost.setHeader("Accept-Language", locale.toLanguageTag());
		         
		    httpPost.setEntity(new StringEntity(params, "utf-8"));
		    response = httpclient.execute(httpPost);
		    if(response.getStatusLine().getStatusCode() == 200){
		    	HttpEntity entity = response.getEntity();
		 	    InputStream in = entity.getContent();
		 	    sBuilder = new StringBuilder();
		 	    String line = "";
		 	    bReader = new BufferedReader(new InputStreamReader(in, "utf-8"));
		 	    while ((line=bReader.readLine()) != null) {
		 	    	sBuilder.append(line);
		 		}
		 	    EntityUtils.consume(entity);
		    }else{
		    	return response.getStatusLine().getStatusCode()+"@$$@请求失败";
		    }
		}finally{
			if(bReader != null) bReader.close();
			if(response != null) response.close();
			if(httpclient != null) httpclient.close();
		}
		return sBuilder==null?null:sBuilder.toString();
	}
	
	//发送原生post请求
	public static String encodepost(String url, String params) throws Exception{
		if(params != null){
			return encodepost(url, JSON.parseObject(params));
		}else{
			return encodepost(url, new JSONObject());
		}
	}
	
	//发送原生post请求
	public static String encodepost(String url, JSONObject params) throws Exception{
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		BufferedReader bReader = null;
		StringBuilder sBuilder = null;
		try{
			httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Accept-Charset", "utf-8");
			httpPost.setHeader("Accept-Language", "en-US,en");
			
			//处理参数
			List <NameValuePair> keyValues = new ArrayList<NameValuePair>();  
			if(params != null){
				Set<String> keys = params.keySet();
				for(String key:keys){
					keyValues.add(new BasicNameValuePair(key, params.getString(key)));  
				}
			}
			httpPost.setEntity(new UrlEncodedFormEntity(keyValues, HTTP.UTF_8)); 
			
	        response = httpclient.execute(httpPost);
	        if(response.getStatusLine().getStatusCode() == 200){
	        	HttpEntity entity = response.getEntity();
	 	        InputStream in = entity.getContent();
	 	        sBuilder = new StringBuilder();
	 	        String line = "";
	 	        bReader = new BufferedReader(new InputStreamReader(in, "utf-8"));
	 	        while ((line=bReader.readLine()) != null) {
	 				sBuilder.append(line);
	 			}
	 	        EntityUtils.consume(entity);
	        }else{
	        	return response.getStatusLine().getStatusCode()+"@$$@请求失败";
	        }
		}finally{
			if(bReader != null) bReader.close();
			if(response != null) response.close();
			if(httpclient != null) httpclient.close();
		}
		return sBuilder==null?null:sBuilder.toString();
	}
	
	/*******************
	 *  http POST file
	 *******************/
	
//	//上传文件--重载
//	public static String postFile(String path, String url) throws Exception{
//		return postFile(path, url, null);
//	}
//	
//	//上传文件--重载
//	public static String postFile(String path, String url, RequestConfig config) throws Exception{
//		File file = new File(path);
//		if(!file.exists()) {
//			throw new Exception("文件不存在！路径："+path);
//		}
//		return postFile(file, url, config);
//	}
//	
//	//上传文件--重载
//	public static String postFile(File file, String url) throws Exception{
//		return postFile(file, url, null);
//	}
//	
//	//上传文件
//	public static String postFile(File file, String url, RequestConfig config) throws Exception{
//        CloseableHttpClient httpClient = null;
//        CloseableHttpResponse response = null;
//        BufferedReader bReader = null;
//        StringBuilder sBuilder = null;
//        try {
//        	httpClient = HttpClients.createDefault();
//             
//            // 把一个普通参数和文件上传给下面这个地址 是一个servlet
//            HttpPost httpPost = new HttpPost(url);
//            
//            if(config!=null) httpPost.setConfig(config);
//              
//            // 把文件转换成流对象FileBody
//            FileBody bin = new FileBody(file);
// 
//	        HttpEntity reqEntity = MultipartEntityBuilder.create()
//                     									 .addPart("file", bin)
//                     									 .build();
//            httpPost.setEntity(reqEntity);
// 
//            // 发起请求 并返回请求的响应
//            response = httpClient.execute(httpPost);
//             
//            //读取请求结果
//            if(response.getStatusLine().getStatusCode() == 200){
//            	HttpEntity entity = response.getEntity();
// 	 	        InputStream in = entity.getContent();
// 	 	        sBuilder = new StringBuilder();
// 	 	        String line = "";
// 	 	        bReader = new BufferedReader(new InputStreamReader(in, "utf-8"));
// 	 	        while ((line=bReader.readLine()) != null) {
// 	 				sBuilder.append(line);
// 	 			}
// 	 	        EntityUtils.consume(entity);
// 	        }else{
// 	        	return response.getStatusLine().getStatusCode()+"@$$@请求失败";
// 	        }
//             
//        }catch (Exception e){
//        	e.printStackTrace();
//        }finally {
//        	if(bReader != null) bReader.close();
//			if(response != null) response.close();
//			if(httpClient != null) httpClient.close();
//        }
//        return sBuilder==null?null:sBuilder.toString();
//	}
	
	/****************
	 *  http GET方法
	 ****************/
	
	//默认请求头
	public static String get(String url) throws Exception{
		return get(url, null);
	}
	
	//默认请求头
	public static String get(String url, RequestConfig config) throws Exception{
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Accept-Charset", "utf-8");
		headers.put("Accept-Language", "en-US,en");
		return get(url, headers, config);
	}
	
	//get，若异常则返回null
	public static String getNoException(String url, RequestConfig config){
		try{
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/json");
			headers.put("Accept-Charset", "utf-8");
			headers.put("Accept-Language", "en-US,en");
			return get(url, headers, config);
		}catch(Exception e){
			return null;
		}
	}
	
	//无序参数
	public static String get(String url, JSONObject params, Map<String, String> headers) throws Exception{
		return get(url, params, headers, null);
	}
	
	//无序参数
	public static String get(String url, JSONObject params, Map<String, String> headers, RequestConfig config) throws Exception{
		StringBuilder urlBuilder = new StringBuilder();
    	urlBuilder.append(url);
    	urlBuilder.append("?");
    	
    	//url设置参数 
    	Set<String> keySet = params.keySet();
    	for(String key:keySet){
    		urlBuilder.append(key);
    		urlBuilder.append("=");
    		urlBuilder.append(params.get(key));
    		urlBuilder.append("&");
    	}
    	
    	url = urlBuilder.toString();
    	url = url.substring(0, url.length()-1);
    	return get(url, headers, config);
	}
	
	public static String get(String url, Map<String, String> params, Map<String, String> headers) throws Exception{
		return get(url, params, headers, null);
	}
	
	public static String get(String url, Map<String, String> params, Map<String, String> headers, RequestConfig config) throws Exception{
		StringBuilder urlBuilder = new StringBuilder();
    	urlBuilder.append(url);
    	urlBuilder.append("?");
    	
    	//url设置参数 
    	Set<String> keySet = params.keySet();
    	for(String key:keySet){
    		urlBuilder.append(key);
    		urlBuilder.append("=");
    		urlBuilder.append(params.get(key));
    		urlBuilder.append("&");
    	}
    	
    	url = urlBuilder.toString();
    	url = url.substring(0, url.length()-1);
    	return get(url, headers, config);
	}
	
	//有序参数
	public static String get(String url, List<String> params, Map<String, String> headers) throws Exception{
		return get(url, params, headers, null);
	}
	
	//有序参数
	public static String get(String url, List<String> params, Map<String, String> headers, RequestConfig config) throws Exception{
		StringBuilder urlBuilder = new StringBuilder();
    	urlBuilder.append(url);
    	urlBuilder.append("?");
    	
    	//url设置参数 
    	for(int i=0; i<params.size(); i++){
    		urlBuilder.append(params.get(i));
    		urlBuilder.append("&");
    	}
    	
    	url = urlBuilder.toString();
    	url = url.substring(0, url.length()-1);
    	return get(url, headers, config);
	}
	
	//直接请求
	public static String get(String url, Map<String, String> headers, RequestConfig config) throws Exception{
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		BufferedReader bReader = null;
		StringBuilder sBuilder = null;
		try{
		
			httpclient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(url);
			if(config != null) httpGet.setConfig(config);
			//设置请求头
			if(headers != null){
				Set<String> keySet = headers.keySet();
				for(String key:keySet){
					httpGet.setHeader(key, headers.get(key));
				}
			}
			
			//发送请求
			response = httpclient.execute(httpGet);
			
			//读取请求结果
			if(response.getStatusLine().getStatusCode() == 200){
	        	HttpEntity entity = response.getEntity();
	 	        InputStream in = entity.getContent();
	 	        sBuilder = new StringBuilder();
	 	        String line = "";
	 	        bReader = new BufferedReader(new InputStreamReader(in, "utf-8"));
	 	        while ((line=bReader.readLine()) != null) {
	 				sBuilder.append(line);
	 			}
	 	        EntityUtils.consume(entity);
	        }else{
	        	return response.getStatusLine().getStatusCode()+"@$$@请求失败";
	        }
		}finally{
			if(bReader != null) bReader.close();
			if(response != null) response.close();
			if(httpclient != null) httpclient.close();
		}
		return sBuilder==null?null:sBuilder.toString();
	}
	
	/*******************
	 *  https POST方法
	 *******************/
	
	public static String postHttps(String url, JSONObject params) throws Exception{
		return postHttps(url, params, "utf-8");
	}
	
	public static String postHttps(String url, JSONObject params, String charset, RequestConfig config) throws Exception{
		Map<String, String> map = JSON.parseObject(params.toJSONString(), Map.class);
		return postHttps(url, map, charset, config);
	}
	
	public static String postHttps(String url, JSONObject params, String charset) throws Exception{
		Map<String, String> map = JSON.parseObject(params.toJSONString(), Map.class);
		return postHttps(url, map, charset, null);
	}
	
	public static String postHttps(String url, Map<String, String> map) throws Exception{
		return postHttps(url, map, "utf-8", null);
	}
	
	public static String postHttps(String url, Map<String, String> map, String charset, RequestConfig config) throws Exception{  
        String result = null;  
        
        org.apache.http.client.HttpClient httpClient = new SSLClient();  
        HttpPost httpPost = new HttpPost(url); 
        if(config != null) httpPost.setConfig(config);
        
        //设置参数  
        List<NameValuePair> list = new ArrayList<NameValuePair>();  
        Iterator iterator = map.entrySet().iterator();  
        while(iterator.hasNext()){  
            Entry<String,String> elem = (Entry<String, String>) iterator.next();  
            list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));  
        }  
        if(list.size() > 0){  
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);  
            httpPost.setEntity(entity);  
        }  
        
        HttpResponse response = httpClient.execute(httpPost); 
        
        if(response != null){  
            HttpEntity resEntity = response.getEntity();  
            if(resEntity != null){  
                result = EntityUtils.toString(resEntity,charset);  
            }  
        }  
        return result;  
    }  
	
	/*******************
	 *  https GET方法
	 *******************/
	
	//无序参数
	public static String getHttps(String url, JSONObject params) throws Exception{
		return getHttps(url, params, "utf-8");
	}
	
	public static String getHttps(String url, JSONObject params, String charset) throws Exception{
		StringBuilder urlBuilder = new StringBuilder();
    	urlBuilder.append(url);
    	urlBuilder.append("?");
    	
    	//url设置参数 
    	Set<String> keySet = params.keySet();
    	for(String key:keySet){
    		urlBuilder.append(key);
    		urlBuilder.append("=");
    		urlBuilder.append(params.get(key));
    		urlBuilder.append("&");
    	}
    	
    	url = urlBuilder.toString();
    	url = url.substring(0, url.length()-1);
		return getHttps(url, params, charset);
	}
	
	//无序参数
	public static String getHttps(String url, Map<String, String> params) throws Exception{
		return getHttps(url, params, "utf-8");
	}
	
	public static String getHttps(String url, Map<String, String> params, String charset) throws Exception{  
		StringBuilder urlBuilder = new StringBuilder();
    	urlBuilder.append(url);
    	urlBuilder.append("?");
    	
    	//url设置参数 
    	Set<String> keySet = params.keySet();
    	for(String key:keySet){
    		urlBuilder.append(key);
    		urlBuilder.append("=");
    		urlBuilder.append(params.get(key));
    		urlBuilder.append("&");
    	}
    	
    	url = urlBuilder.toString();
    	url = url.substring(0, url.length()-1);
    	return getHttps(url, charset, null);
    }  
	
	//有序参数
	public static String getHttps(String url, List<String> params) throws Exception{
		return getHttps(url, params, "utf-8");
	}
	
	public static String getHttps(String url, List<String> params, String charset) throws Exception{
		StringBuilder urlBuilder = new StringBuilder();
    	urlBuilder.append(url);
    	urlBuilder.append("?");
    	
    	//url设置参数 
    	for(int i=0; i<params.size(); i++){
    		urlBuilder.append(params.get(i));
    		urlBuilder.append("&");
    	}
    	
    	url = urlBuilder.toString();
    	url = url.substring(0, url.length()-1);
    	return getHttps(url, charset, null);
	}
	
	public static String getHttps(String url, RequestConfig config) throws Exception{
		return getHttps(url, "utf-8", config);
	}
	
	//直接请求
	public static String getHttps(String url) throws Exception{
		return getHttps(url, "utf-8", null);
	}
	
	public static String getHttps(String url, String charset, RequestConfig config) throws Exception{
		System.out.println(url);
		org.apache.http.client.HttpClient httpClient = new SSLClient(); 
        HttpGet httpGet = new HttpGet(url);
        if(config != null) httpGet.setConfig(config);
        HttpResponse response = httpClient.execute(httpGet);  
        
        String result = null;  
        if(response != null){  
            HttpEntity resEntity = response.getEntity();  
            if(resEntity != null){  
                result = EntityUtils.toString(resEntity,charset);  
            }  
        }  
        return result;
	}
	
	//https，若异常则返回null
	public static String getHttpsNoException(String url, String charset, RequestConfig config){
		try{
			System.out.println(url);
			org.apache.http.client.HttpClient httpClient = new SSLClient(); 
	        HttpGet httpGet = new HttpGet(url);
	        if(config != null) httpGet.setConfig(config);
	        HttpResponse response = httpClient.execute(httpGet);  
	        
	        String result = null;  
	        if(response != null){  
	            HttpEntity resEntity = response.getEntity();  
	            if(resEntity != null){  
	                result = EntityUtils.toString(resEntity,charset);  
	            }  
	        }  
	        return result;
		}catch(Exception e){
			return null;
		}
	}
	
}
