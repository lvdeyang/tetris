package com.sumavision.tetris.bvc.business.group.forward;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.sumavision.bvc.command.group.user.layout.scheme.PlayerSplitLayout;
import com.sumavision.bvc.device.group.bo.LogicBO;
import com.sumavision.bvc.device.group.bo.SourceBO;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.jv230.bo.Jv230BaseParamBO;
import com.sumavision.bvc.device.jv230.bo.Jv230ChannelParamBO;
import com.sumavision.bvc.device.jv230.bo.Jv230ForwardBO;
import com.sumavision.bvc.device.jv230.bo.Jv230SourceBO;
import com.sumavision.bvc.device.jv230.bo.PositionBO;
import com.sumavision.tetris.bvc.model.terminal.TerminalDAO;
import com.sumavision.tetris.bvc.model.terminal.TerminalPO;
import com.sumavision.tetris.bvc.model.terminal.TerminalType;
import com.sumavision.tetris.bvc.model.terminal.exception.TerminalNotFoundException;
import com.sumavision.tetris.bvc.model.terminal.layout.LayoutDAO;
import com.sumavision.tetris.bvc.model.terminal.layout.LayoutPO;
import com.sumavision.tetris.bvc.model.terminal.layout.LayoutPositionDAO;
import com.sumavision.tetris.bvc.model.terminal.layout.LayoutPositionPO;
import com.sumavision.tetris.bvc.model.terminal.layout.exception.LayoutNotFoundException;
import com.sumavision.tetris.bvc.page.PageTaskPO;
import com.sumavision.tetris.bvc.page.PageTaskQueryService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Service
public class Jv230ForwardService {

	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private TerminalDAO terminalDao;
	
	@Autowired
	private LayoutDAO layoutDao;
	
	@Autowired
	private LayoutPositionDAO layoutPositionDao;
	
	@Autowired
	private PageTaskQueryService pageTaskQueryService;
	
	@Autowired
	private Jv230ForwardDAO jv230ForwardDao;
	
	@Autowired
	private Jv230CombineAudioDAO jv230CombineAudioDao;
	
	@Autowired
	private Jv230CombineAudioSrcDAO jv230CombineAudioSrcDao;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	/**
	 * qt终端全部上屏jv230（这里默认是16视频解6音频解）（有事务）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 上午9:30:12
	 * @param String bundleId jv230 设备id
	 */
	@Transactional(rollbackFor = Exception.class)
	public void totalForward(String bundleId) throws Exception{
		totalForwardWithoutTransactional(bundleId);
	}
	
