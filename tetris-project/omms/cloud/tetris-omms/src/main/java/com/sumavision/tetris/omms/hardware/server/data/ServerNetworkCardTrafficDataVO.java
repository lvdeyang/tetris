package com.sumavision.tetris.omms.hardware.server.data;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class ServerNetworkCardTrafficDataVO extends AbstractBaseVO<ServerNetworkCardTrafficDataVO, ServerNetworkCardTrafficDataPO>{

	/** 网卡名称 */
	private String name;
	
	/** 收包速率 kbps*/
	private Long rxkB;
	
	/** 发包速率 kbps*/
	private Long txkB;
	
	public String getName() {
		return name;
	}

	public ServerNetworkCardTrafficDataVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getRxkB() {
		return rxkB;
	}

	public ServerNetworkCardTrafficDataVO setRxkB(Long rxkB) {
		this.rxkB = rxkB;
		return this;
	}

	public Long getTxkB() {
		return txkB;
	}

	public ServerNetworkCardTrafficDataVO setTxkB(Long txkB) {
		this.txkB = txkB;
		return this;
	}

	@Override
	public ServerNetworkCardTrafficDataVO set(ServerNetworkCardTrafficDataPO entity) throws Exception {
		this.setId(entity.getId())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setRxkB(entity.getRxkB())
			.setTxkB(entity.getTxkB());
		return this;
	}

}
