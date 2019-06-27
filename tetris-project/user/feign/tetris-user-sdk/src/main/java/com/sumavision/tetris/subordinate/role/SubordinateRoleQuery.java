package com.sumavision.tetris.subordinate.role;

import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;


/**
 * 公司角色查询sdk接口<br/>
 * <b>作者:</b>ql<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年6月18日 上午10:21:45
 */
@Component
public class SubordinateRoleQuery {
	@Autowired
	SubordinateRoleFeign subordinateRoleFeign;
	/**
	 * 通过公司id查找公司管理员角色id<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月18日 上午10:21:59
	 * @param company 用户id
	 * @return SubordinaryRoleVO 角色
	 */
	public SubordinateRoleVO queryRoleByCompany(String company)throws Exception{
		return JsonBodyResponseParser.parseObject(subordinateRoleFeign.roleByCompany(company), SubordinateRoleVO.class);
	}
	
	/**
	 * 通过公司id查找所有公司角色<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月18日 上午10:21:59
	 * @param company 用户id
	 * @return List<SubordinaryRoleVO> 角色
	 */
	public List<SubordinateRoleVO> queryRolesByCompany(String company)throws Exception{
		return JsonBodyResponseParser.parseArray(subordinateRoleFeign.rolesByCompany(company), SubordinateRoleVO.class);
	}
	/**
	 * 通过角色id列表查找角色列表<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月18日 上午10:21:59
	 * @param  Set<Long> 用户id
	 * @return List<SubordinaryRoleVO> 角色列表
	 */
	public List<SubordinateRoleVO> queryRolesByIds(Set<Long> ids)throws Exception{
		JSONObject response = null;
		List<Long> list1 = new ArrayList<Long>(ids);
		response = subordinateRoleFeign.rolesByIds(list1.toString());
		return  JsonBodyResponseParser.parseArray(response, SubordinateRoleVO.class);
	}
	
	/**
	 * 通过角色id查找角色<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 上午10:21:59
	 * @param  Long 用户id
	 * @return SubordinaryRoleVO 角色
	 */
	public SubordinateRoleVO queryRoleById(Long id)throws Exception{
		JSONObject response = null;
		response = subordinateRoleFeign.roleById(id.toString());
		return JsonBodyResponseParser.parseObject(response, SubordinateRoleVO.class);
	}
	
	/**
	 * 通过角色id列表和公司id查找角色<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 上午10:21:59
	 * @return List<SubordinaryRoleVO> 角色
	 */
	public List<SubordinateRoleVO> queryRolesByIdsAndCompanyId(List<Long> roleIds,Long comapnyId)throws Exception{
		JSONObject response = null;
		response = subordinateRoleFeign.rolesByCompanyAndIds(comapnyId.toString(), roleIds.toString());
		return JsonBodyResponseParser.parseArray(response, SubordinateRoleVO.class);
	}
	
	/**
	 * 通过用户id查找角色<br/>
	 * <b>作者:</b>ql<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 上午10:21:59
	 * @return List<SubordinaryRoleVO> 角色
	 */
	public Long queryRolesByUserId(Long userId)throws Exception{
		JSONObject response = null;
		response = subordinateRoleFeign.queryByUser(userId.toString());
		return JsonBodyResponseParser.parseObject(response, Long.class);
	}
	
	
}
