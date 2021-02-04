/**
 * 
 */
package com.sumavision.tetris.user.feign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.GetLoginPageService;

/**
 * 获取登陆页面<br/>
 * <b>作者:</b>zhouaining<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年2月3日 上午8:45:19
 */
@Controller
@RequestMapping(value = "/getLoginPage/feign")
public class GetLoginPageFeignController {
	@Autowired
	private GetLoginPageService getLoginPageService;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/loginPage/get")
	public Object getLoginPage() throws Exception{

		String HTMLString = getLoginPageService.getLoginPageString();
		return HTMLString;
	}
}
