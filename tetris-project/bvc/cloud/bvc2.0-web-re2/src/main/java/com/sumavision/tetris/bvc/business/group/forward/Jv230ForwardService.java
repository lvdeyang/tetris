//package com.sumavision.tetris.bvc.business.group.forward;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.suma.venus.resource.dao.BundleDao;
//import com.suma.venus.resource.pojo.BundlePO;
//import com.sumavision.bvc.command.group.user.layout.scheme.PlayerSplitLayout;
//import com.sumavision.bvc.device.command.common.CommandCommonServiceImpl;
//import com.sumavision.bvc.device.group.bo.CodecParamBO;
//import com.sumavision.bvc.device.group.bo.CombineAudioBO;
//import com.sumavision.bvc.device.group.bo.CombineVideoBO;
//import com.sumavision.bvc.device.group.bo.DisconnectBundleBO;
//import com.sumavision.bvc.device.group.bo.ForwardSetBO;
//import com.sumavision.bvc.device.group.bo.ForwardSetDstBO;
//import com.sumavision.bvc.device.group.bo.ForwardSetSrcBO;
//import com.sumavision.bvc.device.group.bo.LogicBO;
//import com.sumavision.bvc.device.group.bo.PositionSrcBO;
//import com.sumavision.bvc.device.group.bo.SourceBO;
//import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
//import com.sumavision.bvc.device.jv230.bo.Jv230BaseParamBO;
//import com.sumavision.bvc.device.jv230.bo.Jv230ChannelParamBO;
//import com.sumavision.bvc.device.jv230.bo.Jv230ForwardBO;
//import com.sumavision.bvc.device.jv230.bo.Jv230SourceBO;
//import com.sumavision.bvc.device.jv230.bo.PositionBO;
//import com.sumavision.tetris.bvc.model.layout.LayoutPO;
//import com.sumavision.tetris.bvc.model.layout.LayoutPositionPO;
//import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
//import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
//import com.sumavision.tetris.bvc.model.terminal.TerminalType;
//import com.sumavision.tetris.bvc.model.terminal.exception.TerminalNotFoundException;
//import com.sumavision.tetris.bvc.page.PageTaskPO;
//import com.sumavision.tetris.bvc.page.PageTaskQueryService;
//import com.sumavision.tetris.commons.exception.BaseException;
//import com.sumavision.tetris.commons.exception.code.StatusCode;
//import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
//import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
//import com.sumavision.tetris.user.UserQuery;
//import com.sumavision.tetris.user.UserVO;
//
//@Service
//public class Jv230ForwardService {
//
//	@Autowired
//	private UserQuery userQuery;
//	
//	@Autowired
//	private BundleDao bundleDao;
//	
//	@Autowired
//	private TerminalDAO terminalDao;
//	
//	//@Autowired
//	//private LayoutDAO layoutDao;
//	
//	//@Autowired
//	//private LayoutPositionDAO layoutPositionDao;
//	
//	@Autowired
//	private PageTaskQueryService pageTaskQueryService;
//	
//	@Autowired
//	private Jv230ForwardDAO jv230ForwardDao;
//	
//	@Autowired
//	private Jv230CombineAudioDAO jv230CombineAudioDao;
//	
//	@Autowired
//	private Jv230CombineAudioSrcDAO jv230CombineAudioSrcDao;
//	
//	@Autowired
//	private CommandCommonServiceImpl commandCommonServiceImpl;
//	
//	@Autowired
//	private QtTerminalCombineVideoDAO qtTerminalCombineVideoDao;
//	
//	@Autowired
//	private QtTerminalCombineVideoSrcDAO qtTerminalCombineVideoSrcDao;
//	
//	@Autowired
//	private QtTerminalForwardDAO qtTerminalForwardDao;
//	
//	@Autowired
//	private ExecuteBusinessProxy executeBusiness;
//	
//	/**
//	 * qt终端全部上屏jv230（这里默认是16视频解6音频解）（有事务）<br/>
//	 * <b>作者:</b>lvdeyang<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年7月15日 上午9:30:12
//	 * @param String bundleId jv230 设备id
//	 */
//	@Transactional(rollbackFor = Exception.class)
//	public void totalForward(String bundleId) throws Exception{
//		totalForwardWithoutTransactional(bundleId);
//	}
//	
//	/**
//	 * qt终端全部上屏jv230（这里默认是16视频解6音频解）（无事务）<br/>
//	 * <b>作者:</b>lvdeyang<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年7月15日 上午9:30:12
//	 * @param String bundleId jv230 设备id
//	 */
//	private void totalForwardWithoutTransactional(String bundleId) throws Exception{
//		UserVO user = userQuery.current();
//		TerminalType terminalType = TerminalType.fromTokenType(user.getTerminalType());
//		TerminalPO terminalEntity = terminalDao.findByType(terminalType);
//		if(terminalEntity == null){
//			throw new TerminalNotFoundException(terminalType);
//		}
//		BundlePO bundle = bundleDao.findByBundleId(bundleId);
//		if(bundle == null){
//			throw new BaseException(StatusCode.FORBIDDEN, new StringBufferWrapper().append("jv230不存在，bundleId：").append(bundleId).toString());
//		}
//		deleteForwardByBundleId(bundleId);
//		List<PageTaskPO> tasks = pageTaskQueryService.queryCurrentPageTasks(String.valueOf(user.getId()), terminalEntity.getId());
//		List<PageTaskPO> bundleTasks = new ArrayList<PageTaskPO>();
//		if(tasks!=null && tasks.size()>0){
//			for(PageTaskPO task:tasks){
//				if(task.getSrcVideoBundleId() != null){
//					bundleTasks.add(task);
//				}
//			}
//		}
//		if(bundleTasks.size() <= 0) return;
//		int splitCount = pageTaskQueryService.queryCurrentPageSize(String.valueOf(user.getId()), terminalEntity.getId());
//		String layoutName = PlayerSplitLayout.fromPlayerCount(splitCount).getName();
//		LayoutPO layout = layoutDao.findByName(layoutName);
//		if(layout == null){
//			throw new LayoutNotFoundException(layoutName);
//		}
//		List<LayoutPositionPO> layoutPositions = layoutPositionDao.findByLayoutId(layout.getId());
//		CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
//		LogicBO protocol = null;
//		if("jv230".equals(bundle.getDeviceModel())){
//			List<Jv230ForwardPO> forwards = new ArrayList<Jv230ForwardPO>();
//			for(PageTaskPO bundleTask:bundleTasks){
//				//从0开始
//				Integer serialNum = bundleTask.getLocationIndex();
//				String screenPrimaryKey = new StringBufferWrapper().append("rect_").append(serialNum + 1).toString();
//				String jv230VideoChannelId =  new StringBufferWrapper().append("VenusVideoOut_").append(serialNum + 1).toString();
//				Jv230ForwardPO videoForward = new Jv230ForwardPO();
//				videoForward.setLayerId(bundle.getAccessNodeUid());
//				videoForward.setBundleId(bundle.getBundleId());
//				videoForward.setChannelId(jv230VideoChannelId);
//				videoForward.setSerialNum(serialNum);
//				LayoutPositionPO targetPosition = null;
//				for(LayoutPositionPO position:layoutPositions){
//					if(position.getScreenPrimaryKey().equals(screenPrimaryKey)){
//						targetPosition = position;
//						break;
//					}
//				}
//				videoForward.setX(Integer.parseInt(targetPosition.getX()));
//				videoForward.setY(Integer.parseInt(targetPosition.getY()));
//				videoForward.setW(Integer.parseInt(targetPosition.getWidth()));
//				videoForward.setH(Integer.parseInt(targetPosition.getHeight()));
//				videoForward.setBusinessName(bundleTask.getBusinessName());
//				videoForward.setSourceType(ForwardSourceType.CHANNEL_VIDEO);
//				videoForward.setSourceLayerId(bundleTask.getSrcVideoLayerId());
//				videoForward.setSourceBundleId(bundleTask.getSrcVideoBundleId());
//				videoForward.setSourceChannelId(bundleTask.getSrcVideoChannelId());
//				videoForward.setUserId(String.valueOf(user.getId()));
//				videoForward.setTerminalId(terminalEntity.getId());
//				videoForward.setBusinessType(ForwardBusinessType.QT_TOTAL_FORWARD);
//				forwards.add(videoForward);
//			}
//			//处理音频（混音）
//			Jv230CombineAudioPO combineAudio = new Jv230CombineAudioPO();
//			combineAudio.setUpdateTime(new Date());
//			
//			jv230CombineAudioDao.save(combineAudio);
//			List<Jv230CombineAudioSrcPO> combineAudioSrcs = new ArrayList<Jv230CombineAudioSrcPO>();
//			for(PageTaskPO bundleTask:bundleTasks){
//				Jv230CombineAudioSrcPO combineAudioSrc = new Jv230CombineAudioSrcPO();
//				combineAudioSrc.setSourceLayerId(bundleTask.getSrcAudioLayerId());
//				combineAudioSrc.setSourceBundleId(bundleTask.getSrcAudioBundleId());
//				combineAudioSrc.setSourceChannelId(bundleTask.getSrcAudioChannelId());
//				combineAudioSrc.setSerialNum(bundleTask.getLocationIndex());
//				combineAudioSrc.setJv230CombineAudioId(combineAudio.getId());
//				combineAudioSrc.setUpdateTime(new Date());
//				combineAudioSrcs.add(combineAudioSrc);
//			}
//			jv230CombineAudioSrcDao.save(combineAudioSrcs);
//			Jv230ForwardPO audioForward = new Jv230ForwardPO();
//			audioForward.setLayerId(bundle.getAccessNodeUid());
//			audioForward.setBundleId(bundle.getBundleId());
//			audioForward.setChannelId("VenusAudioOut_1");
//			audioForward.setSourceType(ForwardSourceType.COMBINE_AUDIO);
//			audioForward.setSourceBundleId(combineAudio.getUuid());
//			audioForward.setUserId(String.valueOf(user.getId()));
//			audioForward.setTerminalId(terminalEntity.getId());
//			audioForward.setBusinessType(ForwardBusinessType.QT_TOTAL_FORWARD);
//			audioForward.setUpdateTime(new Date());
//			forwards.add(audioForward);
//			jv230ForwardDao.save(forwards);
//			
//			//生成协议
//			protocol = new LogicBO().setUserId(user.getId().toString())
//									.setCombineAudioSet(new ArrayList<CombineAudioBO>())
//									.setJv230AudioSet(new ArrayList<Jv230ForwardBO>())
//									.setJv230ForwardSet(new ArrayList<Jv230ForwardBO>());
//			
//			CombineAudioBO combineAudioProtocol = new CombineAudioBO().setUuid(combineAudio.getUuid())
//																	  .setCodec_param(codec)
//																	  .setSrc(new ArrayList<SourceBO>());
//			for(Jv230CombineAudioSrcPO combineAudioSrc:combineAudioSrcs){
//				SourceBO combineAudioSrcProtocol = new SourceBO().setLayerId(combineAudioSrc.getSourceLayerId())
//																 .setBundleId(combineAudioSrc.getSourceBundleId())
//																 .setChannelId(combineAudioSrc.getSourceChannelId());
//				combineAudioProtocol.getSrc().add(combineAudioSrcProtocol);
//			}
//			protocol.getCombineAudioSet().add(combineAudioProtocol);
//			
//			for(Jv230ForwardPO forward:forwards){
//				if(ForwardSourceType.CHANNEL_VIDEO.equals(forward.getSourceType())){
//					//处理视频转发
//					Jv230ForwardBO videoForwardProtocol = new Jv230ForwardBO().setLayerId(forward.getLayerId())
//																			  .setBundleId(forward.getBundleId())
//																			  .setChannelId(forward.getChannelId())
//																			  .setChannel_param(new Jv230ChannelParamBO());
//					videoForwardProtocol.getChannel_param().setBase_type("VideoMatrixVideoOut")
//														   .setBase_param(new Jv230BaseParamBO());
//					videoForwardProtocol.getChannel_param().getBase_param().setCodec_type(codec.getVideo_param().getCodec())
//																		   .setIs_polling("false")
//																		   .setSrc_mode(0)
//																		   .setDisplay_rect(new PositionBO())
//																		   .setSources(new ArrayList<Jv230SourceBO>());
//					videoForwardProtocol.getChannel_param().getBase_param().getDisplay_rect().setX(forward.getX())
//																							 .setY(forward.getY())
//																							 .setWidth(forward.getW())
//																							 .setHeight(forward.getH())
//																							 .setZ_index(1);
//					videoForwardProtocol.getChannel_param().getBase_param().getSources().add(new Jv230SourceBO()
//																						.setLayer_id(forward.getSourceLayerId())
//																					   .setBundle_id(forward.getSourceBundleId())
//																					   .setChannel_id(forward.getSourceChannelId()));
//					protocol.getJv230ForwardSet().add(videoForwardProtocol);
//				}else if(ForwardSourceType.CHANNEL_AUDIO.equals(forward.getSourceType())){
//					//处理音频转发
//				}else if(ForwardSourceType.COMBINE_AUDIO.equals(forward.getSourceType())){
//					//处理混音
//					Jv230ForwardBO audioForwardProtocol = new Jv230ForwardBO().setLayerId(forward.getLayerId())
//																			  .setBundleId(forward.getBundleId())
//																			  .setChannelId(forward.getChannelId())
//																			  .setChannel_param(new Jv230ChannelParamBO());
//					audioForwardProtocol.getChannel_param().setBase_type("VideoMatrixAudioOut")
//						   								   .setBase_param(new Jv230BaseParamBO());
//					audioForwardProtocol.getChannel_param().getBase_param()
//														   .setSource(new Jv230SourceBO())
//														   .setCodec(codec.getAudio_param().getCodec());
//					audioForwardProtocol.getChannel_param().getBase_param().getSource().setType("combineAudio")
//																					   .setUuid(forward.getSourceBundleId());
//					protocol.getJv230AudioSet().add(audioForwardProtocol);
//				}
//			}
//		}else{
//			List<QtTerminalCombineVideoPO> terminalCombineVideos = qtTerminalCombineVideoDao.findByUserIdAndTerminalId(user.getId().toString(), terminalEntity.getId());
//			QtTerminalCombineVideoPO terminalCombineVideo = null;
//			List<QtTerminalCombineVideoSrcPO> terminalCombineVideoSrcs = new ArrayList<QtTerminalCombineVideoSrcPO>();
//			if(terminalCombineVideos==null || terminalCombineVideos.size()<=0){
//				terminalCombineVideo = new QtTerminalCombineVideoPO();
//				terminalCombineVideo.setUserId(user.getId().toString());
//				terminalCombineVideo.setTerminalId(terminalEntity.getId());
//				terminalCombineVideo.setBusinessType(ForwardBusinessType.QT_TOTAL_FORWARD);
//				terminalCombineVideo.setUpdateTime(new Date());
//				qtTerminalCombineVideoDao.save(terminalCombineVideo);
//				for(PageTaskPO bundleTask:bundleTasks){
//					QtTerminalCombineVideoSrcPO terminalCombineVideoSrc = new QtTerminalCombineVideoSrcPO();
//					Integer serialNum = bundleTask.getLocationIndex();
//					String screenPrimaryKey = new StringBufferWrapper().append("rect_").append(serialNum + 1).toString();
//					terminalCombineVideoSrc.setSourceLayerId(bundleTask.getSrcVideoLayerId());
//					terminalCombineVideoSrc.setSourceBundleId(bundleTask.getSrcVideoBundleId());
//					terminalCombineVideoSrc.setSourceChannelId(bundleTask.getSrcVideoChannelId());
//					terminalCombineVideoSrc.setSerialNum(serialNum);
//					LayoutPositionPO targetPosition = null;
//					for(LayoutPositionPO position:layoutPositions){
//						if(position.getScreenPrimaryKey().equals(screenPrimaryKey)){
//							targetPosition = position;
//							break;
//						}
//					}
//					terminalCombineVideoSrc.setX(Integer.parseInt(targetPosition.getX()));
//					terminalCombineVideoSrc.setY(Integer.parseInt(targetPosition.getY()));
//					terminalCombineVideoSrc.setW(Integer.parseInt(targetPosition.getWidth()));
//					terminalCombineVideoSrc.setH(Integer.parseInt(targetPosition.getHeight()));
//					terminalCombineVideoSrc.setBusinessName(bundleTask.getBusinessName());
//					terminalCombineVideoSrc.setQtTerminalCombineVideoId(terminalCombineVideo.getId());
//					terminalCombineVideoSrcs.add(terminalCombineVideoSrc);
//					qtTerminalCombineVideoSrcDao.save(terminalCombineVideoSrcs);
//				}
//			}else{
//				terminalCombineVideo = terminalCombineVideos.get(0);
//			}
//			
//			QtTerminalForwardPO videoForward = new QtTerminalForwardPO();
//			videoForward.setSourceId(terminalCombineVideo.getUuid());
//			videoForward.setSourceType(ForwardSourceType.COMBINE_VIDEO);
//			videoForward.setLayerId(bundle.getAccessNodeUid());
//			videoForward.setBundleId(bundle.getBundleId());
//			videoForward.setChannelId("VenusVideoOut_1");
//			videoForward.setUserId(user.getId().toString());
//			videoForward.setTerminalId(terminalEntity.getId());
//			videoForward.setBusinessType(ForwardBusinessType.QT_TOTAL_FORWARD);
//			videoForward.setUpdateTime(new Date());
//			qtTerminalForwardDao.save(videoForward);
//			
//			//处理音频（混音）
//			Jv230CombineAudioPO combineAudio = new Jv230CombineAudioPO();
//			combineAudio.setUpdateTime(new Date());
//			
//			jv230CombineAudioDao.save(combineAudio);
//			List<Jv230CombineAudioSrcPO> combineAudioSrcs = new ArrayList<Jv230CombineAudioSrcPO>();
//			for(PageTaskPO bundleTask:bundleTasks){
//				Jv230CombineAudioSrcPO combineAudioSrc = new Jv230CombineAudioSrcPO();
//				combineAudioSrc.setSourceLayerId(bundleTask.getSrcAudioLayerId());
//				combineAudioSrc.setSourceBundleId(bundleTask.getSrcAudioBundleId());
//				combineAudioSrc.setSourceChannelId(bundleTask.getSrcAudioChannelId());
//				combineAudioSrc.setSerialNum(bundleTask.getLocationIndex());
//				combineAudioSrc.setJv230CombineAudioId(combineAudio.getId());
//				combineAudioSrc.setUpdateTime(new Date());
//				combineAudioSrcs.add(combineAudioSrc);
//			}
//			jv230CombineAudioSrcDao.save(combineAudioSrcs);
//			
//			QtTerminalForwardPO audioForward = new QtTerminalForwardPO();
//			audioForward.setSourceId(combineAudio.getUuid());
//			audioForward.setSourceType(ForwardSourceType.COMBINE_AUDIO);
//			audioForward.setLayerId(bundle.getAccessNodeUid());
//			audioForward.setBundleId(bundle.getBundleId());
//			audioForward.setChannelId("VenusAudioOut_1");
//			audioForward.setUserId(user.getId().toString());
//			audioForward.setTerminalId(terminalEntity.getId());
//			audioForward.setBusinessType(ForwardBusinessType.QT_TOTAL_FORWARD);
//			audioForward.setUpdateTime(new Date());
//			qtTerminalForwardDao.save(audioForward);
//			
//			//生成协议
//			protocol = new LogicBO().setUserId(user.getId().toString())
//									.setCombineVideoSet(new ArrayList<CombineVideoBO>())
//									.setCombineAudioSet(new ArrayList<CombineAudioBO>())
//									.setForwardSet(new ArrayList<ForwardSetBO>());
//			
//			CombineVideoBO combineVideoProtocol = new CombineVideoBO().setUuid(terminalCombineVideo.getUuid())
//																	  .setCodec_param(codec)
//																	  .setPosition(new ArrayList<PositionSrcBO>());
//			for(QtTerminalCombineVideoSrcPO terminalCombineVideoSrc:terminalCombineVideoSrcs){
//				PositionSrcBO position = new PositionSrcBO().setX(terminalCombineVideoSrc.getX())
//															.setY(terminalCombineVideoSrc.getY())
//															.setW(terminalCombineVideoSrc.getW())
//															.setH(terminalCombineVideoSrc.getH())
//															.setSrc(new ArrayList<SourceBO>());
//				SourceBO src = new SourceBO().setLayerId(terminalCombineVideoSrc.getSourceLayerId())
//											 .setBundleId(terminalCombineVideoSrc.getSourceBundleId())
//											 .setChannelId(terminalCombineVideoSrc.getSourceChannelId());
//				position.getSrc().add(src);
//				combineVideoProtocol.getPosition().add(position);
//			}		
//			protocol.getCombineVideoSet().add(combineVideoProtocol);
//			
//			ForwardSetBO videoForwardProtocol = new ForwardSetBO().setSrc(new ForwardSetSrcBO())
//														  		  .setDst(new ForwardSetDstBO());
//			videoForwardProtocol.getSrc().setType("combineVideo")
//										 .setUuid(videoForward.getSourceId());
//			videoForwardProtocol.getDst().setLayerId(videoForward.getLayerId())
//										 .setBundleId(videoForward.getBundleId())
//										 .setChannelId(videoForward.getChannelId())
//										 .setBase_type("VenusVideoOut")
//										 .setBundle_type(bundle.getBundleType())
//										 .setCodec_param(codec);
//			protocol.getForwardSet().add(videoForwardProtocol);
//			
//			CombineAudioBO combineAudioProtocol = new CombineAudioBO().setUuid(combineAudio.getUuid())
//																	  .setCodec_param(codec)
//																	  .setSrc(new ArrayList<SourceBO>());
//			for(Jv230CombineAudioSrcPO combineAudioSrc:combineAudioSrcs){
//				SourceBO combineAudioSrcProtocol = new SourceBO().setLayerId(combineAudioSrc.getSourceLayerId())
//																 .setBundleId(combineAudioSrc.getSourceBundleId())
//																 .setChannelId(combineAudioSrc.getSourceChannelId());
//				combineAudioProtocol.getSrc().add(combineAudioSrcProtocol);
//			}
//			protocol.getCombineAudioSet().add(combineAudioProtocol);
//			
//			ForwardSetBO audioForwardProtocol = new ForwardSetBO().setSrc(new ForwardSetSrcBO())
//														  		  .setDst(new ForwardSetDstBO());
//			audioForwardProtocol.getSrc().setType("combineAudio")
//										 .setUuid(combineAudio.getUuid());
//			audioForwardProtocol.getDst().setLayerId(audioForward.getLayerId())
//										 .setBundleId(audioForward.getBundleId())
//										 .setChannelId(audioForward.getChannelId())
//										 .setBase_type("VenusAudioOut")
//										 .setBundle_type(bundle.getBundleType())
//										 .setCodec_param(codec);
//			protocol.getForwardSet().add(audioForwardProtocol);
//		}
//		
//		executeBusiness.execute(protocol, new StringBufferWrapper().append("qt终端为jv230全部上屏，bunldeId：").append(bundleId).toString());
//	}
//	
//	/**
//	 * qt终端切换分屏<br/>
//	 * <b>作者:</b>lvdeyang<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年7月15日 上午9:31:02
//	 */
//	@Transactional(rollbackFor = Exception.class)
//	public void changeSplit() throws Exception{
//		UserVO user = userQuery.current();
//		TerminalType terminalType = TerminalType.fromTokenType(user.getTerminalType());
//		TerminalPO terminalEntity = terminalDao.findByType(terminalType);
//		if(terminalEntity == null){
//			throw new TerminalNotFoundException(terminalType);
//		}
//		List<String> bundleIds = jv230ForwardDao.findDistinctBundleIdByUserIdAndTerminalIdAndBusinessType(String.valueOf(user.getId()), terminalEntity.getId(), ForwardBusinessType.QT_TOTAL_FORWARD);
//		if(bundleIds==null || bundleIds.size()<=0) return;
//		deleteTotalForwardWithoutTransactional();
//		for(String bundleId:bundleIds){
//			totalForwardWithoutTransactional(bundleId);
//		}
//	}
//	
//	/**
//	 * qt终端某个分屏内容变化<br/>
//	 * <b>作者:</b>lvdeyang<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年7月15日 上午9:31:24
//	 * @param int serialNum 分屏序号
//	 */
//	@Transactional(rollbackFor = Exception.class)
//	public void changeForwardBySerialNum(int serialNum) throws Exception{
//		UserVO user = userQuery.current();
//		TerminalType terminalType = TerminalType.fromTokenType(user.getTerminalType());
//		TerminalPO terminalEntity = terminalDao.findByType(terminalType);
//		if(terminalEntity == null){
//			throw new TerminalNotFoundException(terminalType);
//		}
//		List<String> bundleIds = jv230ForwardDao.findDistinctBundleIdByUserIdAndTerminalIdAndBusinessType(String.valueOf(user.getId()), terminalEntity.getId(), ForwardBusinessType.QT_TOTAL_FORWARD);
//		List<BundlePO> bundles = bundleDao.findByBundleIdIn(bundleIds);
//		deleteForwardBySerialNumWithoutTransactional(serialNum);
//		PageTaskPO task = pageTaskQueryService.queryPageTask(String.valueOf(user.getId()), terminalEntity.getId(), serialNum);
//		if(task.getSrcVideoBundleId() == null) return;
//		PageTaskPO bundleTask = task;
//		int splitCount = pageTaskQueryService.queryCurrentPageSize(String.valueOf(user.getId()), terminalEntity.getId());
//		String layoutName = PlayerSplitLayout.fromPlayerCount(splitCount).getName();
//		LayoutPO layout = layoutDao.findByName(layoutName);
//		if(layout == null){
//			throw new LayoutNotFoundException(layoutName);
//		}
//		List<LayoutPositionPO> layoutPositions = layoutPositionDao.findByLayoutId(layout.getId());
//		List<Jv230ForwardPO> forwards = new ArrayList<Jv230ForwardPO>();
//		boolean exceptJv230 = false;
//		//处理jv230视频
//		for(BundlePO bundle:bundles){
//			if(!"jv230".equals(bundle.getDeviceModel())) exceptJv230 = true;
//			//从0开始
//			String screenPrimaryKey = new StringBufferWrapper().append("rect_").append(serialNum + 1).toString();
//			String jv230VideoChannelId =  new StringBufferWrapper().append("VenusVideoOut_").append(serialNum + 1).toString();
//			Jv230ForwardPO videoForward = new Jv230ForwardPO();
//			videoForward.setLayerId(bundle.getAccessNodeUid());
//			videoForward.setBundleId(bundle.getBundleId());
//			videoForward.setChannelId(jv230VideoChannelId);
//			videoForward.setSerialNum(serialNum);
//			LayoutPositionPO targetPosition = null;
//			for(LayoutPositionPO position:layoutPositions){
//				if(position.getScreenPrimaryKey().equals(screenPrimaryKey)){
//					targetPosition = position;
//					break;
//				}
//			}
//			videoForward.setX(Integer.parseInt(targetPosition.getX()));
//			videoForward.setY(Integer.parseInt(targetPosition.getY()));
//			videoForward.setW(Integer.parseInt(targetPosition.getWidth()));
//			videoForward.setH(Integer.parseInt(targetPosition.getHeight()));
//			videoForward.setBusinessName(bundleTask.getBusinessName());
//			videoForward.setSourceType(ForwardSourceType.CHANNEL_VIDEO);
//			videoForward.setSourceLayerId(bundleTask.getSrcVideoLayerId());
//			videoForward.setSourceBundleId(bundleTask.getSrcVideoBundleId());
//			videoForward.setSourceChannelId(bundleTask.getSrcVideoChannelId());
//			videoForward.setUserId(String.valueOf(user.getId()));
//			videoForward.setTerminalId(terminalEntity.getId());
//			videoForward.setBusinessType(ForwardBusinessType.QT_TOTAL_FORWARD);
//			forwards.add(videoForward);
//		}
//		jv230ForwardDao.save(forwards);
//		
//		//协议
//		LogicBO protocol = new LogicBO().setUserId(user.getId().toString())
//										.setCombineVideoUpdate(new ArrayList<CombineVideoBO>())
//										.setCombineAudioUpdate(new ArrayList<CombineAudioBO>())
//										.setJv230ForwardSet(new ArrayList<Jv230ForwardBO>());
//		CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
//		if(exceptJv230){
//			//处理设备合屏
//			List<QtTerminalCombineVideoPO> combineVideos = qtTerminalCombineVideoDao.findByUserIdAndTerminalId(user.getId().toString(), terminalEntity.getId());
//			QtTerminalCombineVideoPO combineVideo = combineVideos.get(0);
//			List<QtTerminalCombineVideoSrcPO> combineVideoSrcs = qtTerminalCombineVideoSrcDao.findByQtTerminalCombineVideoId(combineVideo.getId());
//			List<QtTerminalCombineVideoSrcPO> willDeleteCombineVideoSrcs = new ArrayList<QtTerminalCombineVideoSrcPO>();
//			List<QtTerminalCombineVideoSrcPO> willAddCombineVideoSrcs = new ArrayList<QtTerminalCombineVideoSrcPO>();
//			List<QtTerminalCombineVideoSrcPO> leftCombineVideoSrcs = new ArrayList<QtTerminalCombineVideoSrcPO>();
//			if(combineVideoSrcs!=null && combineVideoSrcs.size()>0){
//				for(QtTerminalCombineVideoSrcPO combineVideoSrc:combineVideoSrcs){
//					if(combineVideoSrc.getSerialNum() == serialNum){
//						willDeleteCombineVideoSrcs.add(combineVideoSrc);
//					}else{
//						leftCombineVideoSrcs.add(combineVideoSrc);
//					}
//				}
//			}
//			QtTerminalCombineVideoSrcPO combineVideoSrc = new QtTerminalCombineVideoSrcPO();
//			String screenPrimaryKey = new StringBufferWrapper().append("rect_").append(serialNum + 1).toString();
//			combineVideoSrc.setSourceLayerId(bundleTask.getSrcVideoLayerId());
//			combineVideoSrc.setSourceBundleId(bundleTask.getSrcVideoBundleId());
//			combineVideoSrc.setSourceChannelId(bundleTask.getSrcVideoChannelId());
//			combineVideoSrc.setSerialNum(serialNum);
//			LayoutPositionPO targetPosition = null;
//			for(LayoutPositionPO position:layoutPositions){
//				if(position.getScreenPrimaryKey().equals(screenPrimaryKey)){
//					targetPosition = position;
//					break;
//				}
//			}
//			combineVideoSrc.setX(Integer.parseInt(targetPosition.getX()));
//			combineVideoSrc.setY(Integer.parseInt(targetPosition.getY()));
//			combineVideoSrc.setW(Integer.parseInt(targetPosition.getWidth()));
//			combineVideoSrc.setH(Integer.parseInt(targetPosition.getHeight()));
//			combineVideoSrc.setBusinessName(bundleTask.getBusinessName());
//			combineVideoSrc.setQtTerminalCombineVideoId(combineVideo.getId());
//			willAddCombineVideoSrcs.add(combineVideoSrc);
//			if(willDeleteCombineVideoSrcs.size() > 0){
//				qtTerminalCombineVideoSrcDao.deleteInBatch(willDeleteCombineVideoSrcs);
//			}
//			if(willAddCombineVideoSrcs.size() > 0){
//				qtTerminalCombineVideoSrcDao.save(willAddCombineVideoSrcs);
//				leftCombineVideoSrcs.addAll(willAddCombineVideoSrcs);
//			}
//			CombineVideoBO combineVideoProtocol = new CombineVideoBO().setUuid(combineVideo.getUuid())
//																	  .setCodec_param(codec)
//																	  .setPosition(new ArrayList<PositionSrcBO>());
//			for(QtTerminalCombineVideoSrcPO terminalCombineVideoSrc:leftCombineVideoSrcs){
//				PositionSrcBO position = new PositionSrcBO().setX(terminalCombineVideoSrc.getX())
//															.setY(terminalCombineVideoSrc.getY())
//															.setW(terminalCombineVideoSrc.getW())
//															.setH(terminalCombineVideoSrc.getH())
//															.setSrc(new ArrayList<SourceBO>());
//				SourceBO src = new SourceBO().setLayerId(terminalCombineVideoSrc.getSourceLayerId())
//											 .setBundleId(terminalCombineVideoSrc.getSourceBundleId())
//											 .setChannelId(terminalCombineVideoSrc.getSourceChannelId());
//				position.getSrc().add(src);
//				combineVideoProtocol.getPosition().add(position);
//			}		
//			protocol.getCombineVideoUpdate().add(combineVideoProtocol);
//		}
//		
//		//处理音频（混音）
//		List<Jv230ForwardPO> audioForwards = jv230ForwardDao.findByBundleIdInAndSourceTypeInAndUserIdAndTerminalIdAndBusinessType(bundleIds, new ArrayListWrapper<ForwardSourceType>().add(ForwardSourceType.COMBINE_AUDIO).add(ForwardSourceType.CHANNEL_AUDIO).getList(), String.valueOf(user.getId()), terminalEntity.getId(), ForwardBusinessType.QT_TOTAL_FORWARD);
//		Set<String> combineAudioUuids = new HashSet<String>();
//		if(audioForwards!=null && audioForwards.size()>0){
//			for(Jv230ForwardPO audioForward:audioForwards){
//				combineAudioUuids.add(audioForward.getSourceBundleId());
//			}
//		}
//		List<QtTerminalForwardPO> exceptJv230AudioForwards = qtTerminalForwardDao.findByUserIdAndTerminalIdAndBusinessTypeAndSourceType(user.getId().toString(), terminalEntity.getId(), ForwardBusinessType.QT_TOTAL_FORWARD, ForwardSourceType.COMBINE_AUDIO);
//		if(exceptJv230AudioForwards!=null && exceptJv230AudioForwards.size()>0){
//			for(QtTerminalForwardPO exceptJv230AudioForward:exceptJv230AudioForwards){
//				combineAudioUuids.add(exceptJv230AudioForward.getSourceId());
//			}
//		}
//		List<Jv230CombineAudioPO> combineAudios = jv230CombineAudioDao.findByUuidIn(combineAudioUuids);
//		List<Long> combineAudioIds = new ArrayList<Long>();
//		for(Jv230CombineAudioPO combineAudio:combineAudios){
//			combineAudioIds.add(combineAudio.getId());
//		}
//		List<Jv230CombineAudioSrcPO> combineAudioSrcs = jv230CombineAudioSrcDao.findByJv230CombineAudioIdIn(combineAudioIds);
//		List<Jv230CombineAudioSrcPO> willDeleteCombineAudioSrcs = new ArrayList<Jv230CombineAudioSrcPO>();
//		List<Jv230CombineAudioSrcPO> willAddCombineAudiosSrcs = new ArrayList<Jv230CombineAudioSrcPO>();
//		List<Jv230CombineAudioSrcPO> leftCombineAudioSrcs = new ArrayList<Jv230CombineAudioSrcPO>();
//		if(combineAudioSrcs!=null && combineAudioSrcs.size()>0){
//			for(Jv230CombineAudioSrcPO combineAudioSrc:combineAudioSrcs){
//				if(combineAudioSrc.getSerialNum() == serialNum){
//					willDeleteCombineAudioSrcs.add(combineAudioSrc);
//				}else{
//					leftCombineAudioSrcs.add(combineAudioSrc);
//				}
//			}
//		}
//		for(Jv230CombineAudioPO combineAudio:combineAudios){
//			Jv230CombineAudioSrcPO combineAudioSrc = new Jv230CombineAudioSrcPO();
//			combineAudioSrc.setSerialNum(serialNum);
//			combineAudioSrc.setJv230CombineAudioId(combineAudio.getId());
//			combineAudioSrc.setSourceLayerId(bundleTask.getSrcAudioLayerId());
//			combineAudioSrc.setSourceBundleId(bundleTask.getSrcAudioBundleId());
//			combineAudioSrc.setSourceChannelId(bundleTask.getSrcAudioChannelId());
//			combineAudioSrc.setUpdateTime(new Date());
//			willAddCombineAudiosSrcs.add(combineAudioSrc);
//		}
//		if(willDeleteCombineAudioSrcs.size() > 0){
//			jv230CombineAudioSrcDao.deleteInBatch(willDeleteCombineAudioSrcs);
//		}
//		if(willAddCombineAudiosSrcs.size() > 0){
//			jv230CombineAudioSrcDao.save(willAddCombineAudiosSrcs);
//			leftCombineAudioSrcs.addAll(willAddCombineAudiosSrcs);
//		}
//		
//		//混音协议
//		for(Jv230CombineAudioPO combineAudio:combineAudios){
//			CombineAudioBO combineAudioProtocol = new CombineAudioBO().setUuid(combineAudio.getUuid())
//																	  .setCodec_param(codec)
//																	  .setSrc(new ArrayList<SourceBO>());
//			for(Jv230CombineAudioSrcPO combineAudioSrc:combineAudioSrcs){
//				if(combineAudioSrc.getJv230CombineAudioId().equals(combineAudio.getId())){
//					SourceBO combineAudioSrcProtocol = new SourceBO().setLayerId(combineAudioSrc.getSourceLayerId())
//																	 .setBundleId(combineAudioSrc.getSourceBundleId())
//																	 .setChannelId(combineAudioSrc.getSourceChannelId());
//					combineAudioProtocol.getSrc().add(combineAudioSrcProtocol);
//				}
//			}
//			protocol.getCombineAudioUpdate().add(combineAudioProtocol);
//		}
//		for(Jv230ForwardPO forward:forwards){
//			if(ForwardSourceType.CHANNEL_VIDEO.equals(forward.getSourceType())){
//				//处理视频转发
//				Jv230ForwardBO videoForwardProtocol = new Jv230ForwardBO().setLayerId(forward.getLayerId())
//																		  .setBundleId(forward.getBundleId())
//																		  .setChannelId(forward.getChannelId())
//																		  .setChannel_param(new Jv230ChannelParamBO());
//				videoForwardProtocol.getChannel_param().setBase_type("VideoMatrixVideoOut")
//													   .setBase_param(new Jv230BaseParamBO());
//				videoForwardProtocol.getChannel_param().getBase_param().setCodec_type(codec.getVideo_param().getCodec())
//																	   .setIs_polling("false")
//																	   .setSrc_mode(0)
//																	   .setDisplay_rect(new PositionBO())
//																	   .setSource(new Jv230SourceBO());
//				videoForwardProtocol.getChannel_param().getBase_param().getDisplay_rect().setX(forward.getX())
//																						 .setY(forward.getY())
//																						 .setWidth(forward.getW())
//																						 .setHeight(forward.getH())
//																						 .setZ_index(1);
//				videoForwardProtocol.getChannel_param().getBase_param().getSources().add(new Jv230SourceBO()
//																					.setLayer_id(forward.getSourceLayerId())
//																				   .setBundle_id(forward.getSourceBundleId())
//																				   .setChannel_id(forward.getSourceChannelId()));
//				protocol.getJv230ForwardSet().add(videoForwardProtocol);
//			}else if(ForwardSourceType.CHANNEL_AUDIO.equals(forward.getSourceType())){
//				//处理音频转发
//			}else if(ForwardSourceType.COMBINE_AUDIO.equals(forward.getSourceType())){
//				//处理混音
//			}
//		}
//		executeBusiness.execute(protocol, new StringBufferWrapper().append("qt终端修改分屏内容，serianlNum：").append(serialNum).toString());
//	}
//	
//	/**
//	 * qt终端某个分屏内容变化<br/>
//	 * <b>作者:</b>lvdeyang<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年7月15日 上午9:31:24
//	 * @param String bundleId jv230设备id
//	 * @param int serialNum 分屏序号
//	 * @return Jv230ForwardVO 视频转发信息
//	 */
//	@Transactional(rollbackFor = Exception.class)
//	public Jv230ForwardVO changeForwardByBundleIdAndSerialNum(String bundleId, int serialNum) throws Exception{
//		UserVO user = userQuery.current();
//		TerminalType terminalType = TerminalType.fromTokenType(user.getTerminalType());
//		TerminalPO terminalEntity = terminalDao.findByType(terminalType);
//		if(terminalEntity == null){
//			throw new TerminalNotFoundException(terminalType);
//		}
//		BundlePO bundle = bundleDao.findByBundleId(bundleId);
//		deleteForwardByBundleIdAndSerialNumWithoutTransactional(bundleId, serialNum);
//		PageTaskPO task = pageTaskQueryService.queryPageTask(String.valueOf(user.getId()), terminalEntity.getId(), serialNum);
//		if(task.getSrcVideoBundleId() == null) return null;
//		PageTaskPO bundleTask = task;
//		int splitCount = pageTaskQueryService.queryCurrentPageSize(String.valueOf(user.getId()), terminalEntity.getId());
//		String layoutName = PlayerSplitLayout.fromPlayerCount(splitCount).getName();
//		LayoutPO layout = layoutDao.findByName(layoutName);
//		if(layout == null){
//			throw new LayoutNotFoundException(layoutName);
//		}
//		List<LayoutPositionPO> layoutPositions = layoutPositionDao.findByLayoutId(layout.getId());
//		List<Jv230ForwardPO> forwards = new ArrayList<Jv230ForwardPO>();
//		//生成协议
//		LogicBO protocol = new LogicBO().setUserId(user.getId().toString())
//										.setCombineVideoUpdate(new ArrayList<CombineVideoBO>())
//										.setCombineAudioUpdate(new ArrayList<CombineAudioBO>())
//										.setJv230ForwardSet(new ArrayList<Jv230ForwardBO>());
//		CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
//		//从0开始
//		String screenPrimaryKey = new StringBufferWrapper().append("rect_").append(serialNum + 1).toString();
//		String jv230VideoChannelId =  new StringBufferWrapper().append("VenusVideoOut_").append(serialNum + 1).toString();
//		Jv230ForwardPO videoForward = new Jv230ForwardPO();
//		videoForward.setLayerId(bundle.getAccessNodeUid());
//		videoForward.setBundleId(bundle.getBundleId());
//		videoForward.setChannelId(jv230VideoChannelId);
//		videoForward.setSerialNum(serialNum);
//		LayoutPositionPO targetPosition = null;
//		for(LayoutPositionPO position:layoutPositions){
//			if(position.getScreenPrimaryKey().equals(screenPrimaryKey)){
//				targetPosition = position;
//				break;
//			}
//		}
//		videoForward.setX(Integer.parseInt(targetPosition.getX()));
//		videoForward.setY(Integer.parseInt(targetPosition.getY()));
//		videoForward.setW(Integer.parseInt(targetPosition.getWidth()));
//		videoForward.setH(Integer.parseInt(targetPosition.getHeight()));
//		videoForward.setBusinessName(bundleTask.getBusinessName());
//		videoForward.setSourceType(ForwardSourceType.CHANNEL_VIDEO);
//		videoForward.setSourceLayerId(bundleTask.getSrcVideoLayerId());
//		videoForward.setSourceBundleId(bundleTask.getSrcVideoBundleId());
//		videoForward.setSourceChannelId(bundleTask.getSrcVideoChannelId());
//		videoForward.setUserId(String.valueOf(user.getId()));
//		videoForward.setTerminalId(terminalEntity.getId());
//		videoForward.setBusinessType(ForwardBusinessType.QT_TOTAL_FORWARD);
//		if("jv230".equals(bundle.getDeviceModel())){
//			forwards.add(videoForward);
//			jv230ForwardDao.save(forwards);
//		}else{
//			//处理设备合屏
//			List<QtTerminalCombineVideoPO> combineVideos = qtTerminalCombineVideoDao.findByUserIdAndTerminalId(user.getId().toString(), terminalEntity.getId());
//			QtTerminalCombineVideoPO combineVideo = combineVideos.get(0);
//			List<QtTerminalCombineVideoSrcPO> combineVideoSrcs = qtTerminalCombineVideoSrcDao.findByQtTerminalCombineVideoId(combineVideo.getId());
//			List<QtTerminalCombineVideoSrcPO> willDeleteCombineVideoSrcs = new ArrayList<QtTerminalCombineVideoSrcPO>();
//			List<QtTerminalCombineVideoSrcPO> willAddCombineVideoSrcs = new ArrayList<QtTerminalCombineVideoSrcPO>();
//			List<QtTerminalCombineVideoSrcPO> leftCombineVideoSrcs = new ArrayList<QtTerminalCombineVideoSrcPO>();
//			if(combineVideoSrcs!=null && combineVideoSrcs.size()>0){
//				for(QtTerminalCombineVideoSrcPO combineVideoSrc:combineVideoSrcs){
//					if(combineVideoSrc.getSerialNum() == serialNum){
//						willDeleteCombineVideoSrcs.add(combineVideoSrc);
//					}else{
//						leftCombineVideoSrcs.add(combineVideoSrc);
//					}
//				}
//			}
//			QtTerminalCombineVideoSrcPO combineVideoSrc = new QtTerminalCombineVideoSrcPO();
//			combineVideoSrc.setSourceLayerId(bundleTask.getSrcVideoLayerId());
//			combineVideoSrc.setSourceBundleId(bundleTask.getSrcVideoBundleId());
//			combineVideoSrc.setSourceChannelId(bundleTask.getSrcVideoChannelId());
//			combineVideoSrc.setSerialNum(serialNum);
//			combineVideoSrc.setX(Integer.parseInt(targetPosition.getX()));
//			combineVideoSrc.setY(Integer.parseInt(targetPosition.getY()));
//			combineVideoSrc.setW(Integer.parseInt(targetPosition.getWidth()));
//			combineVideoSrc.setH(Integer.parseInt(targetPosition.getHeight()));
//			combineVideoSrc.setBusinessName(bundleTask.getBusinessName());
//			combineVideoSrc.setQtTerminalCombineVideoId(combineVideo.getId());
//			willAddCombineVideoSrcs.add(combineVideoSrc);
//			if(willDeleteCombineVideoSrcs.size() > 0){
//				qtTerminalCombineVideoSrcDao.deleteInBatch(willDeleteCombineVideoSrcs);
//			}
//			if(willAddCombineVideoSrcs.size() > 0){
//				qtTerminalCombineVideoSrcDao.save(willAddCombineVideoSrcs);
//				leftCombineVideoSrcs.addAll(willAddCombineVideoSrcs);
//			}
//			CombineVideoBO combineVideoProtocol = new CombineVideoBO().setUuid(combineVideo.getUuid())
//																	  .setCodec_param(codec)
//																	  .setPosition(new ArrayList<PositionSrcBO>());
//			for(QtTerminalCombineVideoSrcPO terminalCombineVideoSrc:leftCombineVideoSrcs){
//				PositionSrcBO position = new PositionSrcBO().setX(terminalCombineVideoSrc.getX())
//															.setY(terminalCombineVideoSrc.getY())
//															.setW(terminalCombineVideoSrc.getW())
//															.setH(terminalCombineVideoSrc.getH())
//															.setSrc(new ArrayList<SourceBO>());
//				SourceBO src = new SourceBO().setLayerId(terminalCombineVideoSrc.getSourceLayerId())
//											 .setBundleId(terminalCombineVideoSrc.getSourceBundleId())
//											 .setChannelId(terminalCombineVideoSrc.getSourceChannelId());
//				position.getSrc().add(src);
//				combineVideoProtocol.getPosition().add(position);
//			}		
//			protocol.getCombineVideoUpdate().add(combineVideoProtocol);
//		}
//		
//		//处理音频（混音）
//		List<Jv230ForwardPO> audioForwards = jv230ForwardDao.findByBundleIdInAndSourceTypeInAndUserIdAndTerminalIdAndBusinessType(new ArrayListWrapper<String>().add(bundleId).getList(), new ArrayListWrapper<ForwardSourceType>().add(ForwardSourceType.COMBINE_AUDIO).add(ForwardSourceType.CHANNEL_AUDIO).getList(), String.valueOf(user.getId()), terminalEntity.getId(), ForwardBusinessType.QT_TOTAL_FORWARD);
//		Set<String> combineAudioUuids = new HashSet<String>();
//		if(audioForwards!=null && audioForwards.size()>0){
//			for(Jv230ForwardPO audioForward:audioForwards){
//				combineAudioUuids.add(audioForward.getSourceBundleId());
//			}
//		}
//		List<QtTerminalForwardPO> exceptJv230AudioForwards = qtTerminalForwardDao.findByUserIdAndTerminalIdAndBusinessTypeAndSourceType(user.getId().toString(), terminalEntity.getId(), ForwardBusinessType.QT_TOTAL_FORWARD, ForwardSourceType.COMBINE_AUDIO);
//		if(exceptJv230AudioForwards!=null && exceptJv230AudioForwards.size()>0){
//			for(QtTerminalForwardPO exceptJv230AudioForward:exceptJv230AudioForwards){
//				combineAudioUuids.add(exceptJv230AudioForward.getSourceId());
//			}
//		}
//		List<Jv230CombineAudioPO> combineAudios = jv230CombineAudioDao.findByUuidIn(combineAudioUuids);
//		List<Long> combineAudioIds = new ArrayList<Long>();
//		for(Jv230CombineAudioPO combineAudio:combineAudios){
//			combineAudioIds.add(combineAudio.getId());
//		}
//		List<Jv230CombineAudioSrcPO> combineAudioSrcs = jv230CombineAudioSrcDao.findByJv230CombineAudioIdIn(combineAudioIds);
//		List<Jv230CombineAudioSrcPO> willDeleteCombineAudioSrcs = new ArrayList<Jv230CombineAudioSrcPO>();
//		List<Jv230CombineAudioSrcPO> willAddCombineAudiosSrcs = new ArrayList<Jv230CombineAudioSrcPO>();
//		List<Jv230CombineAudioSrcPO> leftCombineAudioSrcs = new ArrayList<Jv230CombineAudioSrcPO>();
//		if(combineAudioSrcs!=null && combineAudioSrcs.size()>0){
//			for(Jv230CombineAudioSrcPO combineAudioSrc:combineAudioSrcs){
//				if(combineAudioSrc.getSerialNum() == serialNum){
//					willDeleteCombineAudioSrcs.add(combineAudioSrc);
//				}else{
//					leftCombineAudioSrcs.add(combineAudioSrc);
//				}
//			}
//		}
//		for(Jv230CombineAudioPO combineAudio:combineAudios){
//			Jv230CombineAudioSrcPO combineAudioSrc = new Jv230CombineAudioSrcPO();
//			combineAudioSrc.setSerialNum(serialNum);
//			combineAudioSrc.setJv230CombineAudioId(combineAudio.getId());
//			combineAudioSrc.setSourceLayerId(bundleTask.getSrcAudioLayerId());
//			combineAudioSrc.setSourceBundleId(bundleTask.getSrcAudioBundleId());
//			combineAudioSrc.setSourceChannelId(bundleTask.getSrcAudioChannelId());
//			combineAudioSrc.setUpdateTime(new Date());
//			willAddCombineAudiosSrcs.add(combineAudioSrc);
//		}
//		if(willDeleteCombineAudioSrcs.size() > 0){
//			jv230CombineAudioSrcDao.deleteInBatch(willDeleteCombineAudioSrcs);
//		}
//		if(willAddCombineAudiosSrcs.size() > 0){
//			jv230CombineAudioSrcDao.save(willAddCombineAudiosSrcs);
//			leftCombineAudioSrcs.addAll(willAddCombineAudiosSrcs);
//		}
//		
//		//音频协议
//		for(Jv230CombineAudioPO combineAudio:combineAudios){
//			CombineAudioBO combineAudioProtocol = new CombineAudioBO().setUuid(combineAudio.getUuid())
//																	  .setCodec_param(codec)
//																	  .setSrc(new ArrayList<SourceBO>());
//			for(Jv230CombineAudioSrcPO combineAudioSrc:combineAudioSrcs){
//				if(combineAudioSrc.getJv230CombineAudioId().equals(combineAudio.getId())){
//					SourceBO combineAudioSrcProtocol = new SourceBO().setLayerId(combineAudioSrc.getSourceLayerId())
//																	 .setBundleId(combineAudioSrc.getSourceBundleId())
//																	 .setChannelId(combineAudioSrc.getSourceChannelId());
//					combineAudioProtocol.getSrc().add(combineAudioSrcProtocol);
//				}
//			}
//			protocol.getCombineAudioUpdate().add(combineAudioProtocol);
//		}
//		for(Jv230ForwardPO forward:forwards){
//			if(ForwardSourceType.CHANNEL_VIDEO.equals(forward.getSourceType())){
//				//处理视频转发
//				Jv230ForwardBO videoForwardProtocol = new Jv230ForwardBO().setLayerId(forward.getLayerId())
//																		  .setBundleId(forward.getBundleId())
//																		  .setChannelId(forward.getChannelId())
//																		  .setChannel_param(new Jv230ChannelParamBO());
//				videoForwardProtocol.getChannel_param().setBase_type("VideoMatrixVideoOut")
//													   .setBase_param(new Jv230BaseParamBO());
//				videoForwardProtocol.getChannel_param().getBase_param().setCodec_type("h264")
//																	   .setIs_polling("false")
//																	   .setSrc_mode(0)
//																	   .setDisplay_rect(new PositionBO())
//																	   .setSources(new ArrayList<Jv230SourceBO>())
//																	   .setCodec("pcmu");
//				videoForwardProtocol.getChannel_param().getBase_param().getDisplay_rect().setX(forward.getX())
//																						 .setY(forward.getY())
//																						 .setWidth(forward.getW())
//																						 .setHeight(forward.getH())
//																						 .setZ_index(1);
//				videoForwardProtocol.getChannel_param().getBase_param().getSources().add(new Jv230SourceBO()
//																					.setLayer_id(forward.getSourceLayerId())
//																				   .setBundle_id(forward.getSourceBundleId())
//																				   .setChannel_id(forward.getSourceChannelId()));
//				protocol.getJv230ForwardSet().add(videoForwardProtocol);
//			}else if(ForwardSourceType.CHANNEL_AUDIO.equals(forward.getSourceType())){
//				//处理音频转发
//			}else if(ForwardSourceType.COMBINE_AUDIO.equals(forward.getSourceType())){
//				//处理混音
//			}
//		}
//		executeBusiness.execute(protocol, new StringBufferWrapper().append("qt终端修改分屏内容，serianlNum：").append(serialNum).toString());
//		return new Jv230ForwardVO().set(videoForward);
//	}
//	
//	/**
//	 * qt结束某个jv220上某个分屏上的内容（有事务）<br/>
//	 * <b>作者:</b>lvdeyang<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年7月15日 下午2:45:14
//	 * @param String bundleId 设备id
//	 * @param int serialNum 分屏序号
//	 */
//	@Transactional(rollbackFor = Exception.class)
//	public void deleteForwardByBundleIdAndSerialNum(String bundleId, int serialNum) throws Exception{
//		deleteForwardByBundleIdAndSerialNumWithoutTransactional(bundleId, serialNum);
//	}
//	
//	/**
//	 * qt结束某个jv230上某个分屏上的内容（无事务）非jv230设备不做处理<br/>
//	 * <b>作者:</b>lvdeyang<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年7月15日 下午2:45:14
//	 * @param String bundleId 设备id
//	 * @param int serialNum 分屏序号
//	 */
//	private void deleteForwardByBundleIdAndSerialNumWithoutTransactional(String bundleId, int serialNum) throws Exception{
//		UserVO user = userQuery.current();
//		TerminalType terminalType = TerminalType.fromTokenType(user.getTerminalType());
//		TerminalPO terminalEntity = terminalDao.findByType(terminalType);
//		if(terminalEntity == null){
//			throw new TerminalNotFoundException(terminalType);
//		}
//		Jv230ForwardPO videoForward = jv230ForwardDao.findByBundleIdAndSerialNumAndUserIdAndTerminalIdAndBusinessType(bundleId, serialNum, String.valueOf(user.getId()), terminalEntity.getId(), ForwardBusinessType.QT_TOTAL_FORWARD);
//		if(videoForward == null) return;
//		LogicBO protocol = new LogicBO().setUserId(user.getId().toString())
//										.setJv230ForwardDel(new ArrayList<SourceBO>());
//		SourceBO deleteJv230ForwardProtocol = new SourceBO();
//		deleteJv230ForwardProtocol.setLayerId(videoForward.getLayerId());
//		deleteJv230ForwardProtocol.setBundleId(videoForward.getBundleId());
//		deleteJv230ForwardProtocol.setChannelId(videoForward.getChannelId());
//		protocol.getJv230ForwardDel().add(deleteJv230ForwardProtocol);
//		List<Jv230ForwardPO> audioForwards = jv230ForwardDao.findByBundleIdAndSourceTypeInAndUserIdAndTerminalIdAndBusinessType(bundleId, new ArrayListWrapper<ForwardSourceType>().add(ForwardSourceType.COMBINE_AUDIO).add(ForwardSourceType.CHANNEL_AUDIO).getList(), String.valueOf(user.getId()), terminalEntity.getId(), ForwardBusinessType.QT_TOTAL_FORWARD);
//		if(audioForwards!=null && audioForwards.size()>0){
//			//如果是混音则移出混音成员
//			//如果不是混音则删除音频转发
//			Set<String> combineAudioUuids = new HashSet<String>();
//			for(Jv230ForwardPO audioForward:audioForwards){
//				combineAudioUuids.add(audioForward.getSourceBundleId());
//			}
//			List<Jv230CombineAudioPO> combineAudios = jv230CombineAudioDao.findByUuidIn(combineAudioUuids);
//			List<Long> combineAudioIds = new ArrayList<Long>();
//			for(Jv230CombineAudioPO combineAudio:combineAudios){
//				combineAudioIds.add(combineAudio.getId());
//			}
//			List<Jv230CombineAudioSrcPO> combineAudioSrcs = jv230CombineAudioSrcDao.findByJv230CombineAudioIdIn(combineAudioIds);
//			List<Jv230CombineAudioSrcPO> willDeleteCombineAudioSrcs = new ArrayList<Jv230CombineAudioSrcPO>();
//			List<Jv230CombineAudioSrcPO> leftCombineAudioSrcs = new ArrayList<Jv230CombineAudioSrcPO>();
//			if(combineAudioSrcs!=null && combineAudioSrcs.size()>0){
//				for(Jv230CombineAudioSrcPO combineAudioSrc:combineAudioSrcs){
//					if(combineAudioSrc.getSerialNum() == serialNum){
//						willDeleteCombineAudioSrcs.add(combineAudioSrc);
//					}else{
//						leftCombineAudioSrcs.add(combineAudioSrc);
//					}
//				}
//			}
//			if(willDeleteCombineAudioSrcs.size() > 0){
//				jv230CombineAudioSrcDao.deleteInBatch(willDeleteCombineAudioSrcs);
//				CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
//				for(Jv230CombineAudioPO combineAudio:combineAudios){
//					CombineAudioBO combineAudioProtocol = new CombineAudioBO().setUuid(combineAudio.getUuid())
//																			  .setCodec_param(codec)
//																			  .setSrc(new ArrayList<SourceBO>());
//					for(Jv230CombineAudioSrcPO combineAudioSrc:combineAudioSrcs){
//						if(combineAudioSrc.getJv230CombineAudioId().equals(combineAudio.getId())){
//							SourceBO combineAudioSrcProtocol = new SourceBO().setLayerId(combineAudioSrc.getSourceLayerId())
//																			 .setBundleId(combineAudioSrc.getSourceBundleId())
//																			 .setChannelId(combineAudioSrc.getSourceChannelId());
//							combineAudioProtocol.getSrc().add(combineAudioSrcProtocol);
//						}
//					}
//					protocol.getCombineAudioUpdate().add(combineAudioProtocol);
//					
//				}
//			}
//		}
//		executeBusiness.execute(protocol, new StringBufferWrapper().append("qt终端某个jv230分屏停止转发，serialNum：").append(serialNum).append("，bundleId：").append(bundleId).toString());
//		jv230ForwardDao.delete(videoForward);
//	}
//	
//	/**
//	 * qt结束某个分屏上的内容（有事务）<br/>
//	 * <b>作者:</b>lvdeyang<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年7月15日 上午9:31:57
//	 * @param int serialNum 分屏序号
//	 */
//	@Transactional(rollbackFor = Exception.class)
//	public void deleteForwardBySerialNum(int serialNum) throws Exception{
//		deleteForwardBySerialNumWithoutTransactional(serialNum);
//	}
//	
//	/**
//	 * qt结束某个分屏上的内容（无事务）<br/>
//	 * <b>作者:</b>lvdeyang<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年7月15日 上午9:31:57
//	 * @param int serialNum 分屏序号
//	 */
//	private void deleteForwardBySerialNumWithoutTransactional(int serialNum) throws Exception{
//		UserVO user = userQuery.current();
//		TerminalType terminalType = TerminalType.fromTokenType(user.getTerminalType());
//		TerminalPO terminalEntity = terminalDao.findByType(terminalType);
//		if(terminalEntity == null){
//			throw new TerminalNotFoundException(terminalType);
//		}
//		List<Jv230ForwardPO> videoForwards = jv230ForwardDao.findBySerialNumAndUserIdAndTerminalIdAndBusinessType(serialNum, String.valueOf(user.getId()), terminalEntity.getId(), ForwardBusinessType.QT_TOTAL_FORWARD);
//		if(videoForwards==null || videoForwards.size()<=0) return;
//		LogicBO protocol = new LogicBO().setUserId(user.getId().toString())
//										.setCombineAudioUpdate(new ArrayList<CombineAudioBO>())
//										.setJv230ForwardDel(new ArrayList<SourceBO>());
//		for(Jv230ForwardPO forward:videoForwards){
//			SourceBO deleteJv230ForwardProtocol = new SourceBO();
//			deleteJv230ForwardProtocol.setLayerId(forward.getLayerId());
//			deleteJv230ForwardProtocol.setBundleId(forward.getBundleId());
//			deleteJv230ForwardProtocol.setChannelId(forward.getChannelId());
//			protocol.getJv230ForwardDel().add(deleteJv230ForwardProtocol);
//		}
//		List<Jv230ForwardPO> audioForwards = jv230ForwardDao.findBySourceTypeInAndUserIdAndTerminalIdAndBusinessType(new ArrayListWrapper<ForwardSourceType>().add(ForwardSourceType.COMBINE_AUDIO).add(ForwardSourceType.CHANNEL_AUDIO).getList(), String.valueOf(user.getId()), terminalEntity.getId(), ForwardBusinessType.QT_TOTAL_FORWARD);
//		if(audioForwards!=null && audioForwards.size()>0){
//			//如果是混音则移出混音成员
//			//如果不是混音则删除音频转发
//			Set<String> combineAudioUuids = new HashSet<String>();
//			for(Jv230ForwardPO audioForward:audioForwards){
//				combineAudioUuids.add(audioForward.getSourceBundleId());
//			}
//			List<Jv230CombineAudioPO> combineAudios = jv230CombineAudioDao.findByUuidIn(combineAudioUuids);
//			List<Long> combineAudioIds = new ArrayList<Long>();
//			for(Jv230CombineAudioPO combineAudio:combineAudios){
//				combineAudioIds.add(combineAudio.getId());
//			}
//			List<Jv230CombineAudioSrcPO> combineAudioSrcs = jv230CombineAudioSrcDao.findByJv230CombineAudioIdIn(combineAudioIds);
//			List<Jv230CombineAudioSrcPO> willDeleteCombineAudioSrcs = new ArrayList<Jv230CombineAudioSrcPO>();
//			List<Jv230CombineAudioSrcPO> leftCombineAudioSrcs = new ArrayList<Jv230CombineAudioSrcPO>();
//			if(combineAudioSrcs!=null && combineAudioSrcs.size()>0){
//				for(Jv230CombineAudioSrcPO combineAudioSrc:combineAudioSrcs){
//					if(combineAudioSrc.getSerialNum() == serialNum){
//						willDeleteCombineAudioSrcs.add(combineAudioSrc);
//					}else{
//						leftCombineAudioSrcs.add(combineAudioSrc);
//					}
//				}
//			}
//			if(willDeleteCombineAudioSrcs.size() > 0){
//				jv230CombineAudioSrcDao.deleteInBatch(willDeleteCombineAudioSrcs);
//				CodecParamBO codec = commandCommonServiceImpl.queryDefaultAvCodecParamBO();
//				for(Jv230CombineAudioPO combineAudio:combineAudios){
//					CombineAudioBO combineAudioProtocol = new CombineAudioBO().setUuid(combineAudio.getUuid())
//																			  .setCodec_param(codec)
//																			  .setSrc(new ArrayList<SourceBO>());
//					for(Jv230CombineAudioSrcPO combineAudioSrc:combineAudioSrcs){
//						if(combineAudioSrc.getJv230CombineAudioId().equals(combineAudio.getId())){
//							SourceBO combineAudioSrcProtocol = new SourceBO().setLayerId(combineAudioSrc.getSourceLayerId())
//																			 .setBundleId(combineAudioSrc.getSourceBundleId())
//																			 .setChannelId(combineAudioSrc.getSourceChannelId());
//							combineAudioProtocol.getSrc().add(combineAudioSrcProtocol);
//						}
//					}
//					protocol.getCombineAudioUpdate().add(combineAudioProtocol);
//					
//				}
//			}
//		}
//		executeBusiness.execute(protocol, new StringBufferWrapper().append("qt终端全部jv230分屏停止转发，serialNum：").append(serialNum).toString());
//		jv230ForwardDao.deleteInBatch(videoForwards);
//	}
//	
//	/**
//	 * qt结束某个jv230全部转发（有事务）<br/>
//	 * <b>作者:</b>lvdeyang<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年7月15日 下午2:44:49
//	 */
//	@Transactional(rollbackFor = Exception.class)
//	public void deleteForwardByBundleId(String bundleId) throws Exception{
//		deleteForwardByBundleIdWithoutTransactional(bundleId);
//	}
//	
//	/**
//	 * qt结束某个jv230全部转发（无事务）<br/>
//	 * <b>作者:</b>lvdeyang<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年7月15日 下午2:44:49
//	 */
//	private void deleteForwardByBundleIdWithoutTransactional(String bundleId) throws Exception{
//		UserVO user = userQuery.current();
//		TerminalType terminalType = TerminalType.fromTokenType(user.getTerminalType());
//		TerminalPO terminalEntity = terminalDao.findByType(terminalType);
//		if(terminalEntity == null){
//			throw new TerminalNotFoundException(terminalType);
//		}
//		
//		BundlePO bundle = bundleDao.findByBundleId(bundleId);
//		if("jv230".equals(bundle.getDeviceModel())){
//			List<Jv230ForwardPO> forwards = jv230ForwardDao.findByBundleIdAndUserIdAndTerminalIdAndBusinessType(bundleId, String.valueOf(user.getId()), terminalEntity.getId(), ForwardBusinessType.QT_TOTAL_FORWARD);
//			if(forwards==null || forwards.size()<=0) return;
//			List<Jv230ForwardPO> combineAudioForwards = new ArrayList<Jv230ForwardPO>();
//			List<Jv230ForwardPO> combineVideoForwards = new ArrayList<Jv230ForwardPO>();
//			LogicBO protocol = new LogicBO().setUserId(user.getId().toString())
//											.setCombineAudioDel(new ArrayList<CombineAudioBO>())
//											.setJv230ForwardDel(new ArrayList<SourceBO>());
//			for(Jv230ForwardPO forward:forwards){
//				if(ForwardSourceType.COMBINE_VIDEO.equals(forward.getSourceType())){
//					combineVideoForwards.add(forward);
//				}else if(ForwardSourceType.COMBINE_AUDIO.equals(forward.getSourceType())){
//					combineAudioForwards.add(forward);
//				}
//				SourceBO deleteJv230ForwardProtocol = new SourceBO();
//				deleteJv230ForwardProtocol.setLayerId(forward.getLayerId());
//				deleteJv230ForwardProtocol.setBundleId(forward.getBundleId());
//				deleteJv230ForwardProtocol.setChannelId(forward.getChannelId());
//				protocol.getJv230ForwardDel().add(deleteJv230ForwardProtocol);
//			}
//			if(combineAudioForwards.size() > 0){
//				//删除混音
//				for(Jv230ForwardPO audioForward:combineAudioForwards){
//					CombineAudioBO combineAudioProtocol = new CombineAudioBO().setUuid(audioForward.getSourceBundleId());
//					protocol.getCombineAudioDel().add(combineAudioProtocol);
//				}
//			}
//			if(combineVideoForwards.size() > 0){
//				//删除合屏
//			}
//			jv230ForwardDao.deleteInBatch(forwards);
//			executeBusiness.execute(protocol, new StringBufferWrapper().append("qt终端jv230停止转发，bundleId：").append(bundleId).toString());
//		}else{
//			LogicBO protocol = new LogicBO().setUserId(user.getId().toString())
//											.setCombineAudioDel(new ArrayList<CombineAudioBO>())
//											.setCombineVideoDel(new ArrayList<CombineVideoBO>())
//											.setDisconnectBundle(new ArrayList<DisconnectBundleBO>());
//			List<QtTerminalForwardPO> forwards = qtTerminalForwardDao.findByUserIdAndTerminalIdAndBundleId(user.getId().toString(), terminalEntity.getId(), bundleId);
//			if(forwards!=null && forwards.size()>0){
//				for(QtTerminalForwardPO forward:forwards){
//					if(ForwardSourceType.COMBINE_AUDIO.equals(forward.getSourceType())){
//						Jv230CombineAudioPO combineAudio = jv230CombineAudioDao.findByUuid(forward.getSourceId());
//						List<Jv230CombineAudioSrcPO> combineAudioSrcs = jv230CombineAudioSrcDao.findByJv230CombineAudioIdIn(new ArrayListWrapper<Long>().add(combineAudio.getId()).getList());
//						protocol.getCombineAudioDel().add(new CombineAudioBO().setUuid(combineAudio.getUuid()));
//						jv230CombineAudioDao.delete(combineAudio);
//						jv230CombineAudioSrcDao.deleteInBatch(combineAudioSrcs);
//					}else if(ForwardSourceType.COMBINE_VIDEO.equals(forward.getSourceType())){
//						List<QtTerminalForwardPO> otherForwards = qtTerminalForwardDao.findByUserIdAndTerminalIdAndBundleIdNot(user.getId().toString(), terminalEntity.getId(), bundleId);
//						if(otherForwards!=null && otherForwards.size()>0) continue;
//						QtTerminalCombineVideoPO combineVideo = qtTerminalCombineVideoDao.findByUuid(forward.getSourceId());
//						List<QtTerminalCombineVideoSrcPO> combineVideoSrcs = qtTerminalCombineVideoSrcDao.findByQtTerminalCombineVideoId(combineVideo.getId());
//						protocol.getCombineVideoDel().add(new CombineVideoBO().setUuid(combineVideo.getUuid()));
//						qtTerminalCombineVideoDao.delete(combineVideo);
//						qtTerminalCombineVideoSrcDao.deleteInBatch(combineVideoSrcs);
//					}
//				}
//				qtTerminalForwardDao.deleteInBatch(forwards);
//			}
//			protocol.getDisconnectBundle().add(new DisconnectBundleBO().setLayerId(bundle.getAccessNodeUid())
//																	   .setBundleId(bundle.getBundleId()));
//			executeBusiness.execute(protocol, new StringBufferWrapper().append("qt终端停止合屏上屏，bundleId：").append(bundleId).toString());
//		}
//	}
//	
//	/**
//	 * qt结束全部上屏内容(有事务)<br/>
//	 * <b>作者:</b>lvdeyang<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年7月15日 上午9:32:24
//	 */
//	@Transactional(rollbackFor = Exception.class)
//	public void deleteTotalForward() throws Exception{
//		deleteTotalForwardWithoutTransactional();
//	}
//	
//
//	/**
//	 * qt结束全部上屏内容(无事务)<br/>
//	 * <b>作者:</b>lvdeyang<br/>
//	 * <b>版本：</b>1.0<br/>
//	 * <b>日期：</b>2020年7月15日 上午9:32:24
//	 */
//	private void deleteTotalForwardWithoutTransactional() throws Exception{
//		UserVO user = userQuery.current();
//		TerminalType terminalType = TerminalType.fromTokenType(user.getTerminalType());
//		TerminalPO terminalEntity = terminalDao.findByType(terminalType);
//		if(terminalEntity == null){
//			throw new TerminalNotFoundException(terminalType);
//		}
//		LogicBO protocol = new LogicBO().setUserId(user.getId().toString())
//										.setCombineAudioDel(new ArrayList<CombineAudioBO>())
//						                .setCombineVideoDel(new ArrayList<CombineVideoBO>())
//										.setJv230ForwardDel(new ArrayList<SourceBO>())
//										.setDisconnectBundle(new ArrayList<DisconnectBundleBO>());
//		Set<String> combineAudioUuids = new HashSet<String>();
//		Set<String> combineVideoUuids = new HashSet<String>();
//		List<Jv230ForwardPO> forwards = jv230ForwardDao.findByUserIdAndTerminalIdAndBusinessType(String.valueOf(user.getId()), terminalEntity.getId(), ForwardBusinessType.QT_TOTAL_FORWARD);
//		if(forwards!=null && forwards.size()>0){
//			List<Jv230ForwardPO> combineAudioForwards = new ArrayList<Jv230ForwardPO>();
//			List<Jv230ForwardPO> combineVideoForwards = new ArrayList<Jv230ForwardPO>();
//			
//			for(Jv230ForwardPO forward:forwards){
//				if(ForwardSourceType.COMBINE_VIDEO.equals(forward.getSourceType())){
//					combineVideoForwards.add(forward);
//				}else if(ForwardSourceType.COMBINE_AUDIO.equals(forward.getSourceType())){
//					combineAudioForwards.add(forward);
//				}
//				SourceBO deleteJv230ForwardProtocol = new SourceBO();
//				deleteJv230ForwardProtocol.setLayerId(forward.getLayerId());
//				deleteJv230ForwardProtocol.setBundleId(forward.getBundleId());
//				deleteJv230ForwardProtocol.setChannelId(forward.getChannelId());
//				protocol.getJv230ForwardDel().add(deleteJv230ForwardProtocol);
//			}
//			if(combineAudioForwards.size() > 0){
//				//删除混音
//				for(Jv230ForwardPO audioForward:combineAudioForwards){
//					combineAudioUuids.add(audioForward.getSourceBundleId());
//					CombineAudioBO combineAudioProtocol = new CombineAudioBO().setUuid(audioForward.getSourceBundleId());
//					protocol.getCombineAudioDel().add(combineAudioProtocol);
//				}
//			}
//			jv230ForwardDao.deleteInBatch(forwards);
//		}
//		
//		List<QtTerminalForwardPO> exceptJv230Forwards = qtTerminalForwardDao.findByUserIdAndTerminalIdAndBusinessType(user.getId().toString(), terminalEntity.getId(), ForwardBusinessType.QT_TOTAL_FORWARD);
//		if(exceptJv230Forwards!=null && exceptJv230Forwards.size()>0){
//			Set<String> bundleIds = new HashSet<String>();
//			for(QtTerminalForwardPO exceptJv230Forward:exceptJv230Forwards){
//				bundleIds.add(exceptJv230Forward.getBundleId());
//				if(ForwardSourceType.COMBINE_AUDIO.equals(exceptJv230Forward.getSourceType())){
//					combineAudioUuids.add(exceptJv230Forward.getSourceId());
//					CombineAudioBO combineAudioProtocol = new CombineAudioBO().setUuid(exceptJv230Forward.getSourceId());
//					protocol.getCombineAudioDel().add(combineAudioProtocol);
//				}else if(ForwardSourceType.COMBINE_VIDEO.equals(exceptJv230Forward.getSourceType())){
//					combineVideoUuids.add(exceptJv230Forward.getSourceId());
//					CombineVideoBO combineVideoProtocol = new CombineVideoBO().setUuid(exceptJv230Forward.getSourceId());
//					protocol.getCombineVideoDel().add(combineVideoProtocol);	
//				}
//			}
//			if(bundleIds.size() > 0){
//				List<BundlePO> bundles = bundleDao.findByBundleIdIn(bundleIds);
//				if(bundles!=null && bundles.size()>0){
//					for(BundlePO bundle:bundles){
//						protocol.getDisconnectBundle().add(new DisconnectBundleBO().setLayerId(bundle.getAccessNodeUid())
//								                                                   .setBundleId(bundle.getBundleId()));
//					}
//				}
//			}
//			if(combineVideoUuids.size() > 0){
//				List<QtTerminalCombineVideoPO> combineVideos = qtTerminalCombineVideoDao.findByUuidIn(combineVideoUuids);
//				if(combineVideos!=null && combineVideos.size()>0){
//					List<Long> combineVideoIds = new ArrayList<Long>();
//					for(QtTerminalCombineVideoPO combineVideo:combineVideos){
//						combineVideoIds.add(combineVideo.getId());
//					}
//					List<QtTerminalCombineVideoSrcPO> combineVideoSrcs = qtTerminalCombineVideoSrcDao.findByQtTerminalCombineVideoIdIn(combineVideoIds);
//					qtTerminalCombineVideoDao.deleteInBatch(combineVideos);
//					if(combineVideoSrcs!=null && combineVideoSrcs.size()>0){
//						qtTerminalCombineVideoSrcDao.deleteInBatch(combineVideoSrcs);
//					}
//				}
//			}
//			if(combineAudioUuids.size() > 0){
//				List<Jv230CombineAudioPO> combineAudios = jv230CombineAudioDao.findByUuidIn(combineAudioUuids);
//				if(combineAudios!=null && combineAudios.size()>0){
//					List<Long> combineAudioIds = new ArrayList<Long>();
//					for(Jv230CombineAudioPO combineAudio:combineAudios){
//						combineAudioIds.add(combineAudio.getId());
//					}
//					List<Jv230CombineAudioSrcPO> combineAudioSrcs = jv230CombineAudioSrcDao.findByJv230CombineAudioIdIn(combineAudioIds);
//					jv230CombineAudioDao.deleteInBatch(combineAudios);
//					if(combineAudioSrcs!=null && combineAudioSrcs.size()>0){
//						jv230CombineAudioSrcDao.deleteInBatch(combineAudioSrcs);
//					}
//				}
//			}
//			qtTerminalForwardDao.deleteInBatch(exceptJv230Forwards);
//		}
//		executeBusiness.execute(protocol, "qt终端jv230全部停止转发");
//	}
//	
//}
