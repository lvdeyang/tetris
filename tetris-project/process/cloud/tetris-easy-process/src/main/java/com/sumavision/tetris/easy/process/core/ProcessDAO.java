package com.sumavision.tetris.easy.process.core;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = ProcessPO.class, idClass = Long.class)
public interface ProcessDAO extends BaseDAO<ProcessPO>{

	/**
	 * 根据类型分页查询流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月13日 上午11:50:15
	 * @param ProcessType type 流程类型
	 * @param Pageable page 分页信息
	 * @return List<ProcessPO> 流程列表
	 */
	public Page<ProcessPO> findByType(ProcessType type, Pageable page);
	
	/**
	 * 根据类型统计流程数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月13日 上午11:51:31
	 * @param ProcessType type 流程类型
	 * @return long 流程数量
	 */
	public long countByType(ProcessType type);
	
	/**
	 * 根据自定义流程id查询流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月24日 下午5:38:11
	 * @param String processId 流程id
	 * @return ProcessPO 流程数据
	 */
	public ProcessPO findByProcessId(String processId);
	
	/**
	 * 查询公司下的流程<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月11日 下午1:10:41
	 * @param String companyId 公司id
	 * @param Pageable page 分页信息
	 * @return List<ProcessPO> 流程列表
	 */
	@Query(value = "SELECT process.* FROM TETRIS_PROCESS process LEFT JOIN TETRIS_PROCESS_COMPANY_PERMISSION permission ON process.ID=permission.PROCESS_ID WHERE permission.COMPANY_ID=?1 \n#pageable\n",
		   countQuery = "SELECT count(process.id) FROM TETRIS_PROCESS process LEFT JOIN TETRIS_PROCESS_COMPANY_PERMISSION permission ON process.id=permission.PROCESS_ID WHERE permission.COMPANY_ID=?1",
		   nativeQuery = true)
	public Page<ProcessPO> findByCompanyId(String companyId, Pageable page) throws Exception;
	
	/**
	 * 统计公司下的流程数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月11日 下午1:12:26
	 * @param String companyId 公司id
	 * @return int 流程数量
	 */
	@Query(value = "SELECT count(process.id) FROM TETRIS_PROCESS process LEFT JOIN TETRIS_PROCESS_COMPANY_PERMISSION permission ON process.id=permission.PROCESS_ID WHERE permission.COMPANY_ID=?1", nativeQuery = true)
	public int countByCompanyId(String companyId);
	
	/**
	 * 查询公司下的流程（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月11日 下午1:10:41
	 * @param String companyId 公司id
	 * @param Collection<Long> except 例外流程id列表
	 * @param Pageable page 分页信息
	 * @return Page<ProcessPO> 流程列表
	 */
	@Query(value = "SELECT process.* FROM TETRIS_PROCESS process LEFT JOIN TETRIS_PROCESS_COMPANY_PERMISSION permission ON process.ID=permission.PROCESS_ID WHERE permission.COMPANY_ID=?1 AND process.ID NOT IN ?2 \n#pageable\n",
			   countQuery = "SELECT count(process.id) FROM TETRIS_PROCESS process LEFT JOIN TETRIS_PROCESS_COMPANY_PERMISSION permission ON process.id=permission.PROCESS_ID WHERE permission.COMPANY_ID=?1 AND process.ID NOT IN ?2",
			   nativeQuery = true)
	public Page<ProcessPO> findByCompanyIdWithExcept(String companyId, Collection<Long> except, Pageable page);
	
	/**
	 * 统计公司下的流程数量（带例外）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月11日 下午1:12:26
	 * @param String companyId 公司id
	 * @param Collection<Long> except 例外流程id列表
	 * @return int 流程数量
	 */
	@Query(value = "SELECT count(process.id) FROM TETRIS_PROCESS process LEFT JOIN TETRIS_PROCESS_COMPANY_PERMISSION permission ON process.id=permission.PROCESS_ID WHERE permission.COMPANY_ID=?1 AND process.id NOT IN ?2", nativeQuery = true)
	public int countByCompanyIdWithExcept(String companyId, Collection<Long> except);
	
}
