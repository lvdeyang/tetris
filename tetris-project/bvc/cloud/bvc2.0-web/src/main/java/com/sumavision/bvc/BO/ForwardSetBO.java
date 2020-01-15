package com.sumavision.bvc.BO;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.BO.ForwardSetBO.SrcBO;

/**
 * 
 * @ClassName:  ForwardSetBO   
 * @Description:转发操作BO  
 * @author: 
 * @date:   2018年7月13日 下午4:27:20   
 *     
 * @Copyright: 2018 Sumavision. All rights reserved. 
 * 注意：本内容仅限于北京数码视讯科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
public class ForwardSetBO {
	
	/**
	 * 任务Id
	 */
	private String taskId;

	
	private SrcBO src;
	
	private DstBO dst;	
	
	private JSONArray screens;
	
	
	
	public JSONArray getScreens() {
		return screens;
	}

	public void setScreens(JSONArray screens) {
		this.screens = screens;
	}

	public SrcBO getSrc() {
		return src;
	}

	public void setSrc(SrcBO src) {
		this.src = src;
	}

	public DstBO getDst() {
		return dst;
	}

	public void setDst(DstBO dst) {
		this.dst = dst;
	}		

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	


	@Override
	public String toString() {
		return "ForwardSetBO [taskId=" + taskId + ", src=" + src + ", dst=" + dst + "]";
	}




	/**
	 * 
	 * @ClassName:  SrcBO   
	 * @Description:对流的源的描述 
	 * @author: 
	 * @date:   2018年7月13日 下午4:30:45   
	 *     
	 * @Copyright: 2018 Sumavision. All rights reserved. 
	 * 注意：本内容仅限于北京数码视讯科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
	 */
	public static class SrcBO{
		
		/**
		 * 源的类型 channel/combineVideo/combineAudio
		 */
		private String type;
		
		/**
		 * 任务Id
		 */
		private String taskId;
		
		/**
		 * 接入层Id
		 */
		private String layerId;
		
		/**
		 * 设备标识		
		 */
		private String bundleId;
		
		/**
		 * 设备能力通道ID
		 */
		private String channelId;
		
		/**
		 * 源的UUID，当类型为combineVideo/combineAudio并且是新建合屏/混音时使用
		 */
		private String uuid;
		
		/**
		 * 锁类型 write/read 
		 */
		private String lock_type;
		
		/**
		 * 转换为整数。仅在合屏源中有用。1显示，2隐藏
		 */
		private String visible;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}	

		public String getTaskId() {
			return taskId;
		}

		public void setTaskId(String taskId) {
			this.taskId = taskId;
		}

		public String getLayerId() {
			return layerId;
		}

		public void setLayerId(String layerId) {
			this.layerId = layerId;
		}

		public String getBundleId() {
			return bundleId;
		}

		public void setBundleId(String bundleId) {
			this.bundleId = bundleId;
		}

		public String getChannelId() {
			return channelId;
		}

		public void setChannelId(String channelId) {
			this.channelId = channelId;
		}

		public String getUuid() {
			return uuid;
		}

		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
		
		

		public String getLock_type() {
			return lock_type;
		}

		public void setLock_type(String lock_type) {
			this.lock_type = lock_type;
		}

		public String getVisible() {
			return visible;
		}

		public void setVisible(String visible) {
			this.visible = visible;
		}

		@Override
		public String toString() {
			return "SrcBO [type=" + type + ", taskId=" + taskId + ", layerId=" + layerId + ", bundleId=" + bundleId
					+ ", channelId=" + channelId + ", uuid=" + uuid + ", visible=" + visible + "]";
		}

		
		
		
	}
	
	/**
	 * 
	 * @ClassName:  DstBO   
	 * @Description:对流的转发目的地的描述 
	 * @author: 
	 * @date:   2018年7月13日 下午4:30:45   
	 *     
	 * @Copyright: 2018 Sumavision. All rights reserved. 
	 * 注意：本内容仅限于北京数码视讯科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
	 */
	public static class DstBO{
		
		/**
		 * 任务Id
		 */
		private String taskId;
				
		/**
		 * 接入层Id
		 */
		private String layerId;
		
		/**
		 * 设备标识		
		 */
		private String bundleId;
		
		/**
		 * 设备能力通道ID
		 */
		private String channelId;
		
		/**
		 * 锁类型 write/read 
		 */
		private String lock_type;
		
		/**
		 * channelParam的基本能力参数类型
		 */
		private String base_type;
		
		/**
		 * 编解码参数
		 */
		private JSONObject codec_param;
		
		/**
		 * 字幕
		 */
		private JSONArray osds;

		public String getTaskId() {
			return taskId;
		}

		public void setTaskId(String taskId) {
			this.taskId = taskId;
		}

		public String getLayerId() {
			return layerId;
		}

		public void setLayerId(String layerId) {
			this.layerId = layerId;
		}

		public String getBundleId() {
			return bundleId;
		}

		public void setBundleId(String bundleId) {
			this.bundleId = bundleId;
		}

		public String getChannelId() {
			return channelId;
		}

		public void setChannelId(String channelId) {
			this.channelId = channelId;
		}				

		public String getLock_type() {
			return lock_type;
		}

		public void setLock_type(String lock_type) {
			this.lock_type = lock_type;
		}				

		public String getBase_type() {
			return base_type;
		}

		public void setBase_type(String base_type) {
			this.base_type = base_type;
		}

		public JSONObject getCodec_param() {
			return codec_param;
		}

		public JSONArray getOsds() {
			return osds;
		}

		public void setOsds(JSONArray osds) {
			this.osds = osds;
		}

		public void setCodec_param(JSONObject codec_param) {
			this.codec_param = codec_param;
		}

		@Override
		public String toString() {
			return "DstBO [taskId=" + taskId + ", layerId=" + layerId + ", bundleId=" + bundleId + ", channelId="
					+ channelId + "]";
		}		
		
	}
		
}
