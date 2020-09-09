package com.sumavision.bvc.control.device.monitor.osd;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdDAO;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdLayerPO;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdLayerService;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdLayerType;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdPO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody; 

@Controller
@RequestMapping(value = "/monitor/osd/layer")
public class MonitorOsdLayerController {

	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private MonitorOsdLayerService monitorOsdLayerService;
	
	@Autowired
	private MonitorOsdDAO monitorOsdDao;
	
	/**
	 * 查询类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月29日 上午8:59:43
	 * @return Set<String> 图层类型
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/types")
	public Object queryTypes(HttpServletRequest request) throws Exception{
		List<String> layerTypes = new ArrayListWrapper<String>().add(MonitorOsdLayerType.SUBTITLE.getName())
															    .add(MonitorOsdLayerType.DATE.getName())
															    .add(MonitorOsdLayerType.DATETIME.getName())
															    .add(MonitorOsdLayerType.DEVNAME.getName())
															    .getList();
		return layerTypes;
	}
	
	/**
	 * 查询osd下所有的图层<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月7日 上午10:39:57
	 * @param Long osdId osd id
	 * @return List<MonitorOsdLayerVO> 图层列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long osdId,
			HttpServletRequest request) throws Exception{
		
		MonitorOsdPO osd = monitorOsdDao.findOne(osdId);
		
		Set<MonitorOsdLayerPO> entities = osd.getLayers();
		
		List<MonitorOsdLayerVO> layers = MonitorOsdLayerVO.getConverter(MonitorOsdLayerVO.class).convert(entities, MonitorOsdLayerVO.class);
		
		Collections.sort(layers, new MonitorOsdLayerVO.MonitorOsdLayerComparator());
		
		return layers;
	}
	
	/**
	 * 添加osd字幕图层<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月7日 上午10:55:58
	 * @param Long osdId osd id
	 * @param int x 万分比坐标
	 * @param int y 万分比坐标
	 * @param int layerIndex 图层顺序
	 * @param Long subtitleId 字幕id
	 * @param String subtitleName 字幕名称
	 * @param String subtitleUsername 字幕创建用户
	 * @return MonitorOsdLayerVO 图层
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/subtitle/layer")
	public Object addSubtitleLayer(
			Long osdId,
			int x,
			int y,
			int layerIndex,
			Long subtitleId,
			String subtitleName,
			String subtitleUsername,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		MonitorOsdLayerPO layer = monitorOsdLayerService.add(osdId, x, y, layerIndex, MonitorOsdLayerType.SUBTITLE, null, null, null, subtitleId, subtitleName, subtitleUsername, userId);
		
		return new MonitorOsdLayerVO().set(layer);
	}
	
	/**
	 * 添加日期图层<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月29日 上午9:37:36
	 * @param Long osdId osd id
	 * @param int x 横坐标百分比
	 * @param int y 纵坐标百分比
	 * @param int layerIndex 图层索引
	 * @param String font 字体
	 * @param Integer height 字号
	 * @param String color 颜色
	 * @return MonitorOsdLayerVO 图层
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/date/layer")
	public Object addDateLayer(
			Long osdId,
			int x,
			int y,
			int layerIndex,
			String font,
			Integer height,
			String color,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		MonitorOsdLayerPO layer = monitorOsdLayerService.add(osdId, x, y, layerIndex, MonitorOsdLayerType.DATE, font, height, color, null, null, null, userId);
		
		return new MonitorOsdLayerVO().set(layer);
	}
	
	/**
	 * 添加时间图层<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月29日 上午10:22:04
	 * @param Long osdId osd id
	 * @param int x 横坐标百分比
	 * @param int y 纵坐标百分比
	 * @param int layerIndex 图层索引
	 * @param String font 字体
	 * @param Integer height 字号
	 * @param String color 颜色
	 * @return MonitorOsdLayerVO 图层
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/datetime/layer")
	public Object addDatetimeLayer(
			Long osdId,
			int x,
			int y,
			int layerIndex,
			String font,
			Integer height,
			String color,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		MonitorOsdLayerPO layer = monitorOsdLayerService.add(osdId, x, y, layerIndex, MonitorOsdLayerType.DATETIME, font, height, color, null, null, null, userId);
		
		return new MonitorOsdLayerVO().set(layer);
	}
	
	/**
	 * 添加设备名称图层<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月29日 上午10:24:55
	 * @param Long osdId osd id
	 * @param int x 横坐标百分比
	 * @param int y 纵坐标百分比
	 * @param int layerIndex 图层索引
	 * @param String font 字体
	 * @param Integer height 字号
	 * @param String color 颜色
	 * @return MonitorOsdLayerVO 图层
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/devname/layer")
	public Object addDevnameLayer(
			Long osdId,
			int x,
			int y,
			int layerIndex,
			String font,
			Integer height,
			String color,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		MonitorOsdLayerPO layer = monitorOsdLayerService.add(osdId, x, y, layerIndex, MonitorOsdLayerType.DEVNAME, font, height, color, null, null, null, userId);
		
		return new MonitorOsdLayerVO().set(layer);
	}
	
	/**
	 * 删除图层<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月7日 上午11:31:44
	 * @param @PathVariable Long id 图层id
	 * @return List<MonitorOsdLayerVO> 剩余图层重新排序
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/{id}")
	public Object remove(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		List<MonitorOsdLayerPO> entities = monitorOsdLayerService.remove(id, userId);
		return MonitorOsdLayerVO.getConverter(MonitorOsdLayerVO.class).convert(entities, MonitorOsdLayerVO.class);
	}
	
	/**
	 * 修改字幕图层<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月7日 上午11:01:37
	 * @param Long id 图层id
	 * @param int x 万分比坐标
	 * @param int y 万分比坐标
	 * @param Long subtitleId 字幕id
	 * @param String subtitleName 字幕名称
	 * @param String subtitleUsername 字幕创建用户
	 * @return MonitorOsdLayerVO 图层
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/subtitle/layer/{id}")
	public Object editSubtitleLayer(
			@PathVariable Long id,
			int x,
			int y,
			Long subtitleId,
			String subtitleName,
			String subtitleUsername,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		MonitorOsdLayerPO layer = monitorOsdLayerService.edit(id, x, y, MonitorOsdLayerType.SUBTITLE, null, null, null, subtitleId, subtitleName, subtitleUsername, userId);
		
		return new MonitorOsdLayerVO().set(layer);
	}
	
	/**
	 * 修改内置图层<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月7日 上午11:01:37
	 * @param Long id 图层id
	 * @param int x 万分比坐标
	 * @param int y 万分比坐标
	 * @param String type 图层类型
	 * @param String font 字体
	 * @param Integer height 字号
	 * @param String color 颜色
	 * @return MonitorOsdLayerVO 图层
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/enum/layer/{id}")
	public Object editEnumLayer(
			@PathVariable Long id,
			int x,
			int y,
			String type,
			String font,
			Integer height,
			String color,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		MonitorOsdLayerPO layer = monitorOsdLayerService.edit(id, x, y, MonitorOsdLayerType.fromName(type), font, height, color, null, null, null, userId);
		
		return new MonitorOsdLayerVO().set(layer);
		
	}
	
	/**
	 * 图层排序<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月7日 上午11:36:57
	 * @param JSONArray layers 图层顺序 {id:'', layerIndex:''}
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/sort")
	public Object sort(
			String layers, 
			HttpServletRequest request) throws Exception{
		
		monitorOsdLayerService.sort(JSON.parseArray(layers));
		return null;
	}
	
}
