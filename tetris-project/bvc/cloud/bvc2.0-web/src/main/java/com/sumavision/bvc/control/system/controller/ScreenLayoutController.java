package com.sumavision.bvc.control.system.controller;

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
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.control.system.vo.ScreenLayoutVO;
import com.sumavision.bvc.system.dao.ScreenLayoutDAO;
import com.sumavision.bvc.system.po.ScreenLayoutPO;
import com.sumavision.bvc.system.po.ScreenPositionPO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.util.HttpServletRequestParser;


/**
 * @ClassName: 屏幕布局 
 * @author lvdeyang
 * @date 2018年7月26日 下午7:07:02 
 */
@Controller
@RequestMapping(value="/system/screen/layout")
public class ScreenLayoutController {
	
	@Autowired
	private ScreenLayoutDAO screenLayoutDAO;
	
	/**
	 * @Title: 查询全部屏幕布局，适用于表格列查询 
	 * @throws Exception    设定文件 
	 * @return List<ScreenLayoutVO> 屏幕布局
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/all", method = RequestMethod.GET)
	public Object queryAll(HttpServletRequest request) throws Exception{
		List<ScreenLayoutPO> layouts = screenLayoutDAO.findAll();
		List<ScreenLayoutVO> _layouts = ScreenLayoutVO.getConverter(ScreenLayoutVO.class).convert(layouts, ScreenLayoutVO.class);
		return _layouts;
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
		
		Page<ScreenLayoutPO> pageScreens = screenLayoutDAO.findAll(page);
		long total = pageScreens.getTotalElements();
		List<ScreenLayoutVO> _screens = ScreenLayoutVO.getConverter(ScreenLayoutVO.class).convert(pageScreens.getContent(), ScreenLayoutVO.class);

		JSONObject data = new JSONObject();
		data.put("rows", _screens);
		data.put("total", total);
		
		return data;
	}
	
	/**
	 * @Title: 新增数据 
	 * @param name
	 * @param websiteDraw
	 * @param request
	 * @throws Exception
	 * @return Object 参数数据
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/save")
	public Object save(
			String name,
			String websiteDraw,
			String position,
			HttpServletRequest request) throws Exception{
		
		List<ScreenPositionPO> positions = JSON.parseArray(position, ScreenPositionPO.class);
		
		ScreenLayoutPO layout = new ScreenLayoutPO();
		layout.setName(name);
		layout.setWebsiteDraw(websiteDraw);
		layout.setUpdateTime(new Date());
		
		//加关联
		if(layout.getPositions() == null) layout.setPositions(new HashSet<ScreenPositionPO>());
		layout.getPositions().addAll(positions);
		for(ScreenPositionPO p:positions){
			p.setLayout(layout);
		}
		
		screenLayoutDAO.save(layout);
		
		ScreenLayoutVO _layout = new ScreenLayoutVO().set(layout);
		
		return _layout;
	}
	
	/**
	 * @Title: 修改数据 
	 * @param id
	 * @param name
	 * @param websiteDraw
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
			String websiteDraw,
			String position,
			HttpServletRequest request) throws Exception{
		
		List<ScreenPositionPO> positions = JSON.parseArray(position, ScreenPositionPO.class);
		
		ScreenLayoutPO layout = screenLayoutDAO.findOne(id);
		layout.setName(name);
		layout.setWebsiteDraw(websiteDraw);
		layout.setUpdateTime(new Date());
		
		if(positions != null){
			//解关联
			Set<ScreenPositionPO> oldPositions = layout.getPositions();
			layout.getPositions().removeAll(oldPositions);
			for(ScreenPositionPO op:oldPositions){
				op.setLayout(null);
			}
			
			//加关联
			layout.getPositions().addAll(positions);
			for(ScreenPositionPO p:positions){
				p.setLayout(layout);
			}
		}
		
		screenLayoutDAO.save(layout);
		
		ScreenLayoutVO _layout = new ScreenLayoutVO().set(layout);
		
		return _layout;
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
		screenLayoutDAO.delete(id);
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
		screenLayoutDAO.deleteByIdIn(ids);
		return null;
	}
	


}
