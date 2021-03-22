package com.sumavision.tetris.mims.app.media.tag;

import com.alibaba.excel.annotation.ExcelProperty;

public class TagsExcelModel {

	 @ExcelProperty(value = "媒资名", index = 0)
	 private String mediaName;

	 @ExcelProperty(value = "标签名", index = 1)
	 private String tagName;

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

}
