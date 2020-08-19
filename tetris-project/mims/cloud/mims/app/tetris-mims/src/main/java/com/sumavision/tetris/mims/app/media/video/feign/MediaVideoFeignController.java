package com.sumavision.tetris.mims.app.media.video.feign;

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
import com.sumavision.tetris.commons.util.binary.ByteUtil;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderQuery;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.folder.exception.FolderNotExistException;
import com.sumavision.tetris.mims.app.folder.exception.UserHasNoPermissionForFolderException;
import com.sumavision.tetris.mims.app.material.exception.OffsetCannotMatchSizeException;
import com.sumavision.tetris.mims.app.media.UploadStatus;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureItemType;
import com.sumavision.tetris.mims.app.media.tag.TagQuery;
import com.sumavision.tetris.mims.app.media.tag.TagVO;
import com.sumavision.tetris.mims.app.media.upload.MediaFileEquipmentPermissionBO;
import com.sumavision.tetris.mims.app.media.upload.MediaFileEquipmentPermissionQuery;
import com.sumavision.tetris.mims.app.media.video.MediaVideoDAO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoItemType;
import com.sumavision.tetris.mims.app.media.video.MediaVideoPO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoQuery;
import com.sumavision.tetris.mims.app.media.video.MediaVideoService;
import com.sumavision.tetris.mims.app.media.video.MediaVideoTaskVO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoVO;
import com.sumavision.tetris.mims.app.media.video.exception.MediaVideoCannotMatchException;
import com.sumavision.tetris.mims.app.media.video.exception.MediaVideoErrorBeginOffsetException;
import com.sumavision.tetris.mims.app.media.video.exception.MediaVideoNotExistException;
import com.sumavision.tetris.mims.app.media.video.exception.MediaVideoStatusErrorWhenUploadingException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.MultipartHttpServletRequestWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

@Controller
@RequestMapping(value = "/media/video/feign")
public class MediaVideoFeignController {

	@Autowired
	private MediaVideoQuery mediaVideoQuery;
	
	@Autowired
	private MediaVideoService mediaVideoService;
	
	@Autowired
	private MediaVideoDAO mediaVideoDAO;
	
	@Autowired
	private TagQuery tagQuery;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private FolderQuery folderQuery;
	
	@Autowired
	private MediaFileEquipmentPermissionQuery mediaFileEquipmentPermissionQuery;
	
	@Autowired
	private UserQuery userQuery;
	
