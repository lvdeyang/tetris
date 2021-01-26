package com.sumavision.tetris.easy.process.core;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.binary.ByteUtil;
import com.sumavision.tetris.commons.util.json.AliFastJsonObject;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.easy.process.access.point.AccessPointDAO;
import com.sumavision.tetris.easy.process.access.point.AccessPointMethodType;
import com.sumavision.tetris.easy.process.access.point.AccessPointPO;
import com.sumavision.tetris.easy.process.access.point.AccessPointParamDAO;
import com.sumavision.tetris.easy.process.access.point.AccessPointParamPO;
import com.sumavision.tetris.easy.process.access.point.AccessPointType;
import com.sumavision.tetris.easy.process.access.point.ParamDirection;
import com.sumavision.tetris.easy.process.access.point.ParamPackagingMethod;
import com.sumavision.tetris.easy.process.access.service.ServiceType;
import com.sumavision.tetris.easy.process.access.service.rest.RestServiceDAO;
import com.sumavision.tetris.easy.process.access.service.rest.RestServicePO;
import com.sumavision.tetris.easy.process.core.exception.ErrorAccessPointResponseStatusCodeException;
import com.sumavision.tetris.easy.process.core.exception.VariableValueCheckFailedException;
import com.sumavision.tetris.mvc.constant.HttpConstant;
import com.sumavision.tetris.sdk.constraint.api.ConstraintValidator;

/**
 * 远程接入点调用<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月9日 上午8:57:33
 */
