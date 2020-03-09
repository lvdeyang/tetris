package com.sumavision.tetris.resouce.feign.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

public class ResourceService {

	@Autowired
	private ResourceFeign resourceFeign;
	
	public List<UserBO> queryUsersByUserId(Long userId, String terminalType) throws Exception{
		return JsonBodyResponseParser.parseArray(resourceFeign.queryUsersByUserId(userId, terminalType), UserBO.class);
	}
	
}
