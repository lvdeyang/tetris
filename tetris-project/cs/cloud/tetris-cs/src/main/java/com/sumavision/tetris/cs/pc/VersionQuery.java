package com.sumavision.tetris.cs.pc;

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
public class VersionQuery {
	@Autowired
	private VersionDAO versionDAO;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 获取pc终端版本信息<author by mr.>
	 * @return
	 * @throws Exception
	 */
	public VersionVO getVersion() throws Exception {
	
		List<VersionPO> versionPOs=versionDAO.findAll();
		VersionPO versionPO=new VersionPO();
		if(versionPOs!=null&&!versionPOs.isEmpty()){
			versionPO=versionPOs.get(0);
		}
		return new VersionVO().set(versionPO);
	}
	
	
}
