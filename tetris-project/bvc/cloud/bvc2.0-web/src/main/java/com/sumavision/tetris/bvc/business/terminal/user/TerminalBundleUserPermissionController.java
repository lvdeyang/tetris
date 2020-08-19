package com.sumavision.tetris.bvc.business.terminal.user;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/tetris/bvc/business/terminal/bundle/user/permission")
public class TerminalBundleUserPermissionController {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private TerminalBundleUserPermissionQuery terminalBundleUserPermissionQuery;
	
	@Autowired
	private TerminalBundleUserPermissionService terminalBundleUserPermissionService;
	
	/**
	 * 根据公司和条件查询用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月13日 上午11:05:41
	 * @param String nickname 用户昵称
	 * @param String userno 用户号码
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return int total 用户总量
	 * @return List<UserVO> rows 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/users")
	public Object loadUsers(
			String nickname, 
			String userno, 
			int currentPage, 
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		return userQuery.findByCompanyIdAndCondition(Long.valueOf(user.getGroupId()), nickname, userno, currentPage, pageSize);
	}
	
	/**
	 * 查询用户为终端设备绑定的真实设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月10日 上午11:04:21
	 * @param String userId 用户id
	 * @param Long terminalId 终端id
	 * @param Long terminalBundleId 终端设备id
	 * @return TerminalBundleUserPermissionVO 真实设备信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			String userId,
			Long terminalId,
			Long terminalBundleId,
			HttpServletRequest request) throws Exception{
		
		return terminalBundleUserPermissionQuery.load(userId, terminalId, terminalBundleId);
	}
	
	/**
	 * 根据类型查询设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月10日 下午1:42:33
	 * @param String bundleName 设备名称
	 * @param String deviceModel 设备类型
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return total long 总数据量
	 * @return rows  List<JSONObject> 设备列表 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/bundles")
	public Object loadBundles(
			String bundleName,
			String deviceModel,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		return terminalBundleUserPermissionQuery.loadBundles(bundleName, deviceModel, currentPage, pageSize);
	}
	
	/**
	 * 添加用户设备绑定<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月10日 上午11:13:08
	 * @param String userId 用户id
	 * @param Long terminalId 终端id
	 * @param Long terminalBundleId 终端设备id
	 * @param String bundleType 设备类型
	 * @param String bundleId 设备id
	 * @param String bundleName 设备名称
	 * @return TerminalBundleUserPermissionVO 真实设备信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String userId,
			Long terminalId,
			Long terminalBundleId,
			String bundleType,
			String bundleId,
			String bundleName,
			HttpServletRequest request) throws Exception{
		
		return terminalBundleUserPermissionService.add(userId, terminalId, terminalBundleId, bundleType, bundleId, bundleName);
	}
	
	/**
	 * 修改用户设备绑定<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月10日 上午11:13:08
	 * @param Long id 用户绑定设备id
	 * @param String bundleType 设备类型
	 * @param String bundleId 设备id
	 * @param String bundleName 设备名称
	 * @return TerminalBundleUserPermissionVO 真实设备信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			Long id,
			String bundleType,
			String bundleId,
			String bundleName,
			HttpServletRequest rquest) throws Exception{
		
		return terminalBundleUserPermissionService.edit(id, bundleType, bundleId, bundleName);
	}
	
	/**
	 * 删除用户设备绑定<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月10日 上午11:23:25
	 * @param Long id 用户绑定设备id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(Long id) throws Exception{
		
		terminalBundleUserPermissionService.delete(id);
		return null;
	}
	
}
