package com.sumavision.tetris.organization;

public class CompanyVO{

	private Long id;
	
	private String uuid;
	
	private String updateTime;
	
	/** 公司名称 */
	private String name;
	
	/** 系统角色id */
	private Long systemRoleId;
	
	/** 系统角色名称 */
	private String systemRoleName;
	
	/** 公司首页地址 */
	private String homeLink;
	
	/** 皮肤id */
	private Long themeId;
	
	/** 皮肤名称 */
	private String themeName;
	
	/** 皮肤url */
	private String themeUrl;
	
	/** logo url */
	private String logo;
	
	/** logo样式 */
	private String logoStyle;
	
	/** logo缩略名称 */
	private String logoShortName;
	
	/** 平台全称 */
	private String platformFullName;
	
	/** 平台缩略名称 */
	private String platformShortName;
	
	public Long getId() {
		return id;
	}

	public CompanyVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public CompanyVO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public CompanyVO setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public String getName() {
		return name;
	}

	public CompanyVO setName(String name) {
		this.name = name;
		return this;
	}
	
	public Long getSystemRoleId() {
		return systemRoleId;
	}

	public CompanyVO setSystemRoleId(Long systemRoleId) {
		this.systemRoleId = systemRoleId;
		return this;
	}

	public String getSystemRoleName() {
		return systemRoleName;
	}

	public CompanyVO setSystemRoleName(String systemRoleName) {
		this.systemRoleName = systemRoleName;
		return this;
	}

	public String getHomeLink() {
		return homeLink;
	}

	public CompanyVO setHomeLink(String homeLink) {
		this.homeLink = homeLink;
		return this;
	}

	public Long getThemeId() {
		return themeId;
	}

	public CompanyVO setThemeId(Long themeId) {
		this.themeId = themeId;
		return this;
	}

	public String getThemeName() {
		return themeName;
	}

	public CompanyVO setThemeName(String themeName) {
		this.themeName = themeName;
		return this;
	}

	public String getThemeUrl() {
		return themeUrl;
	}

	public CompanyVO setThemeUrl(String themeUrl) {
		this.themeUrl = themeUrl;
		return this;
	}

	public String getLogo() {
		return logo;
	}

	public CompanyVO setLogo(String logo) {
		this.logo = logo;
		return this;
	}

	public String getLogoStyle() {
		return logoStyle;
	}

	public CompanyVO setLogoStyle(String logoStyle) {
		this.logoStyle = logoStyle;
		return this;
	}

	public String getLogoShortName() {
		return logoShortName;
	}

	public CompanyVO setLogoShortName(String logoShortName) {
		this.logoShortName = logoShortName;
		return this;
	}

	public String getPlatformFullName() {
		return platformFullName;
	}

	public CompanyVO setPlatformFullName(String platformFullName) {
		this.platformFullName = platformFullName;
		return this;
	}

	public String getPlatformShortName() {
		return platformShortName;
	}

	public CompanyVO setPlatformShortName(String platformShortName) {
		this.platformShortName = platformShortName;
		return this;
	}

}
