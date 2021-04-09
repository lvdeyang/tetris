package com.sumavision.bvc.control.system.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.control.system.vo.AutoBuildAgendaVO;
import com.sumavision.bvc.control.system.vo.BusinessRoleVO;
import com.sumavision.bvc.control.system.vo.RecordSchemeVO;
import com.sumavision.bvc.control.system.vo.ScreenLayoutVO;
import com.sumavision.bvc.control.system.vo.TplVO;
import com.sumavision.bvc.device.group.enumeration.AutoBuildAgenda;
import com.sumavision.bvc.device.group.exception.CommonNameAlreadyExistedException;
import com.sumavision.bvc.system.dao.TplContentDAO;
import com.sumavision.bvc.system.dao.TplDAO;
import com.sumavision.bvc.system.dto.TplContentBusinessRoleDTO;
import com.sumavision.bvc.system.dto.TplContentRecordSchemeDTO;
import com.sumavision.bvc.system.dto.TplContentScreenLayoutDTO;
import com.sumavision.bvc.system.enumeration.TplContentType;
import com.sumavision.bvc.system.po.TplContentPO;
import com.sumavision.bvc.system.po.TplPO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.util.HttpServletRequestParser;

import lombok.experimental.var;


/**
 * @ClassName: 会议模板
 * @author zy
 * @date 2018年7月27日 下午02:01:14 
 *
 */
@Controller
@RequestMapping(value="/system/tpl")
public class TplController {
	
	@Autowired
	private TplDAO tplDAO;
	
	@Autowired
	private TplContentDAO tplContentDAO;
	
