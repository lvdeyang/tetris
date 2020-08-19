package com.sumavision.tetris.bvc.business.vod;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_BVC_VOD")
public class VodPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	private Long srcMemberId;
	
	private Long dstMemberId;
	
	/** 发起人id */
	private Long userId;
	
	/** 发起人名称 */
	private String userName;

	/** 点播源类型：用户/设备/文件 */
	private VodType vodType;
	
	/** 源的用户名/设备名/文件名 */
	private String srcName;
	
	/** 目的类型：设备/用户 */
	private DstType dstType;
	
	/** 目的设备id */
	private String dstBundleId;
	
	/** 目的设备名称 */
	private String dstBundleName;
	
	/** 隶属业务id */
	private Long groupId;

	public Long getSrcMemberId() {
		return srcMemberId;
	}

	public void setSrcMemberId(Long srcMemberId) {
		this.srcMemberId = srcMemberId;
	}

	public Long getDstMemberId() {
		return dstMemberId;
	}

	public void setDstMemberId(Long dstMemberId) {
		this.dstMemberId = dstMemberId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public DstType getDstType() {
		return dstType;
	}

	public void setDstType(DstType dstType) {
		this.dstType = dstType;
	}

	public String getDstBundleId() {
		return dstBundleId;
	}

	public void setDstBundleId(String dstBundleId) {
		this.dstBundleId = dstBundleId;
	}

	public String getDstBundleName() {
		return dstBundleName;
	}

	public void setDstBundleName(String dstBundleName) {
		this.dstBundleName = dstBundleName;
	}

	public String getSrcName() {
		return srcName;
	}

	public void setSrcName(String srcName) {
		this.srcName = srcName;
	}

	public VodType getVodType() {
		return vodType;
	}

	public void setVodType(VodType vodType) {
		this.vodType = vodType;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public VodPO(){}

	/*public VodPO(VodType vodType, Long sourceUserId, String sourceNo, String sourceUserName, String sourceBundleId,
			String sourceBundleName, String sourceBundleType, String sourceLayerId, String sourceVideoChannelId,
			String sourceVideoBaseType, String sourceAudioChannelId, String sourceAudioBaseType, Long dstUserId, String dstUserNo,
			String dstUserName, String dstBundleId, String dstBundleName, String dstBundleType, String dstLayerId,
			String dstVideoChannelId, String dstVideoBaseType, String dstAudioChannelId, String dstAudioBaseType) {
		this.vodType = vodType;
		this.sourceUserId = sourceUserId;
		this.sourceNo = sourceNo;
		this.sourceUserName = sourceUserName;
		this.sourceBundleId = sourceBundleId;
		this.sourceBundleName = sourceBundleName;
		this.sourceBundleType = sourceBundleType;
		this.sourceLayerId = sourceLayerId;
		this.sourceVideoChannelId = sourceVideoChannelId;
		this.sourceVideoBaseType = sourceVideoBaseType;
		this.sourceAudioChannelId = sourceAudioChannelId;
		this.sourceAudioBaseType = sourceAudioBaseType;
		this.dstUserId = dstUserId;
		this.dstUserNo = dstUserNo;
		this.dstUserName = dstUserName;
		this.dstBundleId = dstBundleId;
		this.dstBundleName = dstBundleName;
		this.dstBundleType = dstBundleType;
		this.dstLayerId = dstLayerId;
		this.dstVideoChannelId = dstVideoChannelId;
		this.dstVideoBaseType = dstVideoBaseType;
		this.dstAudioChannelId = dstAudioChannelId;
		this.dstAudioBaseType = dstAudioBaseType;
	}*/

}
