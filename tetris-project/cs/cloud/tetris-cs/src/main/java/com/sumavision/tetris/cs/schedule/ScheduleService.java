package com.sumavision.tetris.cs.schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.cs.channel.ChannelService;
import com.sumavision.tetris.cs.channel.api.ApiServerScheduleCastVO;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityBroadInfoDAO;
import com.sumavision.tetris.cs.channel.broad.ability.BroadAbilityBroadInfoPO;
import com.sumavision.tetris.cs.channel.broad.terminal.BroadTerminalBroadInfoDAO;
import com.sumavision.tetris.cs.channel.broad.terminal.BroadTerminalBroadInfoPO;
import com.sumavision.tetris.cs.menu.CsResourceDAO;
import com.sumavision.tetris.cs.menu.CsResourcePO;
import com.sumavision.tetris.cs.menu.CsResourceQuery;
import com.sumavision.tetris.cs.program.ProgramDAO;
import com.sumavision.tetris.cs.program.ProgramPO;
import com.sumavision.tetris.cs.program.ProgramService;
import com.sumavision.tetris.cs.program.ProgramVO;
import com.sumavision.tetris.cs.program.ScreenDAO;
import com.sumavision.tetris.cs.program.ScreenPO;
import com.sumavision.tetris.cs.program.ScreenVO;
import com.sumavision.tetris.cs.schedule.api.server.ApiServerScheduleVO;
import com.sumavision.tetris.cs.schedule.exception.ProgramNotExistsException;
import com.sumavision.tetris.cs.schedule.exception.ScheduleNoneToBroadException;
import com.sumavision.tetris.cs.schedule.exception.ScheduleNotExistsException;
import com.sumavision.tetris.mims.app.media.avideo.MediaAVideoQuery;
import com.sumavision.tetris.mims.app.media.avideo.MediaAVideoVO;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamQuery;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamVO;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamQuery;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamVO;
import com.sumavision.tetris.orm.exception.ErrorTypeException;

@Service
@Transactional(rollbackFor = Exception.class)
public class ScheduleService {
	@Autowired
	private ChannelService channelService;
	
	@Autowired
	private ScheduleQuery scheduleQuery;
	
	@Autowired
	private ScheduleDAO scheduleDAO;
	
	@Autowired
	private ProgramService programService;
	
	@Autowired
	private MediaAVideoQuery mediaAVideoQuery;
	
	@Autowired
	private MediaAudioStreamQuery mediaAudioStreamQuery;
	
	@Autowired
	private MediaVideoStreamQuery mediaVideoStreamQuery;
	
	@Autowired
	private BroadTerminalBroadInfoDAO broadTerminalBroadInfoDao;
	
	@Autowired
	private CsResourceDAO csSourceDao;
	
	@Autowired
	private ScreenDAO screenDao;

	@Autowired
	private ProgramDAO programDao;
	
	/**
	 * 添加排期<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 * @param channelId 频道id
	 * @param broadDate 播发日期
	 * @param remark 备注
	 * @return ScheduleVO 排期信息
	 */
	public ScheduleVO add(Long channelId, String broadDate, String endDate, String remark) throws Exception{
		SchedulePO schedulePO = addToPO(channelId, broadDate, endDate, remark);
		
		return new ScheduleVO().set(schedulePO);
	}
	
	/**
	 * 添加排期<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 * @param channelId 频道id
	 * @param broadDate 播发日期
	 * @param remark 备注
	 * @return SchedulePO 排期信息
	 */
	public SchedulePO addToPO(Long channelId, String broadDate, String endDate, String remark) throws Exception {
		SchedulePO schedulePO = new SchedulePO();
		schedulePO.setChannelId(channelId);
		schedulePO.setUpdateTime(new Date());
		schedulePO.setBroadDate(broadDate);
		schedulePO.setEndDate(endDate);
		schedulePO.setRemark(remark);
		scheduleDAO.save(schedulePO);
		
		return schedulePO;
	}
	
