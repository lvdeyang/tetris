package com.sumavision.tetris.bvc.model.terminal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumavision.tetris.bvc.model.layout.forward.CombineTemplateDAO;
import com.sumavision.tetris.bvc.model.layout.forward.CombineTemplatePO;
import com.sumavision.tetris.bvc.model.layout.forward.CombineTemplatePositionDAO;
import com.sumavision.tetris.bvc.model.layout.forward.CombineTemplatePositionPO;
import com.sumavision.tetris.bvc.model.layout.forward.LayoutForwardDAO;
import com.sumavision.tetris.bvc.model.layout.forward.LayoutForwardPO;
import com.sumavision.tetris.bvc.model.layout.forward.LayoutForwardSourceType;
import com.sumavision.tetris.bvc.model.role.RoleChannelTerminalChannelPermissionDAO;
import com.sumavision.tetris.bvc.model.role.RoleChannelTerminalChannelPermissionPO;
import com.sumavision.tetris.bvc.model.terminal.audio.TerminalAudioOutputChannelPermissionDAO;
import com.sumavision.tetris.bvc.model.terminal.audio.TerminalAudioOutputChannelPermissionPO;
import com.sumavision.tetris.bvc.model.terminal.audio.TerminalAudioOutputDAO;
import com.sumavision.tetris.bvc.model.terminal.audio.TerminalAudioOutputPO;
import com.sumavision.tetris.bvc.model.terminal.audio.TerminalEncodeAudioVideoChannelPermissionDAO;
import com.sumavision.tetris.bvc.model.terminal.audio.TerminalEncodeAudioVideoChannelPermissionPO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelBundleChannelPermissionDAO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelBundleChannelPermissionPO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelDAO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelPO;
import com.sumavision.tetris.bvc.model.terminal.channel.TerminalChannelType;
import com.sumavision.tetris.bvc.model.terminal.exception.TerminalNotFoundException;
import com.sumavision.tetris.bvc.model.terminal.physical.screen.TerminalPhysicalScreenChannelPermissionDAO;
import com.sumavision.tetris.bvc.model.terminal.physical.screen.TerminalPhysicalScreenChannelPermissionPO;
import com.sumavision.tetris.bvc.model.terminal.physical.screen.TerminalPhysicalScreenDAO;
import com.sumavision.tetris.bvc.model.terminal.physical.screen.TerminalPhysicalScreenPO;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;

@Service
public class TerminalService {

	@Autowired
	private TerminalDAO terminalDao;
	
	@Autowired
	private TerminalBundleDAO terminalBundleDao;
	
	@Autowired
	private TerminalBundleChannelDAO terminalBundleChannelDao;
	
	@Autowired
	private TerminalChannelDAO terminalChannelDao;
	
	@Autowired
	private TerminalBundleService terminalBundleService;
	
	@Autowired
	private TerminalPhysicalScreenDAO terminalPhysicalScreenDao;
	
	@Autowired
	private TerminalAudioOutputDAO terminalAudioOutputDao;
	
	@Autowired
	private TerminalAudioOutputChannelPermissionDAO terminalAudioOutputChannelPermissionDao;
	
	@Autowired
	private TerminalPhysicalScreenChannelPermissionDAO terminalPhysicalScreenChannelPermissionDao;
	
	@Autowired
	private TerminalEncodeAudioVideoChannelPermissionDAO terminalEncodeAudioVideoChannelPermissionDao;
	
	@Autowired
	private TerminalChannelBundleChannelPermissionDAO terminalChannelBundleChannelPermissionDao;
	
	@Autowired
	private RoleChannelTerminalChannelPermissionDAO roleChannelTerminalChannelPermissionDao;
	
	@Autowired
	private LayoutForwardDAO layoutForwardDao;
	
	@Autowired
	private CombineTemplateDAO combineTemplateDao;
	
	@Autowired
	private CombineTemplatePositionDAO combineTemplatePositionDao;
	
	/**
	 * 添加终端<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月5日 下午3:14:38
	 * @param String name 终端名称
	 * @param String typeName 类型名称
	 * @return TerminalVO 终端
	 */
	@Transactional(rollbackFor = Exception.class)
	public TerminalVO create(String name, String typeName) throws Exception{
		TerminalPO entity = new TerminalPO();
		entity.setName(name);
		if(typeName != null){
			entity.setType(TerminalType.fromName(typeName));
		}
		entity.setUpdateTime(new Date());
		terminalDao.save(entity);
		return new TerminalVO().set(entity);
	}
	
