package com.sumavision.bvc.BO;


import java.util.List;

import com.suma.venus.resource.base.bo.ErrorDescription;

public class OperateChannelRespParam {
	
	private List<ChannelRespInfo> channels;		
	
	public List<ChannelRespInfo> getChannels() {
		return channels;
	}



	public void setChannels(List<ChannelRespInfo> channels) {
		this.channels = channels;
	}



	public static class ChannelRespInfo{
		
		private String bundle_id;
		
		private Integer channel_id;
		
		private String result;
		
		private ErrorDescription error_description;

		public String getBundle_id() {
			return bundle_id;
		}

		public void setBundle_id(String bundle_id) {
			this.bundle_id = bundle_id;
		}

		public Integer getChannel_id() {
			return channel_id;
		}

		public void setChannel_id(Integer channel_id) {
			this.channel_id = channel_id;
		}

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}

		public ErrorDescription getError_description() {
			return error_description;
		}

		public void setError_description(ErrorDescription error_description) {
			this.error_description = error_description;
		}

		@Override
		public String toString() {
			return "ChannelRespInfo [bundle_id=" + bundle_id + ", channel_id=" + channel_id + ", result=" + result
					+ ", error_description=" + error_description + "]";
		}
		
		
		
		
	}
}
