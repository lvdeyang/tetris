package com.sumavision.bvc.control.device.command.group.cloud;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.bvc.control.device.monitor.point.MonitorPointVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.command.cloud.CloudControlService;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.group.bo.BundleBO;
import com.sumavision.bvc.device.monitor.point.MonitorPointDAO;
import com.sumavision.bvc.device.monitor.point.MonitorPointPO;
import com.sumavision.bvc.device.monitor.point.MonitorPointService;
import com.sumavision.bvc.device.monitor.ptzctrl.ApertureControl;
import com.sumavision.bvc.device.monitor.ptzctrl.Direction;
import com.sumavision.bvc.device.monitor.ptzctrl.FocusControl;
import com.sumavision.bvc.device.monitor.ptzctrl.ZoomControl;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/zk/cloud/control")
public class CloudControlController {

	@Autowired
	private UserUtils userUtils;

	@Autowired
	private MonitorPointDAO monitorPointDao;
	
	@Autowired
	private CloudControlService cloudControlService;
	
	@Autowired
	private MonitorPointService monitorPointService;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	/**
	 * 竖直方向移动镜头<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月20日 下午7:06:49
	 * @param int serial 播放器位置索引
	 * @param String direction 方向描述
	 * @param String speed 速度
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/vertical")
	public Object vertical(
			String direction,
			int serial,
			String speed,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		cloudControlService.vertical(serial, Direction.valueOf(direction), speed, userId);
		
		return null;
	}
	
	/**
	 * 水平方向移动镜头<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月20日 下午7:06:49
	 * @param int serial 播放器位置索引
	 * @param String direction 方向描述
	 * @param String speed 速度
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/horizontal")
	public Object horizontal(
			int serial,
			String direction,
			String speed,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		cloudControlService.horizontal(serial, Direction.valueOf(direction), speed, userId);
		
		return null;
	}
	
	/**
	 * 镜头变倍控制<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月20日 下午7:06:49
	 * @param int serial 播放器位置索引
	 * @param String direction 缩放描述
	 * @param String speed 速度
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/zoom")
	public Object zoom(
			int serial,
			String direction,
			String speed,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		cloudControlService.zoom(serial, ZoomControl.valueOf(direction), speed, userId);
		
		return null;
	}
	
	/**
	 * 焦距控制<br/>
	 * <b>作者:</b>zsu<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月20日 下午7:06:49
	 * @param int serial 播放器位置索引
	 * @param String direction 缩放描述
	 * @param String speed 速度
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/focus")
	public Object focus(
			int serial,
			String direction, 
			String speed, 
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		cloudControlService.focus(serial, FocusControl.valueOf(direction), speed, userId);
		
		return null;
	}
	
	/**
	 * 光圈控制<br/>
	 * <b>作者:</b>zsu<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月20日 下午7:06:49
	 * @param int serial 播放器位置索引
	 * @param String direction 缩放描述
	 * @param String speed 速度
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/aperture")
	public Object aperture(
			int serial,
			String direction, 
			String speed, 
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		cloudControlService.aperture(serial, ApertureControl.valueOf(direction), speed, userId);
		
		return null;
	}
	
	/**
	 * 停止控制<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月20日 下午7:06:49
	 * @param int serial 播放器位置索引
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/stop")
	public Object stop(
			int serial,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		cloudControlService.stop(serial, userId);
		
		return null;
	}
	
	/**
	 * 查询设备所有的预置点<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月4日 上午11:53:52
	 * @param int serial 播放器位置索引
	 * @return List<MonitorPointVO> 预置点列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/points")
	public Object loadPoints(
			int serial,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		BundleBO bundle = commandCommonServiceImpl.queryBundleByPlayerIndexForCloudControl(userId, serial);
		List<MonitorPointPO> entities = monitorPointDao.findByBundleId(bundle.getBundleId());
		
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
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 上午11:15:35
	 * @param int serial 播放器位置索引
	 * @param String name 预置点名称
	 * @return MonitorPointVO 预置点
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/point")
	public Object addPoint(
			int serial,
			String name,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		MonitorPointPO entity = cloudControlService.add(serial, name, user.getId(), user.getName());
		
		return new MonitorPointVO().set(entity, user.getId());
	}
	
	/**
	 * 移动到预置点<br/>
	 * <b>作者:</b>zsu<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 上午11:45:12
	 * @param String id 预置点id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/invoke/point")
	public Object invokePoint(
			String id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		monitorPointService.invoke(Long.parseLong(id), userId);
		
		return null;
	}
	
	/**
	 * 删除预置点<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 上午11:51:05
	 * @param int serial 播放器位置索引（暂时无用）
	 * @param Long id 预置点id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/point")
	public Object removePoint(
			int serial,
			String id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		monitorPointService.remove(Long.parseLong(id), userId);
		
		return null;
	}
	
}
