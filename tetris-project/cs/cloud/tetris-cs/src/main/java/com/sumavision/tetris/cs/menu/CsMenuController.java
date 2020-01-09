package com.sumavision.tetris.cs.menu;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mims.app.media.avideo.MediaAVideoVO;
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

	/**
	 * 获取cs媒资目录树<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 * @return List<CsMenuVO> cs媒资目录树
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/tree")
	public Object listTree(Long channelId, HttpServletRequest request) throws Exception {

		return csMenuQuery.queryMenuTree(channelId);
	}
	
	/**
	 * 获取cs媒资目录树(包括目录下媒资)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 * @return List<CsMenuVO> cs媒资目录树
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/tree/with/resource")
	public Object listTreeWithResource(Long channelId, HttpServletRequest request) throws Exception {

		return csMenuQuery.queryMenuTreeWithResource(channelId);
	}

	/**
	 * 添加cs媒资根目录<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long channelId 频道id
	 * @param String name 根目录名称
	 * @return CsMenuVO cs媒资目录
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/root")
	public Object addRoot(Long channelId, String name, HttpServletRequest request) throws Exception {

		CsMenuPO menu = csMenuService.addRoot(channelId, name);

		return new CsMenuVO().set(menu);
	}
	
	/**
	 * 移动cs媒资目录<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long id 媒资目录id
	 * @param Long newParantId 新父目录id
	 * @return CsMenuVO cs媒资目录
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/move")
	public Object editMenu(Long id, Long newParentId, HttpServletRequest request) throws Exception {

		CsMenuPO menuPO = csMenuService.topPO(id, newParentId);

		return new CsMenuVO().set(menuPO);
	}

	/**
	 * 编辑cs媒资目录<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long id 目录id
	 * @param String name 目录新名称
	 * @param String remark 目录新备注
	 * @return CsMenuVO cs媒资目录
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object editMenu(Long id, String name, String remark, HttpServletRequest request) throws Exception {

		CsMenuPO menuPO = csMenuService.editPO(id, name, remark);

		return new CsMenuVO().set(menuPO);
	}

	/**
	 * 添加cs下属媒资目录<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long id 父目录id
	 * @param Long channelId 频道id
	 * @param String name 新添加的标签名
	 * @return CsMenuVO 添加的cs媒资目录
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/append")
	public Object appendMenu(Long id, Long channelId, String name, HttpServletRequest request) throws Exception {

		CsMenuPO menuPO = csMenuService.appendPO(id, channelId, name);

		return new CsMenuVO().set(menuPO);
	}
	
	/**
	 * 删除cs媒资目录<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long id 目录id
	 * @return CsMenuVO cs媒资目录
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object editMenu(Long id, HttpServletRequest request) throws Exception {

		CsMenuPO menuPO = csMenuService.removePO(id);

		return new CsMenuVO().set(menuPO);
	}
	
	/**
	 * 获取cs目录下的媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long id 目录id
	 * @return List<CsResourceVO> cs媒资列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/resource/get")
	public Object getResource(Long id, HttpServletRequest request) throws Exception {

		List<CsResourceVO> resources = resourceQuery.queryMenuResources(id);

		return resources;
	}
	
	/**
	 * 获取mims的所有音视频媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long id 目录id
	 * @return List<MediaAVideoVO> mims媒资列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/resource/get/mims")
	public Object getMIMSResource(Long id,HttpServletRequest request) throws Exception {

		List<MediaAVideoVO> resources = resourceQuery.getMIMSResources(id);

		return resources;
	}
	
	/**
	 * 向cs目录添加媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param String resourcesListStr mims媒资列表
	 * @param Long parentId cs目录id
	 * @param Long channelId 频道id 
	 * @return List<CsResourceVO> cs媒资列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/resource/add")
	public Object addResources(String resourcesListStr,Long parentId,Long channelId, HttpServletRequest request) throws Exception {

		List<MediaAVideoVO> resources = JSON.parseArray(resourcesListStr,MediaAVideoVO.class);

		return resourceService.addResources(resources, parentId,channelId);
	}
	
	/**
	 * 移除cs目录内的媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param Long id cs媒资id
	 * @return CsResourceVO cs媒资
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/resource/remove")
	public Object removeResource(Long id, HttpServletRequest request) throws Exception {
		CsResourceVO resource = resourceService.removeResource(id);
		
		return resource;
	}
}
