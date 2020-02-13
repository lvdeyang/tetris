package com.sumavision.tetris.statistics.register.covid19;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class Covid19RegisterStatisticsVO extends AbstractBaseVO<Covid19RegisterStatisticsVO, Covid19RegisterStatisticsPO>{
	
	/** 姓名 */
	private String name;
	
	/** 年龄 */
	private Integer age;
	
	/** 单位 */
	private String company;
	
	/** 部门 */
	private String department;
	
	/** 电话号码 */
	private String phone;
	
	/** 家庭住址 （详细到房间号） */
	private String homeAddress;
	
	/** 现居地址（详细到房间号） */
	private String liveAddress;
	
	/** 工作地址（详细到房间号） */
	private String workAddress;
	
	/** 前往单位出行方式 */
	private String wayOfWork;
	
	/** 2020年1月1后有无去过武汉 */
	private Boolean beenToWuhanAfter20200101;
	
	/** 2020年1月1后有无接触疑似确诊病人 */
	private Boolean contactWithSuspectedOrConfirmedPatientsAfter20200101;
	
	/** 自2020年1月1日起，在湖北省停留（包括旅游、出差、转车、转机、经停当地有乘客上车或登机等情况） */
	private Boolean stayInHubeiSince20200101;
	
	/** 近期有无咳嗽发烧症状 */
	private Boolean coughOrFever;
	
	/** 进入单位时间 */
	private String timeForWork;
	
	/** 离开单位时间 */
	private String closingTime;
	
	/** 体温 */
	private String temperature;

	public String getName() {
		return name;
	}

	public Covid19RegisterStatisticsVO setName(String name) {
		this.name = name;
		return this;
	}

	public Integer getAge() {
		return age;
	}

	public Covid19RegisterStatisticsVO setAge(Integer age) {
		this.age = age;
		return this;
	}

	public String getCompany() {
		return company;
	}

	public Covid19RegisterStatisticsVO setCompany(String company) {
		this.company = company;
		return this;
	}

	public String getDepartment() {
		return department;
	}

	public Covid19RegisterStatisticsVO setDepartment(String department) {
		this.department = department;
		return this;
	}

	public String getPhone() {
		return phone;
	}

	public Covid19RegisterStatisticsVO setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public Covid19RegisterStatisticsVO setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
		return this;
	}

	public String getLiveAddress() {
		return liveAddress;
	}

	public Covid19RegisterStatisticsVO setLiveAddress(String liveAddress) {
		this.liveAddress = liveAddress;
		return this;
	}

	public String getWorkAddress() {
		return workAddress;
	}

	public Covid19RegisterStatisticsVO setWorkAddress(String workAddress) {
		this.workAddress = workAddress;
		return this;
	}

	public String getWayOfWork() {
		return wayOfWork;
	}

	public Covid19RegisterStatisticsVO setWayOfWork(String wayOfWork) {
		this.wayOfWork = wayOfWork;
		return this;
	}

	public Boolean getBeenToWuhanAfter20200101() {
		return beenToWuhanAfter20200101;
	}

	public Covid19RegisterStatisticsVO setBeenToWuhanAfter20200101(Boolean beenToWuhanAfter20200101) {
		this.beenToWuhanAfter20200101 = beenToWuhanAfter20200101;
		return this;
	}

	public Boolean getContactWithSuspectedOrConfirmedPatientsAfter20200101() {
		return contactWithSuspectedOrConfirmedPatientsAfter20200101;
	}

	public Covid19RegisterStatisticsVO setContactWithSuspectedOrConfirmedPatientsAfter20200101(
			Boolean contactWithSuspectedOrConfirmedPatientsAfter20200101) {
		this.contactWithSuspectedOrConfirmedPatientsAfter20200101 = contactWithSuspectedOrConfirmedPatientsAfter20200101;
		return this;
	}

	public Boolean getStayInHubeiSince20200101() {
		return stayInHubeiSince20200101;
	}

	public Covid19RegisterStatisticsVO setStayInHubeiSince20200101(Boolean stayInHubeiSince20200101) {
		this.stayInHubeiSince20200101 = stayInHubeiSince20200101;
		return this;
	}

	public Boolean getCoughOrFever() {
		return coughOrFever;
	}

	public Covid19RegisterStatisticsVO setCoughOrFever(Boolean coughOrFever) {
		this.coughOrFever = coughOrFever;
		return this;
	}

	public String getTimeForWork() {
		return timeForWork;
	}

	public Covid19RegisterStatisticsVO setTimeForWork(String timeForWork) {
		this.timeForWork = timeForWork;
		return this;
	}

	public String getClosingTime() {
		return closingTime;
	}

	public Covid19RegisterStatisticsVO setClosingTime(String closingTime) {
		this.closingTime = closingTime;
		return this;
	}
	
	public String getTemperature() {
		return temperature;
	}

	public Covid19RegisterStatisticsVO setTemperature(String temperature) {
		this.temperature = temperature;
		return this;
	}

	@Override
	public Covid19RegisterStatisticsVO set(Covid19RegisterStatisticsPO entity) throws Exception {
		this.setId(entity.getId())
			.setUpdateTime(entity.getUpdateTime())
			.setName(entity.getName())
			.setAge(entity.getAge())
			.setCompany(entity.getCompany())
			.setDepartment(entity.getDepartment())
			.setPhone(entity.getPhone())
			.setHomeAddress(entity.getHomeAddress())
			.setLiveAddress(entity.getLiveAddress())
			.setWorkAddress(entity.getWorkAddress())
			.setWayOfWork(entity.getWayOfWork())
			.setBeenToWuhanAfter20200101(entity.getBeenToWuhanAfter20200101())
			.setContactWithSuspectedOrConfirmedPatientsAfter20200101(entity.getContactWithSuspectedOrConfirmedPatientsAfter20200101())
			.setStayInHubeiSince20200101(entity.getStayInHubeiSince20200101())
			.setCoughOrFever(entity.getCoughOrFever())
			.setTimeForWork(entity.getTimeForWork()==null?"":DateUtil.format(entity.getTimeForWork(), DateUtil.defaultTimePattern))
			.setClosingTime(entity.getClosingTime()==null?"":DateUtil.format(entity.getClosingTime(), DateUtil.defaultTimePattern))
			.setTemperature(entity.getTemperature());
		return this;
	}
	
}