	/**
	 * qt终端全部上屏jv230（这里默认是16视频解6音频解）（无事务）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 上午9:30:12
	 * @param String bundleId jv230 设备id
	 */
	private void totalForwardWithoutTransactional(String bundleId) throws Exception{
		UserVO user = userQuery.current();
		TerminalType terminalType = TerminalType.fromTokenType(user.getTerminalType());
		TerminalPO terminalEntity = terminalDao.findByType(terminalType);
		if(terminalEntity == null){
			throw new TerminalNotFoundException(terminalType);
		}
		BundlePO bundle = bundleDao.findByBundleId(bundleId);
		if(bundle == null){
			throw new BaseException(StatusCode.FORBIDDEN, new StringBufferWrapper().append("jv230不存在，bundleId：").append(bundleId).toString());
		}
		deleteForwardByBundleId(bundleId);
		List<PageTaskPO> tasks = pageTaskQueryService.queryCurrentPageTasks(String.valueOf(user.getId()), terminalEntity.getId());
		List<PageTaskPO> bundleTasks = new ArrayList<PageTaskPO>();
		if(tasks!=null && tasks.size()>0){
			for(PageTaskPO task:tasks){
				if(task.getSrcVideoBundleId() != null){
					bundleTasks.add(task);
				}
			}
		}
		if(bundleTasks.size() <= 0) return;
		int splitCount = pageTaskQueryService.queryCurrentPageSize(String.valueOf(user.getId()), terminalEntity.getId());
		String layoutName = PlayerSplitLayout.fromPlayerCount(splitCount).getName();
		LayoutPO layout = layoutDao.findByName(layoutName);
		if(layout == null){
			throw new LayoutNotFoundException(layoutName);
		}
		List<LayoutPositionPO> layoutPositions = layoutPositionDao.findByLayoutId(layout.getId());
		List<Jv230ForwardPO> forwards = new ArrayList<Jv230ForwardPO>();
		for(PageTaskPO bundleTask:bundleTasks){
			//从0开始
			Integer serialNum = bundleTask.getLocationIndex();
			String screenPrimaryKey = new StringBufferWrapper().append("screen_").append(serialNum + 1).toString();
			String jv230VideoChannelId =  new StringBufferWrapper().append("VenusVideoOut_").append(serialNum + 1).toString();
			Jv230ForwardPO videoForward = new Jv230ForwardPO();
			videoForward.setLayerId(bundle.getAccessNodeUid());
			videoForward.setBundleId(bundle.getBundleId());
			videoForward.setChannelId(jv230VideoChannelId);
			videoForward.setSerialNum(serialNum);
			LayoutPositionPO targetPosition = null;
			for(LayoutPositionPO position:layoutPositions){
				if(position.getScreenPrimaryKey().equals(screenPrimaryKey)){
					targetPosition = position;
					break;
				}
			}
			videoForward.setX(Integer.parseInt(targetPosition.getX()));
			videoForward.setY(Integer.parseInt(targetPosition.getY()));
			videoForward.setW(Integer.parseInt(targetPosition.getWidth()));
			videoForward.setH(Integer.parseInt(targetPosition.getHeight()));
			videoForward.setBusinessName(bundleTask.getBusinessName());
			videoForward.setSourceType(Jv230ForwardSourceType.CHANNEL_VIDEO);
			videoForward.setSourceLayerId(bundleTask.getSrcVideoLayerId());
			videoForward.setSourceBundleId(bundleTask.getSrcVideoBundleId());
			videoForward.setSourceChannelId(bundleTask.getSrcVideoChannelId());
			videoForward.setUserId(String.valueOf(user.getId()));
			videoForward.setTerminalId(terminalEntity.getId());
			videoForward.setBusinessType(Jv230ForwardBusinessType.QT_TOTAL_FORWARD);
			forwards.add(videoForward);
		}
		//处理音频（混音）
		Jv230CombineAudioPO combineAudio = new Jv230CombineAudioPO();
		combineAudio.setUpdateTime(new Date());
		jv230CombineAudioDao.save(combineAudio);
		List<Jv230CombineAudioSrcPO> combineAudioSrcs = new ArrayList<Jv230CombineAudioSrcPO>();
		for(PageTaskPO bundleTask:bundleTasks){
			Jv230CombineAudioSrcPO combineAudioSrc = new Jv230CombineAudioSrcPO();
			combineAudioSrc.setSourceLayerId(bundleTask.getSrcAudioLayerId());
			combineAudioSrc.setSourceBundleId(bundleTask.getSrcAudioBundleId());
			combineAudioSrc.setSourceChannelId(bundleTask.getSrcAudioChannelId());
			combineAudioSrc.setSerialNum(bundleTask.getLocationIndex());
			combineAudioSrc.setJv230CombineAudioId(combineAudio.getId());
			combineAudioSrc.setUpdateTime(new Date());
			combineAudioSrcs.add(combineAudioSrc);
		}
		jv230CombineAudioSrcDao.save(combineAudioSrcs);
		Jv230ForwardPO audioForward = new Jv230ForwardPO();
		audioForward.setLayerId(bundle.getAccessNodeUid());
		audioForward.setBundleId(bundle.getBundleId());
		audioForward.setChannelId("VenusAudioOut_1");
		audioForward.setSourceType(Jv230ForwardSourceType.CHANNEL_AUDIO);
		audioForward.setSourceLayerId(combineAudio.getUuid());
		audioForward.setUserId(String.valueOf(user.getId()));
		audioForward.setTerminalId(terminalEntity.getId());
		audioForward.setBusinessType(Jv230ForwardBusinessType.QT_TOTAL_FORWARD);
		audioForward.setUpdateTime(new Date());
		forwards.add(audioForward);
		jv230ForwardDao.save(forwards);
		
		//生成协议
		LogicBO protocal = new LogicBO().setJv230AudioSet(new ArrayList<Jv230ForwardBO>())
										.setJv230ForwardSet(new ArrayList<Jv230ForwardBO>());
		for(Jv230ForwardPO forward:forwards){
			if(Jv230ForwardSourceType.CHANNEL_VIDEO.equals(forward.getSourceType())){
				//处理视频转发
				Jv230ForwardBO videoForwardProtocal = new Jv230ForwardBO().setLayerId(forward.getLayerId())
																		  .setBundleId(forward.getBundleId())
																		  .setChannelId(forward.getChannelId())
																		  .setChannel_param(new Jv230ChannelParamBO());
				videoForwardProtocal.getChannel_param().setBase_type("VideoMatrixVideoOut")
													   .setBase_param(new Jv230BaseParamBO());
				videoForwardProtocal.getChannel_param().getBase_param().setCodec_type("h264")
																	   .setIs_polling("false")
																	   .setSrc_mode(0)
																	   .setDisplay_rect(new PositionBO())
																	   .setSource(new Jv230SourceBO());
				videoForwardProtocal.getChannel_param().getBase_param().getDisplay_rect().setX(forward.getX())
																						 .setY(forward.getY())
																						 .setWidth(forward.getW())
																						 .setHeight(forward.getH())
																						 .setZ_index(1);
				videoForwardProtocal.getChannel_param().getBase_param().getSource().setLayer_id(forward.getSourceLayerId())
																				   .setBundle_id(forward.getSourceBundleId())
																				   .setChannel_id(forward.getSourceChannelId());
				protocal.getJv230ForwardSet().add(videoForwardProtocal);
			}else if(Jv230ForwardSourceType.CHANNEL_AUDIO.equals(forward.getSourceType())){
				//处理音频转发
			}else if(Jv230ForwardSourceType.COMBINE_AUDIO.equals(forward.getSourceType())){
				//处理混音
				Jv230ForwardBO videoForwardProtocal = new Jv230ForwardBO().setLayerId(forward.getLayerId())
						  .setBundleId(forward.getBundleId())
						  .setChannelId(forward.getChannelId())
						  .setChannel_param(new Jv230ChannelParamBO());
videoForwardProtocal.getChannel_param().setBase_type("VideoMatrixVideoOut")
	   .setBase_param(new Jv230BaseParamBO());
videoForwardProtocal.getChannel_param().getBase_param().setCodec_type("h264")
					   .setIs_polling("false")
					   .setSrc_mode(0)
					   .setDisplay_rect(new PositionBO())
					   .setSource(new Jv230SourceBO())
					   .setCodec("pcmu");
			}
		}
		executeBusiness.execute(protocal, new StringBufferWrapper().append("qt终端为jv230全部上屏，bunldeId：").append(bundleId).toString());
	}
	
