package com.sumavision.bvc.control.system.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import com.sumavision.bvc.control.system.vo.ChannelNameVO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.exception.CommonNameAlreadyExistedException;
import com.sumavision.bvc.system.dao.ChannelNameDAO;
import com.sumavision.bvc.system.po.ChannelNamePO;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.util.HttpServletRequestParser;

/**
 * @ClassName: 通道别名
 * @author zy
 * @date 2018年7月25日 下午8:42:02
 */
@Controller
@RequestMapping(value = "/system/channel/name")
public class ChannelNameController {
	
	@Autowired
	private ChannelNameDAO channelNameDAO;
	
	
	/**
	 * @Title: 查询页面枚举类型 
	 * @throws Exception 
	 * @return channelInfo：资源管理中的所有通道类型已经对应名称，格式：[{channelType:'', channelName:''}]
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/code", method = RequestMethod.GET)
	public Object queryCode(HttpServletRequest request) throws Exception{
		
		//TODO 向资源管理请求所有的通道类型以及通道名称
		//测试数据如下：
		ChannelType [] channelType = ChannelType.values();
		Set<Map<String, String>> channelInfo = new HashSet<Map<String,String>>();
		for(int i=0; i<channelType.length; i++){
			
			channelInfo.add(new HashMapWrapper<String, String>().put("channelType", channelType[i].toString())
																.put("channelName", channelType[i].getName())
																.getMap());
			
//			channelInfo.add(new HashMapWrapper<String, String>().put("channelType", new StringBufferWrapper().append("channel_")
//																											 .append(i+1)
//																											 .toString())
//					                                            .put("channelName", new StringBufferWrapper().append("通道_")
//					                                            											 .append(i+1)
//					                                            											 .toString())
//					                                            .getMap());
		}
		
		
		
		return new HashMapWrapper<String, Object>().put("channelInfo", channelInfo)
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
	@RequestMapping(value = "/load",method=RequestMethod.GET)
	public Object load(
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		PageRequest page = new PageRequest(currentPage-1, pageSize);
		
		Page<ChannelNamePO> pagedChannelNames = channelNameDAO.findAll(page);
		long total = pagedChannelNames.getTotalElements();
		List<ChannelNameVO> _channelNames = ChannelNameVO.getConverter(ChannelNameVO.class).convert(pagedChannelNames.getContent(), ChannelNameVO.class);

		JSONObject data = new JSONObject();
		data.put("rows", _channelNames);
		data.put("total", total);
		
		return data;
	}
	
	/**
	 * @Title: 新增数据 
	 * @param name
	 * @param channelName
	 * @param channelType
	 * @param status
	 * @throws Exception
	 * @return ChannelNameVO 通道别名数据
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/save")
	public Object save(
			String name,
			String channelName,
			String channelType,
			String status,
			HttpServletRequest request) throws Exception{
		
		if(channelNameDAO.findByName(name).size() > 0){
			throw new CommonNameAlreadyExistedException("通道别名", name);
		}
		
		ChannelNamePO cname = new ChannelNamePO();
		cname.setName(name);
		cname.setChannelName(channelName);
		cname.setChannelType(channelType);
		cname.setUpdateTime(new Date());
		channelNameDAO.save(cname);
		
		ChannelNameVO _cname = new ChannelNameVO().set(cname);
		
		return _cname;
	}
	
	/**
	 * @Title: 修改数据 
	 * @param id
	 * @param name
	 * @param channelName
	 * @param channelType
	 * @param status
	 * @param request
	 * @throws Exception
	 * @return ChannelNameVO 通道别名数据
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/update/{id}")
	public Object update(
			@PathVariable Long id,
			String name,
			String channelName,
			String channelType,
			String status,
			HttpServletRequest request) throws Exception{
		
		ChannelNamePO cname = channelNameDAO.findOne(id);
		if(!cname.getName().equals(name)){
			if(channelNameDAO.findByName(name).size() > 0){
				throw new CommonNameAlreadyExistedException("通道别名", name);
			}
		}
		cname.setName(name);
		cname.setChannelName(channelName);
		cname.setChannelType(channelType);
		cname.setUpdateTime(new Date());
		channelNameDAO.save(cname);
		
		ChannelNameVO _cname = new ChannelNameVO().set(cname);
	
		return _cname;
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
		channelNameDAO.delete(id);
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
		channelNameDAO.deleteByIdIn(ids);
		return null;
	}
}
