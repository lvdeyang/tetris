package com.sumavision.tetris.spring.zuul.auth.filter;


import javax.servlet.http.HttpServletRequest;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class AccessTokenFilter extends ZuulFilter{

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		/*HttpServletRequest request = ctx.getRequest();
 
		Object accessToken = request.getParameter("accessToken");
		System.out.println("accessToken:" + accessToken);
		if (accessToken == null) {
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(401);
			return null;
		}*/
		return null;

	}

	@Override
	public boolean shouldFilter() {
		return true;
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
