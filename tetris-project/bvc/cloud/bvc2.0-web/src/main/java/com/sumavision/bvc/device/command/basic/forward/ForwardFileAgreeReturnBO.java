package com.sumavision.bvc.device.command.basic.forward;

import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerPO;
import lombok.Getter;
import lombok.Setter;

/**
 * 
* @ClassName: ForwardFileAgreeReturnBO 
* @Description: 成员“同意文件转发的请求”的返回值
* @author zsy
* @date 2019年11月15日 下午1:15:12 
*
 */
@Getter
@Setter
public class ForwardFileAgreeReturnBO {

	private int serial;
	
	private String url = "";
	
	private String businessType = "";
	
	private String businessId = "";
	
	private String businessInfo = "";
	
	public ForwardFileAgreeReturnBO set(CommandGroupUserPlayerPO player){
		this.serial = player.getLocationIndex();
		this.url = player.getPlayUrl();
		this.businessType = player.getPlayerBusinessType().getCode();
		this.businessId = player.getBusinessId();
		this.businessInfo = player.getBusinessName();
		return this;
	}

}
