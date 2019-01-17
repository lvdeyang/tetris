package com.sumavision.tetris.easy.process.access.service;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.easy.process.access.service.exception.UserHasNoPermissionForServiceQueryException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserClassify;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/service")
public class ServiceController {

	@Autowired
	private UserQuery userTool;
	
	/**
	 * 获取服务类型枚举<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月18日 下午4:42:05
	 * @return Set<String> 服务类型
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/all/types")
	public Object queryAllTypes(HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		if(!UserClassify.MAINTENANCE.equals(UserClassify.valueOf(user.getClassify()))){
			throw new UserHasNoPermissionForServiceQueryException(user.getUuid(), "服务类型查询");
		}
		
		ServiceType[] values = ServiceType.values();
		
		Set<String> types = new HashSet<String>();
		for(ServiceType value:values){
			types.add(value.getName());
		}
		
		return types;
	}
	
}
