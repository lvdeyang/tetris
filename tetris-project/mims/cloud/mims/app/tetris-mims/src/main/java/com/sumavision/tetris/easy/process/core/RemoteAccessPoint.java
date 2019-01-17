package com.sumavision.tetris.easy.process.core;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.binary.ByteUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.constraint.api.ConstraintValidator;
import com.sumavision.tetris.easy.process.access.point.AccessPointDAO;
import com.sumavision.tetris.easy.process.access.point.AccessPointMethodType;
import com.sumavision.tetris.easy.process.access.point.AccessPointPO;
import com.sumavision.tetris.easy.process.access.point.AccessPointParamDAO;
import com.sumavision.tetris.easy.process.access.point.AccessPointParamPO;
import com.sumavision.tetris.easy.process.access.point.AccessPointType;
import com.sumavision.tetris.easy.process.access.point.ParamDirection;
import com.sumavision.tetris.easy.process.access.service.ServiceType;
import com.sumavision.tetris.easy.process.access.service.rest.RestServiceDAO;
import com.sumavision.tetris.easy.process.access.service.rest.RestServicePO;
import com.sumavision.tetris.easy.process.api.InternalVariableKey;
import com.sumavision.tetris.easy.process.api.exception.RepeatedVariableValueCheckFailedException;
import com.sumavision.tetris.easy.process.api.exception.VariableValueCheckFailedException;
import com.sumavision.tetris.easy.process.core.exception.ErrorAccessPointResponseStatusCodeException;

/**
 * 远程接入点调用<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月9日 上午8:57:33
 */
@Component
public class RemoteAccessPoint {
	
	private static final Logger LOG = LoggerFactory.getLogger(RemoteAccessPoint.class);
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private RestServiceDAO restServiceDao;
	
	@Autowired
	private AccessPointDAO accessPointDao;
	
	@Autowired
	private AccessPointParamDAO accessPointParamDao;
	
	@Autowired
	private ConstraintValidator constraintValidator;

	/**
	 * 远程接入点调用<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月9日 下午4:23:31
	 * @param DelegateExecution execution 流程
	 * @param Long accessPointId 接入点id
	 */
	public void invoke(DelegateExecution execution, Long accessPointId) throws Exception{
		
		String processInstanceId = execution.getProcessInstanceId();
		
		AccessPointPO accessPoint = accessPointDao.findOne(accessPointId);
		
		List<AccessPointParamPO> paramDefinitions = accessPointParamDao.findByAccessPointIdInAndDirection(new ArrayListWrapper<Long>().add(accessPoint.getId()).getList(), ParamDirection.FORWARD);
		
		if(ServiceType.REST.equals(accessPoint.getServiceType())){
			RestServicePO restService = restServiceDao.findOne(accessPoint.getServiceId());
			
			invokeHttpAccessPoint(processInstanceId, restService, accessPoint, paramDefinitions);
			
		}
		
		System.out.println("调用："+accessPointId);
	}
	
