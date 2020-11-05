package com.sumavision.tetris.cs.template;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.cs.channel.BroadWay;
import com.sumavision.tetris.cs.channel.ChannelPO;
import com.sumavision.tetris.mims.app.media.avideo.MediaAVideoQuery;
import com.sumavision.tetris.mims.app.media.avideo.MediaAVideoVO;
import com.sumavision.tetris.mims.app.media.live.MediaPushLiveQuery;
import com.sumavision.tetris.mims.app.media.live.MediaPushLiveVO;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureQuery;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureVO;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamQuery;
import com.sumavision.tetris.mims.app.media.stream.audio.MediaAudioStreamVO;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamQuery;
import com.sumavision.tetris.mims.app.media.stream.video.MediaVideoStreamVO;




@Service
@Transactional(rollbackFor = Exception.class)
public class ChannelTemplateService {
	
	@Autowired
    ChannelTemplateDao channelTemplateDao;
	@Autowired
	TemplateProgrameDao templateProgrameDao;
	
	public Map<String, Object> findAll(Integer currentPage, Integer pageSize) throws Exception {
		List<ChannelTemplatePO> temps;
		Pageable page = new PageRequest(currentPage - 1, pageSize);
		Page<ChannelTemplatePO> pageTemps = channelTemplateDao.findAll(page);
		temps = pageTemps.getContent();
		List<ChannelTemplateVo> tempVOs = ChannelTemplateVo.getConverter(ChannelTemplateVo.class).convert(temps, ChannelTemplateVo.class);
		return new HashMapWrapper<String, Object>().put("rows", tempVOs)
				.put("total", pageTemps.getTotalElements())
				.getMap();
	}
	
	
	public List<ChannelTemplateVo> findAllNopage() throws Exception {
		List<ChannelTemplatePO> temps= channelTemplateDao.findAll();
		List<ChannelTemplateVo> tempVOs = ChannelTemplateVo.getConverter(ChannelTemplateVo.class).convert(temps, ChannelTemplateVo.class);
		return tempVOs;
	}
	
	public ChannelTemplatePO add(String name) throws Exception {
	    ChannelTemplatePO channelTemplatePo=new ChannelTemplatePO();
	    channelTemplatePo.setName(name);
		channelTemplateDao.save(channelTemplatePo);
		return channelTemplatePo;
	}
	
	public void remove(Long tempId) throws Exception {
		channelTemplateDao.delete(tempId);
	}
	
	public Map<String, Object> findAllPros(Integer currentPage, Integer pageSize,long tempId) throws Exception {
		List<TemplateProgramePO> temppros;
		Pageable page = new PageRequest(currentPage - 1, pageSize);
		Page<TemplateProgramePO> pageTemppros = templateProgrameDao.PagefindAllByTemplateId(tempId,page);
		temppros = pageTemppros.getContent();
		List<TemplateProgrameVo> tempproVOs = ChannelTemplateVo.getConverter(TemplateProgrameVo.class).convert(temppros, TemplateProgrameVo.class);
		return new HashMapWrapper<String, Object>().put("rows", tempproVOs)
				.put("total", pageTemppros.getTotalElements())
				.getMap();
	}
	

	
	public TemplateProgramePO addpro(long templateId,
			Date startTime,
			Date endTime,
			ProgrameType programeType,
			String labelIds,
			String labelNames,
			String mimsId,
			String mimsName,
			String url
			) throws Exception {
	    TemplateProgramePO templateProgramePO=new TemplateProgramePO();
	    templateProgramePO.setTemplateId(templateId);
	    templateProgramePO.setStartTime(startTime);
	    templateProgramePO.setEndTime(endTime);
	    templateProgramePO.setProgrameType(programeType);
	    templateProgramePO.setLabelIds(labelIds);
	    templateProgramePO.setLabelNames(labelNames);
	    templateProgramePO.setMimsId(mimsId);
	    templateProgramePO.setMimsName(mimsName);
	    templateProgramePO.setUrl(url);
	    templateProgrameDao.save(templateProgramePO);
	    return templateProgramePO;
	}
	
	public void removepro(Long tempproId) throws Exception {
		templateProgrameDao.delete(tempproId);
	}
	
	@Autowired
	private MediaAVideoQuery mediaAVideoQuery;
	@Autowired
	private MediaPushLiveQuery mediaPushLiveQuery;
	
	@Autowired
	private MediaPictureQuery mediaPictureQuery;
	
	@Autowired
	private MediaVideoStreamQuery mediaVideoStreamQuery;
	
	@Autowired
	private MediaAudioStreamQuery mediaAudioStreamQuery;
	
	public List<MediaAVideoVO> getMIMSResources() throws Exception {
		List<MediaAVideoVO> mimsVideoList = mediaAVideoQuery.loadAll();
		return mimsVideoList;
	}
	
	public List<MediaPushLiveVO> getMIMSLiveResources() throws Exception {
		
		List<MediaPushLiveVO> mimsLiveList = mediaPushLiveQuery.loadAll();
		return mimsLiveList;
	}
	
	public List<MediaPictureVO> getMIMSPictureResources() throws Exception {
		List<MediaPictureVO> mimsPictureList = mediaPictureQuery.loadAll();
		return mimsPictureList;
	}
	
	public List<MediaVideoStreamVO> getMIMSVideoStreamResources() throws Exception {
		List<MediaVideoStreamVO> mimsVideoStreamList = mediaVideoStreamQuery.loadAll();
		return mimsVideoStreamList;
	}
	
	public List<MediaAudioStreamVO> getMIMSAudioStreamResources() throws Exception {
		List<MediaAudioStreamVO> mimsAudioStreamList = mediaAudioStreamQuery.loadAll();
		return mimsAudioStreamList;
	}
	
}
