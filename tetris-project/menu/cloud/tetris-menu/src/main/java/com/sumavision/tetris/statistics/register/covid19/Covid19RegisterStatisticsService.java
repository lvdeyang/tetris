package com.sumavision.tetris.statistics.register.covid19;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Exception.class)
@Service
public class Covid19RegisterStatisticsService {

	@Autowired
	private Covid19RegisterStatisticsDAO covid19RegisterStatisticsDao;
	
	/**
	 * 上班<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月12日 下午3:56:52
	 * @param name
	 * @param age
	 * @param company
	 * @param department
	 * @param phone
	 * @param homeAddress
	 * @param liveAddress
	 * @param workAddress
	 * @param wayOfWork
	 * @param beenToWuhanAfter20200101
	 * @param contactWithSuspectedOrConfirmedPatientsAfter20200101
	 * @param stayInHubeiSince20200101
	 * @param coughOrFever
	 * @param timeForWork
	 * @return
	 * @throws Exception
	 */
	public Covid19RegisterStatisticsPO save(
			String name,
			Integer age,
			String company,
			String department,
			String phone,
			String homeAddress,
			String liveAddress,
			String workAddress,
			String wayOfWork,
			Boolean beenToWuhanAfter20200101,
			Boolean contactWithSuspectedOrConfirmedPatientsAfter20200101,
			Boolean stayInHubeiSince20200101,
			Boolean coughOrFever,
			Date timeForWork,
			String temperature) throws Exception{
		
		Covid19RegisterStatisticsPO entity = new Covid19RegisterStatisticsPO();
		entity.setUpdateTime(new Date());
		entity.setName(name);
		entity.setAge(age);
		entity.setCompany(company);
		entity.setDepartment(department);
		entity.setPhone(phone);
		entity.setHomeAddress(homeAddress);
		entity.setLiveAddress(liveAddress);
		entity.setWorkAddress(workAddress);
		entity.setWayOfWork(wayOfWork);
		entity.setBeenToWuhanAfter20200101(beenToWuhanAfter20200101);
		entity.setContactWithSuspectedOrConfirmedPatientsAfter20200101(contactWithSuspectedOrConfirmedPatientsAfter20200101);
		entity.setStayInHubeiSince20200101(stayInHubeiSince20200101);
		entity.setCoughOrFever(coughOrFever);
		entity.setTimeForWork(timeForWork);
		entity.setTemperature(temperature);
		covid19RegisterStatisticsDao.save(entity);
		return entity;
	}
	
	/**
	 * 下班<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月12日 下午3:57:02
	 * @param id
	 * @param closingTime
	 * @return
	 * @throws Exception
	 */
	public Covid19RegisterStatisticsPO close(
			Long id,
			String name,
			Integer age,
			String company,
			String department,
			String phone,
			String homeAddress,
			String liveAddress,
			String workAddress,
			String wayOfWork,
			Boolean beenToWuhanAfter20200101,
			Boolean contactWithSuspectedOrConfirmedPatientsAfter20200101,
			Boolean stayInHubeiSince20200101,
			Boolean coughOrFever,
			Date closingTime,
			String temperature) throws Exception{
		Covid19RegisterStatisticsPO entity = covid19RegisterStatisticsDao.findById(id);
		entity.setName(name);
		entity.setAge(age);
		entity.setCompany(company);
		entity.setDepartment(department);
		entity.setPhone(phone);
		entity.setHomeAddress(homeAddress);
		entity.setLiveAddress(liveAddress);
		entity.setWorkAddress(workAddress);
		entity.setWayOfWork(wayOfWork);
		entity.setBeenToWuhanAfter20200101(beenToWuhanAfter20200101);
		entity.setContactWithSuspectedOrConfirmedPatientsAfter20200101(contactWithSuspectedOrConfirmedPatientsAfter20200101);
		entity.setStayInHubeiSince20200101(stayInHubeiSince20200101);
		entity.setCoughOrFever(coughOrFever);
		entity.setClosingTime(closingTime);
		entity.setTemperature(temperature);
		covid19RegisterStatisticsDao.save(entity);
		return entity;
	} 
	
}
