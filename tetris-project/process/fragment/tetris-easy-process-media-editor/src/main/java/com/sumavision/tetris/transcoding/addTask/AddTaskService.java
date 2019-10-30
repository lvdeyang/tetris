package com.sumavision.tetris.transcoding.addTask;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.media.editor.task.MediaEditorTaskRatePermissionVO;
import com.sumavision.tetris.media.editor.task.MediaEditorTaskService;
import com.sumavision.tetris.mims.app.folder.FolderQuery;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.folder.FolderVO;
import com.sumavision.tetris.mims.app.media.StoreType;
import com.sumavision.tetris.mims.app.media.avideo.MediaAVideoQuery;
import com.sumavision.tetris.mims.app.media.avideo.MediaAVideoVO;
import com.sumavision.tetris.transcoding.Adapter;
import com.sumavision.tetris.transcoding.RequestCmdType;
import com.sumavision.tetris.transcoding.addTask.requestVO.AddTaskVO;
import com.sumavision.tetris.transcoding.addTask.requestVO.MsgHeaderVO;
import com.sumavision.tetris.transcoding.addTask.requestVO.SourceVO;
import com.sumavision.tetris.transcoding.addTask.requestVO.SourcesVO;
import com.sumavision.tetris.transcoding.addTask.requestVO.SrcURIVO;
import com.sumavision.tetris.transcoding.addTask.requestVO.TargetVO;
import com.sumavision.tetris.transcoding.addTask.requestVO.TranscodeJobsVO;
import com.sumavision.tetris.transcoding.addTask.requestVO.TranscodeVO;
import com.sumavision.tetris.transcoding.addTask.rsponseVO.AddTaskResponseVO;
import com.sumavision.tetris.transcoding.getStatus.GetStatusHeartbeatThread;

@Service
@Transactional(rollbackFor = Exception.class)
public class AddTaskService {
	
	@Autowired
	private Adapter adapter;
	
	@Autowired
	private MediaEditorTaskService mediaEditorTaskService;
	
	@Autowired
	private MediaAVideoQuery mediaAVideoQuery;
	
	@Autowired
	private FolderQuery folderQuery;
	
	private GetStatusHeartbeatThread getStatusHeartbeatThread;
	
