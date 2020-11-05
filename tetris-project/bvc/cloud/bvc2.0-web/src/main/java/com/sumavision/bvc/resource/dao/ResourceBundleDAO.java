package com.sumavision.bvc.resource.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.BundlePO.ONLINE_STATUS;
import com.suma.venus.resource.pojo.WorkNodePO.NodeType;
import com.sumavision.bvc.resource.dto.BundleOnlineStatusDTO;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: 资源层bundle查询对象<br/> 
 * @author lvdeyang
 * @date 2018年8月25日 下午6:15:37 
 */
@Slf4j
@Repository
public class ResourceBundleDAO{

	@Resource
	@Qualifier("resourceEntityManager")
	private EntityManager resourceEntityManager;
	
	@PersistenceUnit(unitName="resourceEntityManagerFactory")
    private EntityManagerFactory emf;
	
	/**
	 * @Title: 根据id批量查询<br/> 
	 * @param ids 
	 * @return List<BundlePO>
	 */
	public List<BundlePO> findAll(Collection<Long> ids){
		if(ids==null || ids.size()<=0) return null;
		//TODO：清空hibernate一级缓存，暂时先这样写
		clearResourceEntityManager();
		List<BundlePO> bundles = gainResultList("from BundlePO where id in ?1", BundlePO.class, ids, null, null, null);
		return bundles;
	}
	
	/**
	 * @Title: 根据bundleId批量查询<br/> 
	 * @param bundleIds
	 * @return List<BundlePO>
	 */
	public List<BundlePO> findByBundleIds(Collection<String> bundleIds){
		if(bundleIds==null || bundleIds.size()<=0) return null;
		//TODO：清空hibernate一级缓存，暂时先这样写
		clearResourceEntityManager();
		List<BundlePO> bundles = gainResultList("from BundlePO where bundleId in ?1", BundlePO.class, bundleIds, null, null, null);
		return bundles;
	}
	
	/**
	 * @Title: 根据UserId查询，BundlePO中有userId的都是播放器，因此该方法实质上是在查询某个用户的播放器<br/> 
	 * @param userId
	 * @return List<BundlePO>
	 */
	public List<BundlePO> findByUserId(Long userId){
		if(userId == null) return null;
		//TODO：清空hibernate一级缓存，暂时先这样写
		clearResourceEntityManager();
		List<BundlePO> bundles = gainResultList("from BundlePO where userId = ?1", BundlePO.class, userId, null, null, null);
		return bundles;
	}
	
	/**
	 * @Title: 根据UserId和deviceModel查询某用户的特定类型的设备<br/>
	 * @param userId
	 * @param deviceModel
	 * @return List<BundlePO>
	 */
	public List<BundlePO> findByUserIdAndDeviceModel(Long userId, String deviceModel){
		if(userId == null) return null;
		//TODO：清空hibernate一级缓存，暂时先这样写
		clearResourceEntityManager();
		List<BundlePO> bundles = gainResultList("from BundlePO where userId = ?1 and deviceModel = ?2", BundlePO.class, userId, deviceModel, null, null);
		return bundles;
	}

	/**
	 * 根据号码查询设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月4日 下午5:40:26
	 * @param Sring username 设备号码
	 * @return BundlePO 设备
	 */
	public BundlePO findByUsername(String username){
		//TODO：清空hibernate一级缓存，暂时先这样写
		clearResourceEntityManager();
		List<BundlePO> bundles = gainResultList("from BundlePO where username = ?1", BundlePO.class, username, null, null, null);
		if(bundles==null || bundles.size()<=0) return null;
		return bundles.get(0);
	}
	
	/**
	 * @Title: 根据bundleId批量查询<br/> 
	 * @param bundleIds
	 * @return List<BundlePO>
	 */
	public List<BundlePO> findByBundleIdsAndFolderId(Collection<String> bundleIds, Long folderId){
		if(bundleIds==null || bundleIds.size()<=0) return null;
		//TODO：清空hibernate一级缓存，暂时先这样写
		clearResourceEntityManager();
		List<BundlePO> bundles = gainResultList("from BundlePO where bundleId in ?1 and folderId=?2", BundlePO.class, bundleIds, folderId, null, null);
		return bundles;
	}
	
	/**
	 * @Title: 根据bundleId批量查询jv230<br/> 
	 * @param bundleIds
	 * @return List<BundlePO>
	 */
	public List<BundlePO> findJv230ByBundelIds(Collection<String> bundleIds){
		if(bundleIds==null || bundleIds.size()<=0) return null;
		//TODO：清空hibernate一级缓存，暂时先这样写
		clearResourceEntityManager();
		List<BundlePO> bundles = gainResultList("from BundlePO where bundleId in ?1 and deviceModel='jv230'", BundlePO.class, bundleIds, null, null, null);
		return bundles;
	}
	
	/**
	 * @Title: findFolderBundles 
	 * @Description: 根据folderId查询该文件夹下的bundles 
	 * @param @param folderId
	 * @return List<BundlePO>
	 */
	public List<BundlePO> findFolderBundles(Long folderId) {
		if(folderId==0) return null;
		//TODO：清空hibernate一级缓存，暂时先这样写
		clearResourceEntityManager();
		String sqlString = "select bundle from BundlePO bundle where bundle.folderId=?1 and (bundle.bundleType='tvos' or bundle.bundleType='mobile' or bundle.bundleType='ipc')";
		List<BundlePO> bundles = gainResultList(sqlString, BundlePO.class, folderId, null, null, null);
		return bundles;
	}
	
