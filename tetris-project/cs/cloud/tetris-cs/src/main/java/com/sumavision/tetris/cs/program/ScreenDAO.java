package com.sumavision.tetris.cs.program;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ScreenPO.class, idClass = Long.class)
public interface ScreenDAO extends BaseDAO<ScreenPO>{

	@Query(value = "SELECT * FROM TETRIS_CS_SCREEN WHERE PROGRAM_ID LIKE ?1 ORDER BY SCREEN_INDEX", nativeQuery = true)
	public List<ScreenPO> findByProgramId(Long reg1);
	
	@Query(value = "SELECT * FROM TETRIS_CS_SCREEN WHERE PROGRAM_ID LIKE ?1 AND MIMS_UUID LIKE ?2", nativeQuery = true)
	public List<ScreenPO> findByProgramIdAndMimsUuid(Long programId,String mimsUuid);
	
	public List<ScreenPO> findByProgramIdAndResourceId(Long programId,Long resourceId);
	
	@Query(value = "SELECT * FROM TETRIS_CS_SCREEN WHERE PROGRAM_ID LIKE ?1 AND SERIAL_NUM LIKE ?2", nativeQuery = true)
	public List<ScreenPO> findByProgramIdAndSerialNum(Long programId,Long serialNum);
	
//	@Query(value = "DELETE * FROM TETRIS_CS_SCREEN WHERE PROGRAM_ID LIKE ?1", nativeQuery = true)
//	public void deleteByPRogramId(Long reg1);
}
