package com.sumavision.tetris.mims.app.folder;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

/**
 * 文件夹面包屑数据<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月25日 上午10:24:21
 */
public class FolderBreadCrumbVO extends AbstractBaseVO<FolderBreadCrumbVO, FolderPO>{

	private String name;
	
	private String type;
	
	/* android另加，返回list，添加子节点id做标识 */
	private Long childId;
	
	/* android另加，返回list，添加父节点id做标识 */
	private Long parentId;

	private FolderBreadCrumbVO next;
	
	public String getName() {
		return name;
	}

	public FolderBreadCrumbVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getType() {
		return type;
	}

	public FolderBreadCrumbVO setType(String type) {
		this.type = type;
		return this;
	}
	
	public Long getChildId() {
		return childId;
	}
	
	public FolderBreadCrumbVO setChildId(Long childId) {
		this.childId = childId;
		return this;
	}
	
	public Long getParentId() {
		return parentId;
	}

	public FolderBreadCrumbVO setParentId(Long parentId) {
		this.parentId = parentId;
		return this;
	}
	
	public FolderBreadCrumbVO getNext() {
		return next;
	}

	public FolderBreadCrumbVO setNext(FolderBreadCrumbVO next) {
		this.next = next;
		return this;
	}

	@Override
	public FolderBreadCrumbVO set(FolderPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setType(entity.getType().toString());
		return this;
	}
	
}
