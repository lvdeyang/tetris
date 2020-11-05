package com.sumavision.bvc.control.system.controller;

import java.util.Date;
import java.util.HashSet;
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
import com.sumavision.bvc.control.system.vo.RecordSchemeVO;
import com.sumavision.bvc.device.group.exception.CommonNameAlreadyExistedException;
import com.sumavision.bvc.system.dao.BusinessRoleDAO;
import com.sumavision.bvc.system.dao.RecordSchemeDAO;
import com.sumavision.bvc.system.dto.RecordSchemeDTO;
import com.sumavision.bvc.system.enumeration.BusinessRoleType;
import com.sumavision.bvc.system.po.BusinessRolePO;
import com.sumavision.bvc.system.po.RecordSchemePO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.util.HttpServletRequestParser;

/**
 * @ClassName: 录制方案
 * @author zy
 * @date 2018年7月26日 上午9:30:14 
 */
@Controller
@RequestMapping(value="/system/record/scheme")
public class RecordSchemeController {

	@Autowired
	private RecordSchemeDAO recordSchemeDAO;
	
	@Autowired
	private BusinessRoleDAO businessRoleDAO;
	
	/**
	 * @Title: 查询枚举类型 
	 * @throws Exception 
	 * @return role:List<BusinessRoleVO> 业务角色
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/code", method = RequestMethod.GET)
	public Object queryCode(HttpServletRequest request) throws Exception{
		
		//可录制的业务角色
		List<BusinessRolePO> roles = businessRoleDAO.findByType(BusinessRoleType.RECORDABLE);
		List<BusinessRoleVO> _roles = BusinessRoleVO.getConverter(BusinessRoleVO.class).convert(roles, BusinessRoleVO.class);
		
		return new HashMapWrapper<String, Object>().put("role", _roles)
												   .getMap();
	}
	
	/**
	 * @Title: 查询全部录制方案，适用于表格列检索 
	 * @throws Exception
	 * @return List<RecordSchemeVO> 录制方案
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/all", method = RequestMethod.GET)
	public Object queryAll(HttpServletRequest request) throws Exception{
		List<RecordSchemeDTO> records = recordSchemeDAO.findAllOutputDTO();
		List<RecordSchemeVO> _records = RecordSchemeVO.getConverter(RecordSchemeVO.class).convert(records, RecordSchemeVO.class);
		return _records;
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
	@RequestMapping(value = "/load",method=RequestMethod.GET)
	public Object load(
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		PageRequest page = new PageRequest(currentPage-1, pageSize);
		
		Page<RecordSchemeDTO> pagedRecords = recordSchemeDAO.findAllOutputDTO(page);
		long total = pagedRecords.getTotalElements();
		List<RecordSchemeVO> _records = RecordSchemeVO.getConverter(RecordSchemeVO.class).convert(pagedRecords.getContent(), RecordSchemeVO.class);

		JSONObject data = new JSONObject();
		data.put("rows", _records);
		data.put("total", total);
		
		return data;
	}
	
	/**
	 * @Title: 新增数据 
	 * @param name
	 * @param roleId
	 * @throws Exception
	 * @return RecordSchemeVO 录制方案数据
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/save")
	public Object save(
			String name,
			Long roleId,
			HttpServletRequest request) throws Exception{
		
		if(recordSchemeDAO.findByName(name).size() > 0){
			throw new CommonNameAlreadyExistedException("录制方案", name);
		}
		
		RecordSchemePO recordSchemePO = new RecordSchemePO();
		recordSchemePO.setName(name);
		BusinessRolePO role = businessRoleDAO.findOne(roleId);
		recordSchemePO.setRole(role);
		if(role.getRecords() == null) role.setRecords(new HashSet<RecordSchemePO>());
		role.getRecords().add(recordSchemePO);
		recordSchemePO.setUpdateTime(new Date());
		recordSchemeDAO.save(recordSchemePO);
		
		RecordSchemeVO _record = new RecordSchemeVO().set(recordSchemePO);
		
		return _record;
	}
	
	/**
	 * @Title: 修改数据 
	 * @param id
	 * @param name
	 * @param roleId
	 * @throws Exception
	 * @return RecordSchemeVO 录制方案数据 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/update/{id}")
	public Object update(
			@PathVariable Long id,
			String name,
			Long roleId,
			HttpServletRequest request) throws Exception{
		
		RecordSchemePO recordSchemePO = recordSchemeDAO.findOne(id);
		
		if(!recordSchemePO.getName().equals(name)){
			if(recordSchemeDAO.findByName(name).size() > 0){
				throw new CommonNameAlreadyExistedException("录制方案", name);
			}
		}
		
		//解关联
		BusinessRolePO role = recordSchemePO.getRole();
		if(role != null){
			role.getRecords().remove(recordSchemePO);
		}
		recordSchemePO.setRole(null);
		
		//建立关联
		BusinessRolePO newRole = businessRoleDAO.findOne(roleId);
		if(newRole.getRecords() == null) newRole.setRecords(new HashSet<RecordSchemePO>());
		recordSchemePO.setRole(newRole);
		
		recordSchemePO.setName(name);
		recordSchemePO.setUpdateTime(new Date());
		recordSchemeDAO.save(recordSchemePO);
		
		RecordSchemeVO _record = new RecordSchemeVO().set(recordSchemePO);
		
		return _record;
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
		recordSchemeDAO.delete(id);
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
		recordSchemeDAO.deleteByIdIn(ids);
		return null;
	}

}
