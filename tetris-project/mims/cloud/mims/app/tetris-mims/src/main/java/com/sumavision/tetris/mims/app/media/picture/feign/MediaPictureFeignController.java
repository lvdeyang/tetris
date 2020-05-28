package com.sumavision.tetris.mims.app.media.picture.feign;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.binary.ByteUtil;
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
import com.sumavision.tetris.mims.app.media.picture.MediaPictureDAO;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureItemType;
import com.sumavision.tetris.mims.app.media.picture.MediaPicturePO;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureQuery;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureService;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureTaskVO;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureVO;
import com.sumavision.tetris.mims.app.media.picture.exception.MediaPictureCannotMatchException;
import com.sumavision.tetris.mims.app.media.picture.exception.MediaPictureErrorBeginOffsetException;
import com.sumavision.tetris.mims.app.media.picture.exception.MediaPictureNotExistException;
import com.sumavision.tetris.mims.app.media.picture.exception.MediaPictureStatusErrorWhenUploadingException;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsDAO;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsPO;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsType;
import com.sumavision.tetris.mims.app.media.upload.MediaFileEquipmentPermissionBO;
import com.sumavision.tetris.mims.app.media.upload.MediaFileEquipmentPermissionQuery;
import com.sumavision.tetris.mims.config.server.MimsServerPropsQuery;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.MultipartHttpServletRequestWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

@Controller
@RequestMapping(value = "/media/picture/feign")
public class MediaPictureFeignController {
	@Autowired
	private MediaPictureDAO mediaPictureDao;

	@Autowired
	private MediaPictureQuery mediaPictureQuery;
	
	@Autowired
	private MediaPictureService mediaPictureService;
	
	@Autowired
	private FolderQuery folderQuery;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private MediaSettingsDAO mediaSettingsDao;
	
	@Autowired
	private ProcessQuery processQuery;
	
	@Autowired
	private ProcessService processService;
	
	@Autowired
	private MimsServerPropsQuery serverPropsQuery; 
	
	@Autowired
	private MediaFileEquipmentPermissionQuery mediaFileEquipmentPermissionQuery;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 查询媒资库图片feign接口<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年2月26日 下午5:30:44
	 * @param Long folderId 当前文件夹id
	 * @return rows List<MediaPictureVO> 媒资项目列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long folderId, 
			HttpServletRequest request) throws Exception{
		
		
		return mediaPictureQuery.load(folderId);
	}
	
	/**
	 * 加载所有的图片媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月6日 下午4:03:27
	 * @return List<MediaPictureVO> 图片媒资列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/all")
	public Object loadAll(HttpServletRequest request) throws Exception{
		return mediaPictureQuery.loadAll();
	}
	
	/**
	 * 根据目录id获取目录及文件(一级)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月29日 下午4:09:41
	 * @param folderId 目录id
	 * @return MediaPictureVO
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/collection")
	public Object loadCollection(Long folderId, HttpServletRequest request) throws Exception{
		MediaPictureVO folder = mediaPictureQuery.loadPictureCollection(folderId);
		folder.setDeviceUpload(mediaFileEquipmentPermissionQuery.queryList(new MediaFileEquipmentPermissionBO().setMediaId(folder.getId()).setMediaType("folder")));
		if (folder != null) {
			List<MediaPictureVO> children = folder.getChildren();
			if (children != null) {
				for (MediaPictureVO media : children) {
					if(media.getType().equals(MediaPictureItemType.PICTURE.toString())){
						media.setDeviceUpload(mediaFileEquipmentPermissionQuery.queryList(new MediaFileEquipmentPermissionBO().setFromPictureVO(media)));
					}else if(media.getType().equals(MediaPictureItemType.FOLDER.toString())){
						media.setDeviceUpload(mediaFileEquipmentPermissionQuery.queryList(new MediaFileEquipmentPermissionBO().setMediaId(media.getId()).setMediaType("folder")));
					}
				}
			}
		}
		return folder;
	}
	
	/**
	 * 根据预览地址查询图片列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午10:34:58
	 * @param JSONString previewUrls 预览地址列表
	 * @return List<MediaPictureVO> 图片列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/preview/url/in")
	public Object findByPreviewUrlIn(
			String previewUrls,
			HttpServletRequest request) throws Exception{
		return mediaPictureQuery.findByPreviewUrlIn(JSON.parseArray(previewUrls, String.class));
	}
	
	/**
	 * 删除图片媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月29日 上午9:07:53
	 * @param Long id 媒资id
	 * @return deleted List<MediaPictureVO> 删除列表
	 * @return processed List<MediaPictureVO> 待审核列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public Object remove(
			String ids,
			HttpServletRequest request) throws Exception{
		
		UserVO user = userQuery.current();
		
		if (ids == null || ids.isEmpty()) return null;
		
		List<Long> idList = JSONArray.parseArray(ids, Long.class);
		List<MediaPicturePO> picturePOs = new ArrayList<MediaPicturePO>();
		for (Long id : idList) {
			MediaPicturePO media = mediaPictureDao.findOne(id);
			if(!folderQuery.hasGroupPermission(user.getGroupId(), media.getFolderId())){
				throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
			}
			picturePOs.add(media);
		}
		
		return mediaPictureService.remove(picturePOs);
	}
	
	/**
	 * 添加上传图片媒资任务<br/>
	 * <b>作者:</b>lzp<br/>
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
		
		FolderPO folder = null;
		
		if(folderId == null){
			List<FolderPO> folders = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_PICTURE.toString());
			List<FolderPO> roots = folderQuery.findRoots(folders);
			if(roots != null && roots.size() > 0){
				folder = roots.get(0);
			}
		}else{
			if(!folderQuery.hasGroupPermission(user.getGroupId(), folderId)){
				throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
			}
			
			folder = folderDao.findOne(folderId);
		}

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
	public Object upload(
			String uuid,
			String name,
			Long blockSize,
			Long lastModified,
			Long size,
			String type,
			Long beginOffset,
			Long endOffset,
			HttpServletRequest request) throws Exception{
		
		MultipartHttpServletRequestWrapper multipartRequest = new MultipartHttpServletRequestWrapper(request);
		FileItem fileItem = multipartRequest.getFileItem("file");
		
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
				|| !lastModified.equals(task.getLastModified()) 
				|| !size.equals(task.getSize()) 
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
			block = fileItem.getInputStream();
			byte[] blockBytes = ByteUtil.inputStreamToBytes(block);
			out = new FileOutputStream(file, true);
			out.write(blockBytes);
		}finally{
			if(block != null) block.close();
			if(out != null) out.close();
		}
		
		if(endOffset.equals(size)){
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
}