	/**
	 * rest服务接入点调用<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月9日 下午5:22:05
	 * @param final String processInstanceId 流程实例id
	 * @param RestServicePO service rest服务
	 * @param AccessPointPO accessPoint 接入点
	 * @param List<AccessPointParamPO> paramDefinitions 参数列表
	 */
	@SuppressWarnings("unchecked")
	public void invokeHttpAccessPoint(
			final String processInstanceId, 
			RestServicePO service, 
			AccessPointPO accessPoint, 
			List<AccessPointParamPO> paramDefinitions) throws Exception{
		
		Map<String, Object> processVariables = runtimeService.getVariables(processInstanceId);
		
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		for(AccessPointParamPO paramDefinition:paramDefinitions){
			params.add(new BasicNameValuePair(paramDefinition.getPrimaryKey(), processVariables.get(paramDefinition.getPrimaryKey()).toString()));
		}
		params.add(new BasicNameValuePair(InternalVariableKey.PROCESSID.getVariableKey(), processVariables.get(InternalVariableKey.PROCESSID.getVariableKey()).toString()));
		params.add(new BasicNameValuePair(InternalVariableKey.STARTUSERID.getVariableKey(), processVariables.get(InternalVariableKey.STARTUSERID.getVariableKey()).toString()));
		if(AccessPointType.REMOTE_ASYNCHRONOUS.equals(accessPoint.getType())){
			//异步接口回调的时候需要把接入点id回传到sdk中
			params.add(new BasicNameValuePair(InternalVariableKey.ACCESSPOINTID.getVariableKey(), accessPoint.getId().toString()));
		}
		String url = new StringBufferWrapper().append("http://")
											  .append(service.getHost())
											  .append(":")
											  .append(service.getPort())
											  .append(service.getContextPath())
											  .append(accessPoint.getMethod())
											  .toString();
		
		RestAccessPointInvoker invoker = new RestAccessPointInvoker(url, params);
		try{
			String result = null;
			JSONObject data = null;
			if(accessPoint.getMethodType().equals(AccessPointMethodType.HTTP_METHOD_POST)){
				result = invoker.doPost();
			}else if(accessPoint.getMethodType().equals(AccessPointMethodType.HTTP_METHOD_GET)){
				result = invoker.doGet();
			}
			JSONObject resultJson = JSON.parseObject(result);
			int status = resultJson.getIntValue("status");
			if(status != 200){
				String message = resultJson.getString("message");
				throw new ErrorAccessPointResponseStatusCodeException(url, status, message);
			}else{
				data = resultJson.getJSONObject("data");
			}
			
			if(data==null || !AccessPointType.REMOTE_SYNCHRONOUS.equals(accessPoint.getType())) return;
			List<AccessPointParamPO> reverseParamDefinitions = accessPointParamDao.findByAccessPointIdInAndDirection(new ArrayListWrapper<Long>().add(accessPoint.getId()).getList(), ParamDirection.REVERSE);
			if(reverseParamDefinitions==null || reverseParamDefinitions.size()<=0) return;
			
			List<ProcessVariableVO> variableDefinitions = (List<ProcessVariableVO>)processVariables.get("variableDefinitions");
			
			//回写流程变量
			for(AccessPointParamPO reverseParamDefinition:reverseParamDefinitions){
				if(!data.containsKey(reverseParamDefinition.getPrimaryKey())){
					//没有返回值就不回写
					LOG.warn(new StringBufferWrapper().append("接入点返回值缺失! 接入点：")
													  .append(url)
													  .append("参数主键：")
													  .append(reverseParamDefinition.getPrimaryKey())
													  .toString());
				}else{
					String value = data.getString(reverseParamDefinition.getPrimaryKey());
					
					//检查返回值与参数是否重复
					ProcessVariableVO targetVariableDefinition = null;
					for(ProcessVariableVO variableDefinition:variableDefinitions){
						if(variableDefinition.getPrimaryKey().equals(reverseParamDefinition.getPrimaryKey())){
							targetVariableDefinition = variableDefinition;
							break;
						}
					}
					if(targetVariableDefinition != null){
						boolean checkResult = constraintValidator.validate(reverseParamDefinition.getPrimaryKey(), 
																		   value, 
																		   targetVariableDefinition.getConstraintExpression());
						if(!checkResult){
							throw new RepeatedVariableValueCheckFailedException(reverseParamDefinition.getPrimaryKey(),
																				reverseParamDefinition.getName(),
																				value,
																				targetVariableDefinition.getConstraintExpression());
						}
					}
					
					if(reverseParamDefinition.getConstraintExpression() != null){
						boolean checkResult = constraintValidator.validate(reverseParamDefinition.getPrimaryKey(), 
																		   value, 
																		   reverseParamDefinition.getConstraintExpression());
						if(!checkResult){
							throw new VariableValueCheckFailedException(reverseParamDefinition.getPrimaryKey(), 
																		reverseParamDefinition.getName(), 
																		value, 
																		reverseParamDefinition.getConstraintExpression());
						}else{
							//回写变量
							runtimeService.setVariable(processInstanceId, reverseParamDefinition.getPrimaryKey(), value);
						}
					}else{
						//回写变量
						runtimeService.setVariable(processInstanceId, reverseParamDefinition.getPrimaryKey(), value);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			//TODO 发生异常
			throw e;
		}
	}
	
	/**
	 * rest接入点调用封装<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月10日 下午2:01:53
	 */
	public static class RestAccessPointInvoker{
		
		private static final Logger LOG = LoggerFactory.getLogger(RestAccessPointInvoker.class);
		
		private RequestConfig requestConfig;
		
		private CloseableHttpClient httpClient;
		
		private List<BasicNameValuePair> params;

		private String url;
		
		private HttpClientContext context;
		
		private Header[] headers;
		
		private long retryInterval;
		
		private int retryTimes;
		
		private int currentRetry;
		
		/**
		 * rest接入点调用<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2019年1月10日 下午1:51:08
		 * @param String url rest url
		 * @param List<BasicNameValuePair> params 传参
		 * @param BasicClientCookie cookie
		 * @param Header[] headers
		 */
		public RestAccessPointInvoker(
				String url, 
				List<BasicNameValuePair> params, 
				BasicClientCookie cookie,
				Header[] headers){
			this.url = url;
			this.params = params;
			this.requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
			this.httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
			this.context = HttpClientContext.create();
			if(cookie != null){
				CookieStore cookieStore = new BasicCookieStore();
				cookieStore.addCookie(cookie);
				this.context.setCookieStore(cookieStore);
			}
			if(headers!=null && headers.length>0){
				this.headers = headers;
			}
	        this.retryInterval = 3000;
	        this.retryTimes = 10;
	        this.currentRetry = 1;
		}
		
		/**
		 * 构造方法重载<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2019年1月10日 下午1:58:02
		 */
		public RestAccessPointInvoker(
				String url, 
				List<BasicNameValuePair> params){
			this(url, params, null, null);
		}
		
		/**
		 * 构造方法重载<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2019年1月10日 下午1:58:02
		 */
		public RestAccessPointInvoker(
				String url, 
				List<BasicNameValuePair> params,
				BasicClientCookie cookie){
			this(url, params, cookie, null);
		}
		
		/**
		 * 构造方法重载<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2019年1月10日 下午1:58:02
		 */
		public RestAccessPointInvoker(
				String url, 
				List<BasicNameValuePair> params,
				Header[] headers){
			this(url, params, null, headers);
		}
		
		/**
		 * post调用<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2019年1月10日 下午1:50:05
		 * @return String 调用结果
		 */
		public String doPost() throws Exception{
			String result = null;
			try{
				HttpPost postMethod = new HttpPost(this.url);
				if(this.headers!=null && this.headers.length>0) postMethod.setHeaders(this.headers);
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(this.params, "utf-8");
				postMethod.setEntity(entity);
				CloseableHttpResponse response = null;
				if(this.context != null){
					response = this.httpClient.execute(postMethod, this.context);
				}else{
					response = this.httpClient.execute(postMethod);
				}
				result = this.parseResponse(response);
				this.httpClient.close();
			}catch(Exception e){
				e.printStackTrace();
				if(this.currentRetry<=this.retryTimes){
					this.doIntevalErrorLog();
					this.currentRetry += 1;
					Thread.sleep(this.retryInterval);
					result = this.doPost();
				}else{
					this.doErrorLog();
					throw e;
				}
			}
			return result;
		}
		
		/**
		 * get调用<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2019年1月10日 下午1:50:40
		 * @return String 调用结果
		 */
		public String doGet() throws Exception{
			String result = null;
			try{
				HttpGet getMethod = new HttpGet(this.url);
				if(this.headers!=null && this.headers.length>0) getMethod.setHeaders(this.headers);
				String urlParam = EntityUtils .toString(new UrlEncodedFormEntity(this.params));  
				getMethod.setURI(new URI(new StringBufferWrapper().append(getMethod.getURI().toString())
	            												  .append("?")
	            												  .append(urlParam)
	            												  .toString())); 
				CloseableHttpResponse response = null;
				if(this.context != null){
					response = this.httpClient.execute(getMethod, this.context);
				}else{
					response = this.httpClient.execute(getMethod);
				}
				result = this.parseResponse(response);
				this.httpClient.close();
			}catch(Exception e){
				e.printStackTrace();
				if(this.currentRetry<=this.retryTimes){
					this.doIntevalErrorLog();
					this.currentRetry += 1;
					Thread.sleep(this.retryInterval);
					result = this.doGet();
				}else{
					this.doErrorLog();
					throw e;
				}
			}
			return result;
		}
		
		/**
		 * 循环调用计次日志<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2019年1月10日 下午1:47:22
		 */
		public void doIntevalErrorLog(){
			LOG.error(new StringBufferWrapper().append("第")
											   .append(this.currentRetry)
											   .append("次访问：")
											   .append(this.url)
											   .append("失败，")
											   .append(this.retryInterval/1000)
											   .append("秒后再次尝试访问！")
											   .toString());
		}
		
		/**
		 * 节点调用最终失败日志<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2019年1月10日 下午1:47:53
		 */
		public void doErrorLog(){
			LOG.error(new StringBufferWrapper().append("第")
											   .append(this.currentRetry)
											   .append("次访问：")
											   .append(this.url)
											   .append("失败，节点调用失败！")
											   .toString());
		}
		
		/**
		 * 解析HttpResponse<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2019年1月10日 下午1:48:34
		 * @param CloseableHttpResponse response http响应
		 * @return String response body
		 */
        public String parseResponse(CloseableHttpResponse response) throws Exception{
        	 String content = null;
		    if(response.getStatusLine().getStatusCode() == 200){
			    HttpEntity entity = response.getEntity();
	            content = new String(ByteUtil.inputStreamToBytes(entity.getContent()), "utf-8");
		 	    EntityUtils.consume(entity);
		 	    response.close();
			}else{
				throw new ErrorAccessPointResponseStatusCodeException(this.url, response.getStatusLine().getStatusCode());
			}
		    return content;
		}
		
	}
	
}
