package com.sumavision.bvc.control.device.group.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.bvc.control.utils.UserUtils;
import com.sumavision.bvc.control.welcome.UserVO;
import com.sumavision.bvc.device.group.dao.DeviceGroupAuthorizationDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupDAO;
import com.sumavision.bvc.device.group.dao.DeviceGroupMemberDAO;
import com.sumavision.bvc.device.group.dao.RecordAssetDAO;
import com.sumavision.bvc.device.group.dao.RecordDAO;
import com.sumavision.bvc.device.group.dto.DeviceGroupMemberDTO;
import com.sumavision.bvc.device.group.enumeration.RecordType;
import com.sumavision.bvc.device.group.po.DeviceGroupAuthorizationPO;
import com.sumavision.bvc.device.group.po.DeviceGroupPO;
import com.sumavision.bvc.device.group.po.RecordAssetPO;
import com.sumavision.bvc.device.group.po.RecordLiveChannelPO;
import com.sumavision.bvc.device.group.po.RecordPO;
import com.sumavision.bvc.device.group.service.DeviceGroupServiceImpl;
import com.sumavision.bvc.device.group.service.log.LogService;
import com.sumavision.bvc.device.group.service.test.ExecuteBusinessProxy;
import com.sumavision.bvc.device.group.service.util.QueryUtil;
import com.sumavision.bvc.listener.MQListener.Path;
import com.sumavision.bvc.system.dao.DictionaryDAO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.util.ts.DownloadTsTask;
import com.sumavision.tetris.mvc.util.ts.DownloadTsTask.Callback;

@Controller
@RequestMapping(value = "/device/group/record")
public class DeviceGroupRecordController {
	
	@Autowired
	private DeviceGroupDAO deviceGroupDao;
	
	@Autowired
	private DeviceGroupAuthorizationDAO deviceGroupAuthorizationDao;
	
	@Autowired
	private DeviceGroupMemberDAO deviceGroupMemberDao;
	
	@Autowired
	private RecordDAO recordDao;
	
	@Autowired
	private DictionaryDAO dictionaryDao;
	
	@Autowired
	private DeviceGroupServiceImpl deviceGroupServiceImpl;
	
	@Autowired
	private ExecuteBusinessProxy executeBusiness;
	
	@Autowired
	private QueryUtil queryUtil;
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	private RecordAssetDAO recordAssetDao;
	
	@Autowired
	private Path path;

	/**
	 * @Title: 查询监控录制设备<br/>
	 * @param groupId 设备组id
	 * @throws Exception
	 * @return 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/query/playlist/{groupId}", method = RequestMethod.GET)
	public Object queryPlaylist(
			@PathVariable Long groupId,
			int pageSize,
			int currentPage,
			HttpServletRequest request) throws Exception{
		
		PageRequest page = new PageRequest(currentPage-1, pageSize);
		
		Page<DeviceGroupMemberDTO> pageMembers = deviceGroupMemberDao.findGroupRecordMembersByGroupId(groupId, page);
		List<RecordPO> records= recordDao.findBundleRecordByGroupId(groupId, RecordType.BUNDLE);
		
		DeviceGroupPO groupPO = deviceGroupDao.findOne(groupId);
		DeviceGroupAuthorizationPO authorizationPO = deviceGroupAuthorizationDao.findByGroupUuid(groupPO.getUuid());
		JSONArray assetArray = new JSONArray();
		if(null == authorizationPO){
			
		}else{
			//先不要直播
//			Set<RecordLiveChannelPO> liveChannels = authorizationPO.getLiveChannels();
//			for(RecordLiveChannelPO liveChannelPO : liveChannels){
//				JSONObject liveChannel = new JSONObject();
//				liveChannel.put("name", liveChannelPO.getName());
//				liveChannel.put("assetPlayUrl", liveChannelPO.getPlayUrl());
//				assetArray.add(liveChannel);
//			}			
			Set<RecordAssetPO> assets = authorizationPO.getAssets();
			for(RecordAssetPO assetPO : assets){				
				JSONObject asset = new JSONObject();
				asset.put("id", assetPO.getId());
				asset.put("name", assetPO.getName());
				asset.put("assetPlayUrl", assetPO.getAssetPlayUrl());
				assetArray.add(asset);
			}
		}
		long total = assetArray.size();
	
		JSONObject data = new JSONObject();
		data.put("rows", assetArray);
		data.put("total", total);
		
		return data;
	}
	
	/**
	 * @Title: 开始监控录制<br/>
	 * @param groupId 设备组id
	 * @param memberId 设备memberId
	 * @throws Exception
	 * @return 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/start/record/{groupId}")
	public Object startRecord(
			@PathVariable Long groupId,
			Long memberId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		DeviceGroupPO group = deviceGroupServiceImpl.startMonitorRecord(groupId, memberId);	
		
		logService.logsHandle(user.getName(), "开始监控录制", "设备组名称："+group.getName()+"设备memberId"+memberId);
		
		return null;
	}
	
	/**
	 * @Title: 停止监控录制<br/>
	 * @param groupId 设备组id
	 * @param memberId 设备memberId
	 * @throws Exception
	 * @return 
	 */
	@ResponseBody
	@JsonBody
	@RequestMapping(value = "/stop/record/{groupId}")
	public Object stopRecord(
			@PathVariable Long groupId,
			Long memberId,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		DeviceGroupPO group = deviceGroupServiceImpl.stopMonitorRecord(groupId, memberId);
		
		logService.logsHandle(user.getName(), "停止监控录制", "设备组名称："+group.getName()+"设备memberId"+memberId);
		
		return null;
	}
	
