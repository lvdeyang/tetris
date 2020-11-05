package com.sumavision.tetris.mims.app.media.audio.api.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sumavision.tetris.commons.util.binary.ByteUtil;
import com.sumavision.tetris.mims.app.boss.MediaType;
import com.sumavision.tetris.mims.app.boss.QdBossService;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderQuery;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.folder.exception.FolderNotExistException;
import com.sumavision.tetris.mims.app.folder.exception.UserHasNoPermissionForFolderException;
import com.sumavision.tetris.mims.app.material.exception.OffsetCannotMatchSizeException;
import com.sumavision.tetris.mims.app.media.UploadStatus;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioDAO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioPO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioService;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioTaskVO;
import com.sumavision.tetris.mims.app.media.audio.MediaAudioVO;
import com.sumavision.tetris.mims.app.media.audio.exception.MediaAudioCannotMatchException;
import com.sumavision.tetris.mims.app.media.audio.exception.MediaAudioErrorBeginOffsetException;
import com.sumavision.tetris.mims.app.media.audio.exception.MediaAudioNotExistException;
import com.sumavision.tetris.mims.app.media.audio.exception.MediaAudioStatusErrorWhenUploadingException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.JSONHttpServletRequestWrapper;
import com.sumavision.tetris.mvc.wrapper.MultipartHttpServletRequestWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;
import com.sun.mail.handlers.image_gif;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;

@Controller
@RequestMapping(value = "/api/server/media/audio")
public class ApiServerMediaAudioController {
	@Autowired
	private FolderQuery folderQuery;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private UserQuery userQuery;
	
	@Autowired
	private MediaAudioService mediaAudioService;
	
	@Autowired
	private MediaAudioDAO mediaAudioDao;
	
	@Autowired
	private QdBossService bossService;
	
	/**
	 * 添加上传音频媒资任务<br/>
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
			HttpServletRequest request) throws Exception{
		
		MediaAudioTaskVO taskParam = JSON.parseObject(task, MediaAudioTaskVO.class);
		
		UserVO user = userQuery.current();
		
		FolderPO folder = folderDao.findCompanyRootFolderByType(user.getGroupId(), FolderType.COMPANY_AUDIO.toString());
		
		if (folder == null) {
			throw new FolderNotExistException(1l);
		}
		
		List<String> tagList = new ArrayList<String>();
		if (tags != null && !tags.isEmpty()) {
			tagList = Arrays.asList(tags.split(","));
		}
		
		MediaAudioPO entity = mediaAudioService.addTask(user, name, tagList, null, "", false, taskParam, folder);
		
		return new MediaAudioVO().set(entity);
		
	}
	
	/**
	 * 音频媒资上传<br/>
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
	 * @return MediaAudioVO 音频媒资
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
		
		MediaAudioPO task = mediaAudioDao.findByUuid(uuid);
		
		if(task == null){
			throw new MediaAudioNotExistException(uuid);
		}
		
		UserVO user = userQuery.current();
		
		if(!folderQuery.hasGroupPermission(user.getGroupId(), task.getFolderId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		//状态错误
		if(!UploadStatus.UPLOADING.equals(task.getUploadStatus())){
			throw new MediaAudioStatusErrorWhenUploadingException(uuid, task.getUploadStatus());
		}
		
		//文件不是一个
		if(!name.equals(task.getFileName()) 
				|| lastModified!=task.getLastModified() 
				|| size!=task.getSize() 
				|| !type.equals(task.getMimetype())){
			throw new MediaAudioCannotMatchException(uuid, name, lastModified, size, type, task.getFileName(), 
											   task.getLastModified(), task.getSize(), task.getMimetype());
		}
		
		//文件起始位置错误
		File file = new File(task.getUploadTmpPath());
		if((!file.exists() && beginOffset!=0l) 
				|| (file.length() != beginOffset)){
			throw new MediaAudioErrorBeginOffsetException(uuid, beginOffset, file.length());
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
			MultimediaInfo multimediaInfo = new Encoder().getInfo(file);
			task.setDuration(multimediaInfo.getDuration());
			mediaAudioDao.save(task);
			mediaAudioService.checkMediaEdit(task);
		}
		
        return new MediaAudioVO().set(task);
	}
	
	/**
	 * 添加音频媒资(远程音频媒资)<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2019年8月6日 下午1:44:06
	 * @param String name 媒资名称
	 * @param String httpUrl 媒资预览地址
	 * @param String ftpUrl 媒资存储ftp路径
	 * @return MediaAudioVO 音频媒资信息
	 * @throws Exception 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/task/add/remote")
	public Object addFar(String name, String httpUrl, String ftpUrl, HttpServletRequest request) throws Exception{
		UserVO user = userQuery.current();
		
		return mediaAudioService.addTask(user, name, "", httpUrl, ftpUrl);
	}
	
	
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/set/price")
	public Object setPrice(HttpServletRequest request) throws Exception{
		JSONHttpServletRequestWrapper wrapper = new JSONHttpServletRequestWrapper(request);
		JSONArray medias = wrapper.getJSONArray("list");
		return bossService.setMediaPrice(medias);
	}
	/**
	 * 开启同步计费<br/>
	 * <b>作者:</b>lqxuhv<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年10月15日 下午3:02:55
	 * @param String charged “0”表示不启用计费，“1”表示启用计费。初始状态为不启用计费。
	 * @return code 0：成功，其它：失败
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/product/charge")
	public Object productCharge(HttpServletRequest request) throws Exception{
		JSONHttpServletRequestWrapper wrapper = new JSONHttpServletRequestWrapper(request);
		String charged = wrapper.getString("charged");
		return bossService.productCharge(charged);
	}
}
