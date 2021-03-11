package com.suma.venus.resource.query;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.dao.OutlandPermissionCheckDao;
import com.suma.venus.resource.dao.OutlandPermissionExceptDao;
import com.suma.venus.resource.pojo.OutlandPermissionCheckPO;
import com.suma.venus.resource.pojo.OutlandPermissionExceptPO;
import com.suma.venus.resource.pojo.PermissionType;
import com.suma.venus.resource.pojo.SourceType;
import com.suma.venus.resource.query.exception.OutlandPermissionParamException;
import com.suma.venus.resource.service.OutlandPermissionService;
import com.suma.venus.resource.vo.OutlandPermissionCheckVO;
import com.suma.venus.resource.vo.OutlandPermissionExceptVO;
import com.sumavision.tetris.business.role.BusinessRoleQuery;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.system.role.SystemRoleVO;

@Component
public class OutlandPermissionQuery {

	@Autowired
	private OutlandPermissionCheckDao outlandPermissionCheckDao;
	
	@Autowired
	private OutlandPermissionExceptDao outlandPermissionExceptDao;
	
	@Autowired
	private BusinessRoleQuery businessRoleQuery;
	
	@Autowired
	private OutlandPermissionService outlandPermissionService;
	
	/**
	 * 向管理页面推送文件夹信息，时添加文件夹勾选情况和例外情况<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月9日 下午4:59:27
	 * @param JSONObject message 
	 * {
	 *	    businessType: "foreignUpdate",
	 *	    type: "FOLDER",
	 *	    uuid: "703f2403a79b46f9b855e3e839068fb4",
	 *	    serNodeNamePath: "/本机61",
	 *	    folderPath: "/CB9AD95E-AD82-4597-94AF-262CC0764B6C/83A72761-1C57-4FBD-A5D4-DDE215A289B2",
	 *	    institutions: [{
	 *	        domainPath: "/本机61",
	 *	        name: "第三级_1",
	 *	        path: "/CB9AD95E-AD82-4597-94AF-262CC0764B6C/83A72761-1C57-4FBD-A5D4-DDE215A289B2/DDB8ED01-995D-48D4-99A0-4A83DBF98366",
	 *	        type: "FOLDER"
	 *	    }],
	 *	    permissionType: "非空",
	 *	    roleId: "非空"
	 *	}
	 * @return JSONObject message 中 添加 permissions:{check:[{}], except:[{}]}
	 */
	public JSONObject packFolderPermissions(JSONObject message) throws Exception{
		String serNodeNamePath = message.getString("serNodeNamePath");
		String permissionType = message.getString("permissionType");
		String roleId = message.getString("roleId");
		if(permissionType==null || "".equals(permissionType)){
			throw new OutlandPermissionParamException(new StringBufferWrapper().append("permissionType为空：").append(permissionType).toString());
		}
		if(roleId==null || "".equals(roleId)){
			throw new OutlandPermissionParamException(new StringBufferWrapper().append("roleId为空：").append(roleId).toString());
		}
		
		Long lRoleId = Long.valueOf(roleId);
		
		JSONObject permissions = new JSONObject();
		message.put("permissions", permissions);
		
		List<OutlandPermissionCheckPO> outlandPermissionCheckEntities = outlandPermissionCheckDao.findBySerNodeNamePathAndPermissionTypeAndRoleId(serNodeNamePath, PermissionType.valueOf(permissionType), lRoleId);
		permissions.put("check", OutlandPermissionCheckVO.getConverter(OutlandPermissionCheckVO.class).convert(outlandPermissionCheckEntities, OutlandPermissionCheckVO.class));
		
		List<OutlandPermissionExceptPO> outlandPermissionExceptEntities = outlandPermissionExceptDao.findBySerNodeNamePathAndPermissionTypeAndRoleId(serNodeNamePath, PermissionType.valueOf(permissionType), lRoleId);
		permissions.put("except", OutlandPermissionExceptVO.getConverter(OutlandPermissionExceptVO.class).convert(outlandPermissionExceptEntities, OutlandPermissionExceptVO.class));
		
		return message;
	}
	
