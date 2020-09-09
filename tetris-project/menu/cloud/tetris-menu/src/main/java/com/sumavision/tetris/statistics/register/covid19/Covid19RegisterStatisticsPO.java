package com.sumavision.tetris.statistics.register.covid19;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 新冠肺炎出入登记数据<br/>
 * <b>作者:</b>吕德阳<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年2月12日 下午2:56:51
 */
@Entity
@Table(name = "TETRIS_COVID19_REGISTER_STATISTICS")
public class Covid19RegisterStatisticsPO extends AbstractBasePO{

	/** 这是一个属性的说明 */
	private static final long serialVersionUID = 1L;

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
	private Date timeForWork;
	
	/** 离开单位时间 */
	private Date closingTime;
	
	/** 体温 */
	private String temperature;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "AGE")
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Column(name = "COMPANY")
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Column(name = "DEPARTMENT")
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@Column(name = "PHONE")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "HOME_ADDRESS")
	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	@Column(name = "LIVE_ADDRESS")
	public String getLiveAddress() {
		return liveAddress;
	}

	public void setLiveAddress(String liveAddress) {
		this.liveAddress = liveAddress;
	}

	@Column(name = "WORK_ADDRESS")
	public String getWorkAddress() {
		return workAddress;
	}

	public void setWorkAddress(String workAddress) {
		this.workAddress = workAddress;
	}

	@Column(name = "WAY_OF_WORK")
	public String getWayOfWork() {
		return wayOfWork;
	}

	public void setWayOfWork(String wayOfWork) {
		this.wayOfWork = wayOfWork;
	}

	@Column(name = "BEEN_TO_WUHAN_AFTER_20200101")
	public Boolean getBeenToWuhanAfter20200101() {
		return beenToWuhanAfter20200101;
	}

	public void setBeenToWuhanAfter20200101(Boolean beenToWuhanAfter20200101) {
		this.beenToWuhanAfter20200101 = beenToWuhanAfter20200101;
	}

	@Column(name = "CONTACT_WITH_SUSPECTED_OR_CONFIRMED_PATIENTS_AFTER_20200101")
	public Boolean getContactWithSuspectedOrConfirmedPatientsAfter20200101() {
		return contactWithSuspectedOrConfirmedPatientsAfter20200101;
	}

	public void setContactWithSuspectedOrConfirmedPatientsAfter20200101(
			Boolean contactWithSuspectedOrConfirmedPatientsAfter20200101) {
		this.contactWithSuspectedOrConfirmedPatientsAfter20200101 = contactWithSuspectedOrConfirmedPatientsAfter20200101;
	}

	@Column(name = "STAY_IN_HUBEI_SINCE_20200101")
	public Boolean getStayInHubeiSince20200101() {
		return stayInHubeiSince20200101;
	}

	public void setStayInHubeiSince20200101(Boolean stayInHubeiSince20200101) {
		this.stayInHubeiSince20200101 = stayInHubeiSince20200101;
	}

	@Column(name = "COUGH_OR_FEVER")
	public Boolean getCoughOrFever() {
		return coughOrFever;
	}

	public void setCoughOrFever(Boolean coughOrFever) {
		this.coughOrFever = coughOrFever;
	}

	@Temporal(TemporalType.TIME)
	@Column(name = "TIME_FOR_WORK")
	public Date getTimeForWork() {
		return timeForWork;
	}

	public void setTimeForWork(Date timeForWork) {
		this.timeForWork = timeForWork;
	}

	@Temporal(TemporalType.TIME)
	@Column(name = "CLOSING_TIME")
	public Date getClosingTime() {
		return closingTime;
	}

	public void setClosingTime(Date closingTime) {
		this.closingTime = closingTime;
	}

	@Column(name = "TEMPERATURE")
	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	
}