	/**
	 * 添加转码任务<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @return List<String> 下成功的转码任务id
	 */
	public HashMapWrapper<String, MediaEditorTaskRatePermissionVO> add(String __processInstanceId__, Long __accessPointId__, String transcode, String param, String name, Long folderId, String tags) throws Exception{
		List<TranscodeMediaVO> mediaVOs = JSON.parseArray(transcode, TranscodeMediaVO.class);
		if (mediaVOs == null || mediaVOs.isEmpty()) return null;
		FolderVO folder = folderQuery.getById(folderId);
		
		TranscodeVO transcodeVO = new TranscodeVO();
		transcodeVO.setId("");
		transcodeVO.setType("file");
		transcodeVO.setPriority("50");
		transcodeVO.setTarget(new TargetVO());
		ArrayListWrapper<String> mediaUuids = new ArrayListWrapper<String>();
		for (TranscodeMediaVO media : mediaVOs) {
			mediaUuids.add(media.getUuid());
		}
		HashMapWrapper<String, MediaAVideoVO> map = mediaAVideoQuery.getByUuids(mediaUuids.getList());
		if (folder.getType().equals(FolderType.COMPANY_AUDIO)) {
			List<SourceVO> sourceVOs = new ArrayList<SourceVO>();
			transcodeVO.setSource(sourceVOs);
		} else {
			SourcesVO sourcesVO = new SourcesVO();
			sourcesVO.setName("transcode_" + name);
			sourcesVO.setSource(new ArrayListWrapper<SourceVO>().getList());
			transcodeVO.setSources(sourcesVO);
		}
		for (TranscodeMediaVO media : mediaVOs) {
			SrcURIVO src = new SrcURIVO();
			src.setName("");
			MediaAVideoVO mediaInfo = map.getMap().get(media.getUuid());
			if (mediaInfo.getStoreType() == StoreType.REMOTE) {
				src.setValue(mediaInfo.getUploadTmpPath());
			}else {
				src.setValue(adapter.changeHttpToFtp(mediaInfo.getPreviewUrl()));
			}
			SourceVO source = new SourceVO();
			source.setSrcURI(src);
			source.setStartTime(media.getStartTime());
			source.setEndTime(media.getEndTime());
			if (folder.getType().equals(FolderType.COMPANY_AUDIO)) {
				transcodeVO.getSource().add(source);
			} else {
				transcodeVO.getSources().getSource().add(source);
			}
		}
		transcodeVO.getTarget().setTargetURI(adapter.addTreatyToUrl(mediaAVideoQuery.buildUrl(folder, name)));
		transcodeVO.getTarget().setTranscodeTargetParams(param);
		
		TranscodeJobsVO transcodeJobs = new TranscodeJobsVO();
		List<TranscodeVO> transcodeVOs = new ArrayListWrapper<TranscodeVO>().add(transcodeVO).getList();
		transcodeJobs.setTranscode(transcodeVOs);
		
		MsgHeaderVO msgHeader = new MsgHeaderVO();
		msgHeader.setCmdType(folder.getType().equals(FolderType.COMPANY_AUDIO) ? RequestCmdType.ADD_TASK_SINGLE.getTypeName() : RequestCmdType.ADD_TASK_MULTI.getTypeName());
		msgHeader.setTransactionId(adapter.getTransactionId());
		
		AddTaskVO addTask = new AddTaskVO();
		addTask.setMsgHeader(msgHeader);
		addTask.setTranscodeJobs(transcodeJobs);
		
		AddTaskResponseVO response = adapter.addTask(addTask);
		return dealAddResponse(transcodeVOs, folderId, tags, response.getTranscodeJobs().getTranscodes(), __processInstanceId__, __accessPointId__);
	}
	
	/**
	 * 解析添加转码任务的云转码返回信息，开启循环获取进度进程<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年6月25日 上午11:06:57
	 * 
	 * @return HashMapWrapper<String, Integer> 转码任务id，转码任务进度
	 */
	public HashMapWrapper<String, MediaEditorTaskRatePermissionVO> dealAddResponse(List<TranscodeVO> requesTranscodes, Long folderId, String tags, List<com.sumavision.tetris.transcoding.addTask.rsponseVO.TranscodeVO> responseTranscodes, String __processInstanceId__, Long __accessPointId__) throws Exception{
		if (responseTranscodes == null || responseTranscodes.size() < 0) {
			return null;
		}else {
			HashMapWrapper<String, MediaEditorTaskRatePermissionVO> idToInfo = new HashMapWrapper<String, MediaEditorTaskRatePermissionVO>();
			for(com.sumavision.tetris.transcoding.addTask.rsponseVO.TranscodeVO transcode:responseTranscodes){
				if(transcode.getResultCode() == 0 && transcode.getResultString().equals("OK")){
					MediaEditorTaskRatePermissionVO permissionVO = new MediaEditorTaskRatePermissionVO();
					TranscodeVO transcodeItem = requesTranscodes.get(responseTranscodes.indexOf(transcode));
					permissionVO.setSaveUrl(adapter.changeFtpToHttp(transcodeItem.getTarget().getTargetURI()));
					permissionVO.setFolderId(folderId);
					permissionVO.setTags(tags);
					idToInfo.put(transcode.getId(), permissionVO);
				}
			}
			
			//存数据库
			mediaEditorTaskService.addMediaEditorTask(__processInstanceId__, __accessPointId__, idToInfo);
			
			//开启获取任务进度线程
//			if (getStatusHeartbeatThread == null) {
//				getStatusHeartbeatThread = new GetStatusHeartbeatThread();
//				getStatusHeartbeatThread.start();
//			}else {
//				getStatusHeartbeatThread.setSleep(5000);
//			}
			
			return idToInfo;
		}
	}
}
