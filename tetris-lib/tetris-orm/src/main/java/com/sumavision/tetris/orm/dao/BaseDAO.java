
package com.sumavision.tetris.orm.dao;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Repository
public interface BaseDAO <T extends AbstractBasePO>{
	public T findById(Long id);
	public T findByUuid(String uuid);
	public List<T> findByUuidIn(Collection<String> uuids);
	public List<T> findAll();
	public List<T> findAllById(Iterable<Long> ids);
	public long count();
	public T save(T t);
	public T saveAndFlush(T t);
	public List<T> saveAll(Iterable<T> list);
	public void delete(T t);
	public void deleteById(Long id);
	public void deleteAll();
	public void deleteInBatch(Iterable<T> ts);
	public boolean existsById(Long id);	
	public Page<T> findAll(Pageable pageable);
	public List<T> findAll(Specification<T> spec);
	public Page<T> findAll( Specification<T> spec, Pageable pageable);
	public void flush();
}
