package com.sumavision.signal.bvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suma.venus.resource.service.ResourceService;
import com.sumavision.signal.bvc.entity.dao.TerminalBindRepeaterDAO;

/**
 * 业务恢复流程<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年7月8日 下午1:17:44
 */
@Service
@Transactional
public class ResumeBusinessService {
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private TerminalBindRepeaterDAO terminalBindRepeaterDao;
	
	public void bundleResumeProcess(){
		
	}
}
