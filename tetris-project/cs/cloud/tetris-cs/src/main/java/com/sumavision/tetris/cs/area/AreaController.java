package com.sumavision.tetris.cs.area;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.cs.channel.ChannelPO;
import com.sumavision.tetris.cs.channel.ChannelVO;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/cs/area")
public class AreaController {
	@Autowired
	AreaDAO areaDao;
	
	@Autowired
	AreaQuery areaQuery;
	
	@Autowired
	AreaService areaService;
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object getAreaList(Long channelId,HttpServletRequest request) throws Exception {
		
		return areaQuery.getAreaList(channelId);
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/set")
	public Object setAreaList(Long channelId,String areaListStr,HttpServletRequest request) throws Exception{
		
		List<AreaVO> areaList = JSON.parseArray(areaListStr,AreaVO.class);
		
		return areaService.setCheckArea(channelId,areaList);
	}
}