	/**
	 * qt终端切换分屏<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 上午9:31:02
	 */
	@Transactional(rollbackFor = Exception.class)
	public void changeSplit() throws Exception{
		UserVO user = userQuery.current();
		TerminalType terminalType = TerminalType.fromTokenType(user.getTerminalType());
		TerminalPO terminalEntity = terminalDao.findByType(terminalType);
		if(terminalEntity == null){
			throw new TerminalNotFoundException(terminalType);
		}
		List<String> bundleIds = jv230ForwardDao.findDistinctBundleIdByUserIdAndTerminalIdAndBusinessType(String.valueOf(user.getId()), terminalEntity.getId(), Jv230ForwardBusinessType.QT_TOTAL_FORWARD);
		if(bundleIds==null || bundleIds.size()<=0) return;
		deleteTotalForwardWithoutTransactional();
		for(String bundleId:bundleIds){
			totalForwardWithoutTransactional(bundleId);
		}
	}
	
	/**
	 * qt终端某个分屏内容变化<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 上午9:31:24
	 * @param int serialNum 分屏序号
	 */
	@Transactional(rollbackFor = Exception.class)
	public void changeForwardBySerialNum(int serialNum) throws Exception{
		UserVO user = userQuery.current();
		TerminalType terminalType = TerminalType.fromTokenType(user.getTerminalType());
		TerminalPO terminalEntity = terminalDao.findByType(terminalType);
		if(terminalEntity == null){
			throw new TerminalNotFoundException(terminalType);
		}
		List<String> bundleIds = jv230ForwardDao.findDistinctBundleIdByUserIdAndTerminalIdAndBusinessType(String.valueOf(user.getId()), terminalEntity.getId(), Jv230ForwardBusinessType.QT_TOTAL_FORWARD);
		List<BundlePO> bundles = bundleDao.findByBundleIdIn(bundleIds);
		deleteForwardBySerialNumWithoutTransactional(serialNum);
		PageTaskPO task = pageTaskQueryService.queryPageTask(String.valueOf(user.getId()), terminalEntity.getId(), serialNum);
		if(task.getSrcVideoBundleId() == null) return;
		PageTaskPO bundleTask = task;
		int splitCount = pageTaskQueryService.queryCurrentPageSize(String.valueOf(user.getId()), terminalEntity.getId());
		String layoutName = PlayerSplitLayout.fromPlayerCount(splitCount).getName();
		LayoutPO layout = layoutDao.findByName(layoutName);
		if(layout == null){
			throw new LayoutNotFoundException(layoutName);
		}
		List<LayoutPositionPO> layoutPositions = layoutPositionDao.findByLayoutId(layout.getId());
		List<Jv230ForwardPO> forwards = new ArrayList<Jv230ForwardPO>();
		for(BundlePO bundle:bundles){
			//从0开始
			String screenPrimaryKey = new StringBufferWrapper().append("screen_").append(serialNum + 1).toString();
			String jv230VideoChannelId =  new StringBufferWrapper().append("VenusVideoOut_").append(serialNum + 1).toString();
			Jv230ForwardPO videoForward = new Jv230ForwardPO();
			videoForward.setLayerId(bundle.getAccessNodeUid());
			videoForward.setBundleId(bundle.getBundleId());
			videoForward.setChannelId(jv230VideoChannelId);
			videoForward.setSerialNum(serialNum);
			LayoutPositionPO targetPosition = null;
			for(LayoutPositionPO position:layoutPositions){
				if(position.getScreenPrimaryKey().equals(screenPrimaryKey)){
					targetPosition = position;
					break;
				}
			}
			videoForward.setX(Integer.parseInt(targetPosition.getX()));
			videoForward.setY(Integer.parseInt(targetPosition.getY()));
			videoForward.setW(Integer.parseInt(targetPosition.getWidth()));
			videoForward.setH(Integer.parseInt(targetPosition.getHeight()));
			videoForward.setBusinessName(bundleTask.getBusinessName());
			videoForward.setSourceType(Jv230ForwardSourceType.CHANNEL_VIDEO);
			videoForward.setSourceLayerId(bundleTask.getSrcVideoLayerId());
			videoForward.setSourceBundleId(bundleTask.getSrcVideoBundleId());
			videoForward.setSourceChannelId(bundleTask.getSrcVideoChannelId());
			videoForward.setUserId(String.valueOf(user.getId()));
			videoForward.setTerminalId(terminalEntity.getId());
			videoForward.setBusinessType(Jv230ForwardBusinessType.QT_TOTAL_FORWARD);
			forwards.add(videoForward);
		}
		
		//处理音频（混音）
		List<Jv230ForwardPO> audioForwards = jv230ForwardDao.findByBundleIdInAndSourceTypeInAndUserIdAndTerminalIdAndBusinessType(bundleIds, new ArrayListWrapper<Jv230ForwardSourceType>().add(Jv230ForwardSourceType.COMBINE_AUDIO).add(Jv230ForwardSourceType.CHANNEL_AUDIO).getList(), String.valueOf(user.getId()), terminalEntity.getId(), Jv230ForwardBusinessType.QT_TOTAL_FORWARD);
		
		
		jv230ForwardDao.save(forwards);
		
		//生成协议
		LogicBO protocal = new LogicBO().setJv230AudioSet(new ArrayList<Jv230ForwardBO>())
										.setJv230ForwardSet(new ArrayList<Jv230ForwardBO>());
		for(Jv230ForwardPO forward:forwards){
			if(Jv230ForwardSourceType.CHANNEL_VIDEO.equals(forward.getSourceType())){
				//处理视频转发
				Jv230ForwardBO videoForwardProtocal = new Jv230ForwardBO().setLayerId(forward.getLayerId())
																		  .setBundleId(forward.getBundleId())
																		  .setChannelId(forward.getChannelId())
																		  .setChannel_param(new Jv230ChannelParamBO());
				videoForwardProtocal.getChannel_param().setBase_type("VideoMatrixVideoOut")
													   .setBase_param(new Jv230BaseParamBO());
				videoForwardProtocal.getChannel_param().getBase_param().setCodec_type("h264")
																	   .setIs_polling("false")
																	   .setSrc_mode(0)
																	   .setDisplay_rect(new PositionBO())
																	   .setSource(new Jv230SourceBO())
																	   .setCodec("pcmu");
				videoForwardProtocal.getChannel_param().getBase_param().getDisplay_rect().setX(forward.getX())
																						 .setY(forward.getY())
																						 .setWidth(forward.getW())
																						 .setHeight(forward.getH())
																						 .setZ_index(1);
				videoForwardProtocal.getChannel_param().getBase_param().getSource().setLayer_id(forward.getSourceLayerId())
																				   .setBundle_id(forward.getSourceBundleId())
																				   .setChannel_id(forward.getSourceChannelId());
				protocal.getJv230ForwardSet().add(videoForwardProtocal);
			}else if(Jv230ForwardSourceType.CHANNEL_AUDIO.equals(forward.getSourceType())){
				//处理音频转发
			}else if(Jv230ForwardSourceType.COMBINE_AUDIO.equals(forward.getSourceType())){
				//处理混音
			}
		}
		executeBusiness.execute(protocal, new StringBufferWrapper().append("qt终端修改分屏内容，serianlNum：").append(serialNum).toString());
	}
	
