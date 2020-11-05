package com.sumavision.bvc.device.group.bo;

import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;

public class BundlePreviewBO extends BundleBO {

	private String previewPlayUrl;

	public String getPreviewPlayUrl() {
		return previewPlayUrl;
	}

	public BundlePreviewBO setPreviewPlayUrl(String previewPlayUrl) {
		this.previewPlayUrl = previewPlayUrl;
		return this;
	}
	
	/**
	 * @Title: 从设备组成员中复制数据<br/> 
	 * @param member 设备组成员
	 * @param url 预览地址
	 * @return BundleBO 业务数据 
	 */
	public BundlePreviewBO set(DeviceGroupMemberPO member, String url){
		this.setPreviewPlayUrl(url)
			.setId(member.getBundleId())
			.setBundleId(member.getBundleId())
			.setName(member.getBundleName())
			.setFolderId(member.getFolderId())
			.setNodeUid(member.getLayerId())
			.setOpenAudio(member.isOpenAudio())
			.setModel(member.getBundleType())
			.setMemberId(member.getId());
		return this;
	}
}
