package com.sumavision.bvc.control.device.monitor.point;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.monitor.point.MonitorPointDAO;
import com.sumavision.bvc.device.monitor.point.MonitorPointPO;
import com.sumavision.bvc.device.monitor.point.MonitorPointService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/monitor/point")
public class MonitorPointController {

	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private MonitorPointDAO monitorPointDao;
	
	@Autowired
	private MonitorPointService monitorPointService;
	
	/**
	 * 查询设备所有的预置点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 上午11:03:52
	 * @param String bundleId 设备id
	 * @return List<MonitorPointVO> 预置点列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			String bundleId,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		List<MonitorPointPO> entities = monitorPointDao.findByBundleId(bundleId);
		
		List<MonitorPointVO> points = new ArrayList<MonitorPointVO>();
		if(entities!=null && entities.size()>0){
			for(MonitorPointPO entity:entities){
				points.add(new MonitorPointVO().set(entity, userId));
			}
		}
		
		return points;
	}
	
	/**
	 * 添加预置点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 上午11:15:35
	 * @param String bundleId 设备id
	 * @param String bundleName 设备名称
	 * @param String layerId 接入层id
	 * @param String name 预置点名称
	 * @return MonitorPointVO 预置点
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String bundleId,
			String bundleName,
			String layerId,
			String name,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		MonitorPointPO entity = monitorPointService.add(bundleId, bundleName, layerId, name, user.getId(), user.getName());
		
		return new MonitorPointVO().set(entity, user.getId());
	}
	
	/**
	 * 设置预置点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 上午11:45:12
	 * @param @PathVariable Long id 预置点id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/invoke/{id}")
	public Object invoke(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		monitorPointService.invoke(id, userId);
		
		return null;
	}
	
	/**
	 * 删除预置点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 上午11:51:05
	 * @param @PathVariable Long id 预置点id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/{id}")
	public Object remove(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		monitorPointService.remove(id, userId);
		
		return null;
	}
	
}
