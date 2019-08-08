package com.sumavision.tetris.mims.app.folder.feign;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mims.app.folder.FolderRolePermissionDAO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

/**
 * folder角色rest接口<br/>
 * <b>作者:</b>ql<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年6月25日 上午10:54:34
 */
@Controller
@RequestMapping(value = "/folder/feign")
public class FolderRolePermissionFeignController {
	@Autowired
	private FolderRolePermissionDAO folderRolePermissionDAO;
	/**
	 * 解除文件夹授权<br/>
	 * <p>包括文件夹的子文件夹一并解除授权</p>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:30:46
	 * @param SubordinateRoleVO role 待解除授权的角色
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete/by/role")
	public Object deleteByRole(
			String roleId, 
			HttpServletRequest request) throws Exception{
		folderRolePermissionDAO.deleteInBatch(folderRolePermissionDAO.findByRoleId(Long.parseLong(roleId)));
		return null;
	}
	
	/**
	 * 根据文件夹id获取文件夹信息<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:30:46
	 * @param Long folderId 文件夹id
	 */
	@RequestMapping(value = "/query")
	public Object getById(Long folderId, HttpServletRequest request)throws Exception{
		return folderRolePermissionDAO.findOne(folderId);
	};
}
