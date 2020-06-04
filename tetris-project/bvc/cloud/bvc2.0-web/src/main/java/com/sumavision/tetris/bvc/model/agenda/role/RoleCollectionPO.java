package com.sumavision.tetris.bvc.model.agenda.role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 议程转发中用到的角色或角色通道集合<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:28:54
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_AGENDA_ROLE_COLLECTION")
public class RoleCollectionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 集合名称--可有可无 */
	private String name;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
