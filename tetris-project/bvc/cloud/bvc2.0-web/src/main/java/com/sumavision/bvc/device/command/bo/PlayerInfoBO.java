package com.sumavision.bvc.device.command.bo;

import com.sumavision.bvc.command.group.enumeration.SrcType;

import lombok.Getter;
import lombok.Setter;

/**
 * 
* @ClassName: PlayerInfoBO 
* @Description: 主要用来传递播放器播放的源信息
* @author zsy 
* @date 2020年4月26日 上午9:45:51 
*
 */
@Getter
@Setter
public class PlayerInfoBO {
	
	/** 当前是否有业务 */
	private boolean hasBusiness = false;

	/** 用于显示的源名称，用户名或设备名 */
	private String srcInfo = "";
	
	/** 源号码 */
	private String srcCode = "";
	
	/** 源类型 */
	private SrcType srcType;
	
	/** 播放地址 */
	private String playUrl;
	
	/** osd id */
	private Long osdId;
	
	/** osd 名称 */
	private String osdName;
	
	public PlayerInfoBO(){}
	
	public PlayerInfoBO(
			boolean hasBusiness,
			String srcInfo,
			String srcCode,
			SrcType srcType,
			String playUrl,
			Long osdId,
			String osdName){
		this.hasBusiness = hasBusiness;
		this.srcInfo = srcInfo;
		this.srcCode = srcCode;
		this.srcType = srcType;
		this.playUrl = playUrl;
		this.osdId = osdId;
		this.osdName = osdName;
	}

}
