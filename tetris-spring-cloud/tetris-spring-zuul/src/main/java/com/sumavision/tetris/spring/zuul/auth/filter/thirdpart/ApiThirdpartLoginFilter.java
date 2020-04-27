package com.sumavision.tetris.spring.zuul.auth.filter.thirdpart;

import javax.servlet.http.HttpServletRequest;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.encoder.MessageEncoder.Md5Encoder;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.constant.HttpConstant;

/**
 * 第三方服务调用api拦截器<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年1月13日 下午3:21:58
 */
public class ApiThirdpartLoginFilter extends ZuulFilter{

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		String requestUri = request.getRequestURI();
		requestUri = requestUri.replace(new StringBufferWrapper().append("/").append(requestUri.split("/")[1]).toString(), "");
		if(requestUri.startsWith("/api/thirdpart")){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		
		try{
			//md5加密校验
			String nonce = request.getParameter("nonce");
			String certify = request.getParameter("certify");
			
			String message = new StringBufferWrapper().append(nonce)
													  .append(":")
													  .append(HttpConstant.HEADER_FEIGN_CLIENT_KEY)
													  .toString();
			
			Md5Encoder md5Encoder = SpringContext.getBean(Md5Encoder.class);
			
			String zuulCertify = md5Encoder.encode(message);
			if(!zuulCertify.equals(certify)){
				System.out.println("certify的md5校验不通过");
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

	public static void main(String[] args) throws Exception{
		String nonce = "1";
		String message = new StringBufferWrapper().append(nonce)
												  .append(":")
												  .append(HttpConstant.HEADER_FEIGN_CLIENT_KEY)
												  .toString();
		Md5Encoder md5Encoder = new Md5Encoder();
		System.out.println(md5Encoder.encode(message));
	}
	
	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

}
