package com.sumavision.tetris.bvc.model.terminal.layout;

import java.util.Date;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.bvc.model.terminal.layout.exception.LayoutNotFoundException;

@Service
public class LayoutService {
	
	@Autowired
	private LayoutDAO layoutDao;

	/**
	 * 添加布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月24日 上午11:06:32
	 * @param String name 布局名称
	 * @return LayoutVO 布局
	 */
	@Transactional(rollbackFor = Exception.class)
	public LayoutVO add(String name) throws Exception{
		
		LayoutPO layout = new LayoutPO();
		layout.setName(name);
		layout.setUpdateTime(new Date());
		layoutDao.save(layout);
		
		return new LayoutVO().set(layout);
	}
	
	/**
	 * 修改布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月24日 上午11:06:32
	 * @param Long id 布局id
	 * @param String name 布局名称
	 * @return LayoutVO 布局
	 */
	@Transactional(rollbackFor = Exception.class)
	public LayoutVO edit(
			Long id,
			String name) throws Exception{
		LayoutPO layout = layoutDao.findOne(id);
		if(layout == null){
			throw new LayoutNotFoundException(id);
		}
		layout.setName(name);
		layoutDao.save(layout);
		return new LayoutVO().set(layout);
	}
	
	/**
	 * 删除布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月24日 上午11:08:56
	 * @param Long id 布局id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) throws Exception{
		LayoutPO layout = layoutDao.findOne(id);
		if(layout != null){
			layoutDao.delete(layout);
		}
	}
	
}
