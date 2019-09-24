package com.sumavision.tetris.cs.bak;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class SendBakService {
	@Autowired
	private AreaSendDAO areaSendDAO;
	
	@Autowired
	private ResourceSendDAO resourceSendDAO;
	
	@Autowired
	private VersionSendDAO versionSendDAO;
	
	/**
	 * 根据频道id删除所有上次播发保存信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * @param channelId 频道id
	 */
	public void removeBakFromChannel(String channelId){
		areaSendDAO.removeByChannelId(channelId);
		resourceSendDAO.removeByChannelId(channelId);
		versionSendDAO.removeByChannelId(channelId);
	}
}
