package com.sumavision.bvc.control.device.monitor.subtitle;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.monitor.subtitle.MonitorSubtitleDAO;
import com.sumavision.bvc.device.monitor.subtitle.MonitorSubtitleFont;
import com.sumavision.bvc.device.monitor.subtitle.MonitorSubtitlePO;
import com.sumavision.bvc.device.monitor.subtitle.MonitorSubtitleQuery;
import com.sumavision.bvc.device.monitor.subtitle.MonitorSubtitleService;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/monitor/subtitle")
public class MonitorSubtitleController {

	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private MonitorSubtitleQuery monitorSubtitleQuery;
	
	@Autowired
	private MonitorSubtitleDAO monitorSubtitleDao;
	
	@Autowired
	private MonitorSubtitleService monitorSubtitleService;
	
	@RequestMapping(value = "/index")
	public ModelAndView index(String token){
		
		ModelAndView mv = new ModelAndView("web/bvc/monitor/subtitle/subtitle");
		mv.addObject("token", token);
		
		return mv;
	}

	/**
	 * 查询字体<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月6日 下午2:03:31
	 * @return Set<String> 字体列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/fonts")
	public Object queryFonts(HttpServletRequest request) throws Exception{
		MonitorSubtitleFont[] values = MonitorSubtitleFont.values();
		Set<String> fonts = new HashSet<String>();
		for(MonitorSubtitleFont value:values){
			fonts.add(value.getName());
		}
		return fonts;
	}
	
	/**
	 * 分页查询字幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月7日 下午2:39:00
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return total int 总数据量
	 * @return rows List<MonitorSubtitleVO> 字幕列表 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/all")
	public Object loadAll(
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		Long total = monitorSubtitleDao.count();
		
		List<MonitorSubtitlePO> entities = monitorSubtitleQuery.findAll(currentPage, pageSize);
		
		List<MonitorSubtitleVO> rows = MonitorSubtitleVO.getConverter(MonitorSubtitleVO.class).convert(entities, MonitorSubtitleVO.class);
		
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", rows)
												   .getMap();
	}
	
	/**
	 * 分页查询用户字幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月6日 上午11:12:45
	 * @param int currentPage 当前页码
	 * @param int pageSize 每页数据量
	 * @return total int 总数据量
	 * @return rows List<MonitorSubtitleVO> 字幕列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			int currentPage,
			int pageSize,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		int total = monitorSubtitleDao.countByUserId(userId.toString());
		
		List<MonitorSubtitlePO> entities = monitorSubtitleQuery.findByUserId(userId, currentPage, pageSize);
		
		List<MonitorSubtitleVO> rows = MonitorSubtitleVO.getConverter(MonitorSubtitleVO.class).convert(entities, MonitorSubtitleVO.class);
		
		return new HashMapWrapper<String, Object>().put("total", total)
												   .put("rows", rows)
												   .getMap();
	}
	
	/**
	 * 添加字幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月6日 下午1:14:59
	 * @param String name 字幕名称
	 * @param String content 文本内容
	 * @param String font 字体
	 * @param Integer height 字体大小
	 * @param String color颜色
	 * @return MonitorSubtitleVO 字幕
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String name,
			String content,
			String font,
			Integer height,
			String color,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		MonitorSubtitlePO subtitle = monitorSubtitleService.add(name, content, font, height, color, user.getId(), user.getName());
		
		return new MonitorSubtitleVO().set(subtitle);
	}
	
	/**
	 * 删除字幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月6日 下午1:59:04
	 * @param @PathVariable id 字幕id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/{id}")
	public Object remove(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		monitorSubtitleService.remove(id, userId);
		
		return null;
	}
	
	/**
	 * 修改字幕<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月6日 下午1:56:23
	 * @param @PathVariable Long id 字幕id
	 * @param String name 字幕名称
	 * @param String content 字幕内容
	 * @param String font 字体
	 * @param String height 字体大小
	 * @param String color 字体颜色
	 * @return MonitorSubtitleVO 字幕
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/{id}")
	public Object edit(
			@PathVariable Long id,
			String name,
			String content,
			String font,
			Integer height,
			String color,
			HttpServletRequest request) throws Exception{
		
		Long userId = userUtils.getUserIdFromSession(request);
		
		MonitorSubtitlePO subtitle = monitorSubtitleService.edit(id, name, content, font, height, color, userId);
		
		return new MonitorSubtitleVO().set(subtitle);
	}
	
}
