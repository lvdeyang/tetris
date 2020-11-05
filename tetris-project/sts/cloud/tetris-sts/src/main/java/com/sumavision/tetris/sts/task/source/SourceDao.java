package com.sumavision.tetris.sts.task.source;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.sts.common.CommonConstants.ProtoType;
import com.sumavision.tetris.sts.common.CommonDao;
import com.sumavision.tetris.sts.task.source.SourcePO.SourceStatus;

@RepositoryDefinition(domainClass = SourcePO.class, idClass = Long.class)
public interface SourceDao extends CommonDao<SourcePO>{

	public List<SourcePO> findBySourceGroupId(Long sourceGroupId);
	
	public List<SourcePO> findByNetGroupId(Long netGroupId);
	
	public List<SourcePO> findBySourceUrl(String sourceUrl);

	public List<SourcePO> findBySourceUrlAndNetGroupIdAndIsMain(String sourceUrl,Long netGroupId,Integer isMain);

	public List<SourcePO> findBySourceName(String sourceName);

	List<SourcePO> findByProtoTypeAndSourceUrl(ProtoType protoType, String sourceUrl);

	List<SourcePO> findByProtoTypeAndSourceIpAndSourcePort(ProtoType protoType, String sourceIp, Integer sourcePort);

	//这里组播源有可能有多个？？
	//SourcePO findBySourceUrlAndNetGroupIdNot(String name , Long netGroupId);
	List<SourcePO> findBySourceUrlAndNetGroupId(String sourceUrl , Long netGroupId);
	    
	public List<SourcePO> findByDeviceId(Long deviceId);

	@Query(value = "select distinct s from SourcePO s left join s.programPOs p where s.isMain = 1 and"
			+" ((s.sourceUrl like %?1% or s.sourceName like %?1%) or (p.programName like %?1%))")
	public Page<SourcePO> findMainSourceByKeyword(String keyWord,Pageable pageRequest);
	
	@Query(value = "select count(*) from SourcePO s where s.isMain = 1 and s.sourceName = ?1 ")
	public  int findMainCountBySourceName(String sourceName);
	
	@Query(value = "select count(*) from SourcePO s where s.isMain = 0 and s.sourceName = ?1 ")
	public  int findBackupCountBySourceName(String sourceName);
	
	@Query(value = "select distinct s from SourcePO s left join s.programPOs p where s.isMain = 0 and"
			+" ((s.sourceUrl like %?1% or s.sourceName like %?1%) or (p.programName like %?1%))")
	public Page<SourcePO> findBackupSourceByKeyword(String keyWord,Pageable pageRequest);
	
	@Query(value = "select distinct s from SourcePO s left join s.programPOs p where s.isMain = 0 and"
			+" ((s.sourceUrl like %?1% or s.sourceName like %?1%) or (p.programName like %?1%))")
	public List<SourcePO> findBackupSourceByKeyword(String keyWord);
	
	@Query(value = "select distinct s from SourcePO s left join s.programPOs p where s.isMain = 1 and s.netGroupId = ?2 and"
			+" ((s.sourceUrl like %?1% or s.sourceName like %?1%) or (p.programName like %?1%))")
	public List<SourcePO> findMainSourceByKeywordAndNetGroupId(String keyWord,Long netGroupId);
	
	/**
	 * 查询所有创建过任务的源
	 * @return
	 */
	@Query(value = "select distinct s from SourcePO s,TaskLinkPO t where s.id = t.sourceId")
	public List<SourcePO> findByTasks();
	
	@Transactional(propagation = Propagation.REQUIRED)
    @Modifying(clearAutomatically = true)
	@Query(value = "update SourcePO s set s.status = ?1 where s.id = ?2")
	public Integer updateSourceStatus(SourceStatus sourceStatus, Long sourceId);
	
    /**
     * 
     * <pre>
     * simple introduction
     * </pre>
     *
     * @param isMain
     * @param keyWord
     * @param groupIds
     * @param netGroupIds
     * @param pageRequest
     * @return
     */
    @Query(value = "select distinct s from SourcePO s left join s.programPOs p where s.isMain = ?1 and"
                +" ((s.sourceUrl like %?2% or s.sourceName like %?2%) or (p.programName like %?2%)) and s.sourceGroupId in ?3 and s.netGroupId in ?4")
    public Page<SourcePO> findSourceByKeywordandGroupIdNetgroupId(Integer isMain,String keyWord,List<Long> groupIds,List<Long> netGroupIds,Pageable pageRequest);
    
    /**
     * 查询sdi采集卡的输入源
     */
    @Query(value = "select distinct s from SourcePO s left join s.programPOs p where s.isMain = ?1 and s.sourceType = 'SDI' and"
            +" ((s.sourceUrl like %?2% or s.sourceName like %?2%) or (p.programName like %?2%)) and s.sourceGroupId in ?3")
    public Page<SourcePO> findSDISourceByKeywordandGroupId(Integer isMain,String keyWord,List<Long> groupIds,Pageable pageRequest);
    
    
    /**
     * 
     * <pre>
     * 关键字查找，不分页
     * </pre>
     *
     * @param keyWord
     * @param isMain
     * @return
     */
    @Query(value = "select distinct s from SourcePO s left join s.programPOs p where s.isMain = ?2 and"
            +" ((s.sourceUrl like %?1% or s.sourceName like %?1%) or (p.programName like %?1%))")
    public List<SourcePO> findByKeyword(String keyWord,Integer isMain);

    /**
     * 根据节目po的id查询出所属sourcePO
     * @param programId
     * @return
     */
    @Query(value = "select distinct s from SourcePO s left join s.programPOs p where p.id = ?1")
    public SourcePO findByProgramId(Long programId);
    
    /**
     * 源统计：总数量、ABNORMAL、NORMAL、REFRESHING、NEED_REFRESH
     * @return count
     */
    @Query(value = "select count(*) from SourcePO s where s.isMain = 1")
	public int findMainCount();
    @Query(value = "select count(*) from SourcePO s where s.isMain = 1 and s.status = 'ABNORMAL' ")
	public int findMainAbnormalCount();
    @Query(value = "select count(*) from SourcePO s where s.isMain = 1 and s.status = 'NORMAL' ")
	public int findMainNormalCount();
    @Query(value = "select count(*) from SourcePO s where s.isMain = 1 and s.status = 'REFRESHING' ")
	public int findMainRefreshCount();
    @Query(value = "select count(*) from SourcePO s where s.isMain = 1 and s.status = 'NEED_REFRESH' ")
	public int findMainNeedRefreshCount();
    
    @Query(value = "select count(*) from SourcePO s where s.isMain = 0 ")
	public int findBackupCount();
    @Query(value = "select count(*) from SourcePO s where s.isMain = 0 and s.status = 'ABNORMAL' ")
	public int findBackupAbnormalCount();
    @Query(value = "select count(*) from SourcePO s where s.isMain = 0 and s.status = 'NORMAL' ")
	public int findBackupNormalCount();
    @Query(value = "select count(*) from SourcePO s where s.isMain = 0 and s.status = 'REFRESHING' ")
	public int findBackupRefreshCount();
    @Query(value = "select count(*) from SourcePO s where s.isMain = 0 and s.status = 'NEED_REFRESH' ")
	public int findBackupNeedRefreshCount();

	@Transactional(propagation = Propagation.REQUIRED)
	@Modifying(clearAutomatically = true)
	@Query(value = "update SourcePO s set s.status = 'NORMAL' where s.status = 'ABNORMAL'")
	public Integer updateAbnormalSourceStatus();
}
