package com.suma.venus.resource.base.bo;

import java.util.Comparator;
import com.sumavision.tetris.user.UserVO;

public class UserBO {
	
	private UserVO user;

	private Long id;

	private String name;

	private boolean isAdmin = false;

	private int level;

	private String phone;

	private String email;

	private String createTime;

	private String creater;

	private String encoderId;

	private String decoderId;

	private boolean logined = false;

	//用户号码
	private String userNo;

	// 所属分组ID
	private Long folderId;

	// 所属分组的uuid
	private String folderUuid;

	private Integer folderIndex;
	
	private EncoderBO local_encoder;
	
	private EncoderBO external_encoder;
	
	public UserVO getUser() {
		return user;
	}

	public void setUser(UserVO user) {
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getEncoderId() {
		return encoderId;
	}

	public void setEncoderId(String encoderId) {
		this.encoderId = encoderId;
	}

	public String getDecoderId() {
		return decoderId;
	}

	public void setDecoderId(String decoderId) {
		this.decoderId = decoderId;
	}

	public boolean isLogined() {
		return logined;
	}

	public void setLogined(boolean logined) {
		this.logined = logined;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	public String getFolderUuid() {
		return folderUuid;
	}

	public void setFolderUuid(String folderUuid) {
		this.folderUuid = folderUuid;
	}

	public Integer getFolderIndex() {
		return folderIndex;
	}

	public void setFolderIndex(Integer folderIndex) {
		this.folderIndex = folderIndex;
	}

	public EncoderBO getLocal_encoder() {
		return local_encoder;
	}

	public void setLocal_encoder(EncoderBO local_encoder) {
		this.local_encoder = local_encoder;
	}

	public EncoderBO getExternal_encoder() {
		return external_encoder;
	}

	public void setExternal_encoder(EncoderBO external_encoder) {
		this.external_encoder = external_encoder;
	}
	
	public static final class UserComparatorFromFolderIndex implements Comparator<UserBO>{
		@Override
		public int compare(UserBO o1, UserBO o2) {
			
			Integer id1 = o1.getFolderIndex();
			Integer id2 = o2.getFolderIndex();
			
			if(id1 == null){
				if(id2 == null) return 0;
				return -1;
			}else if(id2 == null){
				return 1;
			}
			
			return id1 - id2;
		}
	}
	
}
