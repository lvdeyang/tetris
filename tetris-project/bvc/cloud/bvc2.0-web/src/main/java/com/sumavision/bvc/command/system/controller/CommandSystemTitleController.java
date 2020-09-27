package com.sumavision.bvc.command.system.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.bvc.command.system.dao.CommandSystemTitleDAO;
import com.sumavision.bvc.command.system.po.CommandSystemTitlePO;
import com.sumavision.bvc.command.system.vo.CommandSystemTitleVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/command/system/title")
public class CommandSystemTitleController {
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private CommandSystemTitleDAO commandSystemTitleDao;
	
	/**
	 * 添加任务标题<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月24日 下午3:30:57
	 * @param beginTime
	 * @param titleName
	 * @return
	 * @throws Exception 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/add")
	public Object add(
			String beginTime,
			String titleName,
			Boolean isCurrentTask,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		CommandSystemTitlePO title=new CommandSystemTitlePO();
		
		title.setBeginTime(DateUtil.parse(beginTime, DateUtil.dateTimePattern));
		title.setId(user.getId());
		title.setCurrentTask(isCurrentTask);
		title.setTitleName(titleName);
		commandSystemTitleDao.save(title);
		
		if(Boolean.TRUE.equals(isCurrentTask)){
			List<CommandSystemTitlePO> titles=commandSystemTitleDao.findAll();
			if(titles!=null&&titles.size()>0){
				for(CommandSystemTitlePO oldTitle:titles){
					oldTitle.setCurrentTask(false);
				}
				commandSystemTitleDao.save(titles);
			}
			
		}
		
		return null;
	}
	
	/**
	 * 删除任务标题<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月24日 下午3:42:13
	 * @param id 任务标题
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/delete")
	public Object delete(Long id){
		
		commandSystemTitleDao.delete(id);
		
		return null;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/edit")
	public Object edit(
			Long id,
			Date beginTime,
			String titleName,
			Boolean isCurrentTask){

		CommandSystemTitlePO title=commandSystemTitleDao.findOne(id);
		title.setBeginTime(beginTime);
		title.setTitleName(titleName);
		title.setCurrentTask(isCurrentTask);
		commandSystemTitleDao.save(title);
		
		if(Boolean.TRUE.equals(isCurrentTask)){
			List<CommandSystemTitlePO> titles=commandSystemTitleDao.findAll();
			if(titles!=null&&titles.size()>0){
				for(CommandSystemTitlePO oldTitle:titles){
					oldTitle.setCurrentTask(false);
				}
				commandSystemTitleDao.save(titles);
			}
		}
		
		return null;
	}
	
	/**
	 * 分页查询任务标题<br/>
	 * <b>作者:</b>lx<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月24日 下午3:50:41
	 * @return
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/query")
	public Object query(
			Integer pageSize,
			Integer currentPage){
		Pageable page=new PageRequest(currentPage-1, pageSize);
		
		Page<CommandSystemTitlePO> pageTitles=commandSystemTitleDao.findAll(page);
		
		if(pageTitles.getContent()==null){
			return null;
		}
		
		List<CommandSystemTitleVO> titleVos=pageTitles.getContent().stream().map(title->{
			CommandSystemTitleVO titleVo=new CommandSystemTitleVO();
			titleVo.setId(title.getId())
					.setTitleName(title.getTitleName())
					.setBeginTime(DateUtil.format(title.getBeginTime()))
					.setCurrentTask(title.getCurrentTask())
					.setUserId(title.getUserId());
			return titleVo;
		}).collect(Collectors.toList());
		
		return new HashMapWrapper<String, Object>().put("total", pageTitles.getTotalElements())
				   .put("rows", titleVos)
				   .getMap();
	}
	
	/**
	 * 查询当前任务标题<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月27日 上午11:33:27
	 * @return 如果查不到，返回null
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value="/query/current")
	public Object queryCurrent(){
		
		CommandSystemTitlePO title = commandSystemTitleDao.findByCurrentTaskEquals(true);
		
		if(title == null) return null;
		
		CommandSystemTitleVO titleVo = new CommandSystemTitleVO()
				.setId(title.getId())
				.setTitleName(title.getTitleName())
				.setBeginTime(DateUtil.format(title.getBeginTime()))
				.setCurrentTask(title.getCurrentTask())
				.setUserId(title.getUserId());
		
		return titleVo;
	}
	
}
