package com.sumavision.bvc.control.device.monitor.api_92;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.AccessNodeBO;
import com.suma.venus.resource.base.bo.UserBO;
import com.suma.venus.resource.constant.BusinessConstants;
import com.suma.venus.resource.dao.BundleDao;
import com.suma.venus.resource.pojo.BundlePO;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.control.device.monitor.exception.UserHasNoPermissionForBusinessException;
import com.sumavision.bvc.control.device.monitor.playback.RecordFileVO;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.device.command.user.CommandUserServiceImpl;
import com.sumavision.bvc.device.group.service.util.ResourceQueryUtil;
import com.sumavision.bvc.device.monitor.live.MonitorLiveCommons;
import com.sumavision.bvc.device.monitor.live.MonitorLivePO;
import com.sumavision.bvc.device.monitor.live.MonitorLiveQuery;
import com.sumavision.bvc.device.monitor.live.MonitorLiveService;
import com.sumavision.bvc.device.monitor.live.MonitorUserCallService;
import com.sumavision.bvc.device.monitor.live.call.MonitorLiveCallService;
import com.sumavision.bvc.device.monitor.live.device.MonitorLiveDeviceService;
import com.sumavision.bvc.device.monitor.live.exception.BindingEncoderNotFoundException;
import com.sumavision.bvc.device.monitor.live.user.MonitorLiveUserService;
import com.sumavision.bvc.device.monitor.message.MonitorCalledMessageDAO;
import com.sumavision.bvc.device.monitor.message.MonitorCalledMessagePO;
import com.sumavision.bvc.device.monitor.playback.MonitorRecordPlaybackTaskDAO;
import com.sumavision.bvc.device.monitor.playback.MonitorRecordPlaybackTaskPO;
import com.sumavision.bvc.device.monitor.playback.MonitorRecordPlaybackTaskQuery;
import com.sumavision.bvc.device.monitor.playback.MonitorRecordPlaybackTaskService;
import com.sumavision.bvc.device.monitor.playback.MonitorRecordPlaybackTaskType;
import com.sumavision.bvc.device.monitor.point.MonitorPointService;
import com.sumavision.bvc.device.monitor.ptzctrl.MonitorPtzctrlService;
import com.sumavision.bvc.device.monitor.record.MonitorRecordDAO;
import com.sumavision.bvc.device.monitor.record.MonitorRecordPO;
import com.sumavision.bvc.device.monitor.record.MonitorRecordQuery;
import com.sumavision.bvc.meeting.logic.dao.MediaPushDao;
import com.sumavision.bvc.meeting.logic.po.MediaPushPO;
import com.sumavision.bvc.resource.dao.ResourceBundleDAO;
import com.sumavision.bvc.resource.dao.ResourceChannelDAO;
import com.sumavision.bvc.resource.dto.ChannelSchemeDTO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.util.HttpServletRequestParser;

/**
 * 92项目联网调用接口<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月5日 上午8:47:32
 */
@Controller
@RequestMapping(value = "/monitor/api/92")
public class MonitorApi92Controller {
	
	public static final String username = "Admin";
	
	@Autowired
	private ResourceBundleDAO resourceBundleDao;
	
	@Autowired
	private ResourceChannelDAO resourceChannelDao;
	
	@Autowired
	private MonitorLiveDeviceService monitorLiveDeviceService;
	
	@Autowired
	private MonitorLiveUserService monitorLiveUserService;
	
	@Autowired
	private MonitorLiveCallService monitorLiveCallService;
	
	@Autowired
	private MonitorLiveCommons monitorLiveCommons;
	
	@Deprecated
	@Autowired
	private MonitorLiveQuery monitorLiveQuery;
	
	@Deprecated
	@Autowired
	private MonitorLiveService monitorLiveService;
	
	@Deprecated
	@Autowired
	private MonitorUserCallService monitorUserCallService;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private MonitorRecordDAO monitorRecordDao;
	
	@Autowired
	private MonitorRecordQuery monitorRecordQuery;
	
	@Autowired
	private MonitorRecordPlaybackTaskService monitorRecordPlaybackTaskService;
	
	@Autowired
	private MonitorRecordPlaybackTaskQuery monitorRecordPlaybackTaskQuery;
	
	@Autowired
	private MonitorRecordPlaybackTaskDAO monitorRecordPlaybackTaskDao;
	
	@Autowired
	private MediaPushDao mediaPushDao;
	
	@Autowired
	private ResourceQueryUtil resourceQueryUtil;
	
	@Deprecated
	@Autowired
	private MonitorCalledMessageDAO monitorCalledMessageDao;
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private MonitorPtzctrlService monitorPtzctrlService;
	
	@Autowired
	private MonitorPointService monitorPointService;
	
	@Autowired
	private CommandUserServiceImpl commandUserServiceImpl;
	
