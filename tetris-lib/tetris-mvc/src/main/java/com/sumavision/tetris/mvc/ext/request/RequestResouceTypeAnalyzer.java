package com.sumavision.tetris.mvc.ext.request;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class RequestResouceTypeAnalyzer {

	/** 静态资源后缀 */
	private String[] staticResourceSuffix = new String[]{
		".html", ".js", ".css", ".jpg", ".png", ".ico",
		".eot", ".svg", ".ttf", ".woff"
	};
	
	/**
	 * 判断是否请求静态资源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月13日 上午11:47:08
	 * @return 判断结果
	 */
	public boolean isStaticResource(HttpServletRequest request){
		String requestUri = request.getRequestURI();
		for(String suffix:staticResourceSuffix){
			if(requestUri.endsWith(suffix)){
				return true;
			}
		}
		return false;
	}
	
}
