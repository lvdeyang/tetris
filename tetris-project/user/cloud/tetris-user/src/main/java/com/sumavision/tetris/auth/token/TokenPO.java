package com.sumavision.tetris.auth.token;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.sumavision.tetris.orm.po.AbstractBasePO;
import com.sumavision.tetris.user.UserStatus;

/**
 * 多终端登录token列表<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年12月4日 下午1:36:15
 */
@Entity
@Table(name = "TETRIS_TOKEN", uniqueConstraints = @UniqueConstraint(columnNames={"USER_ID", "TYPE"}))
public class TokenPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** token */
	private String token;
	
	/** 用户id */
	private Long userId;
	
	/** 终端类型 */
	private TerminalType type;
	
	/** 登录ip */
	private String ip;
	
	/** 操作时间 */
	private Date lastModifyTime;

	/** 用户状态 */
	private UserStatus status;
	
	/**
	 * 生成新的token<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月6日 上午9:58:57
	 */
	public void newToken(){
		this.token = UUID.randomUUID().toString().replaceAll("-", "");
		this.lastModifyTime = new Date();
	}
	
	@Column(name = "TOKEN")
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Column(name = "USER_ID")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "TYPE")
	public TerminalType getType() {
		return type;
	}

	public void setType(TerminalType type) {
		this.type = type;
	}
	
	@Column(name = "IP")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFY_TIME")
	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "STATUS")
	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}
	
}
