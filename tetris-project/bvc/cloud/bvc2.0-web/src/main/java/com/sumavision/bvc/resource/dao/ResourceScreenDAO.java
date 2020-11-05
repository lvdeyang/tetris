package com.sumavision.bvc.resource.dao;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.suma.venus.resource.pojo.ScreenRectTemplatePO;
import com.suma.venus.resource.pojo.ScreenSchemePO;

/**
 * @ClassName: 资源层屏幕查询 
 * @author wjw
 * @date 2018年11月7日 下午2:33:11
 */
@Repository
public class ResourceScreenDAO {

	@Resource
	@Qualifier("resourceEntityManager")
	private EntityManager resourceEntityManager;
	
	@PersistenceUnit(unitName="resourceEntityManagerFactory")
    private EntityManagerFactory emf;
	
	/**
	 * @Title: 根据bundleId批量查询<br/> 
	 * @param bundleIds
	 * @return List<ScreenSchemePO>
	 */
	public List<ScreenSchemePO> findByBundleIds(Collection<String> bundleIds){
		if(bundleIds==null || bundleIds.size()<=0) return null;
		//TODO：清空hibernate一级缓存，暂时先这样写
		resourceEntityManager.clear();
		List<ScreenSchemePO> screens = gainResultList("from ScreenSchemePO where bundleId in ?1", ScreenSchemePO.class, bundleIds, null, null, null);;
		return screens;
	}
	
	/**
	 * @Title: 根据screenId批量查询区域信息<br/>
	 * @param screenIds
	 * @return List<ScreenRectTemplatePO>
	 */
	public List<ScreenRectTemplatePO> findByScreenIds(Collection<String> screenIds){
		if(screenIds==null || screenIds.size()<=0) return null;
		//TODO：清空hibernate一级缓存，暂时先这样写
		resourceEntityManager.clear();
		List<ScreenRectTemplatePO> rects = gainResultList("from ScreenRectTemplatePO where screenId in ?1", ScreenRectTemplatePO.class, screenIds, null, null, null);;
		return rects;
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
