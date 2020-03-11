package com.sumavision.tetris.resouce.feign.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

public class ResourceService {

	@Autowired
	private ResourceFeign resourceFeign;
	
	/**
	 * 查询用户有权限的用户，终端类型用于显示在线状态<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月10日 上午8:40:17
	 * @param Long userId 用户id
	 * @param String terminalType 终端类型
	 * @return List<UserBO>
	 */
	public List<UserBO> queryUsersByUserId(Long userId, String terminalType) throws Exception{
		return JsonBodyResponseParser.parseArray(resourceFeign.queryUsersByUserId(userId, terminalType), UserBO.class);
	}
	
}
