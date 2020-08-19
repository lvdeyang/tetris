package com.sumavision.bvc.device.group.service;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupMemberDAO;
import com.sumavision.bvc.device.group.enumeration.GroupType;
import com.sumavision.bvc.device.group.exception.AllRunningMeetingsNumberOfMembersOverLimitException;
import com.sumavision.bvc.device.group.exception.SingleMeetingNumberOfMembersOverLimitException;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.meeting.logic.ExecuteBusiness;

/**
 * @ClassName: 统计和成员数量，超出限制时抛出异常，“监控室”不限制人数
 * @author zsy
 * @date 2019年3月18日 上午9:46:30 
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class NumberOfMembersServiceImpl {
	
	int singleMeetingLimit = 100;
	int allRunningMeetingLimit = 100;
	
	@Autowired
	DeviceGroupDAO deviceGroupDao;
	
	@Autowired
	DeviceGroupMemberDAO deviceGroupMemberDao;

	/**
	 * 某个会议增加成员时，校验人数
	 * @param group
	 * @param addNumber
	 * @return
	 * @throws Exception
	 */
	public boolean checkOneMeetingAddMember(DeviceGroupPO group, int addNumber) throws Exception {
		int existNumber = group.getMembers().size();
		int totalNumber = existNumber + addNumber;
		return checkOneMeeting(group.getType(), totalNumber);
	}
	

	/**
	 * 校验单个会议的总人数
	 * @param totalNumber
	 * @return
	 * @throws Exception
	 */
	public boolean checkOneMeeting(GroupType groupType, int totalNumber) throws Exception {
		if(!GroupType.MONITOR.equals(groupType) && totalNumber > singleMeetingLimit){
			throw new SingleMeetingNumberOfMembersOverLimitException(singleMeetingLimit);
		}
		return true;
	}

	/**
	 * 校验正在进行中的会议总人数
	 * @param addNumber 新增的人数，启动会议时为“会议人数”，进行中的会议添加成员时为“新成员人数”
	 * @return
	 * @throws Exception
	 */
	public boolean checkAllMeeting(GroupType groupType, int addNumber) throws Exception {
		if(GroupType.MONITOR.equals(groupType)){
			return true;
		}
		int runningNumber = deviceGroupMemberDao.getNumberOfMembersOfRunningMeeting();
		if(runningNumber + addNumber > allRunningMeetingLimit){
			throw new AllRunningMeetingsNumberOfMembersOverLimitException(allRunningMeetingLimit);
		}
		return true;
	}
	
	/**
	 * 配置文件 number.properties 中的两个参数
	 * singleMeetingLimit 单个会议的总人数限制
	 * allRunningMeetingLimit 所有进行中的会议的总人数限制
	 */
	@Autowired
	public void getConfig() {
		try {
			Properties prop = new Properties();
			prop.load(ExecuteBusiness.class.getClassLoader().getResourceAsStream("number.properties"));
			singleMeetingLimit = Integer.parseInt(prop.getProperty("singleMeetingLimit"));
			allRunningMeetingLimit = Integer.parseInt(prop.getProperty("allRunningMeetingLimit"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