	/**
	 * 看编码器<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月4日 下午5:47:40
	 * @param Long seq 协议序号
	 * @param String srcUser 源的设备号码
	 * @param String dstBundle 目标设备bundleid
	 * @return Long seq 协议序号
	 * @return String srcUser 源的设备号码
	 * @return String dstBundle 目标设备bundleid
	 */
	@Deprecated
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/see/encoder")
	public Object seeEncoder(HttpServletRequest request) throws Exception{
		
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		String seq = params.getString("seq");
		String srcUser = params.getString("srcUser");
		String dstBundle = params.getString("dstBundle");
		
		BundlePO srcBundleEntity = resourceBundleDao.findByUsername(srcUser);
		
		List<ChannelSchemeDTO> srcVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(srcBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
		ChannelSchemeDTO srcVideoChannel = srcVideoChannels.get(0);
		
		List<ChannelSchemeDTO> srcAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(srcBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		ChannelSchemeDTO srcAudioChannel = srcAudioChannels.get(0);
		
		List<BundlePO> dstBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(dstBundle).getList());
		BundlePO dstBundleEntity = dstBundleEntities.get(0);
		
		List<ChannelSchemeDTO> dstVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(dstBundleEntity.getBundleId()).getList(), ResourceChannelDAO.DECODE_VIDEO);
		ChannelSchemeDTO dstVideoChannel = dstVideoChannels.get(0);
		
		List<ChannelSchemeDTO> dstAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(dstBundleEntity.getBundleId()).getList(), ResourceChannelDAO.DECODE_AUDIO);
		ChannelSchemeDTO dstAudioChannel = dstAudioChannels.get(0);
		
//		UserBO admin = resourceService.queryUserInfoByUsername(username);
		UserBO admin = new UserBO(); admin.setId(-1L);
		
		monitorLiveService.add(
				null,
				srcBundleEntity.getBundleId(), srcBundleEntity.getBundleName(), srcBundleEntity.getBundleType(), srcBundleEntity.getAccessNodeUid(), srcVideoChannel.getChannelId(), srcVideoChannel.getBaseType(), 
				srcBundleEntity.getBundleId(), srcBundleEntity.getBundleName(), srcBundleEntity.getBundleType(), srcBundleEntity.getAccessNodeUid(), srcAudioChannel.getChannelId(), srcAudioChannel.getBaseType(), 
				dstBundleEntity.getBundleId(), dstBundleEntity.getBundleName(), dstBundleEntity.getBundleType(), dstBundleEntity.getAccessNodeUid(), dstVideoChannel.getChannelId(), dstVideoChannel.getBaseType(), 
				dstBundleEntity.getBundleId(), dstBundleEntity.getBundleName(), dstBundleEntity.getBundleType(), dstBundleEntity.getAccessNodeUid(), dstAudioChannel.getChannelId(), dstAudioChannel.getBaseType(), 
				"DEVICE", admin.getId());
		
		return new HashMapWrapper<String, Object>().put("seq", seq)
												   .put("srcUser", srcUser)
												   .put("dstBundle", dstBundle)
												   .getMap();
	}
	
	/**
	 * 停止看编码器<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月5日 上午8:43:00
	 * @param Long seq 协议序号
	 * @param String srcUser 源设备号码
	 * @param String dstBundle 目标设备bundleId
	 * @return String seq 协议序号
	 * @return String srcUser 源的设备号码
	 * @return String dstBundle 目标设备bundleid
	 */
	@Deprecated
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/stop/see/encoder")
	public Object stopSeeEncoder(HttpServletRequest request) throws Exception{
		
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		String seq = params.getString("seq");
		String srcUser = params.getString("srcUser");
		String dstBundle = params.getString("dstBundle");
		
		BundlePO srcBundleEntity = resourceBundleDao.findByUsername(srcUser);
		
		List<ChannelSchemeDTO> srcVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(srcBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
		ChannelSchemeDTO srcVideoChannel = srcVideoChannels.get(0);
		
		List<ChannelSchemeDTO> srcAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(srcBundleEntity.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
		ChannelSchemeDTO srcAudioChannel = srcAudioChannels.get(0);
		
		List<BundlePO> dstBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(dstBundle).getList());
		BundlePO dstBundleEntity = dstBundleEntities.get(0);
		
		List<ChannelSchemeDTO> dstVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(dstBundleEntity.getBundleId()).getList(), ResourceChannelDAO.DECODE_VIDEO);
		ChannelSchemeDTO dstVideoChannel = dstVideoChannels.get(0);
		
		List<ChannelSchemeDTO> dstAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(dstBundleEntity.getBundleId()).getList(), ResourceChannelDAO.DECODE_AUDIO);
		ChannelSchemeDTO dstAudioChannel = dstAudioChannels.get(0);
		
