package com.sumavision.tetris.bvc.business.location;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.bvc.control.device.monitor.live.MonitorLiveUtil;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.monitor.live.device.MonitorLiveDeviceDAO;
import com.sumavision.bvc.device.monitor.live.device.MonitorLiveDevicePO;
import com.sumavision.bvc.device.monitor.live.device.MonitorLiveDeviceService;
import com.sumavision.bvc.device.monitor.record.MonitorRecordStatus;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.user.UserQuery;

/**
 * <p>屏幕墙上某个屏幕</p>
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
	
	@Autowired
	private MonitorLiveDeviceDAO monitorLiveDeviceDao;
	
	/**
	 * 屏幕墙的编解码信息<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月5日 下午2:54:51
	 * @param locationTemplateLayoutId 屏幕墙id
	 */
	public List<LocationOfScreenWallPO> allScreenInformation(Long locationTemplateLayoutId) {
		
		return locationOfScreenWallDao.findByLocationTemplateLayoutId(locationTemplateLayoutId);
	} 

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
	public LocationOfScreenWallVO bindDecoder(String bundleId, 
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
		//已经在其他屏幕墙模板绑定过就不能再绑定
		LocationOfScreenWallPO wallOfOtherLayout = locationOfScreenWallDao.findByLocationTemplateLayoutIdNotAndDecoderBundleId(locationTemplateLayoutId, bundleId);
		
		if(wallOfOtherLayout != null){
			throw new BaseException(StatusCode.FORBIDDEN, "绑定失败:解码器已经被其他屏幕墙模板占用");
		}
		
		if((searchScreenWall != null && LocationExecuteStatus.RUN.equals(searchScreenWall.getStatus())) || 
		   (locationScreen !=null && LocationExecuteStatus.RUN.equals(locationScreen.getStatus()))){
			throw new BaseException(StatusCode.FORBIDDEN, "绑定失败:解码器正在播放或者屏幕正在被占用播放");
		}
		
		if(locationScreen !=null && LocationExecuteStatus.STOP.equals(locationScreen.getStatus())){
			locationOfScreenWallDao.delete(locationScreen);
		}
		
		LocationOfScreenWallPO screenWall = new LocationOfScreenWallPO();
		
		if(searchScreenWall != null){
			searchScreenWall.setLocationX(locationX);
			searchScreenWall.setLocationY(locationY);
			locationOfScreenWallDao.save(searchScreenWall);
			return new LocationOfScreenWallVO().set(searchScreenWall);
		}else{
			screenWall.setDecoderBundleId(bundleId)
			 		  .setDecoderBundleName(bundleName)
			 		  .setLocationX(locationX)
			 		  .setLocationY(locationY)
			 		  .setLocationTemplateLayoutId(locationTemplateLayoutId);
			locationOfScreenWallDao.save(screenWall);
			return new LocationOfScreenWallVO().set(screenWall);
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
	 * @param locationTemplateLayoutId 屏幕墙id
	 */
	public void unbindAllDecoder(Long locationTemplateLayoutId) throws BaseException {
		
		List<LocationOfScreenWallPO> deleteScreenWallList = locationOfScreenWallDao.findByLocationTemplateLayoutId(locationTemplateLayoutId).stream().filter(screen->{
			if(LocationExecuteStatus.STOP.equals(screen.getStatus())){
				return true;
			}
			return false;
		}).collect(Collectors.toList());
		
		locationOfScreenWallDao.deleteInBatch(deleteScreenWallList);
	}

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
							  .setStatus(LocationExecuteStatus.STOP)
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
		}else{
			monitorLiveDeviceDao.delete(locationScreen.getMonitorLiveDeviceId());
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
				}else{
					screen.setEncoderBundleId("");
					screen.setEncoderBundleName("");
				}
			});
			
			screenList.removeAll(errorScreenList);
			List<Long> needToDeleteLiveIds = screenList.stream().map(LocationOfScreenWallPO::getMonitorLiveDeviceId).collect(Collectors.toList());
			monitorLiveDeviceDao.deleteByIdIn(needToDeleteLiveIds);
			
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
				}else{
					screen.setEncoderBundleId("");
					screen.setEncoderBundleName("");
				}
			});
			
			screenList.removeAll(errorScreenList);
			
			locationOfScreenWallDao.save(screenList);
		}
	}

	/**
	 * <br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月9日 上午9:03:38
	 * @param id 屏幕的id
	 * @param monitorLiveDeviceId 录制任务id
	 * @param Boolean stopOrStart TRUE停止,FALSE开始
	 * @param userNo 业务人员名字
	 * @param userId 业务人员id
	 */
	
	public Object exchangeLocationStatus(
			Long id, 
			Boolean stopOrStart,
			String userNo,
			Long userId) throws Exception {
		
		LocationOfScreenWallPO screenPO = locationOfScreenWallDao.findOne(id);
		
		if(screenPO == null || screenPO.getMonitorLiveDeviceId() == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有找到对应的转发");
		}
		
		if(stopOrStart){
			monitorLiveDeviceService.stop(screenPO.getMonitorLiveDeviceId(), userId, userNo, stopOrStart);
			screenPO.setStatus(LocationExecuteStatus.STOP);
		}else{
			monitorLiveDeviceService.stopToRestart(new ArrayListWrapper<Long>().add(screenPO.getMonitorLiveDeviceId()).getList(), userId);
			screenPO.setStatus(LocationExecuteStatus.RUN);
		}
		
		locationOfScreenWallDao.save(screenPO);
		
		return screenPO;
	}
	

	/**
	 * 为了测试需要批量停止或者开始已经配置转发关系<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月12日 下午7:23:10
	 * @param allStart true将所有的转发开始，stop将所有转发停止
	 * @param layoutId 屏幕墙模板id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void allStartOrStop(
			Boolean allStart, 
			Long layoutId,
			Long userId, 
			String userno) throws Exception{
		
		if(allStart != null && allStart){
			
			List<LocationOfScreenWallPO> locationOfScreenWalls = locationOfScreenWallDao.findByEncoderBundleIdNotNullAndStatusAndLocationTemplateLayoutId(LocationExecuteStatus.STOP, layoutId);
			locationOfScreenWalls.forEach(wall->{
				wall.setStatus(LocationExecuteStatus.RUN);
			});
			locationOfScreenWallDao.save(locationOfScreenWalls);
			
			List<Long> liveIds = locationOfScreenWalls.stream().map(LocationOfScreenWallPO::getMonitorLiveDeviceId).collect(Collectors.toList());
			monitorLiveDeviceService.stopToRestart(liveIds, userId);
		}else if(allStart != null && !allStart){
			
			List<LocationOfScreenWallPO> locationOfScreenWalls = locationOfScreenWallDao.findByEncoderBundleIdNotNullAndStatusAndLocationTemplateLayoutId(LocationExecuteStatus.RUN, layoutId);
			locationOfScreenWalls.forEach(wall->{
				wall.setStatus(LocationExecuteStatus.STOP);
			});
			locationOfScreenWallDao.save(locationOfScreenWalls);
			
			List<Long> liveIds = locationOfScreenWalls.stream().map(LocationOfScreenWallPO::getMonitorLiveDeviceId).collect(Collectors.toList());
			monitorLiveDeviceService.stop(liveIds, userId, userno, Boolean.TRUE);
		}
	}
	
	/**
	 * 查找屏幕墙上是否有对应live,并且重新布置编码器<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月28日 下午2:31:41
	 * @param live
	 * @return
	 */
	public MonitorLiveDevicePO hasLiveForScreenWallAndReset(MonitorLiveDevicePO live , String videoBundleId, String videoBundleName, UserVO user) throws Exception{
		LocationOfScreenWallPO screenWall = locationOfScreenWallDao.findByMonitorLiveDeviceId(live.getId());
		if(screenWall != null){
			screenWall = bindEncoder(videoBundleId, videoBundleName, screenWall.getLocationX(), screenWall.getLocationY(), screenWall.getLocationTemplateLayoutId(), user);
			MonitorLiveDevicePO newLive = monitorLiveDeviceDao.findOne(screenWall.getMonitorLiveDeviceId());
			return newLive;
		}
		return null;
	}
	
	/**
	 * 查找屏幕墙上是否有对应live,并且修改状态<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月28日 下午3:29:59
	 * @param liveIds 转发id集合
	 * @param stopOrStart TRUE停止,FALSE开始
	 * @param userNo 业务人员名字
	 * @param userId 业务人员id
	 */
	public void hasLiveForScreenWallAndExchangeStatus(List<Long> liveIds, Boolean stopOrStart, String userNo, Long userId) throws Exception{
		List<LocationOfScreenWallPO> screenWallList = locationOfScreenWallDao.findByMonitorLiveDeviceIdIn(liveIds);
		for(LocationOfScreenWallPO screenWall : screenWallList){
			exchangeLocationStatus(screenWall.getId(), stopOrStart, userNo, userId);
			liveIds.remove(screenWall.getMonitorLiveDeviceId());
		}
	}
	
	/**
	 * 查找屏幕墙上是否有对应live,并且解绑<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月28日 下午3:29:59
	 * @param liveIds 转发id集合
	 * @param user 业务人员
	 */
	public void hasLiveForScreenWallAndUnbind(List<Long> liveIds,UserVO user) throws Exception{
		List<LocationOfScreenWallPO> screenWallList = locationOfScreenWallDao.findByMonitorLiveDeviceIdIn(liveIds);
		for(LocationOfScreenWallPO screenWall : screenWallList){
			unbindEncoder(screenWall.getId(), user);
			liveIds.remove(screenWall.getMonitorLiveDeviceId());
		}
	}
	
}