	/**
	 * @Title: 会议角色分页查询 
	 * @param pageSize 每页数据量
	 * @param currentPage 当前页
	 * @return rows 数据行
	 * @return total 总数据量
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value="/load",method=RequestMethod.GET)
	public Object roleLoad(
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		PageRequest page = new PageRequest(currentPage-1, pageSize);
		Page<TplPO> pagedTpls = tplDAO.findAll(page);
		long total = pagedTpls.getTotalElements();
		List<TplPO> tpls = pagedTpls.getContent();
		List<TplVO> _tpls = TplVO.getConverter(TplVO.class).convert(tpls, TplVO.class);
		if(_tpls.size() > 0){
			Set<Long> tplIds = new HashSet<Long>();
			for(TplVO _tpl:_tpls){
				tplIds.add(_tpl.getId());
			}
			
			//获取关联的业务角色
			List<TplContentBusinessRoleDTO> roles = tplContentDAO.findAllBusinessRolesByTplIdsOutputDTO(tplIds);
			
			//获取关联的录制方案
			List<TplContentRecordSchemeDTO> records = tplContentDAO.findAllRecordSchemesByTplIdsOutputDTO(tplIds);
			
			//获取关联的布局方案
			List<TplContentScreenLayoutDTO> layouts = tplContentDAO.findAllScreenLayoutsByTplIdsOutputDTO(tplIds);
			
			for(TplVO _tpl:_tpls){
				
				//关联默认议程
				if(_tpl.getAutoBuildAgendas() == null) _tpl.setAutoBuildAgendas(new ArrayList<AutoBuildAgendaVO>());
				List<AutoBuildAgenda> autoBuildAgendas = AutoBuildAgenda.fromAgendaIds(_tpl.getAutoBuildAgendaIds());				
				for(AutoBuildAgenda autoBuildAgenda:autoBuildAgendas){
					AutoBuildAgendaVO autoBuildAgendaVO = new AutoBuildAgendaVO().set(autoBuildAgenda);
					_tpl.getAutoBuildAgendas().add(autoBuildAgendaVO);
				}
				
				//关联业务角色
				if(_tpl.getRoles() == null) _tpl.setRoles(new ArrayList<BusinessRoleVO>());
				for(TplContentBusinessRoleDTO role:roles){
					if(role.getTplId().equals(_tpl.getId())){
						_tpl.getRoles().add(new BusinessRoleVO().set(role));
					}
				}
				
				//关联录制方案
				if(_tpl.getRecords() == null) _tpl.setRecords(new ArrayList<RecordSchemeVO>());
				for(TplContentRecordSchemeDTO record:records){
					if(record.getTplId().equals(_tpl.getId())){
						_tpl.getRecords().add(new RecordSchemeVO().set(record));
					}
				}
				
				//关联布局方案
				if(_tpl.getLayouts() == null) _tpl.setLayouts(new ArrayList<ScreenLayoutVO>());
				for(TplContentScreenLayoutDTO layout:layouts){
					if(layout.getTplId().equals(_tpl.getId())){
						_tpl.getLayouts().add(new ScreenLayoutVO().set(layout));
					}
				}
				
			}
		}
		
		JSONObject data = new JSONObject();
		data.put("rows", _tpls);
		data.put("total", total);
		
		return data;
	}
	
	/**
	 * @Title: 新增数据 
	 * @param name  模板名称
	 * @param autoBuildAgendaIds 逗号分隔的id字符串，参见枚举类AutoBuildAgenda
	 * @param request
	 * @throws Exception
	 * @return Object 参数数据
	 * 
	 *	新建空模板存储
	 * 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/save")
	public Object save(
			String name,
			String roles,
			String layouts,
			String records,
			String autoBuildAgendaIds,
			HttpServletRequest request) throws Exception{
		
		if(tplDAO.findByName(name).size() > 0){
			throw new CommonNameAlreadyExistedException("会议模板", name);
		}
		
		List<BusinessRoleVO> _roles = new ArrayList<BusinessRoleVO>();
		JSONArray roleJsons = JSON.parseArray(roles);
		for(int i=0; i<roleJsons.size(); i++){
			BusinessRoleVO _role = new BusinessRoleVO().setId(roleJsons.getJSONObject(i).getLong("id"))
													   .setName(roleJsons.getJSONObject(i).getString("name"));
			_roles.add(_role);
		}
		
		List<ScreenLayoutVO> _layouts = new ArrayList<ScreenLayoutVO>();
		JSONArray layoutJsons = JSON.parseArray(layouts);
		for(int i=0; i<layoutJsons.size(); i++){
			ScreenLayoutVO _layout = new ScreenLayoutVO().setId(layoutJsons.getJSONObject(i).getLong("id"))
														.setName(layoutJsons.getJSONObject(i).getString("name"));
			_layouts.add(_layout);
		}
		
		List<RecordSchemeVO> _records = new ArrayList<RecordSchemeVO>();
		JSONArray recordJsons = JSON.parseArray(records);
		for(int i=0; i<recordJsons.size(); i++){
			RecordSchemeVO _record = new RecordSchemeVO().setId(recordJsons.getJSONObject(i).getLong("id"))
					                                     .setName(recordJsons.getJSONObject(i).getString("name"));
			_records.add(_record);
		} 
		
		//添加一个会议模板
		TplPO tpl = new TplPO();
		tpl.setName(name);
		tpl.setUpdateTime(new Date());
		tpl.setContents(new HashSet<TplContentPO>());
		tpl.setAutoBuildAgendaIds(autoBuildAgendaIds);
		
		//添加会议模板内容
		for(BusinessRoleVO _role:_roles){
			TplContentPO roleContent = new TplContentPO();
			roleContent.setContentId(_role.getId());
			roleContent.setType(TplContentType.BUSINESSROLE);
			roleContent.setTpl(tpl);
			tpl.getContents().add(roleContent);
		}
		
		for(ScreenLayoutVO _layout:_layouts){
			TplContentPO layoutContent = new TplContentPO();
			layoutContent.setContentId(_layout.getId());
			layoutContent.setType(TplContentType.SCREENLAYOUT);
			layoutContent.setTpl(tpl);
			tpl.getContents().add(layoutContent);
		}
		
		for(RecordSchemeVO _record:_records){
			TplContentPO recordContent = new TplContentPO();
			recordContent.setContentId(_record.getId());
			recordContent.setType(TplContentType.RECORDSCHEME);
			recordContent.setTpl(tpl);
			tpl.getContents().add(recordContent);
		}
		
		tplDAO.save(tpl);
		
		TplVO _tpl = new TplVO().set(tpl);
		return _tpl;
	}
	
	/**
	 * @Title: 修改数据 -只修改模板名称
	 * @param id
	 * @param name
	 * @param autoBuildAgendaIds 逗号分隔的id字符串，参见枚举类AutoBuildAgenda
	 * @throws Exception
	 * @return Object 参数数据 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/update/{id}")
	public Object update(
			@PathVariable Long id,
			String name,
			String roles,
			String layouts,
			String records,
			String autoBuildAgendaIds,
			HttpServletRequest request) throws Exception{
		
		List<BusinessRoleVO> _roles = new ArrayList<BusinessRoleVO>();
		JSONArray roleJsons = JSON.parseArray(roles);
		for(int i=0; i<roleJsons.size(); i++){
			BusinessRoleVO _role = new BusinessRoleVO().setId(roleJsons.getJSONObject(i).getLong("id"))
													   .setName(roleJsons.getJSONObject(i).getString("name"));
			_roles.add(_role);
		}
		
		List<ScreenLayoutVO> _layouts = new ArrayList<ScreenLayoutVO>();
		JSONArray layoutJsons = JSON.parseArray(layouts);
		for(int i=0; i<layoutJsons.size(); i++){
			ScreenLayoutVO _layout = new ScreenLayoutVO().setId(layoutJsons.getJSONObject(i).getLong("id"))
														.setName(layoutJsons.getJSONObject(i).getString("name"));
			_layouts.add(_layout);
		}
		
		List<RecordSchemeVO> _records = new ArrayList<RecordSchemeVO>();
		JSONArray recordJsons = JSON.parseArray(records);
		for(int i=0; i<recordJsons.size(); i++){
			RecordSchemeVO _record = new RecordSchemeVO().setId(recordJsons.getJSONObject(i).getLong("id"))
					                                     .setName(recordJsons.getJSONObject(i).getString("name"));
			_records.add(_record);
		} 
		
		TplPO tpl = tplDAO.findOne(id);
		if(!tpl.getName().equals(name)){
			if(tplDAO.findByName(name).size() > 0){
				throw new CommonNameAlreadyExistedException("会议模板", name);
			}
		}
		tpl.setName(name);
		tpl.setUpdateTime(new Date());
		tpl.setAutoBuildAgendaIds(autoBuildAgendaIds);
		
		//旧数据解关联
		Set<TplContentPO> oldContents = tpl.getContents();
		for(TplContentPO content:oldContents){
			content.setTpl(null);
		}
		tpl.getContents().removeAll(oldContents);
		tplDAO.save(tpl);
		tplContentDAO.deleteInBatch(oldContents);
		
		//添加新会议模板内容
		for(BusinessRoleVO _role:_roles){
			TplContentPO roleContent = new TplContentPO();
			roleContent.setContentId(_role.getId());
			roleContent.setType(TplContentType.BUSINESSROLE);
			roleContent.setTpl(tpl);
			tpl.getContents().add(roleContent);
		}
		
		for(ScreenLayoutVO _layout:_layouts){
			TplContentPO layoutContent = new TplContentPO();
			layoutContent.setContentId(_layout.getId());
			layoutContent.setType(TplContentType.SCREENLAYOUT);
			layoutContent.setTpl(tpl);
			tpl.getContents().add(layoutContent);
		}
		
		for(RecordSchemeVO _record:_records){
			TplContentPO recordContent = new TplContentPO();
			recordContent.setContentId(_record.getId());
			recordContent.setType(TplContentType.RECORDSCHEME);
			recordContent.setTpl(tpl);
			tpl.getContents().add(recordContent);
		}
		
		tplDAO.save(tpl);
		
		TplVO _tpl = new TplVO().set(tpl);
		
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
		
		tplDAO.delete(id);
		
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
		tplDAO.deleteByIdIn(ids);
		return null;
	}

}
