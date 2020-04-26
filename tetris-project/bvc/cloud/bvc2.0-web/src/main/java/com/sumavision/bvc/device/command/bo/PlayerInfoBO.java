package com.sumavision.bvc.device.command.bo;

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
	
	private String srcCode = "";
	
	public PlayerInfoBO(){}
	
	public PlayerInfoBO(
			boolean hasBusiness,
			String srcInfo,
			String srcCode){
		this.hasBusiness = hasBusiness;
		this.srcInfo = srcInfo;
		this.srcCode = srcCode;
	}

}
