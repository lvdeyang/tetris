package com.suma.venus.resource.vo;

public class UserBindBundleVO {

	/** 用户id */
	private Long userId;
	
	/** 用户名 */
	private String userName;
	
	private Long encodeId;
	
	/** 编码器bundleId */
	private String encodeBundleId;
	
	/** 编码器名 */
	private String encodeBundleName;
	
	private String encodeIp;
	
	private String encodeUserName;
	
	/** 解码器bundleId */
	private String decodeBundleId;
	
	private Long decodeId;
	
	/** 解码器名 */
	private String decodeBundleName;
	
	private String decodeIp;
	
	private String decodeUserName;

	public Long getUserId() {
		return userId;
	}

	public UserBindBundleVO setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public String getUserName() {
		return userName;
	}

	public UserBindBundleVO setUserName(String userName) {
		this.userName = userName;
		return this;
	}

	public String getEncodeBundleId() {
		return encodeBundleId;
	}

	public UserBindBundleVO setEncodeBundleId(String encodeBundleId) {
		this.encodeBundleId = encodeBundleId;
		return this;
	}

	public String getEncodeBundleName() {
		return encodeBundleName;
	}

	public UserBindBundleVO setEncodeBundleName(String encodeBundleName) {
		this.encodeBundleName = encodeBundleName;
		return this;
	}

	public String getEncodeIp() {
		return encodeIp;
	}

	public UserBindBundleVO setEncodeIp(String encodeIp) {
		this.encodeIp = encodeIp;
		return this;
	}

	public String getEncodeUserName() {
		return encodeUserName;
	}

	public UserBindBundleVO setEncodeUserName(String encodeUserName) {
		this.encodeUserName = encodeUserName;
		return this;
	}

	public String getDecodeBundleId() {
		return decodeBundleId;
	}

	public UserBindBundleVO setDecodeBundleId(String decodeBundleId) {
		this.decodeBundleId = decodeBundleId;
		return this;
	}

	public String getDecodeBundleName() {
		return decodeBundleName;
	}

	public UserBindBundleVO setDecodeBundleName(String decodeBundleName) {
		this.decodeBundleName = decodeBundleName;
		return this;
	}

	public String getDecodeIp() {
		return decodeIp;
	}

	public UserBindBundleVO setDecodeIp(String decodeIp) {
		this.decodeIp = decodeIp;
		return this;
	}

	public String getDecodeUserName() {
		return decodeUserName;
	}

	public UserBindBundleVO setDecodeUserName(String decodeUserName) {
		this.decodeUserName = decodeUserName;
		return this;
	}

	public Long getEncodeId() {
		return encodeId;
	}

	public UserBindBundleVO setEncodeId(Long encodeId) {
		this.encodeId = encodeId;
		return this;
	}

	public Long getDecodeId() {
		return decodeId;
	}

	public UserBindBundleVO setDecodeId(Long decodeId) {
		this.decodeId = decodeId;
		return this;
	}
	
}
