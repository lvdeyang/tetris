package com.sumavision.tetris.spring.eureka.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.file.FileUtil;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.constant.HttpConstant;
import com.sumavision.tetris.spring.eureka.application.exception.EurekaResponseErrorException;
import com.sumavision.tetris.spring.eureka.config.ApplicationYml;

public class EurekaFeign {

	/**
	 * get请求<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月30日 下午1:35:01
	 * @param String url 请求地址
	 * @param Map<String, Object> params 参数
	 * @param String errorMsg 异常信息
	 * @return String 接口返回
	 */
	public static String doGet(String url, Map<String, Object> params, String errorMsg) throws Exception{
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		try{
			httpclient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader(HttpConstant.HEADER_FEIGN_CLIENT, HttpConstant.HEADER_FEIGN_CLIENT_KEY);
			response = httpclient.execute(httpGet);
			if(response.getStatusLine().getStatusCode() == 200){
	        	HttpEntity entity = response.getEntity();
	        	String appInfos = FileUtil.readAsString(entity.getContent());
	 	        EntityUtils.consume(entity);
	 	        return appInfos;
	        }else{
	        	throw new EurekaResponseErrorException(errorMsg);
	        }
		}finally{
			if(response != null) response.close();
			if(httpclient != null) httpclient.close();
		}
	}
	
	/**
	 * post请求<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月30日 下午1:35:01
	 * @param String url 请求地址
	 * @param Map<String, Object> params 参数
	 * @param String errorMsg 异常信息
	 * @return String 接口返回
	 */
	public static String doPost(String url, Map<String, Object> params, String errorMsg) throws Exception{
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		try{
			httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader(HttpConstant.HEADER_FEIGN_CLIENT, HttpConstant.HEADER_FEIGN_CLIENT_KEY);
			if(params!=null && params.size()>0){
				List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
				Set<String> keys = params.keySet();
				for(String key:keys){
					formparams.add(new BasicNameValuePair(key, params.get(key).toString()));  
				}
				httpPost.setEntity(new UrlEncodedFormEntity(formparams, "utf-8"));
			}
			response = httpclient.execute(httpPost);
			if(response.getStatusLine().getStatusCode() == 200){
	        	HttpEntity entity = response.getEntity();
	        	String result = FileUtil.readAsString(entity.getContent());
	 	        EntityUtils.consume(entity);
	 	        return result;
	        }else{
	        	throw new EurekaResponseErrorException(errorMsg);
	        }
		}finally{
			if(response != null) response.close();
			if(httpclient != null) httpclient.close();
		}
	}
	
	/**
	 * 内存查询<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月30日 下午1:28:45
	 */
	@Component
	public static class MemoryQuery{
		
		@Autowired
		private ApplicationYml applicationYml;
		
		/**
		 * eureka监控首页<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2020年3月6日 下午3:39:41
		 * @return html页面
		 */
		public String homePage() throws Exception{
			return doGet(new StringBufferWrapper().append(applicationYml.getBaseUrl()).append("/monitor").toString(), 
					null, 
					"获取eureka首页失败");
		}
		
		/**
		 * 根据appId查询app实例列表<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2019年10月11日 下午2:52:23
		 * @param appId String 服务id
		 * @return eureka api 返回，xml格式
		 */
		public String findByAppId(String appId) throws Exception{
			return doGet(new StringBufferWrapper().append(applicationYml.getEurekaUrl()).append("apps/").append(appId).toString(), 
					null, 
					"根据appId查询app实例列表失败");
		}
		
		/**
		 * 获取app实例列表<br/>
		 * <b>作者:</b>lqxuhv<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2020年8月14日 下午5:43:57
		 * @return eureka api  返回  .xml文件
		 */
		public String findAll() throws Exception{
			return doGet(new StringBufferWrapper().append(applicationYml.getEurekaUrl()).append("apps/").toString(), 
					null,
					"获取app实例列表失败");
		}
		
	}
	
	
	
	/**
	 * 数据库查询<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月30日 下午1:29:00
	 */
	@Component
	public static class SqlQuery{
		
		@Autowired
		private ApplicationYml applicationYml;
		
		/**
		 * 查询所有微服务实例<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2019年12月26日 上午9:29:56
		 * @return JSONObject 微服务实例
		 */
		public JSONObject findAll() throws Exception{
			return JSON.parseObject(doPost(new StringBufferWrapper().append(applicationYml.getBaseUrl()).append("/application/feign/find/all").toString(), 
					null, 
					"查询所有微服务实例"));
		}
		
		/**
		 * 根据实例id查询服务节点<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2019年12月30日 下午2:00:36
		 * @param String instanceId 微服务实例id
		 * @return JSONObject 微服务实例
		 */
		public JSONObject findByInstanceId(String instanceId) throws Exception{
			return JSON.parseObject(doPost(new StringBufferWrapper().append(applicationYml.getBaseUrl()).append("/application/feign/find/by/instance/id").toString(), 
					new HashMapWrapper<String, Object>().put("instanceId", instanceId).getMap(), 
					"根据服务实例查询服务节点信息"));
		}
		
	}
	
	
}
