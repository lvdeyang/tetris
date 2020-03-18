package com.sumavision.tetris.zoom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 会议表<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月2日 上午9:12:10
 */
@Entity
@Table(name = "TETRIS_ZOOM")
public class ZoomPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 会议名称 */
	private String name;
	
	/** 会议号码 */
	private String code;
	
	/** 会议呼入密码 */
	private String password;
	
	/** 会议状态 */
	private ZoomStatus status;

	/** 保密等级 */
	private ZoomSecretLevel secretLevel;
	
	/** 会议模式 */
	private ZoomMode mode;
	
	/** 创建者用户id */
	private Long creatorUserId;
	
	/** 创建者用户昵称 */
	private String creatorUserNickname;
	
	/** 创建者入会名称 */
	private String creatorUserRename;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "PASSWORD")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "STATUS")
	public ZoomStatus getStatus() {
		return status;
	}

	public void setStatus(ZoomStatus status) {
		this.status = status;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "SECRET_LEVEL")
	public ZoomSecretLevel getSecretLevel() {
		return secretLevel;
	}

	public void setSecretLevel(ZoomSecretLevel secretLevel) {
		this.secretLevel = secretLevel;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "MODE")
	public ZoomMode getMode() {
		return mode;
	}

	public void setMode(ZoomMode mode) {
		this.mode = mode;
	}

	@Column(name = "CREATOR_USER_ID")
	public Long getCreatorUserId() {
		return creatorUserId;
	}

	public void setCreatorUserId(Long creatorUserId) {
		this.creatorUserId = creatorUserId;
	}

	@Column(name = "CREATOR_USER_NICKNAME")
	public String getCreatorUserNickname() {
		return creatorUserNickname;
	}

	public void setCreatorUserNickname(String creatorUserNickname) {
		this.creatorUserNickname = creatorUserNickname;
	}

	@Column(name = "CREATOR_USER_RENAME")
	public String getCreatorUserRename() {
		return creatorUserRename;
	}

	public void setCreatorUserRename(String creatorUserRename) {
		this.creatorUserRename = creatorUserRename;
	}
	
}
