package com.sumavision.tetris.subordinate.role;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
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
		return response != null ? JsonBodyResponseParser.parseObject(response, Long.class) : null;
	}
	
	/**
	 * 通过角色id列表查找角色列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月18日 上午10:21:59
	 * @param  Set<Long> 用户id
	 * @return List<SubordinaryRoleVO> 角色列表
	 */
	public List<SubordinateRoleVO> findByIdIn(Collection<Long> ids)throws Exception{
		return JsonBodyResponseParser.parseArray(subordinateRoleFeign.findByIdIn(JSON.toJSONString(ids)), SubordinateRoleVO.class);
	}
	
	/**
	 * 通过角色id查找角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月19日 上午10:21:59
	 * @param  Long 用户id
	 * @return SubordinaryRoleVO 角色
	 */
	public SubordinateRoleVO findById(Long id)throws Exception{
		return JsonBodyResponseParser.parseObject(subordinateRoleFeign.findById(id), SubordinateRoleVO.class);
	}
	
	/**
	 * 查询公司下的业务角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月16日 上午9:16:45
	 * @param Long companyId 公司id
	 * @return List<SubordinateRoleVO> 角色列表
	 */
	public List<SubordinateRoleVO> findByCompanyId(Long companyId) throws Exception{
		return JsonBodyResponseParser.parseArray(subordinateRoleFeign.findByCompanyId(companyId), SubordinateRoleVO.class);
	}
	
	/**
	 * 查询公司下的业务角色（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月16日 上午9:19:01
	 * @param Long companyId 公司id
	 * @param Collection<Long> except 例外角色id列表
	 * @return List<SubordinateRoleVO> 角色列表
	 */
	public List<SubordinateRoleVO> findByCompanyIdWithExcept(Long companyId, Collection<Long> except) throws Exception{
		return JsonBodyResponseParser.parseArray(subordinateRoleFeign.findByCompanyIdWithExcept(companyId, JSON.toJSONString(except)), SubordinateRoleVO.class);
	}
	
}
