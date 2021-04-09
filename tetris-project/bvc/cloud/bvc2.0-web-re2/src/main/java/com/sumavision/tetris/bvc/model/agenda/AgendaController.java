package com.sumavision.tetris.bvc.model.agenda;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller(value = "com.sumavision.tetris.bvc.model.agenda.AgendaController")
@RequestMapping(value = "/tetris/bvc/model/agenda")
public class AgendaController {

	@Autowired
	private AgendaQuery agendaQuery;
	
	@Autowired
	private AgendaService agendaService;
	
	/**
	 * 查询议程业务类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月30日 上午8:45:29
	 * @return Set<String> 业务类型
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/business/types")
	public Object queryBusinessTypes(HttpServletRequest request) throws Exception{
		
		return agendaQuery.queryBusinessTypes();
	}
	
	/**
	 * 查询议程音频优先级<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月27日 上午10:51:49
	 * @return List<Map<String, String>> 议程音频优先级列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/audio/priorities")
	public Object queryAudioPriorities(HttpServletRequest request) throws Exception{
		
		return agendaQuery.queryAudioPriorities();
	}
	
	/**
	 * 根据id查询议程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月22日 下午3:52:41
	 * @param Long id 议程id
	 * @return AgendaVO 议程信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/by/id")
	public Object queryById(
			Long id, 
			HttpServletRequest request) throws Exception{
		
		return agendaQuery.queryById(id);
	}
	
	/**
	 * 分页查询内置议程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:32:18
	 * @param int currentPage 当前页
	 * @param int pageSize 每页数据量
	 * @return total long 总数据量
	 * @return rows List<AgendaVO> 议程列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/internal/agenda")
	public Object loadInternalAgenda(
			int currentPage, 
			int pageSize, 
			HttpServletRequest request) throws Exception{
		
		return agendaQuery.loadInternalAgenda(currentPage, pageSize);
	}
	
	/**
	 * 根据业务查询议程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月25日 上午11:46:04
	 * @param Long businessId 业务id
	 * @param BusinessInfoType businessInfoType 业务类型
	 * @return List<AgendaVO> 议程列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/by/business/id/and/business/info/type")
	public Object loadByBusinessIdAndBusinessInfoType(
			Long businessId,
			String businessInfoType,
			HttpServletRequest request) throws Exception{
		
		return agendaQuery.loadByBusinessIdAndBusinessInfoType(businessId, businessInfoType);
	}
	
	/**
	 * 添加议程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:38:25
	 * @param String name 议程名称
	 * @param String remark 备注
	 * @param Long businessId 关联业务id
	 * @param String businessTypeName 业务类型
	 * @return AgendaVO 议程
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add")
	public Object add(
			String name,
			String remark,
			Long businessId,
			String businessInfoTypeName,
			HttpServletRequest request) throws Exception{
		
		return agendaService.add(name, remark, businessId, businessInfoTypeName);
	}
	
	/**
	 * 修改议程信息<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:42:26
	 * @param Long id 议程id
	 * @param String name 议程名称
	 * @param String remark 备注
	 * @param String businessInfoTypeName 业务类型
	 * @return AgendaVO 议程
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit")
	public Object edit(
			Long id,
			String name,
			String remark,
			String businessInfoTypeName,
			HttpServletRequest request) throws Exception{
		
		return agendaService.edit(id, name, remark, businessInfoTypeName);
	}
	
	/**
	 * 是否启动全局音频修改<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月20日 下午1:52:44
	 * @param Long id 议程id
	 * @param Boolean globalCustomAudio 是否启动全局音频
	 * @return AgendaVO 议程
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/global/custom/audio/change")
	public Object globalCustomAudioChange(
			Long id,
			Boolean globalCustomAudio,
			HttpServletRequest request) throws Exception{
		
		return agendaService.globalCustomAudioChange(id, globalCustomAudio);
	}
	
	/**
	 * 议程音频类型修改<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月27日 上午9:16:14
	 * @param Long id 议程id
	 * @param String audioType 音频类型
	 * @return AgendaVO 议程
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/audio/type/change")
	public Object audioTypeChange(
			Long id,
			String audioType,
			HttpServletRequest request) throws Exception{
		
		return agendaService.audioTypeChange(id, audioType);
	}
	
	/**
	 * 修改议程音频优先级设置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月27日 上午11:13:08
	 * @param Long id 议程id
	 * @param String audioPriority 音频优先级
	 * @return AgendaVO 议程
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/audio/priority/change")
	public Object audioPriorityChange(
			Long id,
			String audioPriority,
			HttpServletRequest request) throws Exception{
		
		return agendaService.audioPriorityChange(id, audioPriority);
	}
	
	/**
	 * 议程音量修改<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月20日 下午4:05:54
	 * @param Long id 议程id
	 * @param Integer volume 音量
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/volume/change")
	public Object volumeChange(
			Long id,
			Integer volume,
			HttpServletRequest request) throws Exception{
		
		agendaService.volumeChange(id, volume);
		return null;
	}
	
	/**
	 * 删除议程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月30日 下午4:43:30
	 * @param Long id 议程id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/delete")
	public Object delete(
			Long id, 
			HttpServletRequest request) throws Exception{
		
		agendaService.delete(id);
		return null;
	}
	
}
