package com.sumavision.bvc.device.group.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.bvc.device.group.bo.CodecParamBO;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.dao.DeviceGroupAuthorizationDAO;
import com.sumavision.bvc.device.group.dao.PublishStreamDAO;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.device.group.enumeration.GroupStatus;
import com.sumavision.bvc.device.group.enumeration.RecordType;
import com.sumavision.bvc.device.group.exception.DeviceGroupHasNotStartedException;
import com.sumavision.bvc.device.group.po.DeviceGroupAuthorizationPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplGearsPO;
import com.sumavision.bvc.device.group.po.DeviceGroupAvtplPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.po.PublishStreamPO;
import com.sumavision.bvc.device.group.po.RecordPO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Transactional(rollbackFor = Exception.class)
@Service
public class PublishStreamServiceImpl {
	
	@Autowired
	private PublishStreamDAO publishStreamDao;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private DeviceGroupAuthorizationDAO deviceGroupAuthorizationDao;
	
	@Autowired
	private RecordServiceImpl recordServiceImpl;
	
	@Autowired
	private AuthorizationServiceImpl authorizationServiceImpl;

	public PublishStreamPO publishRtmp(RecordPO record, String name, String url) throws Exception{
		return publishRtmp(record, name, url, true, true);
	}
	
	/**
	 * @Title: 发布RTMP<br/> 
	 * @param group 设备组
	 * @param doPersistence 是否做数据持久化
	 * @param doProtocal 是否调用逻辑层
	 * @throws Exception  
	 * @return LogicBO 协议数据 
	 */
	public PublishStreamPO publishRtmp(
			RecordPO record,
			String name,
			String url,
			boolean doPersistence,
			boolean doProtocal) throws Exception{
		
		DeviceGroupPO group = record.getGroup();
		if(GroupStatus.STOP.equals(group.getStatus())){
			throw new DeviceGroupHasNotStartedException(group.getId(), group.getName());
		}
		
		//协议数据
		LogicBO logic = new LogicBO();
		logic.setUserId(group.getUserId().toString());
		
		//rtmp转为hls
		String url1 = url.replace("rtmp", "http");
		String url2 = url1.replace("1935", "8080");
		StringBufferWrapper sBuffer = new StringBufferWrapper().append(url2).append(".m3u8");
		
		PublishStreamPO publishStream = new PublishStreamPO();
		publishStream.setRecord(record);
		publishStream.setUrl(url);
		publishStream.setTransferUrl(sBuffer.toString());
		if(null == record.getPublishStreams()) record.setPublishStreams(new HashSet<PublishStreamPO>());
		record.getPublishStreams().add(publishStream);	
		
		publishStreamDao.save(publishStream);
		
		String groupUuid = group.getUuid();
		DeviceGroupAuthorizationPO authorizationPO = deviceGroupAuthorizationDao.findByGroupUuid(groupUuid);
		//如果该会议没有权限就新建一个
		if(null == authorizationPO){
			List<String> bundleIds = new ArrayList<String>();
			try {
				authorizationPO = authorizationServiceImpl.save(group.getId(), bundleIds);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//然后在回调中（MimsResourceCallBack）添加了一条RecordLiveChannelPO
				
//		Set<DeviceGroupRecordSchemePO> schemes = group.getRecordSchemes();
//		if(schemes==null || schemes.size()<=0) return logic;
//		
//		//地区分类
//		String regionId = group.getDicRegionId();
//		String programId = group.getDicProgramId();
//
//		
		//参数模板
		DeviceGroupAvtplPO avtpl = group.getAvtpl();
		DeviceGroupAvtplGearsPO currentGear = queryUtil.queryCurrentGear(group);
		CodecParamBO codec = new CodecParamBO().set(avtpl, currentGear);
		
		List<PublishStreamPO> pos = new ArrayList<PublishStreamPO>();
		pos.add(publishStream);
		logic.setPublishStreamSet(pos, codec);
		
		if(doProtocal){
			executeBusiness.execute(logic, "发布直播：");
		}
		
		return publishStream;
	}
	
	public PublishStreamPO autoPublishRtmp(DeviceGroupPO group, String name, String url) throws Exception{
		
		//创建一个观众角色的录制
		List<RecordPO> recordPOs = recordServiceImpl.runRecordScheme(group, group.getName()+"-会议直播", "", false, false, RecordType.PUBLISH);
		
		for(RecordPO recordPO : recordPOs){
			if(recordPO.getRecordId().endsWith(ChannelType.VIDEODECODE1.toString())){
				PublishStreamPO publishStream = publishRtmp(recordPO, "", url);
				return publishStream;
			}
		}
		return null;
	}
	
}
