package com.sumavision.bvc.meeting.logic.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.bvc.meeting.logic.po.CombineAudioSrcPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CombineAudioSrcPO.class, idClass = long.class)
public interface CombineAudioSrcDao extends MetBaseDAO<CombineAudioSrcPO> {

	public CombineAudioSrcPO getByUuid(String uuid);
	public List<CombineAudioSrcPO> findByDstUuid(String dstUuid);
}
