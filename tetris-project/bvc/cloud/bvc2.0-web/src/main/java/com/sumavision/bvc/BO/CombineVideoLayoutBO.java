package com.sumavision.bvc.BO;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.BO.ForwardSetBO.SrcBO;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @ClassName:  CombineVideoLayoutBO   
 * @Description:合屏编码通道布局操作  
 * @author: 
 * @date:   2018年7月13日 下午4:44:24   
 *     
 * @Copyright: 2018 Sumavision. All rights reserved. 
 * 注意：本内容仅限于北京数码视讯科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Getter
@Setter
public class CombineVideoLayoutBO {

	/**
	 * 任务Id
	 */
	private String taskId;
	
	/**
	 * 锁类型 write/read 
	 */
	private String lock_type;
	
	/**
	 * 编解码参数
	 */
	private JSONObject codec_param;
	
	/**
	 * 编码通道布局UUID
	 */
	private String uuid;
	
	/**
	 * 解码通道布局
	 */
	private List<LayoutPosBO> position;		
	
	
	@Override
	public String toString() {
		return "CombineVideoLayoutBO [taskId=" + taskId + ", lock_type=" + lock_type + ", codec_param=" + codec_param
				+ ", uuid=" + uuid + ", position=" + position + "]";
	}



	/**
	 * 
	 * @ClassName:  LayoutPosBO   
	 * @Description:合屏的布局BO 
	 * @author: 
	 * @date:   2018年7月13日 下午5:21:31   
	 *     
	 * @Copyright: 2018 Sumavision. All rights reserved. 
	 * 注意：本内容仅限于北京数码视讯科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
	 */

	@Getter
	@Setter
	public static class LayoutPosBO{
		
		/**
		 * 任务Id
		 */
		private String taskId;
		
		/**
		 * 锁类型 write/read 
		 */
		private String lock_type;
		
		/**
		 * 编解码参数
		 */
		private JSONObject codec_param;
		
		
		/**
		 * 解码通道UUID，combineVideoSet使用
		 */
		private String uuid;
		
		/**
		 * 接入层Id
		 */
		private String layerId;
		
		
		/**
		 * 解码通道的bundleId和channelId,combineVideoUpdate使用
		 */
		private String bundleId;
		private String channelId;
		
		/**
		 * x,y,w,h表示解码通道在合屏中的布局位置
		 */
		private String x;
		
		private String y;
		
		private String w;
		
		private String h;
		
		private String z_index;
		/**
		 * 轮询时间，单画面不轮询为0
		 */
		private Integer pollingTime;
		
		/**
		 * 轮询状态，pause/polling
		 */
		private String pollingStatus;
		
		/**
		 * 最后转换为整数。下一个轮询的序号，-1表示继续轮询当前或由合屏器决定
		 */
		private String polling_index;				
		
		/**
		 * 解码通道的源
		 */
		private List<SrcBO> src;

		@Override
		public String toString() {
			return "LayoutPosBO [taskId=" + taskId + ", lock_type=" + lock_type + ", codec_param=" + codec_param
					+ ", uuid=" + uuid + ", layerId=" + layerId + ", bundleId=" + bundleId + ", channelId=" + channelId
					+ ", x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + ", pollingTime=" + pollingTime
					+ ", pollingStatus=" + pollingStatus + ", polling_index=" + polling_index + ", src=" + src + "]";
		}
		
		
		
	}
	
	/**
	 * 
	 * @ClassName:  LayoutPosBO   
	 * @Description:解码通道的布局BO 
	 * @author: 
	 * @date:   2018年7月13日 下午5:21:31   
	 *     
	 * @Copyright: 2018 Sumavision. All rights reserved. 
	 * 注意：本内容仅限于北京数码视讯科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
	 */

	@Getter
	@Setter
	public static class DecodeLayoutBO{						
				
		/**
		 * x,y,w,h表示解码通道在合屏中的布局位置
		 */
		private String x;
		
		private String y;
		
		private String w;
		
		private String h;
		
		private String z_index;
		/**
		 * 轮询时间，单画面不轮询为0
		 */
		private Integer pollingTime;
		
		/**
		 * 轮询状态，pause/polling
		 */
		private String pollingStatus;
		
		/**
		 * 最后转换为整数。下一个轮询的序号，-1表示继续轮询当前或由合屏器决定
		 */
		private String polling_index;
				
		
		@Override
		public String toString() {
			return "DecodeLayoutBO [x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + ", pollingTime=" + pollingTime
					+ ", pollingStatus=" + pollingStatus + ", polling_index=" + polling_index + "]";
		}
				
		
		public static DecodeLayoutBO transFromLayoutBO(LayoutPosBO posBO){
			DecodeLayoutBO decodeLayoutBO = new DecodeLayoutBO();
			BeanUtils.copyProperties(posBO, decodeLayoutBO, "src");
			return decodeLayoutBO;
		}
		
		
	}
}
