package com.sumavision.bvc.meeting.logic.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.sumavision.tetris.orm.po.AbstractBasePO;


/**
 * 从 com.suma.venus.resource.dao 复制得到
 * 运用JPA，方法虽然不需要自己实现，但每个Dao都自己声名也不合适
 * 所有Dao通用的方法可以写在这里，为了方法的灵活性，Dao层全用Repository
 * @author gaofeng
 *
 * @param <T>
 */
@Repository
public interface CommonDao <T extends AbstractBasePO>{
	public T findOne(Long id);
	public List<T> findAll();
	public List<T> findAll(Iterable<Long> ids);
	public long count();
	public void save(T t);
	public List<T> save(Iterable<T> list);
	public void delete(T t);
	public void delete(Long id);
	public void deleteAll();
	public void deleteInBatch(Iterable<T> ts);
	public boolean exists(Long id);	
	public Page<T> findAll(Pageable pageable);
	public List<T> findAll(Specification<T> spec);
}
