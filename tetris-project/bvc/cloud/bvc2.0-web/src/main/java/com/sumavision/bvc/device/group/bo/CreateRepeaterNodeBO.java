package com.sumavision.bvc.device.group.bo;

/**
 * 创建转发器任务节点透传<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月29日 下午4:09:56
 */
public class CreateRepeaterNodeBO implements BasePassByContent{

	/** 通道channelId1%channelId2 */
	private String channels;

	public String getChannels() {
		return channels;
	}

	public void setChannels(String channels) {
		this.channels = channels;
	}
}
