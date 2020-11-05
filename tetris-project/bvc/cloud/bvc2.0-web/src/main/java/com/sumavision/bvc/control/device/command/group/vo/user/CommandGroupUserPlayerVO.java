package com.sumavision.bvc.control.device.command.group.vo.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.sumavision.bvc.command.group.dao.CommandGroupDAO;
import com.sumavision.bvc.command.group.enumeration.GroupStatus;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerCastDevicePO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import com.sumavision.bvc.command.group.user.layout.player.PlayerBusinessType;
import com.sumavision.tetris.commons.context.SpringContext;

/**
 * 网页播放器<br/>
 * <b>作者:</b>zsy<br/>
 * <b>说明:</b><br/>
 * <b>日期：</b>2019年10月8日
 */
@Deprecated
public class CommandGroupUserPlayerVO {	
	
	/** 位置索引，表示在布局中的第几个位置，取值1-16 */
	private int locationIndex;
	
	private String playerBusinessType;
	
	private String businessId;

	/** 业务名称，用于显示在播放器顶端 */
	private String businessName;
	
	/** 文件、录像的播放地址 */
	private String playUrl;
	
	/** 指挥的状态 */
	private String status;
	
	/** 设备id */
	private String bundleId;
	
	/** 绑定的上屏设备 */
	private Set<CommandGroupUserPlayerCastDeviceVO> castDevices = new HashSet<CommandGroupUserPlayerCastDeviceVO>();

	public int getLocationIndex() {
		return locationIndex;
	}

	public void setLocationIndex(int locationIndex) {
		this.locationIndex = locationIndex;
	}
	
	public String getPlayerBusinessType() {
		return playerBusinessType;
	}

	public void setPlayerBusinessType(String playerBusinessType) {
		this.playerBusinessType = playerBusinessType;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getPlayUrl() {
		return playUrl;
	}

	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
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

	public Set<CommandGroupUserPlayerCastDeviceVO> getCastDevices() {
		return castDevices;
	}

	public void setCastDevices(Set<CommandGroupUserPlayerCastDeviceVO> castDevices) {
		this.castDevices = castDevices;
	}

	private CommandGroupUserPlayerVO setCastDevicesByPO(List<CommandGroupUserPlayerCastDevicePO> castDevices) {
		this.setCastDevices(new HashSet<CommandGroupUserPlayerCastDeviceVO>()); 
		if(castDevices == null) return this;
		for(CommandGroupUserPlayerCastDevicePO castDevice : castDevices){
			this.getCastDevices().add(new CommandGroupUserPlayerCastDeviceVO().set(castDevice));
		}
		return this;
	}
	
	public CommandGroupUserPlayerVO set(CommandGroupUserPlayerPO entity){
		this.setLocationIndex(entity.getLocationIndex());
		this.setPlayerBusinessType(entity.getPlayerBusinessType().getCode());
		this.setBusinessId(entity.getBusinessId());
		this.setBusinessName(entity.getBusinessName());
		this.setPlayUrl(entity.getPlayUrl());
		this.setBundleId(entity.getBundleId());
		PlayerBusinessType type = entity.getPlayerBusinessType();
		if(type.equals(PlayerBusinessType.BASIC_COMMAND)
				|| type.equals(PlayerBusinessType.CHAIRMAN_BASIC_COMMAND)
				|| type.equals(PlayerBusinessType.COOPERATE_COMMAND)){
			CommandGroupDAO commandGroupDao = SpringContext.getBean(CommandGroupDAO.class);
			Long groupId = Long.parseLong(entity.getBusinessId().split("-")[0]);
			GroupStatus status = commandGroupDao.findStatusById(groupId);
			this.setStatus(status.toString().toLowerCase());
		}
		this.setStatus(status);
		
		this.setCastDevicesByPO(entity.getCastDevices());
		
		return this;
	}
	
}
