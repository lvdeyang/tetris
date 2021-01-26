package com.sumavision.tetris.cs.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.auth.token.TerminalType;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.cs.channel.Adapter;
import com.sumavision.tetris.cs.channel.BroadWay;
import com.sumavision.tetris.cs.channel.ChannelPO;
import com.sumavision.tetris.cs.channel.ChannelQuery;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityBroadInfoService;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityBroadInfoVO;
import com.sumavision.tetris.cs.program.ProgramQuery;
import com.sumavision.tetris.cs.program.ScreenContentType;
import com.sumavision.tetris.cs.program.ScreenVO;
import com.sumavision.tetris.cs.program.TemplateScreenVO;
import com.sumavision.tetris.cs.program.TemplateVO;
import com.sumavision.tetris.cs.schedule.api.qt.response.ApiQtScheduleScreenProgramVO;
import com.sumavision.tetris.cs.schedule.api.qt.response.ApiQtScheduleScreenVO;
import com.sumavision.tetris.cs.schedule.api.qt.response.ApiQtScheduleVO;
import com.sumavision.tetris.mims.app.media.encode.MediaEncodeQuery;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Component
public class ScheduleQuery {
	@Autowired
	private ScheduleDAO scheduleDAO;
	
	@Autowired
	private ProgramQuery programQuery;
	
	@Autowired
	private ChannelQuery channelQuery;
	
	@Autowired
	private BroadAbilityBroadInfoService broadAbilityBroadInfoService;
	
	@Autowired
	private MediaEncodeQuery mediaEncodeQuery;
	
	@Autowired
	private Adapter adapter;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 获取排期信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 * @param Long scheduleId 排期id
	 * @return ScheduleVO 排期信息
	 */
	public ScheduleVO getById(Long scheduleId) throws Exception {
		SchedulePO schedulePO = scheduleDAO.findById(scheduleId);
		
		return new ScheduleVO().set(schedulePO);
	}
	
	/**
	 * 获取排期列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 * @param Long channelId 频道id
	 * @param int currentPage 分页当前页
	 * @param int pageSize 分页大小
	 * @return total 列表总数
	 * @return List<ScheduleVO> 排期信息
	 */
	public Map<String, Object> getByChannelId(Long channelId, int currentPage, int pageSize) throws Exception{
		Pageable page = PageRequest.of(currentPage - 1, pageSize);
		Page<SchedulePO> schedulePages = scheduleDAO.findByChannelId(channelId, page);
		List<SchedulePO> schedules = schedulePages.getContent();
		List<ScheduleVO> scheduleVOs = ScheduleVO.getConverter(ScheduleVO.class).convert(schedules, ScheduleVO.class);
		for (ScheduleVO scheduleVO : scheduleVOs) {
			scheduleVO.setProgram(programQuery.getProgram(scheduleVO.getId()));
		}
		return new HashMapWrapper<String,Object>().put("data", scheduleVOs)
				.put("total", schedulePages.getTotalElements())
				.getMap();
	}
	
	/**
	 * 获取排期列表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 * @param Long channelId 频道id
	 * @return List<ScheduleVO> 排期信息
	 */
	public List<ScheduleVO> getByChannelId(Long channelId) throws Exception {
		List<SchedulePO> schedulePOs = scheduleDAO.findByChannelId(channelId);
		List<ScheduleVO> scheduleVOs = new ArrayList<ScheduleVO>();
		for (SchedulePO schedulePO : schedulePOs) {
			scheduleVOs.add(new ScheduleVO().set(schedulePO).setProgram(programQuery.getProgram(schedulePO.getId())));
		}
		return scheduleVOs;
	}
	
	/**
	 * 根据id数列请求排期表<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午9:50:10
	 * @param ids 排期id数列
	 */
	public List<ScheduleVO> questSchedulesByChannelId(Long channelId) throws Exception {
		List<SchedulePO> schedulePOs = scheduleDAO.findByChannelId(channelId);
		List<ScheduleVO> scheduleVOs = new ArrayList<ScheduleVO>();
		if (schedulePOs == null || schedulePOs.isEmpty()) return scheduleVOs;
		scheduleVOs = ScheduleVO.getConverter(ScheduleVO.class).convert(schedulePOs, ScheduleVO.class);

		ChannelPO channel = channelQuery.findByChannelId(channelId);
		List<BroadAbilityBroadInfoVO> broadAbilityBroadInfoVOs = broadAbilityBroadInfoService.queryFromChannelId(channelId);
		
		String port = "9999";
		for (BroadAbilityBroadInfoVO broadAbilityBroadInfoVO : broadAbilityBroadInfoVOs) {
			if (broadAbilityBroadInfoVO.getUserId() != null) {
				String previewUrlPort = broadAbilityBroadInfoVO.getPreviewUrlPort();
				if (previewUrlPort != null && !previewUrlPort.isEmpty()){
					port = previewUrlPort;
					break;
				}
			}
		}
		
		for (ScheduleVO scheduleVO : scheduleVOs) {
			scheduleVO.setProgram(programQuery.getProgram(scheduleVO.getId()));
			if (channel.getBroadWay().equals(BroadWay.ABILITY_BROAD.getName())) {
				scheduleVO.setMediaType("stream");
			} else if (channel.getBroadWay().equals(BroadWay.FILE_DOWNLOAD_BROAD)) {
				scheduleVO.setMediaType("file");
			}
			scheduleVO.setStreamUrlPort(Integer.parseInt(port));
			if (channel.getEncryption() != null && channel.getEncryption()){
				scheduleVO.setEncryptKey(mediaEncodeQuery.queryKey());
			}
		}
		
		return scheduleVOs;
	}
	