@Service
@Transactional(rollbackFor = Exception.class)
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
	
	@Autowired
	private AliFastJsonObject aliFastJsonObject;
	
	@Autowired
	private ProcessDAO processDao;
	
	@Autowired
	private ProcessVariableDAO processVariableDao;
	
	@Autowired
	private ProcessParamReferenceDAO processParamReferenceDao;

	/**
	 * 远程接入点调用<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月9日 下午4:23:31
	 * @param DelegateExecution execution 流程
	 * @param Long accessPointId 接入点id
	 */
	public void invoke(DelegateExecution execution, Long accessPointId) throws Exception{
		
		AccessPointPO accessPoint = accessPointDao.findById(accessPointId);
		
		List<AccessPointParamPO> paramDefinitions = accessPointParamDao.findByAccessPointIdInAndDirection(new ArrayListWrapper<Long>().add(accessPoint.getId()).getList(), ParamDirection.FORWARD);
		
		if(ServiceType.REST.equals(accessPoint.getServiceType())){
			RestServicePO restService = restServiceDao.findById(accessPoint.getServiceId());
			
			invokeHttpAccessPoint(execution, restService, accessPoint, paramDefinitions);
			
		}
		
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
	public void invokeHttpAccessPoint(
			DelegateExecution execution,
			RestServicePO service, 
			AccessPointPO accessPoint, 
			List<AccessPointParamPO> paramDefinitions) throws Exception{
		
		final String processInstanceId = execution.getProcessInstanceId();
		
		ProcessPO process = processDao.findByUuid(execution.getProcessDefinitionId().split(":")[0]);
		
		Map<String, Object> processVariables = runtimeService.getVariables(processInstanceId);
		
		JSONObject variableContext = (JSONObject)processVariables.get(InternalVariableKey.VARIABLE_CONTEXT.getVariableKey());
		Map<String, String> requestHeaders = (Map<String, String>)processVariables.get(InternalVariableKey.REQUEST_HEADERS.getVariableKey());
		String startUserId = processVariables.get(InternalVariableKey.START_USER_ID.getVariableKey()).toString();
		
		//加入内置变量：流程实例id
		if(variableContext.getString(InternalVariableKey.PROCESS_INSTANCE_ID.getVariableKey()) == null){
			variableContext.put(InternalVariableKey.PROCESS_INSTANCE_ID.getVariableKey(), processInstanceId);
		}
		
		Map<String, Object> contextVariableMap = aliFastJsonObject.convertToHashMap(variableContext);
		
		Map<String, Object> paramVariableMap = new HashMap<String, Object>();
		if(paramDefinitions!=null && paramDefinitions.size()>0){
			for(AccessPointParamPO paramDefinition:paramDefinitions){
				Object findedValue = contextVariableMap.get(paramDefinition.getPrimaryKeyPath());
				if(findedValue == null){
					LOG.error(new StringBufferWrapper().append("接入点调用时未获取到参数；")
													   .append("流程：").append(process.getName())
													   .append("，流程实例：").append(processInstanceId)
													   .append("参数：").append(paramDefinition.getPrimaryKeyPath())
													   .toString());
				}else{
					//key值转换
					paramVariableMap.put(paramDefinition.getReferenceKeyPath(), findedValue);
				}
			}
		}
		
		//加入内置参数
		paramVariableMap.put(InternalVariableKey.PROCESS_INSTANCE_ID.getVariableKey(), contextVariableMap.get(InternalVariableKey.PROCESS_INSTANCE_ID.getVariableKey()));
		
		//这个参数先不加了，不需要，本来上下文变量中也没有
		//paramVariableMap.put(InternalVariableKey.START_USER_ID.getVariableKey(), contextVariableMap.get(InternalVariableKey.START_USER_ID.getVariableKey()));
		
		if(AccessPointType.REMOTE_ASYNCHRONOUS.equals(accessPoint.getType())){
			paramVariableMap.put(InternalVariableKey.ACCESS_POINT_ID.getVariableKey(), accessPoint.getId());
		}
		
		JSONObject paramJson = aliFastJsonObject.convertFromHashMap(paramVariableMap);
		
		String url = new StringBufferWrapper().append("http://")
											  .append(service.getHost())
											  .append(":")
											  .append(service.getPort())
											  .append(service.getContextPath())
											  .append(accessPoint.getMethod())
											  .toString();

		RestAccessPointInvoker invoker = null;
		
		//临时解决方案：这个header用于通过老接口的拦截器
		Header tokenHeader = new BasicHeader(HttpConstant.HEADER_AUTH_TOKEN, requestHeaders.get(HttpConstant.HEADER_AUTH_TOKEN));
		//以下两个header用户通过/api/process拦截器
		Header processClientHeader = new BasicHeader(HttpConstant.HEADER_PROCESS_CLIENT, HttpConstant.HEADER_PROCESS_CLIENT_KEY);
		Header doUserIdLoginHeader = new BasicHeader(HttpConstant.HEADER_PROCESS_DO_USER_ID_LOGIN, startUserId);
		Header[] headers = new Header[]{tokenHeader, processClientHeader, doUserIdLoginHeader};
		if(AccessPointMethodType.HTTP_METHOD_POST.equals(accessPoint.getMethodType()) && 
				ParamPackagingMethod.JSON.equals(accessPoint.getParamPackagingMethod())){
			invoker = new RestAccessPointInvoker(url, paramJson, RestAccessPointInvoker.JSON, headers);
		}else{
			invoker = new RestAccessPointInvoker(url, paramJson, RestAccessPointInvoker.FORMDATA, headers);
		}
		
		try{
			//返回值判断
			String result = null;
			JSONObject data = null;
			if(AccessPointMethodType.HTTP_METHOD_GET.equals(accessPoint.getMethodType())){
				result = invoker.doGet();
			}else{
				result = invoker.doPost();
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
			
			//返回值定义
			List<AccessPointParamPO> reverseParamDefinitions = accessPointParamDao.findByAccessPointIdInAndDirection(new ArrayListWrapper<Long>().add(accessPoint.getId()).getList(), ParamDirection.REVERSE);
			if(reverseParamDefinitions==null || reverseParamDefinitions.size()<=0) return;
			
			//key值转换
			Map<String, Object> reverseReferenceParamMap = aliFastJsonObject.convertToHashMap(data);
			Map<String, Object> reverseParamMap = new HashMap<String, Object>();
			Map<String, Object> loopedReverseParamMap = new HashMap<String, Object>();
			Set<String> reverseReferenceParamMapKeys = reverseReferenceParamMap.keySet();
			for(String reverseReferenceParamMapKey:reverseReferenceParamMapKeys){
				for(AccessPointParamPO reverseParamDefinition:reverseParamDefinitions){
					if(reverseReferenceParamMapKey.equals(reverseParamDefinition.getReferenceKeyPath())){
						reverseParamMap.put(reverseParamDefinition.getPrimaryKeyPath(), reverseReferenceParamMap.get(reverseReferenceParamMapKey));
						loopedReverseParamMap.put(reverseParamDefinition.getPrimaryKeyPath(), reverseReferenceParamMap.get(reverseReferenceParamMapKey));
						break;
					}
				}
			}
			
			//处理映射
			Set<String> reverseParamMapKeys = loopedReverseParamMap.keySet();
			List<ProcessParamReferencePO> paramReferences = processParamReferenceDao.findByProcessId(process.getId());
			if(paramReferences!=null && paramReferences.size()>0){
				for(String reverseParamMapKey:reverseParamMapKeys){
					for(ProcessParamReferencePO paramReference:paramReferences){
						String scopeReference = paramReference.getReference();
						if(scopeReference != null){
							if(scopeReference==null || "".equals(scopeReference)) continue;
							if(scopeReference.indexOf(reverseParamMapKey) < 0) continue;
							String[] primaryKeyPaths = scopeReference.split(ProcessParamReferencePO.KEY_SEPARATOR);
							Object effectValue = null;
							//校验值的有效性
							for(String keyPath:primaryKeyPaths){
								Object setValue = loopedReverseParamMap.get(keyPath);
								if(setValue != null){
									if(effectValue == null){
										effectValue = setValue;
									}else{
										if(!effectValue.equals(setValue)){
											throw new Exception("两个key存在映射但赋值不一样！");
										}
									}
								}
							}
							//设置值
							for(String keyPath:primaryKeyPaths){
								reverseParamMap.put(keyPath, effectValue);
							}
						}
					}
				}
			}
			
			//返回值有效性校验
			JSONObject validataJsonContext = aliFastJsonObject.convertFromHashMap(reverseParamMap);
			for(String reverseParamMapKey:reverseParamMapKeys){
				for(AccessPointParamPO reverseParamDefinition:reverseParamDefinitions){
					if(reverseParamMapKey.equals(reverseParamDefinition.getPrimaryKeyPath())){
						boolean validateResult = constraintValidator.validate(validataJsonContext, reverseParamDefinition.getConstraintExpression());
						if(!validateResult){
							//校验未通过
							throw new VariableValueCheckFailedException(reverseParamDefinition.getPrimaryKeyPath(), reverseParamDefinition.getName(), reverseParamMap.get(reverseParamMapKey).toString(), reverseParamDefinition.getConstraintExpression());
						}
						break;
					}
				}
			}
			
			//处理引用值
			List<ProcessVariablePO> variableWithExpressionValue =  processVariableDao.findByProcessIdAndExpressionValueNotNull(process.getId());
			if(variableWithExpressionValue!=null && variableWithExpressionValue.size()>0){
				for(ProcessVariablePO variable:variableWithExpressionValue){
					String value = constraintValidator.getStringValue(validataJsonContext, variable.getExpressionValue());
					if(value != null){
						reverseParamMap.put(variable.getPrimaryKey(), value);
					}
				}
			}
			
			//回写参数
			reverseParamMapKeys = reverseParamMap.keySet();
			for(String reverseParamMapKey:reverseParamMapKeys){
				contextVariableMap.put(reverseParamMapKey, reverseParamMap.get(reverseParamMapKey));
			}
			variableContext = aliFastJsonObject.convertFromHashMap(contextVariableMap);
			runtimeService.setVariable(processInstanceId, InternalVariableKey.VARIABLE_CONTEXT.getVariableKey(), variableContext);
		}catch(Exception e){
			e.printStackTrace();
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
		
		public static final String FORMDATA = "application/x-www-form-urlencoded; charset=UTF-8";
		
		public static final String JSON = "application/json;charset=UTF-8";
		
		public static final String GET = "get";
		
		public static final String POST = "post";
		
		private static final Logger LOG = LoggerFactory.getLogger(RestAccessPointInvoker.class);
		
		private RequestConfig requestConfig;
		
		private CloseableHttpClient httpClient;
		
		private JSONObject params;
		
		private String dataType;
		
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
				JSONObject params, 
				String dataType,
				BasicClientCookie cookie,
				Header[] headers){
			this.url = url;
			this.params = params;
			this.dataType = dataType;
			this.requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(5000).build();
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
	        this.retryTimes = 1;
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
				JSONObject params,
				String dataType){
			this(url, params, dataType, null, null);
		}
		
		/**
		 * 构造方法重载<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2019年1月10日 下午1:58:02
		 */
		public RestAccessPointInvoker(
				String url, 
				JSONObject params,
				String dataType,
				BasicClientCookie cookie){
			this(url, params, dataType, cookie, null);
		}
		
		/**
		 * 构造方法重载<br/>
		 * <b>作者:</b>lvdeyang<br/>
		 * <b>版本：</b>1.0<br/>
		 * <b>日期：</b>2019年1月10日 下午1:58:02
		 */
		public RestAccessPointInvoker(
				String url, 
				JSONObject params,
				String dataType,
				Header[] headers){
			this(url, params, dataType, null, headers);
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
				HttpEntity entity = null;
				if(this.dataType == FORMDATA){
					entity = getEntity(POST);
					postMethod.setHeader("Content-Type", FORMDATA);
				}else if(this.dataType == JSON){
					entity = getEntity(POST);
					postMethod.setHeader("Content-Type", JSON);
				}
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
				String urlParam = EntityUtils.toString(getEntity(GET));  
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
        
        /**
         * 生成http Entity<br/>
         * <b>作者:</b>lvdeyang<br/>
         * <b>版本：</b>1.0<br/>
         * <b>日期：</b>2019年3月27日 下午4:17:16
         * @return HttpEntity 
         */
        public HttpEntity getEntity(String httpMethod) throws Exception{
        	if(httpMethod==GET || (httpMethod==POST && this.dataType==FORMDATA)){
        		List<BasicNameValuePair> formatParams = new ArrayList<BasicNameValuePair>();
        		if(this.params!=null && this.params.size()>0){
        			Set<String> paramKeys = params.keySet();
        			for(String paramKey:paramKeys){
        				BasicNameValuePair formatParam = new BasicNameValuePair(paramKey, this.params.getString(paramKey));
        				formatParams.add(formatParam);
        			}
        		}
        		return new UrlEncodedFormEntity(formatParams, "utf-8");
        	}else if(httpMethod==POST && this.dataType==JSON){
        		String formatParams = this.params==null?new JSONObject().toJSONString():this.params.toJSONString();
        		return new StringEntity(formatParams);
        	}
        	return null;
        }
		
	}
	
}
