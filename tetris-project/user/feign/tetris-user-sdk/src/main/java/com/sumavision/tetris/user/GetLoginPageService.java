/**
 * 
 */
package com.sumavision.tetris.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

/**
 * 类型概述<br/>
 * <b>作者:</b>zhouaining<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年2月3日 上午9:09:20
 */
@Service
public class GetLoginPageService {
	@Autowired
	private GetLoginPageFeign getLoginPageFeign;
	
	public String getLoginPageString()throws Exception{
		JSONObject HTMLSting = getLoginPageFeign.getLoginPage();
		return JsonBodyResponseParser.parseObject(HTMLSting, String.class);
	}
}
