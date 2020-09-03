package com.sumavision.tetris.bvc.model.terminal.screen;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.bvc.model.terminal.screen.exception.ScreenPrimaryKeyExistException;
import com.sumavision.tetris.bvc.model.terminal.screen.exception.ScreenPrimaryKeyNotFoundException;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Service
public class TerminalScreenPrimaryKeyService {

	@Autowired
	private TerminalScreenPrimaryKeyDAO terminalScreenPrimaryKeyDao;
	
	/**
	 * 自动添加屏幕主键rect_1到rect_16<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月19日 下午3:15:20
	 * @return List<TerminalScreenPrimaryKeyVO> 主键
	 */
	@Transactional(rollbackFor = Exception.class)
	public List<TerminalScreenPrimaryKeyVO> autoAdd() throws Exception{
		List<String> primaryKeys = new ArrayList<String>();
		for(int i=1; i<=16; i++){
			primaryKeys.add(new StringBufferWrapper().append("rect_").append(i).toString());
		}
		List<TerminalScreenPrimaryKeyPO> existEntities = terminalScreenPrimaryKeyDao.findByScreenPrimaryKeyIn(primaryKeys);
		if(existEntities!=null && existEntities.size()>0){
			throw new ScreenPrimaryKeyExistException();
		}
		
		List<TerminalScreenPrimaryKeyPO> entities = new ArrayList<TerminalScreenPrimaryKeyPO>();
		for(String primaryKey:primaryKeys){
			TerminalScreenPrimaryKeyPO entity = new TerminalScreenPrimaryKeyPO();
			entity.setName(primaryKey.replace("rect_", "分屏"));
			entity.setScreenPrimaryKey(primaryKey);
			entity.setUpdateTime(new Date());
			entities.add(entity);
		}
		terminalScreenPrimaryKeyDao.save(entities);
		return TerminalScreenPrimaryKeyVO.getConverter(TerminalScreenPrimaryKeyVO.class).convert(entities, TerminalScreenPrimaryKeyVO.class);
	}
	
	/**
	 * 添加屏幕主键<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月19日 下午3:15:20
	 * @param String name 名称
	 * @param String primaryKey 主键
	 * @return TerminalScreenPrimaryKeyVO 主键
	 */
	@Transactional(rollbackFor = Exception.class)
	public TerminalScreenPrimaryKeyVO add(
			String name, 
			String primaryKey) throws Exception{
		
		List<TerminalScreenPrimaryKeyPO> entities = terminalScreenPrimaryKeyDao.findByScreenPrimaryKey(primaryKey);
		if(entities!=null && entities.size()>0){
			throw new ScreenPrimaryKeyExistException(primaryKey);
		}
		TerminalScreenPrimaryKeyPO entity = new TerminalScreenPrimaryKeyPO();
		entity.setName(name);
		entity.setScreenPrimaryKey(primaryKey);
		entity.setUpdateTime(new Date());
		terminalScreenPrimaryKeyDao.save(entity);
		
		return new TerminalScreenPrimaryKeyVO().set(entity);
	}
	
	/**
	 * 修改屏幕主键<br/>
	 * <p>
	 *   1.修改屏幕主键<br/>
	 *   2.修改终端屏幕映射<br/>
	 *   3.修改布局屏幕映射<br/>
	 *   4.修改用户自定义中的屏幕id<br/>
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月19日 下午3:15:20
	 * @param Long id 屏幕主键id
	 * @param String name 名称
	 * @param String primaryKey 主键
	 * @return TerminalScreenPrimaryKeyVO 主键
	 */
	@Transactional(rollbackFor = Exception.class)
	public TerminalScreenPrimaryKeyVO edit(
			Long id,
			String name,
			String primaryKey) throws Exception{
		TerminalScreenPrimaryKeyPO entity = terminalScreenPrimaryKeyDao.findOne(id);
		if(entity == null){
			throw new ScreenPrimaryKeyNotFoundException(id);
		}
		entity.setName(name);
		entity.setScreenPrimaryKey(primaryKey);
		entity.setUpdateTime(new Date());
		terminalScreenPrimaryKeyDao.save(entity);
		
		/*2.修改终端屏幕映射
		3.修改布局屏幕映射
		4.修改用户自定义中的屏幕id*/
		
		return new TerminalScreenPrimaryKeyVO().set(entity);
	}
	
	/**
	 * 删除屏幕主键<br/>
	 * <p>
	 *   1.删除屏幕主键<br/>
	 *   2.删除终端屏幕映射<br/>
	 *   3.删除布局屏幕映射<br/>
	 *   4.删除用户自定义中的屏幕id<br/>
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月19日 下午3:15:20
	 * @param Long id 屏幕主键id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) throws Exception{
		TerminalScreenPrimaryKeyPO entity = terminalScreenPrimaryKeyDao.findOne(id);
		if(entity != null){
			terminalScreenPrimaryKeyDao.delete(entity);
		}
	}
	
}
