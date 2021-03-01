package com.sumavision.tetris.user;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.commons.context.SpringContext;
import com.sumavision.tetris.commons.util.binary.ByteUtil;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;
import com.sumavision.tetris.organization.CompanyPO;

/**
 * 登录用户信息<br/>
 * <p>特殊注明：用uuid字段存储userId</p>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月19日 下午4:51:51
 */
public class UserVO extends AbstractBaseVO<UserVO, UserPO>{

	/** 用户名 */
	private String username;
	
	/** 用户昵称 */
	private String nickname;
	
	/** 用户号码 */
	private String userno;
	
	/** 头像 */
	private String icon;
	
	/** 用户状态 */
	private String status;
	
	/** 用户登录token */
	private String token;
	
	/** 手机号 */
	private String mobile;
	
	/** 邮箱 */
	private String mail;
	
	/** 用户级别 */
	private Integer level;
	
	/** 是否是自动生成的 */
	private boolean autoGeneration;
	
	/** 用户分类 */
	private String classify;
	
	/** 用户当前ip */
	private String ip;
	
	/** 用户设备类型 */
	private String equipType;
	
	/** 发送给当前用户的信息数目 */
	private Integer numbersOfMessage;
	
	/** 用户隶属组织id */
	private String groupId;
	
	/** 是否是组织创建者 */
	private Boolean isGroupCreator;
	
	/** 用户隶属组织名称 */
	private String groupName;
	
	/** 用户隶属组织首页链接 */
	private String groupHomeLink;
	
	/** 皮肤链接 */
	private String themeUrl;
	
	/** logo */
	private String logo;
	
	/** logo样式 */
	private String logoStyle;
	
	/** logo缩写 */
	private String logoShortName;
	
	/** 平台全名 */
	private String platformFullName;
	
	/** 平台名称缩写 */
	private String platformShortName;
	
	/** 素材库文件夹id */
	private Long rootFolderId;
	
	/** 素材库文件夹名称 */
	private String rootFolderName;
	
	/** 业务角色id,以“,”分割 */
	private String businessRoles;
	
	/** 用户标签,以“,”分割 */
	private List<String> tags;
	
	/** 异常登录次数 */
	private int errorLoginTimes;
	
	/** 终端类型 */
	private TerminalType terminalType;
	
	/**备注*/
	private String remark;
	
	/**上次登陆时间*/
	private String lastLoginTime;
	
	private String loginIp;
	
	/**token多个，仅25*/
	private String tokenIds;
	
	/**企业角色名*/
	private String companyRoleName;
	
	/**企业角色Id*/
	private Long companyRoleId;
	
	public String getCompanyRoleName() {
		return companyRoleName;
	}

	public UserVO setCompanyRoleName(String companyRoleName) {
		this.companyRoleName = companyRoleName;
		return this;
	}

	public Long getCompanyRoleId() {
		return companyRoleId;
	}

	public UserVO setCompanyRoleId(Long companyRoleId) {
		this.companyRoleId = companyRoleId;
		return this;
	}
	
	public String getTokenIds() {
		return tokenIds;
	}

