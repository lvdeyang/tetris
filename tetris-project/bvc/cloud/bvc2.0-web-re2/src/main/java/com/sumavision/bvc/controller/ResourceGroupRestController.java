package com.sumavision.bvc.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suma.venus.resource.base.bo.BundleBody;
import com.suma.venus.resource.pojo.FolderPO;
import com.suma.venus.resource.service.BundleService;
import com.suma.venus.resource.service.FolderService;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.common.RestResult;
import com.sumavision.bvc.common.RestResultGenerator;
import com.sumavision.bvc.feign.ResourceRemoteService;
import com.sumavision.bvc.repository.ResourceRespository;
import com.sumavision.bvc.vo.FolderVO;
import com.sumavision.bvc.vo.JoiningGroupVO;
import com.sumavision.bvc.vo.tree.ResourceTreeGenerater;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/api/resourceGroup")
@Slf4j
public class ResourceGroupRestController {
	@Autowired
	ResourceRespository resourceRespository;

	@Autowired
	private ResourceService resourceService;

	@Autowired
	private FolderService folderService;

	@Autowired
	private BundleService bundleService;

	@Autowired
	ResourceRemoteService resourceRemoteService;

	
	//获取分组列表
	@RequestMapping("/queryAll")
	@ResponseBody
	public RestResult<String> queryAll() {
		// TODO
		Long userId = 1l;
		// todo-查询出所有分组
		List<FolderPO> folders = resourceService.queryAllFolders();
		List<BundleBody> bundles = resourceService.queryBundles(null);
		ResourceTreeGenerater generater = new ResourceTreeGenerater(folders, bundles);
		String treeString = null;
		Long maxexpandId = null;
		try {
			treeString = generater.loadTree();
			maxexpandId = generater.findMaxGroupId();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			RestResultGenerator.genErrorResult(e.getMessage());
		}
		JSONObject retDataJson = new JSONObject();
		retDataJson.put("maxexpandId", maxexpandId);
		retDataJson.put("treelist", treeString);
		return RestResultGenerator.genResult(true, retDataJson.toString(), "ok");
	}

	// 添加资源分组
	@RequestMapping("/add")
	@ResponseBody
	public RestResult<String> addgroup(FolderVO folderVO) {
		List<FolderPO> pos = null;
		// pos = null;
		// todo-查询分组
		// folderService.save(po);
		// List<String> res = resourceService.queryBundles(condition);
		return RestResultGenerator.genSuccessResult();
	}

	@RequestMapping("/joinGroup")
	@ResponseBody
	public RestResult<String> joingroup(JoiningGroupVO joiningGroupVO) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(joiningGroupVO.getFolderJsonString());
		FolderVO folder = (FolderVO) JSONObject.toBean(jsonObject, FolderVO.class);
		FolderPO folderPo = folderService.get((folder.getGroupId()));
		Long folderId = (folderPo == null) ? saveFolder(folder) : folder.getGroupId();
		Boolean joinResult = false;
		try {
			 joinResult = resourceService.setFolderToBundles(folderId, Arrays.asList(joiningGroupVO.getResourceIds()));
		} catch (Exception e) {
			log.error("add group failed");
			return RestResultGenerator.genErrorResult(e.getMessage());
		}
		return joinResult ? RestResultGenerator.genSuccessResult()
					 	  :RestResultGenerator.genErrorResult("添加失败");
	}

	private Long saveFolder(FolderVO folder) {
		FolderPO folderPO = new FolderPO();
		folderPO.setName(folder.getName());
		folderPO.setParentId(folder.getParentId());
		folderService.save(folderPO);
		return folderPO.getId();
	}

	
	//用分组里删除资源
	@RequestMapping("/deJoinGroup")
	@ResponseBody
	public RestResult<String> delete(String bundle_Id) {
		Boolean deJoinResult = false;
		try {
			deJoinResult = resourceService.clearFolderIdOfBundles(Arrays.asList(bundle_Id));
		} catch (Exception e) {
			RestResultGenerator.genErrorResult(e.getMessage());
		}
		return deJoinResult?RestResultGenerator.genSuccessResult():RestResultGenerator.genErrorResult("删除失败");
	}

	//根据分组查资源
	@RequestMapping("/getResource/{groupId}")
	@ResponseBody
	public RestResult<List<String>> getResourceByGroup(@PathVariable("groupId") String groupId, String param) {
		// String bundle = JSON.toJSONString(groupVO);
		// todo-查询分组
		List<String> res = null;
		// List<String> res = resourceService.queryBundles(condition);
		res.addAll(res);
		return RestResultGenerator.genSuccessResult(res);
	}

	//删除分组
	@RequestMapping("/deleteGroup/{groupId}/{resourceId}")
	@ResponseBody
	public RestResult<List<String>> deleteGroup(@PathVariable("groupId") String groupId,
			@PathVariable("resourceId") String resourceId, String param) {
		// String bundle = JSON.toJSONString(groupVO);
		// todo-查询分组
		List<String> res = null;
		// List<String> res = resourceService.queryBundles(condition);
		res.addAll(res);
		return RestResultGenerator.genSuccessResult(res);
	}

}
