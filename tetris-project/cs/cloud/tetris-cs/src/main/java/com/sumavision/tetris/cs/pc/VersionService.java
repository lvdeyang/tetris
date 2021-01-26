package com.sumavision.tetris.cs.pc;

import java.util.ArrayList;
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
import com.sumavision.tetris.cs.program.ProgramService;
import com.sumavision.tetris.cs.program.ProgramVO;
import com.sumavision.tetris.cs.program.ScreenVO;
import com.sumavision.tetris.cs.schedule.api.server.ApiServerScheduleVO;
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
public class VersionService {
	
	@Autowired
	private VersionQuery versionQuery;
	
	@Autowired
	private VersionDAO versionDAO;
	
	
	/**
	 * 修改pc终端版本信息<author by mr.>
	 * @param version 版本号
	 * @param url 下载地址
	 * @return
	 * @throws Exception
	 */
	public VersionVO edit(String version,String url,String name,long size) throws Exception{
		VersionVO versionVO=versionQuery.getVersion();
		VersionPO versionPO=new VersionPO();
		if(versionVO.getId()!=null){
			versionPO=versionDAO.findById(versionVO.getId());
		}
		versionPO.setVersion(version);
		versionPO.setUrl(url);
		versionPO.setName(name);
		versionPO.setSize(size);
		versionDAO.save(versionPO);
		return new VersionVO().set(versionPO);
	}
	
	
}
