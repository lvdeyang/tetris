package com.sumavision.tetris.mims.app.material;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.sumavision.tetris.commons.util.binary.ByteUtil;
import com.sumavision.tetris.commons.util.file.FileUtil;
import com.sumavision.tetris.commons.util.wrapper.ArrayListWrapper;
import com.sumavision.tetris.commons.util.wrapper.HashMapWrapper;
import com.sumavision.tetris.mims.app.folder.FolderBreadCrumbVO;
import com.sumavision.tetris.mims.app.folder.FolderDAO;
import com.sumavision.tetris.mims.app.folder.FolderPO;
import com.sumavision.tetris.mims.app.folder.FolderQuery;
import com.sumavision.tetris.mims.app.folder.FolderType;
import com.sumavision.tetris.mims.app.folder.exception.FolderNotExistException;
import com.sumavision.tetris.mims.app.folder.exception.FolderTypeCannotMatchException;
import com.sumavision.tetris.mims.app.folder.exception.UserHasNoPermissionForFolderException;
import com.sumavision.tetris.mims.app.material.exception.ErrorBeginOffsetException;
import com.sumavision.tetris.mims.app.material.exception.ErrorMaterialTypeException;
import com.sumavision.tetris.mims.app.material.exception.FileCannotMatchException;
import com.sumavision.tetris.mims.app.material.exception.MaterialFileNotExistException;
import com.sumavision.tetris.mims.app.material.exception.MaterialTaskNotExistException;
import com.sumavision.tetris.mims.app.material.exception.OffsetCannotMatchSizeException;
import com.sumavision.tetris.mims.app.material.exception.StatusErrorWhenUploadCancelException;
import com.sumavision.tetris.mims.app.material.exception.StatusErrorWhenUploadErrorException;
import com.sumavision.tetris.mims.app.material.exception.StatusErrorWhenUploadingException;
import com.sumavision.tetris.mims.app.material.exception.UserHasNoPermissionForMaterialException;
import com.sumavision.tetris.mvc.ext.response.json.aop.annotation.JsonBody;
import com.sumavision.tetris.mvc.wrapper.MultipartHttpServletRequestWrapper;
import com.sumavision.tetris.user.UserQuery;
import com.sumavision.tetris.user.UserVO;

/**
 * 素材库相关接口<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月22日 下午2:00:08
 */
@Controller
@RequestMapping(value = "/material")
public class MaterialController {

	@Autowired
	private UserQuery userTool;
	
	@Autowired
	private FolderQuery folderTool;
	
	@Autowired
	private FolderDAO folderDao;
	
	@Autowired
	private MaterialFileDAO materialFileDao;
	
	@Autowired
	private MaterialFileQuery materialFileTool;
	
	@Autowired
	private MaterialFileService materialFileService;
	