	/**
	 * 向管理页面推送设备信息，时添加设备勾选情况和例外情况<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月9日 下午5:03:12
	 * @param JSONObject message
	 * {
	 *	    businessType: "foreignUpdate",
	 *	    type: "BUNDLE", 
	 *	    uuid: "0756c9f3e2274c8e9d90cc1d78018493",
	 *	    serNodeNamePath: "/本机61",
	 *	    folderPath: "/CB9AD95E-AD82-4597-94AF-262CC0764B6C",
	 *	    devices: [{
	 *	        domainPath: "/本机61",
	 *	        id: "6C719086-6F2A-40EB-847D-DC024DF98BE5",
	 *	        isMulticast: true,
	 *	        multiAddr: "239.100.0.1:11000",
	 *	        multiSrcIp: "10.1.41.196",
	 *	        name: "6931offline-摄像机1",
	 *	        status: "offline",
	 *	        type: "BUNDLE",
	 *	    }],
	 *	    permissionType: "",
	 *	    roleId: ""
	 * }
	 * @return JSONObject message 中 添加 permissions:{check:[{}], except:[{}]}
	 */
	public JSONObject packDevicePermissions(JSONObject message) throws Exception{
		String serNodeNamePath = message.getString("serNodeNamePath");
		String permissionType = message.getString("permissionType");
		String roleId = message.getString("roleId");
		if(permissionType==null || "".equals(permissionType)){
			throw new OutlandPermissionParamException(new StringBufferWrapper().append("permissionType为空：").append(permissionType).toString());
		}
		if(roleId==null || "".equals(roleId)){
			throw new OutlandPermissionParamException(new StringBufferWrapper().append("roleId为空：").append(roleId).toString());
		}
		
		Long lRoleId = Long.valueOf(roleId);
		
		JSONObject permissions = new JSONObject();
		message.put("permissions", permissions);
		
		List<OutlandPermissionCheckPO> outlandPermissionCheckEntities = outlandPermissionCheckDao.findBySerNodeNamePathAndPermissionTypeAndRoleId(serNodeNamePath, PermissionType.valueOf(permissionType), lRoleId);
		permissions.put("check", OutlandPermissionCheckVO.getConverter(OutlandPermissionCheckVO.class).convert(outlandPermissionCheckEntities, OutlandPermissionCheckVO.class));
		
		List<OutlandPermissionExceptPO> outlandPermissionExceptEntities = outlandPermissionExceptDao.findBySerNodeNamePathAndPermissionTypeAndRoleId(serNodeNamePath, PermissionType.valueOf(permissionType), lRoleId);
		permissions.put("except", OutlandPermissionExceptVO.getConverter(OutlandPermissionExceptVO.class).convert(outlandPermissionExceptEntities, OutlandPermissionExceptVO.class));
		
		return message;
	}
	
