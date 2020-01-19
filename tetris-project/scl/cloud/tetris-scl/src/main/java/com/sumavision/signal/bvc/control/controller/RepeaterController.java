package com.sumavision.signal.bvc.control.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suma.venus.message.util.RegisterStatus;
import com.sumavision.signal.bvc.entity.dao.InternetAccessDAO;
import com.sumavision.signal.bvc.entity.dao.RepeaterDAO;
import com.sumavision.signal.bvc.entity.enumeration.InternetAccessType;
import com.sumavision.signal.bvc.entity.enumeration.RepeaterType;
import com.sumavision.signal.bvc.entity.po.InternetAccessPO;
import com.sumavision.signal.bvc.entity.po.RepeaterPO;
import com.sumavision.signal.bvc.entity.vo.InternetAccessVO;
import com.sumavision.signal.bvc.entity.vo.RepeaterVO;
import com.sumavision.signal.bvc.exception.InternetAccessAddressAlreadyExistException;
import com.sumavision.signal.bvc.exception.RepeaterIpAlreadyExistException;
import com.sumavision.signal.bvc.exception.RepeaterNameAlreadyExistException;
import com.sumavision.signal.bvc.service.RepeaterService;
import com.sumavision.signal.bvc.service.TaskExecuteService;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/signal/control/repeater")
public class RepeaterController {
	
	@Autowired
	private RepeaterDAO repeaterDao;
	
	@Autowired
	private InternetAccessDAO internetAccessDao;
	
	@Autowired
	private RepeaterService repeaterService;
	
	@Autowired
	private TaskExecuteService taskExecuteService;

