package com.sumavision.tetris.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
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
	
	/** 标签分隔符 */
	public static final String SEPARATOR_TAG = ",";
	
	/** 用户名 */
	private String username;
	
	/** 密码 */
	private String password;
	
	/** 用户昵称 */
	private String nickname;
	
	/** 用户号码 */
	private String userno;
	
	/** 用户头像 */
	private String icon;
	
	/** 用户状态 */
	//private UserStatus status;
	
	/** 用户登录的token */
	//private String token;
	
	/** 操作时间 */
	//private Date lastModifyTime;
	
	/** 电话号 */
	private String mobile;
	
	/** 邮箱 */
	private String mail;
	
	/** 标签 */
	private String tags;
	
	/** 是否是自动生成的 */
	private boolean autoGeneration;
	
	/** 用户分类 */
	private UserClassify classify;
	
	/** 用户级别 */
	private Integer level;
	
	/** 用户当前ip */
	//private String ip;
	
	/** 用户当前设备类型 */
	//private UserEquipType equipType;
	
	/** 异常登录次数 */
	private Integer errorLoginTimes;
	
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

	@Column(name = "USERNO")
	public String getUserno() {
		return userno;
	}

	public void setUserno(String userno) {
		this.userno = userno;
	}

	@Column(name = "ICON")
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	/*@Enumerated(value = EnumType.STRING)
	@Column(name = "STATUS")
	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}*/
	
	/*@Column(name = "TOKEN")
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}*/

	/*@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFY_TIME")
	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}*/

	@Column(name = "MOBILE")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "MAIL")
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	@Column(name = "tags")
	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	@Column(name = "AUTO_GENERATION")
	public boolean isAutoGeneration() {
		return autoGeneration;
	}

	public void setAutoGeneration(boolean autoGeneration) {
		this.autoGeneration = autoGeneration;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "CLASSIFY")
	public UserClassify getClassify() {
		return classify;
	}

	public void setClassify(UserClassify classify) {
		this.classify = classify;
	}

	/*@Column(name = "ip")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}*/

	/*@Enumerated(value = EnumType.STRING)
	@Column(name = "EQUIP_TYPE")
	public UserEquipType getEquipType() {
		return equipType;
	}

	public void setEquipType(UserEquipType equipType) {
		this.equipType = equipType;
	}*/
	
	@Column(name = "LEVEL")
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Column(name = "ERROR_LOGIN_TIMES")
	public Integer getErrorLoginTimes() {
		return errorLoginTimes;
	}

	public void setErrorLoginTimes(Integer errorLoginTimes) {
		this.errorLoginTimes = errorLoginTimes;
	}

	/**
	 * 生产csv表头<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月19日 上午9:26:53
	 * @return String csv表头
	 */
	public static String head(){
		return "用户账号,用户昵称,密码,所属分组编码,用户sip号码,用户类型,用户角色,手机号,邮箱,描述";
	}
	
	/**
	 * 生成csv一行数据<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月19日 上午9:27:24
	 * @param String roles #号分割
	 * @return String csv一行数据
	 */
	public String row(String roles){
		return new StringBufferWrapper().append(this.getUsername()).append(",")
									    .append(this.getNickname()).append(",")
									    .append(",")
									    .append(",")
									    .append(this.getUserno()).append(",")
									    .append(",")
									    .append(roles).append(",")
									    .append(this.getMobile()).append(",")
									    .append(this.getMail()).append(",")
									    .append(",")
									    .toString();
	}
	
}