	/**
	 * 一键添加单设备类型终端<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2021年1月20日 上午9:09:25
	 * @return List<TerminalVO> 终端列表
	 */
	@Transactional(rollbackFor = Exception.class)
	public List<TerminalVO> createSingleBundleTerminalBatch() throws Exception{
		List<TerminalType> terminalTypes = TerminalType.findBySignleBundle(true);
		List<TerminalPO> existTerminalEntities = terminalDao.findByTypeIn(terminalTypes);
		if(existTerminalEntities!=null && existTerminalEntities.size()>0){
			for(TerminalPO existTerminalEntity:existTerminalEntities){
				terminalTypes.remove(existTerminalEntity.getType());
			}
		}
		List<TerminalPO> singleBundleTerminalEntities = new ArrayList<TerminalPO>();
		for(TerminalType terminalType:terminalTypes){
			TerminalPO singleBundleTerminalEntity = new TerminalPO();
			singleBundleTerminalEntity.setName(terminalType.getName());
			singleBundleTerminalEntity.setType(terminalType);
			singleBundleTerminalEntity.setUpdateTime(new Date());
			singleBundleTerminalEntities.add(singleBundleTerminalEntity);
		}
		if(singleBundleTerminalEntities.size() > 0){
			terminalDao.save(singleBundleTerminalEntities);
			
			for(TerminalPO singleBundleTerminalEntity:singleBundleTerminalEntities){
				Map<String, Object> entities = terminalBundleService.add(singleBundleTerminalEntity.getId(), singleBundleTerminalEntity.getType().getName(), 
						singleBundleTerminalEntity.getType().getDevicModel(), singleBundleTerminalEntity.getType().getTerminalBundleType(), 1, true);
				
				List<TerminalBundlePO> terminalBundles = (List<TerminalBundlePO>)entities.get("terminalBundles");
				List<TerminalBundleChannelPO> terminalBundleChannels = (List<TerminalBundleChannelPO>)entities.get("terminalBundleChannels");
				List<TerminalChannelPO> terminalChannels = (List<TerminalChannelPO>)entities.get("terminalChannels");
				
				//音频输出
				TerminalAudioOutputPO terminalAudioOutput = new TerminalAudioOutputPO();
				terminalAudioOutput.setName("音频输出1");
				terminalAudioOutput.setTerminalId(singleBundleTerminalEntity.getId());
				terminalAudioOutput.setUpdateTime(new Date());
				terminalAudioOutputDao.save(terminalAudioOutput);
				
				//音频解码绑定输出
				if(terminalChannels!=null && terminalChannels.size()>0){
					List<TerminalAudioOutputChannelPermissionPO> terminalAudioOutputChannelPermissions = new ArrayList<TerminalAudioOutputChannelPermissionPO>();
					for(TerminalChannelPO terminalChannel:terminalChannels){
						if(TerminalChannelType.AUDIO_DECODE.equals(terminalChannel.getType())){
							TerminalAudioOutputChannelPermissionPO permission = new TerminalAudioOutputChannelPermissionPO();
							permission.setTerminalAudioChannelId(terminalChannel.getId());
							permission.setTerminalAudioOutputId(terminalAudioOutput.getId());
							permission.setTerminalId(singleBundleTerminalEntity.getId());
							permission.setUpdateTime(new Date());
							terminalAudioOutputChannelPermissions.add(permission);
						}
					}
					if(terminalAudioOutputChannelPermissions.size() > 0) terminalAudioOutputChannelPermissionDao.save(terminalAudioOutputChannelPermissions);
				}
				
				//物理屏幕
				TerminalPhysicalScreenPO terminalPhysicalScreen = new TerminalPhysicalScreenPO();
				terminalPhysicalScreen.setName("物理屏幕1");
				terminalPhysicalScreen.setTerminalAudioOutputId(terminalAudioOutput.getId());
				terminalPhysicalScreen.setTerminalId(singleBundleTerminalEntity.getId());
				terminalPhysicalScreenDao.save(terminalPhysicalScreen);
				
				//视频解码绑定屏幕
				if(terminalChannels!=null && terminalChannels.size()>0){
					List<TerminalPhysicalScreenChannelPermissionPO> terminalPhysicalScreenChannelPermissions = new ArrayList<TerminalPhysicalScreenChannelPermissionPO>();
					for(TerminalChannelPO terminalChannel:terminalChannels){
						if(TerminalChannelType.VIDEO_DECODE.equals(terminalChannel.getType())){
							TerminalPhysicalScreenChannelPermissionPO permission = new TerminalPhysicalScreenChannelPermissionPO();
							permission.setTerminalChannelId(terminalChannel.getId());
							permission.setTerminalPhysicalScreenId(terminalPhysicalScreen.getId());
							permission.setTerminalId(singleBundleTerminalEntity.getId());
							permission.setUpdateTime(new Date());
							terminalPhysicalScreenChannelPermissions.add(permission);
						}
					}
					if(terminalPhysicalScreenChannelPermissions.size() > 0) terminalPhysicalScreenChannelPermissionDao.save(terminalPhysicalScreenChannelPermissions);
				}
				
				//视频编码绑定音频编码
				if(terminalChannels!=null && terminalChannels.size()>0){
					TerminalChannelPO audioEncodeChannel = null;
					for(TerminalChannelPO terminalChannel:terminalChannels){
						if(TerminalChannelType.AUDIO_ENCODE.equals(terminalChannel.getType())){
							audioEncodeChannel = terminalChannel;
							break;
						}
					}
					if(audioEncodeChannel != null){
						List<TerminalEncodeAudioVideoChannelPermissionPO> terminalEncodeAudioVideoChannelPermissions = new ArrayList<TerminalEncodeAudioVideoChannelPermissionPO>();
						for(TerminalChannelPO terminalChannel:terminalChannels){
							if(TerminalChannelType.VIDEO_ENCODE.equals(terminalChannel.getType())){
								TerminalEncodeAudioVideoChannelPermissionPO permission = new TerminalEncodeAudioVideoChannelPermissionPO();
								permission.setTerminalAudioChannelId(audioEncodeChannel.getId());
								permission.setTerminalVideoChannelId(terminalChannel.getId());
								permission.setTerminalId(singleBundleTerminalEntity.getId());
								permission.setUpdateTime(new Date());
								terminalEncodeAudioVideoChannelPermissions.add(permission);
							}
						}
						if(terminalEncodeAudioVideoChannelPermissions.size() > 0) terminalEncodeAudioVideoChannelPermissionDao.save(terminalEncodeAudioVideoChannelPermissions);
					}
				}
			}
			
		}
		return TerminalVO.getConverter(TerminalVO.class).convert(singleBundleTerminalEntities, TerminalVO.class);
	}
	
