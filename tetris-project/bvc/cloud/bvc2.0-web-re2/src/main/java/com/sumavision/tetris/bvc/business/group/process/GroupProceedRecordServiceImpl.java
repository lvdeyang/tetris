package com.sumavision.tetris.bvc.business.group.process;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.bvc.device.group.dao.DeviceGroupAuthorizationDAO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.po.DeviceGroupProceedRecordPO;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.tetris.bvc.business.group.GroupMemberOnlineStatus;
import com.sumavision.tetris.bvc.business.group.GroupPO;
import com.sumavision.tetris.bvc.business.po.member.BusinessGroupMemberPO;

/**
 * 会议执行记录<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年10月19日 上午11:11:30
 */
@Service
public class GroupProceedRecordServiceImpl {

	@Autowired
	private GroupProceedRecordDAO groupProceedRecordDao;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Autowired
	private DeviceGroupAuthorizationDAO deviceGroupAuthorizationDao;
	
	/**
	 * 开会记录<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月19日 上午11:33:46
	 * @param group
	 * @throws Exception
	 */
	public void saveStart(
			GroupPO group
			) throws Exception{
		
		List<BusinessGroupMemberPO> members = group.getMembers();
		List<BusinessGroupMemberPO> onlineMembers = new ArrayList<BusinessGroupMemberPO>();
		for(BusinessGroupMemberPO member: members){
			if(GroupMemberOnlineStatus.ONLINE.equals(member.getOnlineStatus())){
				onlineMembers.add(member);
			}
		}
		GroupProceedRecordPO recordPO = new GroupProceedRecordPO();
		recordPO.setUserId(group.getUserId());
		recordPO.setStartTime(group.getStartTime());
		recordPO.setTotalMemberNumber(group.getMembers().size());
		recordPO.setOnlineMemberNumber(onlineMembers.size());
		recordPO.setGroupId(group.getId());
		recordPO.setGroupName(group.getName());
		groupProceedRecordDao.save(recordPO);
	}
	

	public void saveStop(
			GroupPO group
			) throws Exception{
				
		GroupProceedRecordPO recordPO = groupProceedRecordDao.findByGroupIdAndFinished(group.getId(), false);
		if(recordPO == null) return;
		recordPO.setEndTime(group.getEndTime());
		recordPO.setFinished(true);
		groupProceedRecordDao.save(recordPO);
	}
	
}
