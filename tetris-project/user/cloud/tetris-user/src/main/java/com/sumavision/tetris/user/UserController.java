package com.sumavision.tetris.user;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.file.FileUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.config.server.ServerProps;
import com.sumavision.tetris.config.server.UserServerPropsQuery;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.MultipartHttpServletRequestWrapper;
import com.sumavision.tetris.system.role.SystemRoleDAO;
import com.sumavision.tetris.system.role.SystemRolePO;
import com.sumavision.tetris.system.role.SystemRoleType;
import com.sumavision.tetris.system.role.UserSystemRolePermissionDAO;
import com.sumavision.tetris.system.role.UserSystemRolePermissionPO;
import com.sumavision.tetris.user.event.UserImportEventPublisher;
import com.sumavision.tetris.user.exception.UserNotExistException;

@Controller
@RequestMapping(value = "/user")
public class UserController {
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private UserDAO userDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserServerPropsQuery userServerPropsQuery;
	
	@Autowired
	private UserSystemRolePermissionDAO userSystemRolePermissionDao;
	
	@Autowired
	private SystemRoleDAO systemRoleDao;
	
	@Autowired
	private UserImportInfoDAO userImportInfoDao;
	
	/**
	 * 查询枚举类型<br/>
	 * <p>
	 *   查询用户分类<br/>
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月24日 上午9:26:40
	 * @return Set<String> classifies 用户分类
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/types")
	public Object queryTypes(HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		Set<String> values = new HashSet<String>();
		UserClassify[] classifies = UserClassify.values();
		for(UserClassify classify:classifies){
			if(classify.isShow()){
				values.add(classify.getName());
			}
		}
		
		return values;
	}
	
	/**
	 * 分页查询用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月19日 上午8:29:31
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<UserVO> 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object list(
			int currentPage,
			int pageSize,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		return userQuery.list(currentPage, pageSize);
	}
	
	/**
	 * 根据条件查询用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月13日 上午11:05:41
	 * @param String nickname 用户昵称
	 * @param String userno 用户号码
	 * @return int total 用户总量
	 * @return List<UserVO> rows 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/condition")
	public Object findByCondition(
			String nickname,
			String userno,
			int currentPage,
			int pageSize,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		
		return userQuery.findByCondition(nickname, userno, currentPage, pageSize);
	}
	
	/**
	 * 分页查询用户（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月23日 下午5:33:31
	 * @param JSONString except 例外用户id列表
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return int total 总数据量
	 * @return List<UserVO> rows 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/with/except")
	public Object listWithExceptIds(
			String except,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		if(except == null){
			return userQuery.list(currentPage, pageSize);
		}else{
			List<Long> exceptIds = JSON.parseArray(except, Long.class);
			return userQuery.listWithExcept(exceptIds, currentPage, pageSize);
		}
		
	}
	
	/**
	 * 分页查询公司下的用户列表（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月25日 上午11:17:57
	 * @param Long companyId 公司id
	 * @param JSONString except 例外用户id列表
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return int total 用户总量
	 * @return List<UserVO> rows 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/company/user/with/except")
	public Object listCompanyUserWithExceptIds(
			String except,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		Long companyId = Long.valueOf(user.getGroupId());
		List<Long> exceptIds = null;
		if(except != null){
			exceptIds = JSON.parseArray(except, Long.class);
		}
		return userQuery.listByCompanyIdWithExcept(companyId, exceptIds, currentPage, pageSize);
	}
	
	/**
	 * 分页查询公司下的用户列表（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月25日 上午11:17:57
	 * @param Long companyId 公司id
	 * @param JSONString except 例外用户id列表
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return int total 用户总量
	 * @return List<UserVO> rows 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list/by/{companyId}/with/except")
	public Object listByCompanyIdWithExcept(
			@PathVariable Long companyId,
			String except,
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		List<Long> exceptIds = null;
		if(except != null){
			exceptIds = JSON.parseArray(except, Long.class);
		}
		return userQuery.listByCompanyIdWithExcept(companyId, exceptIds, currentPage, pageSize);
	}
	
	/**
	 * 锁定用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月24日 下午3:49:20
	 * @param @PathVariable Long id 用户id
	 * @return UserVO 用户
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/lock/{id}")
	public Object lock(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		return userService.lock(id);
	}
	
	/**
	 * 解除锁定用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月24日 下午3:49:20
	 * @param @PathVariable Long id 用户id
	 * @return UserVO 用户
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/unlock/{id}")
	public Object unlock(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		return userService.unlock(id);
	}
	
	/**
	 * 添加一个用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月19日 下午3:55:30
	 * @param String nickname 昵称
	 * @param String username 用户名
	 * @param String password 密码
	 * @param String repeat 密码确认
	 * @param String mobile 手机号
	 * @param String mail 邮箱
	 * @param Integer level 用户级别
	 * @param Long companyId 公司id
	 * @param String companyName 公司名称
	 * @return UserVO 用户数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String nickname,
            String username,
            String userno,
            String password,
            String repeat,
            String mobile,
            String mail,
            Integer level,
            String classify,
            Long companyId,
            String companyName) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		if(classify.equals(UserClassify.NORMAL.getName())){
			return userService.add(nickname, username, userno, password, repeat, mobile, mail, level, classify, true);
		}else if(classify.equals(UserClassify.COMPANY.getName())){
			if(companyId!=null && companyName==null){
				return userService.add(nickname, username, userno, password, repeat, mobile, mail, level, classify, companyId);
			}else if(companyName!=null && companyId==null){
				return userService.add(nickname, username, userno, password, repeat, mobile, mail, level, classify, companyName);
			}
		}
		return null;
	}
	
	/**
	 * 删除一个用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月19日 下午4:39:20
	 * @param @PathVariable id 用户id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/{id}")
	public Object delete(
			@PathVariable Long id, 
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		userService.delete(id);
		
		return null;
	}
	
	/**
	 * 修改一个用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月19日 下午4:54:11
	 * @param @PathVariable Long id 用户id
	 * @param String nickname 昵称
	 * @param String mobile 手机号
	 * @param String mail 邮箱
	 * @param Integer level 用户级别
	 * @param boolean editPassword 是否修改密码
	 * @param String oldPassword 旧密码
	 * @param String newPassword 新密码
	 * @param String repeat 重复新密码
	 * @return UserVO 修改后的数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/{id}")
	public Object edit(
			@PathVariable Long id,
			String nickname,
            String mobile,
            String mail,
            Integer level,
            String tags,
            boolean editPassword,
            String oldPassword,
            String newPassword,
            String repeat) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		
		return userService.edit(id, nickname, mobile, mail, level, tags, editPassword, oldPassword, newPassword, repeat);
	}
	
	/**
	 * 根据公司和条件查询用户<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月13日 上午11:05:41
	 * @param String nickname 用户昵称
	 * @param String userno 用户号码
	 * @return int total 用户总量
	 * @return List<UserVO> rows 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/company/id/and/condition")
	public Object findByCompanyIdAndCondition(
			String nickname, 
			String userno, 
			int currentPage, 
			int pageSize) throws Exception{
		
		UserVO user = userQuery.current();
		
		return userQuery.findByCompanyIdAndCondition(Long.valueOf(user.getGroupId()), nickname, userno, currentPage, pageSize);
	}
	
	/**
	 * 分页查询用户<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月19日 上午8:29:31
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return List<UserVO> 用户列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/subordinate/list")
	public Object listBySubordinate(
			int currentPage,
			int pageSize,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		UserVO user = userQuery.current();
		
		//TODO 权限校验
		if(user.getId() != null){
			return userQuery.list(currentPage, pageSize,user.getId());
		}else if(user.getUuid() != null){
			return userQuery.list(currentPage, pageSize,Long.parseLong(user.getUuid()));
		}else {
			return null;
		}
	}
	
	/**
	 * 查询当前用户详细信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月19日 上午8:29:31
	 * @return UserVO 用户
	 * @throws Exception 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query")
	public Object query(HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		List<UserVO> userInfo = userQuery.findByIdIn(new ArrayListWrapper<Long>().add(user.getId()).getList());
		
		if (userInfo == null || userInfo.isEmpty()) throw new UserNotExistException(user.getId()); 
		
		return userInfo.get(0);
	}
	
	/**
	 * 重定向到个人中心<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月4日 下午3:11:36
	 * @throws Exception 
	 */
	@RequestMapping(value = "/index/personal/{token}")
	public void queryPersonalUrl(@PathVariable String token, HttpServletRequest request, HttpServletResponse response) throws Exception{
		ServerProps serverProps = userServerPropsQuery.queryProps();
		
		StringBufferWrapper redirectUrl = new StringBufferWrapper().append("http://")
				   //TODO serverIp.properties
				   //.append(serverProps.getIp())
				   .append(serverProps.getIpFromProperties())
				   .append(":")
				   .append(serverProps.getPort())
				   .append("/")
				   .append("index/")
				   .append(token)
				   .append("#/page-personal");
		
		response.sendRedirect(redirectUrl.toString());
	}
	
