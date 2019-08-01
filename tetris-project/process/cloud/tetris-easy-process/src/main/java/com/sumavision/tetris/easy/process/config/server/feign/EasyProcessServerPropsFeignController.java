package com.sumavision.tetris.easy.process.config.server.feign;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sumavision.tetris.easy.process.config.server.EasyProcessServerPropsQuery;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/easy/process/server/props/feign")
public class EasyProcessServerPropsFeignController {

	@Autowired
	private EasyProcessServerPropsQuery easyProcessServerPropsQuery;
	
	/**
	 * 查询媒资服务属性<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月4日 下午3:11:36
	 * @return ServerProps 服务属性
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/props")
	public Object queryProps(HttpServletRequest request) throws Exception{
		return easyProcessServerPropsQuery.queryProps();
	}
	
}
