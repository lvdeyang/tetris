package com.sumavision.tetris.agent.vo;

public class RemoteVO {

	private String local_channel_no;
	
	private int priority;
	
	private RemotePullVO remote_video_pull;
	
	private RemotePullVO remote_audio_pull;

	public String getLocal_channel_no() {
		return local_channel_no;
	}

	public RemoteVO setLocal_channel_no(String local_channel_no) {
		this.local_channel_no = local_channel_no;
		return this;
	}

	public int getPriority() {
		return priority;
	}

	public RemoteVO setPriority(int priority) {
		this.priority = priority;
		return this;
	}

	public RemotePullVO getRemote_video_pull() {
		return remote_video_pull;
	}

	public RemoteVO setRemote_video_pull(RemotePullVO remote_video_pull) {
		this.remote_video_pull = remote_video_pull;
		return this;
	}

	public RemotePullVO getRemote_audio_pull() {
		return remote_audio_pull;
	}

	public RemoteVO setRemote_audio_pull(RemotePullVO remote_audio_pull) {
		this.remote_audio_pull = remote_audio_pull;
		return this;
	}
	
}
