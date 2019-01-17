package com.sumavision.tetris.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 用户数据表<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月15日 下午4:40:52
 */
@Entity
@Table(name = "TETRIS_USER")
public class UserPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 用户名 */
	private String username;
	
	/** 密码 */
	private String password;
	
	/** 用户昵称 */
	private String nickname;
	
	/** 是否是自动生成的 */
	private boolean autoGeneration;

	@Column(name = "USERNAME")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "PASSWORD")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "NICKNAME")
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Column(name = "AUTO_GENERATION")
	public boolean isAutoGeneration() {
		return autoGeneration;
	}

	public void setAutoGeneration(boolean autoGeneration) {
		this.autoGeneration = autoGeneration;
	}
	
}
