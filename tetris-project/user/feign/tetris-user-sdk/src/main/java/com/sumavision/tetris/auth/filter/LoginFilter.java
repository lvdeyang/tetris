package com.sumavision.tetris.auth.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.mvc.constant.HttpConstant;
import com.sumavision.tetris.user.UserQuery;

/**
 * 登录校验<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年3月6日 上午11:38:45
 */
@Order(0)
@WebFilter(urlPatterns = "/*", filterName = "com.sumavision.tetris.auth.filter.LoginFilter")
public class LoginFilter implements Filter{

	@Override
	public void doFilter(ServletRequest nativeRequest, ServletResponse nativeResponse, FilterChain chain)
			throws IOException, ServletException {
		
		FilterValidate filterValidate = SpringContext.getBean(FilterValidate.class);
		UserQuery userQuery = SpringContext.getBean(UserQuery.class);
		
		HttpServletRequest request = (HttpServletRequest)nativeRequest;
		HttpServletResponse response = (HttpServletResponse)nativeResponse;
		
		String requestUri = request.getRequestURI();
		if(!filterValidate.canDoFilter(requestUri)){
			chain.doFilter(request, response);
			return;
		}
		
		String token = request.getHeader(HttpConstant.HEADER_AUTH_TOKEN);
		
		try {
			userQuery.checkToken(token);
			chain.doFilter(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("----------------------------");
			System.out.println(requestUri);
			JSONObject jsonResult = new JSONObject();
			//返回错误信息
			if(e instanceof BaseException){
				BaseException bex = (BaseException)e;
				jsonResult.put("status", bex.getCode().getCode());
				jsonResult.put("message", bex.getMessage());
			}else{
				jsonResult.put("status", StatusCode.ERROR.getCode());
				jsonResult.put("message", "服务器端异常");
			}
			response.setContentType("application/json; charset=UTF-8");
			PrintWriter writer = response.getWriter();
			writer.write(jsonResult.toJSONString());
			writer.close();
		}
		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}
	
	@Override
	public void destroy() {}

}
