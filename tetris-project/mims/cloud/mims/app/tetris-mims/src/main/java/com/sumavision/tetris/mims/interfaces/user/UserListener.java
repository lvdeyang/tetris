package com.sumavision.tetris.mims.interfaces.user;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderGroupPermissionDAO;
import com.sumavision.tetris.mims.app.folder.FolderGroupPermissionPO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderRolePermissionDAO;
import com.sumavision.tetris.mims.app.folder.FolderRolePermissionPO;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.folder.FolderUserPermissionDAO;
import com.sumavision.tetris.mims.app.folder.FolderUserPermissionPO;
import com.sumavision.tetris.mims.app.role.RoleDAO;
import com.sumavision.tetris.mims.app.role.RolePO;
import com.sumavision.tetris.mims.app.role.RoleUserPermissionDAO;
import com.sumavision.tetris.mims.app.role.RoleUserPermissionPO;
import com.sumavision.tetris.mims.app.user.UserClassify;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "")
public class UserListener {

	@Autowired
	private FolderGroupPermissionDAO folderGroupPermissionDao;
	
	@Autowired
	private FolderUserPermissionDAO folderUserPermissionDao;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private RoleDAO roleDao;
	
	@Autowired
	private RoleUserPermissionDAO roleUserPermissionDao;
	
	@Autowired
	private FolderRolePermissionDAO folderRolePermissionDao;
	
	/**
	 * 注册个人硬盘<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月7日 下午1:48:17
	 * @param String userId 用户id
	 * @param String userName 用户名
	 * @return JSON {status:200或其他, message:'接口调用信息'}
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/persional/disk/regester")
	public Object personalDiskRegister(
			String userId, 
			String userName, 
			HttpServletRequest request) throws Exception{
		
		List<FolderUserPermissionPO> folderUserPermissions = folderUserPermissionDao.findByUserId(userId);
		
		if(folderUserPermissions==null || folderUserPermissions.size()<=0){
			//创建个人硬盘
			createPersonalDisk(userId, userName);
		}
		
		return null;
	}
	
	
	/**
	 * 注册企业硬盘<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月21日 下午1:46:17
	 * @param String userId 用户id
	 * @param String userName 用户名
	 * @param String groupId 用户隶属用户组的id
	 * @param String groupName 用户隶属用户组的名称
	 * @return JSON {status:200或其他, message:'接口调用信息'}
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/company/disk/regester")
	public Object companyDiskRegester(
			String userId, 
			String userName, 
			String groupId, 
			String groupName, 
			HttpServletRequest request) throws Exception{
		
		List<FolderGroupPermissionPO> folderGroupPermissions = folderGroupPermissionDao.findByGroupId(groupId);
		if(folderGroupPermissions==null || folderGroupPermissions.size()<=0){
			//创建企业网盘
			createCompanyDisk(groupId, groupName, userId);
		}
		
		List<FolderUserPermissionPO> folderUserPermissions = folderUserPermissionDao.findByUserId(userId);
		if(folderUserPermissions==null || folderUserPermissions.size()<=0){
			//创建个人网盘
			createPersonalDisk(userId, userName);
		}
		
		return null;
	}
	
	//创建媒资库
	private void createCompanyDisk(String groupId, String groupName, String userId) throws Exception{
		//创建管理员
		RolePO role = new RolePO();
		role.setGroupId(groupId);
		role.setName("管理员");
		role.setRemoveable(false);
		role.setSerial(0);
		role.setUpdateTime(new Date());
		role.setClassify(UserClassify.COMPANY_ADMIN);
		roleDao.save(role);
		RoleUserPermissionPO permission1 = new RoleUserPermissionPO();
		permission1.setRoleId(role.getId());
		permission1.setUserId(userId);
		permission1.setUpdateTime(new Date());
		roleUserPermissionDao.save(permission1);
		
		//企业根目录
		FolderPO root = createFolder(groupId, groupName, FolderType.COMPANY, null, null, permission1.getId());
		
		String parentPath = new StringBufferWrapper().append("/").append(root.getId()).toString();
		
		//图片
		createFolder(groupId, groupName, FolderType.COMPANY_PICTURE, root.getId(), parentPath, permission1.getId());
		
		//视频
		createFolder(groupId, groupName, FolderType.COMPANY_VIDEO, root.getId(), parentPath, permission1.getId());
		
		//音频
		createFolder(groupId, groupName, FolderType.COMPANY_AUDIO, root.getId(), parentPath, permission1.getId());
		
		//视频流
		createFolder(groupId, groupName, FolderType.COMPANY_VIDEO_STREAM, root.getId(), parentPath, permission1.getId());
		
		//音频流
		createFolder(groupId, groupName, FolderType.COMPANY_AUDIO_STREAM, root.getId(), parentPath, permission1.getId());
		
		//文本
		createFolder(groupId, groupName, FolderType.COMPANY_TXT, root.getId(), parentPath, permission1.getId());
		
	}
	
	//创建企业文件夹
	private FolderPO createFolder(
			String groupId, 
			String groupName, 
			FolderType type, 
			Long parentId, 
			String parentPath,
			Long roleId) throws Exception{
		FolderPO folder = new FolderPO();
		folder.setName(new StringBufferWrapper().append(groupName).append("（").append(type.getName()).append("）").toString());
		folder.setDepth();
		folder.setType(type);
		folder.setUpdateTime(new Date());
		folder.setParentId(parentId);
		folder.setParentPath(parentPath);
		folderDao.save(folder);
		FolderGroupPermissionPO permission0 = new FolderGroupPermissionPO();
		permission0.setFolderId(folder.getId());
		permission0.setGroupId(groupId);
		permission0.setUpdateTime(new Date());
		folderGroupPermissionDao.save(permission0);
		FolderRolePermissionPO permission1 = new FolderRolePermissionPO();
		permission1.setFolderId(folder.getId());
		permission1.setRoleId(roleId);
		permission1.setUpdateTime(new Date());
		folderRolePermissionDao.save(permission1);
		return folder;
	}
	
	//创建个人硬盘
	private void createPersonalDisk(String userId, String userName) throws Exception{
		FolderPO folder = new FolderPO();
		folder.setName(new StringBufferWrapper().append(userName).append("（").append(FolderType.PERSONAL.getName()).append("）").toString());
		folder.setDepth();
		folder.setType(FolderType.PERSONAL);
		folder.setUpdateTime(new Date());
		folderDao.save(folder);
		FolderUserPermissionPO permission = new FolderUserPermissionPO();
		permission.setFolderId(folder.getId());
		permission.setUserId(userId);
		permission.setUpdateTime(new Date());
		folderUserPermissionDao.save(permission);
	}
	
}
