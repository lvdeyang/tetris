package com.sumavision.bvc.system.dto;

/**
 * @ClassName: 会议模板内容——屏幕布局 
 * @author lvdeyang
 * @date 2018年7月30日 上午10:24:13 
 */
public class TplContentScreenLayoutDTO {

	/** 会议模板内容id */
	private Long contentId;
	
	/** 会议模板id */
	private Long tplId;
	
	/** 屏幕布局id */
	private Long layoutId;
	
	/** 屏幕布局名称 */
	private String layoutName;
	
	/** 网页端布局数据，格式：{basic:{column:4, row:4}, cellspan:[{x:0, y0, r:1, b:1}]}*/
	private String websiteDraw;

	public TplContentScreenLayoutDTO(){}
	
	public TplContentScreenLayoutDTO(Long contentId, Long tplId, Long layoutId, String layoutName, String websiteDraw){
		this.contentId = contentId;
		this.tplId = tplId;
		this.layoutId = layoutId;
		this.layoutName = layoutName;
		this.websiteDraw = websiteDraw;
	}
	
	public Long getContentId() {
		return contentId;
	}

	public TplContentScreenLayoutDTO setContentId(Long contentId) {
		this.contentId = contentId;
		return this;
	}

	public Long getTplId() {
		return tplId;
	}

	public TplContentScreenLayoutDTO setTplId(Long tplId) {
		this.tplId = tplId;
		return this;
	}

	public Long getLayoutId() {
		return layoutId;
	}

	public TplContentScreenLayoutDTO setLayoutId(Long layoutId) {
		this.layoutId = layoutId;
		return this;
	}

	public String getLayoutName() {
		return layoutName;
	}

	public TplContentScreenLayoutDTO setLayoutName(String layoutName) {
		this.layoutName = layoutName;
		return this;
	}

	public String getWebsiteDraw() {
		return websiteDraw;
	}

	public TplContentScreenLayoutDTO setWebsiteDraw(String websiteDraw) {
		this.websiteDraw = websiteDraw;
		return this;
	}
	
}
