package com.sumavision.tetris.bvc.business.location;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.bvc.control.device.monitor.live.MonitorLiveUtil;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.monitor.live.device.MonitorLiveDevicePO;
import com.sumavision.bvc.device.monitor.live.device.MonitorLiveDeviceService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.user.UserQuery;

/**
 * <p>详细描述</p>
 * <b>作者:</b>lixin<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年11月4日 下午7:16:55
 */
@Service
public class LocationOfScreenWallService {
	
	@Autowired
	private LocationTemplateLayoutDAO locationTemplateLayoutDao;
	
	@Autowired
	private LocationOfScreenWallDAO locationOfScreenWallDao;
	
	@Autowired
	private MonitorLiveUtil monitorLiveUtil;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private MonitorLiveDeviceService monitorLiveDeviceService;

	/**
	 * 绑定解码器<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月3日 上午11:06:12
	 * @param bundleId 设备id
	 * @param bundleName 设备name
	 * @param locationX x方向位置
	 * @param locationY y方向位置
	 * @param locationTemplateLayoutId 屏幕墙模板id
	 * @return
	 */
	public LocationOfScreenWallPO bindDecoder(String bundleId, 
			String bundleName, 
			Integer locationX, 
			Integer locationY,
			Long locationTemplateLayoutId) throws Exception{
		
		if(locationTemplateLayoutDao.findOne(locationTemplateLayoutId) == null){
			throw new BaseException(StatusCode.FORBIDDEN, "屏幕墙模板没有创建："+locationTemplateLayoutId);
		}
		
		//如果要绑定的解码器已经绑定过并且正在转发，或者转发播放或者对应位置已经被其他设备绑定并且在播放，抛错
		//查找解码器被绑定过的信息.
		LocationOfScreenWallPO searchScreenWall = locationOfScreenWallDao.findByLocationTemplateLayoutIdAndDecoderBundleId(locationTemplateLayoutId, bundleId);
		//查找屏幕现在绑定的解码器.
		LocationOfScreenWallPO locationScreen = locationOfScreenWallDao.findByLocationTemplateLayoutIdAndLocationXAndLocationY(locationTemplateLayoutId, locationX, locationY);
		
		if((searchScreenWall != null && LocationExecuteStatus.RUN.equals(searchScreenWall.getStatus())) || 
		   (locationScreen !=null && LocationExecuteStatus.RUN.equals(locationScreen.getStatus()))){
			throw new BaseException(StatusCode.FORBIDDEN, "绑定失败:解码器正在播放或者屏幕正在被占用播放");
		}
		
		LocationOfScreenWallPO screenWall = new LocationOfScreenWallPO();
		
		if(searchScreenWall != null){
			searchScreenWall.setLocationX(locationX);
			searchScreenWall.setLocationY(locationX);
			locationOfScreenWallDao.save(searchScreenWall);
			return searchScreenWall;
		}else{
			screenWall.setDecoderBundleId(bundleId)
			 		  .setDecoderBundleName(bundleName)
			 		  .setLocationX(locationX)
			 		  .setLocationY(locationY)
			 		  .setLocationTemplateLayoutId(locationTemplateLayoutId);
			locationOfScreenWallDao.save(screenWall);
			return screenWall;
		}
		
	}

	/**
	 * 解绑解码器<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月3日 上午11:07:48
	 * @param id 屏幕id
	 */
	
	public void unbindDecoder(Long id) throws BaseException {
		
		LocationOfScreenWallPO screenWall = locationOfScreenWallDao.findOne(id);
		
		if( screenWall == null || LocationExecuteStatus.RUN.equals(screenWall.getStatus())){
			throw new BaseException(StatusCode.FORBIDDEN, "还没有绑定解码器或者正在执行转发,请先停止转发");
		}
		
		locationOfScreenWallDao.delete(id);
	}
	
	/**
	 * 解绑所有解码器<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月3日 上午11:07:48
	 * @param id 屏幕id
	 */
	
//	public void unbindDecoder(Long id) throws BaseException {
//		
//		LocationOfScreenWallPO screenWall = locationOfScreenWallDao.findOne(id);
//		
//		if( screenWall == null || LocationExecuteStatus.RUN.equals(screenWall.getStatus())){
//			throw new BaseException(StatusCode.FORBIDDEN, "还没有绑定解码器或者正在执行转发,请先停止转发");
//		}
//		
//		locationOfScreenWallDao.delete(id);
//	}

	/**
	 * 绑定编码器<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月3日 上午11:06:12
	 * @param bundleId 设备id
	 * @param bundleName 设备name
	 * @param locationX x方向位置
	 * @param locationY y方向位置
	 * @param locationTemplateLayoutId 屏幕墙模板id
	 * @throws Exception 
	 */
	public LocationOfScreenWallPO bindEncoder(String bundleId, 
			String bundleName, 
			Integer locationX, 
			Integer locationY,
			Long locationTemplateLayoutId,
			UserVO user) throws Exception {
		
//		if(locationTemplateLayoutDao.findOne(locationTemplateLayoutId) == null){
//			throw new BaseException(StatusCode.FORBIDDEN, "屏幕墙模板没有创建："+locationTemplateLayoutId);
//		}
		
		//查找屏幕现在绑定的解码器.
		LocationOfScreenWallPO locationScreen = locationOfScreenWallDao.findByLocationTemplateLayoutIdAndLocationXAndLocationY(locationTemplateLayoutId, locationX, locationY);
		
		if(locationScreen == null || locationScreen.getDecoderBundleId() == null || locationScreen.getDecoderBundleId().equals("")){
			throw new BaseException(StatusCode.FORBIDDEN, "请先绑定解码器");
		}
		
		//如果有正在执行的转发先停止并删除转发
		if (LocationExecuteStatus.RUN.equals(locationScreen.getStatus())) {
			monitorLiveDeviceService.stop(locationScreen.getMonitorLiveDeviceId(), user.getId(), user.getUserno(), null);
		}
		
		//执行转发
		MonitorLiveDevicePO live = monitorLiveUtil.vodDevice(null, "BUNDLE", bundleId, null, null, "BUNDLE", locationScreen.getDecoderBundleId(), null, null, "DEVICE", user.getId(), user.getUserno(), user.getName());
		locationScreen.setEncoderBundleId(bundleId)
							  .setEncoderBundleName(bundleName)
							  .setStatus(LocationExecuteStatus.RUN)
							  .setMonitorLiveDeviceId(live.getId());
		
		locationOfScreenWallDao.save(locationScreen);
		
		return locationScreen;
	}