	/**
	 * qt终端某个分屏内容变化<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 上午9:31:24
	 * @param String bundleId jv230设备id
	 * @param int serialNum 分屏序号
	 * @return Jv230ForwardVO 视频转发信息
	 */
	@Transactional(rollbackFor = Exception.class)
	public Jv230ForwardVO changeForwardByBundleIdAndSerialNum(String bundleId, int serialNum) throws Exception{
		UserVO user = userQuery.current();
		TerminalType terminalType = TerminalType.fromTokenType(user.getTerminalType());
		TerminalPO terminalEntity = terminalDao.findByType(terminalType);
		if(terminalEntity == null){
			throw new TerminalNotFoundException(terminalType);
		}
		BundlePO bundle = bundleDao.findByBundleId(bundleId);
		deleteForwardByBundleIdAndSerialNumWithoutTransactional(bundleId, serialNum);
		PageTaskPO task = pageTaskQueryService.queryPageTask(String.valueOf(user.getId()), terminalEntity.getId(), serialNum);
		if(task.getSrcVideoBundleId() == null) return null;
		PageTaskPO bundleTask = task;
		int splitCount = pageTaskQueryService.queryCurrentPageSize(String.valueOf(user.getId()), terminalEntity.getId());
		String layoutName = PlayerSplitLayout.fromPlayerCount(splitCount).getName();
		LayoutPO layout = layoutDao.findByName(layoutName);
		if(layout == null){
			throw new LayoutNotFoundException(layoutName);
		}
		List<LayoutPositionPO> layoutPositions = layoutPositionDao.findByLayoutId(layout.getId());
		List<Jv230ForwardPO> forwards = new ArrayList<Jv230ForwardPO>();
		//从0开始
		String screenPrimaryKey = new StringBufferWrapper().append("screen_").append(serialNum + 1).toString();
		String jv230VideoChannelId =  new StringBufferWrapper().append("VenusVideoOut_").append(serialNum + 1).toString();
		Jv230ForwardPO videoForward = new Jv230ForwardPO();
		videoForward.setLayerId(bundle.getAccessNodeUid());
		videoForward.setBundleId(bundle.getBundleId());
		videoForward.setChannelId(jv230VideoChannelId);
		videoForward.setSerialNum(serialNum);
		LayoutPositionPO targetPosition = null;
		for(LayoutPositionPO position:layoutPositions){
			if(position.getScreenPrimaryKey().equals(screenPrimaryKey)){
				targetPosition = position;
				break;
			}
		}
		videoForward.setX(Integer.parseInt(targetPosition.getX()));
		videoForward.setY(Integer.parseInt(targetPosition.getY()));
		videoForward.setW(Integer.parseInt(targetPosition.getWidth()));
		videoForward.setH(Integer.parseInt(targetPosition.getHeight()));
		videoForward.setBusinessName(bundleTask.getBusinessName());
		videoForward.setSourceType(Jv230ForwardSourceType.CHANNEL_VIDEO);
		videoForward.setSourceLayerId(bundleTask.getSrcVideoLayerId());
		videoForward.setSourceBundleId(bundleTask.getSrcVideoBundleId());
		videoForward.setSourceChannelId(bundleTask.getSrcVideoChannelId());
		videoForward.setUserId(String.valueOf(user.getId()));
		videoForward.setTerminalId(terminalEntity.getId());
		videoForward.setBusinessType(Jv230ForwardBusinessType.QT_TOTAL_FORWARD);
		forwards.add(videoForward);
		
		//处理音频（混音）
		List<Jv230ForwardPO> audioForwards = jv230ForwardDao.findByBundleIdInAndSourceTypeInAndUserIdAndTerminalIdAndBusinessType(new ArrayListWrapper<String>().add(bundleId).getList(), new ArrayListWrapper<Jv230ForwardSourceType>().add(Jv230ForwardSourceType.COMBINE_AUDIO).add(Jv230ForwardSourceType.CHANNEL_AUDIO).getList(), String.valueOf(user.getId()), terminalEntity.getId(), Jv230ForwardBusinessType.QT_TOTAL_FORWARD);
		
		
		jv230ForwardDao.save(forwards);
		
		//生成协议
		LogicBO protocal = new LogicBO().setJv230AudioSet(new ArrayList<Jv230ForwardBO>())
										.setJv230ForwardSet(new ArrayList<Jv230ForwardBO>());
		for(Jv230ForwardPO forward:forwards){
			if(Jv230ForwardSourceType.CHANNEL_VIDEO.equals(forward.getSourceType())){
				//处理视频转发
				Jv230ForwardBO videoForwardProtocal = new Jv230ForwardBO().setLayerId(forward.getLayerId())
																		  .setBundleId(forward.getBundleId())
																		  .setChannelId(forward.getChannelId())
																		  .setChannel_param(new Jv230ChannelParamBO());
				videoForwardProtocal.getChannel_param().setBase_type("VideoMatrixVideoOut")
													   .setBase_param(new Jv230BaseParamBO());
				videoForwardProtocal.getChannel_param().getBase_param().setCodec_type("h264")
																	   .setIs_polling("false")
																	   .setSrc_mode(0)
																	   .setDisplay_rect(new PositionBO())
																	   .setSource(new Jv230SourceBO())
																	   .setCodec("pcmu");
				videoForwardProtocal.getChannel_param().getBase_param().getDisplay_rect().setX(forward.getX())
																						 .setY(forward.getY())
																						 .setWidth(forward.getW())
																						 .setHeight(forward.getH())
																						 .setZ_index(1);
				videoForwardProtocal.getChannel_param().getBase_param().getSource().setLayer_id(forward.getSourceLayerId())
																				   .setBundle_id(forward.getSourceBundleId())
																				   .setChannel_id(forward.getSourceChannelId());
				protocal.getJv230ForwardSet().add(videoForwardProtocal);
			}else if(Jv230ForwardSourceType.CHANNEL_AUDIO.equals(forward.getSourceType())){
				//处理音频转发
			}else if(Jv230ForwardSourceType.COMBINE_AUDIO.equals(forward.getSourceType())){
				//处理混音
			}
		}
		executeBusiness.execute(protocal, new StringBufferWrapper().append("qt终端修改分屏内容，serianlNum：").append(serialNum).toString());
		return new Jv230ForwardVO().set(videoForward);
	}
	