	/**
	 * 查询bundle的在线/离线状态
	 */
	@Deprecated
	public List<BundleOnlineStatusDTO> findBundleOnlineStatusByBundleIds_old(Collection<String> bundleIds){
		if(bundleIds==null || bundleIds.size()<=0) return null;
		//TODO：清空hibernate一级缓存，暂时先这样写
		clearResourceEntityManager();
		
		StringBufferWrapper sqlBuffer = new StringBufferWrapper().append("select new com.sumavision.bvc.resource.dto.BundleOnlineStatusDTO(")
																 .append("bundle.bundleId, ")
																 .append("bundle.onlineStatus")
																 .append(") ")
																 .append("from BundlePO bundle ")
																 .append("where bundle.bundleId in ?1");
		List<BundleOnlineStatusDTO> bundleOnlineStatuses = gainResultList(sqlBuffer.toString(), BundleOnlineStatusDTO.class, bundleIds, null, null, null);
		return bundleOnlineStatuses;
	}
	
	/**
	 * 查询bundle的在线/离线状态，IP地址，接入层地址
	 */
	@SuppressWarnings("rawtypes")
	public List<BundleOnlineStatusDTO> findBundleOnlineStatusByBundleIds(Collection<String> bundleIds){
		if(bundleIds==null || bundleIds.size()<=0) return null;
		//TODO：清空hibernate一级缓存，暂时先这样写
		clearResourceEntityManager();
		
		//把bundleid拼接成(aaa,bbb)的字符串
		StringBufferWrapper bundleidBuffer = new StringBufferWrapper().append("(");
		for(String bundleId : bundleIds){
			bundleidBuffer.append("'").append(bundleId).append("'").append(",");
		}
		String bundleidsString = bundleidBuffer.toString();
		bundleidsString = bundleidsString.substring(0, bundleidsString.length()-1) + ")"; 
		
		StringBufferWrapper sqlBuffer = new StringBufferWrapper().append("select ")
																 .append("bundle.bundle_id, ")
																 .append("bundle.online_status, ")
																 .append("bundle.device_ip, ")
																 .append("bundle.device_port, ")
																 .append("bundle.device_model, ")
																 .append("layer.ip, ")
																 .append("layer.type ")
																 .append("from bundlepo bundle ")
																 .append("LEFT JOIN work_nodepo layer ")
																 .append("ON bundle.access_node_uid=layer.node_uid ")
																 .append("where bundle.bundle_id in ")
																 .append(bundleidsString);

//		System.out.println(sqlBuffer.toString());
		List<BundleOnlineStatusDTO> dtos = new ArrayList<BundleOnlineStatusDTO>();
		javax.persistence.Query query  = resourceEntityManager.createNativeQuery(sqlBuffer.toString());
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List rows;
		try{
			rows = query.getResultList();
		}catch (Exception e){
			resourceEntityManager = emf.createEntityManager();
			query = resourceEntityManager.createNativeQuery(sqlBuffer.toString());
			query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			rows = query.getResultList();
			
		}
		for(Object obj : rows){
			Map row = (Map)obj;
			NodeType layerType = null;
			String layerTypeString = (String)row.get("type");
			if(null != layerTypeString){
				layerType = NodeType.valueOf(layerTypeString);
			}
			BundleOnlineStatusDTO dto = new BundleOnlineStatusDTO(
					(String)row.get("bundle_id"),
					ONLINE_STATUS.valueOf((String)row.get("online_status")),
					(String)row.get("device_ip"),
					(Integer)row.get("device_port"),
					(String)row.get("device_model"),
					(String)row.get("ip"),
					layerType
					);
			dtos.add(dto);
		}
		
		return dtos;
	}
	
	/**
	 * @Title: 通用查询方法，最多支持4个参数；当数据库重启导致查询失败时，会重新创建EntityManager实体再查询<br/> 
	 * @param qlString 查询语句
	 * @param resultClass 返回类型
	 * @param param1
	 * @param param2
	 * @param param3
	 * @param param4
	 * @return 查询结果
	 */
	@SuppressWarnings("unchecked")
	private <T> List<T> gainResultList(String qlString, Class<?> resultClass,
			Object param1,
			Object param2,
			Object param3,
			Object param4){
		TypedQuery<?> query = resourceEntityManager.createQuery(qlString, resultClass);
		if(param1 != null) query.setParameter(1, param1);
		if(param2 != null) query.setParameter(2, param2);
		if(param3 != null) query.setParameter(3, param3);
		if(param4 != null) query.setParameter(4, param4);
		List<T> resultList;
		try{
			resultList = (List<T>) query.getResultList();
		}catch (Exception e){
			e.printStackTrace();
			log.warn("query.getResultList()抛错，重设resourceEntityManager");
			resultList = regainResultList(qlString, resultClass, param1, param2, param3, param4);
		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	private <T> List<T> regainResultList(String qlString, Class<?> resultClass,
			Object param1,
			Object param2,
			Object param3,
			Object param4){
//		resourceEntityManager.close();
		resourceEntityManager = emf.createEntityManager();
		TypedQuery<?> query = resourceEntityManager.createQuery(qlString, resultClass);
		if(param1 != null) query.setParameter(1, param1);
		if(param2 != null) query.setParameter(2, param2);
		if(param3 != null) query.setParameter(3, param3);
		if(param4 != null) query.setParameter(4, param4);
		List<T> resultList = (List<T>) query.getResultList();
		return resultList;
	}
	
	private void clearResourceEntityManager(){
		try{
			resourceEntityManager.clear();
		}catch (Exception e){
			e.printStackTrace();
			log.warn("resourceEntityManager.clear()抛错，重设resourceEntityManager");
			resourceEntityManager = emf.createEntityManager();
		}
	}
}
