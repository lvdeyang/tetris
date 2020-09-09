package com.sumavision.tetris.mims.app.operation.mediaPackage.userPermission;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/operation/package/user/permission")
public class OperationPackageUserPermissionController {
	@Autowired
	private OperationPackageUserPermissionService userPermissionService;
	
	@Autowired
	private OperationPackageUserPermissionQuery userPermissionQuery;
	
	/**
	 * 添加用户套餐绑定关系<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月25日 下午3:52:17
	 * @param Long userId 用户id
	 * @param Long packageId 套餐id
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object addPermission(Long userId, Long packageId, HttpServletRequest request) throws Exception {
		return userPermissionService.addPermission(userId, packageId);
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/list")
	public Object addPermissionList(Long userId, String packageIds, HttpServletRequest request) throws Exception {
		if (packageIds == null || packageIds.isEmpty()) return null;
		List<Long> packageIdList = JSONArray.parseArray(packageIds, Long.class);
		return userPermissionService.addPermissions(userId, packageIdList);
	}
	
	/**
	 * 根据用户查询套餐绑定关系<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月3日 下午5:11:59
	 * @param Long userId 用户id
	 * @return List<OperationPackageUserPermissionVO> 用户套餐关联关系
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/permission/by/user")
	public Object queryPermissionByUserId(Long userId, HttpServletRequest request) throws Exception {
		return userPermissionQuery.queryPermissionByUserId(userId);
	}
	
	/**
	 * 查询用户绑定的套餐列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月3日 下午5:12:54
	 * @param Long userId 用户id
	 * @return List<OperationPackageVO> 绑定的套餐列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/package/by/user")
	public Object queryPackageByUserId(Long userId, HttpServletRequest request) throws Exception {
		return userPermissionQuery.queryPackageByUserId(userId);
	}
	
	/**
	 * 删除套餐绑定关系<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月3日 下午5:13:51
	 * @param Long userId 用户id
	 * @param Long packageId 套餐id
	 * @return OperationPackageUserPermissionVO 关联关系
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object removeBind(Long userId, Long packageId, HttpServletRequest request) throws Exception {
		userPermissionService.removePermission(userId, packageId);
		return null;
	}
	
	/**
	 * 删除套餐绑定关系<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月3日 下午5:13:51
	 * @param Long userId 用户id
	 * @param Long packageId 套餐id
	 * @return OperationPackageUserPermissionVO 关联关系
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/{id}")
	public Object removeBindById(@PathVariable Long id, Long packageId, HttpServletRequest request) throws Exception {
//		userPermissionService.removePermissionById(id);
		userPermissionService.finish(id);
		return null;
	}
}
