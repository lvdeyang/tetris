package com.sumavision.tetris.business.role;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Component
public class BusinessRolePermissionQuery {
	@Autowired
	private BusinessRolePermissionFeign businessRolePermission;
	
	public Map<String, Object> listByUserId(
			Long userId,
			int currentPage,
			int pageSize) throws Exception{
		return JsonBodyResponseParser.parseObject(businessRolePermission.listByUserId(userId, currentPage, pageSize), Map.class);
	}
}