	/**
	 * 查询转发器类型<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月20日 下午7:55:42
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/type")
	public Object queryType(HttpServletRequest request) throws Exception{
		
		//查询转发器类型
		Set<String> types = new HashSet<String>(); 
		RepeaterType[] values = RepeaterType.values();
		for(RepeaterType value: values){
			types.add(value.getName());
		}
		
		//查询主转发器
		List<RepeaterPO> repeaters = repeaterDao.findByType(RepeaterType.MAIN);
		List<RepeaterVO> repeaterVOs = RepeaterVO.getConverter(RepeaterVO.class).convert(repeaters, RepeaterVO.class);
		
		return new HashMapWrapper<String, Object>().put("type", types)
												   .put("repeaters", repeaterVOs)
			   									   .getMap();
	}
	
	/**
	 * 查询转发器列表<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 上午9:49:56
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/list")
	public Object queryRepeaters(HttpServletRequest request) throws Exception{
		
		List<RepeaterPO> repeaters = repeaterDao.findAll();
		
		List<RepeaterVO> repeaterVOs = RepeaterVO.getConverter(RepeaterVO.class).convert(repeaters, RepeaterVO.class);
		
		return repeaterVOs;
	}
	
	/**
	 * 添加流转发器<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 上午10:22:55
	 * @param String name 名称
	 * @param String ip 集群ip
	 * @param String address 流转发器ip
	 * @param String type 类型
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object addRepeater(
			String name,
			String ip,
			String address,
			String type,
			String main,
			HttpServletRequest request) throws Exception{
		
		//校验
		RepeaterPO repeater1 = repeaterDao.findByName(name);
		if(repeater1 != null){
			throw new RepeaterNameAlreadyExistException(name);
		}
		
		RepeaterPO repeater2 = repeaterDao.findByAddress(address);
		if(repeater2 != null){
			throw new RepeaterIpAlreadyExistException(address);
		}
		
		RepeaterPO repeater = new RepeaterPO();
		repeater.setName(name);
		repeater.setIp(ip);
		repeater.setAddress(address);
		repeater.setType(RepeaterType.fromName(type));
		repeater.setRepeaterId(main);
		repeater.setUpdateTime(new Date());
		
		//流转发器控制网口开关
		if(RepeaterType.fromName(type).equals(RepeaterType.MAIN)){
			repeater.setOpen(true);
			taskExecuteService.gbeControl(repeater, true);
		}else if(RepeaterType.fromName(type).equals(RepeaterType.BACKUP)){
			repeater.setOpen(false);
			taskExecuteService.gbeControl(repeater, false);
		}
		
		repeaterDao.save(repeater);
		
		return new RepeaterVO().set(repeater);
	}
	
	/**
	 * 删除流转发器<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午1:30:05
	 * @param Long id 流转发器id 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/{id}")
	public Object removeRepeater(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		repeaterService.removeRepeater(id);
		
		return null;
	}
	
	/**
	 * 查询网口类型<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午1:46:34
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/internet/access/type")
	public Object interAccessType(HttpServletRequest request) throws Exception{
		
		//查询网口类型
		Set<String> types = new HashSet<String>(); 
		InternetAccessType[] values = InternetAccessType.values();
		for(InternetAccessType value: values){
			types.add(value.getName());
		}
		
		return new HashMapWrapper<String, Object>().put("type", types)
			   									   .getMap();
	}
	
	/**
	 * 查询网口列表<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 上午9:49:56
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/internet/access/query/list/{id}")
	public Object queryRepeaters(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		List<InternetAccessPO> internetAccessList = internetAccessDao.findByRepeaterId(id);
		
		List<InternetAccessVO> internetAccessVOs = InternetAccessVO.getConverter(InternetAccessVO.class).convert(internetAccessList, InternetAccessVO.class);
		
		return internetAccessVOs;
	}
	
	/**
	 * 添加网口<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 上午10:22:55
	 * @param String address IP地址
	 * @param String type 类型
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/internet/access/add/{id}")
	public Object addInterAccess(
			@PathVariable Long id,
			String address,
			String type,
			HttpServletRequest request) throws Exception{
		
		//校验
		InternetAccessType internetAccessType = InternetAccessType.fromName(type);
		InternetAccessPO access1 = internetAccessDao.findByAddressAndRepeaterIdAndType(address, id, internetAccessType);
		if(access1 != null){
			throw new InternetAccessAddressAlreadyExistException(address);
		}
		
		InternetAccessPO access = new InternetAccessPO();
		access.setAddress(address);
		access.setType(internetAccessType);
		access.setUpdateTime(new Date());
		access.setRepeaterId(id);
		internetAccessDao.save(access);
		
		return new InternetAccessVO().set(access);
	}
	
	/**
	 * 删除网口<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午1:30:05
	 * @param Long id 流转发器id 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/internet/access/remove/{id}")
	public Object removeInterAccess(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		repeaterService.removeAccess(id);
		
		return null;
	}
	
	/**
	 * 切换主机<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月13日 下午2:39:28
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/switch/main")
	public Object switchMain(HttpServletRequest request) throws Exception{
		
		List<RepeaterPO> mainRepeaters = repeaterDao.findByType(RepeaterType.MAIN);
		List<RepeaterPO> backupRepeaters = repeaterDao.findByType(RepeaterType.BACKUP);
		
		if(backupRepeaters != null && backupRepeaters.size() > 0){
			for(RepeaterPO repeater: backupRepeaters){
				taskExecuteService.gbeControl(repeater, false);
			}
		}
		
		if(mainRepeaters != null && mainRepeaters.size() > 0){
			for(RepeaterPO repeater: mainRepeaters){
				taskExecuteService.gbeControl(repeater, true);
			}
		}
		
		return null;
	}
	
	/**
	 * 切换备份<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月13日 下午2:39:28
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/switch/backup")
	public Object switchBackup(HttpServletRequest request) throws Exception{
		
		List<RepeaterPO> mainRepeaters = repeaterDao.findByType(RepeaterType.MAIN);
		List<RepeaterPO> backupRepeaters = repeaterDao.findByType(RepeaterType.BACKUP);
		
		if(mainRepeaters != null && mainRepeaters.size() > 0){
			for(RepeaterPO repeater: mainRepeaters){
				taskExecuteService.gbeControl(repeater, false);
			}
		}
		
		if(backupRepeaters != null && backupRepeaters.size() > 0){
			for(RepeaterPO repeater: backupRepeaters){
				taskExecuteService.gbeControl(repeater, true);
			}
		}
		
		return null;
	}
	
	/**
	 * 查询接入的layerId<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月6日 上午9:56:48
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/layer")
	public Object queryLayer(HttpServletRequest request) throws Exception{
			
		String layerId = RegisterStatus.getNodeId(); 
		
		return layerId; 
	}
	
}
