package com.sumavision.tetris.stream.transcoding.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.stream.transcoding.task.exception.TaskMessageOutputIpAndPortNotFoundException;

@Component
public class StreamTranscodingTaskOutputPermissionQuery {
	@Autowired
	private StreamTranscodingTaskOutputPermissionDAO streamTranscodingTaskOutputPermissionDAO;
	
	public Long getNextOutputId() throws Exception {
		List<StreamTranscodingTaskOutputPermissionPO> permissionPOs = streamTranscodingTaskOutputPermissionDAO.findAll();
		
		Long newId = 30l;
		if (permissionPOs != null && !permissionPOs.isEmpty()) {
			newId = permissionPOs.get(permissionPOs.size() - 1).getOutputId() + 1;
		}
		
		return newId;
	}
	
	public StreamTranscodingTaskOutputPermissionVO queryByOutputIpAndPort(String ip, String port) throws Exception{
		StreamTranscodingTaskOutputPermissionPO permissionPO = streamTranscodingTaskOutputPermissionDAO.findByOutputIpAndOutputPort(ip, port);
		
		if (permissionPO == null) throw new TaskMessageOutputIpAndPortNotFoundException(ip, port);
		
		return new StreamTranscodingTaskOutputPermissionVO().set(permissionPO);
	}
}
