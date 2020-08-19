package com.sumavision.bvc.meeting.logic.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import com.sumavision.bvc.meeting.logic.po.CombineVideoDstPO;
import com.sumavision.bvc.meeting.logic.po.OutConnMediaMuxPO;
import com.sumavision.tetris.orm.dao.MetBaseDAO;

@RepositoryDefinition(domainClass = OutConnMediaMuxPO.class, idClass = long.class)
public interface OutConnMediaMuxDao extends MetBaseDAO<OutConnMediaMuxPO>{
	
//	@Query(value="select s from CombineVideoDstPO s where s.uuid = %:uuid%")
	//public List<OutConnMediaMuxPO> findByUuid(@Param("uuid") String uuid);

//	@Query(value="select s from CombineVideoDstPO s where s.uuid like %:uuid%")
	public OutConnMediaMuxPO getByUuid(@Param("uuid") String uuid);
	
//	@Query(value="delete CombineVideoDstPO where uuid like %:uuid%")
//	public void deleteByUuid(@Param("uuid") String uuid);
	
	public List<OutConnMediaMuxPO> getByRecordUuid(@Param("recordUuid") String recordUuid);
}