	/**
	 * 向客户端推送设备信息，过滤掉没有任何权限的设备，有权限的将具体权限返回给客户端<br/>
	 * <p>
	 *   1.根据设备id判断
	 *       -例外表中存在设备授权：一定无权限
	 *       -勾选表中存在设备授权：一定有权限
	 *   2.根据父目录判断
	 *       勾选表目录路径length>例外表目录路径length：一定有权限
	 *       勾选表目录路径length<例外表目录路径length：一定无权限
	 *   3.除以上两种情况，勾选表目录不存在并且例外表不存在：一定无权限
	 *   4.勾选表与里外表都存在的情况不存在        
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月8日 下午4:48:19
	 * @param String userId 用户id
	 * @param JSONObject message 同 packDevicePermissions参数
	 * @return JSONObject message中添加  message.devices[i].Privilege:[{
     *       //服务器录制
	 *		hasReadPrivilege：true|false,
     *      //点播
     *		hasWritePrivilege：true|false,
     *      //云台
	 *		hasCloudPrivilege：true|false,   
     *      //下载
	 *		hasDownloadPrivilege：true|false, 
     *      //本地录制
	 *		hasLocalReadPrivilege：true|false   
	 *	}]
	 */
	public JSONObject packDevicePrivilege(String userId, JSONObject message) throws Exception{
		
		List<SystemRoleVO> roles = businessRoleQuery.findBusinessRoleByUserId(Long.valueOf(userId));
		Set<Long> roleIds = new HashSet<Long>();
		if(roles!=null && roles.size()>0){
			for(SystemRoleVO role:roles){
				roleIds.add(Long.valueOf(role.getId()));
			}
		}
		if(roleIds.size() <= 0){
			//当前用户没有权限，将设备清空
			message.put("devices", new JSONArray());
			return message;
		}
		
		String serNodeNamePath = message.getString("serNodeNamePath");
		String folderPath = message.getString("folderPath");
		List<String> folderPaths = outlandPermissionService.generateFolderPaths(Arrays.asList(folderPath.split("/")));
		
		JSONArray devices = message.getJSONArray("devices");
		JSONArray filterDevices = new JSONArray();
		if(devices!=null && devices.size()>0){
			Set<String> deviceIds = new HashSet<String>();
			for(int i=0; i<devices.size(); i++){
				JSONObject device = devices.getJSONObject(i);
				String id = device.getString("id");
				deviceIds.add(id);
			}
			List<OutlandPermissionCheckPO> outlandFolderPermissionCheckEntities = outlandPermissionCheckDao.findBySerNodeNamePathAndFolderPathInAndSourceTypeAndRoleIdIn(serNodeNamePath, folderPaths, SourceType.FOLDER, roleIds);
			List<OutlandPermissionCheckPO> outlandDevicePermissionCheckEntities = outlandPermissionCheckDao.findBySerNodeNamePathAndFolderPathAndDeviceIdInAndSourceTypeAndRoleIdIn(serNodeNamePath, folderPath, deviceIds, SourceType.DEVICE, roleIds);
			List<OutlandPermissionExceptPO> outlandFolderPermissionExceptEntities = outlandPermissionExceptDao.findBySerNodeNamePathAndFolderPathInAndSourceTypeAndRoleIdIn(serNodeNamePath, folderPaths, SourceType.FOLDER, roleIds);
			List<OutlandPermissionExceptPO> outlandDevicePermissionExceptEntities = outlandPermissionExceptDao.findBySerNodeNamePathAndFolderPathAndDeviceIdInAndSourceTypeAndRoleIdIn(serNodeNamePath, folderPath, deviceIds, SourceType.DEVICE, roleIds);
			for(int i=0; i<devices.size(); i++){
				JSONObject device = devices.getJSONObject(i);
				String deviceId = device.getString("id");
				boolean visible = false;
				JSONObject privilege = new JSONObject();
				PermissionType[] permissionTypes = PermissionType.values();
				for(PermissionType permissionType:permissionTypes){
					boolean hasPrivilege = privilegeByDeviceIdAndPermissionType(
							outlandFolderPermissionCheckEntities, 
							outlandDevicePermissionCheckEntities, 
							outlandFolderPermissionExceptEntities, 
							outlandDevicePermissionExceptEntities, 
							deviceId, 
							permissionType);
					if(hasPrivilege){
						visible = true;
					}
					privilege.put(permissionType.getReferencePrivilegeKey(), hasPrivilege);
				}
				if(visible){
					JSONArray privilegeWrapper = new JSONArray();
					privilegeWrapper.add(privilege);
					device.put("Privilege", privilegeWrapper);
					filterDevices.add(device);
				}
			}
			message.put("devices", filterDevices);
		}
		return message;
	}
	