	/**
	 * 删除当前时间后的排期<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月3日 下午7:33:45
	 * @param channelId
	 * @throws Exception
	 */
	public void removeFromNowByChannelId(Long channelId) throws Exception {
		Long now = DateUtil.getLongDate();
		List<SchedulePO> schedulePOs = scheduleDAO.findByChannelId(channelId);
		
		if (schedulePOs == null || schedulePOs.isEmpty()) return;
		
		List<SchedulePO> deletePOs = new ArrayList<SchedulePO>();
		for (SchedulePO schedulePO : schedulePOs) {
			Long broadTime = DateUtil.parse(schedulePO.getBroadDate()).getTime();
			if (broadTime > now) deletePOs.add(schedulePO);
		}
		if (!deletePOs.isEmpty()) scheduleDAO.deleteInBatch(deletePOs);
	}
	
	/**
	 * 删除排期<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 * @param scheduleId 排期id
	 */
	public ScheduleVO remove(Long scheduleId) throws Exception{
		SchedulePO schedule = scheduleDAO.findById(scheduleId);
		
		if (schedule == null) throw new ScheduleNotExistsException(scheduleId);
		
		programService.removeProgramByScheduleId(schedule.getId());
		
		scheduleDAO.delete(schedule);
		
		return new ScheduleVO().set(schedule);
	}
	
	/**
	 * 批量删除排期<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月28日 上午11:14:06
	 * @param List<Long> scheduleIds 排期id列表
	 * @return List<ScheduleVO> 被删除的排期列表
	 */
	public List<ScheduleVO> removeInBatch(List<Long> scheduleIds) throws Exception{
		if (scheduleIds == null || scheduleIds.isEmpty()) return null;
		
		List<SchedulePO> schedulePOs = scheduleDAO.findAllById(scheduleIds);
		
		if (schedulePOs == null || schedulePOs.isEmpty()) return null;
		
		for (SchedulePO schedulePO : schedulePOs) {
			programService.removeProgramByScheduleId(schedulePO.getId());
		}
		
		scheduleDAO.deleteInBatch(schedulePOs);
		
		return ScheduleVO.getConverter(ScheduleVO.class).convert(schedulePOs, ScheduleVO.class);
	}
	
	/**
	 * 编辑排期<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 * @param scheduleId 排期id
	 * @param broadDate 播发日期
	 * @param remark 备注
	 * @return ScheduleVO 排期信息
	 */
	public ScheduleVO edit(Long scheduleId, String broadDate, String endDate, String remark) throws Exception{
		SchedulePO schedule = scheduleDAO.findById(scheduleId);
		
		if (schedule == null) throw new ScheduleNotExistsException(scheduleId);
		
		schedule.setBroadDate(broadDate);
		schedule.setRemark(remark);
		schedule.setEndDate(endDate);
		
		channelService.changeScheduleDeal(schedule.getChannelId());
		
		return new ScheduleVO().set(schedule);
	}
	
	/**
	 * 根据频道id删除排期<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午11:06:57
	 * @param channelId 频道id
	 */
	public void removeByChannelId(Long channelId) throws Exception {
		List<SchedulePO> schedulePOs = scheduleDAO.findByChannelId(channelId);
		
		if (schedulePOs == null || schedulePOs.isEmpty()) return;
		
		for (SchedulePO schedulePO : schedulePOs) {
			programService.removeProgramByScheduleId(schedulePO.getId());
		}
		
		scheduleDAO.deleteInBatch(schedulePOs);
	}
	
