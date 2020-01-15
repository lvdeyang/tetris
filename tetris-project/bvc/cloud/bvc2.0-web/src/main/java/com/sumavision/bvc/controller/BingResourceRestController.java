package com.sumavision.bvc.controller;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.ResourceIdListBO;
import com.suma.venus.resource.base.bo.ResultBO;
import com.suma.venus.resource.base.bo.UnbindResouceBO;
import com.suma.venus.resource.base.bo.UnbindUserPrivilegeBO;
import com.suma.venus.resource.base.bo.UserAndResourceIdBO;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.feign.UserQueryFeign;
import com.sumavision.aop.annotation.WebLog;
import com.sumavision.bvc.common.RestResult;
import com.sumavision.bvc.common.RestResultGenerator;
import com.sumavision.bvc.vo.BindingResourceVO;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/api/resourcePrivilege")
@Slf4j
public class BingResourceRestController {

	@Autowired
	UserQueryFeign userRomoteService;

	@RequestMapping(value = "queryAllUser")
	@ResponseBody
	public RestResult<String> queryUsers() {
		Map<String, List<UserBO>> resultMap = userRomoteService.queryUsers();
		JSONArray userArray = JSONArray.parseArray(JSON.toJSONString(resultMap.get("users")));
		JSONObject retDataJson = new JSONObject();
		retDataJson.put("users", userArray.toString());
		return RestResultGenerator.genResult(true, retDataJson.toString(), "ok");
	}

	@WebLog
	@RequestMapping(value = "bindingResource")
	@ResponseBody
	public RestResult<String> bindingResource(@RequestBody BindingResourceVO bindingResourceVO) {
		log.debug("-----------------binding-------------");
		List<Long> userIds = bindingResourceVO.getUserIds();
		List<String> resourceIds = bindingResourceVO.getResourceIds();
		List<ResultBO> results = new ArrayList<>();
		for (Long userId : userIds) {
			UserAndResourceIdBO userAndResourceIds = new UserAndResourceIdBO();
			userAndResourceIds.setUserId(userId);
			userAndResourceIds.setResourceCodes(resourceIds);
			results.add(userRomoteService.bindUserPrivilege(userAndResourceIds));
		}
		return RestResultGenerator.genResult(true, results.toString(), "ok");
	}

	@WebLog
	@RequestMapping(value = "unbindingResource")
	@ResponseBody
	public RestResult<String> bingResources(@RequestBody BindingResourceVO bindingResourceVO) {
		List<Long> userIds = bindingResourceVO.getUserIds();
		List<String> unbindResourceIds = bindingResourceVO.getResourceIds();
		List<ResultBO> results = new ArrayList<>();
		for (Long userId : userIds) {
			ResourceIdListBO resourceIdListBO = userRomoteService.queryResourceByUserId(userId);
		    List<String> resourceCodes = resourceIdListBO.getResourceCodes();
			List<UnbindResouceBO> unBindResoureceBo = resourceCodes.stream()
																   .filter(i-> !"".equals(i))//过滤掉buildId为空值
																   .map(i ->genernateUnbindBO(i,unbindResourceIds))
																   .collect(toList());
			UnbindUserPrivilegeBO unbindUserPrivilegeBO = new UnbindUserPrivilegeBO();
			unbindUserPrivilegeBO.setUserId(userId);
			unbindUserPrivilegeBO.setUnbindPrivilege(unBindResoureceBo);
			results.add(userRomoteService.unbindUserPrivilege(unbindUserPrivilegeBO));
		}
		return RestResultGenerator.genResult(true, results.toString(), "ok");
	}
	
	private UnbindResouceBO genernateUnbindBO(String resourceCode,List<String> unbindResources) {
		UnbindResouceBO unbindResouceBO = new UnbindResouceBO();
		unbindResouceBO.setResourceCode(resourceCode);
		unbindResouceBO.setbDelete(unbindResources.contains(resourceCode));
		return unbindResouceBO;
	}
}
