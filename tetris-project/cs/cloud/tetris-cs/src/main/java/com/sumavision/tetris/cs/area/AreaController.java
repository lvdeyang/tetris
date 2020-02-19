package com.sumavision.tetris.cs.area;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
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

	/**
	 * 获取根地区<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/list")
	public Object getAreaList(Long channelId, HttpServletRequest request) throws Exception {

		// return areaQuery.getAreaList(channelId);

		return areaQuery.getRootList(channelId);
	}

	/**
	 * 设置频道播发地区<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param channelId 频道id
	 * @param areaListStr 地区列表
	 * @return List<AreaVO> 地区列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/set")
	public Object setAreaList(Long channelId, String areaListStr, HttpServletRequest request) throws Exception {

		List<AreaVO> areaList = JSON.parseArray(areaListStr, AreaVO.class);

		return areaService.setCheckArea(channelId, areaList, false);
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/set/force")
	public Object setAreaListForce(Long channelId, String areaListStr, HttpServletRequest request) throws Exception {
		List<AreaVO> areaList = JSON.parseArray(areaListStr, AreaVO.class);
		return areaService.setCheckArea(channelId, areaList, true);
	}

	/**
	 * 获取子地区<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param channelId 频道id
	 * @param disabled 是否可用(播发过的频道未播发的地区则下次播发不可用)
	 * @param divisionId 地区id
	 * @return List<AreaVO> 地区列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/children")
	public Object getDivisionChild(Long channelId, Boolean disabled, String divisionId, HttpServletRequest request)
			throws Exception {

		return areaQuery.getChildList(channelId, divisionId, disabled);
	}
}
