package com.sumavision.bvc.control.device.monitor.osd;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdDAO;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdPO;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdQuery;
import com.sumavision.bvc.device.monitor.osd.MonitorOsdService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/monitor/osd")
public class MonitorOsdController {

	@Autowired
	private MonitorOsdQuery monitorOsdQuery;
	
	@Autowired
	private MonitorOsdDAO monitorOsdDao;
	
	@Autowired
	private MonitorOsdService monitorOsdService;
	
	@Autowired
	private UserUtils userUtils;
	
	@RequestMapping(value = "/index")
	public ModelAndView index(String token){
		
		ModelAndView mv = new ModelAndView("web/bvc/monitor/osd/osd");
		mv.addObject("token", token);
		
		return mv;
	}
	
	/**
	 * 分页查询osd<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月7日 下午6:54:57
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return total long 总数据量
	 * @return rows List<MonitorOsdVO> rows osd列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/all")
	public Object loadAll(
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		Long total = monitorOsdDao.count();
		
		List<MonitorOsdPO> entities = monitorOsdQuery.findAll(currentPage, pageSize);
		
		List<MonitorOsdVO> rows = MonitorOsdVO.getConverter(MonitorOsdVO.class).convert(entities, MonitorOsdVO.class);
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", rows)
												   .getMap();
	}
	
	/**
	 * 分页查询用户创建的osd<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月6日 下午5:29:23
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return total int 总数据量
	 * @return rows List<MonitorOsdVO> osd列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		int total = monitorOsdDao.countByUserId(userId.toString());
		
		List<MonitorOsdPO> entities = monitorOsdQuery.findByUserId(userId, currentPage, pageSize);
		
		List<MonitorOsdVO> rows = MonitorOsdVO.getConverter(MonitorOsdVO.class).convert(entities, MonitorOsdVO.class);
		
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", rows)
												   .getMap();
	}
	
	/**
	 * 添加一个osd显示<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月7日 上午8:50:45
	 * @param String name osd名称
	 * @return MonitorOsdVO osd
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String name,
			HttpServletRequest request) throws Exception{
		if(name==null || "".equals(name)) throw new BaseException(StatusCode.FORBIDDEN, "名称不能为空！");
		UserVO user = userUtils.getUserFromSession(request);
		MonitorOsdPO osd = monitorOsdService.add(name, user.getId(), user.getName());
		return new MonitorOsdVO().set(osd);
	}
	
	/**
	 * 修改osd显示<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月7日 上午9:04:29
	 * @param @PathVariable id osd id
	 * @param String name osd名称
	 * @return MonitorOsdVO osd
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/{id}")
	public Object edit(
			@PathVariable Long id,
			String name,
			HttpServletRequest request) throws Exception{
		if(name==null || "".equals(name)) throw new BaseException(StatusCode.FORBIDDEN, "名称不能为空！");
		Long userId = userUtils.getUserIdFromSession(request);
		MonitorOsdPO osd = monitorOsdService.edit(id, name, userId);
		return new MonitorOsdVO().set(osd);
	}
	
	/**
	 * 删除osd<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月7日 上午8:57:26
	 * @param @PathVariable Long id osd id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/{id}")
	public Object remove(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		monitorOsdService.remove(id, userId);
		return null;
	}
	
}
