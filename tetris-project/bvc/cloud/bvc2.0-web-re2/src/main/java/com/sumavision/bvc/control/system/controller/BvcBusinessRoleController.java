package com.sumavision.bvc.control.system.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.control.system.vo.BusinessRoleVO;
import com.sumavision.bvc.device.group.exception.CommonNameAlreadyExistedException;
import com.sumavision.bvc.system.dao.BusinessRoleDAO;
import com.sumavision.bvc.system.enumeration.BusinessRoleSpecial;
import com.sumavision.bvc.system.enumeration.BusinessRoleType;
import com.sumavision.bvc.system.po.BusinessRolePO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.util.HttpServletRequestParser;

/**
 * @ClassName: 权限角色（原名BusinessRoleController，因为名称冲突所以加了个Bvc）
 * @author zy
 * @date 2018年7月25日 下午8:45:14 
 */
@Controller
@RequestMapping(value = "/system/business/role")
public class BvcBusinessRoleController {

	@Autowired
	private BusinessRoleDAO businessRoleDAO;
	
	/**
	 * @Title: 获取枚举类型 
	 * @throws Exception 
	 * @return specials 角色属性列表
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/code", method = RequestMethod.GET)
	public Object queryCode(HttpServletRequest request) throws Exception{
		List<String> specialArr = BusinessRoleSpecial.getVisiableList();
		List<String> typeArr = BusinessRoleType.getTotalList();
		return new HashMapWrapper<String, Object>().put("special", specialArr)
												   .put("type", typeArr)
												   .getMap();
	}
	
	/**
	 * @Title: 全检索，适用于表格多选列 
	 * @throws Exception
	 * @return List<BusinessRoleVO> 角色列表
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/all", method = RequestMethod.GET)
	public Object queryAll(HttpServletRequest request) throws Exception{
		List<BusinessRolePO> roles = businessRoleDAO.findAll();
		List<BusinessRoleVO> _roles = BusinessRoleVO.getConverter(BusinessRoleVO.class).convert(roles, BusinessRoleVO.class);
		return _roles;
	}
	
	/**
	 * @Title: 分页查询 
	 * @param pageSize 每页数据量
	 * @param currentPage 当前页
	 * @return rows 数据行
	 * @return total 总数据量
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public Object load(
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		PageRequest page = new PageRequest(currentPage-1, pageSize);
		
		Page<BusinessRolePO> pagedRoles = businessRoleDAO.findAll(page);
		long total = pagedRoles.getTotalElements();
		List<BusinessRoleVO> _roles = BusinessRoleVO.getConverter(BusinessRoleVO.class).convert(pagedRoles.getContent(), BusinessRoleVO.class);

		JSONObject data = new JSONObject();
		data.put("rows", _roles);
		data.put("total", total);
		
		return data;
	}
	
	/**
	 * @Title: 新增数据 
	 * @param name
	 * @param special
	 * @param request
	 * @throws Exception
	 * @return Object 参数数据
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/save")
	public Object save(
			String name,
			String special,
			String type,
			HttpServletRequest request) throws Exception{
		
		if(businessRoleDAO.findByName(name).size() > 0){
			throw new CommonNameAlreadyExistedException("通道别名", name);
		}
		
		BusinessRolePO role = new BusinessRolePO();
		role.setName(name);
		role.setSpecial(BusinessRoleSpecial.fromName(special));
		role.setType(BusinessRoleType.fromName(type));
		role.setUpdateTime(new Date());
		businessRoleDAO.save(role);
		
		BusinessRoleVO _role = new BusinessRoleVO().set(role);
		
		return _role;
	}
	
	/**
	 * @Title: 修改数据 
	 * @param id
	 * @param name
	 * @param special
	 * @param request
	 * @throws Exception
	 * @return Object 参数数据 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/update/{id}")
	public Object update(
			@PathVariable Long id,
			String name,
			String special,
			String type,
			HttpServletRequest request) throws Exception{
		
		BusinessRolePO tpl = businessRoleDAO.findOne(id);
		if(!tpl.getName().equals(name)){
			if(businessRoleDAO.findByName(name).size() > 0){
				throw new CommonNameAlreadyExistedException("通道别名", name);
			}
		}
		tpl.setName(name);
		tpl.setSpecial(BusinessRoleSpecial.fromName(special));
		tpl.setType(BusinessRoleType.fromName(type));
		tpl.setUpdateTime(new Date());
		businessRoleDAO.save(tpl);
		
		BusinessRoleVO _tpl = new BusinessRoleVO().set(tpl);
		
		return _tpl;
	}
	
	/**
	 * @Title: 根据id删除数据 
	 * @param id
	 * @param request
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/{id}")
	public Object remove(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		businessRoleDAO.delete(id);
		return null;
	}
	
	/**
	 * @Title: 根据id批量删除 
	 * @param ids
	 * @param request
	 * @throws Exception 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/remove/all")
	public Object removeAll(HttpServletRequest request) throws Exception{
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		List<Long> ids = JSON.parseArray(params.getString("ids"), Long.class);
		businessRoleDAO.deleteByIdIn(ids);
		return null;
	}

}