	/**
	 * 单设备具体权限判断<br/>
	 * <p>
	 * 	 1.根据设备id判断<br/>
	 *       -例外表中存在设备授权：一定无权限<br/>
	 *       -勾选表中存在设备授权：一定有权限<br/>
	 *   2.根据父目录判断<br/>
	 *       勾选表目录路径length>例外表目录路径length：一定有权限<br/>
	 *       勾选表目录路径length<例外表目录路径length：一定无权限<br/>
	 *   3.除以上两种情况，勾选表目录不存在并且例外表不存在：一定无权限<br/>
	 *   4.勾选表与里外表都存在的情况不存在<br/>        
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月10日 上午10:37:09
	 * @param List<OutlandPermissionCheckPO> outlandFolderPermissionCheckEntities 父目录授权勾选
	 * @param List<OutlandPermissionCheckPO> outlandDevicePermissionCheckEntities 设备授权勾选
	 * @param List<OutlandPermissionExceptPO> outlandFolderPermissionExceptEntities 父目录授权例外
	 * @param List<OutlandPermissionExceptPO> outlandDevicePermissionExceptEntities 设备录授权例外
	 * @param String deviceId 设备id
	 * @param PermissionType permissionType 权限类型
	 * @return boolean 权限判断结果
	 */
	private boolean privilegeByDeviceIdAndPermissionType(
			List<OutlandPermissionCheckPO> outlandFolderPermissionCheckEntities,
			List<OutlandPermissionCheckPO> outlandDevicePermissionCheckEntities,
			List<OutlandPermissionExceptPO> outlandFolderPermissionExceptEntities,
			List<OutlandPermissionExceptPO> outlandDevicePermissionExceptEntities,
			String deviceId,
			PermissionType permissionType) throws Exception{
		
		//例外表中存在设备授权：一定无权限
		if(outlandDevicePermissionExceptEntities!=null && outlandDevicePermissionExceptEntities.size()>0){
			for(OutlandPermissionExceptPO outlandDevicePermissionExceptEntity:outlandDevicePermissionExceptEntities){
				if(outlandDevicePermissionExceptEntity.getDeviceId().equals(deviceId) && 
						outlandDevicePermissionExceptEntity.getPermissionType().equals(permissionType)){
					return false;
				}
			}
		}
		
		//勾选表中存在设备授权：一定有权限
		if(outlandDevicePermissionCheckEntities!=null && outlandDevicePermissionCheckEntities.size()>0){
			for(OutlandPermissionCheckPO outlandDevicePermissionCheckEntity:outlandDevicePermissionCheckEntities){
				if(outlandDevicePermissionCheckEntity.getDeviceId().equals(deviceId) && 
						outlandDevicePermissionCheckEntity.getPermissionType().equals(permissionType)){
					return true;
				}
			}
		}
		
		//勾选表目录路径length>例外表目录路径length：一定有权限
		int checkLength = 0;
		int exceptLength = 0;
		if(outlandFolderPermissionCheckEntities!=null && outlandFolderPermissionCheckEntities.size()>0){
			for(OutlandPermissionCheckPO outlandFolderPermissionCheckEntity:outlandFolderPermissionCheckEntities){
				if(outlandFolderPermissionCheckEntity.getFolderPath().length() > checkLength){
					checkLength = outlandFolderPermissionCheckEntity.getFolderPath().length();
				}
			}
		}
		if(outlandFolderPermissionExceptEntities!=null && outlandFolderPermissionExceptEntities.size()>0){
			for(OutlandPermissionExceptPO outlandFolderPermissionExceptEntity:outlandFolderPermissionExceptEntities){
				if(outlandFolderPermissionExceptEntity.getFolderPath().length() > exceptLength){
					exceptLength = outlandFolderPermissionExceptEntity.getFolderPath().length();
				}
			}
		}
		if(checkLength > exceptLength) return true;
		
		//其他情况无权限
		return false;
	}
	
}
