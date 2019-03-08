package com.sumavision.tetris.auth.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.sumavision.tetris.mvc.constant.HttpConstant;
import com.sumavision.tetris.mvc.ext.context.HttpSessionContext;
import com.sumavision.tetris.mvc.ext.context.HttpSessionThreadLocal;

/**
 * 用户上下文环境拦截器<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年3月6日 上午11:34:58
 */
@Order(1)
@Component
@WebFilter(urlPatterns = "/*", filterName = "com.sumavision.tetris.auth.filter.HttpSessionThreadLocalFilter")
public class HttpSessionThreadLocalFilter implements Filter{

	@Autowired
	private FilterValidate filterValidate;
	
	@Override
	public void doFilter(ServletRequest nativeRequest, ServletResponse nativeResponse, FilterChain chain)
			throws IOException, ServletException {
		
		try{
			HttpServletRequest request = (HttpServletRequest)nativeRequest;
			HttpServletResponse response = (HttpServletResponse)nativeResponse; 
			String requestUri = request.getRequestURI();
			
			if(!filterValidate.canDoFilter(requestUri)){
				chain.doFilter(request, response);
				return;
			}
			
			String sessionId = request.getHeader(HttpConstant.HEADER_SESSION_ID);
			HttpSession session = null;
			if(sessionId == null){
				//临时session 5秒超时
				session = request.getSession();
				session.setMaxInactiveInterval(HttpConstant.TEMPORARY_SESSION_TIMEOUT);
			}else{
				session = HttpSessionContext.get(sessionId);
				if(session == null){
					//feign调用统一sessionid 超时时间
					session = request.getSession();
					session.setMaxInactiveInterval(HttpConstant.SESSION_TIMEOUT);
					HttpSessionContext.remove(session);
					HttpSessionContext.put(sessionId, session);
				}
			}
			
			String token = request.getHeader(HttpConstant.HEADER_AUTH_TOKEN);
			session.setAttribute(HttpConstant.ATTRIBUTE_AUTH_TOKEN, token);
			
			Thread thread = Thread.currentThread();
			HttpSessionThreadLocal.put(thread, session);
			
			chain.doFilter(request, response);
			
			HttpSessionThreadLocal.remove(thread);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}
	
	@Override
	public void destroy() {}

}
