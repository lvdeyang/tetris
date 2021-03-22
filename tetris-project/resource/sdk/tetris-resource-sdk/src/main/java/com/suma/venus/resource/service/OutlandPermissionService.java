package com.suma.venus.resource.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suma.venus.resource.dao.OutlandPermissionCheckDao;
import com.suma.venus.resource.dao.OutlandPermissionExceptDao;
import com.suma.venus.resource.pojo.OutlandPermissionCheckPO;
import com.suma.venus.resource.pojo.OutlandPermissionExceptPO;
import com.suma.venus.resource.pojo.PermissionType;
import com.suma.venus.resource.pojo.SourceType;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Service
public class OutlandPermissionService {

	@Autowired
	private OutlandPermissionCheckDao outlandPermissionCheckDao;
	
	@Autowired
	private OutlandPermissionExceptDao outlandPermissionExceptDao;
	
	/**
	 * 添加设备授权<br/>
	 * <p>
	 *   1.勾选表加一条设备授权<br/>
	 *   2.例外表中对应删除设备授权
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月8日 下午4:25:33
	 * @param String serNodeNamePath 域路径
	 * @param String folderPath 目录路径
	 * @param String deviceId 设备id
	 * @param String name 文件夹/设备名称
	 * @param String permissionType 权限类型
	 * @param Long roleId 角色id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addDevicePermission(
			String serNodeNamePath,
			String folderPath,
			String deviceId,
			String name,
			String permissionType,
			Long roleId) throws Exception{
		
		//勾选表加一条设备授权
		OutlandPermissionCheckPO permissionCheck = new OutlandPermissionCheckPO();
		permissionCheck.setSerNodeNamePath(serNodeNamePath);
		permissionCheck.setFolderPath(folderPath);
		permissionCheck.setDeviceId(deviceId);
		permissionCheck.setName(name);
		permissionCheck.setPermissionType(PermissionType.valueOf(permissionType));
		permissionCheck.setSourceType(SourceType.DEVICE);
		permissionCheck.setRoleId(roleId);
		outlandPermissionCheckDao.save(permissionCheck);
		
		//例外表中对应删除设备授权
		List<OutlandPermissionExceptPO> outlandPermissionExcept = outlandPermissionExceptDao.findBySerNodeNamePathAndFolderPathAndDeviceIdAndPermissionTypeAndSourceTypeAndRoleId(serNodeNamePath, folderPath, deviceId, PermissionType.valueOf(permissionType), SourceType.DEVICE, roleId);
		if(outlandPermissionExcept!=null && outlandPermissionExcept.size()>0) outlandPermissionExceptDao.delete(outlandPermissionExcept);
	}
	
	/**
	 * 添加文件夹授权<br/>
	 * <p>
	 *   1.勾选表加一条目录权限<br/>
	 *   2.勾选表中当前目录下的设备以及子目录和子目录设备的权限全部删除<br/>
	 *   3.例外表中本目录以及子目录授权全部删除<br/>
	 *   4.例外表中本目录以及子目录设备授权全部删除
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月8日 下午4:27:54
	 * @param String serNodeNamePath 域路径
	 * @param String folderPath 目录路径
	 * @param String name 目录名称
	 * @param String permissionType 权限类型
	 * @param Long roleId 角色id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void addFolderPermission(
			String serNodeNamePath,
			String folderPath,
			String name,
			String permissionType,
			Long roleId) throws Exception{
		
		//勾选表中当前目录下的设备以及子目录和子目录设备的权限全部删除
		List<OutlandPermissionCheckPO> needDeletePermissionCheckEntities = new ArrayList<OutlandPermissionCheckPO>();
		List<OutlandPermissionCheckPO> permissionCheckEntities = outlandPermissionCheckDao.findBySerNodeNamePathAndPermissionTypeAndRoleId(serNodeNamePath, PermissionType.valueOf(permissionType), roleId);
		if(permissionCheckEntities!=null && permissionCheckEntities.size()>0){
			for(OutlandPermissionCheckPO permissionCheckEntity:permissionCheckEntities){
				if(permissionCheckEntity.getFolderPath()!=null && permissionCheckEntity.getFolderPath().startsWith(folderPath)){
					needDeletePermissionCheckEntities.add(permissionCheckEntity);
				}
			}
		}
		if(needDeletePermissionCheckEntities!=null && needDeletePermissionCheckEntities.size()>0){
			outlandPermissionCheckDao.delete(needDeletePermissionCheckEntities);
		}
		
		//例外表中本目录以及子目录授权全部删除, 例外表中本目录以及子目录设备授权全部删除
		List<OutlandPermissionExceptPO> needDeletePermissionExceptEntities = new ArrayList<OutlandPermissionExceptPO>();
		List<OutlandPermissionExceptPO> permissionExceptEntities = outlandPermissionExceptDao.findBySerNodeNamePathAndPermissionTypeAndRoleId(serNodeNamePath, PermissionType.valueOf(permissionType), roleId);
		if(permissionExceptEntities!=null && permissionExceptEntities.size()>0){
			for(OutlandPermissionExceptPO permissionExceptEntity:permissionExceptEntities){
				if(permissionExceptEntity.getFolderPath()!=null && permissionExceptEntity.getFolderPath().startsWith(folderPath)){
					needDeletePermissionExceptEntities.add(permissionExceptEntity);
				}
			}
		}
		if(needDeletePermissionExceptEntities!=null && needDeletePermissionExceptEntities.size()>0){
			outlandPermissionExceptDao.delete(needDeletePermissionExceptEntities);
		}
		
		//勾选表加一条目录权限
		OutlandPermissionCheckPO permissionCheckEntity = new OutlandPermissionCheckPO();
		permissionCheckEntity.setSerNodeNamePath(serNodeNamePath);
		permissionCheckEntity.setFolderPath(folderPath);
		permissionCheckEntity.setName(name);
		permissionCheckEntity.setPermissionType(PermissionType.valueOf(permissionType));
		permissionCheckEntity.setSourceType(SourceType.FOLDER);
		permissionCheckEntity.setRoleId(roleId);
		outlandPermissionCheckDao.save(permissionCheckEntity);
	}
	
	/**
	 * 删除设备授权<br/>
	 * <p>
	 *   1.勾选表删除设备授权<br/>
	 *   2.比较勾选表中目录路径length>列外表中目录路径length 时在例外表中加一条设备授权
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月8日 下午4:31:27
	 * @param String serNodeNamePath 域路径
	 * @param String folderPath 目录路径
	 * @param String deviceId 设备id
	 * @param String name 设备名称
	 * @param String permissionType 权限类型
	 * @param Long roleId 角色id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void removeDevicePermission(
			String serNodeNamePath,
			String folderPath,
			String deviceId,
			String name,
			String permissionType,
			Long roleId) throws Exception{
		
		//勾选表删除设备授权
		List<OutlandPermissionCheckPO> outlandPermissionCheckEntities = outlandPermissionCheckDao.findBySerNodeNamePathAndFolderPathAndDeviceIdAndPermissionTypeAndSourceTypeAndRoleId(serNodeNamePath, folderPath, deviceId, PermissionType.valueOf(permissionType), SourceType.DEVICE, roleId);
		if(outlandPermissionCheckEntities!=null && outlandPermissionCheckEntities.size()>0){
			outlandPermissionCheckDao.delete(outlandPermissionCheckEntities);
		}
		
		//比较勾选表中目录路径length>列外表中目录路径length 时在例外表中加一条设备授权
		String[] folderIds = folderPath.split("/");
		List<String> parentFolderPaths = generateFolderPaths(Arrays.asList(folderIds));
		
		int checkLength = 0;
		List<OutlandPermissionCheckPO> checkEntities = outlandPermissionCheckDao.findBySerNodeNamePathAndFolderPathInAndPermissionTypeAndSourceTypeAndRoleId(serNodeNamePath, parentFolderPaths, PermissionType.valueOf(permissionType), SourceType.FOLDER, roleId);
		if(checkEntities!=null && checkEntities.size()>0){
			for(OutlandPermissionCheckPO checkEntity:checkEntities){
				if(checkEntity.getFolderPath()!=null && checkEntity.getFolderPath().length()>checkLength){
					checkLength = checkEntity.getFolderPath().length();
				}
			}
		}
		
		int exceptLength = 0;
		List<OutlandPermissionExceptPO> exceptEntities = outlandPermissionExceptDao.findBySerNodeNamePathAndFolderPathInAndPermissionTypeAndSourceTypeAndRoleId(serNodeNamePath, parentFolderPaths, PermissionType.valueOf(permissionType), SourceType.FOLDER, roleId);
		if(exceptEntities!=null && exceptEntities.size()>0){
			for(OutlandPermissionExceptPO exceptEntity:exceptEntities){
				if(exceptEntity.getFolderPath()!=null && exceptEntity.getFolderPath().length()>exceptLength){
					exceptLength = exceptEntity.getFolderPath().length();
				}
			}
		}
		
		if(checkLength > exceptLength){
			OutlandPermissionExceptPO outlandPermissionExceptEntity = new OutlandPermissionExceptPO();
			outlandPermissionExceptEntity.setSerNodeNamePath(serNodeNamePath);
			outlandPermissionExceptEntity.setFolderPath(folderPath);
			outlandPermissionExceptEntity.setDeviceId(deviceId);
			outlandPermissionExceptEntity.setName(name);
			outlandPermissionExceptEntity.setPermissionType(PermissionType.valueOf(permissionType));
			outlandPermissionExceptEntity.setSourceType(SourceType.DEVICE);
			outlandPermissionExceptEntity.setRoleId(roleId);
			outlandPermissionExceptDao.save(outlandPermissionExceptEntity);
		}
		
	}
	
	/**
	 * 删除文件夹授权<br/>
	 * <p>
	 *   1.勾选表中本目录授权删除<br/>
	 *   2.勾选表中本目录下设备授权删除<br/>
	 *   3.勾选表子目录及子目录设备授权删除<br/>
	 *   4.例外表中子目录及子目录设备授权删除<br/>
	 *   5.勾选表父目录路径length>例外表父目录路径length 时在例外表中加一条目录授权
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月8日 下午4:34:34
	 * @param String serNodeNamePath 域路径
	 * @param String folderPath 目录路径
	 * @param String name 目录名称
	 * @param String permissionType 权限类型
	 * @param Long roleId 角色id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void removeFolderPermission(
			String serNodeNamePath,
			String folderPath,
			String name,
			String permissionType,
			Long roleId) throws Exception{
		
		//勾选表中本目录授权删除, 勾选表中本目录下设备授权删除, 勾选表子目录及子目录设备授权删除
		List<OutlandPermissionCheckPO> needDeletePermissionCheckEntities = new ArrayList<OutlandPermissionCheckPO>();
		List<OutlandPermissionCheckPO> permissionCheckEntities = outlandPermissionCheckDao.findBySerNodeNamePathAndPermissionTypeAndRoleId(serNodeNamePath, PermissionType.valueOf(permissionType), roleId);
		if(permissionCheckEntities!=null && permissionCheckEntities.size()>0){
			for(OutlandPermissionCheckPO permissionCheckEntity:permissionCheckEntities){
				if(permissionCheckEntity.getFolderPath()!=null && permissionCheckEntity.getFolderPath().startsWith(folderPath)){
					needDeletePermissionCheckEntities.add(permissionCheckEntity);
				}
			}
		}
		if(needDeletePermissionCheckEntities!=null && needDeletePermissionCheckEntities.size()>0){
			outlandPermissionCheckDao.delete(needDeletePermissionCheckEntities);
		}
		
		//例外表中子目录及子目录设备授权删除
		List<OutlandPermissionExceptPO> needDeletePermissionExceptEntities = new ArrayList<OutlandPermissionExceptPO>();
		List<OutlandPermissionExceptPO> permissionExceptEntities = outlandPermissionExceptDao.findBySerNodeNamePathAndPermissionTypeAndRoleId(serNodeNamePath, PermissionType.valueOf(permissionType), roleId);
		if(permissionExceptEntities!=null && permissionExceptEntities.size()>0){
			for(OutlandPermissionExceptPO permissionExceptEntity:permissionExceptEntities){
				if(permissionExceptEntity.getFolderPath()!=null && permissionExceptEntity.getFolderPath().startsWith(folderPath)){
					needDeletePermissionExceptEntities.add(permissionExceptEntity);
				}
			}
		}
		if(needDeletePermissionExceptEntities!=null && needDeletePermissionExceptEntities.size()>0){
			outlandPermissionExceptDao.delete(needDeletePermissionExceptEntities);
		}
		
		//勾选表父目录路径length>例外表父目录路径length 时在例外表中加一条目录授权
		List<String> parentFolderIds = getParentPath(folderPath);
		if(parentFolderIds == null) return;
		List<String> parentFolderPaths = generateFolderPaths(parentFolderIds);
		
		int checkLength = 0;
		List<OutlandPermissionCheckPO> checkEntities = outlandPermissionCheckDao.findBySerNodeNamePathAndFolderPathInAndPermissionTypeAndSourceTypeAndRoleId(serNodeNamePath, parentFolderPaths, PermissionType.valueOf(permissionType), SourceType.FOLDER, roleId);
		if(checkEntities!=null && checkEntities.size()>0){
			for(OutlandPermissionCheckPO checkEntity:checkEntities){
				if(checkEntity.getFolderPath()!=null && checkEntity.getFolderPath().length()>checkLength){
					checkLength = checkEntity.getFolderPath().length();
				}
			}
		}
		
		int exceptLength = 0;
		List<OutlandPermissionExceptPO> exceptEntities = outlandPermissionExceptDao.findBySerNodeNamePathAndFolderPathInAndPermissionTypeAndSourceTypeAndRoleId(serNodeNamePath, parentFolderPaths, PermissionType.valueOf(permissionType), SourceType.FOLDER, roleId);
		if(exceptEntities!=null && exceptEntities.size()>0){
			for(OutlandPermissionExceptPO exceptEntity:exceptEntities){
				if(exceptEntity.getFolderPath()!=null && exceptEntity.getFolderPath().length()>exceptLength){
					exceptLength = exceptEntity.getFolderPath().length();
				}
			}
		}
		
		if(checkLength > exceptLength){
			OutlandPermissionExceptPO outlandPermissionExceptEntity = new OutlandPermissionExceptPO();
			outlandPermissionExceptEntity.setSerNodeNamePath(serNodeNamePath);
			outlandPermissionExceptEntity.setFolderPath(folderPath);
			outlandPermissionExceptEntity.setName(name);
			outlandPermissionExceptEntity.setPermissionType(PermissionType.valueOf(permissionType));
			outlandPermissionExceptEntity.setSourceType(SourceType.FOLDER);
			outlandPermissionExceptEntity.setRoleId(roleId);
			outlandPermissionExceptDao.save(outlandPermissionExceptEntity);
		}
		
	}
	
	/**
	 * 获取一个目录路径的父目录br/>
	 * <p>
	 * 	例如：输入 /a/b/c<br/>
	 *      返回 [a,b]
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月9日 下午2:38:56
	 * @param String folderPath 目录路径
	 * @return List<String> 父目录id列表
	 */
	public List<String> getParentPath(String folderPath) throws Exception{
		String[] folderIds = folderPath.split("/");
		if(folderIds.length > 2){
			List<String> parentFolderIds = new ArrayList<String>();
			for(int i=1; i<folderIds.length-1; i++){
				parentFolderIds.add(folderIds[i]);
			}
			return parentFolderIds;
		}else {
			return null;
		}
	}
	
