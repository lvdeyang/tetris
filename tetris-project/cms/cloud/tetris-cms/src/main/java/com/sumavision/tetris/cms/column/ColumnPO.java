package com.sumavision.tetris.cms.column;

import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 栏目<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年2月14日 下午2:18:25
 */
@Entity
@Table(name = "TETRIS_CMS_COLUMN")
public class ColumnPO extends AbstractBasePO{

	/** 这是一个常量的说明 */
	private static final long serialVersionUID = 1L;
	
	/** 栏目名称 */
	private String name;
	
	/** 栏目编号 */
	private String code;
	
	/** 栏目备注 */
	private String remark;
	
	/** 栏目缩略图 */
	private String thumbnail;
	
	/** 父栏目id */
	private Long parentId;
	
	/** 上级栏目id路径：/id/id/id */
	private String parentPath;
	
	/** 栏目顺序--由小到大排序  */
	private Long columnOrder;
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name = "THUMBNAIL")
	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	@Column(name = "COLUMN_ORDER")
	public Long getColumnOrder() {
		return columnOrder;
	}

	public void setColumnOrder(Long columnOrder) {
		this.columnOrder = columnOrder;
	}

	@Column(name = "PARENT_ID")
	public Long getParentId() {
		return parentId;
	}
	
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	@Column(name = "PARENT_PATH")
	public String getParentPath() {
		return parentPath;
	}
	
	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}
	
	/**
	 * @ClassName: 排序器，从小到大排列<br/> 
	 * @author lzp
	 * @date 2019年2月27日 上午8:36:10 
	 */
	public static final class ColumnOrderComparator implements Comparator<ColumnPO>{
		@Override
		public int compare(ColumnPO o1, ColumnPO o2) {
			
			if(o1.getColumnOrder() > o2.getColumnOrder()){
				return 1;
			}
			if(o1.getColumnOrder() == o2.getColumnOrder()){
				return 0;
			}
			return -1;
		}
	}
	
	public static final class ColumnVOOrderComparator implements Comparator<ColumnVO>{
		@Override
		public int compare(ColumnVO o1, ColumnVO o2) {
			
			if(o1.getColumnOrder() > o2.getColumnOrder()){
				return 1;
			}
			if(o1.getColumnOrder() == o2.getColumnOrder()){
				return 0;
			}
			return -1;
		}
	}
}
