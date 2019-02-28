package com.sumavision.tetris.cms.column;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 内容模板<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年2月14日 下午2:18:25
 */
@Entity
@Table(name = "TETRIS_CMS_COLUMN")
public class ColumnPO extends AbstractBasePO{

	/** 这是一个常量的说明 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String remark;
	private long parentId;
	private String parentPath;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	public String getParentPath() {
		return parentPath;
	}
	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}
	
}