	/**
	 * 加载文件夹下列表数据<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月22日 下午2:00:24
	 * @param @PathVariable Long folderId 当前文件夹
	 * @return FolderVO breadCrumb 文件夹面包屑数据
	 * @return MaterialVO[] rows 列表数据
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/load/{folderId}")
	public Object load(@PathVariable Long folderId, HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		//获取当前文件夹
		if(!folderTool.hasPermission(user.getUuid(), folderId)){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		FolderPO current = folderDao.findOne(folderId);
		
		//获取当前文件夹的所有父目录
		List<FolderPO> parentFolders = folderTool.getParentFolders(current);
		
		if(parentFolders==null || parentFolders.size()<=0){
			parentFolders = new ArrayList<FolderPO>();
		}
		parentFolders.add(current);
		
		//生成面包屑数据
		FolderBreadCrumbVO folderBreadCrumb = folderTool.generateFolderBreadCrumb(parentFolders);
		
		//获取当前文件夹的所有子文件夹
		List<FolderPO> subFolders = folderDao.findByParentIdOrderByNameAsc(current.getId());
		
		//获取当前文件夹下所有的素材
		List<MaterialFilePO> materials = materialFileTool.findMaterialsByFolderId(current.getId());
		
		int size = 0;
		if(subFolders!=null && subFolders.size()>0) size += subFolders.size();
		if(materials!=null && materials.size()>0) size += materials.size();
		
		MaterialVO[] rows = new MaterialVO[size];
		if(subFolders!=null && subFolders.size()>0){
			for(int i=0; i<subFolders.size(); i++){
				rows[i] = new MaterialVO().set(subFolders.get(i));
			}
		}
		
		if(materials!=null && materials.size()>0){
			int begin = 0;
			if(subFolders!=null && subFolders.size()>0) begin = subFolders.size();
			for(int i=0; i<materials.size(); i++){
				rows[begin + i] = new MaterialVO().set(materials.get(i));
			}
		}
		
		Map<String, Object> data = new HashMapWrapper<String, Object>().put("breadCrumb", folderBreadCrumb)
																	   .put("rows", rows)
																	   .getMap();
		return data;
	}
	
	/**
	 * 添加上传任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月29日 下午1:44:06
	 * @param tasks[{name:文件名称, size:文件大小, mimetype:文件mime类型, lastModified:最后更新时间}]
	 * @param Long folerId 文件夹id		
	 * @return List<MaterialFileTaskVO> 任务列表 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/tasks/add")
	public Object addTasks(
			String tasks, 
			Long folderId, 
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		List<MaterialFileTaskVO> taskParams = JSON.parseArray(tasks, MaterialFileTaskVO.class);
		
		UserVO user = userTool.current();
		
		if(!folderTool.hasPermission(user.getUuid(), folderId)){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		FolderPO folder = folderDao.findOne(folderId);
		if(folder == null){
			throw new FolderNotExistException(folderId);
		}
		
		if(!FolderType.PERSONAL.equals(folder.getType())){
			throw new FolderTypeCannotMatchException(FolderTypeCannotMatchException.PERSONAL);
		}
		
		List<MaterialFilePO> entities = materialFileService.addTasks(user, taskParams, folder);
		
		List<MaterialFileTaskVO> view_tasks = MaterialFileTaskVO.getConverter(MaterialFileTaskVO.class).convert(entities, MaterialFileTaskVO.class);
		
		return view_tasks;
	}
	
	/**
	 * 查询素材上传任务<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年11月30日 上午10:10:56
	 * @return List<MaterialFileTaskVO> 任务列表 
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/query/tasks")
	public Object queryTasks(HttpServletRequest request) throws Exception{
		
		UserVO user = userTool.current();
		
		Set<Long> folderIds = new HashSet<Long>();
		
		//获取素材库根目录
		FolderPO folder = folderDao.findMaterialRootByUserId(user.getUuid());
		folderIds.add(folder.getId());
		
		List<FolderPO> subFolders = folderTool.findSubFolders(folder.getId());
		if(subFolders!=null && subFolders.size()>0){
			for(FolderPO subFolder:subFolders){
				folderIds.add(subFolder.getId());
			}
		}
		
		List<MaterialFilePO> tasks = materialFileTool.findTasksByFolderIds(folderIds);
		
		List<MaterialFileTaskVO> view_tasks = new ArrayList<MaterialFileTaskVO>();
		
		if(tasks!=null && tasks.size()>0){
			for(MaterialFilePO task:tasks){
				MaterialFileTaskVO view_task = new MaterialFileTaskVO().set(task);
				File file = new File(task.getTmpPath());
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
	 * @return MaterialVO 素材文件
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
		if((endOffset - beginOffset) != blockSize){
			throw new OffsetCannotMatchSizeException(beginOffset, endOffset, blockSize);
		}
		
		MaterialFilePO task = materialFileDao.findByUuid(uuid);
		
		if(task == null){
			throw new MaterialTaskNotExistException(uuid);
		}
		
		UserVO user = userTool.current();
		
		if(!folderTool.hasPermission(user.getUuid(), task.getFolderId())){
			throw new UserHasNoPermissionForMaterialException(user.getUuid(), task.getFolderId());
		}
		
		//状态错误
		if(!MaterialFileUploadStatus.UPLOADING.equals(task.getUploadStatus())){
			throw new StatusErrorWhenUploadingException(uuid, task.getUploadStatus());
		}
		
		//文件不是一个
		if(!name.equals(task.getName()) 
				|| lastModified!=task.getLastModified() 
				|| size!=task.getSize() 
				|| !type.equals(task.getMimetype())){
			throw new FileCannotMatchException(uuid, name, lastModified, size, type, task.getName(), 
											   task.getLastModified(), task.getSize(), task.getMimetype());
		}
		
		//文件起始位置错误
		File file = new File(task.getTmpPath());
		if((!file.exists() && beginOffset!=0l) 
				|| (file.length() != beginOffset)){
			throw new ErrorBeginOffsetException(uuid, beginOffset, file.length());
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
			task.setUploadStatus(MaterialFileUploadStatus.COMPLETE);
			materialFileDao.save(task);
		}
		
        return new MaterialVO().set(task);
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
		
		MaterialFilePO task = materialFileDao.findByUuid(uuid);
		
		if(task == null){
			throw new MaterialTaskNotExistException(uuid);
		}
		
		UserVO user = userTool.current();
		
		if(!folderTool.hasPermission(user.getUuid(), task.getFolderId())){
			throw new UserHasNoPermissionForMaterialException(user.getUuid(), task.getFolderId());
		}
		
		//状态错误
		if(!MaterialFileUploadStatus.UPLOADING.equals(task.getUploadStatus())){
			throw new StatusErrorWhenUploadErrorException(uuid, task.getUploadStatus());
		}
		
		task.setUploadStatus(MaterialFileUploadStatus.ERROR);
		
		materialFileDao.save(task);
		
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
		
		MaterialFilePO task = materialFileDao.findByUuid(uuid);
		
		if(task == null){
			throw new MaterialTaskNotExistException(uuid);
		}
		
		if(MaterialFileUploadStatus.COMPLETE.equals(task.getUploadStatus())){
			throw new StatusErrorWhenUploadCancelException(uuid, task.getUploadStatus());
		}
		
		UserVO user = userTool.current();
		
		if(!folderTool.hasPermission(user.getUuid(), task.getFolderId())){
			throw new UserHasNoPermissionForMaterialException(user.getUuid(), task.getFolderId());
		}
		
		materialFileService.uploadCancel(task);
		
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
		
		MaterialFilePO task = materialFileDao.findByUuid(uuid);
		
		if(task == null){
			throw new MaterialTaskNotExistException(uuid);
		}
		
		UserVO user = userTool.current();
		
		if(!folderTool.hasPermission(user.getUuid(), task.getFolderId())){
			throw new UserHasNoPermissionForMaterialException(user.getUuid(), task.getFolderId());
		}
		
		return new MaterialFileUploadInfoVO().set(task);
	}
	
	/**
	 * 删除素材文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 上午9:07:53
	 * @param @PathVariable Long id 素材id
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/remove/{id}")
	public Object remove(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		MaterialFilePO material = materialFileDao.findOne(id);
		
		if(material == null){
			throw new MaterialFileNotExistException(id);
		}
		
		UserVO user = userTool.current();
		
		if(!folderTool.hasPermission(user.getUuid(), material.getFolderId())){
			throw new UserHasNoPermissionForMaterialException(user.getUuid(), material.getFolderId());
		}
		
		materialFileService.remove(new ArrayListWrapper<MaterialFilePO>().add(material).getList());
		
		return null;
	}
	
	/**
	 * 移动文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 上午11:33:56
	 * @param Long materialId 素材文件id
	 * @param Long targetId 目标文件夹id
	 * @return boolean 是否移动
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/move")
	public Object move(
			Long materialId,
			Long targetId,
			HttpServletRequest request) throws Exception{
	
		MaterialFilePO material = materialFileDao.findOne(materialId);
		
		if(material == null){
			throw new MaterialFileNotExistException(materialId);
		}
		
		UserVO user = userTool.current();
		
		if(!folderTool.hasPermission(user.getUuid(), material.getFolderId())){
			throw new UserHasNoPermissionForMaterialException(user.getUuid(), material.getFolderId());
		}
		
		FolderPO target = folderDao.findOne(targetId);
		if(target == null){
			throw new FolderNotExistException(targetId);
		}
		
		if(!folderTool.hasPermission(user.getUuid(), target.getId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		if(target.getId().equals(material.getFolderId())) return false;
		
		material.setFolderId(target.getId());
		materialFileDao.save(material);
		
		return true;
	}
	
	/**
	 * 复制素材文件<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月4日 下午2:36:40
	 * @param Long materialId 待复制文件夹id
	 * @param Long targetId 目标文件夹id
	 * @return boolean moved 标识文件是否复制到其他文件夹中
	 * @return MaterialVO copied 复制后的素材文件
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/copy")
	public Object copy(
			Long materialId,
			Long targetId,
			HttpServletRequest request) throws Exception{
		
		MaterialFilePO material = materialFileDao.findOne(materialId);
		
		if(material == null){
			throw new MaterialFileNotExistException(materialId);
		}
		
		UserVO user = userTool.current();
		
		if(!folderTool.hasPermission(user.getUuid(), material.getFolderId())){
			throw new UserHasNoPermissionForMaterialException(user.getUuid(), material.getFolderId());
		}
		
		FolderPO target = folderDao.findOne(targetId);
		if(target == null){
			throw new FolderNotExistException(targetId);
		}
		
		if(!folderTool.hasPermission(user.getUuid(), target.getId())){
			throw new UserHasNoPermissionForFolderException(UserHasNoPermissionForFolderException.CURRENT);
		}
		
		boolean moved = true;
		
		//判断是否被复制到其他文件夹中
		if(target.getId().equals(material.getFolderId())) moved = false;
		
		MaterialFilePO copiedMaterial  = materialFileService.copy(material, target);
		
		Map<String, Object> result = new HashMapWrapper<String, Object>().put("moved", moved)
																		 .put("copied", new MaterialVO().set(copiedMaterial))
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
		
		MaterialFilePO material = materialFileDao.findOne(id);
		
		if(material == null){
			throw new MaterialFileNotExistException(id);
		}
		
		UserVO user = userTool.current();
		
		if(!folderTool.hasPermission(user.getUuid(), material.getFolderId())){
			throw new UserHasNoPermissionForMaterialException(user.getUuid(), material.getFolderId());
		}
		
		Map<String, String> result = new HashMapWrapper<String, String>().put("name", material.getName())
																		 .put("uri", material.getPreviewUrl())
																		 .getMap();
		
		return result;
	}
	
	/**
	 * 获取文本内容<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月5日 下午1:38:05
	 * @param @PathVariable Long id 素材id
	 * @return String 文本内容
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/txt/{id}")
	public Object getTxt(
			@PathVariable Long id,
			HttpServletRequest request) throws Exception{
		
		MaterialFilePO material = materialFileDao.findOne(id);
		
		if(material == null){
			throw new MaterialFileNotExistException(id);
		}
		
		if(!MaterialType.TEXT.equals(material.getType())){
			throw new ErrorMaterialTypeException(id, material.getType(), MaterialType.TEXT);
		}
		
		UserVO user = userTool.current();
		
		if(!folderTool.hasPermission(user.getUuid(), material.getFolderId())){
			throw new UserHasNoPermissionForMaterialException(user.getUuid(), material.getFolderId());
		}
		
		String txt = FileUtil.readAsString(material.getTmpPath());
		
		return txt;
	}
	
	/**
	 * 写入文本内容<br/>
	 * <b>作者:</b>lvdeyang<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2018年12月5日 下午4:17:28
	 * @param @PathVariable Long id 素材id
	 * @param String txt 文本内容
	 */
	@JsonBody
	@ResponseBody
	@RequestMapping(value = "/save/txt/{id}")
	public Object saveTxt(
			@PathVariable Long id,
			String txt,
			HttpServletRequest request) throws Exception{
		
		MaterialFilePO material = materialFileDao.findOne(id);
		
		if(material == null){
			throw new MaterialFileNotExistException(id);
		}
		
		if(!MaterialType.TEXT.equals(material.getType())){
			throw new ErrorMaterialTypeException(id, material.getType(), MaterialType.TEXT);
		}
		
		UserVO user = userTool.current();
		
		if(!folderTool.hasPermission(user.getUuid(), material.getFolderId())){
			throw new UserHasNoPermissionForMaterialException(user.getUuid(), material.getFolderId());
		}
		
		String charset = FileUtil.parseCharset(material.getTmpPath());
		FileUtils.writeStringToFile(new File(material.getTmpPath()), txt, charset);
		
		return null;
	}
	
}
