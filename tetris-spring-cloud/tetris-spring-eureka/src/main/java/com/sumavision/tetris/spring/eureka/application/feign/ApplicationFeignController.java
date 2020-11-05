package com.sumavision.tetris.spring.eureka.application.feign;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.spring.eureka.application.ApplicationQuery;

@Controller
@RequestMapping(value = "/application/feign")
public class ApplicationFeignController {

	@Autowired
	private ApplicationQuery applicationQuery;
	
	/**
	 * 查询所有微服务实例<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月23日 下午4:37:37
	 * @return List<ApplicationVO> 实例列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/all")
	public Object findAll(HttpServletRequest request) throws Exception{
		return applicationQuery.findAll();
	}
	
	/**
	 * 根据实例id查询服务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月30日 下午1:43:17
	 * @param String instanceId 实例id
	 * @return ApplicationVO 微服务实例
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/instance/id")
	public Object findByInstanceId(
			String instanceId, 
			HttpServletRequest request) throws Exception{
		return applicationQuery.findByInstanceId(instanceId);
	}
	
}
