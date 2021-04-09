package com.sumavision.tetris.bvc.model.agenda;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sumavision.tetris.bvc.business.common.BusinessReturnService;
import com.sumavision.tetris.bvc.business.group.combine.video.CombineVideoSwitchService;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;

@Controller
@RequestMapping(value = "/tetris/bvc/model/agenda/forward")
public class AgendaForwardController {

	@Autowired
	private AgendaForwardQuery agendaForwardQuery;
	
	@Autowired
	private AgendaForwardService agendaForwardService;
	
	@Autowired
	private CombineVideoSwitchService combineVideoSwitchService;
	
	@Autowired
	private BusinessReturnService businessReturnService;
	
	/**
	 * 查询内置议程转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月22日 下午2:22:11
	 * @param Long agendaId 议程id
	 * @return List<AgendaForwardVO> 议程转发列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/internal")
	public Object loadInternal(
			Long agendaId,
			HttpServletRequest request) throws Exception{
		
		return agendaForwardQuery.loadInternal(agendaId);
	}
	
	/**
	 * 查询自定义议程转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月22日 下午2:22:11
	 * @param Long agendaId 议程id
	 * @return List<AgendaForwardVO> 议程转发列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/custom")
	public Object loadCustom(
			Long agendaId,
			HttpServletRequest request) throws Exception{
		
		return agendaForwardQuery.loadCustom(agendaId);
	}
	
	/**
	 * 查询议程转发中的配置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月29日 下午5:21:22
	 * @param Long id 议程转发id
	 * @return Map<String, Object> 议程转发中的配置
	 * 	layout LayoutVO 布局信息
	 *  sources List<AgendaForwardSourceVO> 转发源
	 *  destinations List<AgendaForwardDestinationVO> 转发目的
	 *  audios List<CustomAudioVO> 音频列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/settings")
	public Object loadSettings(
			Long id,
			HttpServletRequest request) throws Exception{
		
		return agendaForwardQuery.loadSettings(id);
	}
	
	/**
	 * 添加内置议程转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月22日 下午3:34:35
	 * @param Long agendaId 议程id
	 * @return AgendaForwardVO 议程转发
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/internal")
	public Object addInternal(
			Long agendaId,
			HttpServletRequest request) throws Exception{
		
		return agendaForwardService.addInternal(agendaId);
	}
	
	/**
	 * 添加自定义议程转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月27日 下午5:04:55
	 * @param String name 议程转发名称
	 * @param Long agendaId 议程id
	 * @return AgendaForwardVO 议程转发
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/custom")
	public Object addCustom(
			String name,
			Long agendaId,
			HttpServletRequest request) throws Exception{
		
		return agendaForwardService.addCustom(name, agendaId);
	}
	
	/**
	 * 修改自定义议程转发名称<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月29日 下午3:31:46
	 * @param Long id 自定义转发id
	 * @param String name 自定义转发名称
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/edit/custom")
	public Object editCustom(
			Long id,
			String name,
			HttpServletRequest request) throws Exception{
		
		agendaForwardService.editCustom(id, name);
		return null;
	}
	
	/**
	 * 删除议程转发<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月23日 下午5:34:53
	 * @param Long id 议程转发id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(
			Long id,
			HttpServletRequest request) throws Exception{
		
		agendaForwardService.remove(id);
		return null;
	}
	
	/**
	 * 查询音频类型列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月22日 下午4:04:42
	 * @return List<Map<String, String>> 音频类型列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/audio/types")
	public Object queryAutioTypes(HttpServletRequest request) throws Exception{
		
		return agendaForwardQuery.queryAutioTypes();
	}
	
	/**
	 * 查询虚拟源布局设置类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月22日 下午6:22:41
	 * @return List<Map<String, String>> 虚拟源布局设置类型
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/layout/position/selection/type")
	public Object queryLayoutPositionSelectionType(HttpServletRequest request) throws Exception{
		
		return agendaForwardQuery.queryLayoutPositionSelectionType();
	}
	
	/**
	 * 修改音频类型<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月22日 下午5:16:12
	 * @param Long id 内置议程转发id
	 * @param String audioType 音频类型
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/handle/audio/type/change")
	public Object handleAudioTypeChange(
			Long id,
			String audioType,
			HttpServletRequest request) throws Exception{
		
		agendaForwardService.handleAudioTypeChange(id, audioType);
		return null;
	}
	
	/**
	 * 修改音量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月22日 下午5:25:44
	 * @param Long id 议程转发id
	 * @param Integer volume 音量
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/handle/volume/change")
	public Object handleVolumeChange(
			Long id,
			Integer volume,
			HttpServletRequest request) throws Exception{
		
		agendaForwardService.handleVolumeChange(id, volume);
		return null;
	}
	
	/**
	 * 添加内置议程转发源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月22日 下午6:36:08
	 * @param Long agendaForwardId 内置议程转发id
	 * @param JSONString roleChannelIds 角色通道id列表
	 * @return List<AgendaForwardSourceVO> 内置议程转发源列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/internal/source")
	public Object addInternalSource(
			Long agendaForwardId,
			String roleChannelIds,
			HttpServletRequest request) throws Exception{
		
		businessReturnService.init(true);
		return agendaForwardService.addInternalSource(agendaForwardId, roleChannelIds);
	}
	
	/**
	 * 添加自定义议程转发源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月30日 下午4:14:34
	 * @param Long agendaForwardId 议程转发id
	 * @param JSONString sources 源列表 {id:"", type:""}
	 * @param Integer serialNum 分屏序号
	 * @param Boolean isLoop 是否轮询
	 * @param Integer loopTime 轮询时间
	 * @return List<AgendaForwardSourceVO> 转发源列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/custom/source")
	public Object addCustomSource(
			Long agendaForwardId,
			String sources,
			Integer serialNum,
			Boolean isLoop,
			Integer loopTime,
			HttpServletRequest request) throws Exception{
		
		businessReturnService.init(true);		
		return agendaForwardService.addCustomSource(agendaForwardId, sources, serialNum, isLoop, loopTime);
	}
	
	/**
	 * 删除音频转发源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月23日 上午9:56:58
	 * @param Long id 音频转发源id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/source")
	public Object removeSource(
			Long id,
			HttpServletRequest request) throws Exception{
		
		businessReturnService.init(true);
		agendaForwardService.removeSource(id);
		return null;
	}
	
	/**
	 * 删除自定义议程转发源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年12月1日 上午10:20:50
	 * @param Long agendaForwardId 议程转发id
	 * @param Integer serialNum 源序号
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/custom/source")
	public Object removeCustomSource(
			Long agendaForwardId,
			Integer serialNum,
			HttpServletRequest request) throws Exception{
		
		businessReturnService.init(true);
		agendaForwardService.removeCustomSource(agendaForwardId, serialNum);
		return null;
	}
	
	/**
	 * 修改源与虚拟源布局序号对应方式<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月23日 下午6:09:31
	 * @param Long 议程转发源id
	 * @param String layoutPositionSelectionType 源与虚拟源布局序号对应方式
	 * @param serialNum 虚拟源布局序号
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/handle/layout/position/selection/type/change")
	public Object handleLayoutPositionSelectionTypeChange(
			Long id,
			String layoutPositionSelectionType,
			Integer serialNum,
			Boolean isLoop,
			Integer loopTime,
			HttpServletRequest request) throws Exception{
		
		agendaForwardService.handleLayoutPositionSelectionTypeChange(id, layoutPositionSelectionType, serialNum, isLoop, loopTime);
		return null;
	}
	
	/**
	 * 添加议程转发目的<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月23日 下午1:09:52
	 * @param Long agendaForwardId 内置议程转发id
	 * @param JSONString roleIds 角色id
	 * @param JSONString groupMemberIds 会议成员id列表
	 * @return List<AgendaForwardDestinationVO> 议程转发目的
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/destination")
	public Object addDestination(
			Long agendaForwardId,
			String roleIds,
			String groupMemberIds,
			HttpServletRequest request) throws Exception{
		
		return agendaForwardService.addDestination(agendaForwardId, roleIds, groupMemberIds);
	}
	
	/**
	 * 删除议程转发目的<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月23日 下午2:19:12
	 * @param Long id 议程转发目的id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/destination")
	public Object removeDestination(
			Long id,
			HttpServletRequest request) throws Exception{
		
		agendaForwardService.removeDestination(id);
		return null;
	}
	
	/**
	 * 添加虚拟源设置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月23日 下午5:02:41
	 * @param Long id 议程转发id
	 * @param Integer min 源数目下限
	 * @param Integer max 源数目上限
	 * @param Long layoutId 虚拟源id
	 * @return List<LayoutScopeVO> 虚拟源设置列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/layout/scope")
	public Object addLayoutScope(
			Long id,
			Integer min,
			Integer max,
			Long layoutId,
			HttpServletRequest request) throws Exception{
		
		return agendaForwardService.addLayoutScope(id, min, max, layoutId);
	}
	
	/**
	 * 删除虚拟源设置<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月23日 下午5:07:33
	 * @param Long id 虚拟源设置id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/layout/scope")
	public Object removeLayoutScope(
			Long id,
			HttpServletRequest request) throws Exception{
		
		agendaForwardService.removeLayoutScope(id);
		return null;
	}
	
	/**
	 * 为自定义议程转发设置虚拟源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月30日 上午11:51:58
	 * @param Long id 议程转发id
	 * @param Long layoutId 虚拟源id
	 * @return LayoutVO 虚拟源带布局
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/set/layout")
	public Object setLayout(
			Long id,
			Long layoutId,
			HttpServletRequest reque) throws Exception{
		
		return agendaForwardService.setLayout(id, layoutId);
	}
	
	/**
	 * 添加议程转发自定义音频<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月23日 下午2:58:34
	 * @param Long agendaForwardId 议程转发id
	 * @param JSONString roleChannelIds 角色通道id
	 * @param JSONString groupMemberChannelIds 会议成员通道id
	 * @return List<CustomAudioVO> 音频列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/custom/audio")
	public Object addCustomAudio(
			Long agendaForwardId,
			String roleChannelIds,
			String groupMemberChannelIds,
			HttpServletRequest request) throws Exception{
		
		return agendaForwardService.addCustomAudio(agendaForwardId, roleChannelIds, groupMemberChannelIds);
	}
	
	/**
	 * 删除议程转发自定义音频<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月23日 下午3:03:42
	 * @param Long id 自定义音频id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/custom/audio")
	public Object removeCustomAudio(
			Long id,
			HttpServletRequest request) throws Exception{
		
		agendaForwardService.removeCustomAudio(id);
		return null;
	}
	
	/**
	 * 合屏切换至指定源<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年3月8日 下午4:53:13
	 * @param agendaForwardId 议程转发id
	 * @param sourceId AgendaForwardSourcePO的sourceId
	 * @param sourceType AgendaForwardSourcePO的sourceType
	 * @param serialNum 分屏序号
	 * @param index 在源列表中的需要，用来校验
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/switch/polling/index")
	public Object switchPollingIndex(
			Long agendaForwardId,
			Long sourceId,
			String sourceType,
			Integer serialNum,
			int index,
			HttpServletRequest request) throws Exception{
		
		combineVideoSwitchService.switchPollingIndex(agendaForwardId, sourceId, SourceType.valueOf(sourceType), serialNum, index);
		return null;
	}
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/switch/polling/next")
	public Object switchPollingNext(
			Long agendaForwardId,
			Integer serialNum,
			HttpServletRequest request) throws Exception{
		
		businessReturnService.init(false);
		combineVideoSwitchService.switchPollingNext(agendaForwardId, serialNum);
		return null;
	}
	
}