	/**
	 * 下载点播<br/>
	 * <b>作者:</b>wjw<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月21日 上午11:46:43
	 * @param Long id 录制点播id
	 * @return String downloadAddress 下载地址
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/download/{id}")
	public Object download(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userUtils.getUserFromSession(request);
		
		RecordAssetPO asset = recordAssetDao.findOne(id);
		
		if(asset.getTaskError() != null){
			String error = asset.getTaskError();
			asset.setTaskError(null);
			asset.setTsTaskId(null);
			recordAssetDao.save(asset);
			throw new BaseException(StatusCode.FORBIDDEN, error);
		}
		
		if(asset.getDownloadAddress() != null){
			String strore = asset.getDownloadAddress();
			asset.setDownloadAddress(null);
			recordAssetDao.save(asset);
			return strore;
		}else{
			
			DownloadTsTask task = null;
			
			if(asset.getTsTaskId() != null){
				task = DownloadTsTask.Queue.find(user.getId(), asset.getTsTaskId());
			}
			
			if(task == null){
				
				String tsName = new StringBufferWrapper().append(asset.getName()).append(".ts").toString();
				
				task = new DownloadTsTask(user.getId(), asset.getAssetPlayUrl(), tsName, new Callback() {
					@Override
					public void notify(DownloadTsTask task, Object attach) {

						asset.setDownloadAddress(task.getMergedTsLocalHttpPath());
						asset.setTsTaskId(null);
						recordAssetDao.save(asset);
						
					}
				}, new Callback() {
					
					@Override
					public void notify(DownloadTsTask task, Object attach) {
						if(attach!=null && attach instanceof Exception){
							asset.setTaskError(((Exception)attach).getMessage());
							recordAssetDao.save(asset);
							System.out.println(attach);
						}
					}
				}).run();
				
				asset.setTsTaskId(task.getId());
				recordAssetDao.save(asset);
			}
			
			return null;
			
		}
	}
	
	@RequestMapping(value = "/download/file")
	public void downloadFile(
			String storePath, 
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		storePath = URLDecoder.decode(storePath);
		InputStream in = null;
		try{
			in = new FileInputStream(new StringBufferWrapper().append(path.webappPath()).append(storePath.substring(1, storePath.length()).replace("/", File.separator)).toString());
			String[] contets =  storePath.split("/");
			String fileName = contets[contets.length - 1];
			response.reset();
			response.setContentType("application/x-msdownload");
			response.addHeader("Content-Disposition", "attachment;filename="+fileName);
			byte[] b = new byte[1024];
	        int len;
            while ((len = in.read(b)) > 0){
                response.getOutputStream().write(b, 0, len);
            }
		}finally{
			try{
				if(in != null) in.close();
				response.getOutputStream().close();
			}finally{
				DownloadTsTask.clear(storePath);
			}
		}
	} 
	
}
