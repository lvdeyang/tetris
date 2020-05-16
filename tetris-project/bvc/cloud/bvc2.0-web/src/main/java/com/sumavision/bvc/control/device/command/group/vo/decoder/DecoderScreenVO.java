package com.sumavision.bvc.control.device.command.group.vo.decoder;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.bvc.command.group.user.decoder.CommandGroupDecoderScreenPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerCastDevicePO;
import com.sumavision.bvc.control.device.command.group.vo.user.CommandGroupUserPlayerCastDeviceVO;

/**
 * 上屏方案中的分屏<br/>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年5月13日 上午10:37:55
 */
public class DecoderScreenVO{
	
	private String screenId;
	
	/** 业务类别 */
	private String businessType;
	
	/** 业务信息 */
	private String businessInfo;
	
	/** 文件地址 */
	private String url;
	
	private OsdVO osd;
	
	/** 绑定的上屏设备 */
	private List<CommandGroupUserPlayerCastDeviceVO> castDevices = new ArrayList<CommandGroupUserPlayerCastDeviceVO>();

	public String getScreenId() {
		return screenId;
	}

	public DecoderScreenVO setScreenId(String screenId) {
		this.screenId = screenId;
		return this;
	}

	public String getBusinessType() {
		return businessType;
	}

	public DecoderScreenVO setBusinessType(String businessType) {
		this.businessType = businessType;
		return this;
	}

	public String getBusinessInfo() {
		return businessInfo;
	}

	public DecoderScreenVO setBusinessInfo(String businessInfo) {
		this.businessInfo = businessInfo;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public DecoderScreenVO setUrl(String url) {
		this.url = url;
		return this;
	}

	public OsdVO getOsd() {
		return osd;
	}

	public DecoderScreenVO setOsd(OsdVO osd) {
		this.osd = osd;
		return this;
	}

	public List<CommandGroupUserPlayerCastDeviceVO> getCastDevices() {
		return castDevices;
	}

	public DecoderScreenVO setCastDevices(List<CommandGroupUserPlayerCastDeviceVO> castDevices) {
		this.castDevices = castDevices;
		return this;
	}

	public DecoderScreenVO set(CommandGroupDecoderScreenPO screen){
		this.setScreenId(screen.getId().toString())
		   .setBusinessType(screen.getBusinessType().getCode())
		   .setBusinessInfo(screen.getBusinessInfo())
		   .setOsd(new OsdVO().setId(screen.getOsdId()==null?null:screen.getOsdId().toString()).setName(screen.getOsdName()))
		   .setCastDevicesByPO(screen.getCastDevices())
		   .setUrl(screen.getPlayUrl());
		
		return this;
	}
	
	private DecoderScreenVO setCastDevicesByPO(List<CommandGroupUserPlayerCastDevicePO> castDevices) {
		this.setCastDevices(new ArrayList<CommandGroupUserPlayerCastDeviceVO>()); 
		if(castDevices == null) return this;
		for(CommandGroupUserPlayerCastDevicePO castDevice : castDevices){
			this.getCastDevices().add(new CommandGroupUserPlayerCastDeviceVO().set(castDevice));
		}
		return this;
	}
	
}
