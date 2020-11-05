package com.sumavision.tetris.zoom.history;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class HistoryService {

	@Autowired
	private HistoryDAO historyDao;
	
	/**
	 * 创建会议历史记录<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 下午4:05:43
	 * @param String zoomCode 会议号码
	 * @param String userId 用户id
	 * @return HistoryVO 历史记录
	 */
	public HistoryVO addZoomHistory(
			String zoomCode,
			String userId,
			String remark) throws Exception{
		
		HistoryPO history = new HistoryPO();
		history.setHistoryId(zoomCode);
		history.setRemark(remark);
		history.setType(HistoryType.ZOOM_CODE);
		history.setUserId(userId);
		history.setUpdateTime(new Date());
		historyDao.save(history);
		
		return new HistoryVO().set(history);
	}
	
	/**
	 * 删除历史记录<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 下午5:04:42
	 * @param Long id 历史记录id
	 */
	public void remove(Long id) throws Exception{
		HistoryPO history = historyDao.findOne(id);
		if(history!=null){
			historyDao.delete(history);
		}
	}
	
}
