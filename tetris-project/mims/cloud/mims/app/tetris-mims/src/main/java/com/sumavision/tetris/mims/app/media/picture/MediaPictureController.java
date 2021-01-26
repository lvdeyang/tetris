package com.sumavision.tetris.mims.app.media.picture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.binary.ByteUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.easy.process.core.ProcessQuery;
import com.sumavision.tetris.easy.process.core.ProcessService;
import com.sumavision.tetris.easy.process.core.ProcessVO;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderQuery;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.folder.exception.FolderNotExistException;
import com.sumavision.tetris.mims.app.folder.exception.UserHasNoPermissionForFolderException;
import com.sumavision.tetris.mims.app.material.exception.OffsetCannotMatchSizeException;
import com.sumavision.tetris.mims.app.media.UploadStatus;
import com.sumavision.tetris.mims.app.media.picture.exception.MediaPictureCannotMatchException;
import com.sumavision.tetris.mims.app.media.picture.exception.MediaPictureErrorBeginOffsetException;
import com.sumavision.tetris.mims.app.media.picture.exception.MediaPictureNotExistException;
import com.sumavision.tetris.mims.app.media.picture.exception.MediaPictureStatusErrorWhenUploadCancelException;
import com.sumavision.tetris.mims.app.media.picture.exception.MediaPictureStatusErrorWhenUploadErrorException;
import com.sumavision.tetris.mims.app.media.picture.exception.MediaPictureStatusErrorWhenUploadingException;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsDAO;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsPO;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsType;
import com.sumavision.tetris.mims.app.operation.accessRecord.OperationRecordService;
import com.sumavision.tetris.mims.config.server.MimsServerPropsQuery;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.MultipartHttpServletRequestWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/media/picture")
public class MediaPictureController {

	@Autowired
	private FolderQuery folderQuery;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private MediaPictureQuery mediaPictureQuery;
	
	@Autowired
	private MediaPictureService mediaPictureService;
	
	@Autowired
	private MediaPictureDAO mediaPictureDao;
	
	@Autowired
	private MediaSettingsDAO mediaSettingsDao;
	
	@Autowired
	private ProcessQuery processQuery;
	
	@Autowired
	private ProcessService processService;
	
	@Autowired
	private MimsServerPropsQuery serverPropsQuery; 
	
	@Autowired
	private OperationRecordService operationRecordService;
	
