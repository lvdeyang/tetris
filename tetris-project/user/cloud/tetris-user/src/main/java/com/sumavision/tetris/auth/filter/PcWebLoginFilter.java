package com.sumavision.tetris.auth.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.auth.token.TokenQuery;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.uri.UriUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mvc.constant.HttpConstant;
import com.sumavision.tetris.mvc.ext.request.RequestResouceTypeAnalyzer;

/**
 * 登录校验<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年3月6日 上午11:38:45
 */
@Order(0)
@WebFilter(urlPatterns = "/*", filterName = "com.sumavision.tetris.auth.filter.PcWebLoginFilter")
public class PcWebLoginFilter implements Filter{

	private static final Logger LOG = LoggerFactory.getLogger(PcWebLoginFilter.class);
	
	/** 不拦截路径 */
	private List<String> ignorePath = null;
	
	private RequestResouceTypeAnalyzer requestResouceTypeAnalyzer = null;
	
	private TokenQuery tokenQuery = null;
	
	private void initBean(){
		if(requestResouceTypeAnalyzer == null) requestResouceTypeAnalyzer = SpringContext.getBean(RequestResouceTypeAnalyzer.class);
		if(tokenQuery == null) tokenQuery = SpringContext.getBean(TokenQuery.class);
	}
	
	@Override
	public void doFilter(ServletRequest nativeRequest, ServletResponse nativeResponse, FilterChain chain)
			throws IOException, ServletException {
		
		initBean();
		
		HttpServletRequest request = (HttpServletRequest)nativeRequest;
		HttpServletResponse response = (HttpServletResponse)nativeResponse;
		String method = request.getMethod();
		if(method.toUpperCase().equals("OPTIONS")){
			response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
			response.addHeader("Access-Control-Allow-Credentials", "true");
			response.setHeader("Access-Control-Allow-Methods",
	                "POST, GET, OPTIONS, DELETE");
			response.setHeader("Access-Control-Max-Age", "3600");
			response.setHeader("Access-Control-Allow-Headers",
	                "Content-Type, x-requested-with, X-Custom-Header, Request-Ajax, tetris-001");
            return;
        }
		
		JSONObject jsonResult = new JSONObject();
		
		String requestUri = request.getRequestURI();
		if("/".equals(requestUri)){
			response.sendRedirect("/web/app/login/login.html");
			return;
		}
		if(!shouldFilter(requestUri)){
			chain.doFilter(request, response);
			return;
		}
		
		String clientKey = request.getHeader(HttpConstant.HEADER_FEIGN_CLIENT);
		if(HttpConstant.HEADER_FEIGN_CLIENT_KEY.equals(clientKey)){
			chain.doFilter(request, response);
			return;
		}
		
		String token = request.getHeader(HttpConstant.HEADER_AUTH_TOKEN);
		if(token == null){
			LOG.error("----------------------------");
			LOG.error(requestUri);
			jsonResult.put("status", StatusCode.FORBIDDEN.getCode());
			jsonResult.put("message", "非法访问！");
			response.setContentType("application/json; charset=UTF-8");
			PrintWriter writer = response.getWriter();
			writer.write(jsonResult.toJSONString());
			writer.close();
		}else{
			try {
				tokenQuery.checkToken(token, TerminalType.PC_PLATFORM);
				chain.doFilter(request, response);
				return;
			} catch (Exception e) {
				try{
					tokenQuery.checkToken(token, TerminalType.QT_ZK);
					chain.doFilter(request, response);
					return;
				}catch(Exception e1) {
					LOG.error("----------------------------");
					LOG.error(requestUri);
					e.printStackTrace();
					if(e instanceof BaseException){
						BaseException bex = (BaseException)e1;
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
		}
		
	}

	private boolean shouldFilter(String uri) {
		
		//静态资源
		if(requestResouceTypeAnalyzer.isStaticResource(uri)) return false;
		
		//不拦截uri配置
		if(UriUtil.match(uri, ignorePath)) return false;
		
		return true;
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException{
		ignorePath = new ArrayListWrapper<String>().add("/login")
												   .add("/do/password/login")
												   .add("/do/phone/login")
												   .add("/do/wechat/login")
												   .add("/after/login/success")
												   .add("/index")
												   .add("/index/*")
												   .add("/user/feign/check/token")
												   .add("/api/server/media/upload")
												   .add("/user/index/personal/*")
												   .add("/api/server/cs/channel/alarm/reboot")
												   //以下路径系统中不会使用
												   //访问网关的不拦截
												   .add("/tetris-spring-eureka/*")
												   .add("/tetris-spring-zull/*")
												   .add("/tetris-menu/*")
												   .add("/tetris-user/*")
												   .add("/tetris-mims/*")
												   .add("/tetris-cms/*")
												   .add("/tetris-easy-process/*")
												   .add("/tetris-media-editor/*")
												   .add("/tetris-cs/*")
												   .add("/tetris-p2p/*")
												   .add("/tetris-resource/*")
												   .add("/tetris-bvc-business/*")
												   .add("/tetris-capacity/*")
												   .add("/tetris-zoom/*")
												   .add("/demo/*")
												   .add("/tourist/server/websocket/*")
												   .add("/server/websocket/*")
												   .add("/portal/login")
												   .add("/covid19/register/statistics/*")
												   .add("/router/*")
													.add("/taskPreview/queryPreviewUrlList")
													.add("/tetris-sts/*")
												   .getList();
	}
	
	@Override
	public void destroy() {}

}