	public UserVO setTokenIds(String tokenIds) {
		this.tokenIds = tokenIds;
		return this;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public UserVO setLoginIp(String loginIp) {
		this.loginIp = loginIp;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public UserVO setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public UserVO setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public UserVO setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getNickname() {
		return nickname;
	}

	public UserVO setNickname(String nickname) {
		this.nickname = nickname;
		return this;
	}

	public String getUserno() {
		return userno;
	}

	public UserVO setUserno(String userno) {
		this.userno = userno;
		return this;
	}

	public String getIcon() {
		return icon;
	}

	public UserVO setIcon(String icon) {
		this.icon = icon;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public UserVO setStatus(String status) {
		this.status = status;
		return this;
	}
	
	public String getToken() {
		return token;
	}

	public UserVO setToken(String token) {
		this.token = token;
		return this;
	}

	public String getMobile() {
		return mobile;
	}

	public UserVO setMobile(String mobile) {
		this.mobile = mobile;
		return this;
	}

	public String getMail() {
		return mail;
	}

	public UserVO setMail(String mail) {
		this.mail = mail;
		return this;
	}

	public Integer getLevel() {
		return level;
	}

	public UserVO setLevel(Integer level) {
		this.level = level;
		return this;
	}

	public boolean isAutoGeneration() {
		return autoGeneration;
	}

	public UserVO setAutoGeneration(boolean autoGeneration) {
		this.autoGeneration = autoGeneration;
		return this;
	}

	public String getClassify() {
		return classify;
	}

	public UserVO setClassify(String classify) {
		this.classify = classify;
		return this;
	}

	public String getIp() {
		return ip;
	}

	public UserVO setIp(String ip) {
		this.ip = ip;
		return this;
	}

	public String getEquipType() {
		return equipType;
	}

	public UserVO setEquipType(String equipType) {
		this.equipType = equipType;
		return this;
	}

	public Integer getNumbersOfMessage() {
		return numbersOfMessage;
	}

	public UserVO setNumbersOfMessage(Integer numbersOfMessage) {
		this.numbersOfMessage = numbersOfMessage;
		return this;
	}

	public String getGroupId() {
		return groupId;
	}

	public UserVO setGroupId(String groupId) {
		this.groupId = groupId;
		return this;
	}

	public Boolean getIsGroupCreator() {
		return isGroupCreator;
	}

	public UserVO setIsGroupCreator(Boolean isGroupCreator) {
		this.isGroupCreator = isGroupCreator;
		return this;
	}

	public String getGroupName() {
		return groupName;
	}

	public UserVO setGroupName(String groupName) {
		this.groupName = groupName;
		return this;
	}
	
	public String getGroupHomeLink() {
		return groupHomeLink;
	}

	public UserVO setGroupHomeLink(String groupHomeLink) {
		this.groupHomeLink = groupHomeLink;
		return this;
	}

	public String getThemeUrl() {
		return themeUrl;
	}

	public UserVO setThemeUrl(String themeUrl) {
		this.themeUrl = themeUrl;
		return this;
	}

	public String getLogo() {
		return logo;
	}

	public UserVO setLogo(String logo) {
		this.logo = logo;
		return this;
	}

	public String getLogoStyle() {
		return logoStyle;
	}

	public UserVO setLogoStyle(String logoStyle) {
		this.logoStyle = logoStyle;
		return this;
	}

	public String getLogoShortName() {
		return logoShortName;
	}

	public UserVO setLogoShortName(String logoShortName) {
		this.logoShortName = logoShortName;
		return this;
	}

	public String getPlatformFullName() {
		return platformFullName;
	}

	public UserVO setPlatformFullName(String platformFullName) {
		this.platformFullName = platformFullName;
		return this;
	}

	public String getPlatformShortName() {
		return platformShortName;
	}

	public UserVO setPlatformShortName(String platformShortName) {
		this.platformShortName = platformShortName;
		return this;
	}

	public Long getRootFolderId() {
		return rootFolderId;
	}

	public UserVO setRootFolderId(Long rootFolderId) {
		this.rootFolderId = rootFolderId;
		return this;
	}

	public String getRootFolderName() {
		return rootFolderName;
	}

	public UserVO setRootFolderName(String rootFolderName) {
		this.rootFolderName = rootFolderName;
		return this;
	}

	public String getBusinessRoles() {
		return businessRoles;
	}

	public UserVO setBusinessRoles(String businessRoles) {
		this.businessRoles = businessRoles;
		return this;
	}

	public List<String> getTags() {
		return tags;
	}

	public UserVO setTags(List<String> tags) {
		this.tags = tags;
		return this;
	}

	public int getErrorLoginTimes() {
		return errorLoginTimes;
	}

	public UserVO setErrorLoginTimes(int errorLoginTimes) {
		this.errorLoginTimes = errorLoginTimes;
		return this;
	}
	
	public TerminalType getTerminalType() {
		return terminalType;
	}

	public UserVO setTerminalType(TerminalType terminalType) {
		this.terminalType = terminalType;
		return this;
	}

	@Override
	public UserVO set(UserPO entity){
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setUsername(entity.getUsername())
			.setNickname(entity.getNickname())
			.setMobile(entity.getMobile())
			.setMail(entity.getMail())
			.setLevel(entity.getLevel())
			.setIcon(entity.getIcon())
			.setUserno(entity.getUserno())
			.setClassify(entity.getClassify().toString())
			//.setStatus(entity.getStatus()==null?"":entity.getStatus().getName())
			//.setToken(entity.getToken())
			.setIp(entity.getIp())
			//.setEquipType(entity.getEquipType() != null ? entity.getEquipType().toString() : null)
			.setRemark(entity.getRemark())
			.setLastLoginTime(entity.getLastLoginTime() == null?"":DateUtil.format(entity.getLastLoginTime(),DateUtil.dateTimePattern))
			.setLoginIp(entity.getLoginIp())
			.setAutoGeneration(entity.isAutoGeneration());
		if(entity.getTags() != null && !entity.getTags().isEmpty()) this.setTags(Arrays.asList(entity.getTags().split(UserPO.SEPARATOR_TAG))); else this.setTags(new ArrayList<String>());
		this.setErrorLoginTimes(entity.getErrorLoginTimes()==null?0:entity.getErrorLoginTimes());
		return this;
	}
	
	public UserVO setCompanyInfo(CompanyPO entity) throws Exception{
		if(entity != null){
			this.setGroupId(entity.getId().toString())
				.setIsGroupCreator(entity.getUserId().equals(this.getId().toString())?true:false)
				.setGroupName(entity.getName())
				.setGroupHomeLink(entity.getHomeLink()==null?"":entity.getHomeLink())
				.setLogo(entity.getLogo())
				.setLogoStyle(entity.getLogoStyle())
				.setLogoShortName(entity.getLogoShortName())
				.setPlatformFullName(entity.getPlatformFullName())
				.setPlatformShortName(entity.getPlatformShortName());
		}
		
		if(this.getLogo() == null) this.setLogo(CompanyPO.DEFAULT_LOGO);
		
		Path path = SpringContext.getBean(Path.class);
		String logoPath = new StringBufferWrapper().append(path.webappPath()).append(this.getLogo().substring(1, this.getLogo().length())).toString();
		File logoFile = new File(logoPath);
		FileInputStream logoInputStream = new FileInputStream(logoFile);
		byte[] logoBytes = ByteUtil.inputStreamToBytes(logoInputStream);
		String logoBase64 = Base64.getEncoder().encodeToString(logoBytes);
		this.setLogo(new StringBufferWrapper().append("data:image/png;base64,").append(logoBase64).toString());
		//UserServerPropsQuery userServerPropsQuery = SpringContext.getBean(UserServerPropsQuery.class);
		//ServerProps props = userServerPropsQuery.queryProps();
		//TODO serverIp.properties
		//props.getIp()
		//this.setLogo(new StringBufferWrapper().append("http://").append(props.getIpFromProperties()).append(":").append(props.getPort()).append(this.getLogo()).toString());
		if(this.getLogoStyle() == null) this.setLogoStyle(CompanyPO.DEFAULT_LOGOSTYLE);
		if(this.getLogoShortName() == null) this.setLogoShortName(CompanyPO.DEFAULT_LOGOSHORTNAME);
		if(this.getPlatformFullName() == null) this.setPlatformFullName(CompanyPO.DEFAULT_PLATFORMFULLNAME);
		if(this.getPlatformShortName() == null) this.setPlatformShortName(CompanyPO.DEFAULT_PLATFORMSHORTNAME);
		
		return this;
	}
	
	public static void main(String[] args) throws Exception{
		/*File logoFile = new File("D:\\logo.bd8a658.png");
		FileInputStream logoInputStream = new FileInputStream(logoFile);
		byte[] logoBytes = ByteUtil.inputStreamToBytes(logoInputStream);
		String logoBase64 = Base64.getEncoder().encodeToString(logoBytes);
		System.out.println(new StringBufferWrapper().append("data:image/png;base64,").append(logoBase64).toString());*/
		//System.out.println("/log".substring(1,"/log".length()));
	}	
}