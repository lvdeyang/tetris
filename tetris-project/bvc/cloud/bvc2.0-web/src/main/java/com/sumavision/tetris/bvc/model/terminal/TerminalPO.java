package com.sumavision.tetris.bvc.model.terminal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_BVC_MODEL_TERMINAL")
public class TerminalPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 终端类型 */
	private TerminalType type;

	@Column(name = "TYPE")
	@Enumerated(value = EnumType.STRING)
	public TerminalType getType() {
		return type;
	}

	public void setType(TerminalType type) {
		this.type = type;
	}
	
}
