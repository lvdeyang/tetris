package com.sumavision.bvc.device.command.cloud;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.constant.BusinessConstants.BUSINESS_OPR_TYPE;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.group.bo.BundleBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.bo.PtzctrlPassByContent;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.monitor.point.MonitorPointPO;
import com.sumavision.bvc.device.monitor.point.MonitorPointService;
import com.sumavision.bvc.device.monitor.ptzctrl.ApertureControl;
import com.sumavision.bvc.device.monitor.ptzctrl.Direction;
import com.sumavision.bvc.device.monitor.ptzctrl.FocusControl;
import com.sumavision.bvc.device.monitor.ptzctrl.MonitorPtzctrlService;
import com.sumavision.bvc.device.monitor.ptzctrl.ZoomControl;

@Service
@Transactional(rollbackFor = Exception.class)
public class CloudControlService {
	
	private static final String PASSBY_TYPE = "ptzctrl";
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	private MonitorPtzctrlService monitorPtzctrlService;
	
	@Autowired
	private MonitorPointService monitorPointService;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness = new ExecuteBusinessProxy();
	
	/**
	 * 竖直方向移动镜头<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月20日 下午7:06:49
	 * @param int locationIndex 播放器位置索引
	 * @param Direction direction 方向描述
	 * @param String speed 速度
	 * @param Long userId 业务用户
	 */
	public void vertical(
			int locationIndex,
			Direction direction, 
			String speed, 
			Long userId) throws Exception{
		
		BundleBO bundle = commandCommonServiceImpl.queryBundleByPlayerIndexForCloudControl(userId, locationIndex);
		monitorPtzctrlService.vertical(bundle.getBundleId(), bundle.getNodeUid(), direction, speed, userId);
	}
	
	/**
	 * 水平方向移动镜头<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月20日 下午7:06:49
	 * @param int locationIndex 播放器位置索引
	 * @param Direction direction 方向描述
	 * @param String speed 速度
	 * @param Long userId 业务用户
	 */
	public void horizontal(
			int locationIndex,
			Direction direction, 
			String speed, 
			Long userId) throws Exception{
		
		BundleBO bundle = commandCommonServiceImpl.queryBundleByPlayerIndexForCloudControl(userId, locationIndex);
		monitorPtzctrlService.horizontal(bundle.getBundleId(), bundle.getNodeUid(), direction, speed, userId);
	}
	
	/**
	 * 其它方向移动摄像头：左上、左下、右上、右下<br/>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月19日 下午7:06:49
	 * @param int locationIndex 播放器位置索引
	 * @param Direction direction 方向描述
	 * @param String speed 速度
	 * @param Long userId 业务用户
	 */
	public void otherDirection(
			int locationIndex,
			Direction direction, 
			String speed, 
			Long userId) throws Exception{
		
		BundleBO bundle = commandCommonServiceImpl.queryBundleByPlayerIndexForCloudControl(userId, locationIndex);
		monitorPtzctrlService.otherDirection(bundle.getBundleId(), bundle.getNodeUid(), direction, speed, userId);
	}

	/**
	 * 镜头变倍控制<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月20日 下午7:06:49
	 * @param int locationIndex 播放器位置索引
	 * @param ZoomControl direction 缩放描述
	 * @param String speed 速度
	 * @param Long userId 业务用户
	 */
	public void zoom(
			int locationIndex,
			ZoomControl direction, 
			String speed, 
			Long userId) throws Exception{
		
		BundleBO bundle = commandCommonServiceImpl.queryBundleByPlayerIndexForCloudControl(userId, locationIndex);
		commandCommonServiceImpl.authorizeBundle(bundle.getBundleId(), userId, BUSINESS_OPR_TYPE.DIANBO);
		monitorPtzctrlService.zoom(bundle.getBundleId(), bundle.getNodeUid(), direction, speed, userId);
	}
	
	/**
	 * 焦距控制<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月20日 下午7:06:49
	 * @param int locationIndex 播放器位置索引
	 * @param FocusControl direction 缩放描述
	 * @param String speed 速度
	 * @param Long userId 业务用户
	 */
	public void focus(
			int locationIndex,
			FocusControl direction, 
			String speed, 
			Long userId) throws Exception{

		BundleBO bundle = commandCommonServiceImpl.queryBundleByPlayerIndexForCloudControl(userId, locationIndex);
		monitorPtzctrlService.focus(bundle.getBundleId(), bundle.getNodeUid(), direction, speed, userId);
	}
	
	/**
	 * 光圈控制<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月20日 下午7:06:49
	 * @param int locationIndex 播放器位置索引
	 * @param ApertureControl direction 缩放描述
	 * @param String speed 速度
	 * @param Long userId 业务用户
	 */
	public void aperture(
			int locationIndex,
			ApertureControl direction, 
			String speed, 
			Long userId) throws Exception{
		
		BundleBO bundle = commandCommonServiceImpl.queryBundleByPlayerIndexForCloudControl(userId, locationIndex);
		monitorPtzctrlService.aperture(bundle.getBundleId(), bundle.getNodeUid(), direction, speed, userId);
	}
	
	/**
	 * 停止控制<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月20日 下午7:06:49
	 * @param int locationIndex 播放器位置索引
	 * @param Long userId 业务用户
	 */
	public void stop(
			int locationIndex,
			Long userId) throws Exception{
		
		BundleBO bundle = commandCommonServiceImpl.queryBundleByPlayerIndexForCloudControl(userId, locationIndex);
		monitorPtzctrlService.stop(bundle.getBundleId(), bundle.getNodeUid(), userId);
	}
	
	/**
	 * 添加预置点<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 上午11:15:35
	 * @param int locationIndex 播放器位置索引
	 * @param String name 预置点名称
	 * @param Long userId 用户id
	 * @param String username 用户名称
	 * @return MonitorPointPO 预置点
	 */
	public MonitorPointPO add(
			int locationIndex,
			String name,
			Long userId,
			String username) throws Exception{
		
		BundleBO bundle = commandCommonServiceImpl.queryBundleByPlayerIndexForCloudControl(userId, locationIndex);
		MonitorPointPO point = monitorPointService.add(bundle.getBundleId(), bundle.getName(), bundle.getNodeUid(), name, userId, username);
		return point;
	}
	
	/**
	 * 校验权限<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月9日 下午3:52:21
	 * @throws Exception 
	 */
	public void checkPrivilege(
			int locationIndex,
			Long userId,
			BUSINESS_OPR_TYPE type) throws Exception{
		BundleBO bundle = commandCommonServiceImpl.queryBundleByPlayerIndexForCloudControl(userId, locationIndex);
		commandCommonServiceImpl.authorizeBundle(bundle.getBundleId(), userId, BUSINESS_OPR_TYPE.CLOUD);
	}
	
}
