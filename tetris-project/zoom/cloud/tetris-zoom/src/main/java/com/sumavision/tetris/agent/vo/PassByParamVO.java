package com.sumavision.tetris.agent.vo;

import java.util.List;

public class PassByParamVO {

	private LocalPublishVO local_publish;
	
	private List<RemoteVO> remote;

	public LocalPublishVO getLocal_publish() {
		return local_publish;
	}

	public PassByParamVO setLocal_publish(LocalPublishVO local_publish) {
		this.local_publish = local_publish;
		return this;
	}

	public List<RemoteVO> getRemote() {
		return remote;
	}

	public PassByParamVO setRemote(List<RemoteVO> remote) {
		this.remote = remote;
		return this;
	}
	
}
