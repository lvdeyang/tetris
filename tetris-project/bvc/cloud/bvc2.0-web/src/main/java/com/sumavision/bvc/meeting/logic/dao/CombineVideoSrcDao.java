package com.sumavision.bvc.meeting.logic.dao;

import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.bvc.meeting.logic.po.CombineVideoScreenPO;
import com.sumavision.bvc.meeting.logic.po.CombineVideoSrcPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

//@RepositoryDefinition(domainClass = CombineVideoScreenPO.class, idClass = long.class)
public interface CombineVideoSrcDao {//extends MetBaseDAO<CombineVideoSrcPO> {
	public CombineVideoSrcPO getByUuid(String uuid);
//	public List<CombineVideoSrcPO> findByDstUuid(String dstUuid);
}