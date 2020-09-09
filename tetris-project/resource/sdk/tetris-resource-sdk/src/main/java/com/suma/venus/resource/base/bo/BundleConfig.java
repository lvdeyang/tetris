package com.suma.venus.resource.base.bo;

import java.util.ArrayList;
import java.util.List;

public class BundleConfig {

	private List<ChannelConfig> channels = new ArrayList<ChannelConfig>();

	public List<ChannelConfig> getChannels() {
		return channels;
	}

	public void setChannels(List<ChannelConfig> channels) {
		this.channels = channels;
	}

}