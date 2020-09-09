package com.suma.venus.resource.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 编解码器和用户映射<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年1月7日 下午2:39:10
 */
@Entity
@Table(name = "encoder_decoder_user_map")
public class EncoderDecoderUserMap extends CommonPO<EncoderDecoderUserMap> {

	/** 用户id */
	private Long userId;
	
	/** 用户名 */
	private String userName;
	
	/** 编码器id--对应BundlePO的id */
	private Long encodeId;
	
	/** 编码器bundleId */
	private String encodeBundleId;
	
	/** 编码器名 */
	private String encodeBundleName;
	
	/** 编码器类型 */
	private String encodeDeviceModel;
	
	/** 解码器id--对应BundlePO的id */
	private Long decodeId;
	
	/** 解码器bundleId */
	private String decodeBundleId;
	
	/** 解码器名 */
	private String decodeBundleName;
	
	/** 解码器类型 */
	private String decodeDeviceModel;

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

	public Long getEncodeId() {
		return encodeId;
	}

	public void setEncodeId(Long encodeId) {
		this.encodeId = encodeId;
	}

	public String getEncodeBundleId() {
		return encodeBundleId;
	}

	public void setEncodeBundleId(String encodeBundleId) {
		this.encodeBundleId = encodeBundleId;
	}

	public String getEncodeBundleName() {
		return encodeBundleName;
	}

	public void setEncodeBundleName(String encodeBundleName) {
		this.encodeBundleName = encodeBundleName;
	}

	public Long getDecodeId() {
		return decodeId;
	}

	public void setDecodeId(Long decodeId) {
		this.decodeId = decodeId;
	}

	public String getDecodeBundleId() {
		return decodeBundleId;
	}

	public void setDecodeBundleId(String decodeBundleId) {
		this.decodeBundleId = decodeBundleId;
	}

	public String getDecodeBundleName() {
		return decodeBundleName;
	}

	public void setDecodeBundleName(String decodeBundleName) {
		this.decodeBundleName = decodeBundleName;
	}

	public String getEncodeDeviceModel() {
		return encodeDeviceModel;
	}

	public void setEncodeDeviceModel(String encodeDeviceModel) {
		this.encodeDeviceModel = encodeDeviceModel;
	}

	public String getDecodeDeviceModel() {
		return decodeDeviceModel;
	}

	public void setDecodeDeviceModel(String decodeDeviceModel) {
		this.decodeDeviceModel = decodeDeviceModel;
	}
	
}