//		UserBO admin = resourceService.queryUserInfoByUsername(username);
		UserBO admin = new UserBO(); admin.setId(-1L);
		
		MonitorLivePO task = monitorLiveQuery.findBySrcAndDst(
				srcBundleEntity.getBundleId(), srcVideoChannel.getChannelId(), srcBundleEntity.getBundleId(), srcAudioChannel.getChannelId(), 
				dstBundleEntity.getBundleId(), dstVideoChannel.getChannelId(), dstBundleEntity.getBundleId(), dstAudioChannel.getChannelId());
		
		monitorLiveService.remove(task==null?0:task.getId(), task==null?admin.getId():Long.valueOf(task.getUserId()));
		
		return new HashMapWrapper<String, Object>().put("seq", seq)
												   .put("srcUser", srcUser)
												   .put("dstBundle", dstBundle)
												   .getMap();
	}
	
	/**
	 * 看文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月5日 上午8:47:10
	 * @param Long seq 协议序号
	 * @param String srcFile 文件调阅url
	 * @param String dstBundle 目标设备bundleId
	 * @return Long seq 协议序号
	 * @return String srcFile 文件调阅url
	 * @return String dstBundle 目标设备bundleId
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/see/file")
	public Object seeFile(HttpServletRequest request) throws Exception{
		
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		String seq = params.getString("seq");
		String srcFile = params.getString("srcFile");
		String dstBundle = params.getString("dstBundle");
		
		List<BundlePO> dstBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(dstBundle).getList());
		BundlePO dstBundleEntity = dstBundleEntities.get(0);
		
		List<ChannelSchemeDTO> dstVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(dstBundleEntity.getBundleId()).getList(), ResourceChannelDAO.DECODE_VIDEO);
		ChannelSchemeDTO dstVideoChannel = dstVideoChannels.get(0);
		
		List<ChannelSchemeDTO> dstAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(dstBundleEntity.getBundleId()).getList(), ResourceChannelDAO.DECODE_AUDIO);
		ChannelSchemeDTO dstAudioChannel = dstAudioChannels.get(0);
		
//		UserBO admin = resourceService.queryUserInfoByUsername(username);
		UserBO admin = new UserBO(); admin.setId(-1L);
		
		//MonitorRecordPO file = monitorRecordDao.findByPreviewUrl(srcFile);
		if(srcFile.startsWith(RecordFileVO.RECORD)){
			MonitorRecordPO file = monitorRecordDao.findByUuid(srcFile.replace(RecordFileVO.RECORD, ""));
			monitorRecordPlaybackTaskService.addTask(file.getId(), null,
					dstBundleEntity.getBundleId(), dstBundleEntity.getBundleName(), dstBundleEntity.getBundleType(), dstBundleEntity.getAccessNodeUid(), dstVideoChannel.getChannelId(), dstVideoChannel.getBaseType(),
					dstBundleEntity.getBundleId(), dstBundleEntity.getBundleName(), dstBundleEntity.getBundleType(), dstBundleEntity.getAccessNodeUid(), dstAudioChannel.getChannelId(), dstAudioChannel.getBaseType(), 
					admin.getId());
		}else if(srcFile.startsWith(RecordFileVO.IMPORT)){
			//流化文件调阅
			monitorRecordPlaybackTaskService.addTask(srcFile.replace(RecordFileVO.IMPORT, ""), null,
					dstBundleEntity.getBundleId(), dstBundleEntity.getBundleName(), dstBundleEntity.getBundleType(), dstBundleEntity.getAccessNodeUid(), dstVideoChannel.getChannelId(), dstVideoChannel.getBaseType(),
					dstBundleEntity.getBundleId(), dstBundleEntity.getBundleName(), dstBundleEntity.getBundleType(), dstBundleEntity.getAccessNodeUid(), dstAudioChannel.getChannelId(), dstAudioChannel.getBaseType(), 
					admin.getId());
		}
		
		return new HashMapWrapper<String, Object>().put("seq", seq)
												   .put("srcFile", srcFile)
												   .put("dstBundle", dstBundle)
												   .getMap();
	}
	
	/**
	 * 停止看文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月5日 上午8:47:10
	 * @param Long seq 协议序号
	 * @param String srcFile 文件调阅url
	 * @param String dstBundle 目标设备bundleId
	 * @return Long seq 协议序号
	 * @return String srcFile 文件调阅url
	 * @return String dstBundle 目标设备bundleId
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/stop/see/file")
	public Object stopSeeFile(HttpServletRequest request) throws Exception{
		
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		String seq = params.getString("seq");
		String srcFile = params.getString("srcFile");
		String dstBundle = params.getString("dstBundle");
		
		List<BundlePO> dstBundleEntities = resourceBundleDao.findByBundleIds(new ArrayListWrapper<String>().add(dstBundle).getList());
		BundlePO dstBundleEntity = dstBundleEntities.get(0);
		
		List<ChannelSchemeDTO> dstVideoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(dstBundleEntity.getBundleId()).getList(), ResourceChannelDAO.DECODE_VIDEO);
		ChannelSchemeDTO dstVideoChannel = dstVideoChannels.get(0);
		
		List<ChannelSchemeDTO> dstAudioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(dstBundleEntity.getBundleId()).getList(), ResourceChannelDAO.DECODE_AUDIO);
		ChannelSchemeDTO dstAudioChannel = dstAudioChannels.get(0);
		
//		UserBO admin = resourceService.queryUserInfoByUsername(username);
		UserBO admin = new UserBO(); admin.setId(-1L);
		
		//查询文件
		//MonitorRecordPO file = monitorRecordDao.findByPreviewUrl(srcFile);
		if(srcFile.startsWith(RecordFileVO.RECORD)){
			MonitorRecordPO file = monitorRecordDao.findByUuid(srcFile.replace(RecordFileVO.RECORD, ""));
			MonitorRecordPlaybackTaskPO task = monitorRecordPlaybackTaskQuery.findByFileAndDst(
					file.getUuid(), dstBundleEntity.getBundleId(), dstVideoChannel.getChannelId(), dstBundleEntity.getBundleId(), dstAudioChannel.getChannelId(), admin.getId().toString(), MonitorRecordPlaybackTaskType.RECORD);
			
			monitorRecordPlaybackTaskService.remove(task==null?0:task.getId(), admin.getId());
		}else if(srcFile.startsWith(RecordFileVO.IMPORT)){
			//流化文件调阅
			MonitorRecordPlaybackTaskPO task = monitorRecordPlaybackTaskQuery.findByFileAndDst(
					srcFile.replace(RecordFileVO.IMPORT, ""), dstBundleEntity.getBundleId(), dstVideoChannel.getChannelId(), dstBundleEntity.getBundleId(), dstAudioChannel.getChannelId(), admin.getId().toString(), MonitorRecordPlaybackTaskType.IMPORT);
			
			monitorRecordPlaybackTaskService.remove(task==null?0:task.getId(), admin.getId());
		}
		
		return new HashMapWrapper<String, Object>().put("seq", seq)
												   .put("srcFile", srcFile)
												   .put("dstBundle", dstBundle)
												   .getMap();
	}
	
	/**
	 * 文件调阅结束<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月14日 上午8:34:01
	 * @param String seq 序列号
	 * @param String srcBundleId 源文件bundleId
	 * @param String srcChannelId 源文件channelId
	 * @return seq String 序列号
	 * @return srcBundleId String 源文件bundleId
	 * @return srcChannelId String 源文件channelId
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/file/to/end")
	public Object fileToEnd(HttpServletRequest request) throws Exception{
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		String seq = params.getString("seq");
		String srcBundleId = params.getString("bundleid");
		String srcChannelId = params.getString("channelid");
		
		MediaPushPO mediaPush = mediaPushDao.findByBundleIdAndChannelId(srcBundleId, srcChannelId);
		
		MonitorRecordPlaybackTaskPO task = monitorRecordPlaybackTaskDao.findByUuid(mediaPush.getUuid());
		
		if(task != null){
			monitorRecordPlaybackTaskService.remove(task==null?0:task.getId(), Long.valueOf(task.getUserId()));
		}
		
		return new HashMapWrapper<String, Object>().put("seq", seq)
												   .put("srcBundleId", srcBundleId)
												   .put("srcChannelId", srcChannelId)
												   .getMap();
	}
	
	/**
	 * 根据设备查询录制<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月5日 上午8:52:52
	 * @param String bundleId 设备bundleId
	 * @param int beginIndex 开始索引
	 * @param int pageSize 查询数据量
	 * @return total int 总数据量
	 * @return rows List<MonitorRecordVO> 文件列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/file/by/bundle/id")
	public Object findFileByBundleId(HttpServletRequest request) throws Exception{
		
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		String bundleId = params.getString("bundleId");
		int beginIndex = params.getIntValue("beginIndex");
		int pageSize = params.getIntValue("pageSize");
		
//		UserBO admin = resourceService.queryUserInfoByUsername(username);
		UserBO admin = new UserBO(); admin.setId(-1L);
		
		List<MonitorRecordPO> entities = monitorRecordDao.findByVideoBundleId(bundleId);
		
		List<RecordFileVO> rows = new ArrayList<RecordFileVO>();
		if(entities!=null && entities.size()>0){
			Set<String> layerIds = new HashSet<String>();
			for(MonitorRecordPO entity:entities){
				layerIds.add(entity.getStoreLayerId());
			}
			List<AccessNodeBO> layers = resourceService.queryAccessNodeByNodeUids(layerIds);
			
			for(MonitorRecordPO entity:entities){
				AccessNodeBO targetLayer = null;
				for(AccessNodeBO layer:layers){
					if(layer.getNodeUid().equals(entity.getStoreLayerId())){
						targetLayer = layer;
						break;
					}
				}
				rows.add(new RecordFileVO().set(entity, admin.getId(), RecordFileVO.RECORD, targetLayer));
			}
		}
		
		Collections.sort(rows, new RecordFileVO.RecordFileComparator());
		List<RecordFileVO> cutRows = new ArrayList<RecordFileVO>();
		int endIndex = rows.size()<(beginIndex-1+pageSize)?rows.size():beginIndex-1+pageSize;
		for(int i=beginIndex-1; i<endIndex; i++){
			cutRows.add(rows.get(i));
		}
		
		return new HashMapWrapper<String, Object>().put("total", rows.size())
												   .put("rows", cutRows)
												   .getMap();
	}
	
	/**
	 * 根据文件名模糊查询文件(不能调阅的都过滤了)<br/>
	 * <p>详细描述</p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月8日 下午2:02:13
	 * @param String fileName 文件名
	 * @param int beginIndex 开始索引
	 * @param int pageSize 查询数据量
	 * @return total int 总数据量
	 * @return rows List<RecordFileVO> 文件列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/file/by/name")
	public Object findFileByName(HttpServletRequest request) throws Exception{
		
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		String seq = params.getString("seq");
		String fileName = params.getString("fileName");
		int beginIndex = params.getIntValue("beginIndex");
		int pageSize = params.getIntValue("pageSize");
		
//		UserBO admin = resourceService.queryUserInfoByUsername(username);
		UserBO admin = new UserBO(); admin.setId(-1L);
		
		List<RecordFileVO> rows = new ArrayList<RecordFileVO>();
		
		//查询有权限的设备
		List<BundlePO> queryBundles = resourceQueryUtil.queryUseableBundles(admin.getId());
		if(queryBundles!=null && queryBundles.size()>0){
			Set<String> bundleIds = new HashSet<String>();
			for(BundlePO bundle:queryBundles){
				bundleIds.add(bundle.getBundleId());
			}
			List<MonitorRecordPO> entities = monitorRecordQuery.findByVideoBundleIdInAndFileNameLike(bundleIds, fileName);
			if(entities!=null && entities.size()>0){
				Set<String> layerIds = new HashSet<String>();
				for(MonitorRecordPO entity:entities){
					layerIds.add(entity.getStoreLayerId());
				}
				List<AccessNodeBO> layers = resourceService.queryAccessNodeByNodeUids(layerIds);
				
				for(MonitorRecordPO entity:entities){
					AccessNodeBO targetLayer = null;
					for(AccessNodeBO layer:layers){
						if(layer.getNodeUid().equals(entity.getStoreLayerId())){
							targetLayer = layer;
							break;
						}
					}
					rows.add(new RecordFileVO().set(entity, admin.getId(), RecordFileVO.RECORD, targetLayer));
				}
			}
		}
		
		//有权限的点播文件
		List<JSONObject> vods = resourceService.queryOnDemandResourceByNameLike(fileName);
		if(vods!=null && vods.size()>0){
			for(JSONObject vod:vods){
				rows.add(new RecordFileVO().setUuid(new StringBufferWrapper().append(RecordFileVO.IMPORT).append(vod.getString("resourceId")).toString())
										   .setFileName(vod.getString("name"))
										   .setPreviewUrl(vod.getString("previewUrl")));
			}
		}
		
		Collections.sort(rows, new RecordFileVO.RecordFileComparator());
		List<RecordFileVO> cutRows = new ArrayList<RecordFileVO>();
		int endIndex = rows.size()<(beginIndex-1+pageSize)?rows.size():beginIndex-1+pageSize;
		for(int i=beginIndex-1; i<endIndex; i++){
			cutRows.add(rows.get(i));
		}
		
		return new HashMapWrapper<String, Object>().put("seq", seq)
												   .put("total", rows.size())
												   .put("rows", cutRows)
												   .getMap();
	}
	
	/**
	 * 根据区间查询录制文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月8日 下午2:50:44
	 * @param String startTime 时间下限 格式：yyyy-MM-dd hh:mm:ss
	 * @param String endTime 时间下限 格式：yyyy-MM-dd hh:mm:ss
	 * @param int beginIndex 开始索引
	 * @param int pageSize 查询数据量
	 * @return total int 总数据量
	 * @return rows List<RecordFileVO> 文件列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/file/by/time")
	public Object findFileByTime(HttpServletRequest request) throws Exception{
		
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		String seq = params.getString("seq");
		String startTime = params.getString("startTime");
		String endTime = params.getString("endTime");
		int beginIndex = params.getIntValue("beginIndex");
		int pageSize = params.getIntValue("pageSize");
		
		Date parsedStartTime = startTime==null?null:DateUtil.parse(startTime, DateUtil.dateTimePattern);
		Date parsedEndTime = endTime==null?null:DateUtil.parse(endTime, DateUtil.dateTimePattern);
		
//		UserBO admin = resourceService.queryUserInfoByUsername(username);
		UserBO admin = new UserBO(); admin.setId(-1L);
		
		List<RecordFileVO> rows = new ArrayList<RecordFileVO>();
		
		List<BundlePO> queryBundles = resourceQueryUtil.queryUseableBundles(admin.getId());
		if(queryBundles!=null && queryBundles.size()>0){
			Set<String> bundleIds = new HashSet<String>();
			for(BundlePO bundle:queryBundles){
				bundleIds.add(bundle.getBundleId());
			}
			List<MonitorRecordPO> entities = null;
			if(parsedStartTime==null && parsedEndTime==null){
				entities = monitorRecordDao.findByVideoBundleIdInAndStoreLayerIdIsNotNull(bundleIds);
			}else if(parsedStartTime!=null && parsedEndTime==null){
				entities = monitorRecordDao.findByVideoBundleIdInAndStartTimeGreaterThanEqualAndStoreLayerIdIsNotNull(bundleIds, parsedStartTime);
			}else if(parsedStartTime==null && parsedEndTime!=null){
				entities = monitorRecordDao.findByVideoBundleIdInAndStartTimeLessThanEqualAndStoreLayerIdIsNotNull(bundleIds, parsedEndTime);
			}else if(parsedStartTime!=null && parsedEndTime!=null){
				entities = monitorRecordDao.findByVideoBundleIdInAndStartTimeGreaterThanEqualAndStartTimeLessThanEqualAndStoreLayerIdIsNotNull(bundleIds, parsedStartTime, parsedEndTime);
			}
			if(entities!=null && entities.size()>0){
				Set<String> layerIds = new HashSet<String>();
				for(MonitorRecordPO entity:entities){
					layerIds.add(entity.getStoreLayerId());
				}
				List<AccessNodeBO> layers = resourceService.queryAccessNodeByNodeUids(layerIds);
				
				for(MonitorRecordPO entity:entities){
					AccessNodeBO targetLayer = null;
					for(AccessNodeBO layer:layers){
						if(layer.getNodeUid().equals(entity.getStoreLayerId())){
							targetLayer = layer;
							break;
						}
					}
					rows.add(new RecordFileVO().set(entity, admin.getId(), RecordFileVO.RECORD, targetLayer));
				}
			}
		}
		
		Collections.sort(rows, new RecordFileVO.RecordFileComparator());
		List<RecordFileVO> cutRows = new ArrayList<RecordFileVO>();
		int endIndex = rows.size()<(beginIndex-1+pageSize)?rows.size():beginIndex-1+pageSize;
		for(int i=beginIndex-1; i<endIndex; i++){
			cutRows.add(rows.get(i));
		}
		
		return new HashMapWrapper<String, Object>().put("seq", seq)
												   .put("total", rows.size())
												   .put("rows", cutRows)
												   .getMap();
	}
	
	/**
	 * 查询所有联网资源<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午2:34:26
	 * @param String seq 序号
	 * @return seq String 序号
	 */
	@Deprecated
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/get/all/relation/resource")
	public Object getAllRelationResource(HttpServletRequest request) throws Exception{
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		String seq = params.getString("seq");
		
		JSONObject resources = resourceService.queryEncodersAndEncoders();
		
		return new HashMapWrapper<String, Object>().put("seq", seq)
												   .put("ldap", resources.getJSONObject("ldap"))
												   .put("local", resources.getJSONObject("local"))
												   .getMap();
	}
	
	/**
	 * 接收双向呼叫认证<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午2:39:01
	 * @param String seq 序号
	 * @param String ldap_user 主叫用户
	 * @param String local_user 被叫用户
	 * @return seq String 序号
	 * @return ldap_user String 主叫用户
	 * @return local_user String 被叫用户
	 */
	@Deprecated
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/two/side/invite/certify")
	public Object twoSideInviteCertify(HttpServletRequest request) throws Exception{
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		String seq = params.getString("seq");
		String ldap_user = params.getString("ldap_user");
		String local_user = params.getString("local_user");
		
//		UserBO admin = resourceService.queryUserInfoByUsername(username);
		UserBO admin = new UserBO(); admin.setId(-1L);
		
		int action = 0;
		UserBO user = resourceService.queryUserInfoByUsername(local_user);
		if(user == null){
			//用户不存在
			action = 4;
		}/*else if(!user.isLogined()){
			//用户没有登录
			action = 3;
		}*/
		
		//要给联网发消息，所以去ldap_user的接入层
		BundlePO encoder = resourceService.queryLdapEncoderByUserNo(ldap_user);
		if(encoder == null){
			throw new BindingEncoderNotFoundException(ldap_user);
		}
		
		if(action > 0){
			//用户不存在或没有登录
			monitorUserCallService.sendUserTwoSideCallActionWithPassBy(encoder.getAccessNodeUid(), ldap_user, local_user, action, admin.getId());
		}else{
			//添加一条消息
			UserBO ldapUser = resourceService.queryLdapUserByUserNo(ldap_user);
			String message = new StringBufferWrapper().append("用户：").append(ldapUser.getName()).append("邀请你视频通话。").toString();
			MonitorCalledMessagePO calledMessage = new MonitorCalledMessagePO();
			calledMessage.setUpdateTime(new Date());
			calledMessage.setCallUser(ldap_user);
			calledMessage.setReceiveUser(local_user);
			calledMessage.setMessage(message);
			calledMessage.setLayerId(encoder.getAccessNodeUid());
			calledMessage.setNetworkUserId(admin.getId());
			try{
				monitorCalledMessageDao.save(calledMessage);
			}catch(Exception e){
				e.printStackTrace();
				//用户正忙
				monitorUserCallService.sendUserTwoSideCallActionWithPassBy(encoder.getAccessNodeUid(), ldap_user, local_user, 5, admin.getId());
			}
		}
		
		return new HashMapWrapper<String, Object>().put("seq", seq)
												   .put("ldap_user", ldap_user)
												   .put("local_user", local_user)
												   .getMap();
	}
	
	/**
	 * 用户双向通信操作<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午4:06:45
	 * @param String seq 序列号
	 * @param String src_user 主叫用户
	 * @param String dst_user 被叫用户
	 * @param String cmd 操作：start/stop 
	 * @param JSONArray sides [{src_bundle:String(源的bundle—编码器), dst_bundle:String(目标的bundle—解码器)}] 转发关系
	 * @return seq String 序列号
	 * @return src_user String 主叫用户
	 * @return dst_user String 被叫用户
	 * @return cmd String 操作
	 * @return sides JSONArray 转发关系
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/two/side/set")
	@Deprecated
	public synchronized Object twoSideSet(HttpServletRequest request) throws Exception{
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		System.out.println("*******************************************");
		System.out.println("*****92api*/two/side/set*******************");
		System.out.println("*******************************************");
		System.out.println(params.toJSONString());
		String seq = params.getString("seq");
		String srcUser = params.getString("src_user");
		String dstUser = params.getString("dst_user");
		String type = params.getString("type");
		String uuid = params.getString("uuid");
		String cmd = params.getString("cmd");
		JSONArray sides = params.getJSONArray("sides");

