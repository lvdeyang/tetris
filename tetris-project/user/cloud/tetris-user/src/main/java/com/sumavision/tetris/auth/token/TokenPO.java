package com.sumavision.tetris.auth.token;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_TOKEN")
public class TokenPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	private String token;
	
	private Long userId;
	
	private TerminalType type;
	
	
}
