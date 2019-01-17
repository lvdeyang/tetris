package com.sumavision.tetris.tool.test.flow.client.core.annotation.service;

import java.util.Enumeration;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.swing.Spring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.tool.test.flow.client.core.annotation.enumeration.TestFlowSwitch;
import com.sumavision.tetris.tool.test.flow.client.core.api.SchemeApi;
import com.sumavision.tetris.tool.test.flow.client.core.cache.enumeration.Names;
import com.sumavision.tetris.tool.test.flow.client.core.cache.service.MethodResultCache;
import com.sumavision.tetris.tool.test.flow.client.core.metadata.ServiceMetadata;

/**
 * @ClassName: 启用TestFlow测试 <br/>
 * @Description: 1.封装脚本录制<br/>
 * 				 2.包装接口测试<br/>
 * @author lvdeyang
 * @date 2018年8月30日 上午8:33:33 
 */
@Component  
@Aspect
@Order(50)
public class TestFlowService {

	private static final Logger LOG = LoggerFactory.getLogger(TestFlowService.class);
	
	@Autowired
	private SchemeApi schemeApi;
	
	@Pointcut("@annotation(com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody)")
    public void jsonBody(){}
	
	@Around("jsonBody()")
	public Object wrap(ProceedingJoinPoint joinPoint) throws Throwable{
		
		ServiceMetadata metadata = SpringContext.getBean(ServiceMetadata.BEANNAME);
		
		TestFlowSwitch testFlowSwitch = metadata.getSwitch() == null?TestFlowSwitch.OFF:metadata.getSwitch();
		
		//切入点参数
		Object[] args = joinPoint.getArgs();
		
		Object data = null;
		
		if(testFlowSwitch.equals(TestFlowSwitch.OFF)){
			//测试关闭
			data = joinPoint.proceed(args);
		}else{
			//获取类名
			String className = joinPoint.getSignature().getDeclaringTypeName();
			
			//获取方法名
			String methodName = joinPoint.getSignature().getName();
			
			//遍历参数，目前只支持formdata，不支持流的形式
			HttpServletRequest request = null;
			for(Object arg: args){
				if(arg instanceof HttpServletRequest){
					request = (HttpServletRequest)arg;
					break;
				}
			}
			Enumeration<String> em = request.getParameterNames();
			JSONObject param = new JSONObject();
			 while (em.hasMoreElements()) {
			    String name = (String) em.nextElement();
			    String value = request.getParameter(name);
			    param.put(name, value);
			}
			
			String fullUri = request.getRequestURI();
			String uri = fullUri;
			if(metadata.getContextPath()!=null && metadata.getContextPath().startsWith("/")){
				uri = fullUri.replaceAll(metadata.getContextPath(), "");
			}
			
			if(testFlowSwitch.equals(TestFlowSwitch.STORE)){
				//处理接口录制--处理期望返回数据
				data = joinPoint.proceed(args);
				Object expect = packExpectResultData(data);
				//录制一个接口
				schemeApi.save(metadata, className, methodName, uri, param.toJSONString(), JSON.toJSONString(expect));
			}else if(testFlowSwitch.equals(TestFlowSwitch.TEST)){
				//处理接口测试--直接修改接口返回值
				data = packTestResultData(joinPoint.proceed(args));
			}
			
		}
		return data;
	}
	
	/**
	 * @Title: 复制接口返回数据，优先从缓存拿数据，适用于接口录制<br/>
	 * @param data 原接口返回数据 
	 * @return Object 接口期望返回数据
	 */
	private Object packExpectResultData(Object data){
		Object testData = MethodResultCache.getObject(Names.METHOD_RESULT_KEY.getName());
		if(testData != null){
			JSONObject jsonData = (JSONObject)data;
			JSONObject expectData = new JSONObject();
			Set<String> keys = jsonData.keySet();
			for(String key:keys){
				if(!"".equals(key)){
					expectData.put(key, jsonData.get(key));
				}
			}
			expectData.put("data", testData);
			return expectData;
		}else{
			return data;
		}
	}
	
	/**
	 * @Title: 转换接口返回数据，优先从缓存拿数据，适用于接口测试<br/> 
	 * @param data 原接口返回数据 
	 * @return 接口测试返回数据
	 */
	private Object packTestResultData(Object data){
		Object testData = MethodResultCache.getObject(Names.METHOD_RESULT_KEY.getName());
		if(testData != null){
			JSONObject jsonData = (JSONObject)data;
			jsonData.put("data", testData);
			MethodResultCache.clear(Names.METHOD_RESULT_KEY.getName());
			return jsonData;
		}
		return data;
	}
	
}
