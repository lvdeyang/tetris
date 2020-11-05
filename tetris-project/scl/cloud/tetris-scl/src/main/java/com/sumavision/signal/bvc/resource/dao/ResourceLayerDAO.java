package com.sumavision.signal.bvc.resource.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.pojo.WorkNodePO;

/**
 * @ClassName: 资源层bundle查询对象<br/> 
 * @author lvdeyang
 * @date 2018年8月25日 下午6:15:37 
 */
@Repository
public class ResourceLayerDAO{

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
	public List<WorkNodePO> findAll(){
		resourceEntityManager.clear();
		List<WorkNodePO> workNodePOs = gainResultList("from WorkNodePO", WorkNodePO.class, null, null, null, null);
		return workNodePOs;
	}
	
	
	public List<BundlePO> findAllMixer(){
		resourceEntityManager.clear();
		List<BundlePO> bundlePOs = gainResultList("from BundlePO where bundleType="+"'VenusMixer'", BundlePO.class, null, null, null, null);
		return bundlePOs;
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
