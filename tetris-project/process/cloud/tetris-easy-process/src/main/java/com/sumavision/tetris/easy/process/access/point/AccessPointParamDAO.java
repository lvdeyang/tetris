package com.sumavision.tetris.easy.process.access.point;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = AccessPointParamPO.class, idClass = Long.class)
public interface AccessPointParamDAO extends BaseDAO<AccessPointParamPO>{

	/**
	 * 根据接入点（批量）获取参数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月18日 下午1:25:08
	 * @param Collection<Long> accessPointIds 接入点id列表
	 * @return List<AccessPointParamPO> 接入点参数
	 */
	public List<AccessPointParamPO> findByAccessPointIdIn(Collection<Long> accessPointIds);
	
	/**
	 * 根据接入点（批量）获取特定类型的参数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月9日 上午9:32:38
	 * @param Collection<Long> accessPointIds 接入点id列表
	 * @param ParamDirection direction 参数类型
	 * @return List<AccessPointParamPO> 接入点参数
	 */
	public List<AccessPointParamPO> findByAccessPointIdInAndDirection(Collection<Long> accessPointIds, ParamDirection direction);
	
	/**
	 * 分页查询接入点下的参数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月19日 上午10:25:10
	 * @param Long accessPointId 接入点id
	 * @param Pageable page 分页
	 * @return List<AccessPointParamPO> 参数列表
	 */
	public Page<AccessPointParamPO> findByAccessPointId(Long accessPointId, Pageable page);
	
	/**
	 * 统计接入点下的参数数量<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月19日 上午10:53:58
	 * @param Long accessPointId 接入点id
	 * @return int 参数数量
	 */
	public int countByAccessPointId(Long accessPointId);
	
	/**
	 * 根据建查询参数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月23日 下午2:23:20
	 * @param String primaryKey 参数主键
	 * @return AccessPointParamPO 参数
	 */
	public AccessPointParamPO findByPrimaryKey(String primaryKey);
	
	/**
	 * 根据接入点id与参数序号查询参数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月23日 下午3:13:08
	 * @param Long accessPointId 接入点id
	 * @param Integer serial 参数序号
	 * @return AccessPointParamPO 参数
	 */
	public AccessPointParamPO findByAccessPointIdAndSerial(Long accessPointId, Integer serial);
	
	/**
	 * 查询一个参数下的所有子参数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年1月17日 上午8:57:46
	 * @param String reg0 '%/id'
	 * @param String reg1 '%/id/%'
	 * @return List<AccessPointParamPO> 所有的子参数
	 */
	@Query(value = "SELECT * FROM tetris_process_access_point_param WHERE parent_path LIKE ?1 OR parent_path LIKE ?2", nativeQuery = true)
	public List<AccessPointParamPO> findAllSubParams(String reg0, String reg1);
	
	/**
	 * 查询流程内配置的接入点内所有的参数（包括参数和返回值）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月21日 下午4:58:47
	 * @param Long processId 流程id
	 * @return List<AccessPointParamPO> 参数列表
	 */
	@Query(value = "SELECT param.* FROM TETRIS_PROCESS_ACCESS_POINT_PARAM param LEFT JOIN TETRIS_PROCESS_ACCESS_POINT ap ON param.access_point_id=ap.id LEFT JOIN TETRIS_ACCESS_POINT_PROCESS_PERMISSION permission ON ap.id=permission.access_point_id WHERE permission.process_id=?1", nativeQuery = true)
	public List<AccessPointParamPO> findByProcessId(Long processId);
	
}