	/**
	 * 根据频道id添加排期(自动生成排期节目)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 下午3:45:26
	 * @param channelId 频道id
	 * @param broadDate 排期时间
	 * @param screens 排期节目
	 * @return
	 */
	public SchedulePO addScheduleToPO(Long channelId, String broadDate, List<ScreenVO> screens) throws Exception {
		SchedulePO schedule = addToPO(channelId, broadDate, null, "");
		
		if (screens == null || screens.isEmpty()) return null;
			
		Date date = new Date();
		List<ScreenVO> screenVOs = new ArrayList<ScreenVO>();
		for (int i = 0; i < screens.size(); i++) {
			ScreenVO screen = new ScreenVO();
			ScreenVO item = screens.get(i);
			screen.setUpdateTime(date);
			screen.setSerialNum(1l);
			screen.setIndex((long)(i+1));
			screen.setMimsUuid(item.getMimsUuid());
			screen.setName(item.getName());
			screen.setType(item.getType());
			screen.setMimetype(item.getMimetype());
			screen.setPreviewUrl(item.getPreviewUrl());
			screen.setHotWeight(item.getHotWeight());
			screen.setDownloadCount(item.getDownloadCount());
			screen.setDuration(item.getDuration());
			screen.setEncryption(item.getEncryption());
			screen.setEncryptionUrl(item.getEncryptionUrl());
			screen.setResourceId(item.getResourceId());
			screenVOs.add(screen);
		}
		ProgramVO program = new ProgramVO();
		program.setScheduleId(schedule.getId());
		program.setScreenNum(1l);
		program.setUpdateTime(date);
		program.setScreenInfo(screenVOs);
		programService.setProgram(program);
		
		channelService.changeScheduleDeal(channelId);
			
		return schedule;
	}
	
	/**
	 * 根据频道id添加排期(自动生成排期节目)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月14日 下午3:45:26
	 * @param channelId 频道id
	 * @param broadDate 排期时间
	 * @param screens 排期节目
	 * @return
	 */
	public ScheduleVO addSchedule(Long channelId, String broadDate, List<ScreenVO> screens) throws Exception {
		ScheduleVO schedule = add(channelId, broadDate, null, "");
		
		if (screens == null || screens.isEmpty()) return null;
			
		Date date = new Date();
		List<ScreenVO> screenVOs = new ArrayList<ScreenVO>();
		for (int i = 0; i < screens.size(); i++) {
			ScreenVO screen = new ScreenVO();
			ScreenVO item = screens.get(i);
			screen.setUpdateTime(date);
			screen.setSerialNum(1l);
			screen.setIndex((long)(i+1));
			screen.setMimsUuid(item.getMimsUuid());
			screen.setName(item.getName());
			screen.setType(item.getType());
			screen.setMimetype(item.getMimetype());
			screen.setPreviewUrl(item.getPreviewUrl());
			screen.setHotWeight(item.getHotWeight());
			screen.setDownloadCount(item.getDownloadCount());
			screen.setDuration(item.getDuration());
			screen.setEncryption(item.getEncryption());
			screen.setEncryptionUrl(item.getEncryptionUrl());
			screen.setResourceId(item.getResourceId());
			screen.setAudioPid(item.getAudioPid());
			screen.setVideoPid(item.getVideoPid());
			screen.setStartTime(item.getStartTime());
			screen.setEndTime(item.getEndTime());
			screen.setFreq(item.getFreq());
			screenVOs.add(screen);
		}
		ProgramVO program = new ProgramVO();
		program.setScheduleId(schedule.getId());
		program.setScreenNum(1l);
		program.setScreenId(1l);
		program.setOrient("horizontal");
		program.setUpdateTime(date);
		program.setScreenInfo(screenVOs);
			
		schedule.setProgram(programService.setProgram(program));
		
		channelService.changeScheduleDeal(channelId);
			
		return schedule;
	}
	
