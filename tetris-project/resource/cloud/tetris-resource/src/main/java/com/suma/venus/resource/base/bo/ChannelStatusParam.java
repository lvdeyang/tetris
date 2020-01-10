package com.suma.venus.resource.base.bo;

public class ChannelStatusParam {

	private String bundle_id;

	private String channel_id;
	
	private boolean writeLocking;
	
	private boolean readLocking;
	
	private int operate_index;
	
	public ChannelStatusParam() {
	}
	
	public ChannelStatusParam(String bundle_id, String channel_id) {
		super();
		this.bundle_id = bundle_id;
		this.channel_id = channel_id;
	}
	
	public ChannelStatusParam(String bundle_id, String channel_id, int operate_index) {
		super();
		this.bundle_id = bundle_id;
		this.channel_id = channel_id;
		this.operate_index = operate_index;
	}

	public ChannelStatusParam(String bundle_id, String channel_id,boolean writeLocking, boolean readLocking,int operata_index) {
		super();
		this.bundle_id = bundle_id;
		this.channel_id = channel_id;
		this.writeLocking = writeLocking;
		this.readLocking = readLocking;
		this.operate_index = operata_index;
	}

	public String getBundle_id() {
		return bundle_id;
	}

	public void setBundle_id(String bundle_id) {
		this.bundle_id = bundle_id;
	}


	public boolean isWriteLocking() {
		return writeLocking;
	}

	public void setWriteLocking(boolean writeLocking) {
		this.writeLocking = writeLocking;
	}

	public boolean isReadLocking() {
		return readLocking;
	}

	public void setReadLocking(boolean readLocking) {
		this.readLocking = readLocking;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}

	public int getOperate_index() {
		return operate_index;
	}

	public void setOperate_index(int operate_index) {
		this.operate_index = operate_index;
	}
	
}
