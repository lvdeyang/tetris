package com.sumavision.tetris.mims.app.folder;

import java.util.List;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

/**
 * 文件夹树<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月25日 上午10:25:21
 */
public class FolderTreeVO extends AbstractBaseVO<FolderTreeVO, FolderPO>{

	private String name;
	
	private String type;
	
	private List<FolderTreeVO> children;
	
	public String getName() {
		return name;
	}

	public FolderTreeVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getType() {
		return type;
	}

	public FolderTreeVO setType(String type) {
		this.type = type;
		return this;
	}

	public List<FolderTreeVO> getChildren() {
		return children;
	}

	public FolderTreeVO setChildren(List<FolderTreeVO> children) {
		this.children = children;
		return this;
	}

	@Override
	public FolderTreeVO set(FolderPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setType(entity.getType().toString());
		return this;
	}
	
}
