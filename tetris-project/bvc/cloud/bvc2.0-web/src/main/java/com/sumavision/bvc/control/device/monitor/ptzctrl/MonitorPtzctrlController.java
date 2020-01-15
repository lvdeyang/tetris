package com.sumavision.bvc.control.device.monitor.ptzctrl;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.device.monitor.ptzctrl.ApertureControl;
import com.sumavision.bvc.device.monitor.ptzctrl.Direction;
import com.sumavision.bvc.device.monitor.ptzctrl.FocusControl;
import com.sumavision.bvc.device.monitor.ptzctrl.MonitorPtzctrlService;
import com.sumavision.bvc.device.monitor.ptzctrl.ZoomControl;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/monitor/ptzctrl")
public class MonitorPtzctrlController {

	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private MonitorPtzctrlService monitorPtzctrlService;
	
	/**
	 * 竖直方向移动镜头<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月20日 下午7:06:49
	 * @param String bundleId 设备id
	 * @param String layerId 接入层id
	 * @param @PathVariable String direction 方向描述
	 * @param String speed 速度
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/vertical/{direction}")
	public Object vertical(
			String bundleId,
			String layerId,
			@PathVariable String direction,
			String speed,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		monitorPtzctrlService.vertical(bundleId, layerId, Direction.valueOf(direction), speed, userId);
		
		return null;
	}
	
	/**
	 * 水平方向移动镜头<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月20日 下午7:06:49
	 * @param String bundleId 设备id
	 * @param String layerId 接入层id
	 * @param @PathVariable String direction 方向描述
	 * @param String speed 速度
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/horizontal/{direction}")
	public Object horizontal(
			String bundleId,
			String layerId,
			@PathVariable String direction,
			String speed,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		monitorPtzctrlService.horizontal(bundleId, layerId, Direction.valueOf(direction), speed, userId);
		
		return null;
	}
	
	/**
	 * 镜头变倍控制<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月20日 下午7:06:49
	 * @param String bundleId 设备id
	 * @param String layerId 接入层id
	 * @param @PathVariable String direction 缩放描述
	 * @param String speed 速度
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/zoom/{direction}")
	public Object zoom(
			String bundleId,
			String layerId,
			@PathVariable String direction,
			String speed,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		monitorPtzctrlService.zoom(bundleId, layerId, ZoomControl.valueOf(direction), speed, userId);
		
		return null;
	}
	
	/**
	 * 焦距控制<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月20日 下午7:06:49
	 * @param String bundleId 设备id
	 * @param String layerId 接入层id
	 * @param @PathVariable String direction 缩放描述
	 * @param String speed 速度
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/focus/{direction}")
	public Object focus(
			String bundleId, 
			String layerId, 
			@PathVariable String direction, 
			String speed, 
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		monitorPtzctrlService.focus(bundleId, layerId, FocusControl.valueOf(direction), speed, userId);
		
		return null;
	}
	
	/**
	 * 光圈控制<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月20日 下午7:06:49
	 * @param String bundleId 设备id
	 * @param String layerId 接入层id
	 * @param @PathVariable String direction 缩放描述
	 * @param String speed 速度
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/aperture/{direction}")
	public Object aperture(
			String bundleId, 
			String layerId, 
			@PathVariable String direction, 
			String speed, 
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		monitorPtzctrlService.aperture(bundleId, layerId, ApertureControl.valueOf(direction), speed, userId);
		
		return null;
	}
	
	/**
	 * 停止控制<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月20日 下午7:06:49
	 * @param String bundleId 设备id
	 * @param String layerId 接入层id
	 * @param Long userId 业务用户
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/stop")
	public Object stop(
			String bundleId, 
			String layerId, 
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		monitorPtzctrlService.stop(bundleId, layerId, userId);
		
		return null;
	}
	
}
