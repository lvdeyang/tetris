package com.sumavision.tetris.statistics.register.covid19;


import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/covid19/register/statistics")
public class Covid19RegisterStatisticsController {

	@Autowired
	private Covid19RegisterStatisticsService covid19RegisterStatisticsService;
	
	@Autowired
	private Covid19RegisterStatisticsQuery covid19RegisterStatisticsQuery;
	
	@Autowired
	private Covid19RegisterStatisticsDAO covid19RegisterStatisticsDao;
	
	@RequestMapping(value = "/table")
	public ModelAndView table(HttpServletRequest request) throws Exception{
		ModelAndView mv = new ModelAndView("mobile/COVID-19/table");
		return mv;
	}
	
	/**
	 * 分页查询数据<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月13日 下午4:23:43
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return long total 总数据量
	 * @return List<Covid19RegisterStatisticsVO> rows 数据列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		List<Covid19RegisterStatisticsVO> rows = covid19RegisterStatisticsQuery.findAll(currentPage, pageSize);
		long total = covid19RegisterStatisticsDao.count();
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", rows)
												   .getMap();
	}
	
	@RequestMapping(value = "/home")
	public ModelAndView home() throws Exception{
		ModelAndView mv = new ModelAndView("mobile/COVID-19/index");
		return mv;
	}
	
	/**
	 * 上班<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月12日 下午3:58:27
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
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/save")
	public Object save(
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
			String temperature,
			HttpServletRequest request) throws Exception{
		
		Covid19RegisterStatisticsPO entity = covid19RegisterStatisticsService.save(
				name, 
				age, 
				company, 
				department, 
				phone, 
				homeAddress, 
				liveAddress, 
				workAddress, 
				wayOfWork, 
				beenToWuhanAfter20200101, 
				contactWithSuspectedOrConfirmedPatientsAfter20200101, 
				stayInHubeiSince20200101, 
				coughOrFever, 
				new Date(),
				temperature);
		
		return new Covid19RegisterStatisticsVO().set(entity);
	}
	
	/**
	 * 下班<br/>
	 * <b>作者:</b>吕德阳<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年2月12日 下午3:58:59
	 * @param id
	 * @param closingTime
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/close")
	public Object close(
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
			String temperature,
			HttpServletRequest request) throws Exception{
		
		Covid19RegisterStatisticsPO entity = covid19RegisterStatisticsService.close(
				id, 
				name,
				age,
				company,
				department,
				phone,
				homeAddress,
				liveAddress,
				workAddress,
				wayOfWork,
				beenToWuhanAfter20200101,
				contactWithSuspectedOrConfirmedPatientsAfter20200101,
				stayInHubeiSince20200101,
				coughOrFever,
				new Date(),
				temperature);
		
		return new Covid19RegisterStatisticsVO().set(entity);
	}
	
}
