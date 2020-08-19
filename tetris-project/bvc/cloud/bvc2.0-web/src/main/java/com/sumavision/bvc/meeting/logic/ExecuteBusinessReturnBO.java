package com.sumavision.bvc.meeting.logic;

import java.util.List;

public class ExecuteBusinessReturnBO {
	
	private List<ResultDstBO> OutConnMediaMuxSet;
	
	private List<ResultDstBO> connectBundle;

	public List<ResultDstBO> getOutConnMediaMuxSet() {
		return OutConnMediaMuxSet;
	}

	public void setOutConnMediaMuxSet(List<ResultDstBO> outConnMediaMuxSet) {
		this.OutConnMediaMuxSet = outConnMediaMuxSet;
	}	

	public List<ResultDstBO> getConnectBundle() {
		return connectBundle;
	}

	public void setConnectBundle(List<ResultDstBO> connectBundle) {
		this.connectBundle = connectBundle;
	}

	public static class ResultDstBO{
		
		private String uuid;
		
		private String layerId;
		
		private String bundleId;
		
		private String channelId;

		public String getUuid() {
			return uuid;
		}

		public void setUuid(String uuid) {
			this.uuid = uuid;
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
	}
	
}