	/**
	 * 加载文件夹下的图片媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @param folderId 文件夹id
	 * @return rows List<MediaPictureVO> 图片媒资列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/{folderId}")
	public Object load(
			@PathVariable Long folderId,
			HttpServletRequest request) throws Exception{
		
		return mediaPictureQuery.load(folderId);
	}
	
	/**
	 * 添加上传图片媒资任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午1:44:06
	 * @param JSONString task{name:文件名称, size:文件大小, mimetype:文件mime类型, lastModified:最后更新时间}
	 * @param String name 媒资名称
	 * @param JSONString tags 标签数组
	 * @param JSONString keyWords 关键字数组
	 * @param String remark 备注
	 * @param Long folerId 文件夹id		
	 * @return List<MaterialFileTaskVO> 任务列表 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/task/add")
	public Object addTask(
			String task, 
			String name,
            String tags,
            String keyWords,
            String remark,
			Long folderId, 
			String addition,			
			HttpServletRequest request) throws Exception{
		
		MediaPictureTaskVO taskParam = JSON.parseObject(task, MediaPictureTaskVO.class);
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), folderId)){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		FolderPO folder = folderDao.findById(folderId);
		if(folder == null){
			throw new FolderNotExistException(folderId);
		}
		
		List<String> tagList = new ArrayList<String>();
		if (tags!=null && !tags.isEmpty()) {
			tagList = Arrays.asList(tags.split(","));
		}
		
		List<String> keyWordList = new ArrayList<String>();
		if(keyWords != null){
			keyWordList = Arrays.asList(keyWords.split(","));
		}
		
		MediaPicturePO entity = mediaPictureService.addTask(user, name, tagList, keyWordList, remark, taskParam, folder);
		if (addition != null) entity.setAddition(addition);
		mediaPictureDao.save(entity);
		
		return new MediaPictureVO().set(entity);
		
	}
	
	/**
	 * 编辑图片媒资<br/>
	 * <b>作者:</b>sm<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月25日 下午3:22:50
	 * @param id 图片媒资id
	 * @param name 图片媒资名称
	 * @param tags 标签列表
	 * @param keyWords 关键字列表
	 * @param remark 备注
	 * @return MediaPictureVO 图片媒资
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/task/edit/{id}")
	public Object editTask(
			@PathVariable Long id,
			String name,
            String tags,
            String keyWords,
            String remark,
            String addition,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		MediaPicturePO picture = mediaPictureDao.findById(id);
		if(picture == null){
			throw new MediaPictureNotExistException(id);
		}
		
		List<String> tagList = new ArrayList<String>();
		if(tags!=null && !tags.isEmpty()){
			tagList = Arrays.asList(tags.split(","));
		}
		
		List<String> keyWordList = new ArrayList<String>();
		if(keyWords != null){
			keyWordList = Arrays.asList(keyWords.split(","));
		}
		
		MediaPicturePO entity = mediaPictureService.editTask(user, picture, name, tagList, keyWordList, remark);
		if (addition != null) entity.setAddition(addition);
		mediaPictureDao.save(entity);
		
		return new MediaPictureVO().set(entity);
		
	}
	
	/**
	 * 查询图片媒资上传任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月30日 上午10:10:56
	 * @return List<MediaPictureVO> 任务列表 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/tasks")
	public Object queryTasks(HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		Set<Long> folderIds = new HashSet<Long>();
		
		//获取图片媒资库根目录
		FolderPO folder = folderDao.findCompanyRootFolderByType(user.getGroupId(), FolderType.COMPANY_PICTURE.toString());
		folderIds.add(folder.getId());
		List<FolderPO> subFolders = folderQuery.findSubFolders(folder.getId());
		if(subFolders!=null && subFolders.size()>0){
			for(FolderPO subFolder:subFolders){
				folderIds.add(subFolder.getId());
			}
		}
		
		List<MediaPicturePO> tasks = mediaPictureQuery.findTasksByFolderIds(folderIds);
		
		List<MediaPictureVO> view_tasks = new ArrayList<MediaPictureVO>();
		
		if(tasks!=null && tasks.size()>0){
			for(MediaPicturePO task:tasks){
				MediaPictureVO view_task = new MediaPictureVO().set(task);
				File file = new File(task.getUploadTmpPath());
				Long currentSize = file==null?0l:file.length();
				Long totalSize = task.getSize();
				view_task.setProgress((int)((((float)currentSize)/((float)totalSize))*100));
				view_tasks.add(view_task);
			}
		}
		
		return view_tasks;
	}
	
	/**
	 * 素材分片上传<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月2日 下午3:32:40
	 * @param String uuid 任务uuid
	 * @param String name 文件名称
	 * @param long lastModified 最后修改日期
	 * @param long beginOffset 文件分片的起始位置
	 * @param long  endOffset 文件分片的结束位置
	 * @param long blockSize 文件分片大小
	 * @param long size 文件大小
	 * @param String type 文件的mimetype
	 * @param blob block 文件分片数据
	 * @return MediaPictureVO 图片媒资
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/upload")
	public Object upload(HttpServletRequest nativeRequest) throws Exception{
		
		MultipartHttpServletRequestWrapper request = new MultipartHttpServletRequestWrapper(nativeRequest);
		
		String uuid = request.getString("uuid");
		String name = request.getString("name");
		long blockSize = request.getLongValue("blockSize");
		long lastModified = request.getLongValue("lastModified");
		long size = request.getLongValue("size");
		String type = request.getString("type");
		long beginOffset = request.getLongValue("beginOffset");
		long endOffset = request.getLongValue("endOffset");
		
		//参数错误
		if((beginOffset + blockSize) != endOffset){
			throw new OffsetCannotMatchSizeException(beginOffset, endOffset, blockSize);
		}
		
		MediaPicturePO task = mediaPictureDao.findByUuid(uuid);
		
		if(task == null){
			throw new MediaPictureNotExistException(uuid);
		}
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), task.getFolderId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		//状态错误
		if(!UploadStatus.UPLOADING.equals(task.getUploadStatus())){
			throw new MediaPictureStatusErrorWhenUploadingException(uuid, task.getUploadStatus());
		}
		
		//文件不是一个
		if(!name.equals(task.getFileName()) 
				|| lastModified!=task.getLastModified() 
				|| size!=task.getSize() 
				|| !type.equals(task.getMimetype())){
			throw new MediaPictureCannotMatchException(uuid, name, lastModified, size, type, task.getFileName(), 
											   task.getLastModified(), task.getSize(), task.getMimetype());
		}
		
		//文件起始位置错误
		File file = new File(task.getUploadTmpPath());
		if((!file.exists() && beginOffset!=0l) 
				|| (file.length() != beginOffset)){
			throw new MediaPictureErrorBeginOffsetException(uuid, beginOffset, file.length());
		}
		
		//分块
		InputStream block = null;
		FileOutputStream out = null;
		try{
			if(!file.exists()) file.createNewFile();
			block = request.getInputStream("block");
			byte[] blockBytes = ByteUtil.inputStreamToBytes(block);
			out = new FileOutputStream(file, true);
			out.write(blockBytes);
		}finally{
			if(block != null) block.close();
			if(out != null) out.close();
		}
		
		if(endOffset == size){
			//上传完成
			task.setUploadStatus(UploadStatus.COMPLETE);
			if(task.getReviewStatus() != null){
				//开启审核流程
				Long companyId = Long.valueOf(user.getGroupId());
				MediaSettingsPO mediaSettings = mediaSettingsDao.findByCompanyIdAndType(companyId, MediaSettingsType.PROCESS_UPLOAD_PICTURE);
				Long processId = Long.valueOf(mediaSettings.getSettings().split("@@")[0]);
				ProcessVO process = processQuery.findById(processId);
				JSONObject variables = new JSONObject();
				variables.put("name", task.getName());
				variables.put("tags", task.getTags());
				variables.put("keyWords", task.getKeyWords());
				variables.put("media", serverPropsQuery.generateHttpPreviewUrl(task.getPreviewUrl()));
				variables.put("remark", task.getRemarks());
				variables.put("uploadPath", folderQuery.generateFolderBreadCrumb(task.getFolderId()));
				variables.put("_pa6_id", task.getId());
				String category = new StringBufferWrapper().append("上传图片：").append(task.getName()).toString();
				String business = new StringBufferWrapper().append("mediaPicture:").append(task.getId()).toString();
				String processInstanceId = processService.startByKey(process.getProcessId(), variables.toJSONString(), category, business);
				task.setProcessInstanceId(processInstanceId);
			}
			mediaPictureDao.save(task);
		}
		
        return new MediaPictureVO().set(task);
	}
	
	/**
	 * 上传出错<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月2日 下午5:59:53
	 * @param String uuid 任务uuid
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/upload/error/{uuid}")
	public Object uploadError(
			@PathVariable String uuid, 
			HttpServletRequest request) throws Exception{
		
		MediaPicturePO task = mediaPictureDao.findByUuid(uuid);
		
		if(task == null){
			throw new MediaPictureNotExistException(uuid);
		}
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), task.getFolderId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		//状态错误
		if(!UploadStatus.UPLOADING.equals(task.getUploadStatus())){
			throw new MediaPictureStatusErrorWhenUploadErrorException(uuid, task.getUploadStatus());
		}
		
		task.setUploadStatus(UploadStatus.ERROR);
		
		mediaPictureDao.save(task);
		
		return null;
	}
	
	/**
	 * 关闭上传任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月3日 上午11:52:06
	 * @param String uuid 任务uuid
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/upload/cancel/{uuid}")
	public Object uploadCancel(
			@PathVariable String uuid,
			HttpServletRequest request) throws Exception{
		
		MediaPicturePO task = mediaPictureDao.findByUuid(uuid);
		
		if(task == null){
			throw new MediaPictureNotExistException(uuid);
		}
		
		if(UploadStatus.COMPLETE.equals(task.getUploadStatus())){
			throw new MediaPictureStatusErrorWhenUploadCancelException(uuid, task.getUploadStatus());
		}
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), task.getFolderId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		mediaPictureService.uploadCancel(task);
		
		return null;
	}
	
	/**
	 * 查询文件的上传情况<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月3日 下午5:05:32
	 * @param String uuid 任务uuid
	 * @return MaterialFileUploadInfoVO 文件上传信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/upload/info/{uuid}")
	public Object queryUploadInfo(
			@PathVariable String uuid,
			HttpServletRequest request) throws Exception{
		
		MediaPicturePO task = mediaPictureDao.findByUuid(uuid);
		
		if(task == null){
			throw new MediaPictureNotExistException(uuid);
		}
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), task.getFolderId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		return new MediaPictureUploadInfoVO().set(task);
	}
	
	/**
	 * 删除图片媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 上午9:07:53
	 * @param @PathVariable Long id 媒资id
	 * @return deleted List<MediaPictureVO> 删除列表
	 * @return processed List<MediaPictureVO> 待审核列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/{id}")
	public Object remove(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		MediaPicturePO media = mediaPictureDao.findById(id);
		
		if(media == null){
			throw new MediaPictureNotExistException(id);
		}
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), media.getFolderId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		return mediaPictureService.remove(new ArrayListWrapper<MediaPicturePO>().add(media).getList());
	}
	
	/**
	 * 移动图片媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 上午11:33:56
	 * @param Long mediaId 图片媒资id
	 * @param Long targetId 目标文件夹id
	 * @return boolean 是否移动
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/move")
	public Object move(
			Long mediaId,
			Long targetId,
			HttpServletRequest request) throws Exception{
	
		MediaPicturePO media = mediaPictureDao.findById(mediaId);
		
		if(media == null){
			throw new MediaPictureNotExistException(mediaId);
		}
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), media.getFolderId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		FolderPO target = folderDao.findById(targetId);
		if(target == null){
			throw new FolderNotExistException(targetId);
		}
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), target.getId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		if(target.getId().equals(media.getFolderId())) return false;
		
		media.setFolderId(target.getId());
		mediaPictureDao.save(media);
		
		return true;
	}
	
	/**
	 * 复制图片媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 下午2:36:40
	 * @param Long mediaId 待复制图片媒资id
	 * @param Long targetId 目标文件夹id
	 * @return boolean moved 标识文件是否复制到其他文件夹中
	 * @return MeidaPictureVO copied 复制后的图片媒资
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/copy")
	public Object copy(
			Long mediaId,
			Long targetId,
			HttpServletRequest request) throws Exception{
		
		MediaPicturePO media = mediaPictureDao.findById(mediaId);
		
		if(media == null){
			throw new MediaPictureNotExistException(mediaId);
		}
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), media.getFolderId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		FolderPO target = folderDao.findById(targetId);
		if(target == null){
			throw new FolderNotExistException(targetId);
		}
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), target.getId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		boolean moved = true;
		
		//判断是否被复制到其他文件夹中
		if(target.getId().equals(media.getFolderId())) moved = false;
		
		MediaPicturePO copiedMedia  = mediaPictureService.copy(media, target);
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("moved", moved)
																		 .put("copied", new MediaPictureVO().set(copiedMedia))
																		 .getMap();
		return result;
	}
	
	/**
	 * 获取素材文件预览地址<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 下午2:44:22
	 * @param @PathVariable Long id 素材文件id
	 * @return String name 文件名称
	 * @return String uri 预览uri
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/preview/uri/{id}")
	public Object previewUri(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		MediaPicturePO media = mediaPictureDao.findById(id);
		
		if(media == null){
			throw new MediaPictureNotExistException(id);
		}
		
		UserVO user = userQuery.current();
		
//		operationRecordService.addRecord(user.getId(), OperationRecordVO.setFromPicture(new MediaPictureVO().set(media)), 1l);
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), media.getFolderId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		Map<String, String> result = new HashMapWrapper<String, String>().put("name", media.getFileName())
																		 .put("uri", media.getPreviewUrl())
																		 .getMap();
		
		return result;
	}
	
}
