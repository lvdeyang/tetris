package com.sumavision.bvc.control.device.command.group.vo.user;

import java.util.ArrayList;
import java.util.List;

import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.enumeration.GroupStatus;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerCastDevicePO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.tetris.bvc.business.BusinessInfoType;
import com.sumavision.tetris.bvc.page.PageTaskPO;
import com.sumavision.tetris.commons.context.SpringContext;

/**
 * 网页播放器 与 BusinessPlayerVO 基本一样，应该去掉一个<br/>
 * 目前主要用于接口：<br/>
 * /command/user/info/get/current<br/>
 * /command/split/change<br/>
 * <b>作者:</b>zsy<br/>
 * <b>说明:</b><br/>
 * <b>日期：</b>2019年10月8日
 */
public class CommandGroupUserPlayerSettingVO {	
	
	/** 位置索引，表示在布局中的第几个位置，取值0-15 */
	private int serial;
	
	/** 设备id */
	private String bundleId;
	
	private String bundleNo;
	
	private String businessType;
	
	private String businessId;

	/** 业务名称，用于显示在播放器顶端 */
	private String businessInfo;
	
	/** 指挥的状态 */
	private String status;
	
	/** 文件、录像的播放地址 */
	private String url;
	
	/** 绑定的上屏设备 */
	private List<CommandGroupUserPlayerCastDeviceVO> castDevices = new ArrayList<CommandGroupUserPlayerCastDeviceVO>();

	public int getSerial() {
		return serial;
	}

	public void setSerial(int locationIndex) {
		this.serial = locationIndex;
	}
	
	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String playerBusinessType) {
		this.businessType = playerBusinessType;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getBusinessInfo() {
		return businessInfo;
	}

	public void setBusinessInfo(String businessInfo) {
		this.businessInfo = businessInfo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String playUrl) {
		this.url = playUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public String getBundldNo() {
		return bundleNo;
	}

	public void setBundleNo(String businessNo) {
		this.bundleNo = businessNo;
	}

	public List<CommandGroupUserPlayerCastDeviceVO> getCastDevices() {
		return castDevices;
	}

	public void setCastDevices(List<CommandGroupUserPlayerCastDeviceVO> castDevices) {
		this.castDevices = castDevices;
	}

	//关联上屏的设备VO
	private CommandGroupUserPlayerSettingVO setCastDevicesByPO(List<CommandGroupUserPlayerCastDevicePO> castDevices) {
		this.setCastDevices(new ArrayList<CommandGroupUserPlayerCastDeviceVO>()); 
		if(castDevices == null) return this;
		for(CommandGroupUserPlayerCastDevicePO castDevice : castDevices){
			this.getCastDevices().add(new CommandGroupUserPlayerCastDeviceVO().set(castDevice));
		}
		return this;
	}
	
	public CommandGroupUserPlayerSettingVO set(CommandGroupUserPlayerPO entity){		
		
		this.setSerial(entity.getLocationIndex());
		this.setBundleId(entity.getBundleId());
		this.setBundleNo(entity.getCode());
		this.setBusinessType(entity.getPlayerBusinessType().getCode());
		this.setBusinessId(entity.getBusinessId());
		this.setBusinessInfo(entity.getBusinessName());
		this.setUrl(entity.getPlayUrl());
		//status
		this.setStatus("start");
		PlayerBusinessType type = entity.getPlayerBusinessType();
		if(type.equals(PlayerBusinessType.BASIC_COMMAND)
				|| type.equals(PlayerBusinessType.CHAIRMAN_BASIC_COMMAND)
				|| type.equals(PlayerBusinessType.COOPERATE_COMMAND)){
			CommandGroupDAO commandGroupDao = SpringContext.getBean(CommandGroupDAO.class);
			Long groupId = Long.parseLong(entity.getBusinessId().split("-")[0]);
			GroupStatus status = commandGroupDao.findStatusById(groupId);
			this.setStatus(status.toString().toLowerCase());
		}
		//上屏设备
		List<CommandGroupUserPlayerCastDevicePO> devices = entity.getCastDevices();
		this.setCastDevicesByPO(devices);
		
		return this;
	}
	
	public CommandGroupUserPlayerSettingVO setIdlePlayer(int locationIndex, BundlePO entity){		
		
		this.setSerial(locationIndex);
		this.setBundleId(entity.getBundleId());
		this.setBundleNo(entity.getUsername());
		this.setBusinessType(PlayerBusinessType.NONE.getCode());
		//status
		this.setStatus("start");
		//上屏设备
//		List<CommandGroupUserPlayerCastDevicePO> devices = entity.getCastDevices();
//		this.setCastDevicesByPO(devices);
		
		return this;
	}
	
	public CommandGroupUserPlayerSettingVO set(PageTaskPO entity){		
		
		this.setSerial(entity.getLocationIndex());
		this.setBundleId(entity.getDstBundleId());
		this.setBundleNo(entity.getDstCode());
		this.setBusinessType(entity.getBusinessInfoType().getCode());
		this.setBusinessId(entity.getBusinessId());
		this.setBusinessInfo(entity.getBusinessName());
		this.setUrl(entity.getPlayUrl());
		//status，应该不用了
		this.setStatus("start");		
		/*BusinessType type = entity.getBusinessType();
		if(type.equals(BusinessType.BASIC_COMMAND)
				|| type.equals(BusinessType.CHAIRMAN_BASIC_COMMAND)
				|| type.equals(BusinessType.COOPERATE_COMMAND)){
			CommandGroupDAO commandGroupDao = SpringContext.getBean(CommandGroupDAO.class);
			Long groupId = Long.parseLong(entity.getBusinessId().split("-")[0]);
			GroupStatus status = commandGroupDao.findStatusById(groupId);
			this.setStatus(status.toString().toLowerCase());
		}*/
		//上屏设备
		/*List<CommandGroupUserPlayerCastDevicePO> devices = entity.getCastDevices();
		this.setCastDevicesByPO(devices);*/
		
		return this;
	}
	
}
