package com.sumavision.signal.bvc.network.po;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 网络调度输入输出映射--切换<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年2月28日 上午9:55:47
 */
@Entity
@Table(name = "TETRIS_SCL_NETWORK_MAP")
public class NetworkMapPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;

	/** 输入持久化id */
	private Long inputId;
	
	/** 输入调度id */
	private int inputSid;
	
	/** 输出持久化id */
	private Long outputId;
	
	/** 输出调度id */
	private int outputSid;

	public Long getInputId() {
		return inputId;
	}

	public void setInputId(Long inputId) {
		this.inputId = inputId;
	}

	public int getInputSid() {
		return inputSid;
	}

	public void setInputSid(int inputSid) {
		this.inputSid = inputSid;
	}

	public Long getOutputId() {
		return outputId;
	}

	public void setOutputId(Long outputId) {
		this.outputId = outputId;
	}

	public int getOutputSid() {
		return outputSid;
	}

	public void setOutputSid(int outputSid) {
		this.outputSid = outputSid;
	}
	
}
