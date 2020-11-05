package com.sumavision.tetris.bvc.model.terminal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suma.venus.resource.dao.ChannelTemplateDao;
import com.suma.venus.resource.pojo.ChannelTemplatePO;
import com.sumavision.tetris.bvc.model.terminal.exception.TerminalNotFoundException;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

@Service
public class TerminalBundleService {

	@Autowired
	private TerminalDAO terminalDao;
	
	@Autowired
	private TerminalBundleDAO terminalBundleDao;
	
	@Autowired
	private TerminalBundleChannelDAO terminalBundleChannelDao;
	
	@Autowired
	private ChannelTemplateDao channelTemplateDao;
	
	/**
	 * 为终端绑定设备模板<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月17日 上午10:05:19
	 * @param Long terminalId 终端id
	 * @param String name 名称前缀
	 * @param String bundleType 设备模板类型
	 * @param String type 编解码类型
	 * @param Integer number 设备数量
	 * @return List<TerminalBundleVO> 设备列表
	 */
	@Transactional(rollbackFor = Exception.class)
	public List<TerminalBundleVO> add(
			Long terminalId, 
			String name, 
			String bundleType, 
			String type, 
			Integer number) throws Exception{
		TerminalPO terminal = terminalDao.findOne(terminalId);
		if(terminal == null){
			throw new TerminalNotFoundException(terminalId);
		}
		Set<String> channelIds = new HashSet<String>();
		List<ChannelTemplatePO> templates = channelTemplateDao.findByDeviceModel(bundleType);
		if(templates!=null && templates.size()>0){
			for(ChannelTemplatePO template:templates){
				String defaultChannelIds = template.getDefaultChannelIds();
				if(defaultChannelIds == null){
					Integer maxChannelCnt = template.getMaxChannelCnt();
					if(maxChannelCnt != null){
						for(int i=1; i<=maxChannelCnt.intValue(); i++){
							channelIds.add(new StringBufferWrapper().append(template.getBaseType()).append("_").append(i).toString());
						}
					}
				}else{
					channelIds.addAll(Arrays.asList(defaultChannelIds.split(",")));
				}
			}
		}
		List<TerminalBundlePO> bundles = new ArrayList<TerminalBundlePO>();
		for(int i=1; i<=number.intValue(); i++){
			TerminalBundlePO bundle = new TerminalBundlePO();
			bundle.setName(new StringBufferWrapper().append(name).append("_").append(i).toString());
			bundle.setBundleType(bundleType);
			bundle.setType(TerminalBundleType.valueOf(type));
			bundle.setTerminalId(terminalId);
			bundle.setUpdateTime(new Date());
			bundles.add(bundle);
		}
		terminalBundleDao.save(bundles);
		List<TerminalBundleChannelPO> channels = new ArrayList<TerminalBundleChannelPO>();
		for(TerminalBundlePO bundle:bundles){
			for(String channelId:channelIds){
				TerminalBundleChannelPO channel = new TerminalBundleChannelPO();
				channel.setChannelId(channelId);
				if(channelId.startsWith("VenusAudioIn")){
					channel.setType(TerminalBundleChannelType.AUDIO_ENCODE);
				}else if(channelId.startsWith("VenusAudioOut")){
					channel.setType(TerminalBundleChannelType.AUDIO_DECODE);
				}else if(channelId.startsWith("VenusVideoIn")){
					channel.setType(TerminalBundleChannelType.VIDEO_ENCODE);
				}else if(channelId.startsWith("VenusVideoOut")){
					channel.setType(TerminalBundleChannelType.VIDEO_DECODE);
				}
				channel.setUpdateTime(new Date());
				channel.setTerminalBundleId(bundle.getId());
				channels.add(channel);
			}
		}
		terminalBundleChannelDao.save(channels);
		
		return TerminalBundleVO.getConverter(TerminalBundleVO.class).convert(bundles, TerminalBundleVO.class);
	}
	
	/**
	 * 修改终端设备名称<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月17日 下午1:18:21
	 * @param Long id 终端设备id
	 * @param String name 名称
	 * @return TerminalBundleVO 终端设备
	 */
	@Transactional(rollbackFor = Exception.class)
	public TerminalBundleVO editName(
			Long id,
			String name) throws Exception{
		
		TerminalBundlePO bundle = terminalBundleDao.findOne(id);
		if(bundle != null){
			bundle.setName(name);
			terminalBundleDao.save(bundle);
			return new TerminalBundleVO().set(bundle);
		}
		return null;
	}
	
	/**
	 * 删除终端设备<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月17日 下午4:38:06
	 * @param Long id 终端设备id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long id) throws Exception{
		TerminalBundlePO bundle = terminalBundleDao.findOne(id);
		if(bundle != null){
			List<TerminalBundleChannelPO> channels = terminalBundleChannelDao.findByTerminalBundleIdOrderByChannelIdAsc(bundle.getId());
			terminalBundleDao.delete(bundle);
			if(channels!=null&&channels.size()>0){
				terminalBundleChannelDao.deleteInBatch(channels);
			}
		}
	}
	
}
