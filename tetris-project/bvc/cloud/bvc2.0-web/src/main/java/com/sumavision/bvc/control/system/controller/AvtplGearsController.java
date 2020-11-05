package com.sumavision.bvc.control.system.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.control.system.vo.AvtplGearsVO;
import com.sumavision.bvc.system.dao.AVtplGearsDAO;
import com.sumavision.bvc.system.dao.AvtplDAO;
import com.sumavision.bvc.system.enumeration.GearsLevel;
import com.sumavision.bvc.system.enumeration.Resolution;
import com.sumavision.bvc.system.po.AvtplGearsPO;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.util.HttpServletRequestParser;

/**
 * @className 参数模板
 * @author zy
 * @date 2018.07.30
 *
 */
@Controller
@RequestMapping(value="/system/avtpl/gears")
public class AvtplGearsController {
	
	@Autowired
	private AVtplGearsDAO avtplGearsDAO;
	
	@Autowired
	private AvtplDAO avtplDAO;
	
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/code", method = RequestMethod.GET)
	public Object queryCode(HttpServletRequest request) throws Exception{
		
		Set<String> _resolutions = new HashSet<String>();
		Resolution[] resolutions = Resolution.values();
		for(Resolution resolution:resolutions){
			_resolutions.add(resolution.getName());
		}
		
		Set<String> _gears = new HashSet<String>();
		GearsLevel[] gears = GearsLevel.values();
		for(GearsLevel gear:gears){
			_gears.add(gear.getName());
		}
		
		JSONObject data = new JSONObject();
		data.put("resolution", _resolutions);
		data.put("gear", _gears);
		
		return data;
	}
	
	/**
	 * @Title: 模板查询档位
	 * @param id 模板id
	 * @return avtplG:List<AvtplGearsVO> 业务角色
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/load/{id}",method=RequestMethod.GET)
	public Object load(
			@PathVariable Long id,
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		Set<AvtplGearsPO> gears = avtplDAO.findOne(id).getGears();
		
		List<AvtplGearsVO> _gears = AvtplGearsVO.getConverter(AvtplGearsVO.class).convert(gears, AvtplGearsVO.class);
		
		return new HashMapWrapper<String, Object>().put("rows", _gears)
												   .put("total", _gears.size())
												   .getMap();
	}
	
	/**
	 * @Title: 新增数据 
	 * @param name 档位名称
	 * @param id  参数模板id
	 * @param videoBitRate
	 * @param videoBitRateSpare
	 * @param videoResolution
	 * @param videoResolutionSpare
	 * @param audioBitRate
	 * @param level 
	 * @throws Exception
	 * @return Object 参数数据
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/save/{id}")
	public Object save(
			@PathVariable Long id,
			String name,
			String videoBitRate,
			String videoBitRateSpare,
			String videoResolution,
			String videoResolutionSpare,
			String fps,
			String audioBitRate,
			String level,
			HttpServletRequest request) throws Exception{
		
		AvtplPO tpl = avtplDAO.findOne(id);
		tpl.setUpdateTime(new Date());
		if(tpl.getGears() == null) tpl.setGears(new HashSet<AvtplGearsPO>());
		
		AvtplGearsPO gear = new AvtplGearsPO();
		gear.setName(name);
		gear.setVideoBitRate(videoBitRate);
		gear.setVideoBitRateSpare(videoBitRateSpare);
		gear.setVideoResolution(Resolution.fromName(videoResolution));
		gear.setVideoResolutionSpare(Resolution.fromName(videoResolutionSpare));
		if(!fps.contains(".")) fps+=".0";
		gear.setFps(fps);
		gear.setAudioBitRate(audioBitRate);
		gear.setLevel(GearsLevel.fromName(level));
		gear.setUpdateTime(new Date());
		gear.setAvtpl(tpl);
		tpl.getGears().add(gear);
		avtplGearsDAO.save(gear);
		
		AvtplGearsVO _gear = new AvtplGearsVO().set(gear);
		
		return _gear;
	}
	
	/**
	 * @Title: 修改数据 
	 * @param name 档位名称
	 * @param id  档位的id
	 * @param avtplId
	 * @param videoBitRate
	 * @param videoBitRateSpare
	 * @param videoResolution
	 * @param videoResolutionSpare
	 * @param audioBitRate
	 * @param level 
	 * @throws Exception
	 * @return Object 参数数据
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/update/{id}")
	public Object update(
			@PathVariable Long id,
			String name,
			String videoBitRate,
			String videoBitRateSpare,
			String videoResolution,
			String videoResolutionSpare,
			String fps,
			String audioBitRate,
			String level,
			HttpServletRequest request) throws Exception{
		
		AvtplGearsPO gear = avtplGearsDAO.findOne(id);
		gear.setName(name);
		gear.setVideoBitRate(videoBitRate);
		gear.setVideoBitRateSpare(videoBitRateSpare);
		gear.setVideoResolution(Resolution.fromName(videoResolution));
		gear.setVideoResolutionSpare(Resolution.fromName(videoResolutionSpare));
		if(!fps.contains(".")) fps+=".0";
		gear.setFps(fps);
		gear.setAudioBitRate(audioBitRate);
		gear.setLevel(GearsLevel.fromName(level));
		gear.setUpdateTime(new Date());
		avtplGearsDAO.save(gear);
		
		AvtplGearsVO _avtplGtpl = new AvtplGearsVO().set(gear);
		
		return _avtplGtpl;
	}
	
	/**
	 * @Title: 根据档位id删除数据 
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
		
		AvtplGearsPO gear = avtplGearsDAO.findOne(id);
		
		//解关联
		AvtplPO tpl = gear.getAvtpl();
		tpl.getGears().remove(gear);
		gear.setAvtpl(null);
		avtplDAO.save(tpl);
		
		avtplGearsDAO.delete(gear);
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
		
		//解关联
		List<AvtplGearsPO> gears = avtplGearsDAO.findAll(ids);
		AvtplPO tpl = gears.get(0).getAvtpl();
		for(AvtplGearsPO gear:gears){
			tpl.getGears().remove(gear);
			gear.setAvtpl(null);
		}
		avtplDAO.save(tpl);
		avtplGearsDAO.deleteByIdIn(ids);
		return null;
	}
	


}
