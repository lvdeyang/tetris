package com.sumavision.tetris.auth.filter;

import java.util.List;
import org.springframework.stereotype.Component;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;

@Component
public class FilterValidate {

	/** 不拦截路径 */
	private List<String> ignorePath = null;
	
	/** 静态资源 */
	private List<String> staticResource = null;
	
	public FilterValidate(){
		ignorePath = new ArrayListWrapper<String>().add("/login")
												   .add("/do/password/login")
												   .add("/do/phone/login")
												   .add("/do/wechat/login")
												   .add("/after/login/success")
												   .add("/index")
												   .add("/index/*")
												   .add("/user/feign/check/token")
												   .add("/user/feign/find/by/token")
												   .add("/login/feign/do/password/login")
												   .add("/login/feign/query/redirect/url")
												   .add("/mims/server/props/feign/query/props")
												   .add("/cms/server/props/feign/query/props")
												   .add("/user/server/props/feign/query/props")
												   .add("/menu/server/props/feign/query/props")
												   .getList();
		staticResource = new ArrayListWrapper<String>().add(".js")
													   .add(".html")
													   .add(".css")
													   .add(".png")
													   .add(".jpg")
													   .add(".jsp")
													   .add(".ttf")
													   .add(".eot")
													   .add(".svg")
													   .add(".woff")
													   .add(".ico")
											   		   .getList();
	}
	
	/**
	 * 判断请求资源是否是静态资源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月6日 上午11:30:08
	 * @param String uri 请求uri
	 * @return boolean 判断结果
	 */
	public boolean isStaticResource(String uri){
		for(String resource:staticResource){
			if(uri.endsWith(resource)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断是否是不拦截请求<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月6日 上午11:30:48
	 * @param String uri 请求路径
	 * @return boolean 判断结果
	 */
	public boolean isIgnorePath(String uri){
		for(String patten:ignorePath){
			if(patten.equals(uri)){
				return true;
			}else{
				if(patten.endsWith("/*")){
					patten = patten.replace("/*", "");
					if(uri.startsWith(patten)){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 判断是否要进行拦截<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月6日 上午11:32:02
	 * @param String uri 请求uri
	 * @return boolean 判断结果
	 */
	public boolean canDoFilter(String uri){
		if(isStaticResource(uri)) return false;
		if(isIgnorePath(uri)) return false;
		return true;
	}
	
}
