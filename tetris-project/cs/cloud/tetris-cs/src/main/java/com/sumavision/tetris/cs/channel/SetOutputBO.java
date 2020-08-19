package com.sumavision.tetris.cs.channel;

import java.util.List;

import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityBroadInfoVO;
import com.sumavision.tetris.user.UserVO;

public class SetOutputBO {
	/** 选择预播发用户 */
	private List<UserVO> outputUsers;
	
	/** 预播发用户推流可用起始端口 */
	private String outputUserPort;
	
	/** 预播发用户推流可用终止端口 */
	private String outputUserEndPort;
	
	/** 预播发Ip/Port对 */
	private List<BroadAbilityBroadInfoVO> output;

	public List<UserVO> getOutputUsers() {
		return outputUsers;
	}

	public SetOutputBO setOutputUsers(List<UserVO> outputUsers) {
		this.outputUsers = outputUsers;
		return this;
	}

	public String getOutputUserPort() {
		return outputUserPort;
	}

	public SetOutputBO setOutputUserPort(String outputUserPort) {
		this.outputUserPort = outputUserPort;
		return this;
	}

	public String getOutputUserEndPort() {
		return outputUserEndPort;
	}

	public SetOutputBO setOutputUserEndPort(String outputUserEndPort) {
		this.outputUserEndPort = outputUserEndPort;
		return this;
	}

	public List<BroadAbilityBroadInfoVO> getOutput() {
		return output;
	}

	public SetOutputBO setOutput(List<BroadAbilityBroadInfoVO> output) {
		this.output = output;
		return this;
	}
}
