package com.sumavision.tetris.subordinate.role;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;



/**
 * 公司角色操作（主增删改）<br/>
 * <b>作者:</b>ql<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年6月18日 上午10:47:26
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SubordinateRoleService {
	@Autowired
	SubordinateRoleFeign subordinateRoleFeign;
	@Autowired
	private UserQuery userserQuery;
	
	/**
	 * 添加角色<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月18日 上午1103 
	 * @return JSONObject 公司角色
	 */
	public SubordinateRoleVO addRole(Long userId,Long companyId, String roleName,String upDate,String Removeable,String Serial) throws Exception {
		return JsonBodyResponseParser.parseObject(subordinateRoleFeign.add(userId.toString(), roleName, upDate, Removeable, Serial, companyId.toString()),SubordinateRoleVO.class);				
	}
	
	
	/**
	 * 修改角色<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月18日 上午11:05:03 
	 * @return JSONObject 公司角色
	 */
	public SubordinateRoleVO editRole(Long roleId, String roleName) throws Exception {
		SubordinateRoleVO vo = JsonBodyResponseParser.parseObject(subordinateRoleFeign.edit(roleId.toString(), roleName),SubordinateRoleVO.class);				
		return vo;
	}
	/**
	 * 删除角色<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月18日 上午11:05:03 
	 * @return JSONObject 公司角色
	 */
	public SubordinateRoleVO removeRole(Long roleId) throws Exception{
		SubordinateRoleVO vo = JsonBodyResponseParser.parseObject(subordinateRoleFeign.delet(roleId.toString()),SubordinateRoleVO.class);				
		return vo;
	}
}
