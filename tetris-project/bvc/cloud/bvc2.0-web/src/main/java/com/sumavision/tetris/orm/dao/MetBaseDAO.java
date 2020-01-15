package com.sumavision.tetris.orm.dao;

import java.util.Collection;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Repository
public interface MetBaseDAO <T extends AbstractBasePO>{
	
	public T findOne(Long id);
	
	public T findByUuid(String uuid);
	
	public List<T> findAll();
	
	public List<T> findAll(Iterable<Long> ids);
	
	public long count();
	
	public void save(T t);
	
	public void saveAndFlush(T t);
	
	public List<T> save(Iterable<T> list);
	
	public void delete(T t);
	
	public void delete(Long id);
	
	public void deleteAll();
	
	public void deleteInBatch(Iterable<T> ts);
	
	@Modifying
	@Transactional
	public void deleteByIdIn(Collection<Long> ids);
	
	public boolean exists(Long id);	
	
	public Page<T> findAll(Pageable pageable);
	
	public List<T> findAll(Specification<T> spec);
	
}
