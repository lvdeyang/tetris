package com.sumavision.tetris.spring.zuul.auth.filter;

import javax.servlet.http.HttpServletRequest;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.ext.request.RequestUserAgentAnalyzer;

/**
 * 移动终端拦截器<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年3月12日 下午1:34:26
 */
public class QtLoginFilter extends ZuulFilter{

	@Override
	public Object run() {
		RequestUserAgentAnalyzer analyzer = SpringContext.getBean(RequestUserAgentAnalyzer.class);
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		/*if(!analyzer.isMobileDevie(request)){
			//不是移动终端拒绝访问
			ctx.setResponseStatusCode(403);
			ctx.setSendZuulResponse(false);
		}*/
		
		//不需要登录访问
		String requestUri = request.getRequestURI();
		String[] ignores = new String[]{"", ""};
		for(String ignore:ignores){
			if(requestUri.equals(ignore)){
				return null;
			}
		}
		
		//登录校验
		
		return null;
	}

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		String requestUri = request.getRequestURI();
		requestUri = requestUri.replace(new StringBufferWrapper().append("/").append(requestUri.split("/")[1]).toString(), "");
		if(requestUri.startsWith("/api/qt")){
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
