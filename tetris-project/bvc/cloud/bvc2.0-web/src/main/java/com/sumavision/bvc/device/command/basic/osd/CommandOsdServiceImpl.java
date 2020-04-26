package com.sumavision.bvc.device.command.basic.osd;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.bvc.command.group.dao.CommandGroupUserInfoDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserPlayerDAO;
import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerCastDevicePO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.ConnectBO;
import com.sumavision.bvc.device.group.bo.ConnectBundleBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.OsdWrapperBO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdDAO;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdPO;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdService;
import com.sumavision.bvc.device.monitor.osd.exception.MonitorOsdNotExistException;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Service
public class CommandOsdServiceImpl {

	@Autowired
	private MonitorOsdDAO monitorOsdDao;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private CommandGroupUserInfoDAO commandGroupUserInfoDao;
	
	@Autowired
	private CommandGroupUserPlayerDAO commandGroupUserPlayerDao;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	private MonitorOsdService monitorOsdService;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	/**
	 * 获取设备信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月26日 下午3:58:19
	 * @param CommandGroupUserPlayerPO player 播放器
	 * @return List<BundleDTO> 设备信息
	 */
	private List<BundleDTO> packBundles(CommandGroupUserPlayerPO player){
		List<BundleDTO> bunldes = new ArrayList<BundleDTO>();
		bunldes.add(new BundleDTO().setBundleId(player.getBundleId())
								   .setBundleType(player.getBundleType())
								   .setLayerId(player.getLayerId())
								   .setVideoChannelId(player.getVideoChannelId())
								   .setVideoChannelBaseType(player.getVideoBaseType()));
		List<CommandGroupUserPlayerCastDevicePO> castDevices = player.getCastDevices();
		if(castDevices!=null && castDevices.size()>0){
			for(CommandGroupUserPlayerCastDevicePO castDevice:castDevices){
				bunldes.add(new BundleDTO().setBundleId(castDevice.getDstBundleId())
										   .setBundleType(castDevice.getDstBundleType())
										   .setLayerId(castDevice.getDstLayerId())
										   .setVideoChannelId(castDevice.getDstVideoChannelId())
										   .setVideoChannelBaseType("VenusVideoOut"));
			}
		}
		return bunldes;
	}
	
	/**
	 * 设置/修改osd<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月26日 下午1:56:00
	 * @param Integer serial 屏幕布局序号
	 * @param Long osdId 字幕id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void setOsd(
			Integer serial, 
			Long osdId) throws Exception{
		
		MonitorOsdPO osd = monitorOsdDao.findOne(osdId);
		if(osd == null){
			throw new MonitorOsdNotExistException(osdId);
		}
		
		UserVO user = userQuery.current();
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(user.getId());
		
		CommandGroupUserPlayerPO player = commandGroupUserPlayerDao.findByLocationIndexAndUserInfoId(serial, userInfo.getId());
		player.setOsdId(osd.getId());
		player.setOsdName(osd.getName());
		commandGroupUserPlayerDao.save(player);
		
		if(PlayerBusinessType.NONE.equals(player.getPlayerBusinessType())) return;
		
		//获取源
		List<BundleDTO> bundles = packBundles(player);
		
		//获取参数模板
		CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
		
		LogicBO logic = new LogicBO().setUserId("-1")
			 			 			 .setConnectBundle(new ArrayList<ConnectBundleBO>());
		
		//获取字幕协议
		OsdWrapperBO clearOsd = monitorOsdService.clearProtocol("", "");
		OsdWrapperBO setOsd = monitorOsdService.protocol(osd, "", "");
		
		for(BundleDTO bundle:bundles){
			ConnectBundleBO connectDstVideoBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
																		 .setLock_type("write")
																	     .setBundleId(bundle.getBundleId())
																	     .setLayerId(bundle.getLayerId())
																	     .setBundle_type(bundle.getBundleType());
			ConnectBO connectDstVideoChannel = new ConnectBO().setChannelId(bundle.getVideoChannelId())
														      .setChannel_status("Open")
														      .setBase_type(bundle.getVideoChannelBaseType())
														      .setCodec_param(codec);
			connectDstVideoChannel.setOsds(clearOsd);
			connectDstVideoBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectDstVideoChannel).getList());
			logic.getConnectBundle().add(connectDstVideoBundle);
		}
		
		//先发清除
		executeBusiness.execute(logic, "指控系统：清除字幕");
		
		for(ConnectBundleBO connectDstVideoBundle:logic.getConnectBundle()){
			connectDstVideoBundle.getChannels().get(0).setOsds(setOsd);
		}
		
		//后发设置
		executeBusiness.execute(logic, "指控系统：重设字幕");
	}
	
	/**
	 * 清除osd<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月26日 下午2:48:39
	 * @param Integer serial 布局序号
	 */
	@Transactional(rollbackFor = Exception.class)
	public void clearOsd(Integer serial) throws Exception{
		
		UserVO user = userQuery.current();
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(user.getId());
		
		CommandGroupUserPlayerPO player = commandGroupUserPlayerDao.findByLocationIndexAndUserInfoId(serial, userInfo.getId());
		player.setOsdId(null);
		player.setOsdName(null);
		commandGroupUserPlayerDao.save(player);
		
		if(PlayerBusinessType.NONE.equals(player.getPlayerBusinessType())) return;
		
		//获取源
		List<BundleDTO> bundles = packBundles(player);
		
		//获取参数模板
		CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
		
		LogicBO logic = new LogicBO().setUserId("-1")
			 			 			 .setConnectBundle(new ArrayList<ConnectBundleBO>());
		
		//获取字幕协议
		OsdWrapperBO clearOsd = monitorOsdService.clearProtocol("", "");
		
		for(BundleDTO bundle:bundles){
			ConnectBundleBO connectDstVideoBundle = new ConnectBundleBO().setBusinessType(ConnectBundleBO.BUSINESS_TYPE_VOD)
																		 .setLock_type("write")
																	     .setBundleId(bundle.getBundleId())
																	     .setLayerId(bundle.getLayerId())
																	     .setBundle_type(bundle.getBundleType());
			ConnectBO connectDstVideoChannel = new ConnectBO().setChannelId(bundle.getVideoChannelId())
														      .setChannel_status("Open")
														      .setBase_type(bundle.getVideoChannelBaseType())
														      .setCodec_param(codec);
			connectDstVideoChannel.setOsds(clearOsd);
			connectDstVideoBundle.setChannels(new ArrayListWrapper<ConnectBO>().add(connectDstVideoChannel).getList());
			logic.getConnectBundle().add(connectDstVideoBundle);
		}
		
		//先发清除
		executeBusiness.execute(logic, "指控系统：清除字幕");
	}
	
}
