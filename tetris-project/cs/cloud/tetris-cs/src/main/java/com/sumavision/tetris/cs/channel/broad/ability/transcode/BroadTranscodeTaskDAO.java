package com.sumavision.tetris.cs.channel.broad.ability.transcode;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.orm.dao.BaseDAO;

@Transactional
@RepositoryDefinition(domainClass = BroadTranscodeTaskPO.class, idClass = Long.class)
public interface BroadTranscodeTaskDAO extends BaseDAO<BroadTranscodeTaskPO>{
	public List<BroadTranscodeTaskPO> findByChannelId(Long channelId);
	
	public List<BroadTranscodeTaskPO> findByChannelIdAndTranscodeType(Long channelId, String transcodeType);
	
	public BroadTranscodeTaskPO findByTaskId(String taskId);
	
	public void deleteByChannelId(Long channelId);
	
	public void deleteByTaskId(String taskId);
}