	/**
	 * 根据频道id请求排期表信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午9:50:10
	 * @param Long 频道id
	 * @param userIp 用户ip
	 */
	public List<ApiQtScheduleVO> questJSONSchedulesByChannelId(Long channelId, String userIp) throws Exception {
		List<ScheduleVO> scheduleVOs = getByChannelId(channelId);
		if (scheduleVOs == null || scheduleVOs.isEmpty()) return null;

		List<BroadAbilityBroadInfoVO> broadAbilityBroadInfoVOs = broadAbilityBroadInfoService.queryFromChannelId(channelId);
		
		//获取用户ip
		if (userIp == null || userIp.isEmpty()) {
			UserVO user = userQuery.current();
			List<UserVO> userVOs = userQuery.findByIdInAndType(new ArrayListWrapper<Long>().add(user.getId()).getList(), TerminalType.QT_MEDIA_EDITOR.getName());
			if (userVOs != null && !userVOs.isEmpty()) {
				userIp = userVOs.get(0).getIp();
			}
		}
		if (userIp == null || userIp.isEmpty()) return null;
		
		//获取用户推流可用端口
		String startPort = "9999";
		String endPort = "";
		for (BroadAbilityBroadInfoVO broadAbilityBroadInfoVO : broadAbilityBroadInfoVOs) {
			if (broadAbilityBroadInfoVO.getUserId() != null) {
				endPort = broadAbilityBroadInfoVO.getPreviewUrlEndPort();
				String previewUrlPort = broadAbilityBroadInfoVO.getPreviewUrlPort();
				if (previewUrlPort != null && !previewUrlPort.isEmpty()){
					startPort = previewUrlPort;
					break;
				}
			}
		}
		
		List<ApiQtScheduleVO> returnApiQtScheduleVOs = new ArrayList<ApiQtScheduleVO>();
		for (ScheduleVO scheduleVO : scheduleVOs) {
			//获取分屏信息
			TemplateVO template = programQuery.getScreen2Template(scheduleVO.getId(), adapter.getAllTemplate());
			if (template == null) return null;
			List<ApiQtScheduleScreenVO> qtScheduleScreenVOs = new ArrayList<ApiQtScheduleScreenVO>();
			
			//遍历分屏，获取各分屏信息和节目单数组
			Integer outputIndex = 0;
			List<TemplateScreenVO> templateScreenVOs = template.getScreen();
			for (TemplateScreenVO templateVO : templateScreenVOs) {
				//获取文件推流输出端口
				String port = "";
				String streamUrl = "";
				if (outputIndex == 0) {
					port = startPort;
				} else if (endPort != null && !endPort.isEmpty()){
					Integer sendStartPort = Integer.parseInt(startPort);
					Integer sendEndPort = Integer.parseInt(endPort);
					if (sendStartPort + outputIndex <= sendEndPort) {
						port = "" + (sendStartPort + outputIndex);
					}
				}
				if (userIp != null && !userIp.isEmpty() && !port.isEmpty()) {
					streamUrl = new StringBufferWrapper().append("udp://@").append(userIp).append(":").append(port).toString();
				}
				
				ApiQtScheduleScreenVO qtScheduleScreenVO = new ApiQtScheduleScreenVO();
				List<ApiQtScheduleScreenProgramVO> qtScheduleScreenProgramVOs = new ArrayList<ApiQtScheduleScreenProgramVO>();
				List<ScreenVO> screenVOs = templateVO.getData();
				Integer index = 0;
				if (templateScreenVOs.isEmpty()) continue;
				//遍历节目单数组，获取源文件列表
				for (ScreenVO screenVO : screenVOs) {
					String mediaType = screenVO.getType();
					String contentType = screenVO.getContentType();
					ScreenContentType screenContentType = ScreenContentType.fromName(contentType);
					switch (screenContentType) {
					case ABILITY_PICTURE:
					case TEXT:
					case TIME:
						index += 1;
						qtScheduleScreenProgramVOs.add(new ApiQtScheduleScreenProgramVO()
								.setFromScreenVO(screenVO)
								.setIndex(index)
								.setMediaType(screenContentType.getValue())
								.setSource(""));
						break;
					default:
						if (mediaType != null) {
							switch (mediaType) {
							case "AUDIO":
							case "VIDEO":
							case "AUDIO_STREAM":
							case "VIDEO_STREAM":
								if (streamUrl.isEmpty()) break;
								index += 1;
								qtScheduleScreenProgramVOs.add(new ApiQtScheduleScreenProgramVO().setFromScreenVO(screenVO)
										.setIndex(index)
										.setMediaType("fileToStream")
										.setPreviewUrl(streamUrl));
								break;
							default:
								break;
							}
						}
						break;
					}
				}
				
				qtScheduleScreenVO.setTop(templateVO.getTop())
						.setLeft(templateVO.getLeft())
						.setWidth(templateVO.getWidth())
						.setHeight(templateVO.getHeight())
						.setNo(templateVO.getNo())
						.setProgram(qtScheduleScreenProgramVOs);
				qtScheduleScreenVOs.add(qtScheduleScreenVO);
				if (!qtScheduleScreenProgramVOs.isEmpty()) {
					if ("fileToStream".equals(qtScheduleScreenProgramVOs.get(0).getMediaType())) outputIndex++;
				}
				
				if (templateScreenVOs.indexOf(templateVO) == templateScreenVOs.size() - 1) outputIndex = 0;
			}
			
			returnApiQtScheduleVOs.add(new ApiQtScheduleVO().setScreens(qtScheduleScreenVOs).setEffectTime(scheduleVO.getBroadDate()));
		}
		
		return returnApiQtScheduleVOs;
	}
}