	/**
	 * qt结束某个jv220上某个分屏上的内容（有事务）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 下午2:45:14
	 * @param String bundleId 设备id
	 * @param int serialNum 分屏序号
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteForwardByBundleIdAndSerialNum(String bundleId, int serialNum) throws Exception{
		deleteForwardByBundleIdAndSerialNumWithoutTransactional(bundleId, serialNum);
	}
	
	/**
	 * qt结束某个jv220上某个分屏上的内容（无事务）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 下午2:45:14
	 * @param String bundleId 设备id
	 * @param int serialNum 分屏序号
	 */
	private void deleteForwardByBundleIdAndSerialNumWithoutTransactional(String bundleId, int serialNum) throws Exception{
		UserVO user = userQuery.current();
		TerminalType terminalType = TerminalType.fromTokenType(user.getTerminalType());
		TerminalPO terminalEntity = terminalDao.findByType(terminalType);
		if(terminalEntity == null){
			throw new TerminalNotFoundException(terminalType);
		}
		Jv230ForwardPO videoForward = jv230ForwardDao.findByBundleIdAndSerialNumAndUserIdAndTerminalIdAndBusinessType(bundleId, serialNum, String.valueOf(user.getId()), terminalEntity.getId(), Jv230ForwardBusinessType.QT_TOTAL_FORWARD);
		if(videoForward == null) return;
		LogicBO protocal = new LogicBO().setJv230ForwardDel(new ArrayList<SourceBO>());
		SourceBO deleteJv230ForwardProtocal = new SourceBO();
		deleteJv230ForwardProtocal.setLayerId(videoForward.getLayerId());
		deleteJv230ForwardProtocal.setBundleId(videoForward.getBundleId());
		deleteJv230ForwardProtocal.setChannelId(videoForward.getChannelId());
		protocal.getJv230ForwardDel().add(deleteJv230ForwardProtocal);
		List<Jv230ForwardPO> audioForwards = jv230ForwardDao.findByBundleIdAndSourceTypeInAndUserIdAndTerminalIdAndBusinessType(bundleId, new ArrayListWrapper<Jv230ForwardSourceType>().add(Jv230ForwardSourceType.COMBINE_AUDIO).add(Jv230ForwardSourceType.CHANNEL_AUDIO).getList(), String.valueOf(user.getId()), terminalEntity.getId(), Jv230ForwardBusinessType.QT_TOTAL_FORWARD);
		if(audioForwards!=null && audioForwards.size()>0){
			//如果是混音则移出混音成员
			//如果不是混音则删除音频转发
		}
		executeBusiness.execute(protocal, new StringBufferWrapper().append("qt终端某个jv230分屏停止转发，serialNum：").append(serialNum).append("，bundleId：").append(bundleId).toString());
		jv230ForwardDao.delete(videoForward);
	}
	
