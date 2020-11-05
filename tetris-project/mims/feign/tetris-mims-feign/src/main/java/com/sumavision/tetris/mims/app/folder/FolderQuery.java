package com.sumavision.tetris.mims.app.folder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

@Component
public class FolderQuery {
	@Autowired
	private FolderRolePermissionFeign folderRolePermissionFeign;
	
	public FolderVO getById(Long id) throws Exception{
		return JsonBodyResponseParser.parseObject(folderRolePermissionFeign.getById(id), FolderVO.class);
	}
}
