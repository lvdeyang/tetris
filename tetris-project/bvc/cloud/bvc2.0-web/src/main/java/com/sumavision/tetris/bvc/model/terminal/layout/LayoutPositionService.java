package com.sumavision.tetris.bvc.model.terminal.layout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.bvc.model.terminal.layout.exception.LayoutNotFoundException;
import com.sumavision.tetris.bvc.model.terminal.layout.exception.LayoutPositionNotFoundException;
import com.sumavision.tetris.bvc.model.terminal.screen.TerminalScreenPrimaryKeyDAO;
import com.sumavision.tetris.bvc.model.terminal.screen.TerminalScreenPrimaryKeyPO;
import com.sumavision.tetris.bvc.model.terminal.screen.exception.ScreenPrimaryKeyNotFoundException;

@Service
public class LayoutPositionService {

	@Autowired
	private LayoutDAO layoutDao;
	
	@Autowired
	private LayoutPositionDAO layoutPositionDao;
	
	@Autowired
	private TerminalScreenPrimaryKeyDAO terminalScreenPrimaryKeyDao;
	
	/**
	 * 添加排版<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月24日 下午3:01:49
	 * @param Long layoutId 布局id
	 * @param String screenPrimaryKey 屏幕主键
	 * @param String x 横坐标
	 * @param String y 纵坐标
	 * @param String width 宽
	 * @param String height 高
	 * @param Integer zIndex 涂层顺序
	 * @return LayoutPositionVO 排版
	 */
	public LayoutPositionVO add(
			Long layoutId,
			String screenPrimaryKey,
			String x,
			String y,
			String width,
			String height,
			Integer zIndex) throws Exception{
		LayoutPO layout = layoutDao.findOne(layoutId);
		if(layout == null){
			throw new LayoutNotFoundException(layoutId);
		}
		List<TerminalScreenPrimaryKeyPO> primaryKeys = terminalScreenPrimaryKeyDao.findByScreenPrimaryKey(screenPrimaryKey);
		if(primaryKeys==null||primaryKeys.size()<=0){
			throw new ScreenPrimaryKeyNotFoundException(screenPrimaryKey);
		}
		LayoutPositionPO position = new LayoutPositionPO();
		position.setUpdateTime(new Date());
		position.setScreenPrimaryKey(screenPrimaryKey);
		position.setX(x);
		position.setY(y);
		position.setWidth(width);
		position.setHeight(height);
		position.setZIndex(zIndex);
		position.setLayoutId(layoutId);
		layoutPositionDao.save(position);
		return new LayoutPositionVO().set(position)
									 .setScreenName(primaryKeys.get(0).getName());
	}
	
	/**
	 * 批量添加模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月24日 下午3:30:48
	 * @param Long layoutId 布局id
	 * @param String screenPrimaryKeys 屏幕主键列表
	 * @return List<LayoutPositionVO> 排版列表
	 */
	public List<LayoutPositionVO> addBatch(
			Long layoutId,
			String screenPrimaryKeys) throws Exception{
		LayoutPO layout = layoutDao.findOne(layoutId);
		if(layout == null){
			throw new LayoutNotFoundException(layoutId);
		}
		List<LayoutPositionPO> positionEntities = new ArrayList<LayoutPositionPO>();
		List<LayoutPositionVO> positions = new ArrayList<LayoutPositionVO>();
		List<String> keys = JSON.parseArray(screenPrimaryKeys, String.class);
		List<TerminalScreenPrimaryKeyPO> keyEntities = terminalScreenPrimaryKeyDao.findByScreenPrimaryKeyIn(keys);
		if(keyEntities!=null && keyEntities.size()>0){
			for(TerminalScreenPrimaryKeyPO keyEntity:keyEntities){
				LayoutPositionPO position = new LayoutPositionPO();
				position.setLayoutId(layoutId);
				position.setScreenPrimaryKey(keyEntity.getScreenPrimaryKey());
				position.setUpdateTime(new Date());
				position.setZIndex(0);
				positionEntities.add(position);
			}
			layoutPositionDao.save(positionEntities);
			for(LayoutPositionPO positionEntity:positionEntities){
				LayoutPositionVO position = new LayoutPositionVO().set(positionEntity);
				for(TerminalScreenPrimaryKeyPO keyEntity:keyEntities){
					if(position.getScreenPrimaryKey().equals(keyEntity.getScreenPrimaryKey())){
						position.setScreenName(keyEntity.getName());
					}
				}
				positions.add(position);
			}
		}
		return positions;
	}
	
	/**
	 * 修改排版<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月24日 下午3:01:49
	 * @param Long id 排版id
	 * @param String x 横坐标
	 * @param String y 纵坐标
	 * @param String width 宽
	 * @param String height 高
	 * @param Integer zIndex 涂层顺序
	 * @return LayoutPositionVO 排版
	 */
	public LayoutPositionVO edit(
			Long id,
			String x,
			String y,
			String width,
			String height,
			Integer zIndex) throws Exception{
		LayoutPositionPO position = layoutPositionDao.findOne(id);
		if(position == null){
			throw new LayoutPositionNotFoundException(id);
		}
		position.setX(x);
		position.setY(y);
		position.setWidth(width);
		position.setHeight(height);
		position.setZIndex(zIndex);
		layoutPositionDao.save(position);
		return new LayoutPositionVO().set(position);
	}
	
	/**
	 * 删除排版<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月24日 下午3:08:18
	 * @param Long id 排版id
	 */
	public void delete(Long id) throws Exception{
		LayoutPositionPO position = layoutPositionDao.findOne(id);
		if(position != null){
			layoutPositionDao.delete(position);
		}
	}
	
}
