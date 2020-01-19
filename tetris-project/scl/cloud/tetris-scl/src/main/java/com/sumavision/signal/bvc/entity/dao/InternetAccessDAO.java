package com.sumavision.signal.bvc.entity.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.signal.bvc.entity.enumeration.InternetAccessType;
import com.sumavision.signal.bvc.entity.po.InternetAccessPO;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = InternetAccessPO.class, idClass = long.class)
public interface InternetAccessDAO extends BaseDAO<InternetAccessPO>{
	
	/**
	 * 根据地址和转发器id查询网口<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午2:38:05
	 * @param String address 名称
	 * @param Long repeaterId 转发器id
	 * @return InternetAccessPO 
	 */
	public InternetAccessPO findByAddressAndRepeaterIdAndType(String address, Long repeaterId, InternetAccessType type);
	
	/**
	 * 根据转发器id查询所有网口<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午2:43:56
	 * @param Long id 转发器id
	 * @return List<InternetAccessPO> 
	 */
	public List<InternetAccessPO> findByRepeaterId(Long id);
	
	/**
	 * 根据转发器id删除网口<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午3:49:30
	 * @param Long id 转发器id
 	 */
	public void deleteByRepeaterId(Long id);
	
	/**
	 * 根据网口类型查询网口列表<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午7:27:42
	 * @param InternetAccessType type 网口类型
	 */
	public List<InternetAccessPO> findByType(InternetAccessType type);
	
	/**
	 * 根据网口id查询网口信息<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月22日 下午3:12:09
	 * @param Long id 网口id
	 * @return InternetAccessPO
	 */
	public InternetAccessPO findById(Long id);
	
	/**
	 * 查询一个输入网口<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月30日 上午9:39:19
	 * @return
	 */
	@Query(value = "SELECT a.* FROM bvc_signal_internet_access a LEFT JOIN bvc_signal_repeater r ON a.repeater_id = r.id WHERE a.type = 'STREAM_IN' AND r.type = 'MAIN'", nativeQuery = true)
	public InternetAccessPO findByMainRepeater();
}