	/**
	 * qt结束某个分屏上的内容（有事务）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 上午9:31:57
	 * @param int serialNum 分屏序号
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteForwardBySerialNum(int serialNum) throws Exception{
		deleteForwardBySerialNumWithoutTransactional(serialNum);
	}
	
	/**
	 * qt结束某个分屏上的内容（无事务）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 上午9:31:57
	 * @param int serialNum 分屏序号
	 */
	private void deleteForwardBySerialNumWithoutTransactional(int serialNum) throws Exception{
		UserVO user = userQuery.current();
		TerminalType terminalType = TerminalType.fromTokenType(user.getTerminalType());
		TerminalPO terminalEntity = terminalDao.findByType(terminalType);
		if(terminalEntity == null){
			throw new TerminalNotFoundException(terminalType);
		}
		List<Jv230ForwardPO> videoForwards = jv230ForwardDao.findBySerialNumAndUserIdAndTerminalIdAndBusinessType(serialNum, String.valueOf(user.getId()), terminalEntity.getId(), Jv230ForwardBusinessType.QT_TOTAL_FORWARD);
		if(videoForwards==null || videoForwards.size()<=0) return;
		LogicBO protocal = new LogicBO().setJv230ForwardDel(new ArrayList<SourceBO>());
		for(Jv230ForwardPO forward:videoForwards){
			SourceBO deleteJv230ForwardProtocal = new SourceBO();
			deleteJv230ForwardProtocal.setLayerId(forward.getLayerId());
			deleteJv230ForwardProtocal.setBundleId(forward.getBundleId());
			deleteJv230ForwardProtocal.setChannelId(forward.getChannelId());
			protocal.getJv230ForwardDel().add(deleteJv230ForwardProtocal);
		}
		List<Jv230ForwardPO> audioForwards = jv230ForwardDao.findBySourceTypeInAndUserIdAndTerminalIdAndBusinessType(new ArrayListWrapper<Jv230ForwardSourceType>().add(Jv230ForwardSourceType.COMBINE_AUDIO).add(Jv230ForwardSourceType.CHANNEL_AUDIO).getList(), String.valueOf(user.getId()), terminalEntity.getId(), Jv230ForwardBusinessType.QT_TOTAL_FORWARD);
		if(audioForwards!=null && audioForwards.size()>0){
			//如果是混音则移出混音成员
			//如果不是混音则删除音频转发
		}
		executeBusiness.execute(protocal, new StringBufferWrapper().append("qt终端全部jv230分屏停止转发，serialNum：").append(serialNum).toString());
		jv230ForwardDao.deleteInBatch(videoForwards);
	}
	
