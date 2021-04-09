package com.sumavision.bvc.device.monitor.mux;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.dao.WorkNodeDao;
import com.suma.venus.resource.pojo.WorkNodePO;
import com.suma.venus.resource.pojo.WorkNodePO.NodeType;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.PassByBO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.monitor.live.MonitorLiveDAO;
import com.sumavision.bvc.device.monitor.mux.exception.BusinessInvokingException;
import com.sumavision.bvc.device.monitor.playback.MonitorRecordPlaybackTaskDAO;

@Service
@Transactional(rollbackFor = Exception.class)
public class MuxService {

	@Autowired
	private WorkNodeDao workNodeDao;
	
	@Autowired
	private MonitorRecordPlaybackTaskDAO monitorRecordPlaybackTaskDao;
	
	@Autowired
	private MonitorLiveDAO monitorLiveDao;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	/**
	 * 端口复用控制<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月14日 下午5:09:03
	 * @param boolean isOpen 是否开启端口复用
	 * @param Long userId 当前业务用户
	 */
	public void switchMux(boolean isOpen, Long userId) throws Exception{
		
		LogicBO logic = null;
		
		/*Long taskNum = monitorRecordPlaybackTaskDao.count();
		if(taskNum > 0){
			throw new BusinessInvokingException();
		}
		
		taskNum = monitorLiveDao.count();
		if(taskNum > 0){
			throw new BusinessInvokingException();
		}*/
		
		List<WorkNodePO> workNodes = workNodeDao.findAll();
		if(workNodes!=null && workNodes.size()>0){
			for(WorkNodePO workNode:workNodes){
				if(NodeType.ACCESS_JV210.equals(workNode.getType()) || 
						NodeType.ACCESS_NETWORK.equals(workNode.getType())){
					if(logic == null) logic = new LogicBO().setUserId(userId.toString())
														   .setPass_by(new ArrayList<PassByBO>());
					PassByBO passBy = new PassByBO().setLayer_id(workNode.getNodeUid())
													.setType("set_mux")
													.setPass_by_content(new JSONObject());
					((JSONObject)passBy.getPass_by_content()).put("switch", isOpen);
					logic.getPass_by().add(passBy);
				}
			}
		}
		
		if(logic!=null && logic.getPass_by().size()>0){
			executeBusiness.execute(logic, "点播系统：设置端口复用");
		}
	}
	
}
