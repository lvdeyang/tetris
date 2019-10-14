package com.sumavision.tetris.eureka.sdk;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.commons.util.file.FileUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.eureka.exception.EurekaResponseErrorException;
import com.sumavision.tetris.websocket.core.config.ApplicationConfig;

@Component
public class EurekaSdk {

	@Autowired
	private ApplicationConfig applicationConfig;
	
	/**
	 * 根据appId查询app实例列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月11日 下午2:52:23
	 * @param appId String 服务id
	 * @return eureka api 返回
	 */
	public String findByAppId(String appId) throws Exception{
		CloseableHttpClient httpclient = null;
		CloseableHttpResponse response = null;
		try{
			httpclient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(new StringBufferWrapper().append(applicationConfig.getEurekaUrl()).append("apps/").append(appId).toString());
			response = httpclient.execute(httpGet);
			if(response.getStatusLine().getStatusCode() == 200){
	        	HttpEntity entity = response.getEntity();
	        	String appInfos = FileUtil.readAsString(entity.getContent());
	 	        EntityUtils.consume(entity);
	 	        return appInfos;
	        }else{
	        	throw new EurekaResponseErrorException("根据appId查询app实例列表失败");
	        }
		}finally{
			if(response != null) response.close();
			if(httpclient != null) httpclient.close();
		}
	}
	
}