	/**
	 * 根据频道id批量添加排期<br/>
	 * <b>作者:</b>sms<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月18日 上午11:01:05
	 * @param Long channelId 频道id
	 * @param List<ApiServerScheduleVO> scheduleList 排期单
	 */
	public List<ScheduleVO> addSchedules(Long channelId, List<ApiServerScheduleVO> scheduleList) throws Exception {
		if (scheduleList == null || scheduleList.isEmpty()) return null;
		
		List<ScheduleVO> scheduleVOs = new ArrayList<ScheduleVO>();
		
		for (ApiServerScheduleVO apiServerScheduleVO : scheduleList) {
			ScheduleVO schedule = add(channelId, apiServerScheduleVO.getBroadDate(), null, "");
			
			Date date = new Date();
			List<ScreenVO> screenVOs = new ArrayList<ScreenVO>();
			List<ScreenVO> assetScreens = apiServerScheduleVO.getAssetScreens();
			for (int i = 0; i < assetScreens.size(); i++) {
				ScreenVO screen = new ScreenVO();
				screen.setUpdateTime(date);
				screen.setSerialNum(1l);
				screen.setIndex((long)(i+1));
				screen.setPreviewUrl(assetScreens.get(i).getPreviewUrl());
				screen.setDuration(assetScreens.get(i).getDuration());
				screenVOs.add(screen);
			}
			ProgramVO program = new ProgramVO();
			program.setScheduleId(schedule.getId());
			program.setScreenNum(1l);
			program.setUpdateTime(date);
			program.setScreenInfo(screenVOs);
			
			schedule.setProgram(programService.setProgram(program));
			
			scheduleVOs.add(schedule);
		}
		
		channelService.changeScheduleDeal(channelId);
		
		return scheduleVOs;
	}
	
