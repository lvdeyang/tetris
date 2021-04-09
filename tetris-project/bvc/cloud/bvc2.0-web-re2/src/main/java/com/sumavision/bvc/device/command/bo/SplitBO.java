package com.sumavision.bvc.device.command.bo;

import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import lombok.Getter;
import lombok.Setter;

/**
 * 
* @ClassName: SplitBO 
* @Description: 接口中用于发送splits使用，与 CommandGroupUserPlayerSettingVO 及 BusinessPlayerVO 基本一样
* @author zsy 
* @date 2019年11月19日 上午9:45:51 
*
 */
@Getter
@Setter
public class SplitBO {

	/** 位置索引，表示在布局中的第几个位置，取值0-15 */
	private int serial;
	
	/** 设备id */
	private String bundleId;
	
	private String bundleNo;
	
	private String businessType;
	
	private String businessId;

	/** 业务名称，用于显示在播放器顶端 */
	private String businessInfo;
	
//	/** 指挥的状态 */
//	private String status;
	
	/** 文件、录像的播放地址 */
	private String url;
	
//	/** 绑定的上屏设备 */
//	private Set<CommandGroupUserPlayerCastDeviceVO> castDevices = new HashSet<CommandGroupUserPlayerCastDeviceVO>();
	
	public SplitBO(){}
	
	public SplitBO(
			int serial,
			String bundleId,
			String bundleNo,
			String businessType,
			String businessId,
			String businessInfo,
			String url){
		this.serial = serial;
		this.bundleId = bundleId;
		this.bundleNo = bundleNo;
		this.businessType = businessType;
		this.businessId = businessId;
		this.businessInfo = businessInfo;
		this.url = url;
	}
	
	public SplitBO set(CommandGroupUserPlayerPO entity){		
		
		this.setSerial(entity.getLocationIndex());
		this.setBundleId(entity.getBundleId());
		this.setBundleNo(entity.getCode());
		this.setBusinessType(entity.getPlayerBusinessType().getCode());
		this.setBusinessId(entity.getBusinessId());
		this.setBusinessInfo(entity.getBusinessName());
		this.setUrl(entity.getPlayUrl());
		//status
//		this.setStatus("start");
//		PlayerBusinessType type = entity.getPlayerBusinessType();
//		if(type.equals(PlayerBusinessType.BASIC_COMMAND)
//				|| type.equals(PlayerBusinessType.CHAIRMAN_BASIC_COMMAND)
//				|| type.equals(PlayerBusinessType.COOPERATE_COMMAND)){
//			CommandGroupDAO commandGroupDao = SpringContext.getBean(CommandGroupDAO.class);
//			Long groupId = Long.parseLong(entity.getBusinessId().split("-")[0]);
//			GroupStatus status = commandGroupDao.findStatusById(groupId);
//			this.setStatus(status.toString().toLowerCase());
//		}		
//		this.setCastDevicesByPO(entity.getCastDevices());
		
		return this;
	}

}