	/**
	 * 修改终端名称<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月8日 上午9:37:28
	 * @param Long id 终端id
	 * @param String name 名称
	 * @param String typeName 类型名称
	 * @return TerminalVO 终端
	 */
	@Transactional(rollbackFor = Exception.class)
	public TerminalVO edit(Long id, String name, String typeName) throws Exception{
		TerminalPO entity = terminalDao.findOne(id);
		if(entity == null){
			throw new TerminalNotFoundException(id);
		}
		entity.setName(name);
		if(typeName != null){
			entity.setType(TerminalType.fromName(typeName));
		}
		entity.setUpdateTime(new Date());
		terminalDao.save(entity);
		return new TerminalVO().set(entity);
	}
	
	/**
	 * 删除终端<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年6月8日 上午9:43:50
	 * @param Long id 终端id
 	 * @return TerminalVO 终端
	 */
	@Transactional(rollbackFor = Exception.class)
	public TerminalVO delete(Long id) throws Exception{
		TerminalPO entity = terminalDao.findOne(id);
		if(entity == null) return null;
		terminalDao.delete(entity);

		//删除终端设备
		List<TerminalBundlePO> terminalBundleEntities = terminalBundleDao.findByTerminalId(entity.getId());
		List<Long> terminalBundleIds = new ArrayListWrapper<Long>().add(-1l).getList();
		if(terminalBundleEntities!=null && terminalBundleEntities.size()>0){
			terminalBundleDao.deleteInBatch(terminalBundleEntities);
			for(TerminalBundlePO terminalBundleEntity:terminalBundleEntities){
				terminalBundleIds.add(terminalBundleEntity.getId());
			}
		} 
		
		//删除终端设备通道
		List<TerminalBundleChannelPO> terminalBundleChannelEntities = terminalBundleChannelDao.findByTerminalBundleIdIn(terminalBundleIds);
		List<Long> terminalBundleChannelIds = new ArrayListWrapper<Long>().add(-1l).getList();
		if(terminalBundleChannelEntities!=null && terminalBundleChannelEntities.size()>0){
			terminalBundleChannelDao.deleteInBatch(terminalBundleChannelEntities);
			for(TerminalBundleChannelPO terminalBundleChannelEntity:terminalBundleChannelEntities){
				terminalBundleChannelIds.add(terminalBundleChannelEntity.getId());
			}
		}
		
		//删除终端通道
		List<TerminalChannelPO> terminalChannelEntities = terminalChannelDao.findByTerminalId(entity.getId());
		List<Long> terminalChannelIds = new ArrayListWrapper<Long>().add(-1l).getList();
		if(terminalChannelEntities!=null && terminalChannelEntities.size()>0){
			terminalChannelDao.deleteInBatch(terminalChannelEntities);
			for(TerminalChannelPO terminalChannelEntity:terminalChannelEntities){
				terminalChannelIds.add(terminalChannelEntity.getId());
			}
		}
		
		//删除终端通道终端设备通道关联
		List<TerminalChannelBundleChannelPermissionPO> terminalChannelBundleChannelPermissionEntities = terminalChannelBundleChannelPermissionDao.findByTerminalChannelIdIn(terminalChannelIds);
		if(terminalChannelBundleChannelPermissionEntities!=null && terminalChannelBundleChannelPermissionEntities.size()>0){
			terminalChannelBundleChannelPermissionDao.deleteInBatch(terminalChannelBundleChannelPermissionEntities);
		}
		
		//删除角色通道关联
		List<RoleChannelTerminalChannelPermissionPO> roleChannelTerminalChannelPermissionEntities = roleChannelTerminalChannelPermissionDao.findByTerminalChannelIdIn(terminalChannelIds);
		if(roleChannelTerminalChannelPermissionEntities!=null && roleChannelTerminalChannelPermissionEntities.size()>0){
			roleChannelTerminalChannelPermissionDao.deleteInBatch(roleChannelTerminalChannelPermissionEntities);
		}
		
		//删除画面转发
		List<LayoutForwardPO> layoutForwardEntities = layoutForwardDao.findByTerminalDecodeChannelIdIn(terminalChannelIds);
		List<Long> combineTemplateIds = new ArrayListWrapper<Long>().add(-1l).getList();
		if(layoutForwardEntities!=null && layoutForwardEntities.size()>0){
			layoutForwardDao.deleteInBatch(layoutForwardEntities);
			for(LayoutForwardPO layoutForwardEntity:layoutForwardEntities){
				if(LayoutForwardSourceType.COMBINE_TEMPLATE.equals(layoutForwardEntity.getSourceType())){
					combineTemplateIds.add(layoutForwardEntity.getSourceId());
				}
			}
		}
		
		//删除合屏模板
		List<CombineTemplatePO> combineTemplateEntities = combineTemplateDao.findAll(combineTemplateIds);
		if(combineTemplateEntities!=null && combineTemplateEntities.size()>0){
			combineTemplateDao.deleteInBatch(combineTemplateEntities);
		}
		
		//删除合屏模板分屏
		List<CombineTemplatePositionPO> combineTemplatePositionEntities = combineTemplatePositionDao.findByCombineTemplateIdIn(combineTemplateIds);
		if(combineTemplatePositionEntities!=null && combineTemplatePositionEntities.size()>0){
			combineTemplatePositionDao.deleteInBatch(combineTemplatePositionEntities);
		}
		
		//删除物理屏幕
		List<TerminalPhysicalScreenPO> terminalPhysicalScreenEntities = terminalPhysicalScreenDao.findByTerminalId(entity.getId());
		List<Long> terminalPhysicalScreenIds = new ArrayListWrapper<Long>().add(-1l).getList();
		if(terminalPhysicalScreenEntities!=null && terminalPhysicalScreenEntities.size()>0){
			terminalPhysicalScreenDao.deleteInBatch(terminalPhysicalScreenEntities);
			for(TerminalPhysicalScreenPO terminalPhysicalScreenEntity:terminalPhysicalScreenEntities){
				terminalPhysicalScreenIds.add(terminalPhysicalScreenEntity.getId());
			}
		}
		
		//删除物理屏幕视频解码通道关联
		List<TerminalPhysicalScreenChannelPermissionPO> terminalPhysicalScreenChannelPermissionEntities = terminalPhysicalScreenChannelPermissionDao.findByTerminalPhysicalScreenIdIn(terminalPhysicalScreenIds);
		if(terminalPhysicalScreenChannelPermissionEntities!=null && terminalPhysicalScreenChannelPermissionEntities.size()>0){
			terminalPhysicalScreenChannelPermissionDao.deleteInBatch(terminalPhysicalScreenChannelPermissionEntities);
		}
		
		//删除音频输出
		List<TerminalAudioOutputPO> terminalAudioOutputEntities = terminalAudioOutputDao.findByTerminalId(entity.getId());
		List<Long> terminalAudioOutputIds = new ArrayListWrapper<Long>().add(-1l).getList();
		if(terminalAudioOutputEntities!=null && terminalAudioOutputEntities.size()>0){
			terminalAudioOutputDao.deleteInBatch(terminalAudioOutputEntities);
			for(TerminalAudioOutputPO terminalAudioOutputEntity:terminalAudioOutputEntities){
				terminalAudioOutputIds.add(terminalAudioOutputEntity.getId());
			}
		}
		
		//删除音频输出音频解码关联
		List<TerminalAudioOutputChannelPermissionPO> terminalAudioOutputChannelPermissionEntities = terminalAudioOutputChannelPermissionDao.findByTerminalAudioOutputIdIn(terminalAudioOutputIds);
		if(terminalAudioOutputChannelPermissionEntities!=null && terminalAudioOutputChannelPermissionEntities.size()>0){
			terminalAudioOutputChannelPermissionDao.deleteInBatch(terminalAudioOutputChannelPermissionEntities);
		}
		
		//删除音视频通道关联
		List<TerminalEncodeAudioVideoChannelPermissionPO> terminalEncodeAudioVideoChannelPermissionEntities = terminalEncodeAudioVideoChannelPermissionDao.findByTerminalId(entity.getId());
		if(terminalEncodeAudioVideoChannelPermissionEntities!=null && terminalEncodeAudioVideoChannelPermissionEntities.size()>0){
			terminalEncodeAudioVideoChannelPermissionDao.deleteInBatch(terminalEncodeAudioVideoChannelPermissionEntities);
		}
		
		return new TerminalVO().set(entity);
	}
	