	/**
	 * 解绑编码器<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 下午1:28:09
	 * @param id
	 * @param user 业务用户
	 * @throws Exception 
	 */
	public void unbindEncoder(
			Long id,
			UserVO user) throws Exception {
		
		LocationOfScreenWallPO locationScreen = locationOfScreenWallDao.findOne(id);
		
		if(locationScreen == null || locationScreen.getEncoderBundleId() == null || locationScreen.getEncoderBundleId().equals("")){
			throw new BaseException(StatusCode.FORBIDDEN, "屏幕还没有绑定解码器或者编码器");
		}
		
		if(LocationExecuteStatus.RUN.equals(locationScreen.getStatus())){
			monitorLiveDeviceService.stop(locationScreen.getMonitorLiveDeviceId(), user.getId(), user.getUserno(), null);
		}
		
		locationScreen.setEncoderBundleId("");
		locationScreen.setEncoderBundleName("");
		locationScreen.setStatus(LocationExecuteStatus.STOP);
		
		locationOfScreenWallDao.save(locationScreen);
		
	}

	/**
	 * 解绑所有编码器<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 下午1:42:18
	 * @param ids 要解绑的屏幕LocationOfScreenWallPO的id
	 * @param LocationTemplateLayoutId 屏幕墙id
	 * @param unbindAll false解绑屏幕墙指定id编码器,true解绑屏幕墙所有编码器
	 * @param user 业务用户
	 */
	public void unbindALLEncoder(
			List<Long> ids, 
			Long locationTemplateLayoutId, 
			Boolean unbindAll, 
			UserVO user) throws Exception{
		
		if(Boolean.TRUE.equals(unbindAll)){
			List<LocationOfScreenWallPO> screenList = locationOfScreenWallDao.findByLocationTemplateLayoutId(locationTemplateLayoutId);
			
			List<LocationOfScreenWallPO> errorScreenList = new ArrayList<LocationOfScreenWallPO>();
			
			screenList.stream().forEach(screen->{
				if(LocationExecuteStatus.RUN.equals(screen.getStatus())){
					try {
						monitorLiveDeviceService.stop(screen.getMonitorLiveDeviceId(), user.getId(), user.getUserno(), null);
						screen.setEncoderBundleId("");
						screen.setEncoderBundleName("");
						screen.setStatus(LocationExecuteStatus.STOP);
					} catch (Exception e) {
						System.out.println("编码器:"+screen.getEncoderBundleName()+"解绑失败.");
						errorScreenList.add(screen);
						e.printStackTrace();
					}
				}
			});
			
			screenList.removeAll(errorScreenList);
			
			locationOfScreenWallDao.save(screenList);
		}else{
			List<LocationOfScreenWallPO> screenList = locationOfScreenWallDao.findByIdIn(ids);
			
			List<LocationOfScreenWallPO> errorScreenList = new ArrayList<LocationOfScreenWallPO>();
			
			screenList.stream().forEach(screen->{
				if(LocationExecuteStatus.RUN.equals(screen.getStatus())){
					try {
						monitorLiveDeviceService.stop(screen.getMonitorLiveDeviceId(), user.getId(), user.getUserno(), null);
						screen.setEncoderBundleId("");
						screen.setEncoderBundleName("");
						screen.setStatus(LocationExecuteStatus.STOP);
					} catch (Exception e) {
						System.out.println("编码器:"+screen.getEncoderBundleName()+"解绑失败.");
						errorScreenList.add(screen);
						e.printStackTrace();
					}
				}
			});
			
			screenList.removeAll(errorScreenList);
			
			locationOfScreenWallDao.save(screenList);
		}
	}
	
	/**
	 * 切换屏幕的运行状态<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月4日 下午7:17:22
	 * @param id 屏幕的id
	 * @throws Exception 
	 */
	public LocationOfScreenWallVO exchangeLocationStatus(Long id, String status) throws Exception{
		
		LocationOfScreenWallPO screenPO = locationOfScreenWallDao.findOne(id);
		
		if("运行中".equals(status)){
			Optional.ofNullable(screenPO).map(screen->{
				screen.setStatus(LocationExecuteStatus.RUN);
				return status;
			});
		}else{
			Optional.ofNullable(screenPO).map(screen->{
				screen.setStatus(LocationExecuteStatus.STOP);
				return status;
			});
		}
		
		return screenPO == null?null:new LocationOfScreenWallVO().set(screenPO); 
	} 
}
