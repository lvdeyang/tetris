package com.sumavision.tetris.tool.test.flow.client.core.annotation.service;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import com.sumavision.tetris.tool.test.flow.client.core.cache.enumeration.Names;
import com.sumavision.tetris.tool.test.flow.client.core.cache.service.MethodResultCache;

/**
 * @ClassName: 以协议内容代替接口返回结果<br/> 
 * @author lvdeyang
 * @date 2018年8月29日 下午4:50:21 
 */
@Component  
@Aspect	
public class TestResultService {

	//private static final Logger LOG = LoggerFactory.getLogger(TestResultService.class);
	
	@Pointcut("@annotation(com.sumavision.tetris.tool.test.flow.client.core.annotation.TestResult)")
    public void TestResult(){}
	
	@Around("TestResult()")
	public Object wrap(ProceedingJoinPoint joinPoint) throws Throwable{
		
		//切入点参数
		Object[] args = joinPoint.getArgs();
		
		// 执行原代码
		Object data = joinPoint.proceed(args);
		
		//缓存数据
		MethodResultCache.put(Names.METHOD_RESULT_KEY.getName(), data);
		
		return data;
		
	}
	
}