	/**
	 * qt结束某个jv230全部转发（有事务）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 下午2:44:49
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteForwardByBundleId(String bundleId) throws Exception{
		deleteForwardByBundleIdWithoutTransactional(bundleId);
	}
	
	/**
	 * qt结束某个jv230全部转发（无事务）<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 下午2:44:49
	 */
	private void deleteForwardByBundleIdWithoutTransactional(String bundleId) throws Exception{
		UserVO user = userQuery.current();
		TerminalType terminalType = TerminalType.fromTokenType(user.getTerminalType());
		TerminalPO terminalEntity = terminalDao.findByType(terminalType);
		if(terminalEntity == null){
			throw new TerminalNotFoundException(terminalType);
		}
		List<Jv230ForwardPO> forwards = jv230ForwardDao.findByBundleIdAndUserIdAndTerminalIdAndBusinessType(bundleId, String.valueOf(user.getId()), terminalEntity.getId(), Jv230ForwardBusinessType.QT_TOTAL_FORWARD);
		if(forwards==null || forwards.size()<=0) return;
		List<Jv230ForwardPO> combineAudioForwards = new ArrayList<Jv230ForwardPO>();
		List<Jv230ForwardPO> combineVideoForwards = new ArrayList<Jv230ForwardPO>();
		LogicBO protocal = new LogicBO().setJv230ForwardDel(new ArrayList<SourceBO>());
		for(Jv230ForwardPO forward:forwards){
			if(Jv230ForwardSourceType.COMBINE_VIDEO.equals(forward.getSourceType())){
				combineVideoForwards.add(forward);
			}else if(Jv230ForwardSourceType.COMBINE_AUDIO.equals(forward.getSourceType())){
				combineAudioForwards.add(forward);
			}
			SourceBO deleteJv230ForwardProtocal = new SourceBO();
			deleteJv230ForwardProtocal.setLayerId(forward.getLayerId());
			deleteJv230ForwardProtocal.setBundleId(forward.getBundleId());
			deleteJv230ForwardProtocal.setChannelId(forward.getChannelId());
			protocal.getJv230ForwardDel().add(deleteJv230ForwardProtocal);
		}
		if(combineAudioForwards.size() > 0){
			//删除混音
		}
		if(combineVideoForwards.size() > 0){
			//删除合屏
		}
		executeBusiness.execute(protocal, new StringBufferWrapper().append("qt终端jv230停止转发，bundleId：").append(bundleId).toString());
		jv230ForwardDao.deleteInBatch(forwards);
	}
	
