package com.suma.venus.resource.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suma.venus.resource.dao.CommonDao;

@Service
public abstract class CommonService<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonService.class);
	
	@Autowired
	private CommonDao<T> commonDao;
	
	public void save(T po) {
		try {
			commonDao.save(po);
		} catch (Exception e) {
			LOGGER.error("Save " + po.getClass().getSimpleName() + " Error:", e);
			throw e;
		}
	}
	
	public void delete(T po)  {
		try {
			commonDao.delete(po);
		} catch (Exception e) {
			LOGGER.error("Delete "+po.getClass().getSimpleName()+" Error:", e);
			throw e;
		}
	}
	
	public void delete(Long id)  {
        try {
            commonDao.deleteById(id);
        } catch (Exception e) {
            LOGGER.error("Delete Error:", e);
            throw e;
        }
	}
	
	public T get(Long id)  {
		if(null == id){
			return null;
		}
		try {
			return commonDao.findById(id);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			throw e;
		}
	}
	
	public List<T> findAll(){
		return commonDao.findAll();
	}
	
	public void saveAll(List<T> pos) {
		try {
			commonDao.saveAll(pos);
		} catch (Exception e) {
			LOGGER.error("Save " + pos.getClass().getSimpleName() + " Error:", e);
			throw e;
		}
	}
}