	/**
	 * 导出用户数据<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月19日 上午8:41:20
	 * @return blob 用户角色关联表 csv
	 */
	@RequestMapping(value = "/handle/export")
	public ResponseEntity<byte[]> handleExport(HttpServletRequest request) throws Exception{
		
		UserVO self = userQuery.current();
		
		StringBufferWrapper csv = new StringBufferWrapper().append(UserPO.head()).append("\n");
		
		List<UserPO> users = userDao.findByCompanyId(Long.valueOf(self.getGroupId()));
		if(users!=null && users.size()>0){
			List<Long> userIds = new ArrayList<Long>();
			for(UserPO user:users){
				if(user.getId().equals(self.getId())) continue;
				userIds.add(user.getId());
			}
			List<UserSystemRolePermissionPO> permissions = userSystemRolePermissionDao.findByUserIdInAndRoleType(userIds, SystemRoleType.BUSINESS);
			List<SystemRolePO> roles = null;
			List<Long> roleIds = new ArrayList<Long>();
			if(permissions!=null && permissions.size()>0){
				for(UserSystemRolePermissionPO permission:permissions){
					roleIds.add(permission.getRoleId());
				}
				roles = systemRoleDao.findByIdIn(roleIds);
			}
			for(UserPO user:users){
				if(user.getId().equals(self.getId())) continue;
				StringBufferWrapper roleInfo = new StringBufferWrapper();
				boolean hasRoleInfo = false;
				if(permissions!=null && permissions.size()>0){
					for(UserSystemRolePermissionPO permission:permissions){
						if(user.getId().equals(permission.getUserId())){
							for(SystemRolePO role:roles){
								if(permission.getRoleId().equals(role.getId())){
									roleInfo.append(role.getName()).append("#");
									hasRoleInfo = true;
									break;
								}
							}
						}
					}
				}
				csv.append(user.row(hasRoleInfo?roleInfo.toString().substring(0, roleInfo.toString().length()-1):"")).append("\n");
			}
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", new MediaType("application", "octet-stream", Charset.forName("utf-8")).toString());
        headers.add("Content-Disposition", "attchement;filename=user.csv");
        byte[] csvBytes = csv.toString().getBytes();
        byte[] utf8Bytes = new byte[csvBytes.length+3];
        utf8Bytes[0] = (byte)0xEF;
        utf8Bytes[1] = (byte)0xBB;
        utf8Bytes[2] = (byte)0xBF;
        for(int i=0; i<csvBytes.length; i++){
        	utf8Bytes[i+3] = csvBytes[i];
        }
        return new ResponseEntity<byte[]>(utf8Bytes, headers, HttpStatus.OK);
	}
	
	/**
	 * 导入用户表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月19日 上午8:42:16
	 * @param formdata csv 用户角色关联表数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/handle/import")
	public Object handleImport(HttpServletRequest request, HttpServletResponse response) throws Exception{
		MultipartHttpServletRequestWrapper multipartRequest = new MultipartHttpServletRequestWrapper(request);
		InputStream inputstream = multipartRequest.getInputStream("csv");
		String csv = FileUtil.readAsString(inputstream);
		userService.importCsv(csv);
		return null;
	}
	
	/**
	 * 查询用户导入状态<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月9日 下午2:40:38
	 * @return boolean status 是否正在导入
	 * @return long totalUsers 导入总用户数
	 * @return long currentUser 当前导入用户
	 * @return int importTimes 导入次数
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/import/status")
	public Object queryImportStatus(HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		UserImportEventPublisher publisher = userService.getUserImportEventPublisher(user.getGroupId());
		UserImportInfoPO userImportInfo = userImportInfoDao.findByCompanyId(user.getGroupId());
		Map<String, Object> status = new HashMap<String, Object>();
		if(publisher == null){
			status.put("status", false);
			status.put("totalUsers", 0);
			status.put("currentUser", 0);
			status.put("importTimes", userImportInfo==null?0:userImportInfo.getTimes());
		}else{
			status.put("status", true);
			status.put("totalUsers", publisher.getTotalUsers());
			status.put("currentUser", publisher.getCurrentNum());
			status.put("importTimes", userImportInfo==null?0:userImportInfo.getTimes());
		}
		return status;
	}

}