	/**
	 * 根据频道id批量添加排期<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月18日 上午11:01:05
	 * @param Long channelId 频道id
	 * @param List<ApiServerScheduleCastVO> scheduleList 排期单
	 */
	public List<ScheduleVO> addSchedulesFromCast(Long channelId, List<ApiServerScheduleCastVO> scheduleList) throws Exception {
		List<ScheduleVO> scheduleVOs = new ArrayList<ScheduleVO>();
		
		if (scheduleList != null && !scheduleList.isEmpty()) {
			for (ApiServerScheduleCastVO cast : scheduleList) {
				List<ScreenVO> screenVOs = new ArrayList<ScreenVO>();
				SchedulePO schedulePO = null;
				switch (cast.getAssetType().toLowerCase()) {
				case "audio":
				case "video":
					MediaAVideoVO media = mediaAVideoQuery.loadByIdAndType(cast.getAssetId(), cast.getAssetType());
					if (media != null) {
						ScreenVO screen = new ScreenVO();
						screen.getFromAVideoVO(media);
						for (int i = 0; i < cast.getPlayTime(); i++) {
							screenVOs.add(screen);
						}
						schedulePO = addScheduleToPO(channelId, cast.getStartTime(), screenVOs);
					}
					break;
				case "audiostream":
					MediaAudioStreamVO audioStream = mediaAudioStreamQuery.findById(cast.getAssetId());
					if (audioStream != null) {
						ScreenVO screen = new ScreenVO();
						screenVOs.add(screen.getFromAudioStreamVO(audioStream));
						schedulePO = addScheduleToPO(channelId, cast.getStartTime(), screenVOs);
					}
					break;
				case "videostream":
					MediaVideoStreamVO videoStreamVO = mediaVideoStreamQuery.findById(cast.getAssetId());
					if (videoStreamVO != null) {
						ScreenVO screen = new ScreenVO();
						screenVOs.add(screen.getFromVideoStreamVO(videoStreamVO));
						schedulePO = addScheduleToPO(channelId, cast.getStartTime(), screenVOs);
					}
				default:
					throw new ErrorTypeException("type", cast.getAssetType());
				}
				if (schedulePO != null) {
					schedulePO.setEndDate(cast.getEndTime());
					scheduleDAO.save(schedulePO);
					scheduleVOs.add(new ScheduleVO().set(schedulePO));
				}
			}
		}
		
		return scheduleVOs;
	}
	
	
	public void importSchedule(List<ScheduleExcelModel> scheduleList,Long channelId) throws Exception{
		
		List<ScheduleExcelModel> scheduleListCopy = new ArrayList<ScheduleExcelModel>();
		scheduleListCopy.addAll(scheduleList);
		//遍历解析后的节目单
		for (ScheduleExcelModel schedule : scheduleList) {
			//解析节目单获取的排期开始时间
			if(schedule.getScheduleDate()!=null){
				//分割日期时间字符串获取日期
				String broadDate = schedule.getScheduleDate();
				String broadDates[] = broadDate.split(" ");
				//新建排期,获取排期id
				SchedulePO schedulePO = addToPO(channelId, broadDate,"","");
				Long scheduleId = schedulePO.getId();
				List<ScreenPO> screenPOList = new ArrayList<ScreenPO>();
				//遍历节目单，添加节目
				int count = 1;
				for (int i = 1;i<scheduleListCopy.size();i++){
					Long programId = 0l;
					//遍历属于当前排期的节目
					if(scheduleListCopy.get(i).getScheduleDate()==null){
						//新建program
						ProgramPO program =  programDao.findByScheduleId(scheduleId);
						if (program == null) {
							program = new ProgramPO();
							program.setUpdateTime(new Date());
							program.setScheduleId(scheduleId);
						}
						program.setScreenNum(1l);
						program.setScreenId(1l);
						program.setOrient("horizontal");
						programDao.save(program);
						programId = program.getId();
						//获取节目名
						String programName = scheduleListCopy.get(i).getProgramName();
						//拼接日期获得完整开始结束时间
						Date startTime=null;
						Date endTime=null;
						if(scheduleListCopy.get(i).getpStartTime()!=null||scheduleListCopy.get(i).getpEndTime()!=null){
							String fullStartTime = broadDates[0]+" "+scheduleListCopy.get(i).getpStartTime();
							String fullendTime = broadDates[0]+" "+scheduleListCopy.get(i).getpEndTime();
							
							SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							startTime = time.parse(fullStartTime);
							endTime = time.parse(fullendTime);
						}
						//根据节目名和频道id查询媒资目录，获取节目信息
						List<CsResourcePO> resourcePOs = csSourceDao.findByNameAndChannelId(programName, channelId);
						if(resourcePOs.size()==0){
							//节目不存在抛异常
							throw new ProgramNotExistsException(programName);
						}else{
							ScreenPO screenPO = new ScreenPO();
							screenPO.setName(programName);
							screenPO.setUpdateTime(new Date());
							screenPO.setScreenIndex((long)count);
							screenPO.setSerialNum(1l);
							screenPO.setDuration(resourcePOs.get(0).getDuration());
							screenPO.setMimsUuid(resourcePOs.get(0).getMimsUuid());
							screenPO.setPreviewUrl(resourcePOs.get(0).getPreviewUrl());
							screenPO.setSize(resourcePOs.get(0).getSize());
							screenPO.setProgramId(programId);
							screenPO.setType(resourcePOs.get(0).getType());
							screenPO.setMimetype(resourcePOs.get(0).getMimetype());
							screenPO.setIsRequired(false);
							screenPO.setStartTime(startTime);
							screenPO.setEndTime(endTime);
							if("VIDEO".equals(screenPO.getType())){
								screenPO.setContentType("视频资源");
							}else if("AUDIO".equals(screenPO.getType())){
								screenPO.setContentType("音频资源");
							}
							screenPOList.add(screenPO);
							count++;
						}
						if(i+1==scheduleListCopy.size()){
							//最后一个排期
							if(screenPOList.size()>0){
								screenDao.saveAll(screenPOList);
								screenPOList.clear();
								break;
							}
						}
					}else{
						if(screenPOList.size()>0){
							//保存当前排期下的节目单
							screenDao.saveAll(screenPOList);
							screenPOList.clear();
							break;
						}
					}
				}
				for(int j = 0;j<count;j++){
					scheduleListCopy.remove(0);
				}
			}
		}	
	}
}
