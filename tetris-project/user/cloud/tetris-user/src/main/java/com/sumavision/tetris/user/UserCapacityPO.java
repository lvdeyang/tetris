package com.sumavision.tetris.user;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_USER_CAPACITY")
public class UserCapacityPO extends AbstractBasePO{

	public Long userCapacityLong;

	public Long getUserCapacityLong() {
		return userCapacityLong;
	}

	public void setUserCapacityLong(Long userCapacityLong) {
		this.userCapacityLong = userCapacityLong;
	}
}