	/**
	 * qt结束全部上屏内容(有事务)<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 上午9:32:24
	 */
	@Transactional(rollbackFor = Exception.class)
	public void deleteTotalForward() throws Exception{
		deleteTotalForwardWithoutTransactional();
	}
	

	/**
	 * qt结束全部上屏内容(无事务)<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年7月15日 上午9:32:24
	 */
	private void deleteTotalForwardWithoutTransactional() throws Exception{
		UserVO user = userQuery.current();
		TerminalType terminalType = TerminalType.fromTokenType(user.getTerminalType());
		TerminalPO terminalEntity = terminalDao.findByType(terminalType);
		if(terminalEntity == null){
			throw new TerminalNotFoundException(terminalType);
		}
		List<Jv230ForwardPO> forwards = jv230ForwardDao.findByUserIdAndTerminalIdAndBusinessType(String.valueOf(user.getId()), terminalEntity.getId(), Jv230ForwardBusinessType.QT_TOTAL_FORWARD);
		if(forwards==null || forwards.size()<=0) return;
		List<Jv230ForwardPO> combineAudioForwards = new ArrayList<Jv230ForwardPO>();
		List<Jv230ForwardPO> combineVideoForwards = new ArrayList<Jv230ForwardPO>();
		LogicBO protocal = new LogicBO().setJv230ForwardDel(new ArrayList<SourceBO>());
		for(Jv230ForwardPO forward:forwards){
			if(Jv230ForwardSourceType.COMBINE_VIDEO.equals(forward.getSourceType())){
				combineVideoForwards.add(forward);
			}else if(Jv230ForwardSourceType.COMBINE_AUDIO.equals(forward.getSourceType())){
				combineAudioForwards.add(forward);
			}
			SourceBO deleteJv230ForwardProtocal = new SourceBO();
			deleteJv230ForwardProtocal.setLayerId(forward.getLayerId());
			deleteJv230ForwardProtocal.setBundleId(forward.getBundleId());
			deleteJv230ForwardProtocal.setChannelId(forward.getChannelId());
			protocal.getJv230ForwardDel().add(deleteJv230ForwardProtocal);
		}
		if(combineAudioForwards.size() > 0){
			//删除混音
		}
		if(combineVideoForwards.size() > 0){
			//删除合屏
		}
		executeBusiness.execute(protocal, "qt终端jv230全部停止转发");
		jv230ForwardDao.deleteInBatch(forwards);
	}
	
}
