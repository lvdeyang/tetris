package com.sumavision.bvc.meeting.logic.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.bvc.meeting.logic.dao.CommonDao;
import com.sumavision.bvc.meeting.logic.po.OmcRecordPO;
import com.sumavision.bvc.meeting.logic.po.CombineAudioDstPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = OmcRecordPO.class, idClass = long.class)
public interface OmcRecordDao extends MetBaseDAO<OmcRecordPO> {
//	@Query(value="select s from CombineAudioDstPO s where s.uuid like %:uuid%")
	//public List<OmcRecordPO> findByUuid(String uuid);

//	@Query(value="select s from CombineAudioDstPO s where s.uuid like %:uuid%")
	public OmcRecordPO getByUuid(String uuid);
}
