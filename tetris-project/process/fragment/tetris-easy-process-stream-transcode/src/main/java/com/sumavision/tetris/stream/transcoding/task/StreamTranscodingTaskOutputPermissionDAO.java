package com.sumavision.tetris.stream.transcoding.task;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = StreamTranscodingTaskOutputPermissionPO.class, idClass = Long.class)
public interface StreamTranscodingTaskOutputPermissionDAO extends BaseDAO<StreamTranscodingTaskOutputPermissionPO>{
	public StreamTranscodingTaskOutputPermissionPO findByOutputIpAndOutputPort(String outputIp, String outputPort);
	
	@Query(value = "select output_port from TETRIS_STREAM_TRANSCODING_TASK_OUTPUT_PERMISSION where output_ip = ?1", nativeQuery = true)
	public List<String> findByOutputIp(String outputIp);
}
