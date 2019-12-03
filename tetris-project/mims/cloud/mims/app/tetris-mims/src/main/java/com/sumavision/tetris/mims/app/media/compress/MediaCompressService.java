package com.sumavision.tetris.mims.app.media.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.file.CopyFileUtil;
import com.sumavision.tetris.commons.util.file.DeleteFileAndDir;
import com.sumavision.tetris.commons.util.file.GetFileSizeUtil;
import com.sumavision.tetris.commons.util.json.CreateFileUtil;
import com.sumavision.tetris.commons.util.tar.TarUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.commons.util.xml.XMLReader;
import com.sumavision.tetris.commons.util.xml.XmlUtil;
import com.sumavision.tetris.easy.process.core.ProcessQuery;
import com.sumavision.tetris.easy.process.core.ProcessService;
import com.sumavision.tetris.easy.process.core.ProcessVO;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderQuery;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.media.ReviewStatus;
import com.sumavision.tetris.mims.app.media.StoreType;
import com.sumavision.tetris.mims.app.media.UploadStatus;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioDAO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioPO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioService;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioVO;
import com.sumavision.tetris.mims.app.media.compress.api.server.SecureServiceXmlUtils;
import com.sumavision.tetris.mims.app.media.compress.api.server.XmlVO.SignatureVO;
import com.sumavision.tetris.mims.app.media.picture.MediaPicturePO;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureService;
import com.sumavision.tetris.mims.app.media.picture.MediaPictureVO;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsDAO;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsPO;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsQuery;
import com.sumavision.tetris.mims.app.media.settings.MediaSettingsType;
import com.sumavision.tetris.mims.app.media.txt.MediaTxtService;
import com.sumavision.tetris.mims.app.media.video.MediaVideoDAO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoPO;
import com.sumavision.tetris.mims.app.media.video.MediaVideoService;
import com.sumavision.tetris.mims.app.media.video.MediaVideoVO;
import com.sumavision.tetris.mims.app.storage.PreRemoveFileDAO;
import com.sumavision.tetris.mims.app.storage.PreRemoveFilePO;
import com.sumavision.tetris.mims.app.storage.StoreQuery;
import com.sumavision.tetris.mvc.listener.ServletContextListener.Path;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

