package com.sumavision.tetris.mims.app.folder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.subordinate.role.SubordinateRoleVO;

/**
 * 文件夹授权相关操作<br/>
 * <b>作者:</b>ql<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年6月25日 上午11:27:23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FolderRolePermissionService {
	
	@Autowired 
	FolderRolePermissionFeign folderRolePermissionFeign;
	
	
	/**
	 * 解除文件夹授权<br/>
	 * <p>包括文件夹的子文件夹一并解除授权</p>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:30:46
	 * @param SubordinateRoleVO role 待解除授权的角色
	 */
	public void deletePermission(String roleId) throws Exception{	
		folderRolePermissionFeign.deleteByRole(roleId);
	}
}