//		UserBO admin = resourceService.queryUserInfoByUsername(username);
		UserBO admin = new UserBO(); admin.setId(-1L);
		
		if(cmd.equals("stop")){
			monitorUserCallService.stopUserTwoSideCall(srcUser, dstUser, uuid, type, admin.getId());
		}else if(cmd.equals("start")){
			monitorUserCallService.startUserTwoSideCall(srcUser, dstUser, sides, uuid, type, admin.getId());
		}
		
		return new HashMapWrapper<String, Object>().put("seq", seq)
												   .put("src_user", srcUser)
												   .put("dst_user", dstUser)
												   .put("cmd", cmd)
												   .put("sides", sides)
												   .getMap();
	}
	
	/**
	 * xt业务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年5月21日 下午4:06:45
	 * @param String uuid 点播呼叫业务uuid
	 * @param String type 业务类型play/call/paly-call
	 * @param String operate 操作类型 start/stop
	 * @param String src_userno 发起方的用户号码
	 * @param String dst_no 被叫号码 
	 * @return String uuid 点播呼叫业务uuid
	 * @return String type 业务类型play/call/paly-call
	 * @return String operate 操作类型 start/stop
	 * @return String src_userno 发起方的用户号码
	 * @return String dst_no 被叫号码 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/xt/business")
	public synchronized Object xtBusiness(HttpServletRequest request) throws Exception{
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		System.out.println("*******************************************");
		System.out.println("******92api*/xt/business*******************");
		System.out.println("*******************************************");
		System.out.println(params.toJSONString());
		
		String uuid = params.getString("uuid");
		String type = params.getString("type");
		String operate = params.getString("operate");
		String src_userno = params.getString("src_userno");
		String dst_no = params.getString("dst_no");
		
		try{
			UserBO srcUser = userUtils.queryUserByUserno(src_userno);
			
			UserBO dstUser = userUtils.queryUserByUserno(dst_no);
			if("start".equals(operate)){
				if(dstUser != null){
					if("play".equals(type)){
						//开始xt点播本地用户
						monitorLiveUserService.startXtSeeLocal(uuid, dstUser, srcUser.getId(), srcUser.getName(), srcUser.getUserNo());
					}else if("call".equals(type)){
						//开始xt用户呼叫本地用户
//						monitorLiveCallService.startXtCallLocal(uuid, dstUser, srcUser);
						commandUserServiceImpl.userCallUser_Cascade(srcUser, dstUser, -1, uuid);
					}else if("paly-call".equals(type)){
						//开始xt点播本地用户转xt呼叫本地用户
						monitorLiveCallService.transXtCallLocal(uuid, dstUser, srcUser);
					}
				}else{
					//开始点播设备
					BundlePO bundle = bundleDao.findByUsername(dst_no);
					List<ChannelSchemeDTO> videoChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(bundle.getBundleId()).getList(), ResourceChannelDAO.ENCODE_VIDEO);
					ChannelSchemeDTO videoChannel = videoChannels.get(0);
					List<ChannelSchemeDTO> audioChannels = resourceChannelDao.findByBundleIdsAndChannelType(new ArrayListWrapper<String>().add(bundle.getBundleId()).getList(), ResourceChannelDAO.ENCODE_AUDIO);
					ChannelSchemeDTO audioChannel = (audioChannels==null||audioChannels.size()<=0)?null:audioChannels.get(0);
					if(audioChannel != null){
						monitorLiveDeviceService.startXtSeeLocal(
								null, uuid, 
								bundle.getBundleId(), bundle.getBundleName(), bundle.getBundleType(), bundle.getAccessNodeUid(), videoChannel.getChannelId(), videoChannel.getBaseType(), 
								bundle.getBundleId(), bundle.getBundleName(), bundle.getBundleType(), bundle.getAccessNodeUid(), audioChannel.getChannelId(), audioChannel.getBaseType(), 
								srcUser.getId(), srcUser.getUserNo());
					}else{
						monitorLiveDeviceService.startXtSeeLocal(
								null, uuid, 
								bundle.getBundleId(), bundle.getBundleName(), bundle.getBundleType(), bundle.getAccessNodeUid(), videoChannel.getChannelId(), videoChannel.getBaseType(), 
								null, null, null, null, null, null, 
								srcUser.getId(), srcUser.getUserNo());
					}
				}
			}else if("stop".equals(operate)){
				if(dstUser != null){
					if("play".equals(type)){
						//停止xt点播本地用户
						monitorLiveUserService.stop(uuid, srcUser.getId(), srcUser.getUserNo());
					}else if("call".equals(type)){
						//停止xt用户呼叫本地用户
//						monitorLiveCallService.stop(uuid, srcUser.getId());
						commandUserServiceImpl.stopCall_Cascade(srcUser, null, uuid);
					}else if("paly-call".equals(type)){
						//停止xt点播本地用户转xt呼叫本地用户
					}
				}else{
					//停止点播设备
					monitorLiveDeviceService.stop(uuid, srcUser.getId(), srcUser.getUserNo());
				}
			}else if("accept".equals(operate)){
				if(dstUser != null){
					if("call".equals(type)){
						//xt用户同意接听呼叫
						commandUserServiceImpl.acceptCall_Cascade(srcUser, null, uuid);
					}else if("paly-call".equals(type)){
						//TODO: xt用户同意点播转呼叫
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			if("start".equals(operate)){
				if(e instanceof BaseException){
					BaseException businessException = (BaseException)e;
					monitorLiveCommons.sendFailPassby(uuid, businessException.getProtocol(), businessException.getMessage(), 1l);
				}else{
					monitorLiveCommons.sendFailPassby(uuid, "0", "业务异常", 1l);
				}
			}
		}
		
		return params;
	}
	
	/**
	 * 云镜控制<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月11日 上午9:48:04
	 * @param String seq 操作数
	 * @param String fromNumber 发起的用户号码，需要使用这个校验权限
	 * @param String toNumber 目标的编码器号码，使用这个找到对应的接入，将消息透传
	 * @param String content xml内容
	 * @return String seq 操作数
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/ptzctrl")
	public Object ptzctrl(HttpServletRequest request) throws Exception{
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		String seq = params.getString("seq");
		String fromNumber = params.getString("fromNumber");
		String toNumber = params.getString("toNumber");
		String content = params.getString("content");
		
		UserBO fromUser = userUtils.queryUserByUserno(fromNumber);
		BundlePO toBundle = bundleDao.findByUsername(toNumber);
		
		boolean result = resourceService.hasPrivilegeOfBundle(fromUser.getId(), toBundle.getBundleId(), BusinessConstants.BUSINESS_OPR_TYPE.CALL);
		if(!result){
			throw new UserHasNoPermissionForBusinessException(BusinessConstants.BUSINESS_OPR_TYPE.DIANBO, 0);
		}
		
		monitorPtzctrlService.passBy(
				fromUser.getId().toString(), 
				toBundle.getBundleId(), 
				toBundle.getAccessNodeUid(), 
				content);
		
		return new HashMapWrapper<String, Object>().put("seq", seq);
	}
	
	/**
	 * 预置点操作<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月11日 下午5:58:06
	 * @param String seq 操作数
	 * @param String fromNumber 发起的用户号码，需要使用这个校验权限
	 * @param String toNumber 目标的编码器号码，使用这个找到对应的接入，将消息透传
	 * @param String content xml内容
	 * @return String seq 操作数
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/point/index")
	public Object pointIndex(HttpServletRequest request) throws Exception{
		HttpServletRequestParser parser = new HttpServletRequestParser(request);
		JSONObject params = parser.parseJSON();
		String seq = params.getString("seq");
		String fromNumber = params.getString("fromNumber");
		String toNumber = params.getString("toNumber");
		String content = params.getString("content");
		
		UserBO fromUser = userUtils.queryUserByUserno(fromNumber);
		BundlePO toBundle = bundleDao.findByUsername(toNumber);
		
		boolean result = resourceService.hasPrivilegeOfBundle(fromUser.getId(), toBundle.getBundleId(), BusinessConstants.BUSINESS_OPR_TYPE.CALL);
		if(!result){
			throw new UserHasNoPermissionForBusinessException(BusinessConstants.BUSINESS_OPR_TYPE.DIANBO, 0);
		}
		
		if(content.indexOf("<operation>add</operation>") >= 0){
			monitorPointService.passbyAdd(toBundle.getBundleId(), toBundle.getBundleName(), toBundle.getAccessNodeUid(), String.valueOf(new Date().getTime()), fromUser.getId(), fromUser.getName(), content);
		}else if(content.indexOf("<operation>remove</operation>") >= 0){
			monitorPointService.passByRemove(fromUser.getId(), toBundle.getBundleId(), toBundle.getAccessNodeUid(), content);
		}else if(content.indexOf("<operation>invoke</operation>") >= 0){
			monitorPointService.passByInvoke(fromUser.getId(), toBundle.getBundleId(), toBundle.getAccessNodeUid(), content);
		}
		
		return new HashMapWrapper<String, Object>().put("seq", seq);
	}
	
}
