package com.sumavision.tetris.bvc.model.terminal.physical.screen;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sumavision.tetris.bvc.model.terminal.TerminalBundleChannelDAO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelBundleChannelPermissionDAO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelDAO;

@Component
public class TerminalPhysicalScreenQuery {
	
	@Autowired
	private TerminalPhysicalScreenDAO terminalPhysicalScreenDAO;
	
	/*@Autowired
	private TerminalPhysicalScreenChannelPermissionDAO terminalPhysicalScreenChannelPermissionDAO;
		
	@Autowired
	private TerminalChannelDAO terminalChannelDAO;
	
	@Autowired
	private TerminalChannelBundleChannelPermissionDAO terminalChannelBundleChannelPermissionDAO;
	
	@Autowired
	private TerminalBundleChannelDAO terminalBundleChannelDAO;*/

	/*public List<TerminalPhysicalScreenVO> loadScreenPermission (Long terminalId) throws Exception{
		List<TerminalPhysicalScreenVO> terminalPhysicalScreenVOs = new ArrayList<TerminalPhysicalScreenVO>();
		List<TerminalChannelVO> terminalChannelVOs = new ArrayList<TerminalChannelVO>();
		List<TerminalPhysicalScreenPO> terminalPhysicalScreenPOs = terminalPhysicalScreenDAO.findByTerminalId(terminalId);
		Set<Long> screenIds = new HashSet<Long>();
		Set<Long> channelIds = new HashSet<Long>();
		if (!terminalPhysicalScreenPOs.isEmpty()) {
			for (TerminalPhysicalScreenPO terminalPhysicalScreenPO : terminalPhysicalScreenPOs) {
				screenIds.add(terminalPhysicalScreenPO.getId());
			}
			List<TerminalPhysicalScreenChannelPermissionPO> terminalPhysicalScreenChannelPermissionPOs = terminalPhysicalScreenChannelPermissionDAO.findByTerminalPhysicalScreenIdIn(screenIds);
			if (!terminalPhysicalScreenChannelPermissionPOs.isEmpty() && terminalPhysicalScreenChannelPermissionPOs != null) {
				for (TerminalPhysicalScreenChannelPermissionPO terminalPhysicalScreenChannelPermissionPO : terminalPhysicalScreenChannelPermissionPOs) {
					channelIds.add(terminalPhysicalScreenChannelPermissionPO.getTerminalChannelId());
				}
				List<TerminalChannelPO> terminalChannelPOs = terminalChannelDAO.findByIdIn(channelIds);
				Set<Long> channelIdSet = new HashSet<Long>();
				Set<Long> bundleChannelId = new HashSet<Long>();
				Set<Long> terminalChannelIds = new HashSet<Long>();
				if(!terminalChannelPOs.isEmpty() && terminalChannelPOs != null){
					for (TerminalChannelPO terminalChannelPO : terminalChannelPOs) {
						terminalChannelIds.add(terminalChannelPO.getId());
					}
					TerminalChannelVO terminalChannelVO  = new TerminalChannelVO();
					for (TerminalChannelPO terminalChannelPO : terminalChannelPOs) {
						terminalChannelVO.set(terminalChannelPO);
					}
					List<TerminalChannelBundleChannelPermissionDTO>  bundleChannelPermissionDTOs = terminalChannelBundleChannelPermissionDAO.findByPermission(terminalChannelIds);
					if (!bundleChannelPermissionDTOs.isEmpty()&&bundleChannelPermissionDTOs != null) {
						List<TerminalChannelBundleChannelPermissionDTO> children = new ArrayList<TerminalChannelBundleChannelPermissionDTO>();
						for (TerminalChannelPO terminalChannelPO : terminalChannelPOs) {
							for (TerminalChannelBundleChannelPermissionDTO terminalChannelBundleChannelPermissionDTO : bundleChannelPermissionDTOs) {
								if(terminalChannelPO.getId() == terminalChannelBundleChannelPermissionDTO.getTerminalChannelId()){
									children.add(terminalChannelBundleChannelPermissionDTO);
								}
							}
						}
						terminalChannelVO.setChildren(children);
					}
					terminalChannelVOs.add(terminalChannelVO);
				}
		}
			for (TerminalPhysicalScreenPO terminalPhysicalScreenPO : terminalPhysicalScreenPOs) {
				TerminalPhysicalScreenVO terminalPhysicalScreenVO = new TerminalPhysicalScreenVO();
				List<TerminalChannelVO> screenTerminalChannelVO = new ArrayList<TerminalChannelVO>();
				terminalPhysicalScreenVO.setId(terminalPhysicalScreenPO.getId())
				.setName(terminalPhysicalScreenPO.getName())
				.setTerminalId(terminalPhysicalScreenPO.getTerminalId());
				if (!terminalPhysicalScreenChannelPermissionPOs.isEmpty()) {
					for (TerminalPhysicalScreenChannelPermissionPO terminalPhysicalScreenChannelPermissionPO : terminalPhysicalScreenChannelPermissionPOs) {
						if (terminalPhysicalScreenPO.getId() == terminalPhysicalScreenChannelPermissionPO.getTerminalPhysicalScreenId()) {
							for (TerminalChannelVO terminalChannelVO : terminalChannelVOs) {
								if (terminalPhysicalScreenChannelPermissionPO.getTerminalChannelId() == terminalChannelVO.getId()) {
									screenTerminalChannelVO.add(terminalChannelVO);
								}
							}
						}
					}
			}
				terminalPhysicalScreenVO.setChildren(screenTerminalChannelVO);	
				terminalPhysicalScreenVOs.add(terminalPhysicalScreenVO);
		}
		}
		return terminalPhysicalScreenVOs;
	}*/
	
	/**
	 * 根据终端查询物理屏<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月29日 下午1:23:33
	 * @param Long terminalId 终端id
	 * @return List<TerminalPhysicalScreenVO> 物理屏列表
	 */
	public List<TerminalPhysicalScreenVO> findByTerminalId(Long terminalId) throws Exception{
		List<TerminalPhysicalScreenPO> entities =  terminalPhysicalScreenDAO.findByTerminalIdOrderByName(terminalId);
		return TerminalPhysicalScreenVO.getConverter(TerminalPhysicalScreenVO.class).convert(entities, TerminalPhysicalScreenVO.class);
	}

}