	/**
	 * 加载文件夹下的视频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @param folderId 文件夹id
	 * @return rows List<MediaVideoVO> 视频媒资列表
	 * @return breadCrumb FolderBreadCrumbVO 面包屑数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load")
	public Object load(
			Long folderId,
			HttpServletRequest request) throws Exception{
		
		return mediaVideoQuery.load(folderId);
	}
	
	/**
	 * 加载所有视频媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月6日 下午4:03:27
	 * @return List<MediaVideoVO> 视频媒资列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/all")
	public Object loadAll(
			HttpServletRequest request) throws Exception{
		
		return mediaVideoQuery.loadAll();
	}
	
	/**
	 * 根据目录id获取目录及文件(一级)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年4月29日 下午4:09:41
	 * @param folderId 目录id
	 * @return MediaVideoVO
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/collection")
	public Object loadCollection(Long folderId, HttpServletRequest request) throws Exception{
		MediaVideoVO folder = mediaVideoQuery.loadCollection(folderId);
		folder.setDeviceUpload(mediaFileEquipmentPermissionQuery.queryList(new MediaFileEquipmentPermissionBO().setMediaId(folder.getId()).setMediaType("folder")));
		if (folder != null) {
			List<MediaVideoVO> children = folder.getChildren();
			if (children != null) {
				for (MediaVideoVO media : children) {
					if(media.getType().equals(MediaVideoItemType.VIDEO.toString())){
						media.setDeviceUpload(mediaFileEquipmentPermissionQuery.queryList(new MediaFileEquipmentPermissionBO().setFromVideoVO(media)));
					}else if(media.getType().equals(MediaVideoItemType.FOLDER.toString())){
						media.setDeviceUpload(mediaFileEquipmentPermissionQuery.queryList(new MediaFileEquipmentPermissionBO().setMediaId(media.getId()).setMediaType("folder")));
					}
				}
			}
		}
		return folder;
	}
	
	/**
	 * 根据uuid获取媒资信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月6日 下午4:03:27
	 * @return List<MediaVideoVO> 视频媒资列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/quest/by/uuid")
	public Object getByUuids(String uuids) throws Exception{
		List<String> uuidList = JSON.parseArray(uuids, String.class);
		
		return MediaVideoVO.getConverter(MediaVideoVO.class).convert(mediaVideoQuery.questByUuid(uuidList), MediaVideoVO.class);
	}
	
	/**
	 * 根据id获取媒资信息<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年10月9日 下午2:32:55
	 * @param id 媒资id
	 * @return MediaoVideoPO 媒资信息
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/quest/by/id")
	public Object getById(Long id) throws Exception{
		MediaVideoPO audio = mediaVideoDAO.findOne(id);
		if (audio == null) throw new MediaVideoNotExistException(id);
		
		return new MediaVideoVO().set(audio);
	}
	
	/**
	 * 生成文件存储预览路径(云转码使用)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月7日 下午4:03:27
	 * @return String 预览路径
	 * @throws Exception 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/build/url")
	public Object buildUrl(String name, String folderUuid, HttpServletRequest request) throws Exception {
		return mediaVideoQuery.buildUrl(name, folderUuid);
	};
	
	/**
	 * 根据预览地址查询视频列表<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年11月5日 上午10:49:17
	 * @param JSONString previewUrls 预览地址列表
	 * @return List<MediaVideoVO> 视频列表
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/find/by/preview/url/in")
	public Object findByPreviewUrlIn(
			String previewUrls, 
			HttpServletRequest request) throws Exception{
		return mediaVideoQuery.findByPreviewUrlIn(JSON.parseArray(previewUrls, String.class));
	}
 	
	/**
	 * 添加远程媒资(收录系统使用)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年9月3日 下午5:24:19
	 * @param String name 媒资名称
	 * @param Long tagId 标签id 
	 * @param String httpUrl 媒资预览地址
	 * @param String ftpUrl 媒资ftp地址
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/add/remote")
	public Object addRemote(String name, Long tagId, String httpUrl, String ftpUrl, HttpServletRequest request) throws Exception {
		UserVO user = userQuery.current();
		
		TagVO tag = tagQuery.queryById(tagId);
		
		return mediaVideoService.addTask(user, name, tag == null ? "" : tag.getName(), httpUrl, ftpUrl == null ? "" : ftpUrl);
	}
	
	/**
	 * 根据id数组删除媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年12月4日 上午9:12:35
	 * @param List<Long> ids 预删除媒资id数组
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove")
	public void remove(String ids, HttpServletRequest request) throws Exception {
		if (ids == null || ids.isEmpty()) return;
		
		List<Long> idList = JSONArray.parseArray(ids, Long.class);
		
		mediaVideoService.remove(idList);
	}
	
	/**
	 * 添加上传视频媒资任务<br/>
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
            String thumbnail,
            String addition,
			HttpServletRequest request) throws Exception{
		
		MediaVideoTaskVO taskParam = JSON.parseObject(task, MediaVideoTaskVO.class);
		
		UserVO user = userQuery.current();
		
		FolderPO folder = null;
		
		if(folderId == null){
			List<FolderPO> folders = folderQuery.findPermissionCompanyTree(FolderType.COMPANY_VIDEO.toString());
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
		if (tags != null && !tags.isEmpty()) {
			tagList = Arrays.asList(tags.split(","));
		}
		
		MediaVideoPO entity = mediaVideoService.addTask(user, name, tagList, null, remark, taskParam, folder);
		if (thumbnail != null) entity.setThumbnail(thumbnail);
		if(addition != null) entity.setAddition(addition);
		mediaVideoDAO.save(entity);
		
		return new MediaVideoVO().set(entity);
		
	}
	
	/**
	 * 视频媒资上传<br/>
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
	 * @return MediaVideoVO 视频媒资
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
		
		MultipartHttpServletRequestWrapper multipartRequest = new MultipartHttpServletRequestWrapper(request, size.longValue());
		FileItem fileItem = multipartRequest.getFileItem("file");
		
		//参数错误
		if((beginOffset + blockSize) != endOffset){
			throw new OffsetCannotMatchSizeException(beginOffset, endOffset, blockSize);
		}
		
		MediaVideoPO task = mediaVideoDAO.findByUuid(uuid);
		
		if(task == null){
			throw new MediaVideoNotExistException(uuid);
		}
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), task.getFolderId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		//状态错误
		if(!UploadStatus.UPLOADING.equals(task.getUploadStatus())){
			throw new MediaVideoStatusErrorWhenUploadingException(uuid, task.getUploadStatus());
		}
		
		//文件不是一个
		if(!name.equals(task.getFileName()) 
				|| !lastModified.equals(task.getLastModified()) 
				|| !type.equals(task.getMimetype())){
			throw new MediaVideoCannotMatchException(uuid, name, lastModified, size, type, task.getFileName(), 
											   task.getLastModified(), task.getSize(), task.getMimetype());
		}
		
		//文件起始位置错误
		File file = new File(task.getUploadTmpPath());
		if((!file.exists() && beginOffset!=0l) 
				|| (file.length() != beginOffset)){
			throw new MediaVideoErrorBeginOffsetException(uuid, beginOffset, file.length());
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
			out.flush();
		}finally{
			if(block != null) block.close();
			if(out != null) out.close();
		}
		
		if(endOffset.equals(size)){
			//上传完成
			task.setUploadStatus(UploadStatus.COMPLETE);
			
			MultimediaInfo multimediaInfo = new Encoder().getInfo(file);
			task.setDuration(multimediaInfo.getDuration());
			
			if(task.getReviewStatus() != null){
				//开启审核流程--这里会保存媒资
				mediaVideoService.startUploadProcess(task);
			}else{
				mediaVideoDAO.save(task);
			}
		}
		
        return new MediaVideoVO().set(task);
	}
}
