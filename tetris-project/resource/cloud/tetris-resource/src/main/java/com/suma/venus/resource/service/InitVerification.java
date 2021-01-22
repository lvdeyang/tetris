package com.suma.venus.resource.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.bo.AccessCapacityBO;
import com.suma.venus.resource.service.exception.HttpGadgetException;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class InitVerification {
	
	public static Map<String, Object> authMap = new HashMap<String,Object>();
	
	public final static AccessCapacityBO accessCapacity = new AccessCapacityBO();

	
	@Autowired
	private Additional additional;

	/**
	 * 服务启动时调用小工具获取接入设备的限制个数<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月8日 上午8:41:06
	 */
	public void initialCapacity()throws Exception{
		CloseableHttpClient client = null;

		try {
//			读取小工具信息配置文件
			String profile = additional.readGadgetConfig();
			JSONObject gadgetfile = JSONObject.parseObject(profile);
			
			String serverip = gadgetfile.getString("ip") ;
			Integer port = gadgetfile.getInteger("port") ;
			String user =  gadgetfile.getString("username");
			String password =  gadgetfile.getString("password");
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			AuthScope authScope = new AuthScope(serverip, port, "example.com", AuthScope.ANY_SCHEME);
	        credsProvider.setCredentials(authScope, new UsernamePasswordCredentials(user, password));
	        client = HttpClients.custom()
			        		    .setDefaultCredentialsProvider(credsProvider)
			        		    .setRetryHandler(new DefaultHttpRequestRetryHandler(1, true))
			        		    .build();
	        
	        String url = new StringBufferWrapper().append("http://").append(serverip).append(":").append(port).append("/action/license_content_detail").toString();
	        
	        System.out.println(url);
	        HttpPost httpPost = new HttpPost(url);
	        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
	        
			httpPost.setEntity(new UrlEncodedFormEntity(formparams, "utf-8"));
	        
	        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
	        httpPost.setConfig(requestConfig);
	        
			CloseableHttpResponse response = client.execute(httpPost);
			
			// 解析小工具HTTP返回结果并提示异常信息
			HttpEntity httpEntity = response.getEntity();
			InputStream content = httpEntity.getContent();
			byte[] byteArr = new byte[content.available()];
			content.read(byteArr);
			String str = new String(byteArr);
			JSONObject jsonObject = JSON.parseObject(str);
			String result = jsonObject.getString("result");
			String errormsg = jsonObject.getString("errormsg");
			if(!"0".equals(result)){
				throw new HttpGadgetException(serverip, port.toString(), errormsg);
			}
			
			int code = response.getStatusLine().getStatusCode();
			if(code != 200){
				throw new HttpGadgetException(serverip, port.toString(), String.valueOf(code));
			}
			JSONArray process = jsonObject.getJSONArray("process");
			for (int i = 0; i < process.size(); i++) {
				JSONObject processJsonObject = process.getJSONObject(i);
				if(processJsonObject.getString("name").equals("JV210Joiner")){
					if("true".equals(processJsonObject.getString("support"))){
						accessCapacity.setJv210(Long.valueOf(processJsonObject.getString("serverNum")));
					}
				}
				if(processJsonObject.getString("name").equals("CDNJoiner")){
					if("true".equals(processJsonObject.getString("support"))){
						accessCapacity.setCdn(Long.valueOf(processJsonObject.getString("cdnNum")));
					}
				}
				if(processJsonObject.getString("name").equals("MixerJoiner")){
					if("true".equals(processJsonObject.getString("support"))){
						accessCapacity.setMixer(Long.valueOf(processJsonObject.getString("mixerNum")));
					}
				}
			}
			
		} finally {
			if(client != null) client.close();
		}
	}
}
