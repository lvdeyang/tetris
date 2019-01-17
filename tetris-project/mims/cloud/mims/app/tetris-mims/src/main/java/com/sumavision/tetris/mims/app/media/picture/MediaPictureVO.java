package com.sumavision.tetris.mims.app.media.picture;

import java.util.Arrays;
import java.util.List;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MediaPictureVO extends AbstractBaseVO<MediaPictureVO, MediaPicturePO>{

	private String name;
	
	private String authorName;
	
	private Long size;
	
	private String createTime;
	
	private String version;
	
	private String remarks;
	
	private List<String> tags;
	
	private List<String> keyWords;
	
	private String type;
	
	private String icon;
	
	private String style;
	
	public String getName() {
		return name;
	}

	public MediaPictureVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getAuthorName() {
		return authorName;
	}

	public MediaPictureVO setAuthorName(String authorName) {
		this.authorName = authorName;
		return this;
	}

	public Long getSize() {
		return size;
	}

	public MediaPictureVO setSize(Long size) {
		this.size = size;
		return this;
	}

	public String getCreateTime() {
		return createTime;
	}

	public MediaPictureVO setCreateTime(String createTime) {
		this.createTime = createTime;
		return this;
	}

	public String getVersion() {
		return version;
	}

	public MediaPictureVO setVersion(String version) {
		this.version = version;
		return this;
	}

	public String getRemarks() {
		return remarks;
	}

	public MediaPictureVO setRemarks(String remarks) {
		this.remarks = remarks;
		return this;
	}

	public List<String> getTags() {
		return tags;
	}

	public MediaPictureVO setTags(List<String> tags) {
		this.tags = tags;
		return this;
	}

	public List<String> getKeyWords() {
		return keyWords;
	}

	public MediaPictureVO setKeyWords(List<String> keyWords) {
		this.keyWords = keyWords;
		return this;
	}
	
	public String getType() {
		return type;
	}

	public MediaPictureVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public MediaPictureVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStyle() {
		return style;
	}

	public MediaPictureVO setStyle(String style) {
		this.style = style;
		return this;
	}

	@Override
	public MediaPictureVO set(MediaPicturePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setAuthorName(entity.getAuthorName())
			.setSize(entity.getSize())
			.setCreateTime(entity.getCreateTime()==null?"":DateUtil.format(entity.getCreateTime(), DateUtil.dateTimePattern))
			.setVersion(entity.getVersion())
			.setRemarks(entity.getRemarks())
			.setType(MediaPictureItemType.PICTURE.toString())
			.setIcon(MediaPictureItemType.PICTURE.getIcon())
			.setStyle(MediaPictureItemType.PICTURE.getStyle()[0]);
		if(entity.getTags() != null) this.setTags(Arrays.asList(entity.getTags().split(MediaPicturePO.SEPARATOR_TAG)));
		if(entity.getKeyWords() != null) this.setKeyWords(Arrays.asList(entity.getKeyWords().split(MediaPicturePO.SEPARATOR_KEYWORDS)));	 
		return this;
	}
	
	public MediaPictureVO set(FolderPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setAuthorName(entity.getAuthorName())
			.setSize(0l)
			.setCreateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setVersion("-")
			.setRemarks("-")
			.setType(MediaPictureItemType.FOLDER.toString())
			.setIcon(MediaPictureItemType.FOLDER.getIcon())
			.setStyle(MediaPictureItemType.FOLDER.getStyle()[0]);
		return this;
	}
	
}
