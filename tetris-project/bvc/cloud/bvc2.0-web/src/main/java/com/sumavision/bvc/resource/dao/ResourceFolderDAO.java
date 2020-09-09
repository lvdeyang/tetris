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

import com.suma.venus.resource.pojo.FolderPO;

/**
 * @ClassName: 从资源层查询文件夹数据<br/> 
 * @author lvdeyang
 * @date 2018年8月25日 下午3:33:32 
 */
@Repository
public class ResourceFolderDAO{

	@Resource
	@Qualifier("resourceEntityManager")
	private EntityManager resourceEntityManager;
	
	@PersistenceUnit(unitName="resourceEntityManagerFactory")
    private EntityManagerFactory emf;
	
	/**
	 * @Title: 查询根文件夹<br/> 
	 * @return List<FolderPO>
	 */
	public List<FolderPO> findRootFolder(){
		List<FolderPO> folders = gainResultList("from FolderPO  where parentId=-1", FolderPO.class, null, null, null, null);
		return folders;
	}
	
	/**
	 * @Title: 根据id批量查询<br/> 
	 * @param ids
	 * @return List<FolderPO>
	 */
	public List<FolderPO> findAll(Collection<Long> ids){
		if(ids==null || ids.size()<=0) return null;
		List<FolderPO> folders = gainResultList("from FolderPO where id in ?1", FolderPO.class, ids, null, null, null);
		return folders;
	};
	
	/**
	 * @Title: 根据id查询所有父级文件夹路径<br/> 
	 * @param ids
	 * @return List<String>
	 */
	public List<String> queryParentPathByIds(Collection<Long> ids){
		if(ids==null || ids.size()<=0) return null;
		List<String> paths = gainResultList("select folder.parentPath from FolderPO folder where folder.id in ?1", String.class, ids, null, null, null);
		return paths;
	};
	
	/**
	 * @Title: findChildFolderByFolderId 
	 * @Description: 通过folderId查询其子folder 
	 * @param @param folderId
	 * @return List<FolderPO>
	 */
	public List<FolderPO> findChildFolderByFolderId(Long folderId) {
		if(folderId==null) return null;
		List<FolderPO> folders = gainResultList("from FolderPO where parentId=?1", FolderPO.class, folderId, null, null, null);
		return folders;
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
