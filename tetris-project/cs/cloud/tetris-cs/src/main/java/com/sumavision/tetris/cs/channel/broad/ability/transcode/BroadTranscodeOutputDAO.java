package com.sumavision.tetris.cs.channel.broad.ability.transcode;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.orm.dao.BaseDAO;

@Transactional
@RepositoryDefinition(domainClass = BroadTranscodeOutputPO.class, idClass = Long.class)
public interface BroadTranscodeOutputDAO extends BaseDAO<BroadTranscodeOutputPO>{
	public List<BroadTranscodeOutputPO> findByChannelId(Long channelId);
	
	public void deleteByChannelIdAndOutputUrlIn(Long channelId, List<String> outputUrl);
}
