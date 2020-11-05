package com.sumavision.signal.bvc.entity.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;

import com.sumavision.signal.bvc.entity.enumeration.DstType;
import com.sumavision.signal.bvc.entity.enumeration.SrcType;
import com.sumavision.signal.bvc.entity.po.PortMappingPO;
import com.sumavision.tetris.orm.dao.BaseDAO;

@RepositoryDefinition(domainClass = PortMappingPO.class, idClass = long.class)
public interface PortMappingDAO extends BaseDAO<PortMappingPO>{

	/**
	 * 根据目的网口和类型查询端口映射<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月23日 上午9:12:24
	 * @param String address 目的网口地址
	 * @param DstType type 目的类型
	 * @return
	 */
	public List<PortMappingPO> findByDstAddressAndDstType(String address, DstType type);
	
	/**
	 * 根据源bundleId或目的bundleId查询端口映射<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月23日 上午9:35:48
	 * @param String srcBundleId
	 * @param String dstBundleId
	 * @return List<PortMappingPO>
	 */
	public List<PortMappingPO> findBySrcBundleIdOrDstBundleId(String srcBundleId, String dstBundleId);
	
	/**
	 * 根据源bundleId列表或目的bundleId列表查询端口映射<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月25日 下午2:25:05
	 * @param Collection<String> srcBundleIds
	 * @param Collection<String> dstBundleIds
	 */
	public List<PortMappingPO> findBySrcBundleIdInOrDstBundleIdIn(Collection<String> srcBundleIds, Collection<String> dstBundleIds);
	
	/**
	 * 根据bundleIds删除<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月11日 上午9:22:07
	 * @param List<Srting> bundleIds
	 */
	public void deleteBySrcBundleIdInOrDstBundleIdIn(List<String> bundleIds1, List<String> bundleIds2);
	
	/**
	 * 根据源repeaterId或目的repeaterId删除端口映射<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月23日 上午9:48:43
	 * @param Long srcId
	 * @param Long dstId
	 */
	public void deleteBySrcRepeaterIdOrDstRepeaterId(Long srcId, Long dstId);
	
	/**
	 * 根据源accessId或目的accessId删除端口映射<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月23日 上午9:57:46
	 * @param Long srcId
	 * @param Long dstId
	 */
	public void deleteBySrcAccessIdOrDstAccessId(Long srcId, Long dstId);
	
	/**
	 * 根据目的类型和目的bundleId查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月27日 下午6:52:23
	 * @param DstType dstType
	 * @param String bundleId
	 * @return List<PortMappingPO>
	 */
	public List<PortMappingPO> findByDstTypeAndDstBundleId(DstType dstType, String bundleId);
	
	/**
	 * 根据目的类型和目的bundleId和channelId查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月27日 下午6:52:23
	 * @param DstType dstType
	 * @param String bundleId
	 * @return List<PortMappingPO>
	 */
	public List<PortMappingPO> findByDstTypeAndDstBundleIdAndDstChannelId(DstType dstType, String bundleId, String channelId);
	
	/**
	 * 根据目的bundleId和channelId查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月27日 下午6:52:23
	 * @param DstType dstType
	 * @param String bundleId
	 * @return List<PortMappingPO>
	 */
	public List<PortMappingPO> findByDstBundleIdAndDstChannelId(String bundleId, String channelId);
	
	/**
	 * 根据目的类型和目的ip和端口查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月27日 下午6:52:23
	 * @param DstType dstType
	 * @param String address
	 * @param Long port
	 * @return PortMappingPO
	 */
	public PortMappingPO findByDstTypeAndDstAddressAndDstPort(DstType dstType, String address, Long port);

	/**
	 * 根据目的ip和端口查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月27日 下午6:52:23
	 * @param DstType dstType
	 * @param String address
	 * @param Long port
	 * @return PortMappingPO
	 */
	public PortMappingPO findByDstAddressAndDstPort(String address, Long port);

	
	/**
	 * 根据目的类型和目的bundleId列表查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月27日 下午6:52:23
	 * @param DstType dstType
	 * @param List<String> bundleIds
	 * @return List<PortMappingPO>
	 */
	public List<PortMappingPO> findByDstTypeAndDstBundleIdIn(DstType dstType, List<String> bundleIds);
	
	/**
	 * 根据源类型和源bundleId查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月27日 下午6:52:23
	 * @param SrcType srcType
	 * @param String bundleId
	 * @return List<PortMappingPO>
	 */
	public List<PortMappingPO> findBySrcTypeAndSrcBundleId(SrcType srcType, String bundleId);
	
	/**
	 * 根据源类型和源bundleId和channelId查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月27日 下午6:52:23
	 * @param SrcType srcType
	 * @param String bundleId
	 * @param String channelId
	 * @return PortMappingPO
	 */
	public PortMappingPO findBySrcTypeAndSrcBundleIdAndSrcChannelId(SrcType srcType, String bundleId, String channelId);
	
	/**
	 * 根据源bundleId和channelId查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月27日 下午6:52:23
	 * @param String bundleId
	 * @param String channelId
	 * @return PortMappingPO
	 */
	public PortMappingPO findBySrcBundleIdAndSrcChannelId(String bundleId, String channelId);
	
	/**
	 * 根据源类型和目的类型过滤查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月13日 下午3:31:05
	 * @param SrcType srcType 源类型
	 * @param DstType dstType 目的类型
	 * @return
	 */
	public List<PortMappingPO> findBySrcTypeNotOrDstTypeNot(SrcType srcType, DstType dstType);
	
	/**
	 * 根据源类型和目的类型查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月17日 上午9:33:15
	 * @param SrcType srcType 源类型
	 * @param DstType dstType 目的类型
	 * @return
	 */
	public List<PortMappingPO> findBySrcTypeOrDstType(SrcType srcType, DstType dstType);
	
	/**
	 * 根据目的bundleId查询<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月28日 上午10:49:03
	 * @param bundleIds
	 * @return
	 */
	public List<PortMappingPO> findByDstBundleIdIn(Collection<String> bundleIds);
	
	@Query("select mapping from PortMappingPO mapping where mapping.dstType = ?1")
	public Page<PortMappingPO> findByDstType(DstType dstType, Pageable page);
	
	@Query("select distinct mapping.dstAddress from PortMappingPO mapping where mapping.dstType = ?1")
	public List<String> findDstAddressByDstType(DstType dstType);
	
	@Modifying
	public void deleteByIdIn(Collection<Long> ids);
	
}
