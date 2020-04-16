package com.suma.venus.resource.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.suma.venus.resource.base.bo.EncoderBO;
import com.suma.venus.resource.base.bo.ResourceIdListBO;
import com.suma.venus.resource.base.bo.RoleAndResourceIdBO;
import com.suma.venus.resource.base.bo.RoleBO;
import com.suma.venus.resource.base.bo.UnbindResouceBO;
import com.suma.venus.resource.base.bo.UnbindRolePrivilegeBO;
import com.suma.venus.resource.base.bo.UserAndResourceIdBO;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.dao.EncoderDecoderUserMapDAO;
import com.suma.venus.resource.dao.FolderUserMapDAO;
import com.suma.venus.resource.dao.PrivilegeDAO;
import com.suma.venus.resource.dao.RolePrivilegeMapDAO;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.EncoderDecoderUserMap;
import com.suma.venus.resource.pojo.FolderUserMap;
import com.suma.venus.resource.pojo.PrivilegePO;
import com.suma.venus.resource.pojo.PrivilegePO.EPrivilegeType;
import com.suma.venus.resource.pojo.RolePrivilegeMap;
import com.suma.venus.resource.pojo.WorkNodePO;
import com.suma.venus.resource.pojo.WorkNodePO.NodeType;
import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.business.role.BusinessRoleQuery;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.system.role.SystemRoleQuery;
import com.sumavision.tetris.system.role.SystemRoleVO;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserQueryService {
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private SystemRoleQuery roleQuery;
	
	@Autowired
	private BusinessRoleQuery businessRoleQuery;
	
	@Autowired
	private PrivilegeDAO privilegeDao;
	
	@Autowired
	private RolePrivilegeMapDAO rolePrivilegeMapDao;
	
	@Autowired
	private EncoderDecoderUserMapDAO encoderDecoderUserMapDao;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private FolderUserMapDAO folderUserMapDao;
	
	@Autowired
	private WorkNodeService workNodeService;
	
	/**
	 * 当前用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月31日 下午3:50:37
	 * @return UserVO
	 */
	public UserBO current() throws Exception{
		UserVO user = userQuery.current();
		
		BundlePO encoder = bundleDao.findByUserIdAndDeviceModel(user.getId(), "encoder");
		
		EncoderDecoderUserMap map = encoderDecoderUserMapDao.findByUserId(user.getId());
		
		return singleUserVo2Bo(user, encoder, map);
	}

	/**
	 * 查询所有用户基本信息--带文件夹信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月31日 下午2:06:15
	 * @return List<UserBO> 
	 */
	public List<UserBO> queryAllUserBaseInfo(TerminalType terminalType) throws Exception{
		
		String name = terminalType == null? null:terminalType.getName();
		
		List<UserVO> userVOs = userQuery.queryAllUserBaseInfo(name);
		List<UserBO> allUsers = transferUserVo2Bo(userVOs);
		
		List<Long> userIds = new ArrayList<Long>();
		for(UserBO user: allUsers){
			userIds.add(user.getId());
		}
		
		//给用户赋予文件夹信息
		List<FolderUserMap> folderUserMaps = folderUserMapDao.findByUserIdIn(userIds);
		for(UserBO user: allUsers){
			for(FolderUserMap map: folderUserMaps){
				if(user.getId().equals(map.getUserId())){
					user.setFolderId(map.getFolderId());
					user.setFolderIndex(map.getFolderIndex() != null? map.getFolderIndex().intValue(): 0);
					user.setFolderUuid(map.getFolderUuid());
					user.setCreater(map.getCreator());
					break;
				}
			}
		}
		
		return allUsers;
	}
	
	/**
	 * 根据用户名模糊查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 上午10:34:16
	 * @param String userName 用户名
	 * @return List<UserBO>
	 */
	public List<UserBO> queryUserLikeUserName(String userName) throws Exception{
		
		List<UserVO> userVOs = userQuery.queryUsersByNameLike(userName);
		
		return transferUserVo2Bo(userVOs);
	}
	
	/**
	 * 获取所有角色<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 下午1:44:19
	 * @return List<RoleBO>
	 */
	public List<RoleBO> queryAllRoles() throws Exception{
		
		List<SystemRoleVO> roleVOs = roleQuery.queryAllRoles();
		
		return transferRoleVo2Bo(roleVOs);
	}
	
	/**
	 * 根据用户名查询用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 下午2:39:23
	 * @param String userName 用户名
	 * @return UserBO
	 */
	public UserBO queryUserByUserName(String userName) throws Exception{
		
		UserVO user = userQuery.queryUserByName(userName);
		
		BundlePO encoder = bundleDao.findByUserIdAndDeviceModel(user.getId(), "encoder");
		
		EncoderDecoderUserMap map = encoderDecoderUserMapDao.findByUserId(user.getId());
		
		return singleUserVo2Bo(user, encoder, map);
	}
	
	/**
	 * 根据昵称列表查询用户列表<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月19日 下午4:42:03
	 * @param List<String> nicknames
	 * @return List<UserBO>
	 */
	public List<UserBO> queryUsersByNicknameIn(List<String> nicknames) throws Exception{
		
		List<UserVO> users = userQuery.queryUsersByNickNameIn(nicknames);
		
		return transferUserVo2Bo(users);
		
	}
	
	/**
	 * 根据用户号码查询用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月3日 上午11:19:55
	 * @param String userNo 用户号码
	 * @return UserBO
	 */
	public UserBO queryUserByUserNo(String userNo) throws Exception{
		
		UserVO user = userQuery.queryUserByNo(userNo);
		
		BundlePO encoder = bundleDao.findByUserIdAndDeviceModel(user.getId(), "encoder");
		
		EncoderDecoderUserMap map = encoderDecoderUserMapDao.findByUserId(user.getId());
		
		return singleUserVo2Bo(user, encoder, map);
	}
	
	/**
	 * 根据用户id查询用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 下午2:39:23
	 * @param Long id 用户id
	 * @return UserBO
	 */
	public UserBO queryUserByUserId(Long id, TerminalType terminalType) throws Exception{
		
		String name = terminalType == null?null: terminalType.getName();
		
		UserVO user = userQuery.queryUserById(id, name);
		BundlePO encoder = bundleDao.findByUserIdAndDeviceModel(user.getId(), "encoder");
		EncoderDecoderUserMap encodeMap = encoderDecoderUserMapDao.findByUserId(user.getId());
		UserBO userBO = singleUserVo2Bo(user, encoder, encodeMap);
		
		FolderUserMap map = folderUserMapDao.findByUserId(user.getId());
		if(map != null){
			userBO.setFolderId(map.getFolderId());
			userBO.setFolderIndex(map.getFolderIndex().intValue());
			userBO.setFolderUuid(map.getFolderUuid());
			userBO.setCreater(map.getCreator());
		}
		
		return userBO;
	}
	
	/**
	 * 根据用户ids批量查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月6日 上午9:26:13
	 * @param List<Long> ids 用户id列表
	 * @return List<UserBO>
	 */
	public List<UserBO> queryUsersByUserIds(List<Long> ids, TerminalType terminalType) throws Exception{
		
		String name = terminalType == null?null: terminalType.getName();
		
		List<UserVO> users = userQuery.findByIdInAndType(ids, name);
		
		List<UserBO> allUsers = transferUserVo2Bo(users);
		
		List<Long> userIds = new ArrayList<Long>();
		for(UserBO user: allUsers){
			userIds.add(user.getId());
		}
		
		//给用户赋予文件夹信息
		List<FolderUserMap> folderUserMaps = folderUserMapDao.findByUserIdIn(userIds);
		for(UserBO user: allUsers){
			for(FolderUserMap map: folderUserMaps){
				if(user.getId().equals(map.getUserId())){
					user.setFolderId(map.getFolderId());
					user.setFolderIndex(map.getFolderIndex().intValue());
					user.setFolderUuid(map.getFolderUuid());
					user.setCreater(map.getCreator());
					break;
				}
			}
		}
		
		return allUsers;
	}
	
	/**
	 * 根据角色id查询用户<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月3日 上午11:22:46
	 * @param Long roleId 角色id
	 * @return List<UserBO>
	 */
	public List<UserBO> queryUsersByRole(Long roleId) throws Exception{
		
		List<UserVO> users = userQuery.queryUsersByRole(roleId);
		
		List<UserBO> allUsers = transferUserVo2Bo(users);
		
		List<Long> userIds = new ArrayList<Long>();
		for(UserBO user: allUsers){
			userIds.add(user.getId());
		}
		
		//给用户赋予文件夹信息
		List<FolderUserMap> folderUserMaps = folderUserMapDao.findByUserIdIn(userIds);
		for(UserBO user: allUsers){
			for(FolderUserMap map: folderUserMaps){
				if(user.getId().equals(map.getUserId())){
					user.setFolderId(map.getFolderId());
					user.setFolderIndex(map.getFolderIndex().intValue());
					user.setFolderUuid(map.getFolderUuid());
					user.setCreater(map.getCreator());
					break;
				}
			}
		}
		
		return allUsers;
	}
	
	/**
	 * 获取用户权限资源<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月3日 下午5:23:45
	 * @param userId
	 * @return ResourceIdListBO
	 */
	public ResourceIdListBO queryUserPrivilege(Long userId) throws Exception{
		
		ResourceIdListBO bo = new ResourceIdListBO();
		bo.setResourceCodes(new ArrayList<String>());
		
		//先查所有Role
		List<SystemRoleVO> roles = roleQuery.queryRolesByUser(userId);
		
		List<Long> roleIds = new ArrayList<Long>();
		for(SystemRoleVO role: roles){
			roleIds.add(Long.valueOf(role.getId()));
		}
		
		//再查权限
		if(roleIds.size() > 0){
			List<PrivilegePO> privileges = privilegeDao.findByRoleIdIn(roleIds);
			
			for (PrivilegePO po : privileges) {
				bo.getResourceCodes().add(po.getResourceIndentity());
			}
		}
		
		return bo;
	}
	
	/**
	 * 判断用户资源是否有权限<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月6日 上午10:39:54
	 * @param UserAndResourceIdBO bo 用户资源信息
	 * @return boolean
	 */
	public boolean hasPrivilege(UserAndResourceIdBO bo) throws Exception{
		
		// 先查所有Role
		List<SystemRoleVO> roles = roleQuery.queryRolesByUser(bo.getUserId());
		
		List<Long> roleIds = new ArrayList<Long>();
		for(SystemRoleVO role: roles){
			roleIds.add(Long.valueOf(role.getId()));
		}
		
		//再查权限
		if(roleIds.size() > 0){
			
			List<PrivilegePO> privileges = privilegeDao.findByRoleIdIn(roleIds);
			
			if(privileges == null || privileges.size() <= 0){
				throw new BaseException(StatusCode.ERROR, "用户无权限");
			}
			
			for (String resource : bo.getResourceCodes()) {
				for (PrivilegePO po : privileges) {
					if (resource.equalsIgnoreCase(po.getResourceIndentity())) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	/**
	 * 根据用户名查询编码器<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月14日 上午11:41:26
	 * @param String userName 用户名
	 * @return BundlePO
	 */
	public BundlePO queryEncoderByUserName(String userName) throws Exception{
		
		EncoderDecoderUserMap map = encoderDecoderUserMapDao.findByUserName(userName);
		if(map != null && map.getEncodeBundleId() != null){
			return bundleDao.findByBundleId(map.getEncodeBundleId());
		}
		
		return null;
	}
	
	/**
	 * 根据用户名查询解码器<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月14日 上午11:41:26
	 * @param String userName 用户名
	 * @return BundlePO
	 */
	public BundlePO queryDecoderByUserName(String userName) throws Exception{
		
		EncoderDecoderUserMap map = encoderDecoderUserMapDao.findByUserName(userName);
		if(map != null && map.getDecodeBundleId() != null){
			return bundleDao.findByBundleId(map.getDecodeBundleId());
		}
		
		return null;
	}
	
	/**
	 * 单个用户VO转BO<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 下午2:33:07
	 * @param UserVO userVO 用户vo
	 * @return UserBO
	 */
	public UserBO singleUserVo2Bo(UserVO userVO, BundlePO encoder, EncoderDecoderUserMap map) throws Exception{
		
		EncoderBO local_encoder = null;
		if(encoder != null){
			local_encoder = new EncoderBO();
			local_encoder.setEncoderId(encoder.getBundleId());
			local_encoder.setEncoderName(encoder.getBundleName());
			local_encoder.setEncoderNo(encoder.getUsername());
			local_encoder.setEncoderType(EncoderBO.ENCODER_TYPE.fromName(encoder.getDeviceModel()));
		}
		
		EncoderBO Jv210_encoder = null;
		if(map != null && map.getEncodeBundleId() != null){
			Jv210_encoder = new EncoderBO();
			Jv210_encoder.setEncoderId(map.getEncodeBundleId());
			Jv210_encoder.setEncoderName(map.getEncodeBundleName());
			Jv210_encoder.setEncoderType(EncoderBO.ENCODER_TYPE.fromName(map.getEncodeDeviceModel()));
		}
		
		UserBO userBO = new UserBO();
		userBO.setUser(userVO);
		userBO.setId(userVO.getId());
		userBO.setName(userVO.getNickname());
		userBO.setLevel(0);
		userBO.setPhone(userVO.getMobile());
		userBO.setEmail(userVO.getMail());
		userBO.setCreateTime(userVO.getUpdateTime());
		userBO.setCreater("");
		userBO.setLogined((userVO.getStatus() == null || "OFFLINE".equals(userVO.getStatus()))? false: true);
		userBO.setUserNo(userVO.getUserno());
		userBO.setLocal_encoder(local_encoder);
		userBO.setExternal_encoder(Jv210_encoder);
		
		return userBO;
	}
	
	/**
	 * user vo转bo<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月31日 下午3:33:37
	 * @param List<UserVO> userVOs 
	 * @return List<UserBO> 
	 */
	public List<UserBO> transferUserVo2Bo(List<UserVO> userVOs) throws Exception{
		
		List<Long> userIds = new ArrayList<Long>();
		for(UserVO userVO : userVOs){
			userIds.add(userVO.getId());
		}
		List<BundlePO> encoders = bundleDao.findByUserIdInAndDeviceModel(userIds, "encoder");
		List<EncoderDecoderUserMap> maps = encoderDecoderUserMapDao.findByUserIdIn(userIds);
		
		List<UserBO> userBOs = new ArrayList<UserBO>();
		if(userVOs != null && userVOs.size() > 0){
			for(UserVO userVO: userVOs){
				BundlePO _encode = null;
				EncoderDecoderUserMap _map = null;
				for(BundlePO encoder: encoders){
					if(encoder.getUserId().equals(userVO.getId())){
						_encode = encoder;
						break;
					}
				}
				for(EncoderDecoderUserMap map: maps){
					if(map.getUserId().equals(userVO.getId())){
						_map = map;
						break;
					}
				}
				userBOs.add(singleUserVo2Bo(userVO, _encode, _map));
			}
		}
		
		return userBOs;
	}
	
	/**
	 * role vo转bo<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年1月2日 上午11:37:23
	 * @param List<SystemRoleVO> roleVOs
	 * @return List<RoleBO>
	 */
	public List<RoleBO> transferRoleVo2Bo(List<SystemRoleVO> roleVOs) throws Exception{
		List<RoleBO> roleBOs = new ArrayList<RoleBO>();
		if(roleVOs != null && roleVOs.size() > 0){
			for(SystemRoleVO roleVO: roleVOs){
				RoleBO roleBO = new RoleBO();
				roleBO.setId(Long.valueOf(roleVO.getId()));
				roleBO.setCode(roleVO.getId());
				roleBO.setDescription("");
				roleBO.setName(roleVO.getName());
				roleBOs.add(roleBO);
			}
		}
		
		return roleBOs;
	}
	
	/**
	 * 导入用户权限绑定<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月3日 上午10:24:07
	 * @param String userno 用户号码
	 * @param List<String> roleIds 角色ids
	 */
	public void importUserPrivilage(String userno, List<String> roleIds) throws Exception{
		
		List<String> toBindChecks = new ArrayList<String>();
		toBindChecks.add(userno + "-r");
		toBindChecks.add(userno + "-w");
		toBindChecks.add(userno + "-hj");
		toBindChecks.add(userno + "-zk");
		
		for(String roleId: roleIds){
			RoleAndResourceIdBO bo = new RoleAndResourceIdBO();
			bo.setRoleId(Long.valueOf(roleId));
			bo.setResourceCodes(new ArrayListWrapper<String>().addAll(toBindChecks).getList());
			
			bindRolePrivilege(bo);
		}

	}
	
	/**
	 * 角色绑定权限<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月26日 下午2:00:49
	 * @param RoleAndResourceIdBO param
	 * @return boolean
	 */
	public boolean bindRolePrivilege(RoleAndResourceIdBO param) throws Exception{
		
		if (null == param.getRoleId() || param.getRoleId() == 0l) {
			return false;
		}

		if (null == param.getResourceCodes() || param.getResourceCodes().isEmpty()) {
			return false;
		}

		//TODO: feign中加
//		RolePO role = roleService.findById(param.getRoleId());
//		if (null == role) {
//			return false;
//		}
		List<String> resources = new ArrayList<String>();
		for (String resource : param.getResourceCodes()) {
			resources.add(resource);
		}
		
		List<PrivilegePO> privileges = privilegeDao.findByResourceIndentityIn(resources);
		
		// 先保存权限
		List<RolePrivilegeMap> maps = new ArrayList<RolePrivilegeMap>();
		for (String resource : param.getResourceCodes()) {
			PrivilegePO privilege = null;
			for(PrivilegePO _privilege: privileges){
				if(_privilege.getResourceIndentity().equals(resource)){
					privilege = _privilege;
					break;
				}
			}
			if (null == privilege) {
				PrivilegePO p = new PrivilegePO();
				p.setbEdit(false);
				p.setName("roleId-" + param.getRoleId() + "-privilege-" + resource);
				p.setpCode("customcode");
				p.setPrivilegeType(EPrivilegeType.CUSTOM);
				p.setResourceIndentity(resource);
				privilegeDao.save(p);
				privilege = p;
				// 进行绑定
			}
			RolePrivilegeMap mapR = new RolePrivilegeMap();
			mapR.setPrivilegeId(privilege.getId());
			mapR.setRoleId(param.getRoleId());
			maps.add(mapR);
		}
		
		rolePrivilegeMapDao.save(maps);
		
		return true;
	}
	
	/**
	 * 角色解绑权限<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月26日 下午2:02:39
	 */
	public boolean unbindRolePrivilege(UnbindRolePrivilegeBO param) throws Exception{
		
		if (null == param.getRoleId() || param.getRoleId() == 0l) {
			return false;
		}

		if (null == param.getUnbindPrivilege() || param.getUnbindPrivilege().isEmpty()) {
			return false;
		}

		//TODO: 新feign里面写
//		RolePO role = roleService.findById(param.getRoleId());
//		if (null == role) {
//			data.put(ERRMSG, "用户角色为空");
//			data.put("result", false);
//			return data;
//		}
		List<String> resources = new ArrayList<String>();
		for (UnbindResouceBO resource : param.getUnbindPrivilege()) {
			resources.add(resource.getResourceCode());
		}
		List<PrivilegePO> privileges = privilegeDao.findByResourceIndentityIn(resources);
		List<Long> privilegeIds = new ArrayList<Long>();
		for(PrivilegePO po: privileges){
			privilegeIds.add(po.getId());
		}
		
		List<RolePrivilegeMap> maps = rolePrivilegeMapDao.findByRoleIdAndPrivilegeIdIn(param.getRoleId(), privilegeIds);
		if(maps != null && maps.size()>0){
			rolePrivilegeMapDao.delete(maps);
		}
		
		List<PrivilegePO> needDeletePrivileges = new ArrayList<PrivilegePO>();
		for (UnbindResouceBO resource : param.getUnbindPrivilege()) {
			for(PrivilegePO po: privileges){
				if(po.getResourceIndentity().equals(resource.getResourceCode()) && resource.isbDelete()){
					needDeletePrivileges.add(po);
					break;
				}
			}
		}
		if(needDeletePrivileges.size() > 0){
			privilegeDao.delete(needDeletePrivileges);
		}
		
		return true;
	}
	
	/**
	 * 查询用户私有角色<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月7日 下午2:14:40
	 * @param Long userId 用户id
	 * @return SystemRoleVO 角色
	 */
	public SystemRoleVO queryPrivateRoleId(Long userId) throws Exception{
		
		List<Long> userIds = new ArrayList<Long>();
		userIds.add(userId);
		
		List<SystemRoleVO> roles = businessRoleQuery.findPrivateRoleByUserIds(userIds);
		if(roles == null || roles.size() <= 0){
			throw new BaseException(StatusCode.ERROR, "id为：" + userId + "的用户没有私有角色！");
		}
		
		return roles.get(0);
		
	}
	
	/**
	 * 批量给用户的播放器和编码器设置接入层<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月13日 下午4:13:16
	 * @param List<Long> userIds 用户ids
	 */
	public void setUserLayer(List<Long> userIds) throws Exception{
		
		List<WorkNodePO> layers = workNodeService.findByType(NodeType.ACCESS_JV210);
		if(layers == null || layers.size() <= 0) return;
		
		List<BundlePO> bundles = bundleDao.findByUserIdIn(userIds);
		
		for(Long userId: userIds){
			List<BundlePO> userBundles = new ArrayList<BundlePO>();
			for(BundlePO bundle: bundles){
				if(userId.equals(bundle.getUserId()) && (bundle.getDeviceModel().equals("player") || bundle.getDeviceModel().equals("encoder"))){
					userBundles.add(bundle);
				}
			}
			
			WorkNodePO choseNode = choseWorkNode(layers, bundles);
			for(BundlePO bundle: userBundles){
				if(bundle.getAccessNodeUid() == null || StringUtils.isEmpty(bundle.getAccessNodeUid())){
					bundle.setAccessNodeUid(choseNode.getNodeUid());
				}
			}
			
		}
		
		bundleDao.save(bundles);
	}
	
	/**
	 * 选择接入层<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月13日 下午4:46:42
	 * @param List<WorkNodePO> nodes 所有接入层
	 * @param List<BundlePO> bundles 所有设备
	 * @return WorkNodePO 接入层信息
	 */
	public WorkNodePO choseWorkNode(List<WorkNodePO> nodes, List<BundlePO> bundles) throws Exception{
		
		WorkNodePO chose = null;
		
		if(nodes != null && nodes.size() > 0){
			List<String> layerIds = new ArrayList<String>();
			for(WorkNodePO node: nodes){
				layerIds.add(node.getNodeUid());
			}
			
			int count = 0;
			for(WorkNodePO node: nodes){
				int number = 0;
				for(BundlePO bundle: bundles){
					if(bundle.getAccessNodeUid().equals(node.getNodeUid())){
						number ++;
					}
				}
				if(number >= count){
					chose = node;
					count = number;
				}
			}
		}
		
		return chose;
	}
	
}
