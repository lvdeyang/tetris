package com.sumavision.tetris.mims.app.folder.feign;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderRolePermissionDAO;
import com.sumavision.tetris.mims.app.folder.FolderTreeVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

/**
 * folder瑙掕壊rest鎺ュ彛<br/>
 * <b>浣滆��:</b>ql<br/>
 * <b>鐗堟湰锛�</b>1.0<br/>
 * <b>鏃ユ湡锛�</b>2019骞�6鏈�25鏃� 涓婂崍10:54:34
 */
@Controller
@RequestMapping(value = "/folder/feign")
public class FolderRolePermissionFeignController {
	@Autowired
	private FolderRolePermissionDAO folderRolePermissionDAO;
	
	@Autowired
	private FolderDAO folderDAO;
	/**
	 * 瑙ｉ櫎鏂囦欢澶规巿鏉�<br/>
	 * <p>鍖呮嫭鏂囦欢澶圭殑瀛愭枃浠跺す涓�骞惰В闄ゆ巿鏉�</p>
	 * <b>浣滆��:</b>ql<br/>
	 * <b>鐗堟湰锛�</b>1.0<br/>
	 * <b>鏃ユ湡锛�</b>2019骞�6鏈�25鏃� 涓婂崍11:30:46
	 * @param SubordinateRoleVO role 寰呰В闄ゆ巿鏉冪殑瑙掕壊
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
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query")
	public Object getById(Long folderId, HttpServletRequest request)throws Exception{
		return new FolderTreeVO().set(folderDAO.findOne(folderId));
	};
}
