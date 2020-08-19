package com.sumavision.bvc.meeting.logic.dao;

import java.util.List;

import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.bvc.meeting.logic.dao.CommonDao;
import com.sumavision.bvc.meeting.logic.po.CombineAudioDstPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = CombineAudioDstPO.class, idClass = long.class)
public interface CombineAudioDstDao extends MetBaseDAO<CombineAudioDstPO> {
//	@Query(value="select s from CombineAudioDstPO s where s.uuid like %:uuid%")
	//public List<CombineAudioDstPO> findByUuid(String uuid);

//	@Query(value="select s from CombineAudioDstPO s where s.uuid like %:uuid%")
	public CombineAudioDstPO getByUuid(String uuid);
}
