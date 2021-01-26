package com.suma.venus.resource.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;


/**
 * 运用JPA，方法虽然不需要自己实现，但每个Dao都自己声名也不合适
 * 所有Dao通用的方法可以写在这里，为了方法的灵活性，Dao层全用Repository
 * @author gaofeng
 *
 * @param <T>
 */
@Repository
public interface CommonDao<T>{
	public T findById(Long id);
	public List<T> findAll();
	public List<T> findAllById(Iterable<Long> ids);
	public long count();
	public void save(T t);
	public List<T> saveAll(Iterable<T> list);
	public void delete(T t);
	public void deleteInBatch(Iterable<T> list);
	public void deleteById(Long id);
	public void deleteAll();
	public boolean existsById(Long id);	
	public Page<T> findAll(Pageable pageable);
	public List<T> findAll(Specification<T> spec);
}
