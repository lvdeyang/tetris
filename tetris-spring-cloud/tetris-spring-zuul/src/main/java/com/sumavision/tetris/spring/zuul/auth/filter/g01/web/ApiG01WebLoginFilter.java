package com.sumavision.tetris.spring.zuul.auth.filter.g01.web;

import javax.servlet.http.HttpServletRequest;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.auth.token.TokenQuery;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.uri.UriUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.constant.HttpConstant;
import com.sumavision.tetris.mvc.ext.request.RequestUserAgentAnalyzer;

public class ApiG01WebLoginFilter extends ZuulFilter{

	private String[] ignores = new String[]{
		"/api/g01/web/resource/*",
		"/api/g01/web/auth/do/username/password/login"
	};
	
	@Override
	public Object run() {
		RequestUserAgentAnalyzer analyzer = SpringContext.getBean(RequestUserAgentAnalyzer.class);
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		
		//不需要登录访问
		String requestUri = request.getRequestURI();
		requestUri = requestUri.replace(new StringBufferWrapper().append("/").append(requestUri.split("/")[1]).toString(), "");
		if(UriUtil.match(requestUri, ignores)) return null;
		
		//登录校验
		String token = request.getHeader(HttpConstant.HEADER_AUTH_TOKEN);
		TokenQuery tokenQuery = SpringContext.getBean(TokenQuery.class);
		
		try{
			boolean result = tokenQuery.checkToken(token, TerminalType.PC_PLATFORM);
			if(!result){
				ctx.setResponseStatusCode(403);
				ctx.setSendZuulResponse(false);
			}
		}catch(Exception e){
			e.printStackTrace();
			ctx.setResponseStatusCode(403);
			ctx.setSendZuulResponse(false);
		}
		
		return null;
		
	}
	
	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		String requestUri = request.getRequestURI();
		requestUri = requestUri.replace(new StringBufferWrapper().append("/").append(requestUri.split("/")[1]).toString(), "");
		if(requestUri.startsWith(TerminalType.G01_WEB.getUriPrefix())){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public int filterOrder() {
		return 1;
	}
	
	@Override
	public String filterType() {
		return "pre";
	}
	
}
