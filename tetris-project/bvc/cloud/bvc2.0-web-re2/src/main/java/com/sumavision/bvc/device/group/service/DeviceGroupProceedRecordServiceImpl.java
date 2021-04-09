package com.sumavision.bvc.device.group.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumavision.bvc.device.group.bo.BundleBO;
import com.sumavision.bvc.device.group.dao.DeviceGroupAuthorizationDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupProceedRecordDAO;
import com.sumavision.bvc.device.group.po.DeviceGroupAuthorizationPO;
import com.sumavision.bvc.device.group.po.DeviceGroupMemberPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.po.DeviceGroupProceedRecordPO;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;

/**
 * 会议执行记录<br/>
 * <p>详细描述</p>
 * <b>作者:</b>zsy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年10月19日 上午11:11:30
 */
@Service("com.sumavision.bvc.device.group.DeviceGroupProceedRecordServiceImpl")
public class DeviceGroupProceedRecordServiceImpl {

	@Autowired
	private DeviceGroupProceedRecordDAO deviceGroupProceedRecordDao;
	
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
			DeviceGroupPO group
			) throws Exception{
		
		Set<DeviceGroupMemberPO> members = group.getMembers();
		List<BundleBO> bundles = new ArrayList<BundleBO>();
		for(DeviceGroupMemberPO member: members){
			bundles.add(new BundleBO().set(member));
		}
		bundles = resourceQueryUtil.appendBundleOnlineStatusIntoBundleBos(bundles);
		int onlineCount = BundleBO.countOnline(bundles);
		DeviceGroupAuthorizationPO auth = deviceGroupAuthorizationDao.findByGroupUuid(group.getUuid());
		
		DeviceGroupProceedRecordPO recordPO = new DeviceGroupProceedRecordPO();
		recordPO.setUserId(group.getUserId());
		recordPO.setStartTime(new Date());
		recordPO.setAuthorizationMemberNumber(auth==null?0:auth.getAuthorizationMembers().size());
		recordPO.setTotalMemberNumber(group.getMembers().size());
		recordPO.setOnlineMemberNumber(onlineCount);
		recordPO.setGroupId(group.getId());
		recordPO.setGroupName(group.getName());
		deviceGroupProceedRecordDao.save(recordPO);
	}
	

	public void saveStop(
			DeviceGroupPO group
			) throws Exception{
				
		DeviceGroupProceedRecordPO recordPO = deviceGroupProceedRecordDao.findByGroupIdAndFinished(group.getId(), false);
		if(recordPO == null) return;
		recordPO.setEndTime(new Date());
		recordPO.setFinished(true);
		deviceGroupProceedRecordDao.save(recordPO);
	}
	
}
