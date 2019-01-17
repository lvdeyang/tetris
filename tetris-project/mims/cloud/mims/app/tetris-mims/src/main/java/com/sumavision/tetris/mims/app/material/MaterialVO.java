package com.sumavision.tetris.mims.app.material;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

/**
 * 素材库页面数据项<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月22日 上午10:04:56
 */
public class MaterialVO extends AbstractBaseVO<MaterialVO, MaterialFilePO>{

	private String name;
	
	private String size;
	
	private String type;
	
	private String icon;
	
	private String style;
	
	private String mimetype;
	
	public String getName() {
		return name;
	}

	public MaterialVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getSize() {
		return size;
	}

	public MaterialVO setSize(String size) {
		this.size = size;
		return this;
	}

	public String getType() {
		return type;
	}

	public MaterialVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public MaterialVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public MaterialVO setStyle(String style) {
		this.style = style;
		return this;
	}
	
	public String getMimetype() {
		return mimetype;
	}

	public MaterialVO setMimetype(String mimetype) {
		this.mimetype = mimetype;
		return this;
	}

	@Override
	public MaterialVO set(MaterialFilePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setSize(entity.getSize().toString())
			.setType(entity.getType().toString())
			.setIcon(entity.getType().getIcon())
			.setStyle(entity.getType().getStyle()[0])
			.setMimetype(entity.getMimetype());
		return this;
	}
	
	public MaterialVO set(FolderPO entity) throws Exception{
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setSize("-")
			.setType(MaterialType.FOLDER.toString())
			.setIcon(MaterialType.FOLDER.getIcon())
			.setStyle(MaterialType.FOLDER.getStyle()[0]);
		return this;
	}
	
}
