package com.sumavision.tetris.sts.common;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 运用JPA，方法虽然不需要自己实现，但每个Dao都自己声名也不合适
 * 所有Dao通用的方法可以写在这里，为了方法的灵活性，Dao层全用Repository
 * Created by Poemafar on 2019/12/16 13:52
 */

public interface CommonDao<T> {
    public T findOne(Long id);
    public List<T> findAll();
    public long count();
    public void save(T t);
    public List<T> save(Iterable<T> list);
    public void delete(T t);
    public void delete(Long id);
    public void delete(Iterable<T> list);
    public void deleteAll();
    public boolean exists(Long id);
    public Page<T> findAll(Pageable pageable);
    //yzx add 使用 example 进行动态条件查询，仅支持多条件and
    public List<T> findAll(Example<T> example);
    public Page<T> findAll(Example<T> example,Pageable pageable);
}
