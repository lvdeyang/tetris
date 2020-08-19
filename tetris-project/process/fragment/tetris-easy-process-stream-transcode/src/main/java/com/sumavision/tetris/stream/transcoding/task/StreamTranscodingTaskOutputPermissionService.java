package com.sumavision.tetris.stream.transcoding.task;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.streamTranscoding.api.server.OutParamVO;

@Service
@Transactional(rollbackFor = Exception.class)
public class StreamTranscodingTaskOutputPermissionService {
	@Autowired
	private StreamTranscodingTaskOutputPermissionQuery streamTranscodingTaskOutputPermissionQuery;
	
	@Autowired
	private StreamTranscodingTaskOutputPermissionDAO streamTranscodingTaskOutputPermissionDAO;
	
	public List<StreamTranscodingTaskOutputPermissionVO> addPermissions(List<OutParamVO> outParamVOs, Long messageId) throws Exception{
		List<StreamTranscodingTaskOutputPermissionPO> permissionPOs = new ArrayList<StreamTranscodingTaskOutputPermissionPO>();
		Long newOutputId = streamTranscodingTaskOutputPermissionQuery.getNextOutputId();
		for (OutParamVO outParam : outParamVOs) {
			StreamTranscodingTaskOutputPermissionPO permissionPO = new StreamTranscodingTaskOutputPermissionPO();
			String[] urlSplit = outParam.getOutputUrl().split(":");
			
			permissionPO.setMessageId(messageId);
			permissionPO.setOutputId(newOutputId++);
			permissionPO.setOutputIp(urlSplit[0]);
			permissionPO.setOutputPort(urlSplit[1]);
			permissionPOs.add(permissionPO);
		}
		streamTranscodingTaskOutputPermissionDAO.save(permissionPOs);
		return StreamTranscodingTaskOutputPermissionVO.getConverter(StreamTranscodingTaskOutputPermissionVO.class).convert(permissionPOs, StreamTranscodingTaskOutputPermissionVO.class);
	}
}
