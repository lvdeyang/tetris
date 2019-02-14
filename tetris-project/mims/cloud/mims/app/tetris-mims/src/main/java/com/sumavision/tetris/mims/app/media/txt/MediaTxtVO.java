package com.sumavision.tetris.mims.app.media.txt;

import java.util.Arrays;
import java.util.List;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MediaTxtVO extends AbstractBaseVO<MediaTxtVO, MediaTxtPO>{

	private String content;
	
	private String name;
	
	private String authorName;
	
	private String createTime;
	
	private String remarks;
	
	private List<String> tags;
	
	private List<String> keyWords;
	
	private String type;
	
	private String icon;
	
	private String style;
	
	public String getContent() {
		return content;
	}

	public MediaTxtVO setContent(String content) {
		this.content = content;
		return this;
	}

	public String getName() {
		return name;
	}

	public MediaTxtVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getAuthorName() {
		return authorName;
	}

	public MediaTxtVO setAuthorName(String authorName) {
		this.authorName = authorName;
		return this;
	}

	public String getCreateTime() {
		return createTime;
	}

	public MediaTxtVO setCreateTime(String createTime) {
		this.createTime = createTime;
		return this;
	}

	public String getRemarks() {
		return remarks;
	}

	public MediaTxtVO setRemarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	public List<String> getTags() {
		return tags;
	}

	public MediaTxtVO setTags(List<String> tags) {
		this.tags = tags;
		return this;
	}

	public List<String> getKeyWords() {
		return keyWords;
	}

	public MediaTxtVO setKeyWords(List<String> keyWords) {
		this.keyWords = keyWords;
		return this;
	}
	
	public String getType() {
		return type;
	}

	public MediaTxtVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public MediaTxtVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public MediaTxtVO setStyle(String style) {
		this.style = style;
		return this;
	}
	
	@Override
	public MediaTxtVO set(MediaTxtPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			//.setContent(entity.getContent())
			.setName(entity.getName())
			.setAuthorName(entity.getAuthorName())
			.setCreateTime(entity.getCreateTime()==null?"":DateUtil.format(entity.getCreateTime(), DateUtil.dateTimePattern))
			.setRemarks(entity.getRemarks())
			.setType(MediaTxtItemType.TXT.toString())
			.setIcon(MediaTxtItemType.TXT.getIcon())
			.setStyle(MediaTxtItemType.TXT.getStyle()[0]);
		if(entity.getTags() != null) this.setTags(Arrays.asList(entity.getTags().split(MediaTxtPO.SEPARATOR_TAG)));
		if(entity.getKeyWords() != null) this.setKeyWords(Arrays.asList(entity.getKeyWords().split(MediaTxtPO.SEPARATOR_KEYWORDS)));	 
		return this;
	}
	
	public MediaTxtVO set(FolderPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setContent("-")
			.setName(entity.getName())
			.setAuthorName(entity.getAuthorName())
			.setCreateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setRemarks("-")
			.setType(MediaTxtItemType.FOLDER.toString())
			.setIcon(MediaTxtItemType.FOLDER.getIcon())
			.setStyle(MediaTxtItemType.FOLDER.getStyle()[0]);
		return this;
	}
	
}
