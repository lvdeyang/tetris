package com.sumavision.bvc.device.command.cast;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.bvc.command.group.dao.CommandGroupDecoderSchemeDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupDecoderScreenDAO;
import com.sumavision.bvc.command.group.dao.CommandGroupUserInfoDAO;
import com.sumavision.bvc.command.group.dao.CommandVodDAO;
import com.sumavision.bvc.command.group.enumeration.SrcType;
import com.sumavision.bvc.command.group.user.CommandGroupUserInfoPO;
import com.sumavision.bvc.command.group.user.decoder.CommandGroupDecoderSchemePO;
import com.sumavision.bvc.command.group.user.decoder.CommandGroupDecoderScreenPO;
import com.sumavision.bvc.command.group.user.layout.player.CommandGroupUserPlayerCastDevicePO;
import com.sumavision.bvc.command.group.vod.CommandVodPO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.device.command.basic.osd.CommandOsdServiceImpl;
import com.sumavision.bvc.device.group.enumeration.ChannelType;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import lombok.extern.slf4j.Slf4j;

/**
 * 
* @ClassName: CommandDecoderServiceImpl 
* @Description: 解码器上屏的方案管理
* @author zsy
* @date 2020年5月13日 上午10:56:48
*
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class CommandDecoderSchemeServiceImpl {
	
	@Autowired
	private CommandGroupDecoderSchemeDAO commandGroupDecoderSchemeDao;
	
	@Autowired
	private CommandGroupDecoderScreenDAO commandGroupDecoderScreenDao;
	
	@Autowired
	private BundleDao bundleDao;
		
	@Autowired
	private CommandVodDAO commandVodDao;
	
	@Autowired
	private CommandGroupUserInfoDAO commandGroupUserInfoDao;
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private ResourceChannelDAO resourceChannelDao;
	
	@Autowired
	private CommandOsdServiceImpl commandOsdServiceImpl;
	
	@Autowired
	private CommandDecoderServiceImpl commandDecoderServiceImpl;
	
	@Autowired
	private UserUtils userUtils;
	
	/**
	 * 新建方案<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月14日 下午4:47:01
	 * @param userId
	 * @param name
	 * @return
	 * @throws BaseException
	 */
	public CommandGroupDecoderSchemePO saveScheme(Long userId, String name) throws BaseException{
		
		if(name==null || "".equals(name)){
			throw new BaseException(StatusCode.FORBIDDEN, "请输入名称");
		}
		
		CommandGroupUserInfoPO userInfo = commandGroupUserInfoDao.findByUserId(userId);
		List<CommandGroupDecoderSchemePO> decoderSchemes =  userInfo.getDecoderSchemes();
		if(decoderSchemes == null) userInfo.setDecoderSchemes(new ArrayList<CommandGroupDecoderSchemePO>());
		
		CommandGroupDecoderSchemePO scheme = new CommandGroupDecoderSchemePO();
		scheme.setUserInfo(userInfo);
		userInfo.getDecoderSchemes().add(scheme);
		scheme.setName(name);
		scheme.setScreens(new ArrayList<CommandGroupDecoderScreenPO>());
		
		for(int i=0; i<4; i++){
			CommandGroupDecoderScreenPO screen = new CommandGroupDecoderScreenPO();
			screen.setScheme(scheme);
			scheme.getScreens().add(screen);
		}
		
		commandGroupDecoderSchemeDao.save(scheme);
		commandGroupUserInfoDao.save(userInfo);
		log.info(userInfo.getUserName() + "新建上屏方案" + name);
		
		return scheme;
	}
	
	/**
	 * 删除多个方案<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月14日 下午4:47:16
	 * @param userId
	 * @param schemeIds
	 * @throws BaseException
	 */
	public void removeSchemes(Long userId, List<Long> schemeIds) throws BaseException{
		List<CommandGroupDecoderSchemePO> schemes = commandGroupDecoderSchemeDao.findAll(schemeIds);
		
		if(schemes == null || schemes.size()==0){
			throw new BaseException(StatusCode.FORBIDDEN, "没有找到任何方案，id: " + schemeIds);
		}
		
		for(CommandGroupDecoderSchemePO scheme : schemes){
			if(!scheme.getUserInfo().getUserId().equals(userId)){
				throw new BaseException(StatusCode.FORBIDDEN, "创建者才能删除方案：" + scheme.getName());//正常情况不会出现
			}
			
			//判断有没有业务在进行
			List<CommandGroupDecoderScreenPO> screens = scheme.getScreens();
			for(CommandGroupDecoderScreenPO screen : screens){
				if(!SrcType.NONE.equals(screen.getBusinessType())){
					if(screen.getCastDevices()!=null && screen.getCastDevices().size()>0){
						throw new BaseException(StatusCode.FORBIDDEN, screen.getScheme().getName() + " 方案中有设备正在上屏，请先停止");
					}
				}
			}
		}
		
		for(CommandGroupDecoderSchemePO scheme : schemes){
			scheme.getUserInfo().getDecoderSchemes().remove(scheme);
			scheme.setUserInfo(null);
		}
		
		commandGroupDecoderSchemeDao.deleteInBatch(schemes);
		log.info("删除上屏方案id: " + schemeIds);
	}
	
	/**
	 * 修改方案名称<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月14日 下午4:47:54
	 * @param userId
	 * @param schemeId
	 * @param name
	 * @throws BaseException
	 */
	public void editScheme(Long userId, Long schemeId, String name) throws BaseException{
		
		if(name==null || "".equals(name)){
			throw new BaseException(StatusCode.FORBIDDEN, "请输入名称");
		}
		
		CommandGroupDecoderSchemePO scheme = commandGroupDecoderSchemeDao.findOne(schemeId);		
		if(scheme == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有找到方案，id: " + schemeId);
		}
		
		scheme.setName(name);
		commandGroupDecoderSchemeDao.save(scheme);
	}
	
	/**
	 * 给分屏绑定多个解码器<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月14日 下午4:48:07
	 * @param userId
	 * @param screenId
	 * @param addBundleIds
	 * @return
	 * @throws Exception
	 */
	public CommandGroupDecoderScreenPO screenAddDecoders(Long userId, Long screenId, List<String> addBundleIds) throws Exception{
		UserBO user = userUtils.queryUserById(userId);
		CommandGroupDecoderScreenPO screen = commandGroupDecoderScreenDao.findOne(screenId);
		if(screen == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有找到分屏，id: " + screenId);
		}
		if(screen.getCastDevices() == null){
			screen.setCastDevices(new ArrayList<CommandGroupUserPlayerCastDevicePO>());
		}
		CommandGroupUserInfoPO userInfo = screen.getScheme().getUserInfo();
		
		//生成addDevices
		List<CommandGroupUserPlayerCastDevicePO> addDevices = new ArrayList<CommandGroupUserPlayerCastDevicePO>();
		
		//从bundleId列表查询所有的bundlePO
		List<BundlePO> dstBundleEntities = resourceBundleDao.findByBundleIds(addBundleIds);
		
		//从bundleId列表查询所有的视频编码1通道
		List<ChannelSchemeDTO> videoEncode1Channels = resourceChannelDao.findByBundleIdsAndChannelId(addBundleIds, ChannelType.VIDEODECODE1.getChannelId());
		
		//从bundleId列表查询所有的音频编码1通道
		List<ChannelSchemeDTO> audioEncode1Channels = resourceChannelDao.findByBundleIdsAndChannelId(addBundleIds, ChannelType.AUDIODECODE1.getChannelId());
		
		//考虑加个判重
		for(String addBundleId : addBundleIds){
			for(BundlePO bundle : dstBundleEntities){
				if(bundle.getBundleId().equals(addBundleId)){
					CommandGroupUserPlayerCastDevicePO device = new CommandGroupUserPlayerCastDevicePO();
					device.setUserId(userInfo.getUserId());
					device.setUserName(userInfo.getUserName());
					device.setFolderId(bundle.getFolderId());
					device.setDstBundleId(bundle.getBundleId());
					device.setDstLayerId(bundle.getAccessNodeUid());
					device.setDstBundleName(bundle.getBundleName());
					device.setDstBundleType(bundle.getDeviceModel());
					device.setDstVenusBundleType(bundle.getBundleType());
					device.setScreen(screen);
//					castDevices.add(device);
					//遍历视频通道
					for(ChannelSchemeDTO videoChannel : videoEncode1Channels){
						if(addBundleId.equals(videoChannel.getBundleId())){
							device.setDstVideoChannelId(videoChannel.getChannelId());
							break;
						}
					}					
					//遍历音频通道
					for(ChannelSchemeDTO audioChannel : audioEncode1Channels){
						if(addBundleId.equals(audioChannel.getBundleId())){
							device.setDstAudioChannelId(audioChannel.getChannelId());
							break;
						}
					}
					addDevices.add(device);
					break;
				}
			}
		}
		
		String des = user.getName() + "给上屏方案绑定解码器，";
		SrcType businessType = screen.getBusinessType();
		if(!SrcType.NONE.equals(businessType)){
			if(SrcType.FILE.equals(businessType)){
				commandDecoderServiceImpl.resourceVodStart(user, addBundleIds, screen.getPlayUrl(), screen.getSrcInfo());
				des += "文件上屏：" + screen.getSrcInfo();
			}else if(SrcType.USER.equals(businessType)){
				UserBO vodUser = userUtils.queryUserByUserno(screen.getSrcCode());
				commandDecoderServiceImpl.userStart(user, addBundleIds, vodUser);
				des += "用户上屏：" + vodUser.getName();
			}else if(SrcType.DEVICE.equals(businessType)){
				BundlePO bundle = bundleDao.findByUsername(screen.getSrcCode());
				commandDecoderServiceImpl.deviceStart(user, addBundleIds, bundle.getBundleId());
				des += "设备上屏：" + bundle.getBundleName();
			}else{
				//暂时没有其它类型了
			}
			
			//字幕
			Long osdId = screen.getOsdId();
			if(osdId != null){
				commandOsdServiceImpl.setOsd(addDevices, screen.getSrcCode(), screen.getSrcInfo(), osdId);
				des += "，有字幕，字幕id: " + osdId;
			}else{
				commandOsdServiceImpl.clearOsd(addDevices);
				des += "，没有字幕，进行清除字幕";
			}
		}
		screen.getCastDevices().addAll(addDevices);
//		commandGroupDecoderScreenDao.save(screen);//这么写，screen.getCastDevices()里边会出现重复的，原因暂不清楚
		commandGroupDecoderSchemeDao.save(screen.getScheme());
		
		des = des + "，新绑定" + addDevices.size() + "个，总个数：" + screen.getCastDevices().size();
		log.info(des);
		return screen;
	}
	
	/**
	 * 给分屏解绑多个解码器<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>zsy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年5月14日 下午4:49:06
	 * @param userId
	 * @param screenId
	 * @param removeBundleIds
	 * @return
	 * @throws Exception
	 */
	public CommandGroupDecoderScreenPO screenRemoveDecoders(Long userId, Long screenId, List<String> removeBundleIds) throws Exception{
		
		UserBO user = userUtils.queryUserById(userId);
		CommandGroupDecoderScreenPO screen = commandGroupDecoderScreenDao.findOne(screenId);
		if(screen == null){
			throw new BaseException(StatusCode.FORBIDDEN, "没有找到分屏，id: " + screenId);
		}
//		CommandGroupUserInfoPO userInfo = screen.getScheme().getUserInfo();
		
		List<CommandGroupUserPlayerCastDevicePO> castDevices = screen.getCastDevices();
		if(castDevices == null){
			screen.setCastDevices(new ArrayList<CommandGroupUserPlayerCastDevicePO>());
			castDevices = screen.getCastDevices();
		}
		
		String des = user.getName() + "给上屏方案解绑解码器，";
		List<CommandGroupUserPlayerCastDevicePO> removeDevices = new ArrayList<CommandGroupUserPlayerCastDevicePO>();
		for(CommandGroupUserPlayerCastDevicePO castDevice : castDevices){
			if(removeBundleIds.contains(castDevice.getDstBundleId())){
				removeDevices.add(castDevice);
				
				SrcType businessType = screen.getBusinessType();
				if(!SrcType.NONE.equals(businessType)){
					des += "有上屏任务";
					//这里会查出该解码器的所有上屏任务，包括绑到其他人绑定的
					List<CommandVodPO> vods = commandVodDao.findByDstBundleIdIn(new ArrayListWrapper<String>().add(castDevice.getDstBundleId()).getList());
					
					//停止上屏点播任务
					commandDecoderServiceImpl.stopVods(user, vods, true);
				}
			}
		}
		
		//字幕
		Long osdId = screen.getOsdId();
		if(osdId != null){
			commandOsdServiceImpl.clearOsd(removeDevices);
			des += "，将字幕清除";
		}
		
		castDevices.removeAll(removeDevices);
//		移除最后一个的时候，把screen清空
//		if(castDevices.size() == 0){
//			screen.setFree();
//			des += "，该分屏不再有解码器，将分屏清空";
//		}
		commandGroupDecoderScreenDao.save(screen);
		
		log.info(des + "剩余解码器个数：" + screen.getCastDevices().size());
		return screen;
	}
}
