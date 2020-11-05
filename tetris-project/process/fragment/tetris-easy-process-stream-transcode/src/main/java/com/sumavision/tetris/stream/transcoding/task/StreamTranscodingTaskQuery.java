package com.sumavision.tetris.stream.transcoding.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.stream.transcoding.task.exception.TaskMessageIdNotFoundException;

@Component
public class StreamTranscodingTaskQuery {
	@Autowired
	private StreamTranscodingTaskDAO streamTranscodingTaskDAO;
	
	public Long getNextInputId() throws Exception{
		List<StreamTranscodingTaskPO> taskPOs = streamTranscodingTaskDAO.findAll();
		
		Long newId = 120l;
		if (taskPOs != null && !taskPOs.isEmpty()) {
			newId = taskPOs.get(taskPOs.size() - 1).getInputId() + 1;
		}
		
		return newId;
	}
	
	public StreamTranscodingTaskVO questByMessageId(Long messageId) throws Exception{
		StreamTranscodingTaskPO taskPO = streamTranscodingTaskDAO.findOne(messageId);
		
		if (taskPO == null) throw new TaskMessageIdNotFoundException(messageId);
		
		return new StreamTranscodingTaskVO().set(taskPO);
	}
}
