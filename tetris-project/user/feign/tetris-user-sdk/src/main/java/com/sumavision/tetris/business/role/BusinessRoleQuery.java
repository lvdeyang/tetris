package com.sumavision.tetris.business.role;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;
import com.sumavision.tetris.system.role.SystemRoleVO;

@Component
public class BusinessRoleQuery {

	@Autowired
	private BusinessRoleFeign businessRoleFeign;
	
	/**
	 * 根据id查询角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月8日 下午1:44:02
	 * @param Collection<Long> ids 角色id列表 
	 * @return List<SystemRoleVO> 角色列表
	 */
	public List<SystemRoleVO> findByIdIn(Collection<Long> ids) throws Exception{
		if(ids==null || ids.size()<=0) return null;
		return JsonBodyResponseParser.parseArray(businessRoleFeign.findByIdIn(JSON.toJSONString(ids)), SystemRoleVO.class);
	}
	
	/**
	 * 分页查询企业业务角色（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月2日 下午5:07:34
	 * @param Collection<Long> except 例外角色id列表
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return long total 总数据量
	 * @return List<SystemRoleVO> rows 业务角色列表
	 */
	public Map<String, Object> listWithExceptIds(
			Collection<Long> except,
			int currentPage,
			int pageSize) throws Exception{
		if(except == null){
			return JsonBodyResponseParser.parseObject(businessRoleFeign.listWithExceptIds(null, currentPage, pageSize), Map.class);
		}else{
			return JsonBodyResponseParser.parseObject(businessRoleFeign.listWithExceptIds(JSON.toJSONString(except), currentPage, pageSize), Map.class);
		}
	}
	
	/**
	 * 分页查询企业业务角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月2日 下午5:07:34
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return long total 总数据量
	 * @return List<SystemRoleVO> rows 业务角色列表
	 */
	public Map<String, Object> list(
			int currentPage,
			int pageSize) throws Exception{
		return JsonBodyResponseParser.parseObject(businessRoleFeign.list(currentPage, pageSize), Map.class);
	}
	
	/**
	 * 查询企业管理员业务角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月7日 下午4:54:09
	 * @return SystemRoleVO 企业管理员业务角色
	 */
	public SystemRoleVO findCompanyAdminRole() throws Exception{
		return JsonBodyResponseParser.parseObject(businessRoleFeign.findCompanyAdminRole(), SystemRoleVO.class);
	}
	
	/**
	 * 查询用户私有角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月7日 上午10:54:41
	 * @param Long userId 用户id
	 * @return SystemRoleVO 私有角色
	 */
	public SystemRoleVO findPrivateRole(Long userId) throws Exception{
		return JsonBodyResponseParser.parseObject(businessRoleFeign.findPrivateRole(), SystemRoleVO.class);
	}
	
	/**
	 * 根据用户id列表查询私有角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月7日 下午1:47:44
	 * @param Collection<Long> userIds 用户id列表
	 * @return List<SystemRoleVO> 私有角色列表
	 */
	public List<SystemRoleVO> findPrivateRoleByUserIds(Collection<Long> userIds) throws Exception{
		return JsonBodyResponseParser.parseArray(businessRoleFeign.findPrivateRoleByUserIds(JSON.toJSONString(userIds)), SystemRoleVO.class);
	}
	
	/**
	 * 查询用户的业务角色<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月10日 上午9:51:25
	 * @param Long userId 用户id
	 * @return List<SystemRoleVO> 业务角色列表
	 */
	public List<SystemRoleVO> findBusinessRoleByUserId(Long userId) throws Exception{
		return JsonBodyResponseParser.parseArray(businessRoleFeign.findBusinessRoleByUserId(userId), SystemRoleVO.class);
	}
	
}
