package com.sumavision.bvc.meeting.logic.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.bvc.meeting.logic.po.CombineVideoScreenPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

//@RepositoryDefinition(domainClass = CombineVideoScreenPO.class, idClass = long.class)
public interface CombineVideoScreenDao {//extends MetBaseDAO<CombineVideoScreenPO> {
	public CombineVideoScreenPO getByUuid(String uuid);
	public List<CombineVideoScreenPO> findByDstUuid(String dstUuid);
}
