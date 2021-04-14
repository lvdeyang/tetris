package com.sumavision.tetris.mims.app.media.tag;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class TagVO extends AbstractBaseVO<TagVO, TagPO>{
	private String name;
	
	private Long ParentId;
	
	private String remark;
	
	private Boolean disabled = false;
	
	private List<TagVO> subColumns;
	
	private int subMediaNum;
	
	private Long downloadCount;
	
	private Long hotCount;

	public String getName() {
		return name;
	}

	public TagVO setName(String name) {
		this.name = name;
		return this;
	}

	public Long getParentId() {
		return ParentId;
	}

	public TagVO setParentId(Long parentId) {
		ParentId = parentId;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public TagVO setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public TagVO setDisabled(Boolean disabled) {
		this.disabled = disabled;
		return this;
	}

	public List<TagVO> getSubColumns() {
		return subColumns;
	}

	public TagVO setSubColumns(List<TagVO> subColumns) {
		this.subColumns = subColumns;
		return this;
	}

	public int getSubMediaNum() {
		return subMediaNum;
	}

	public TagVO setSubMediaNum(int subMediaNum) {
		this.subMediaNum = subMediaNum;
		return this;
	}

	public Long getDownloadCount() {
		return downloadCount;
	}

	public TagVO setDownloadCount(Long downloadCount) {
		this.downloadCount = downloadCount;
		return this;
	}

	@Override
	public TagVO set(TagPO entity) throws Exception {
		this.setId(entity.getId())
		.setUuid(entity.getUuid())
		.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
		.setName(entity.getName())
		.setParentId(entity.getParentId())
		.setRemark(entity.getRemark())
		.setSubMediaNum(0)
		.setHotCount(entity.getHotCount()==null?0:entity.getHotCount())
		.setDownloadCount(0l);
		return this;
	}

	public Long getHotCount() {
		return hotCount;
	}

	public TagVO setHotCount(Long hotCount) {
		this.hotCount = hotCount;
		return this;
	}
	
	/**
	 * @author 614
	 * 对标签进行排序
	 */
	public static final class TagComparator implements Comparator<TagVO>{
		
		@Override
		public int compare(TagVO o1, TagVO o2) {
			Collator instance = Collator.getInstance(Locale.CHINA);
			
			String str1 = o1.getName();
			String str2 = o2.getName();
			
			return instance.compare(str1, str2);

		}		
	}
	
}
