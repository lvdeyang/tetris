package com.sumavision.tetris.sts.task.source;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.sts.common.CommonDao;


@RepositoryDefinition(domainClass = ProgramPO.class, idClass = Long.class)
public interface ProgramDao extends CommonDao<ProgramPO> {

	List<ProgramPO> findByBackupSourceIdAndBackupProgramNum(Long backupSourceId,Integer backupProgramNum);
	
	List<ProgramPO> findByBackupSourceId(Long backupSourceId);

	ProgramPO findTopByNodeId(Long nodeId);
	
	List<ProgramPO> findByProgramNumAndInputId(Integer programNum,Long inputId);
	
	ProgramPO findTopByProgramNumAndInputIdAndCardNum(Integer programNum,Long inputId,Integer cardNum);
	
	List<ProgramPO> findByProgramNumAndInputIdAndCardNum(Integer programNum,Long inputId,Integer cardNum);
	
	public List<ProgramPO> findByInputId(Long inputId);

	public List<ProgramPO> findByInputIdAndProgramNum(Long inputId,Integer programNum);


	@Query(value = "select count(*) from ProgramPO p where p.backupSourceId = ?1 ")
	public int findCountByBackupSourceId(Long backupSourceId);
	
    @Query(value = "select * from program p where p.sourceId = :sourceId and p.programNum = :programNum", nativeQuery = true)
    ProgramPO findBySourceIdAndNum(@Param("sourceId") Long sourceId,
            @Param("programNum") Integer programNum);
    
    @Transactional(propagation = Propagation.REQUIRED)
    @Modifying(clearAutomatically = true)
    @Query(value = "update program set backupSourceId =:backupSourceId, backupProgramNum =:backupProgramNum where sourceId = :mainSourceId and programNum = :mainProgramNum", nativeQuery = true)
    int updateBound(
            @Param("backupSourceId") Long backupSourceId,
            @Param("backupProgramNum") Long backupProgramNum,
            @Param("mainSourceId") Long mainSourceId,
            @Param("mainProgramNum") Long mainProgramNum);
    
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("delete from ProgramPO where inputId=?1")
    public int deleteByInputId(Long inputId);

}