	/**
	 * 修改终端是否开启屏幕布局<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月30日 下午4:15:35
	 * @param Long terminalId 终端id
	 * @param Boolean enableLayout 是否开启屏幕布局
	 */
	@Transactional(rollbackFor = Exception.class)
	public void layoutEnableChange(
			Long terminalId, 
			Boolean enableLayout) throws Exception{
		
		TerminalPO terminal = terminalDao.findOne(terminalId);
		terminal.setPhysicalScreenLayout(enableLayout);
		terminalDao.save(terminal);
		
	}
	
	/**
	 * 修改布局列数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月16日 上午10:18:59
	 * @param Long terminalId 终端id
	 * @param Integer columns 列数
	 */
	@Transactional(rollbackFor = Exception.class)
	public void layoutColumnsChange(
			Long terminalId,
			Integer columns) throws Exception{
		
		TerminalPO terminal = terminalDao.findOne(terminalId);
		terminal.setColumns(columns);
		terminalDao.save(terminal);
		
		List<TerminalPhysicalScreenPO> terminalPhysicalScreenEntities = terminalPhysicalScreenDao.findByTerminalId(terminalId);
		if(terminalPhysicalScreenEntities!=null && terminalPhysicalScreenEntities.size()>0){
			for(TerminalPhysicalScreenPO terminalPhysicalScreenEntity:terminalPhysicalScreenEntities){
				terminalPhysicalScreenEntity.setCol(null);
				terminalPhysicalScreenEntity.setRow(null);
				terminalPhysicalScreenEntity.setX(null);
				terminalPhysicalScreenEntity.setY(null);
				terminalPhysicalScreenEntity.setWidth(null);
				terminalPhysicalScreenEntity.setHeight(null);
			}
		}
		terminalPhysicalScreenDao.save(terminalPhysicalScreenEntities);
	}
	
