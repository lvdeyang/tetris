package com.sumavision.tetris.bvc.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.bvc.command.group.dao.CommandGroupUserInfoDAO;
import com.sumavision.bvc.device.command.cast.CommandCastServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
import com.sumavision.bvc.device.command.common.CommandCommonUtil;
import com.sumavision.bvc.device.command.user.CommandUserServiceImpl;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.tetris.bvc.business.common.BusinessCommonService;
import com.sumavision.tetris.bvc.business.dao.GroupDAO;
import com.sumavision.tetris.bvc.business.terminal.hall.TerminalBundleConferenceHallPermissionDAO;
import com.sumavision.tetris.bvc.business.terminal.user.TerminalBundleUserPermissionDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundleDAO;
import com.sumavision.tetris.bvc.util.TetrisBvcQueryUtil;
import com.sumavision.tetris.websocket.message.WebsocketMessageService;

import lombok.extern.slf4j.Slf4j;

/**
 * PageTaskUtil<br/>
 * <p>分页工具类</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年9月8日 下午3:54:20
 */
@Slf4j
//@Transactional(rollbackFor = Exception.class)
@Service
public class PageTaskUtil {
	
	@Autowired
	private CommandGroupUserInfoDAO commandGroupUserInfoDao;	
	
	@Autowired
	private PageInfoDAO pageInfoDao;
	
	@Autowired
	private PageTaskDAO pageTaskDao;
	
	@Autowired
	private TerminalBundleDAO terminalBundleDao;
	
	@Autowired
	private TerminalBundleUserPermissionDAO terminalBundleUserPermissionDao;
	
	@Autowired
	private TerminalBundleConferenceHallPermissionDAO terminalBundleConferenceHallPermissionDao;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private BusinessCommonService businessCommonService;
	
	@Autowired
	private CommandCommonServiceImpl commandCommonServiceImpl;
	
	@Autowired
	private WebsocketMessageService websocketMessageService;
	
	@Autowired
	private CommandUserServiceImpl commandUserServiceImpl;
	
	@Autowired
	private CommandCastServiceImpl commandCastServiceImpl;

	@Autowired
	private CommandCommonUtil commandCommonUtil;

	@Autowired
	private TetrisBvcQueryUtil tetrisBvcQueryUtil;

	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private GroupDAO groupDao;
	
	/**
	 * 把目的相同的task抽取合并<br/>
	 * <p>为支持多个rect（虚拟源需求）</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年9月8日 下午4:00:03
	 * @param tasks
	 * @return （bundleId, task列表）
	 */
	public Map<String, List<PageTaskPO>> classifyBundleTasks(List<PageTaskPO> tasks){
		Map<String, List<PageTaskPO>> map = new HashMap<String, List<PageTaskPO>>();
		for(PageTaskPO task : tasks){
			String bundleId = task.getDstBundleId();
			List<PageTaskPO> bundleTasks = map.get(bundleId);
			if(bundleTasks == null){
				bundleTasks = new ArrayList<PageTaskPO>();
				map.put(bundleId, bundleTasks);
			}
			bundleTasks.add(task);
		}
		return map;
	}
}