	/**
	 * 根据目录id排列出所有父目录路径<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月9日 下午2:35:40
	 * @param String[] folderIds 当前目录id排列列表，比如获取/a/b/c的所有父目录,传入[a,b,c]
	 * @return List<String> 所有父目录路径列表   如上例返回[/a,/a/b,/a/b/c]
	 */
	public List<String> generateFolderPaths(List<String> folderIds) throws Exception{
		if(!"".equals(folderIds.get(0))){
			folderIds = new ArrayListWrapper<String>().add("").addAll(folderIds).getList();
		}
		List<String> parentFolderPaths = new ArrayList<String>();
		int folderPathNum = folderIds.size() - 1;
		for(int i=1; i<folderPathNum+1; i++){
			StringBufferWrapper path = new StringBufferWrapper();
			for(int j=1; j<=i; j++){
				path.append("/").append(folderIds.get(j));
			}
			parentFolderPaths.add(path.toString());
		}
		return parentFolderPaths;
	} 
	
	public static void main(String[] args) throws Exception{
		OutlandPermissionService service = new OutlandPermissionService();
		String path = "/a";
		List<String> pStrings = service.generateFolderPaths(Arrays.asList(path.split("/")));
		for(String p:pStrings){
			System.out.println(p);
		}
		List<String> parent = service.getParentPath(path);
		if(parent != null){
			System.out.print("[");
			for(int i=0; i<parent.size(); i++){
				System.out.print(parent.get(i));
				if(i!=parent.size()-1) System.out.print(",");
			}
			System.out.println("]");
		}else{
			System.out.println("null");
		}
	}
	
}
