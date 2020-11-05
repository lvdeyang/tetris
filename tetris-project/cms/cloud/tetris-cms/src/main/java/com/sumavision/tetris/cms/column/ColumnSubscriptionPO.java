package com.sumavision.tetris.cms.column;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 用户订阅栏目关联表<br/>
 * <b>作者:</b>lzp<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年4月18日 下午3:45:09
 */
@Table
@Entity(name="TETRIS_CMS_COLUMN_SUBSCRIPTION")
public class ColumnSubscriptionPO extends AbstractBasePO{
	private static final long serialVersionUID = 1L;
	
	/** 用户id */
	private Long userId;
	
	/** 栏目id */
	private Long columnId;

	@Column(name="USER_ID")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name="COLUMN_ID")
	public Long getColumnId() {
		return columnId;
	}

	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}
}
