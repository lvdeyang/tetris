package com.sumavision.tetris.system.role;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mvc.ext.response.parser.JsonBodyResponseParser;

/**
 * 用户系统角色查询sdk接口<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月11日 下午3:21:45
 */
@Component
public class SystemRoleQuery {

	@Autowired
	private SystemRoleFeign systemRoleFeign;
	
	/**
	 * 查询用户的所有系统角色，包含组织机构关联角色，取并集<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月11日 下午3:21:59
	 * @param String userId 用户id
	 * @return List<SystemRoleVO> 角色列表
	 */
	@Deprecated
	public List<SystemRoleVO> queryUserRoles(String userId) throws Exception{
		return new ArrayListWrapper<SystemRoleVO>().add(new SystemRoleVO().setRoleId("1").setRoleName("菜单运维").setLevel_1(SystemRoleLevel.SYSTEM_ADMIN).setLevel_2(SystemRoleLevel.MENU))
												   .add(new SystemRoleVO().setRoleId("2").setRoleName("流程运维").setLevel_1(SystemRoleLevel.SYSTEM_ADMIN).setLevel_2(SystemRoleLevel.MENU))
												   .add(new SystemRoleVO().setRoleId("3").setRoleName("个人用户").setLevel_1(SystemRoleLevel.BUSINESS).setLevel_2(SystemRoleLevel.NORMAL))
												   .add(new SystemRoleVO().setRoleId("4").setRoleName("企业用户").setLevel_1(SystemRoleLevel.BUSINESS).setLevel_2(SystemRoleLevel.COMPANY_USER))
												   .add(new SystemRoleVO().setRoleId("5").setRoleName("企业管理员").setLevel_1(SystemRoleLevel.BUSINESS).setLevel_2(SystemRoleLevel.COMPANY_ADMIN))
												   .getList();
	}
	
	/**
	 * 分组查询系统角色sdk接口（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月17日 下午1:09:53
	 * @param Collection<String> roleIds 例外系统角色id列表
	 * @return List<SystemRoleGroupVO> 分组后的系统角色
	 */
	public List<SystemRoleGroupVO> listWithGroupByExceptIds(Collection<String> roleIds) throws Exception{
		JSONObject response = null;
		if(roleIds==null || roleIds.size()<=0){
			response = systemRoleFeign.listWithGroupByExceptIds(null);
		}else{
			response = systemRoleFeign.listWithGroupByExceptIds(JSON.toJSONString(roleIds));
		}
		return JsonBodyResponseParser.parseArray(response, SystemRoleGroupVO.class);
	}
	
	/**
	 * 根据给定的系统角色id分组查询系统角色sdk接口<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月17日 下午1:09:53
	 * @param Collection<String> roleIds 给定的系统角色id列表
	 * @return List<SystemRoleGroupVO> 分组后的系统角色
	 */
	public List<SystemRoleGroupVO> listWithGroupByIds(Collection<String> roleIds) throws Exception{
		JSONObject response = null;
		if(roleIds==null || roleIds.size()<=0){
			response = systemRoleFeign.listWithGroupByIds(null);
		}else{
			response = systemRoleFeign.listWithGroupByIds(JSON.toJSONString(roleIds));
		}
		return JsonBodyResponseParser.parseArray(response, SystemRoleGroupVO.class);
	}
	
	/**
	 * 根据id（批量）查询系统角色列表sdk接口<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月17日 下午1:41:09
	 * @param Collection<String> roleIds 给定的系统角色id列表
	 * @return List<SystemRoleVO> 系统角色列表
	 */
	public List<SystemRoleVO> listByIds(Collection<String> roleIds) throws Exception{
		JSONObject response = null;
		if(roleIds==null || roleIds.size()<=0){
			response = systemRoleFeign.listByIds(null);
		}else{
			response = systemRoleFeign.listByIds(JSON.toJSONString(roleIds));
		}
		return JsonBodyResponseParser.parseArray(response, SystemRoleVO.class);
	}
	
}
