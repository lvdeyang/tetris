package com.sumavision.tetris.cs.menu;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mims.app.media.video.MediaVideoVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/cs/menu")
public class CsMenuController {
	@Autowired
	private CsMenuService csMenuService;

	@Autowired
	private CsMenuQuery csMenuQuery;
	
	@Autowired
	private CsResourceQuery resourceQuery;
	
	@Autowired
	private CsResourceService resourceService;

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/tree")
	public Object listTree(Long channelId, HttpServletRequest request) throws Exception {

		List<CsMenuVO> rootColumns = csMenuQuery.queryMenuTree(channelId);

		return rootColumns;
	}

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/root")
	public Object addRoot(Long channelId, String name, HttpServletRequest request) throws Exception {

		CsMenuPO menu = csMenuService.addRoot(channelId, name);

		return new CsMenuVO().set(menu);
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/move")
	public Object editMenu(Long id, Long newParentId, HttpServletRequest request) throws Exception {

		CsMenuPO menuPO = csMenuService.topPO(id, newParentId);

		return menuPO;
	}


	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object editMenu(Long id, String name, String remark, HttpServletRequest request) throws Exception {

		CsMenuPO menuPO = csMenuService.editPO(id, name, remark);

		return menuPO;
	}

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/append")
	public Object appendMenu(Long id, Long channelId, String name, HttpServletRequest request) throws Exception {

		CsMenuPO menuPO = csMenuService.appendPO(id, channelId, name);

		return menuPO;
	}

	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object editMenu(Long id, HttpServletRequest request) throws Exception {

		CsMenuPO menuPO = csMenuService.removePO(id);

		return menuPO;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/resource/get")
	public Object getResource(Long id, HttpServletRequest request) throws Exception {

		List<CsResourceVO> resources = resourceQuery.queryMenuResources(id);

		return resources;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/resource/get/mims")
	public Object getMIMSResource(Long id,HttpServletRequest request) throws Exception {

		List<MediaVideoVO> resources = resourceQuery.getMIMSResources(id);

		return resources;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/resource/add")
	public Object addResources(String resourcesListStr,Long parentId,Long channelId, HttpServletRequest request) throws Exception {

		List<MediaVideoVO> resources = JSON.parseArray(resourcesListStr,MediaVideoVO.class);

		return resourceService.addResources(resources, parentId,channelId);
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/resource/remove")
	public Object removeResource(Long id, HttpServletRequest request) throws Exception {
		CsResourceVO resource = resourceService.removeResource(id);
		
		return resource;
	}
}
