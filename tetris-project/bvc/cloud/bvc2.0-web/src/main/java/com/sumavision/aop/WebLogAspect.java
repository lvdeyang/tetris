
/*  
* Copyright @ 2018 com.iflysse.trains  
* bvc-monitor-ui 下午1:56:35  
* All right reserved.  
*  
*/

package com.sumavision.aop;

import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

/**
 * @desc: bvc-monitor-ui aop实例，访问action前后记录weblog
 * @author: cll
 * @createTime: 2018年6月5日 下午1:56:35
 * @history:
 * @version: v1.0
 */
@Aspect
@Component
@Slf4j
public class WebLogAspect {
	// pointCut切点
	@Pointcut("@annotation(com.sumavision.aop.annotation.Weblog)")
	public void log() {
	}

	public void doBeforeController(JoinPoint joinPoint) {

		// 接收到请求，记录请求内容
		log.debug("WebLogAspect.doBefore()");
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();

		// 记录下请求内容
		log.debug("URL : " + request.getRequestURL().toString());
		log.debug("HTTP_METHOD : " + request.getMethod());
		log.debug("IP : " + request.getRemoteAddr());
		log.debug("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "."
				+ joinPoint.getSignature().getName());
		log.debug("ARGS : " + Arrays.toString(joinPoint.getArgs()));
		// 获取所有参数方法一：
		Enumeration<String> enu = request.getParameterNames();
		while (enu.hasMoreElements()) {
			String paraName = (String) enu.nextElement();
			System.out.println(paraName + ": " + request.getParameter(paraName));
		}
	}

	@AfterReturning(pointcut = "log()")
	public void doAfterController(JoinPoint joinPoint) {
		log.debug("WebLogAspect.doAfterReturning()");
	}

}