	/**
	 * 修改布局行数<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年11月16日 上午10:18:59
	 * @param Long terminalId 终端id
	 * @param Integer rows 行数
	 */
	@Transactional(rollbackFor = Exception.class)
	public void layoutRowsChange(
			Long terminalId,
			Integer rows) throws Exception{
		
		TerminalPO terminal = terminalDao.findOne(terminalId);
		terminal.setRows(rows);
		terminalDao.save(terminal);
		
		List<TerminalPhysicalScreenPO> terminalPhysicalScreenEntities = terminalPhysicalScreenDao.findByTerminalId(terminalId);
		if(terminalPhysicalScreenEntities!=null && terminalPhysicalScreenEntities.size()>0){
			for(TerminalPhysicalScreenPO terminalPhysicalScreenEntity:terminalPhysicalScreenEntities){
				terminalPhysicalScreenEntity.setCol(null);
				terminalPhysicalScreenEntity.setRow(null);
				terminalPhysicalScreenEntity.setX(null);
				terminalPhysicalScreenEntity.setY(null);
				terminalPhysicalScreenEntity.setWidth(null);
				terminalPhysicalScreenEntity.setHeight(null);
			}
		}
		terminalPhysicalScreenDao.save(terminalPhysicalScreenEntities);
	}
	
}
