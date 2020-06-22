package com.sumavision.tetris.bvc.model.terminal.channel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.bvc.model.terminal.TerminalBundleDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalBundlePO;

@Component
public class TerminalChannelQuery {

	@Autowired
	private TerminalBundleDAO terminalBundleDao;
	
	@Autowired
	private TerminalChannelDAO terminalChannelDao;
	
	/**
	 * 查询终端下的通道<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月18日 下午4:01:59
	 * @param Long terminalId 终端id
	 * @return List<TerminalChannelVO> 通道列表
	 */
	public List<TerminalChannelVO> load(Long terminalId) throws Exception{
		List<TerminalChannelPO> channelEntities = terminalChannelDao.findByTerminalIdOrderByTypeAscNameAsc(terminalId);
		Set<Long> terminalBundleIds = new HashSet<Long>();
		if(channelEntities!=null && channelEntities.size()>0){
			for(TerminalChannelPO channel:channelEntities){
				terminalBundleIds.add(channel.getTerminalBundleId());
			}
			List<TerminalBundlePO> bundleEntities =terminalBundleDao.findAll(terminalBundleIds);
			List<TerminalChannelVO> channels = TerminalChannelVO.getConverter(TerminalChannelVO.class).convert(channelEntities, TerminalChannelVO.class);
			if(bundleEntities!=null && bundleEntities.size()>0){
				for(TerminalChannelVO channel:channels){
					for(TerminalBundlePO bundle:bundleEntities){
						if(channel.getTerminalBundleId().equals(bundle.getId())){
							channel.setTerminalBundleName(bundle.getName());
							break;
						}
					}
				}
			}
			return channels;
		}
		return null;
	} 
	
}
