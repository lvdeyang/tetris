package com.sumavision.bvc.control.system.controller;

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
import com.sumavision.bvc.control.system.vo.AvtplVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.device.group.service.ConfigServiceImpl;
import com.sumavision.bvc.device.system.AvtplService;
import com.sumavision.bvc.system.dao.AvtplDAO;
import com.sumavision.bvc.system.enumeration.AudioFormat;
import com.sumavision.bvc.system.enumeration.AvtplUsageType;
import com.sumavision.bvc.system.enumeration.VideoFormat;
import com.sumavision.bvc.system.po.AvtplPO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.util.HttpServletRequestParser;


/**
 * @ClassName: 参数模板接口 
 * @author lvdeyang
 * @date 2018年7月25日 上午9:01:14 
 */
@Controller
@RequestMapping(value = "/system/avtpl")
public class AvtplController {
	
	@Autowired
	private AvtplDAO conn_avtpl;
	
	@Autowired
	private AvtplService avtplService;
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private ConfigServiceImpl configServiceImpl;
	
	/**
	 * @Title: 获取表中枚举字段的枚举列表 
	 * @return Object    返回类型 
	 * @throws Exception
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/code", method=RequestMethod.GET)
	public Object queryCode(HttpServletRequest request) throws Exception{
		
		Set<String> videoFormats = new HashSet<String>();
		VideoFormat[] videoFormatEnums = VideoFormat.values();
		for(VideoFormat format:videoFormatEnums){
			videoFormats.add(format.getName());
		}
		
		Set<String> audioFormats = new HashSet<String>();
		AudioFormat[] audioFormatEnums = AudioFormat.values();
		for(AudioFormat format:audioFormatEnums){
			audioFormats.add(format.getName());
		}
		
		Set<String> usageTypes = new HashSet<String>();
		AvtplUsageType [] usageTypeEnums = AvtplUsageType.values();
		for(AvtplUsageType type:usageTypeEnums){
			usageTypes.add(type.getName());
		}
		
		//系统版本号
		String version = configServiceImpl.getVersion();
		
		return new HashMapWrapper<String, Object>().put("videoFormats", videoFormats)
												   .put("audioFormats", audioFormats)
												   .put("usageTypes", usageTypes)
												   .put("version", version)
												   .getMap();
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
	@RequestMapping(value = "/load", method=RequestMethod.GET)
	public Object load(
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		avtplService.generateDefaultAvtpls();
		
		PageRequest page = new PageRequest(currentPage-1, pageSize);
		
		Page<AvtplPO> pagedTpls = conn_avtpl.findAll(page);
		long total = pagedTpls.getTotalElements();
		List<AvtplVO> _tpls = AvtplVO.getConverter(AvtplVO.class).convert(pagedTpls.getContent(), AvtplVO.class);

		JSONObject data = new JSONObject();
		data.put("rows", _tpls);
		data.put("total", total);
		
		return data;
	}
	
	/**
	 * @Title: 新增数据 
	 * @param name
	 * @param videoFormat
	 * @param videoFormatSpare
	 * @param audioFormat
	 * @param usageType
	 * @param request
	 * @throws Exception
	 * @return Object 参数数据
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/save")
	public Object save(
			String name,
			String videoFormat,//（二轮重构多码率不再使用）
			String videoFormatSpare,//（二轮重构多码率不再使用）
			String audioFormat,//（二轮重构多码率不再使用）
			String usageType,
			Boolean mux,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		if(!usageType.equals(AvtplUsageType.VOD.toString())){
			mux = false;
		}
		
		AvtplPO tpl = avtplService.save(name, videoFormat, videoFormatSpare, audioFormat, usageType, mux, userId);
		
		AvtplVO _tpl = new AvtplVO().set(tpl);
		
		return _tpl;
	}
	
	/**
	 * @Title: 修改数据 
	 * @param id
	 * @param name
	 * @param videoFormat
	 * @param videoFormatSpare
	 * @param audioFormat
	 * @param usageType
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
			String videoFormat,//（二轮重构多码率不再使用）
			String videoFormatSpare,//（二轮重构多码率不再使用）
			String audioFormat,//（二轮重构多码率不再使用）
			String usageType,
			Boolean mux,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		if(!usageType.equals(AvtplUsageType.VOD.getName())){
			mux = false;
		}
		
		AvtplPO tpl = avtplService.update(id, name, videoFormat, videoFormatSpare, audioFormat, usageType, mux, userId);
		
		AvtplVO _tpl = new AvtplVO().set(tpl);
		
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
		conn_avtpl.delete(id);
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
		conn_avtpl.deleteByIdIn(ids);
		return null;
	}
	
}