/**
 * 播发媒资操作（主增删改）<br/>
 * <b>作者:</b>ldy<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年3月31日 下午5:38:33
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MediaCompressService {

	@Autowired
	private StoreQuery storeTool;

	@Autowired
	private PreRemoveFileDAO preRemoveFileDao;

	@Autowired
	private MediaCompressDAO mediaCompressDao;

	@Autowired
	private MediaAudioService mediaAudioService;

	@Autowired
	private MediaVideoService mediaVideoService;

	@Autowired
	private MediaPictureService mediaPictureService;

	@Autowired
	private MediaTxtService mediaTxtService;

	@Autowired
	private Path path;

	@Autowired
	private UserQuery userQuery;

	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private MediaVideoDAO mediaVideoDAO;
	
	@Autowired
	private MediaAudioDAO mediaAudioDAO;
	
	@Autowired
	private MediaSettingsQuery mediaSettingsQuery;

	@Autowired
	private MediaSettingsDAO mediaSettingsDao;
	
	@Autowired
	private ProcessQuery processQuery;
	
	@Autowired
	private ProcessService processService;
	
	@Autowired
	private FolderQuery folderQuery;
	
	/**
	 * 播发媒资上传审核通过<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 下午2:16:01
	 * @param Long id 媒资id
	 */
	public void uploadReviewPassed(Long id) throws Exception{
		MediaCompressPO media = mediaCompressDao.findOne(id);
		media.setReviewStatus(null);
		mediaCompressDao.save(media);
	}
	
	/**
	 * 播发媒资上传审核拒绝<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年7月18日 下午2:16:01
	 * @param Long id 媒资id
	 */
	public void uploadReviewRefuse(Long id) throws Exception{
		MediaCompressPO media = mediaCompressDao.findOne(id);
		media.setReviewStatus(ReviewStatus.REVIEW_UPLOAD_REFUSE);
		mediaCompressDao.save(media);
	}
	
	/**
	 * 播发媒资修改审核通过<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:01:51
	 * @param Long id 播发媒资id
	 * @param String name 媒资名称
	 * @param String tags 贴标签，以“,”分隔
	 * @param String keyWords 关键字，以“,”分隔
	 * @param String remarks 备注
	 */
	public void editReviewPassed(
			Long id,
			String name,
			String tags,
			String keyWords,
			String remarks) throws Exception{
		MediaCompressPO media = mediaCompressDao.findOne(id);
		media.setName(name);
		media.setTags(tags);
		media.setKeyWords(keyWords);
		media.setRemarks(remarks);
		media.setReviewStatus(null);
		mediaCompressDao.save(media);
	}
	
	/**
	 * 播发媒资修改审核拒绝<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:19:16
	 * @param Long id 播发媒资id
	 */
	public void editReviewRefuse(Long id) throws Exception{
		MediaCompressPO media = mediaCompressDao.findOne(id);
		media.setReviewStatus(ReviewStatus.REVIEW_EDIT_REFUSE);
		mediaCompressDao.save(media);
	}
	
	/**
	 * 播发媒资删除审核通过<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:01:51
	 * @param Long id 播发媒资id
	 */
	public void deleteReviewPassed(Long id) throws Exception{
		MediaCompressPO media = mediaCompressDao.findOne(id);
		if(media != null){
			List<MediaCompressPO> videosCanBeDeleted = new ArrayListWrapper<MediaCompressPO>().add(media).getList();
			
			//生成待删除存储文件数据
			List<PreRemoveFilePO> preRemoveFiles = storeTool.preRemoveMediaCompresses(videosCanBeDeleted);
			
			//删除素材文件元数据
			mediaCompressDao.deleteInBatch(videosCanBeDeleted);
			
			//保存待删除存储文件数据
			preRemoveFileDao.save(preRemoveFiles);
			
			//调用flush使sql生效
			preRemoveFileDao.flush();
			
			//将待删除存储文件数据押入存储文件删除队列
			storeTool.pushPreRemoveFileToQueue(preRemoveFiles);
			
			Set<Long> videoIds = new HashSet<Long>();
			for(MediaCompressPO video:videosCanBeDeleted){
				videoIds.add(video.getId());
			}
			
			//删除临时文件
			for(MediaCompressPO video:videosCanBeDeleted){
				List<MediaCompressPO> results = mediaCompressDao.findByUploadTmpPathAndIdNotIn(video.getUploadTmpPath(), videoIds);
				if(results==null || results.size()<=0){
					File file = new File(new File(video.getUploadTmpPath()).getParent());
					File[] children = file.listFiles();
					if(children != null){
						for(File sub:children){
							if(sub.exists()) sub.delete();
						}
					}
					if(file.exists()) file.delete();
				}
			}
		}
	}
	
	/**
	 * 播发媒资删除审核拒绝<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月1日 上午9:22:22
	 * @param Long id 播发媒资id
	 */
	public void deleteReviewRefuse(Long id) throws Exception{
		MediaCompressPO media = mediaCompressDao.findOne(id);
		media.setReviewStatus(ReviewStatus.REVIEW_DELETE_REFUSE);
		mediaCompressDao.save(media);
	}
	
	/**
	 * 图片媒资删除<br/>
	 * <p>
	 * 初步设想，考虑到文件夹下可能包含大文件以及文件数量等<br/>
	 * 情况，这里采用线程删除文件，主要步骤如下：<br/>
	 * 1.生成待删除存储文件数据<br/>
	 * 2.删除素材文件元数据<br/>
	 * 3.保存待删除存储文件数据<br/>
	 * 4.调用flush使sql生效<br/>
	 * 5.将待删除存储文件数据押入存储文件删除队列<br/>
	 * </p>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月23日 下午3:43:03
	 * @param Collection<MaterialFilePO> compresses 播发媒资列表
	 */
	public Map<String, Object> remove(Collection<MediaCompressPO> compresses) throws Exception {

		UserVO user = userQuery.current();
		boolean needProcess = mediaSettingsQuery.needProcess(MediaSettingsType.PROCESS_DELETE_COMPRESS);
		List<MediaCompressPO> compressCanBeDeleted = new ArrayList<MediaCompressPO>();
		List<MediaCompressPO> compressNeedProcess = new ArrayList<MediaCompressPO>();
		
		if(needProcess){
			for(MediaCompressPO compress:compresses){
				if(compress.getAuthorId().equals(user.getId().toString()) && 
						ReviewStatus.REVIEW_UPLOAD_REFUSE.equals(compress.getReviewStatus())){
					compressCanBeDeleted.add(compress);
				}else{
					compressNeedProcess.add(compress);
				}
			}
			if(compressNeedProcess.size() > 0){
				//开启审核流程
				Long companyId = Long.valueOf(user.getGroupId());
				MediaSettingsPO mediaSettings = mediaSettingsDao.findByCompanyIdAndType(companyId, MediaSettingsType.PROCESS_DELETE_COMPRESS);
				Long processId = Long.valueOf(mediaSettings.getSettings().split("@@")[0]);
				ProcessVO process = processQuery.findById(processId);
				for(MediaCompressPO compress:compressNeedProcess){
					JSONObject variables = new JSONObject();
					
					//展示修改后参数
					variables.put("name", compress.getName());
					variables.put("tags", compress.getTags());
					variables.put("keyWords", compress.getKeyWords());
					variables.put("remark", compress.getRemarks());
					variables.put("uploadPath", folderQuery.generateFolderBreadCrumb(compress.getFolderId()));
					
					//接口参数
					variables.put("_pa46_id", compress.getId());
					
					String category = new StringBufferWrapper().append("删除播发媒资：").append(compress.getName()).toString();
					String business = new StringBufferWrapper().append("mediaCompress:").append(compress.getId()).toString();
					String processInstanceId = processService.startByKey(process.getProcessId(), variables.toJSONString(), category, business);
					compress.setProcessInstanceId(processInstanceId);
					compress.setReviewStatus(ReviewStatus.REVIEW_DELETE_WAITING);
				}
				mediaCompressDao.save(compressNeedProcess);
			}
		}else{
			compressCanBeDeleted.addAll(compresses);
		}
		
		if(compressCanBeDeleted.size() > 0){
			//生成待删除存储文件数据
			List<PreRemoveFilePO> preRemoveFiles = storeTool.preRemoveMediaCompresses(compressCanBeDeleted);
			
			//删除素材文件元数据
			mediaCompressDao.deleteInBatch(compressCanBeDeleted);
			
			//保存待删除存储文件数据
			preRemoveFileDao.save(preRemoveFiles);
			
			//调用flush使sql生效
			preRemoveFileDao.flush();
			
			//将待删除存储文件数据押入存储文件删除队列
			storeTool.pushPreRemoveFileToQueue(preRemoveFiles);
			
			Set<Long> compressIds = new HashSet<Long>();
			for(MediaCompressPO compress:compressCanBeDeleted){
				compressIds.add(compress.getId());
			}
			
			//删除临时文件
			for(MediaCompressPO compress:compressCanBeDeleted){
				List<MediaCompressPO> results = mediaCompressDao.findByUploadTmpPathAndIdNotIn(compress.getUploadTmpPath(), compressIds);
				if(results==null || results.size()<=0){
					File file = new File(new File(compress.getUploadTmpPath()).getParent());
					File[] children = file.listFiles();
					if(children != null){
						for(File sub:children){
							if(sub.exists()) sub.delete();
						}
					}
					if(file.exists()) file.delete();
				}
			}
		}
		return new HashMapWrapper<String, Object>().put("deleted", MediaCompressVO.getConverter(MediaCompressVO.class).convert(compressCanBeDeleted, MediaCompressVO.class))
												   .put("processed", MediaCompressVO.getConverter(MediaCompressVO.class).convert(compressNeedProcess, MediaCompressVO.class))
												   .getMap();
	}

	/**
	 * 添加图片媒资上传任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午3:21:49
	 * @param UserVO user 用户
	 * @param String name 媒资名称
	 * @param List<String> tags 标签列表
	 * @param List<String> keyWords 关键字列表
	 * @param String remark 备注
	 * @param MediaVideoTaskVO task 上传任务
	 * @param FolderPO folder 文件夹
	 * @return MediaCompressPO 图片媒资
	 */
	public MediaCompressPO addTask(
			UserVO user, 
			String name, 
			List<String> tags, 
			List<String> keyWords, 
			String remark,
			MediaCompressTaskVO task, 
			FolderPO folder) throws Exception {
		
		boolean needProcess = mediaSettingsQuery.needProcess(MediaSettingsType.PROCESS_UPLOAD_COMPRESS);
		String transTags = (tags == null || tags.isEmpty()) ? "" : StringUtils.join(tags.toArray(), MediaCompressPO.SEPARATOR_TAG);
		String transKeyWords = (keyWords == null || keyWords.isEmpty()) ? "" : StringUtils.join(keyWords.toArray(), MediaCompressPO.SEPARATOR_KEYWORDS);
		
		String separator = File.separator;
		// 临时路径采取/base/companyName/folderuuid/fileNamePrefix/version
		String webappPath = path.webappPath();
		String basePath = new StringBufferWrapper().append(webappPath).append(separator).append("upload")
				.append(separator).append("tmp").append(separator).append(user.getGroupName()).append(separator)
				.append(folder.getUuid()).toString();
		Date date = new Date();
		String version = new StringBufferWrapper().append(MediaCompressPO.VERSION_OF_ORIGIN).append(".")
				.append(date.getTime()).toString();
		String fileNamePrefix = task.getName().split("\\.")[0];
		String folderPath = new StringBufferWrapper().append(basePath).append(separator).append(fileNamePrefix)
				.append(separator).append(version).toString();
		File file = new File(folderPath);
		if (!file.exists())
			file.mkdirs();
		// 这个地方保证每个任务的路径都不一样
		Thread.sleep(1);
		MediaCompressPO entity = new MediaCompressPO();
		entity.setLastModified(task.getLastModified());
		entity.setName(name);
		entity.setTags(transTags);
		entity.setKeyWords(transKeyWords);
		entity.setRemarks(remark);
		entity.setAuthorId(user.getUuid());
		entity.setAuthorName(user.getNickname());
		entity.setVersion(version);
		entity.setFileName(task.getName());
		entity.setSize(task.getSize());
		entity.setMimetype(task.getMimetype());
		entity.setFolderId(folder.getId());
		entity.setUploadStatus(UploadStatus.UPLOADING);
		entity.setStoreType(StoreType.LOCAL);
		entity.setUploadTmpPath(
				new StringBufferWrapper().append(folderPath).append(separator).append(task.getName()).toString());
		entity.setPreviewUrl(new StringBufferWrapper().append("/upload/tmp/").append(user.getGroupName()).append("/")
				.append(folder.getUuid()).append("/").append(fileNamePrefix).append("/").append(version).append("/")
				.append(task.getName()).toString());
		entity.setUpdateTime(date);
		entity.setReviewStatus(needProcess?ReviewStatus.REVIEW_UPLOAD_WAITING:null);
		mediaCompressDao.save(entity);

		return entity;
	}

	/**
	 * 编辑图片媒资信息<br/>
	 * <b>作者:</b>ldy<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年3月25日 下午3:19:38
	 * @param compress 图片媒资
	 * @param name 名称
	 * @param tags 标签列表
	 * @param keyWords 关键字列表
	 * @param remark 备注
	 * @return MediaCompressPO 图片媒资
	 */
	public MediaCompressPO editTask(
			UserVO user, 
			MediaCompressPO compress, 
			String name, 
			List<String> tags,
			List<String> keyWords, 
			String remark) throws Exception {

		boolean needProcess = mediaSettingsQuery.needProcess(MediaSettingsType.PROCESS_EDIT_COMPRESS);
		String transTags = tags==null?"":StringUtils.join(tags.toArray(), MediaPicturePO.SEPARATOR_TAG);
		String transKeyWords = keyWords==null?"":StringUtils.join(keyWords.toArray(), MediaPicturePO.SEPARATOR_KEYWORDS);
		
		if(needProcess){
			//开启审核流程
			Long companyId = Long.valueOf(user.getGroupId());
			MediaSettingsPO mediaSettings = mediaSettingsDao.findByCompanyIdAndType(companyId, MediaSettingsType.PROCESS_EDIT_COMPRESS);
			Long processId = Long.valueOf(mediaSettings.getSettings().split("@@")[0]);
			ProcessVO process = processQuery.findById(processId);
			JSONObject variables = new JSONObject();
			
			//展示修改后参数
			variables.put("name", name);
			variables.put("tags", transTags);
			variables.put("keyWords", transKeyWords);
			variables.put("remark", remark);
			
			//展示修改前参数
			variables.put("oldName", compress.getName());
			variables.put("oldTags", compress.getTags());
			variables.put("oldKeyWords", compress.getKeyWords());
			variables.put("oldRemark", compress.getRemarks());
			variables.put("oldUploadPath", folderQuery.generateFolderBreadCrumb(compress.getFolderId()));
			
			//接口参数
			variables.put("_pa44_id", compress.getId());
			variables.put("_pa44_name", name);
			variables.put("_pa44_tags", transTags);
			variables.put("_pa44_keyWords", transKeyWords);
			variables.put("_pa44_remarks", remark);
			
			String category = new StringBufferWrapper().append("修改播发媒资：").append(compress.getName()).toString();
			String business = new StringBufferWrapper().append("mediaCompress:").append(compress.getId()).toString();
			String processInstanceId = processService.startByKey(process.getProcessId(), variables.toJSONString(), category, business);
			compress.setProcessInstanceId(processInstanceId);
			compress.setReviewStatus(ReviewStatus.REVIEW_EDIT_WAITING);
		}else{
			compress.setName(name);
			compress.setTags(transTags);
			compress.setKeyWords(transKeyWords);
			compress.setRemarks(remark);
		}
		mediaCompressDao.save(compress);
		return compress;
	}

	/**
	 * 素材上传任务关闭<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月3日 上午11:29:23
	 * @param MediaCompressPO task 素材上传任务
	 */
	public void uploadCancel(MediaCompressPO task) throws Exception {
		File file = new File(new File(task.getUploadTmpPath()).getParent());
		File[] children = file.listFiles();
		for (File sub : children) {
			sub.delete();
		}
		file.delete();
		mediaCompressDao.delete(task);
	}

	/**
	 * 复制图片媒资<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 下午1:21:15
	 * @param MediaCompressPO media 待复制的图片媒资
	 * @param FolderPO target 目标文件夹
	 * @return MediaCompressPO 复制后的图片媒资
	 */
	public MediaCompressPO copy(MediaCompressPO media, FolderPO target) throws Exception {

		boolean moved = true;

		if (target.getId().equals(media.getFolderId()))
			moved = false;

		MediaCompressPO copiedMedia = media.copy();
		copiedMedia.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
		copiedMedia.setName(moved ? media.getName()
				: new StringBufferWrapper().append(media.getName()).append("（副本：")
						.append(DateUtil.format(new Date(), DateUtil.dateTimePattern)).append("）").toString());
		copiedMedia.setFolderId(target.getId());

		mediaCompressDao.save(copiedMedia);

		return copiedMedia;
	}

	/**
	 * 解压tar包<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月3日 下午4:19:44
	 * @param String path tar包路径
	 * @return String type 文章类型
	 * @return String name 文章名称
	 * @return String publishTime 文章发布时间
	 * @return String remark 备注
	 * @return String author 作者
	 * @return String keywords 关键字
	 * @return String column 栏目
	 * @return String region 地区
	 * @return String content 媒体内容
	 */
	public Map<String, String> parse(String path) throws Exception {

		UserVO user = userQuery.current();

		File file = new File(path);
		String parentFilePath = new File(file.getParent()).getParent();
		String separator = File.separator;

		// 文章的渲染类型
		String type = "TEXT";
		// 作者
		String author = "";
		// 文章名称
		String articleName = "";
		// 发布时间
		String publishTime = "";
		// 备注
		String remark = "";
		// 关键字
		String keywords = "";
		// 栏目
		String column = "";
		// 地区
		String region = "";
		// tar包内容
		String content = "";
		String templateId = "";

		File parseFile = new File(
				new StringBufferWrapper().append(parentFilePath).append(separator).append("compress").toString());

		if (!parseFile.exists()) {
			parseFile.mkdir();
		}

		// 解压到指定文件夹
		TarUtil.dearchive(file, parseFile);

		File[] fileList = parseFile.listFiles();
		
		Arrays.sort(fileList, new Comparator<File>() {
            public int compare(File f1, File f2) {
                long diff = f1.lastModified() - f2.lastModified();
                if (diff > 0)
                    return 1;
                else if (diff == 0)
                    return 0;
                else
                    return -1;
            }

            public boolean equals(Object obj) {
                return true;
            }

        });
		
		File profile = null;
		File signatureFile = null;
		for (File f : fileList) {
			String fileName = f.getName();
			String name = fileName.split("\\.")[0];
			Long size = f.length();
			String uploadTempPath = f.getPath();

			String fileNameSuffix = f.getName().split("\\.")[1];
			if (fileNameSuffix.equals("mp3")) {

				String folderType = "audio";
				String mimeType = "audio/mp3";

				MediaAudioPO audio = mediaAudioService.add(user, name, fileName, size, folderType, mimeType,
						uploadTempPath, "");

				content = new MediaAudioVO().set(audio).getPreviewUrl();
				templateId = "yjgb_audio";
				type = "AVIDEO";

			} else if (fileNameSuffix.equals("mp4")) {

				String folderType = "video";
				String mimeType = "video/mp4";

				MediaVideoPO video = mediaVideoService.add(user, name, fileName, size, folderType, mimeType,
						uploadTempPath, "");

				content = new MediaVideoVO().set(video).getPreviewUrl();
				templateId = "yjgb_video";
				type = "AVIDEO";

			} else if (fileNameSuffix.equals("jpg")) {

				String folderType = "picture";
				String mimeType = "image/jpeg";

				MediaPicturePO picture = mediaPictureService.add(user, name, fileName, size, folderType, mimeType,
						uploadTempPath);

				content = new MediaPictureVO().set(picture).getPreviewUrl();
				templateId = "yjgb_picture";
				type = "TEXT";

			} else if (fileNameSuffix.equals("png")) {

				String folderType = "picture";
				String mimeType = "image/png";

				MediaPicturePO picture = mediaPictureService.add(user, name, fileName, size, folderType, mimeType,
						uploadTempPath);

				content = new MediaPictureVO().set(picture).getPreviewUrl();
				templateId = "yjgb_picture";
				type = "TEXT";

			} else if (fileNameSuffix.equals("gif")) {

				String folderType = "picture";
				String mimeType = "image/gif";

				MediaPicturePO picture = mediaPictureService.add(user, name, fileName, size, folderType, mimeType,
						uploadTempPath);

				content = new MediaPictureVO().set(picture).getPreviewUrl();
				templateId = "yjgb_picture";
				type = "TEXT";

			} else if (fileNameSuffix.equals("xml")) {
				String[] fileNameSplit = fileName.split("_");
				if (fileNameSplit.length == 2) {
					profile = f;
				}else if (fileNameSplit.length == 3) {
					signatureFile = f;
				}
			}
		}
		
		if (profile != null) {
			String check = "OK";
			String profileName = profile.getName();
			
			if (signatureFile != null) {
				String signatureFileName = signatureFile.getName();
				if (profileName.split("_")[1].equals(signatureFileName.split("_")[2])) {
					String xmlString = FileUtils.readFileToString(signatureFile);
					SignatureVO signature = XmlUtil.XML2Obj(xmlString, SignatureVO.class);
					String signatureValue = signature.getSignatureValue().trim();
					check = SecureServiceXmlUtils.checkSignatureValueJnta(profile, signatureValue, 1);
				}
			}
			
			if (check.equals("OK")) {
				String name = profileName.split("\\.")[0];
				// 解析xml
				InputStream xmlStream = new FileInputStream(profile);
				XMLReader reader = new XMLReader(xmlStream);
				author = reader.readString("EBD.EBM.MsgBasicInfo.SenderName");
				publishTime = reader.readString("EBD.EBM.MsgBasicInfo.SendTime");
				articleName = reader.readString("EBD.EBM.MsgContent.MsgTitle");
				remark = reader.readString("EBD.EBM.MsgContent.MsgDesc");

				// 文本媒资
				if (remark != null) {
					mediaTxtService.add(user, name, "txt", remark);
				}

				// 转换栏目
				String eventType = reader.readString("EBD.EBM.MsgBasicInfo.EventType");
				Map<String, String> parseResult = parseEventType(eventType);
				keywords = parseResult.get("keyWords");
				column = JSON.toJSONString(new ArrayListWrapper<String>().add(parseResult.get("code")).getList());

				// 转换地区
				String areaCode = reader.readString("EBD.EBM.MsgContent.AreaCode");
				region = JSON.toJSONString(new ArrayListWrapper<String>().add(areaCode).getList());
			}
		}

		JSONArray contents = new JSONArray();
		JSONObject titleTemplate = new JSONObject();
		titleTemplate.put("type", "yjgb_title");
		titleTemplate.put("value", articleName);
		contents.add(titleTemplate);

		JSONObject authorTemplate = new JSONObject();
		authorTemplate.put("type", "yjgb_txt");
		authorTemplate.put("value", author);
		contents.add(authorTemplate);

		JSONObject contentTemplate = new JSONObject();
		contentTemplate.put("type", "yjgb_txt");
		contentTemplate.put("value", remark);
		contents.add(contentTemplate);

		if (!templateId.equals("")) {
			JSONObject mediaTemplate = new JSONObject();
			mediaTemplate.put("type", templateId);
			mediaTemplate.put("value", content);
			contents.add(mediaTemplate);
		}

		return new HashMapWrapper<String, String>().put("type", type).put("name", articleName)
				.put("publishTime", publishTime).put("remark", articleName).put("author", author)
				.put("keywords", keywords).put("column", column).put("region", region)
				.put("content", contents.toJSONString()).getMap();
	}

	/**
	 * 解析eventType.json<br/>
	 * <b>作者:</b>sm<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月3日 上午9:49:53
	 * @param String code 分类（栏目）编码
	 * @return String code 所在栏目编码
	 * @return String keyWords 关键字 keyword,keyword,keyword
	 */
	private Map<String, String> parseEventType(String code) throws Exception {

		String separator = ",";

		String needCode = "";
		StringBufferWrapper keyWordsBuffer = new StringBufferWrapper();

		File jsonFile = ResourceUtils.getFile("classpath:eventType.json");
		String json = FileUtils.readFileToString(jsonFile);
		List<JSONObject> firstArray = JSONArray.parseArray(json, JSONObject.class);
		if (firstArray != null && firstArray.size() > 0) {
			for (JSONObject first : firstArray) {
				if (first.getString("code").equals(code)) {
					needCode = first.getString("code");
					keyWordsBuffer.append(first.getString("text"));
					break;
				}
				if (first.getString("children") != null) {
					List<JSONObject> secondArray = JSONArray.parseArray(first.getString("children"), JSONObject.class);
					if (secondArray.size() > 0) {
						for (JSONObject second : secondArray) {
							if (second.getString("code").equals(code)) {
								needCode = second.getString("code");
								keyWordsBuffer.append(first.getString("text")).append(separator)
										.append(second.getString("text"));
								break;
							}
							if (second.getString("children") != null) {
								List<JSONObject> thirdArray = JSONArray.parseArray(second.getString("children"),
										JSONObject.class);
								if (thirdArray.size() > 0) {
									for (JSONObject third : thirdArray) {
										if (third.getString("code").equals(code)) {
											needCode = third.getString("code");
											keyWordsBuffer.append(first.getString("text")).append(separator)
													.append(second.getString("text")).append(separator)
													.append(third.getString("text"));
											break;
										}
										if (third.getString("children") != null) {
											List<JSONObject> forthArray = JSONArray
													.parseArray(third.getString("children"), JSONObject.class);
											if (forthArray.size() > 0) {
												for (JSONObject forth : forthArray) {
													if (forth.getString("code").equals(code)) {
														needCode = third.getString("code");
														keyWordsBuffer.append(first.getString("text")).append(separator)
																.append(second.getString("text")).append(separator)
																.append(third.getString("text")).append(separator)
																.append(forth.getString("text"));
														break;
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return new HashMapWrapper<String, String>().put("code", needCode).put("keyWords", keyWordsBuffer.toString())
				.getMap();
	}

	/**
	 * 压缩播发媒资<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年4月3日 上午9:49:53
	 * @param String jsonString 播发媒资json描述
	 * @param List<Long> mimsIdList 打包媒资Id列表
	 * @return MediaCompressVO 生成的播发媒资
	 */
	public MediaCompressVO packageTar(String jsonString, List<FileCompressVO> mimsUuidList) throws Exception {
		UserVO userVO = userQuery.current();
		FolderPO folderPO = folderDao.findCompanyRootFolderByType(userVO.getGroupId(),
				FolderType.COMPANY_COMPRESS.toString());

		JSONObject jsonObject = JSON.parseObject(jsonString);
		String fileNameString = jsonObject.getString("file");
		String fileVersion = jsonObject.getString("version");

		MediaCompressPO mediaCompressPO = addTask(userVO, fileNameString, null, null, fileVersion,
				new MediaCompressTaskVO().setName(fileNameString), folderPO);
		String jsonPathString = mediaCompressPO.getUploadTmpPath().replace(fileNameString, "");
		
		StringBufferWrapper tarDirPath = new StringBufferWrapper().append(jsonPathString).append(fileNameString.replace(".tar", ""));
		File tarDir = new File(tarDirPath.toString());
		if (!tarDir.exists()) {
			tarDir.mkdirs();
		}
		for(FileCompressVO item:mimsUuidList){
			String[] filePaths = item.getPath().split("/");
			StringBufferWrapper filePathBuffer = new StringBufferWrapper().append(tarDirPath.toString());
			for (int i = 1; i < filePaths.length; i++) {
				filePathBuffer.append("/").append(filePaths[i]);
			}
			File fileDir = new File(filePathBuffer.toString());
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}
			
			String uploadPath = ""; 
			MediaVideoPO mediaVideoPO = mediaVideoDAO.findByUuid(item.getUuid());
			if (mediaVideoPO != null) {
				uploadPath = mediaVideoPO.getUploadTmpPath();
			}else {
				MediaAudioPO mediaAudioPO = mediaAudioDAO.findByUuid(item.getUuid());
				if (mediaAudioPO != null) {
					uploadPath = mediaAudioPO.getUploadTmpPath();
				}
			}
			
			if (!uploadPath.isEmpty()) {
				String[] pathLists = uploadPath.split("/");
				String mediaName = pathLists[pathLists.length-1];
				
				String copyDest = new StringBufferWrapper().append(filePathBuffer.toString()).append("/").append(mediaName).toString();
				
				CopyFileUtil.copyFileUsingFileChannels(new File(uploadPath), new File(copyDest));
			}
		}
		
		// 根据json字符串生成json文件
		String jsonPath = tarDirPath.toString();
		CreateFileUtil.createJsonFile(jsonString, jsonPath, "metadata");

//		List<MediaVideoPO> mediaVideoPOs = new ArrayList<MediaVideoPO>();
//		List<MediaAudioPO> mediaAudioPOs = new ArrayList<MediaAudioPO>();
//		if (mimsUuidList != null && mimsUuidList.size() > 0) {
//			mediaVideoPOs = mediaVideoDAO.findByUuidIn(mimsUuidList);
//			mediaAudioPOs = mediaAudioDAO.findByUuidIn(mimsUuidList);
//		}
//
//		List<File> fileList = new ArrayList<File>();
//		for (MediaVideoPO item : mediaVideoPOs) {
//			fileList.add(new File(item.getUploadTmpPath()));
//		}
//		for (MediaAudioPO item : mediaAudioPOs) {
//			fileList.add(new File(item.getUploadTmpPath()));
//		}
//		File jsonFile = new File(tarDirPath.append("\\").append("metadata.json").toString());
//		fileList.add(jsonFile);
//		TarUtil.archive(fileList, mediaCompressPO.getUploadTmpPath());
		TarUtil.archive(tarDir);
		// 获取当前压缩包大小
		mediaCompressPO.setSize(GetFileSizeUtil.getFileSize(new File(mediaCompressPO.getUploadTmpPath())));
		mediaCompressPO.setUploadStatus(UploadStatus.COMPLETE);
		mediaCompressDao.save(mediaCompressPO);
		
		if (tarDir.exists()) {
			DeleteFileAndDir.deleteDirectory(tarDirPath.toString());
		}
		
//        jsonFile.delete();

		return new MediaCompressVO().set(mediaCompressPO);
	}
}
