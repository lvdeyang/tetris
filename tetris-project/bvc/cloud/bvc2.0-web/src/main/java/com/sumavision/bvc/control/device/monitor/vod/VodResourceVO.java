package com.sumavision.bvc.control.device.monitor.vod;

/**
 * 点播资源<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年4月25日 上午11:15:56
 */
public class VodResourceVO {

	/** 资源id */
	private String id;
	
	/** 名称 */
	private String name;
	
	/** 预览地址 */
	private String previewUrl;
	
	/** 隶属文件夹 */
	private Long folderId;
	
	/** 创建用户 */
	private String userId;

	public String getId() {
		return id;
	}

	public VodResourceVO setId(String id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public VodResourceVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public VodResourceVO setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		return this;
	}

	public Long getFolderId() {
		return folderId;
	}

	public VodResourceVO setFolderId(Long folderId) {
		this.folderId = folderId;
		return this;
	}

	public String getUserId() {
		return userId;
	}

	public VodResourceVO setUserId(String userId) {
		this.userId = userId;
		return this;
	}
	
}
