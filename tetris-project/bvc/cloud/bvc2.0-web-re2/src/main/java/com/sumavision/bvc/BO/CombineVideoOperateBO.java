package com.sumavision.bvc.BO;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.BO.CombineVideoLayoutBO.LayoutPosBO;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @ClassName:  CombineVideoOperateBO   
 * @Description:合屏设置/更新操作BO 
 * @author: 
 * @date:   2018年7月13日 下午5:16:33   
 *     
 * @Copyright: 2018 Sumavision. All rights reserved. 
 * 注意：本内容仅限于北京数码视讯科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Getter
@Setter
public class CombineVideoOperateBO {
	
	/**
	 * 任务Id
	 */
	private String taskId;
	
	/**
	 * 锁类型 write/read 
	 */
	private String lock_type;
	
	/**
	 * channel_param的基本能力参数
	 */
	private String base_type;
	
	/**
	 * 编解码参数
	 */
	private JSONObject codec_param;			
	
	/**
	 * 接入层Id
	 */
	private String layerId;
	
	
	/**
	 * 编码通道的bundleId和channelId,combineVideoUpdate使用
	 */
	private String bundleId;
	private String channelId;

	/**
	 * 合屏UUID
	 */
	private String uuid;
	
	/**
	 * 解码通道布局
	 */
	private List<LayoutPosBO> position;

	@Override
	public String toString() {
		return "CombineVideoOperateBO [taskId=" + taskId + ", lock_type=" + lock_type + ", codec_param=" + codec_param
				+ ", layerId=" + layerId + ", bundleId=" + bundleId + ", channelId=" + channelId + ", uuid=" + uuid
				+ ", postion=" + position + "]";
	}

	
	
	
}
