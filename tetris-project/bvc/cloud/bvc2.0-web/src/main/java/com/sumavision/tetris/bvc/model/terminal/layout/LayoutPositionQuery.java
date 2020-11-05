package com.sumavision.tetris.bvc.model.terminal.layout;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.bvc.model.terminal.screen.TerminalScreenPrimaryKeyDAO;
import com.sumavision.tetris.bvc.model.terminal.screen.TerminalScreenPrimaryKeyPO;

@Component
public class LayoutPositionQuery {

	@Autowired
	private LayoutPositionDAO layoutPositionDao;
	
	@Autowired
	private TerminalScreenPrimaryKeyDAO terminalScreenPrimaryKeyDao;
	
	/**
	 * 查询布局排版<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月24日 下午1:30:44
	 * @param Long layoutId 布局id
	 * @return List<LayoutPositionVO> 排版列表
	 */
	public List<LayoutPositionVO> load(Long layoutId) throws Exception{
		List<LayoutPositionVO> positions = new ArrayList<LayoutPositionVO>();
		List<LayoutPositionPO> entities = layoutPositionDao.findByLayoutId(layoutId);
		List<TerminalScreenPrimaryKeyPO> primaryKeys = terminalScreenPrimaryKeyDao.findAll();
		if(entities!=null && entities.size()>0){
			for(LayoutPositionPO entity:entities){
				LayoutPositionVO position = new LayoutPositionVO().set(entity);
				if(primaryKeys!=null && primaryKeys.size()>0){
					for(TerminalScreenPrimaryKeyPO primaryKey:primaryKeys){
						if(primaryKey.getScreenPrimaryKey().equals(position.getScreenPrimaryKey())){
							position.setScreenName(primaryKey.getName());
							break;
						}
					}
				}
				positions.add(position);
			}
		}
		return positions;
	}
	
}
