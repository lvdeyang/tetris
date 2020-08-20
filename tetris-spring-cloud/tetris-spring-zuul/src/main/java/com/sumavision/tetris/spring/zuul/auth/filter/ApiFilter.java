package com.sumavision.tetris.spring.zuul.auth.filter;

import javax.servlet.http.HttpServletRequest;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.constant.HttpConstant;
import com.sumavision.tetris.mvc.ext.request.RequestResouceTypeAnalyzer;

/**
 * 不是api开头的接口都不开放<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年3月12日 下午1:34:26
 */
public class ApiFilter extends ZuulFilter{

	@Override
	public Object run() {
		RequestResouceTypeAnalyzer analyzer = SpringContext.getBean(RequestResouceTypeAnalyzer.class);
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		if(analyzer.isStaticResource(request)){
			return null;
		}
		String requestUri = request.getRequestURI();
		requestUri = requestUri.replace(new StringBufferWrapper().append("/").append(requestUri.split("/")[1]).toString(), "");
		if(!requestUri.startsWith("/api")){
			ctx.setResponseStatusCode(403);
			ctx.setSendZuulResponse(false);
			return null;
		}
		
		//zuul调用client 密钥
		ctx.addZuulRequestHeader(HttpConstant.HEADER_FEIGN_CLIENT, HttpConstant.HEADER_FEIGN_CLIENT_KEY);
		ctx.addZuulRequestHeader(HttpConstant.HEADER_REAL_IP_FROM_ZUUL, request.getRemoteHost());
		return null;

	}

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		String requestUri = request.getRequestURI();
		requestUri = requestUri.replaceFirst(new StringBufferWrapper().append("/").append(requestUri.split("/")[1]).toString(), "");
		if(requestUri.startsWith("/eb-insert-web") || requestUri.startsWith("/eb-monitor-web") || 
				requestUri.startsWith("/eb-screendisplay-web") || requestUri.startsWith("/eb-statistics-web") || requestUri.startsWith("/eb-resource-web")){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public String filterType() {
		return "pre";
	}

}
