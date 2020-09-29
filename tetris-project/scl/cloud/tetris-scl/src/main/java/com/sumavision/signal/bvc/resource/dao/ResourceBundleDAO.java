package com.sumavision.signal.bvc.resource.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.signal.bvc.resource.dto.BundleOnlineStatusDTO;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * @ClassName: 资源层bundle查询对象<br/> 
 * @author lvdeyang
 * @date 2018年8月25日 下午6:15:37 
 */
@Repository
public class ResourceBundleDAO{

	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceBundleDAO.class);

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
		resourceEntityManager.clear();
		List<BundlePO> bundles = gainResultList("from BundlePO where id in ?1", BundlePO.class, ids, null, null, null);
		return bundles;
	}
	
	/**
	 * @Title: 根据bundleId查询<br/> 
	 * @param bundleIds
	 * @return BundlePO
	 */
	public BundlePO findByBundleId(String bundleId){
		List<String> ids = new ArrayList<String>();
		ids.add(bundleId);
		//TODO：清空hibernate一级缓存，暂时先这样写
		resourceEntityManager.clear();
		List<BundlePO> bundles = gainResultList("from BundlePO where bundleId in ?1", BundlePO.class, ids, null, null, null);
		
		if(bundles == null || bundles.size() <= 0){
			return null;
		}else{
			return bundles.get(0);
		}
	}
	
	/**
	 * @Title: 根据bundleId批量查询<br/> 
	 * @param bundleIds
	 * @return List<BundlePO>
	 */
	public List<BundlePO> findByBundleIds(Collection<String> bundleIds){
		if(bundleIds==null || bundleIds.size()<=0) return null;
		//TODO：清空hibernate一级缓存，暂时先这样写
		resourceEntityManager.clear();
		List<BundlePO> bundles = gainResultList("from BundlePO where bundleId in ?1", BundlePO.class, bundleIds, null, null, null);
		return bundles;
	}
	
	/**
	 * @Title: 根据accessNodeUid查询<br/> 
	 * @param accessNodeUid
	 * @return List<BundlePO>
	 */
	public List<BundlePO> findByAccessNodeUid(String accessNodeUid, String deviceModel){
		
		resourceEntityManager.clear();
		List<BundlePO> bundles = gainResultList("from BundlePO where accessNodeUid=?1 and deviceModel=?2", BundlePO.class, accessNodeUid, deviceModel, null, null);
		return bundles;
	}
	
	/**
	 * @Title: 根据accessNodeUid查询<br/> 
	 * @param accessNodeUid
	 * @return List<BundlePO>
	 */
	public List<BundlePO> findByDeviceModel(String deviceModel){
		
		resourceEntityManager.clear();
		List<BundlePO> bundles = gainResultList("from BundlePO where deviceModel=?1", BundlePO.class, deviceModel, null, null, null);
		bundles.stream().forEach(b->{
			LOGGER.info("find bundlepos,ip:{}",b.getDeviceIp());
		});

		return bundles;
	}
	
	/**
	 * @Title: 根据accessNodeUid查询<br/> 
	 * @param accessNodeUid
	 * @return List<BundlePO>
	 */
	public List<BundlePO> findByDeviceModelAndNotBundleAlias(String deviceModel, String bundleAlias){
		
		resourceEntityManager.clear();
		List<BundlePO> bundles = gainResultList("from BundlePO where deviceModel=?1 and ((bundleAlias is null) or (bundleAlias is not null and bundleAlias<>?2))", BundlePO.class, deviceModel, bundleAlias, null, null);
		return bundles;
	}
	
	/**
	 * @Title: 根据bundleId批量查询<br/> 
	 * @param bundleIds
	 * @return List<BundlePO>
	 */
	public List<BundlePO> findByBundleIdsAndFolderId(Collection<String> bundleIds, Long folderId){
		if(bundleIds==null || bundleIds.size()<=0) return null;
		//TODO：清空hibernate一级缓存，暂时先这样写
		resourceEntityManager.clear();
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
		resourceEntityManager.clear();
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
		resourceEntityManager.clear();
		String sqlString = "select bundle from BundlePO bundle where bundle.folderId=?1 and (bundle.bundleType='tvos' or bundle.bundleType='mobile' or bundle.bundleType='ipc')";
		List<BundlePO> bundles = gainResultList(sqlString, BundlePO.class, folderId, null, null, null);
		return bundles;
	}
	
	/**
	 * 查询bundle的在线/离线状态
	 */
	public List<BundleOnlineStatusDTO> findBundleOnlineStatusByBundleIds(Collection<String> bundleIds){
		if(bundleIds==null || bundleIds.size()<=0) return null;
		//TODO：清空hibernate一级缓存，暂时先这样写
		resourceEntityManager.clear();
		
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
}
