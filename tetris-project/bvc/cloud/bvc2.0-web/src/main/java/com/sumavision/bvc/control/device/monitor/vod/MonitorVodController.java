package com.sumavision.bvc.control.device.monitor.vod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.dao.FolderDao;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.pojo.FolderPO.FolderType;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.control.device.group.vo.tree.TreeNodeVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.device.group.bo.FolderBO;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/monitor/vod")
public class MonitorVodController {

	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private ResourceService resourceService; 
	
	@Autowired
	private FolderDao folderDao;
	
	@RequestMapping(value = "/index")
	public ModelAndView index(String token){
		
		ModelAndView mv = new ModelAndView("web/bvc/monitor/vod/vod");
		mv.addObject("token", token);
		
		return mv;
	}
	
	/**
	 * 查询点播资源树<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月25日 上午11:56:38
	 * @return List<TreeNodeVO> 点播资源树
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/resource/tree")
	public Object queryResourceTree(HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		Set<Long> folderIds = new HashSet<Long>();
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		
		//查询有权限的点播资源
		List<JSONObject> entities = resourceService.queryOnDemandResourceByUserId(userId);
		List<VodResourceVO> resources = new ArrayList<VodResourceVO>();
		if(entities==null || entities.size()<=0) return _roots;
		for(JSONObject entity:entities){
			resources.add(new VodResourceVO().setId(entity.getString("resourceId"))
											 .setName(entity.getString("name"))
											 .setPreviewUrl(entity.getString("previewUrl"))
											 .setUserId(entity.getString("userId"))
											 .setFolderId(entity.getLong("folderId")));
		}
		
		if(resources==null || resources.size()<=0) return _roots;
		
		for(VodResourceVO resource:resources){
			if(resource.getFolderId() != null) folderIds.add(resource.getFolderId());
		}
		
		List<FolderPO> foldersTree = resourceQueryUtil.queryFoldersTree(folderIds);
		if(foldersTree != null && foldersTree.size()>0){
			for(FolderPO allFolderPO: foldersTree){
				FolderBO folderBO = new FolderBO().set(allFolderPO);					  						  
				folders.add(folderBO);			
			}
		}
		
		//找所有的根
		List<FolderBO> roots = findRoots(folders);
		
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(_root);
			recursionFolder(_root, folders, resources);
		}
		
		return _roots;
	}
	
	/**
	 * 编辑页面获取点播资源树<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月28日 下午5:18:37
	 * @return List<TreeNodeVO> 点播资源树
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/resource/tree/for/edit")
	public Object queryResourceTreeForEdit(HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		List<FolderBO> folders = new ArrayList<FolderBO>();
		List<TreeNodeVO> _roots = new ArrayList<TreeNodeVO>();
		
		//查询有权限的点播资源 TODO
		List<VodResourceVO> resources = new ArrayList<VodResourceVO>();
		
		List<JSONObject> resourceList = resourceService.queryOnDemandResourceCreatedByUser(userId);
		
		List<FolderPO> foldersTree = folderDao.findByFolderType(FolderType.ON_DEMAND);
		
		for(JSONObject resource: resourceList){
			resources.add(new VodResourceVO().setId(resource.getString("resourceId"))
											 .setName(resource.getString("name"))
											 .setPreviewUrl(resource.getString("previewUrl"))
											 .setUserId(resource.getString("userId"))
											 .setFolderId(resource.getLong("folderId")));
		}
		
		if(foldersTree != null && foldersTree.size()>0){
			for(FolderPO allFolderPO: foldersTree){
				FolderBO folderBO = new FolderBO().set(allFolderPO);					  						  
				folders.add(folderBO);			
			}
		}
		
		//找所有的根
		List<FolderBO> roots = findRoots(folders);
		
		for(FolderBO root:roots){
			TreeNodeVO _root = new TreeNodeVO().set(root)
											   .setChildren(new ArrayList<TreeNodeVO>());
			_roots.add(_root);
			recursionFolder(_root, folders, resources);
		}
		
		return _roots;
	}
	
	/**
	 * 递归资源文件夹树<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月25日 上午11:54:54
	 * @param TreeNodeVO root 根文件夹
	 * @param List<FolderBO> folders 文件夹树
	 * @param List<VodResourceVO> resources 点播资源
	 */
	public void recursionFolder(
			TreeNodeVO root, 
			List<FolderBO> folders, 
			List<VodResourceVO> resources){
		
		//往里装文件夹
		for(FolderBO folder:folders){
			if(folder.getParentId()!=null && folder.getParentId().toString().equals(root.getId())){
				TreeNodeVO folderNode = new TreeNodeVO().set(folder)
														.setChildren(new ArrayList<TreeNodeVO>());
				root.getChildren().add(folderNode);
				recursionFolder(folderNode, folders, resources);
			}
		}
		
		//往里装点播资源
		for(VodResourceVO resource:resources){
			if(resource.getFolderId().toString().equals(root.getId())){
				TreeNodeVO resourceNode = new TreeNodeVO().set(resource);
				root.getChildren().add(resourceNode);
			}
		}
	}
	
	/**
	 * 查找根节点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月17日 下午1:32:53
	 * @param List<FolderBO> folders 查找范围
	 * @return List<FolderBO> 根节点列表
	 */
	private List<FolderBO> findRoots(List<FolderBO> folders){
		List<FolderBO> roots = new ArrayList<FolderBO>();
		for(FolderBO folder:folders){
			if(folder!=null && (folder.getParentId()==null || folder.getParentId()==TreeNodeVO.FOLDERID_ROOT)){
				roots.add(folder);
			}
		}
		return roots;
	}
	
	/**
	 * 添加点播资源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月30日 上午9:24:22
	 * @param Long folderId 点播资源文件夹id
	 * @param JSONString resources 点播资源列表
	 * 			{name:点播资源名称, previewUrl:点播资源预览地址}
	 * @return List<TreeNodeVO> 树节点
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			Long folderId,
			String resources,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		List<JSONObject> resourceObjects = JSONArray.parseArray(resources, JSONObject.class);
		
		//向资源添加资源 TODO
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		for(JSONObject resource: resourceObjects){
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name", resource.getString("name"));
			jsonObject.put("previewUrl", resource.getString("previewUrl"));
			jsonObject.put("userId", userId);		
			jsonObjects.add(jsonObject);
		}

		List<JSONObject> entities = resourceService.setFolderIdToOnDemandResource(folderId, jsonObjects);
		
		List<VodResourceVO> rows = new ArrayList<VodResourceVO>();
		if(entities!=null && entities.size()>0){
			for(JSONObject entity:entities){
				rows.add(new VodResourceVO().setId(entity.getString("resourceId"))
														 .setName(entity.getString("name"))
														 .setPreviewUrl(entity.getString("previewUrl"))
														 .setUserId(entity.getString("userId"))
														 .setFolderId(entity.getLong("folderId")));
			}
		}
		
		List<TreeNodeVO> ndoes = new ArrayList<TreeNodeVO>();
		for(VodResourceVO row:rows){
			ndoes.add(new TreeNodeVO().set(row));
		}

		return ndoes;
	}
	
	/**
	 * 删除点播资源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月30日 上午9:24:22
	 * @param String resourceIds 点播资源id列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(
			String resourceIds,
			HttpServletRequest request) throws Exception{
		
		List<String> ids = JSONArray.parseArray(resourceIds, String.class);
		
		//向资源删除资源
		resourceService.deleteOnDemandResource(ids);
		
		return null;
	}
	
}
